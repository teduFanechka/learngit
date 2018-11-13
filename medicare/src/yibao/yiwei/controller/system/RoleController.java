package yibao.yiwei.controller.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import yibao.yiwei.entity.system.PrivilegeRole;
import yibao.yiwei.entity.system.Role;
import yibao.yiwei.service.IBaseService;

@SuppressWarnings("unchecked")
@Controller
public class RoleController {

	@Autowired
	private IBaseService baseService;
	
	/**
	 * 转到角色管理
	 * @param request
	 * @return
	 */
	@AuthPassport
	@RequestMapping("/toRole")
	public String toRole(HttpServletRequest request) {
		return "/system/role";
	}
	
	/**
	 * 查询角色
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getAllRole")
	public Map getAllRole(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String rows = request.getParameter("rows");// 每页显示的记录数
		String page = request.getParameter("page");// 当前第几页
		String hql = "from Role order by roOrder";// 注意hql
		List<Role> list = baseService.findByPage(hql, rows, page);
		int count = (int) baseService.findCount("select count(roId) from Role");
		map.put("total", count);
		map.put("rows", list);
		return map;
	}
	
	/**
	 * 添加或修改角色
	 * @param role
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/addOrUpdateRole")
	public Map addOrUpdateRole(Role role) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (role.getRoId().equals("")) {
			role.setRoStatus("1");
			baseService.save(role);
			map.put("status", "角色添加成功");
		} else {
			baseService.update(role);
			map.put("status", "角色修改成功");
		}
		return map;
	}
	
	/**
	 * 删除角色
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/delRole")
	public Map delRole(String ids) {
		Map<String, Object> map = new HashMap<String, Object>();
		String arr[] = ids.split(",");
		for (int i = 0; i < arr.length; i++) {
			String roId = arr[i];
			baseService.updateOrDelete("delete Role where roId=?0", roId);//删除角色
			baseService.updateOrDelete("delete PrivilegeRole where roId=?0",roId);//删除角色权限对应
			baseService.updateOrDelete("delete RoleManager where roId=?0",roId);//删除角色用户对应
		}
		map.put("status", "角色删除成功");
		return map;
	}
	
	/**
	 * 修改角色状态
	 * @param status
	 * @param roId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateRoleStatus")
	public Map updateRoleStatus(String status,String roId){
		Map<String, Object> map = new HashMap<String, Object>();
		String hql = "update Role r set r.roStatus=?0 where r.roId=?1";
		if(status.equals("1")){
			baseService.updateOrDelete(hql, "1", roId);
			map.put("status", "角色启用成功");
		} else {
			baseService.updateOrDelete(hql, "0", roId);
			map.put("status", "角色禁用成功");
		}
		return map;
	}
	
	/**
	 * 查询角色已有权限
	 * @param roId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getPriRole")
	public Map getPriRole(String roId) {
		Map<String, Object> map = new HashMap<String, Object>();
		String hql = "select priId from PrivilegeRole where roId=?0";
		List list = baseService.find(hql,roId);
		map.put("list", list);
		return map;
	}
	
	/**
	 * 保存权限设置
	 * @param roId
	 * @param privileges
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/addPriRole")
	public Map addPriRole(String roId,String priList) {
		Map<String, Object> map = new HashMap<String, Object>();
		String hql = "delete PrivilegeRole where roId=?0";
		baseService.updateOrDelete(hql,roId);//先删除
		if (!(priList == null || priList.equals(""))) {// 添加新的区划关系
			String arr[] = priList.split(",");
			for (int i = 0; i < arr.length; i++) {
				String priId = arr[i];
				PrivilegeRole privilegeRole = new PrivilegeRole();
				privilegeRole.setPriId(priId);
				privilegeRole.setRoId(roId);
				baseService.save(privilegeRole);
			}
		} 
		map.put("status", "权限设置成功");
		return map;
	}
}
