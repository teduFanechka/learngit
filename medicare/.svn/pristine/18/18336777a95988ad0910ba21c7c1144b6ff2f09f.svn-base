package yibao.yiwei.controller.count;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import yibao.yiwei.service.IBaseService;
import yibao.yiwei.service.ICustomerManager;
import yibao.yiwei.utils.Utils;

/**
 * 药品使用量排序查询(柱状条形图)
 * 
 * @author Administrator
 * 
 */
@SuppressWarnings("unchecked")
@Controller
public class DrugUsedCountController {
	
	@Autowired
	private IBaseService baseService;
	
	@Autowired
	private ICustomerManager customerManager;

	/**
	 * 
	 * @param cusFlag 客户标识
	 * @param request
	 * @return 搜索药品名称jsp
	 * @throws ParseException
	 */
	@RequestMapping("/toListDrugUsedCountChart")
	public String toListDrugUsedCountChart(String cusFlag, HttpServletRequest request) throws ParseException {

		// 根据客户标识查询客户ids
		// String hql = "select c.cusId from Customer c where c.cusFlag=?0";
		// List cusIds = customerManager.getCusIds(hql, cusFlag);
		// 查询所有药品编码集合 *(where XXX in list)
		// String hql1 =
		// "select d.drugName from Drugcatalog d where d.cusId in (?0) group by d.drugName";
		// List drugCodes = drugcatalogManager.getDrugCodes(hql1, cusIds);

		request.setAttribute("cusFlag", cusFlag);
		return "/salesitem/listDrugUsedCountChart";
	}

