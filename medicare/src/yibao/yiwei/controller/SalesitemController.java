package yibao.yiwei.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import yibao.yiwei.entity.Salesitem;
import yibao.yiwei.service.IBaseService;
import yibao.yiwei.utils.Utils;

/**
 * 销售信息
 * @author Administrator
 *
 */
@Controller
public class SalesitemController {

	@Autowired
	private IBaseService<Salesitem> baseService;

	/**
	 * 转到销售信息
	 * @param request
	 * @return
	 */
	@RequestMapping("/toSalesitem")
	public String toSalesitem(HttpServletRequest request) {
		String cusId = request.getParameter("cusId");
		request.setAttribute("cusId", cusId);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();    
		cal.add(Calendar.MONTH, 0);
		cal.set(Calendar.DAY_OF_MONTH,1);
		request.setAttribute("startDate", sdf.format(cal.getTime()));
		request.setAttribute("endDate", sdf.format(Utils.getDate(1)));
		return "/data/salesitem";
	}
	
	/**
	 * 销售信息
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ParseException
	 */
	@RequestMapping("/getAllSalesitem")
	public void getAllSalesitem(HttpServletRequest request, HttpServletResponse response,String startDate,String endDate,String soNo,String drugCode,String soSalespsnname) throws IOException,ParseException {
		List<Salesitem> list = new ArrayList<Salesitem>();// 返回的结果集无论有没有条件查询
		String rows = request.getParameter("rows");// 每页显示的记录数
		String page = request.getParameter("page");// 当前第几页
		String cusId = request.getParameter("cusId");
		int count = 0;
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String countSql = "select count(SI_ID) from tbl_Salesitem where cus_Id = ?0 ";
		String hql = "from Salesitem where cusId = ?0 ";
		
		if(null != soNo && !soNo.equals("")){
			soNo = soNo.trim();
			countSql+=" and SO_NO = '"+soNo+"'";
			hql+=" and soNo = '"+soNo+"'";
		}
		
		if(null != drugCode && !drugCode.equals("")){
			drugCode = drugCode.trim();
			countSql+=" and (DRUG_CODE = '"+drugCode+"' or DRUG_NAME like '%"+drugCode+"%')";
			hql+=" and (drugCode = '"+drugCode+"' or drugName like '%"+drugCode+"%')";
		}
		
		if(null != soSalespsnname && !soSalespsnname.equals("")){
			soSalespsnname = soSalespsnname.trim();
			countSql+=" and (SI_SETTLEMENTNAME like '%"+soSalespsnname+"%' or SO_PAYTYPE like '%"+soSalespsnname+"%' or SO_SALESPSNNAME like '%"+soSalespsnname+"%')";
			hql+=" and (siSettlementname like '%"+soSalespsnname+"%' or soPaytype like '%"+soSalespsnname+"%' or soSalespsnname like '%"+soSalespsnname+"%')";
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
			countSql+=" and SO_DATETIME between ?1 and ?2";
			hql+=" and soDatetime between ?1 and ?2 order by soDatetime desc nulls last";
			list = baseService.findByPage(hql, rows, page,cusId,itemPicktime,itemPicktime2);
			count = baseService.findCountSql(countSql,cusId,itemPicktime,itemPicktime2);
		}
		if (startDate.equals("") && endDate.equals("")) {
			hql+=" order by soDatetime desc nulls last";
			list = baseService.findByPage(hql, rows, page,cusId);
			count = baseService.findCountSql(countSql,cusId);
		}
		if (startDate.equals("") && !endDate.equals("")) {
			itemPicktime2 = sf.parse(endDate);
			countSql+="and SO_DATETIME <?1";
			hql+="and soDatetime <?1 order by soDatetime desc nulls last";
			list = baseService.findByPage(hql, rows, page,cusId,itemPicktime2);
			count = baseService.findCountSql(countSql,cusId,itemPicktime2);
		}
		if (!startDate.equals("") && endDate.equals("")) {
			itemPicktime = sf.parse(startDate);
			countSql+="and SO_DATETIME >?1";
			hql+="and soDatetime >?1 order by soDatetime desc nulls last";
			list = baseService.findByPage(hql, rows, page,cusId,itemPicktime);
			count = baseService.findCountSql(countSql,cusId,itemPicktime);
		}
		Utils.toBeJson(list, count, response);
	}

}
