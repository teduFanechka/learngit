package yibao.yiwei.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import yibao.yiwei.entity.Checkdetail;
import yibao.yiwei.service.IBaseService;
import yibao.yiwei.utils.Utils;

/**
 * 检查、检验结果详细
 * @author Administrator
 *
 */
@Controller
public class CheckdetailController {
	
	@Autowired
	private IBaseService<Checkdetail> baseService;
	
	/**
	 * 转到检查、检验结果详细
	 * @param cusId
	 * @param request
	 * @return
	 */
	@RequestMapping("/toCheckdetail")
	public String toCheckdetail(String cusId, HttpServletRequest request) {
		request.setAttribute("cusId", cusId);
		return "/data/checkdetail";
	}

	/**
	 * 检查、检验结果详细
	 * @param cusId
	 * @param request
	 * @param response
	 * @param ctCheckno
	 * @param ctHospno
	 * @param ctItemcode
	 * @throws IOException
	 */
	@RequestMapping("/getAllCheckdetail")
	public void getAllCheckdetail(String cusId, HttpServletRequest request, HttpServletResponse response,String ctCheckno,String ctHospno,String ctItemcode) throws IOException {
		String rows = request.getParameter("rows");
		String page = request.getParameter("page");
		List<Checkdetail> list = new ArrayList<Checkdetail>();
		String couHql = "select count(ct_Id) from tbl_Checkdetail where cus_Id=?0 ";
		String hql = "from Checkdetail where cusId=?0 ";
		if(null != ctCheckno && !ctCheckno.equals("")){
			ctCheckno = ctCheckno.trim();
			couHql+=" and CT_CHECKNO = '"+ctCheckno+"'";
			hql+=" and ctCheckno = '"+ctCheckno+"'";
		}
		if(null != ctHospno && !ctHospno.equals("")){
			ctHospno = ctHospno.trim();
			couHql+=" and CT_HOSPNO = '"+ctHospno+"'";
			hql+=" and ctHospno = '"+ctHospno+"'";
		}
		if(null != ctItemcode && !ctItemcode.equals("")){
			ctItemcode = ctItemcode.trim();
			couHql+=" and (CT_ITEMCODE = '"+ctItemcode+"' or CT_ITEMNAME like '%"+ctItemcode+"%')";
			hql+=" and (ctItemcode = '"+ctItemcode+"' or ctItemname like '%"+ctItemcode+"%')";
		}
		hql+="order by ctAddtime desc nulls last";
		int total = baseService.findCountSql(couHql, cusId);
		if (total > 0) {
			list = baseService.findByPage(hql, rows, page, cusId);
		}
		Utils.toBeJson(list, total, response);
	}
}
