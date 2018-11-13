package yibao.yiwei.controller.count;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import yibao.yiwei.entity.system.Customer;
import yibao.yiwei.service.IBaseService;
import yibao.yiwei.service.ICustomerManager;
import yibao.yiwei.service.ISalesitemManager;
import yibao.yiwei.utils.Utils;

@SuppressWarnings("unchecked")
@Controller
public class SalesitemCountController {
	
	@Autowired
	private IBaseService baseService;

	@Resource(name = "salesitemManager")
	private ISalesitemManager salesitemManager;
	
	// 注入客户端记录表service
	@Resource(name = "customerManager")
	private ICustomerManager customerManager;

	// 请求各定点销售总额统计折线图
	@RequestMapping("/toEachPointTotalChart")
	public String toEachPointTotalChart(HttpServletRequest request, HttpSession session) {
		String cusFlag = request.getParameter("cusFlag");
		request.setAttribute("cusFlag", cusFlag);
		return "/salesitem/eachPointTotalChart";
	}

	/**
	 * 1. 获取各定点销售总额统计折线图json 数据量大,获取的是采集日期的数据(数量*单价)*15天
	 * 
	 * @param request
	 * @param response
	 * @param session
	 *            权限控制
	 * @throws ParseException
	 * @throws IOException
	 */
	@RequestMapping("/getEachPointTotalChartJson")
	public void getEachPointTotalChartJson(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ParseException, IOException {
		// 定义查询日期
		String firstDate = request.getParameter("firstDate");
		String secondDate = request.getParameter("secondDate");
		String cusFlag = request.getParameter("cusFlag");
		String hql = "";
		List cusIds = new ArrayList();
		hql = "select c.cusId from Customer c where c.cusFlag=?0 and c.cusStatus=1 and rownum <=10  order by c.cusRegdate desc ";
		// 根据客户标识获取十个注册客户cusId
		cusIds = baseService.find(hql, cusFlag);
		// 根据药品入库统计ids 查询对应的药店名称集合
		String idHql = "select c.cusName from Customer c where c.cusId=?0 ";
		List cusNames = customerManager.getCusNames(idHql, cusIds);
		String startDate = "";// highcharts x轴/起始日期Str
		List totals;
		// 初始化查询15天的各药品销售量
		if (firstDate == null && secondDate == null) {
			totals = salesitemManager.getEachPointTotal(cusIds, 15, null);
		} else {
			// 获取查询条件相差几天
			int count = Utils.daysBetween(firstDate, secondDate);
			if (count > 0) {
				startDate = secondDate;
			} else {
				// 获取绝对值
				count = Math.abs(count);
				startDate = firstDate;
			}
			totals = salesitemManager.getEachPointTotal(cusIds, count, startDate);
		}
		JSONObject json = new JSONObject();
		json.accumulate("cusNames", cusNames);// 客户端名称
		json.accumulate("startDate", startDate);
		json.accumulate("totals", totals);// total代表一共有多少数据
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write(json.toString());// 转化为JSOn格式
	}

	/**
	 * 请求各定点各药品销售金额统计图 客户列表 视图
	 * 
	 * @param request
	 * @param session
	 * @param session
	 *            权限控制
	 */
	@RequestMapping("/toListEachDrugTotalChart")
	public String toListEachDrugTotalChart(HttpServletRequest request, HttpSession session) {
		String acAreacode = (String) session.getAttribute("acAreacode");
		String cusFlag = request.getParameter("cusFlag");
		// 根据客户标识获取所有客户名称&cusId 集合
		List list = customerManager.getAllCusName(cusFlag, acAreacode);
		if (list.size() > 0) {
			request.setAttribute("list", list);
			return "/salesitem/listEachDrugTotalChart";
		} else {
			request.setAttribute("status", "未查询到数据!");
			return "/public/null";
		}
	}

	// 请求各定点各药品销售金额统计图
	@RequestMapping("/toEachDrugTotalChart")
	public String toEachDrugTotalChart(HttpServletRequest request) throws UnsupportedEncodingException {
		// 获取定点cusid,定点名称
		String cusId = request.getParameter("cusId");
		// 根据cusid 查询药店名称
		Customer customer = (Customer)baseService.get(Customer.class, cusId);
		String cusName = customer.getCusName();
		// 或者 String cusName = request.getParameter("cusName");
		// cusName=URLDecoder.decode(cusName,"UTF-8");
		request.setAttribute("cusId", cusId);
		request.setAttribute("cusName", cusName);
		return "/salesitem/eachDrugTotalChart";
	}

	// 获取各定点各药品销售金额统计图
	@RequestMapping("/getEachDrugTotalChartJson")
	public void getEachDrugTotalChartJson(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
		// 定义查询日期
		String firstDate = request.getParameter("firstDate");
		String secondDate = request.getParameter("secondDate");
		String cusId = request.getParameter("cusId");
		// 根据cusId获取对应的药品编码(销售明细表Salesitem)集合
		List drugCodes;
		List totals = new ArrayList();
		String startDate = "";// highcharts x轴/起始日期Str
		// 初始化查询15天的各药品销售量
		if (firstDate == null && secondDate == null) {
			drugCodes = salesitemManager.getDrugCodes(cusId, null, null);
			if (drugCodes.size() > 0) {
				totals = salesitemManager.getEachDrugTotal(cusId, drugCodes, 15, null);
			}
		} else {
			// 获取查询条件相差几天
			int count = Utils.daysBetween(firstDate, secondDate);
			if (count > 0) {
				startDate = secondDate;
				drugCodes = salesitemManager.getDrugCodes(cusId, firstDate, secondDate);
			} else {
				// 获取绝对值
				count = Math.abs(count);
				startDate = firstDate;
				// 如果 负值把小的时间secondDate做为第一个参数
				drugCodes = salesitemManager.getDrugCodes(cusId, secondDate, firstDate);
			}
			if (drugCodes.size() > 0) {
				totals = salesitemManager.getEachDrugTotal(cusId, drugCodes, count, startDate);
			}
		}
		// 根据药品编码drugCodes 查询对应的药品名称集合
		List names = salesitemManager.getAllDrugNames(cusId, drugCodes);
		JSONObject json = new JSONObject();
		json.accumulate("dcCommercials", names);// 药品商用名
		json.accumulate("startDate", startDate);
		json.accumulate("totals", totals);// total代表一共有多少数据
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write(json.toString());// 转化为JSOn格式
	}

	// 请求各定点各药品销售 量统计图 客户列表 视图
	@RequestMapping("/toListEachDrugTotalNumChart")
	public String toListEachDrugTotalNumChart(HttpServletRequest request, HttpSession session) {
		// 获取session中的组织机构id
		String acAreacode = (String) session.getAttribute("acAreacode");
		String cusFlag = request.getParameter("cusFlag");
		// 根据客户标识获取所有客户名称&cusId 集合
		List list = customerManager.getAllCusName(cusFlag, acAreacode);
		if (list.size() > 0) {
			request.setAttribute("list", list);
			return "/salesitem/listEachDrugTotalNumChart";
		} else {
			request.setAttribute("status", "未查询到数据!");
			return "/public/null";
		}
	}

	// 请求各定点各药品销售 量统计图
	@RequestMapping("/toEachDrugTotalNumChart")
	public String toEachDrugTotalNumChart(HttpServletRequest request) throws UnsupportedEncodingException {
		// 获取定点cusid,定点名称
		String cusId = request.getParameter("cusId");
		// 根据cusid 查询药店名称
		Customer customer = (Customer)baseService.get(Customer.class, cusId);
		String cusName = customer.getCusName();
		// 或者 String cusName = request.getParameter("cusName");
		// listEachDrugTotalNumChart.jsp ghref="${path
		// }/toEachDrugTotalChart?cusId=${list[1]}&cusName=${list[0]}
		// cusName=URLDecoder.decode(cusName,"UTF-8");
		request.setAttribute("cusId", cusId);
		request.setAttribute("cusName", cusName);
		return "/salesitem/eachDrugTotalNumChart";
	}

	// 获取各定点各药品销售 量统计图
	@RequestMapping("/getEachDrugTotalNumChartJson")
	public void getEachDrugTotalNumChartJson(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
		// 定义查询日期
		String firstDate = request.getParameter("firstDate");
		String secondDate = request.getParameter("secondDate");
		String cusId = request.getParameter("cusId");
		// 根据cusId获取对应的药品编码(销售明细表Salesitem)集合
		List drugCodes;
		String startDate = "";// highcharts x轴/起始日期Str
		List totals = new ArrayList();
		// 初始化查询15天的各药品销售量
		if (firstDate == null && secondDate == null) {
			drugCodes = salesitemManager.getDrugCodes(cusId, null, null);
			if (drugCodes.size() > 0) {
				totals = salesitemManager.getEachDrugNumTotal(cusId, drugCodes, 15, null);
			}
		} else {
			// 获取查询条件相差几天
			int count = Utils.daysBetween(firstDate, secondDate);
			if (count > 0) {
				startDate = secondDate;
				drugCodes = salesitemManager.getDrugCodes(cusId, firstDate, secondDate);
			} else {
				// 获取绝对值
				count = Math.abs(count);
				startDate = firstDate;
				// 如果 负值把小的时间secondDate做为第一个参数
				drugCodes = salesitemManager.getDrugCodes(cusId, secondDate, firstDate);
			}
			if (drugCodes.size() > 0) {
				totals = salesitemManager.getEachDrugNumTotal(cusId, drugCodes, count, startDate);
			}
		}
		// 根据药品编码drugCodes 查询对应的药品名称(Drugcatalog药品目录表)集合
		List names = salesitemManager.getAllDrugNames(cusId, drugCodes);
		JSONObject json = new JSONObject();
		json.accumulate("dcCommercials", names);// 药品商用名
		json.accumulate("startDate", startDate);
		json.accumulate("totals", totals);// total代表一共有多少数据
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write(json.toString());// 转化为JSOn格式

	}
}
