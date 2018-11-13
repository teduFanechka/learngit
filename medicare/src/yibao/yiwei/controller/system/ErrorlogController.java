package yibao.yiwei.controller.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import yibao.yiwei.entity.system.Errorlog;
import yibao.yiwei.service.IBaseService;

/**
 * 错误日志
 * @author Administrator
 *
 */
@SuppressWarnings("unchecked")
@Controller
public class ErrorlogController {
	
	@Autowired
	private IBaseService<Errorlog> baseService;
	
	/**
	 * 转到解析日志
	 * @return
	 */
	@AuthPassport
	@RequestMapping("/toErrorlog")
	public String toErrorlog() {
		return "/system/errorlog";
	}

	/**
	 * 解析日志
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getErrorlog")
	public Map<String, Object> getErrorlog(HttpServletRequest request,String cusName) {
		String rows = request.getParameter("rows");// 每页显示的记录数
		String page = request.getParameter("page");// 当前第几页
		String hql = "from Errorlog";
		String countHql = "select count(err_Id) from sys_Errorlog";
		if (null != cusName && !cusName.equals("")) {
			cusName = cusName.trim();
			hql+= " where cusName like '%" + cusName + "%' or errFilepath like '%" + cusName + "%' or errType like '%" + cusName + "%'";
			countHql+= " where cus_Name like '%" + cusName + "%' or ERR_FILEPATH like '%" + cusName + "%' or ERR_TYPE like '%" + cusName + "%'";
		}
		hql+=" order by errAddtime desc";
		List<Errorlog> list = baseService.findByPage(hql, rows, page);
		int count = baseService.findCountSql(countHql);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", list);
		map.put("total", count);
		return map;
	}
	
	/**
	 * 删除日志
	 * @param ids
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/delErr")
	public Map delErr(String ids) {
		Map<String, Object> map = new HashMap<String, Object>();
		String arr[] = ids.split(",");
		for (int i = 0; i < arr.length; i++) {
			String errId = arr[i];
			baseService.updateOrDelete("delete Errorlog where errId=?0", errId);
		}
		map.put("status", "日志删除成功");
		return map;
	}
}
