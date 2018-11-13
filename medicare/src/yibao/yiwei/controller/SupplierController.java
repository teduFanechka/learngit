package yibao.yiwei.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import yibao.yiwei.entity.Supplier;
import yibao.yiwei.service.IBaseService;
import yibao.yiwei.utils.Utils;

/**
 * 供应商/生产商信息
 * @author Administrator
 *
 */
@SuppressWarnings("unchecked")
@Controller
public class SupplierController {
	
	@Autowired
	private IBaseService baseService;
	
	/**
	 * 转到供应商/生产商信息
	 * @param request
	 * @param cusId
	 * @param cusFlag
	 * @return
	 */
	@RequestMapping("/toSupplier")
	public String toSupplier(HttpServletRequest request, String cusId, String cusFlag) {
		request.setAttribute("cusFlag", cusFlag);
		request.setAttribute("cusId", cusId);
		return "/data/supplier";
	}
	
	/**
	 * 供应商/生产商信息
	 * @param request
	 * @param cusFlag
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/getAllSupplier")
	public void getAllSupplier(HttpServletRequest request, String cusFlag, HttpServletResponse response) throws IOException, ParseException {
		List<Supplier> list = new ArrayList<Supplier>();
		String rows = request.getParameter("rows");// 每页显示的记录数
		String page = request.getParameter("page");// 当前第几页
		String cusId = request.getParameter("cusId");
		if (null != cusFlag && cusFlag.equals("102")) {//连锁店，查询总店的生产商/供应商
			String phql = "select cusParentid from Customer where cusId=?0";
			String  parentid = (String)baseService.findUnique(phql, cusId);
			if (null != parentid && parentid.length() > 1) {
				cusId = parentid;
			}
		}
		String countSql = "select count(SP_ID) from tbl_Supplier where cus_Id=?0";
		String hql = "from Supplier where cusId=?0 order by spAddtime desc nulls last";
		int count = baseService.findCountSql(countSql,cusId);
		if(count>0){
			list = baseService.findByPage(hql, rows, page,cusId);
		}
		Utils.toBeJson(list, count, response);
	}

}
