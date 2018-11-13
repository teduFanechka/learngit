package yibao.yiwei.controller.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import yibao.yiwei.entity.system.Parameter;
import yibao.yiwei.service.IBaseService;

@Controller
public class ParameterController {
	
	@Autowired
	private IBaseService<Parameter> baseService;
	
	/**
	 * 转到系统参数
	 * @return
	 */
	@AuthPassport
	@RequestMapping("/toParameter")
	public String toParameter() {
		return "/system/parameter";
	}
	
	/**
	 * 系统参数
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getAllParameter")
	public Map<String, Object> getAllParameter(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String rows = request.getParameter("rows");// 每页显示的记录数
		String page = request.getParameter("page");// 当前第几页
		String hql = "from Parameter order by paramKey";
		String hqlC = "select count(paramId) from Parameter";
		List<Parameter> list = baseService.findByPage(hql, rows, page);
		int count = baseService.findCount(hqlC);
		map.put("rows", list);
		map.put("total", count);
		return map;
	}
	
	/**
	 * 添加或修改参数
	 * @param parameter
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/addOrUpdateParameter")
	public Map<String, Object> addOrUpdateParameter(Parameter parameter) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (parameter.getParamId().equals("")) {
			baseService.save(parameter);
			map.put("status", "参数添加成功");
		} else {
			baseService.update(parameter);
			map.put("status", "参数修改成功");
		}
		return map;

	}
	
	/**
	 * 删除参数
	 * @param ids
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/delParameter")
	public Map<String, Object> delParameter(String ids) {
		Map<String, Object> map = new HashMap<String, Object>();
		String arr[] = ids.split(",");
		for (int i = 0; i < arr.length; i++) {
			String paramId = arr[i];
			baseService.updateOrDelete("delete Parameter where paramId=?0", paramId);
		}
		map.put("status", "参数删除成功");
		return map;
	}
}
