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

import yibao.yiwei.entity.Deliveryitem;
import yibao.yiwei.service.IBaseService;
import yibao.yiwei.utils.Utils;

@Controller
public class DeliveryitemController {
	
	@Autowired
	private IBaseService<Deliveryitem> baseService;
	
	/**
	 * 转到出库信息
	 * @param request
	 * @return
	 */
	@RequestMapping("/toDeliveryitem")
	public String toDeliveryitem(HttpServletRequest request) {
		String cusId = request.getParameter("cusId");
		request.setAttribute("cusId", cusId);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();    
		cal.add(Calendar.MONTH, 0);
		cal.set(Calendar.DAY_OF_MONTH,1);
		request.setAttribute("startDate", sdf.format(cal.getTime()));
		request.setAttribute("endDate", sdf.format(Utils.getDate(1)));
		return "/data/deliveryitem";
	}
	
	/**
	 * 出库信息
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ParseException
	 */
	@RequestMapping("/getAllDeliveryitem")
	public void getAllDeliveryitem(HttpServletRequest request, HttpServletResponse response,String startDate,String endDate,String diNo,String drugCode) throws IOException,ParseException {
		List<Deliveryitem> list = new ArrayList<Deliveryitem>();// 返回的结果集无论有没有条件查询
		String rows = request.getParameter("rows");// 每页显示的记录数
		String page = request.getParameter("page");// 当前第几页
		String cusId = request.getParameter("cusId");// 当前cusId
		int count = 0;
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String countSql = "select count(DI_ID) from tbl_Deliveryitem where cus_Id=?0 ";
		String hql = "from Deliveryitem d where d.cusId=?0 ";
		if(null != diNo && !diNo.equals("")){
			diNo = diNo.trim();
			countSql+=" and DI_NO = '"+diNo+"'";
			hql+=" and diNo = '"+diNo+"'";
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
			countSql+=" and DI_OPDATETIME between ?1 and ?2";
			hql+=" and diOpdatetime between ?1 and ?2 order by diOpdatetime desc nulls last";
			count = baseService.findCountSql(countSql,cusId,itemPicktime,itemPicktime2);
			list = baseService.findByPage(hql, rows, page,cusId,itemPicktime,itemPicktime2);
		}
		if (startDate.equals("") && endDate.equals("")) {
			hql+=" order by diOpdatetime desc nulls last";
			count = baseService.findCountSql(countSql,cusId);
			list = baseService.findByPage(hql, rows, page,cusId);
		}
		if (startDate.equals("") && !endDate.equals("")) {
			itemPicktime2 = sf.parse(endDate);
			countSql+="and DI_OPDATETIME <?1";
			hql+="and diOpdatetime <?1 order by diOpdatetime desc nulls last";
			count = baseService.findCountSql(countSql,cusId,itemPicktime2);
			list = baseService.findByPage(hql, rows, page,cusId,itemPicktime2);
		}
		if (!startDate.equals("") && endDate.equals("")) {
			itemPicktime = sf.parse(startDate);
			countSql+="and DI_OPDATETIME >?1";
			hql+="and diOpdatetime >?1 order by diOpdatetime desc nulls last";
			count = baseService.findCountSql(countSql,cusId,itemPicktime);
			list = baseService.findByPage(hql, rows, page,cusId,itemPicktime);
		}
		Utils.toBeJson(list, count, response);
	}

}
