package yibao.yiwei.controller.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import yibao.yiwei.entity.system.Privilege;
import yibao.yiwei.service.IBaseService;

@SuppressWarnings("unchecked")
@Controller
public class PrivilegeController {

	@Autowired
	private IBaseService baseService;
	
	/**
	 * 转到权限管理
	 * @param request
	 * @return
	 */
	@AuthPassport
	@RequestMapping("/toPrivilege")
	public String toPrivilege(HttpServletRequest request) {
		String hql = "from Privilege where priParent='0' order by priOrder";
		List list = baseService.find(hql);
		request.setAttribute("privilegeList", list);
		return "/system/privilege";
	}
	
	/**
	 * 查询权限
	 * @return
	 */
	@RequestMapping("/getPrivilege")
	@ResponseBody
	public List getPrivilege(){
		String hql = "from Privilege where priParent =?0 order by priOrder";
		List<Privilege> list = baseService.find(hql,"0");
		List<Privilege> list2 =new ArrayList();
		Privilege privilege;
		for(int i = 0; i < list.size(); i++){
			privilege = list.get(i);
			list2 = baseService.find(hql,privilege.getPriId());
			privilege.setChildren(list2);
		}
		return list;
	}
	
	/**
	 * 添加或修改权限
	 * @param privilege
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/addOrUpdatePrivilege")
	public Map addOrUpdatePrivilege(Privilege privilege) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (privilege.getPriId().equals("")) {
			privilege.setPriStatus("1");
			baseService.save(privilege);
			map.put("status", "权限添加成功");
		} else {
			baseService.update(privilege);
			map.put("status", "权限修改成功");

		}
		return map;
	}
	
	/**
	 * 删除权限
	 * @param ids
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/delPrivilege")
	public Map delPrivilege(String ids) {
		Map<String, Object> map = new HashMap<String, Object>();
		String arr[] = ids.split(",");
		for (int i = 0; i < arr.length; i++) {
			String priId = arr[i];
			baseService.updateOrDelete("delete Privilege where priId=?0", priId);
			baseService.updateOrDelete("delete PrivilegeRole where priId=?0",priId);
		}
		map.put("status", "权限删除成功");
		return map;
	}
	
	/**
	 * 修改权限状态
	 * @param status
	 * @param priId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updatePriStatus")
	public Map updatePriStatus(String status,String priId){
		Map<String, Object> map = new HashMap<String, Object>();
		String hql = "update Privilege p set p.priStatus=?0 where p.priId=?1";
		if(status.equals("1")){
			baseService.updateOrDelete(hql, "1", priId);
			map.put("status", "权限启用成功");
		} else {
			baseService.updateOrDelete(hql, "0", priId);
			map.put("status", "权限禁用成功");
		}
		return map;
	}
}
