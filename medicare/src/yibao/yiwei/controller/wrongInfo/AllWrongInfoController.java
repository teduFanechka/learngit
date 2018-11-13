package yibao.yiwei.controller.wrongInfo;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import yibao.yiwei.controller.system.AuthPassport;
import yibao.yiwei.entity.wrongInfo.AllWrongInfo;
import yibao.yiwei.service.IBaseService;
import yibao.yiwei.utils.Utils;

@SuppressWarnings("unchecked")
@Controller
public class AllWrongInfoController {
	
	@Autowired
	private IBaseService baseService;
	
	/**
	 * 转到错误信息页面
	 * @return
	 */
	@AuthPassport
	@RequestMapping("/toWrongInfo")
	public String toWrongInfo(){
		
		return "/wrong/wrongInfo";
	}
	
	/**
	 * 获得错误信息
	 * @param request
	 * @param response
	 * @param startDate
	 * @param endDate
	 * @param cusDareway
	 * @param wriStatus
	 * @throws IOException
	 * @throws ParseException 
	 */
	@RequestMapping("/getAllWrongInfo")
	public void getAllWrongInfo(HttpServletRequest request, HttpServletResponse response,String startDate,String endDate,String cusDareway,String wriStatus) throws IOException, ParseException{
		List<AllWrongInfo> list = new ArrayList<AllWrongInfo>();
		String rows = request.getParameter("rows");// 每页显示的记录数
		String page = request.getParameter("page");// 当前第几页
		int count = 0;
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String hql = "from AllWrongInfo where 1=1";
		String countSql = "select count(WRI_ID) from tbl_AllWrongInfo where 1=1";
		//判断按医院名称搜索框是否为空，不为空则增加条件查询
		if(null !=cusDareway && !cusDareway.equals("")){
			cusDareway = cusDareway.trim();
			countSql += " and CUS_DAREWAY like '%"+cusDareway+"%'";
			hql += " and cusDareway like '%"+cusDareway+"%'";
		}
		//判断处理状态是否选择，已选择则增加条件查询
		if(null !=wriStatus && !wriStatus.equals("")){
			int status = Integer.parseInt(wriStatus.trim());
			countSql += " and WRI_STATUS = "+status+"";
			hql += " and wriStatus ="+status;
		}
		//如果选择了时间段查询，则：
		if(null != startDate && !startDate.equals("")){
			startDate = startDate.trim()+" 00:00:00";
		}
		if(null != endDate && !endDate.equals("")){
			endDate = endDate.trim()+" 23:59:59";
		}
		Date itemStartTime = null;
		Date itemEndTime = null;
		if(!startDate.equals("") && !endDate.equals("")){
			itemStartTime = sf.parse(startDate);
			itemEndTime = sf.parse(endDate);
			countSql += " and WRI_ADDTIME between ?0 and ?1";
			hql += " and wriAddtime between ?0 and ?1 order by wriAddtime desc nulls last";
			count = baseService.findCountSql(countSql, itemStartTime, itemEndTime);
			list = baseService.findByPage(hql, rows, page, itemStartTime, itemEndTime);
		}
		if(startDate.equals("") && endDate.equals("")){
			hql += " order by wriAddtime desc nulls last";
			count = baseService.findCountSql(countSql);
			list = baseService.findByPage(hql, rows, page);
		}
		if(startDate.equals("") && !endDate.equals("")){
			itemEndTime = sf.parse(endDate);
			countSql += " and WRI_ADDTIME < ?0";
			hql += " and wriAddtime < ?0 order by wriAddtime desc nulls last";
			count = baseService.findCountSql(countSql, itemEndTime);
			list = baseService.findByPage(hql, rows, page, itemEndTime);
		}
		if(!startDate.equals("") && endDate.equals("")){
			itemStartTime = sf.parse(startDate);
			countSql += " and WRI_ADDTIME > ?0";
			hql += " and wriAddtime > ?0 order by wriAddtime desc nulls last";
			count = baseService.findCountSql(countSql, itemStartTime);
			list = baseService.findByPage(hql, rows, page, itemStartTime);
		}
		Utils.toBeJson(list, count, response);
	}
	
	@ResponseBody
	@RequestMapping("/updateWrongInfoStatus")
	public Map updateAreacodeStatus(String status,String wriId){
		Map<String, Object> map = new HashMap<String, Object>();
		String hql = "update AllWrongInfo a set a.wriStatus=?0 where a.wriId=?1";
		if(status.equals("1")){
			baseService.updateOrDelete(hql, Integer.parseInt(status), wriId);
			map.put("status", "成功！");
		}
		return map;
	}
}
