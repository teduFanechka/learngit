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

import yibao.yiwei.entity.Discharged;
import yibao.yiwei.service.IBaseService;
import yibao.yiwei.utils.Utils;

/**
 * 出院信息
 * @author Administrator
 *
 */
@Controller
public class DischargedController {
	
	@Autowired
	private IBaseService<Discharged> baseService;

	/**
	 * 转到出院记录
	 * @param cusId
	 * @param request
	 * @return
	 */
	@RequestMapping("toDischarged")
	public String toDischarged(String cusId, HttpServletRequest request) {
		request.setAttribute("cusId", cusId);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();    
		cal.add(Calendar.MONTH, 0);
		cal.set(Calendar.DAY_OF_MONTH,1);
		request.setAttribute("startDate", sdf.format(cal.getTime()));
		request.setAttribute("endDate", sdf.format(Utils.getDate(1)));
		return "/data/discharged";
	}

	/**
	 * 出院记录
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("getAllDischarged")
	public void getAllDischarged(HttpServletRequest request, HttpServletResponse response,String startDate,String endDate,String hospNo,String siPtsname) throws IOException ,ParseException{
		List<Discharged> list = new ArrayList<Discharged>();// 返回的结果集无论有没有条件查询
		String rows = request.getParameter("rows");// 每页显示的记录数
		String page = request.getParameter("page");// 当前第几页
		String cusId = request.getParameter("cusId");// 当前cusId
		int count = 0;
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String hql = "from Discharged where cusId=?0 ";
		String countSql = "select count(DC_ID) from tbl_Discharged where cus_Id = ?0 ";
		
		if(null != hospNo && !hospNo.equals("")){
			hospNo = hospNo.trim();
			countSql+=" and HOSP_NO = '"+hospNo+"'";
			hql+=" and hospNo = '"+hospNo+"'";
		}
		
		if(null != siPtsname && !siPtsname.equals("")){
			siPtsname = siPtsname.trim();
			countSql+=" and (SI_PTSNAME like '%"+siPtsname+"%' or DC_DIAGNAME like '%"+siPtsname+"%')";
			hql+=" and (siPtsname like '%"+siPtsname+"%' or dcDiagname like '%"+siPtsname+"%')";
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
			countSql+=" and DC_OUTDATE between ?1 and ?2";
			hql+=" and dcOutdate between ?1 and ?2 order by dcOutdate desc nulls last";
			count = baseService.findCountSql(countSql,cusId,itemPicktime,itemPicktime2);
			list = baseService.findByPage(hql, rows, page,cusId,itemPicktime,itemPicktime2);
		}
		if (startDate.equals("") && endDate.equals("")) {
			hql+=" order by dcOutdate desc nulls last";
			count = baseService.findCountSql(countSql,cusId);
			list = baseService.findByPage(hql, rows, page,cusId);
		}
		if (startDate.equals("") && !endDate.equals("")) {
			itemPicktime2 = sf.parse(endDate);
			countSql+="and DC_OUTDATE <?1";
			hql+="and dcOutdate <?1 order by dcOutdate desc nulls last";
			count = baseService.findCountSql(countSql,cusId,itemPicktime2);
			list = baseService.findByPage(hql, rows, page,cusId,itemPicktime2);
		}
		if (!startDate.equals("") && endDate.equals("")) {
			itemPicktime = sf.parse(startDate);
			countSql+="and DC_OUTDATE >?1";
			hql+="and dcOutdate >?1 order by dcOutdate desc nulls last";
			count = baseService.findCountSql(countSql,cusId,itemPicktime);
			list = baseService.findByPage(hql, rows, page,cusId,itemPicktime);
		}
		Utils.toBeJson(list, count, response);
	}

}
