package yibao.yiwei.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import yibao.yiwei.entity.Warehouseitem;
import yibao.yiwei.service.IBaseService;
import yibao.yiwei.utils.Utils;

/**
 * 入库信息
 * @author Administrator
 *
 */
@SuppressWarnings("unchecked")
@Controller
public class WarehouseitemController {
	
	@Autowired
	private IBaseService baseService;

	/**
	 * 转到入库信息
	 * @param request
	 * @return
	 */
	@RequestMapping("/toWarehouseitem")
	public String toWarehouseitem(HttpServletRequest request) {
		String cusId = request.getParameter("cusId");
		request.setAttribute("cusId", cusId);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();    
		cal.add(Calendar.MONTH, 0);
		cal.set(Calendar.DAY_OF_MONTH,1);
		request.setAttribute("startDate", sdf.format(cal.getTime()));
		request.setAttribute("endDate", sdf.format(Utils.getDate(1)));
		return "/data/warehouseitem";
	}
	
	/**
	 * 入库信息
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ParseException
	 */
	@RequestMapping("/getAllWarehouseitem")
	public void getAllWarehouseitem(HttpServletRequest request, HttpServletResponse response,String startDate,String endDate,String wiCode,String drugCode) throws IOException,ParseException {
		List<Warehouseitem> list = new ArrayList();// 返回的结果集无论有没有条件查询
		String rows = request.getParameter("rows");// 每页显示的记录数
		String page = request.getParameter("page");// 当前第几页
		String cusId = request.getParameter("cusId");// 当前cusId
		int count = 0;
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String hql = "from Warehouseitem where cusId=?0 ";
		String countSql = "select count(WI_ID) from tbl_Warehouseitem where cus_Id=?0 ";
		
		if(null != wiCode && !wiCode.equals("")){
			wiCode = wiCode.trim();
			countSql+=" and WI_CODE = '"+wiCode+"'";
			hql+=" and wiCode = '"+wiCode+"'";
		}
		
		if(null != drugCode && !drugCode.equals("")){
			drugCode = drugCode.trim();
			countSql+=" and (DRUG_CODE = '"+drugCode+"' or DRUG_NAME like '%"+drugCode+"%')";
			hql+=" and (drugCode = '"+drugCode+"' or drugName like '%"+drugCode+"%')";
		}
		Date itemPicktime = null;
		Date itemPicktime2 = null;
		if(null != startDate && !startDate.equals("")){
			startDate = startDate.trim() + " 00:00:00";
		}
		if(null != endDate && !endDate.equals("")){
			endDate = endDate.trim() + " 23:59:59";
		}
		
		if (!startDate.equals("") && !endDate.equals("")) {
			itemPicktime = sf.parse(startDate);
			itemPicktime2 = sf.parse(endDate);
			countSql+=" and WI_DATETIME between ?1 and ?2";
			hql+=" and wiDatetime between ?1 and ?2";// order by wiDatetime desc nulls last";
			count = baseService.findCountSql(countSql,cusId,itemPicktime,itemPicktime2);
			list = baseService.findByPage(hql, rows, page,cusId,itemPicktime,itemPicktime2);
		}
		if (startDate.equals("") && endDate.equals("")) {
			hql+=" order by wiDatetime desc nulls last";
			count = baseService.findCountSql(countSql,cusId);
			list = baseService.findByPage(hql, rows, page,cusId);
		}
		if (startDate.equals("") && !endDate.equals("")) {
			itemPicktime2 = sf.parse(endDate);
			countSql+="and WI_DATETIME <?1";
			hql+="and wiDatetime <?1";// order by wiDatetime desc nulls last";
			count = baseService.findCountSql(countSql,cusId,itemPicktime2);
			list = baseService.findByPage(hql, rows, page,cusId,itemPicktime2);
		}
		if (!startDate.equals("") && endDate.equals("")) {
			itemPicktime = sf.parse(startDate);
			countSql+="and WI_DATETIME >?1";
			hql+="and wiDatetime >?1";// order by wiDatetime desc nulls last";
			count = baseService.findCountSql(countSql,cusId,itemPicktime);
			list = baseService.findByPage(hql, rows, page,cusId,itemPicktime);
		}
		Utils.toBeJson(list, count, response);
	}

	/**
	 * 转入入库核对
	 * @return
	 */
	@RequestMapping("/toCheckWarehouseitem")
	public String toCheckWarehouseitem() {
		return "/warehouseitem/checkwarehouseitem";
	}

	/**
	 * 核对定点单位入库json 每种药品一行（含诊疗项目），显示项目编号、药品本位码、名称、规格剂型等、价格、数量、总金额，可按某字段排序。
	 * @param y ybm  医院编码
	 * @param fDate 日期1
	 * @param sDate 日期2
	 * @param session
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/checkWarehouseitem")
	public Map checkWarehouseitem(String yybm, String fDate, String sDate, HttpSession session, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		List countList = new ArrayList();
		String rows = request.getParameter("rows");// 每页显示的记录数
		String page = request.getParameter("page");// 当前第几页
		sDate = (sDate == null) ? "" : sDate;
		// String areacode = (String) session.getAttribute("acAreacode");
		String countHql = "select t.drugHcscode from Warehouseitem t where t.cusDareway=?0";
		String hql = "select t.drugHcscode,avg(t.wiPrice),sum(t.drugNum),sum(t.wiPrice*t.drugNum)  from Warehouseitem t where t.cusDareway=?0";
		if (sDate.equals("") && (!fDate.equals(""))) {
			countHql += " and t.wiAddtime like to_date('" + fDate + "','yyyy-mm-dd') ";
			hql += " and t.wiAddtime like to_date('" + fDate + "','yyyy-mm-dd') ";
		} else if ((!fDate.equals("")) && (!sDate.equals(""))) {
			countHql += " and t.wiAddtime  between  to_date('" + fDate + "','yyyy-mm-dd')  and to_date('" + sDate + "','yyyy-mm-dd')";
			hql += " and t.wiAddtime  between  to_date('" + fDate + "','yyyy-mm-dd')  and to_date('" + sDate + "','yyyy-mm-dd')";
		} else {
			countHql += " and t.wiAddtime like sysdate ";
			hql += " and t.wiAddtime like sysdate ";
		}
		countHql += " group by t.drugHcscode order by t.drugHcscode";
		hql += " group by t.drugHcscode order by t.drugHcscode";
		// 按本位码分组查询
		List<String> bwmList = baseService.find(countHql, yybm);
		int total = bwmList.size();
		countList = baseService.findByPage(hql, rows, page, yybm);
		List list = new ArrayList();
		if (total > 0) {
			for (int i = 0; i < countList.size(); i++) {
				List temList = new ArrayList();
				Object[] arr1 = (Object[]) countList.get(i);
				String bwm = (String) arr1[0];
				//按照当前库存信息表中的医院编码本位码查找对应的项目编码表中( 项目编号、名称、规格 ,剂型)
				hql = "select d.dcCode,d.dcCommercial,d.dcSpecification,d.dcDosageform from Drugcatalog d where d.cusDareway=?0 and d.dcHcscode=?1";
				List tem = baseService.find(hql, yybm, bwm);
				for (int j = 0; j < arr1.length; j++) {
					temList.add(arr1[j]);
				}
				if (tem.size() > 0) {
					Object[] arr2 = (Object[]) tem.get(0);
					for (int j = 0; j < arr2.length; j++) {
						temList.add(arr2[j]);
					}
				}
				list.add(i, temList);
			}
		}
		map.put("total", total);
		map.put("rows", list);
		return map;

	}

}
