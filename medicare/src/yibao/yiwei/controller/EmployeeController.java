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

import yibao.yiwei.entity.Employee;
import yibao.yiwei.service.IBaseService;
import yibao.yiwei.utils.Utils;

/**
 * 医护人员信息
 * @author Administrator
 *
 */
@Controller
public class EmployeeController {
	
	@Autowired
	private IBaseService<Employee> baseService;

	/**
	 * 转到医护人员信息
	 * @param request
	 * @return
	 */
	@RequestMapping("/toEmployee")
	public String toEmployee(HttpServletRequest request) {
		String cusId = request.getParameter("cusId");
		request.setAttribute("cusId", cusId);
		return "/data/employee";
	}
	
	/**
	 * 医护人员信息
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/getAllEmployee")
	public void getAllEmployee(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
		List<Employee> list = new ArrayList<Employee>();
		String rows = request.getParameter("rows");// 每页显示的记录数
		String page = request.getParameter("page");// 当前第几页
		String cusId = request.getParameter("cusId");
		String countSql = "select count(EM_ID) from tbl_Employee where cus_Id=?0";
		String hql = "from Employee where cusId=?0 order by emAddtime desc nulls last";
		int count = baseService.findCountSql(countSql,cusId);
		if(count > 0){
			list = baseService.findByPage(hql, rows, page,cusId);
		}
		Utils.toBeJson(list, count, response);
	}

}
