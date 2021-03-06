package yibao.yiwei.controller.count;

import java.io.IOException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import yibao.yiwei.service.IBaseService;
import yibao.yiwei.service.ICouWarehouseitemManager;
import yibao.yiwei.service.ICustomerManager;
import yibao.yiwei.service.IUploadfileManager;
import yibao.yiwei.service.IWarehouseitemManager;
import yibao.yiwei.utils.Utils;

/**
 * 药品入库种类统计,药品入库数量统计饼图
 */
@SuppressWarnings("unchecked")
@Controller
public class WarehouseitemCountController {
	
	@Autowired
	private IBaseService baseService;
	
	@Autowired
	private ICouWarehouseitemManager couWarehouseitemManager;
	
	@Autowired
	private ICustomerManager customerManager;
	
	@Autowired
	private IUploadfileManager uploadfileManager;

	@Autowired
	private IWarehouseitemManager warehouseitemManager;

	// 请求药品入库种类统计折线图
	@RequestMapping("/toWarehouseitemKindChart")
	public String toWarehouseitemChart(HttpServletRequest request) {
		String cusFlag = request.getParameter("cusFlag");
		request.setAttribute("cusFlag", cusFlag);
		return "/warehouseitem/warehouseitemKindChart";
	}

	/**
	 * 获取上传客户ids,日期json,药品入库种类统计 数据
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @param session 权限控制
	 * @throws IOException
	 * @throws ParseException
	 */
	@RequestMapping("/warehouseitemKindChartJson")
	public void warehouseitemChartKindJson(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException, ParseException {
		String acAreacode = (String) session.getAttribute("acAreacode");// 获取权限id
		// 定义查询日期
		String firstDate = request.getParameter("firstDate");
		String secondDate = request.getParameter("secondDate");
		String cusFlag = request.getParameter("cusFlag");
		String hql1;
		List cusIds = new ArrayList();// 查询所有cusid
		if (acAreacode != null) {
			hql1 = "select c.cusId from Customer c,CusareaRelate r where c.cusId=r.cusId and c.cusFlag=?0 and r.acAreacode in("+acAreacode+") and c.cusStatus=1 and rownum <=10  order by c.cusRegdate desc nulls last";
			cusIds = baseService.find(hql1, cusFlag);
			
		}
		// 根据cusflag类型过滤cusids
		// List realCusIds = customerManager.getRealCusIds(cusIds, cusFlag);
		// 根据药品入库统计ids 查询对应的药店名称集合
		String hql = "select c.cusName from Customer c where c.cusId=?0 ";
		List names = customerManager.getCusNames(hql, cusIds);

		String startDate = "";// highcharts x轴/起始日期Str
		List totals = new ArrayList();
		// 初始化查询15天的各药品销售量
		if (firstDate == null && secondDate == null) {
			totals = couWarehouseitemManager.getCWIKindTotals(cusIds, 15, null);
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
			totals = couWarehouseitemManager.getCWIKindTotals(cusIds, count, startDate);
		}

		JSONObject json = new JSONObject();
		json.accumulate("cusNames", names);// 客户名称
		json.accumulate("startDate", startDate);
		json.accumulate("totals", totals);// total代表一共有多少数据
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write(json.toString());// 转化为JSOn格式
	}

	/**
	 * 请求药品入库数量统计饼图
	 * @param request
	 * @param session
	 * @param session 权限控制
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping("/toWarehouseitemAmountChart")
	public String toWarehouseitemAmountChart(HttpServletRequest request, HttpSession session) throws ParseException {
		String cusFlag = request.getParameter("cusFlag");
		request.setAttribute("cusFlag", cusFlag);
		String hql;
		// 查询所有cusid
		hql = "select c.cusId from Customer c where c.cusFlag=?0";
		List cusIds = new ArrayList();// 查询所有cusid
		hql = "select c.cusId from Customer c where c.cusFlag=?0  and c.cusStatus=1";
		cusIds = baseService.find(hql, cusFlag);
		// 根据药品入库统计ids 查询(客户端记录表 customer)对应的定点名称集合
		String hql1 = "select c.cusName from Customer c where c.cusId=?0 ";
		List cusNames = customerManager.getCusNames(hql1, cusIds);
		request.setAttribute("cusNames", cusNames);
		request.setAttribute("status", "notEmpty");
		return "/warehouseitem/warehouseitemAmountChart";
	}

	// 获取上传客户ids,日期json,药品入库数量统计 数据
	@RequestMapping("/warehouseitemAmountChartJson")
	public void warehouseitemChartAmountJson(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException, ParseException {
		String acAreacode = (String) session.getAttribute("acAreacode");
		String cusName = request.getParameter("cusName");
		cusName = URLDecoder.decode(cusName, "utf-8");
		// 获取java前端参数得到要查询的天数 一天/一周/一个月
		String days = request.getParameter("days");
		// 根据日期天数与文件类型102 获得 (文件上传记录表Uploadfile) 当天解析出该类型文件的cusID 集合
		List cusIds = uploadfileManager.getAllcusIds(days, acAreacode);
		// 根据cusflag类型过滤cusids
		if (cusIds.size() > 0) {
			// 根据药品入库统计ids 查询(客户端记录表 customer)对应的定点名称集合
			String hql = "select c.cusName from Customer c where c.cusId=?0 ";
			List cusNames = customerManager.getCusNames(hql, cusIds);
			// 获取cusName (客户端记录表 customer) 对应的cusID
			String cusId = customerManager.getCusId(cusName);
			// 根据cusId 获取(药品目录表 Drugcatalog)对应的商品编码集合
			List drugCodes = warehouseitemManager.getgetDrugCodes(cusId, days);
			// 根据cusId ,药品编码集合获取 药品名称集合
			List drugNames = warehouseitemManager.getAlldrugNames(cusId, drugCodes);
			// 获取(药品入库详细表 Warehouseitem)药品数量统计集合
			List totals = warehouseitemManager.getWIAmountTotals(cusId, days, drugCodes);
			Map map = new HashMap<String, Double>();
			for (int i = 0; i < drugNames.size(); i++) {
				String s = (String) drugNames.get(i);
				Double d = (Double) totals.get(i);
				map.put(s, d);
			}
			JSONObject json = new JSONObject();
			json.accumulate("cusNames", cusNames);// 定点名称
			json.accumulate("map", map);// 统计数据
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().write(json.toString());// 转化为JSOn格式
		}
	}
}
