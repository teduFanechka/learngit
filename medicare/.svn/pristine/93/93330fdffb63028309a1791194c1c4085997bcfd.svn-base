package yibao.yiwei.controller.system;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import yibao.yiwei.entity.system.Manager;
import yibao.yiwei.entity.system.ManareaRelate;
import yibao.yiwei.entity.system.RoleManager;
import yibao.yiwei.service.IBaseService;

@SuppressWarnings("unchecked")
@Controller
public class ManagerController {
	
	@Resource(name = "baseService")
	private IBaseService baseService;
	
	/**
	 * 转到用户管理
	 * @param request
	 * @return
	 */
	@AuthPassport
	@RequestMapping("/toManager")
	public String toManager(HttpServletRequest request) {
		String hql = "from Areacode where acStatus=1 order by acOrder";//查询已启用的区划
		List list = baseService.find(hql);
		request.setAttribute("areacodeList", list);
		hql = "from Role where roStatus=1 order by roOrder";//查询已启用的角色
		list = baseService.find(hql);
		request.setAttribute("roleList", list);
		return "/system/manager";
	}
	
	/**
	 * 用户管理
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping("/getAllManager")
	public Map getAllManager(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String rows = request.getParameter("rows");// 每页显示的记录数
		String page = request.getParameter("page");// 当前第几页
		map.put("total", baseService.findCount("select count(muUserid) from Manager"));
		map.put("rows", baseService.findByPage("from Manager", rows, page));
		return map; 
	}
	
	/**
	 * 添加或修改用户
	 * @param request
	 * @param response
	 * @return
	 * @throws ParseException
	 */
	@ResponseBody
	@RequestMapping("/addOrUpdateManager ")
	public Map addOrUpdateManager(Manager manager){
		Map<String, Object> map = new HashMap<String, Object>();
		if (manager.getMuUserid().equals("")) {
			manager.setMuStatus("1");
			baseService.save(manager);
			map.put("status", "添加用户成功");
		} else {
			baseService.update(manager);
			map.put("status", "修改用户成功");
		}
		return map;
	}
	
	/**
	 * 删除用户
	 * @param ids
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/delManager")
	public Map delManager(String ids) {
		String arr[] = ids.split(",");
		for (int i = 0; i < arr.length; i++) {
			String muUserid = arr[i];
			baseService.updateOrDelete("delete Manager where muUserid=?0", muUserid);
			baseService.updateOrDelete("delete ManareaRelate where muUserid=?0", muUserid);
			baseService.updateOrDelete("delete RoleManager where muUserid=?0", muUserid);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", "删除用户成功");
		return map;
	}
	
	/**
	 * 修改用户状态
	 * @param status
	 * @param muUserid
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateManagerStatus")
	public Map updateManagerStatus(String status,String muUserid){
		Map<String, Object> map = new HashMap<String, Object>();
		String hql = "update Manager m set m.muStatus=?0 where m.muUserid=?1";
		if(status.equals("1")){
			baseService.updateOrDelete(hql, "1", muUserid);
			map.put("status", "用户状态启用成功");
		} else {
			baseService.updateOrDelete(hql, "0", muUserid);
			map.put("status", "用户状态禁用成功");
		}
		return map;
	}
	
	/**
	 * 查询用户已有角色
	 * @param muUserid
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getRoleManager")
	public Map getRoleManager(String muUserid) {
		String hql = "select roId from RoleManager where muUserid=?0";
		List list = baseService.find(hql, muUserid);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		return map;
	}
	
	/**
	 * 保存角色设置
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/addRoleManager")
	public Map addRoleManager(String muUserid,String roIdList) {
		String hql = "delete RoleManager where muUserid=?0 ";
		baseService.updateOrDelete(hql,muUserid);
		if (roIdList != null && !roIdList.equals("")) {
			String arr[] = roIdList.split(",");
			for (int i = 0; i < arr.length; i++) {
				String roId = arr[i];
				RoleManager roleManager = new RoleManager();
				roleManager.setRoId(roId);
				roleManager.setMuUserid(muUserid);
				baseService.save(roleManager);
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", "角色设置成功");
		return map;
	}
	
	/**
	 * 查询用户已有区划
	 * @param muUserid
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getAreaManager")
	public Map getxiaquRelate(String muUserid) {
		Map<String, Object> map = new HashMap<String, Object>();
		String hql = "select acAreacode from ManareaRelate where muUserid=?0";
		List list = baseService.find(hql, muUserid);
		map.put("list", list);
		return map;
	}
	
	/**
	 * 保存区划设置
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/addAreaManager")
	public Map addManareaRelate(String muUserid,String areacodeList) {
		String hql = "delete ManareaRelate where  muUserid=?0";
		baseService.updateOrDelete(hql, muUserid);
		if(null != areacodeList && !areacodeList.equals("")){
			String[] arr = areacodeList.split(",");
			for (int i = 0; i < arr.length; i++) {
				String acAreacode = arr[i];
				ManareaRelate manareaRelate = new ManareaRelate();
				manareaRelate.setAcAreacode(acAreacode);
				manareaRelate.setMuUserid(muUserid);
				baseService.save(manareaRelate);
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", "区划设置成功");
		return map;
	}
	
	/**
	 * 查询用户角色
	 * @param userId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/manRole")
	public Map manRole(String userId){
		Map<String, Object> map = new HashMap<String, Object>();
		String sql = "SELECT r.RO_NAME FROM SYS_ROLE r inner join SYS_ROLE_MANAGER rm on r.RO_ID=rm.RO_ID and r.RO_STATUS=1 WHERE rm.MU_USERID=?0 order by r.ro_order";
		List list = baseService.findSql(sql, userId);
		String roName = "";
		for(int i = 0; i < list.size(); i++){
			if(i==list.size()-1){
				roName+=list.get(i);
			}else {
				roName+=list.get(i)+"、";
			}
		}
		map.put("roName", roName);
		return map;
	}
	
	/**
	 * 查询用户区划
	 * @param userId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/manArea ")
	public Map manArea(String userId){
		Map<String, Object> map = new HashMap<String, Object>();
		String sql = "SELECT a.AC_AREANAME FROM TBL_AREACODE a inner join SYS_MANAREA_RELATE mr on a.AC_AREACODE=mr.AC_AREACODE and a.AC_STATUS=1 WHERE mr.MU_USERID=?0 order by a.ac_order";
		List list = baseService.findSql(sql, userId);
		String areaName = "";
		for(int i = 0; i < list.size(); i++){
			if(i==list.size()-1){
				areaName+=list.get(i);
			}else {
				areaName+=list.get(i)+"、";
			}
		}
		map.put("areaName", areaName);
		return map;
	}
	
	/**
	 * 修改密码
	 * @param pw
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updatePW")
	public Map updatePW(String pw, HttpServletRequest request) {
		Manager manager = (Manager)request.getSession().getAttribute("manager");
		String hql = "update Manager m set m.muPasswd=?0 where m.muUserid=?1";
		int result = baseService.updateOrDelete(hql, pw, manager.getMuUserid());
		String status;
		if (result == 1) {
			status = "密码修改成功";
		} else {
			status = "密码修改失败";
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", status);
		return map;
	}
	
}
