package yibao.yiwei.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import yibao.yiwei.entity.Dischargediag;
import yibao.yiwei.entity.Medrecords;
import yibao.yiwei.entity.Operation;
import yibao.yiwei.entity.system.Page;
import yibao.yiwei.service.IBaseService;

/**
 * 病案首页(概要)
 * @author Administrator
 *
 */
@SuppressWarnings("unchecked")
@Controller
public class MedrecordsController {
	
	@Autowired
	private IBaseService baseService;
	
	/**
	 * 转到病案首页(概要)
	 * @param cusId
	 * @param merCaseno
	 * @param merPtsname
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping("/toMedrecords")
	public String getMedrecords(String cusId,String merCaseno,String merPtsname, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		String hql = "from Medrecords where cusId=?0 ";
		String countSql = "select count(mer_Id) from tbl_Medrecords where cus_Id=?0 ";
		String url = request.getRequestURI() + "?cusId=" + cusId;
		if(null != merCaseno && !merCaseno.equals("")){
			hql+=" and (merCaseno = '"+merCaseno+"' or merHospno = '"+merCaseno+"')";
			countSql+=" and (MER_CASENO = '"+merCaseno+"' or MER_HOSPNO = '"+merCaseno+"')";
			url+="&merCaseno="+merCaseno;
		}
		if(null != merPtsname && !merPtsname.equals("")){
			merPtsname = merPtsname.trim();
			hql+=" and merPtsname like '%"+merPtsname+"%'";
			countSql+=" and MER_PTSNAME like '%"+merPtsname+"%'";
			url+="&merPtsname="+merPtsname;
		}
		hql+=" order by merAddtime desc";
		String currentPage = request.getParameter("pagenum");
		Page page;
		if (null == currentPage) {
			page = baseService.queryForPage(countSql, hql, url, 1, cusId);
		} else {
			page = baseService.queryForPage(countSql, hql, url, Integer.parseInt(currentPage), cusId);
		}
		List<Medrecords> list = page.getList();
		if (list.size() > 0) {
			String caseNo = list.get(0).getMerCaseno();
			hql = "from Dischargediag where cusId=?0 and ddCaseno=?1 order by ddAddtime desc";
			List<Dischargediag> disList = baseService.find(hql, cusId, caseNo);//病案首页(出院诊断)
			hql = "from Operation where cusId=?0 and opCaseno=?1 order by opAddtime desc";
			List<Operation> opList = baseService.find(hql, cusId, caseNo);//病案首页(手术操作)
			request.setAttribute("disList", disList);
			request.setAttribute("oplist", opList);
			request.setAttribute("page", page);
		}
		request.setAttribute("cusId", cusId);
		request.setAttribute("merCaseno", merCaseno);
		request.setAttribute("merPtsname", merPtsname);
		return "/data/medrecords";
	}
}
