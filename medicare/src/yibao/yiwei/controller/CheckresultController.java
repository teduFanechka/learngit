package yibao.yiwei.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import yibao.yiwei.entity.Checkresult;
import yibao.yiwei.service.IBaseService;
import yibao.yiwei.utils.Utils;

/**
 * 检查、检验结果
 * 
 * @author Administrator
 * 
 */
@Controller
public class CheckresultController {
	
	@Autowired
	private IBaseService<Checkresult> baseService;
	
	/**
	 * 转到检查、检验结果
	 * @param cusId
	 * @param request
	 * @return
	 */
	@RequestMapping("/toCheckresult")
	public String toCheckresult(String cusId, HttpServletRequest request) {
		request.setAttribute("cusId", cusId);
		return "/data/checkresult";
	}

	/**
	 * 检查、检验结果
	 * @param cusId
	 * @param request
	 * @param response
	 * @param crCheckno
	 * @param crHospno
	 * @param crPtsname
	 * @param crItemcode
	 * @throws IOException
	 */
	@RequestMapping("/getAllCheckresult")
	public void getAllCheckresult(String cusId, HttpServletRequest request, HttpServletResponse response, String crCheckno, String crHospno,String crPtsname,String crItemcode) throws IOException {
		String rows = request.getParameter("rows");
		String page = request.getParameter("page");
		List<Checkresult> list = new ArrayList<Checkresult>();
		String hql = "from Checkresult where cusId=?0 ";
		String couHql = "select count(cr_Id) from tbl_Checkresult where cus_Id=?0 ";
		if(null != crCheckno && !crCheckno.equals("")){
			crCheckno = crCheckno.trim();
			couHql+=" and CR_CHECKNO = '"+crCheckno+"'";
			hql+=" and crCheckno = '"+crCheckno+"'";
		}
		if(null != crHospno && !crHospno.equals("")){
			crHospno = crHospno.trim();
			couHql+=" and CR_HOSPNO = '"+crHospno+"'";
			hql+=" and crHospno = '"+crHospno+"'";
		}
		if(null != crPtsname && !crPtsname.equals("")){
			crPtsname = crPtsname.trim();
			couHql+=" and CR_PTSNAME like '%"+crPtsname+"%'";
			hql+=" and crPtsname like '%"+crPtsname+"%'";
		}		
		if(null != crItemcode && !crItemcode.equals("")){
			crItemcode = crItemcode.trim();
			couHql+=" and (CR_ITEMCODE = '"+crItemcode+"' or CR_ITEMNAME like '%"+crItemcode+"%')";
			hql+=" and (crItemcode = '"+crItemcode+"' or crItemname like '%"+crItemcode+"%')";
		}
		hql+=" order by crAddtime desc nulls last";
		int total = baseService.findCountSql(couHql, cusId);
		if (total > 0) {
			list = baseService.findByPage(hql, rows, page, cusId);
		}
		Utils.toBeJson(list, total, response);
	}
}