	/**
	 * 
	 * @param cusFlag
	 *            客户标识
	 * @param drugName
	 *            药品名称
	 * @param request
	 * @return 药品使用量柱形图
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping("/toDrugUsedCountChart")
	public String toDrugUsedCountChart(String cusFlag, String drugName, HttpServletRequest request) throws UnsupportedEncodingException {
		request.setAttribute("cusFlag", cusFlag);
		// request.setAttribute("drugName", drugName);
		request.getSession().setAttribute("drugName", drugName);
		return "/salesitem/drugUsedCountChart";
	}

	/**
	 * 
	 * @param cusFlag
	 *            客户标识
	 * @param drugName
	 *            药品名称
	 * @param request
	 * @param session
	 *            权限控制
	 * @param response
	 *            药品使用量柱形图Json
	 * @throws ParseException
	 * @throws IOException
	 */
	@RequestMapping("/getDrugUsedCountChart")
	public void getDrugUsedCountChart(String cusFlag, String drugName, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ParseException, IOException {
		drugName = (String) request.getSession().getAttribute("drugName");
		// 定义查询日期参数
		String firstDate = request.getParameter("firstDate");
		String secondDate = request.getParameter("secondDate");
		Date smaDate, bigDate;// 定时查询日期Date
		Object[] obj = null;
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String dcHql;
		// 查询销售表中药品名称对应的所有药品编码,cusId
		List arrList = new ArrayList();
		dcHql = "select s.cusId,s.drugCode from Salesitem s where s.drugName=?0 group by s.cusId,s.drugCode";
		arrList = baseService.find(dcHql, drugName);

		Double count = 0.0;// 定义 平均值
		BigDecimal avg = new BigDecimal(0);// 定义格式化平均值
		List totals = new ArrayList();// 定义排序后的销售条数
		List cusNames = new ArrayList();// 定义排序后对应的药店名称
		if (arrList.size() > 0) {
			// 根据客户标识过滤list(包含cusId与dcCode)元素
			arrList = customerManager.getArrayCusIds(arrList, cusFlag);
			// 过虑日期,在当前销售日期区间内有当前cusID 与 drugCode
			List realarrList = new ArrayList();
			if (firstDate == null && secondDate == null) {
				smaDate = Utils.getDate(-7);
				bigDate = Utils.getDate(-1);
			} else {
				smaDate = sf.parse(firstDate);
				bigDate = sf.parse(secondDate);
				Date tem;
				// 如果第二个时间小于第一个时间,则位置互换
				if (bigDate.before(smaDate)) {
					tem = bigDate;
					bigDate = smaDate;
					smaDate = tem;
				}
			}
			for (int i = 0; i < arrList.size(); i++) {
				// 判断是否为数组
				if (arrList.get(i) instanceof Object[]) {
					obj = (Object[]) arrList.get(i);// 获取第i个元素
				}
				String countHql = "select count(s.cusId) from Salesitem s where s.cusId=?0 and s.drugCode=?1 and s.drugPicktime between ?2 and ?3";
				int temp = baseService.findCount(countHql, obj[0], obj[1], smaDate, bigDate);
				if (temp > 0) {
					realarrList.add(obj);
				}
			}
			// 定义药品(药品销售明细表)统计销售量list(无序)
			List disorderTotals = new ArrayList();
			Object total;// 定义临时变量
			for (int i = 0; i < realarrList.size(); i++) {
				String hql2 = "select sum(s.drugNum) from Salesitem s where s.cusId=?0 and s.drugCode=?1 and s.drugPicktime between ?2 and ?3";
				// 根据cusIds集合,药品名称集合查询(药品销售明细表)计算统计销售量
				if (realarrList.get(i) instanceof Object[]) {
					obj = (Object[]) realarrList.get(i);// 获取第i个元素
				}
				total = baseService.find(hql2, obj[0], obj[1], smaDate, bigDate).get(0);
				disorderTotals.add(total);
			}
			//获取平均值.加入平均值项名称
			for (int i = 0; i < disorderTotals.size(); i++) {
				Double tem = (Double) disorderTotals.get(i);// 所有元素相加获取总数量
				count = count + tem;
			}
			if (disorderTotals.size() > 0) {
				double d = disorderTotals.size();
				count = count / d;// 取平均值
				avg = new BigDecimal(count);
				avg = avg.setScale(2, BigDecimal.ROUND_HALF_UP);// 截取小数点后两位
			}

			// 查询药店名称集合
			String idHql = "select c.cusName from Customer c where c.cusId=?0 ";
			// 根据(元素为数组)的realarrList查询药店名称
			List disorderCusNames = customerManager.getArrCusNames(idHql, realarrList);
			// *********************加入平均值项名称 ******************
			// disorderTotals.add(count); 把平均值添加到柱状条形图
			// disorderCusNames.add("所有药店使用量平均值"); 把名称加到柱状图
			// *********************加入平均值项名称 ******************

			// Map map = new TreeMap().descendingMap();倒序
			// 定义treeMap集合用于给上述两个集合进行按value 比较器排序
			Map treeMap = new TreeMap();
			for (int i = 0; i < disorderCusNames.size(); i++) {
				treeMap.put(disorderCusNames.get(i), disorderTotals.get(i));
			}
			// 这里将map.entrySet()转换成list
			List<Map.Entry<String, Double>> list = new ArrayList<Map.Entry<String, Double>>(treeMap.entrySet());
			// 然后通过比较器来实现排序
			Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
				// 降序排序
				public int compare(Entry<String, Double> o1, Entry<String, Double> o2) {
					return o2.getValue().compareTo(o1.getValue());// 前后互换为升序
				}
			});
			int num = 0;
			for (Map.Entry<String, Double> mapping : list) {
				String cusName = mapping.getKey();// 药店名称
				cusNames.add(cusName);
				Double order = (Double) mapping.getValue();// 销售条数
				totals.add(order);
				num++;
				if (num > 10)
					break;// 超过十家定点跳出
			}

		}
		JSONObject json = new JSONObject();
		json.accumulate("cusNames", cusNames);// 药店名称
		json.accumulate("avg", avg.toString());// 平均值
		json.accumulate("totals", totals);// 使用量(销售条数)
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write(json.toString());// 转化为JSOn格式
	}
}
