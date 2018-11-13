package yibao.yiwei.controller.system;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import yibao.yiwei.entity.system.Areacode;
import yibao.yiwei.entity.system.Manager;
import yibao.yiwei.entity.system.Privilege;
import yibao.yiwei.service.IBaseService;

@Controller
@SuppressWarnings("unchecked")
public class LoginController {
	
	@Autowired
	private IBaseService baseService;
	
	/**
	 * 转到登录页面
	 * @param request
	 * @return
	 */
	@RequestMapping("/toLogin")
	public String toLogin(HttpServletRequest request) {
		request.getSession().invalidate();
		return "login";
	}

	/**
	 * 登录
	 * @param userCode
	 * @param passWord
	 * @param request
	 * @return
	 */
	@RequestMapping("/index")
	public String index(String userCode, String passWord, HttpServletRequest request) {
		String hql = "from Manager where muUsercode=?0 and muPasswd=?1";
		List list = baseService.find(hql, userCode, passWord);
		if(list.size()>0){
			Manager manager = (Manager)list.get(0);
			if(manager.getMuStatus().equals("0")){
				request.setAttribute("userCode", userCode);
				request.setAttribute("info", "该用户已被禁用，请联系管理员");
				return "login";
			} else {
				String userId = manager.getMuUserid();
				request.getSession().setAttribute("manager",manager);
				
				//查询区划
				hql = "SELECT a.AC_AREACODE,a.AC_AREANAME FROM TBL_AREACODE a " +
						"INNER JOIN SYS_MANAREA_RELATE mr ON a.AC_AREACODE=mr.AC_AREACODE AND a.AC_STATUS=1 " +
						"WHERE mr.MU_USERID=?0";
				list = baseService.findSql(hql, userId);
				String areacodes = "";
				Object[] obj = {};
				List list2 = new ArrayList();
				for(int i = 0; i < list.size(); i++){
					obj = (Object[])list.get(i);
					Areacode areacode = new Areacode();
					areacode.setAcAreacode(obj[0]+"");
					areacode.setAcAreaname(obj[1]+"");
					list2.add(areacode);
					if(i == list.size()-1){
						areacodes = areacodes + obj[0];
					} else {
						areacodes = areacodes + obj[0]+",";
					}
				}
				request.setAttribute("areacodeList", list2);
				request.getSession().setAttribute("areacodes", areacodes);
				//后期删除
				request.setAttribute("codelist", list2);
				request.getSession().setAttribute("acAreacode", areacodes);
				
				//查询权限
				String sql = "SELECT DISTINCT p.PRI_NAME,p.PRI_URL,p.PRI_ICON,p.PRI_PARENT,p.PRI_ID,p.PRI_ORDER FROM SYS_PRIVILEGE p " +
						"INNER JOIN SYS_PRIVILEGE_ROLE pr ON p.PRI_ID=pr.PRI_ID AND p.PRI_STATUS='1' " +
						"INNER JOIN SYS_ROLE r ON pr.RO_ID=r.RO_ID AND r.RO_STATUS='1' " +
						"INNER JOIN SYS_ROLE_MANAGER rm ON r.RO_ID=rm.RO_ID " +
						"WHERE rm.MU_USERID=?0 ORDER BY p.PRI_ORDER NULLS LAST";
				list = baseService.findSql(sql, userId);
				list2 = new ArrayList();
				for(int i = 0; i < list.size(); i++){
					obj = (Object[])list.get(i);
					Privilege privilege = new Privilege();
					privilege.setPriName(obj[0] != null ? obj[0].toString() : null);
					privilege.setPriUrl(obj[1] != null ? obj[1].toString() : null);
					privilege.setPriIcon(obj[2] != null ? obj[2].toString() : null);
					privilege.setPriParent(obj[3] != null ? obj[3].toString() : null);
					privilege.setPriId(obj[4] != null ? obj[4].toString() : null);
					list2.add(privilege);
				}
				request.getSession().setAttribute("priList", list2);
				
				//记录登录日志
				/*Errorlog errorlog = new Errorlog();
				errorlog.setCusName("登录名："+manager.getMuUsercode()+"（"+manager.getMuUsername()+"）");
				errorlog.setErrType("登录系统");
				errorlog.setErrAddtime(new Date());
				errorlog.setErrLog("IP地址："+request.getRemoteAddr());
				baseService.save(errorlog);*/
				
				//首页最新动态
				hql = "from Tzddlog where tzOperate=0 and tzStatus=0 order by tzDate desc nulls last"; 
				list = baseService.findByPage(hql, "7", "1");
				request.setAttribute("tzddList", list);
				
				return "index";
			}
		} else {
			request.setAttribute("userCode", userCode);
			request.setAttribute("info", "用户名或密码不正确!");
			return "login";
		}
	}

	/**
	 * 退出
	 * @param request
	 * @return
	 */
	@RequestMapping("/signOut")
	public String signOut(HttpServletRequest request) {
		request.getSession().invalidate();
		return "redirect:/toLogin";
	}

}
