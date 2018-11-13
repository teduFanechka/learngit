package yibao.yiwei.controller.wrongInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import yibao.yiwei.entity.wrongInfo.SettingCheck;
import yibao.yiwei.service.IBaseService;
import yibao.yiwei.utils.Utils;

@SuppressWarnings("unchecked")
@Controller
public class SetWrongStatusController {
	
	@Autowired
	private IBaseService baseService;
	/**
	 * 转到空值0值设置页面
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/toSetWrongInfo")
	public String getAllWrongInfo(HttpServletRequest request) throws IOException{
		String hql = "from SettingCheck where parentId=0";
		List<SettingCheck> list = baseService.find(hql);
	
		request.setAttribute("wrongList", list);		
		return "wrong/setWrongInfo";
	}
	
	/**
	 * 查询权限
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping("/getSettingCheck")
	public List toSetWrongInfo(){		
		String hql = "from SettingCheck where parentId=?0";		
		List<SettingCheck> list = baseService.find(hql,0);
		List<SettingCheck> list2 = new ArrayList();
		SettingCheck settingCheck;
		for(int i=0;i<list.size();i++){
			settingCheck = list.get(i);
			list2 = baseService.find(hql, settingCheck.getSetId());
			settingCheck.setChildren(list2);
		}
		return list;
	}
	/**
	 * 修改0值/空值设置
	 * @param parentId
	 * @param setCode
	 * @param setNull
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateStatus")
	public Map updateStatus(String setId,String setCode,String setNull){
		Map<String,Object> map = new HashMap<String,Object>();
		Integer priId = Integer.parseInt(setId);
		Integer setId2 = Integer.parseInt(setCode);
		String hql = "update SettingCheck p set p.setNull=?0 where p.setId=?1 and p.setCode=?2";
		if(setNull.equals("1")){
			baseService.updateOrDelete(hql,1,priId,setId2);
			map.put("status", "修改成功");
		}else{
			baseService.updateOrDelete(hql,0,priId,setId2);
			map.put("status", "修改成功");
		}
		return map;
	}
	
}
