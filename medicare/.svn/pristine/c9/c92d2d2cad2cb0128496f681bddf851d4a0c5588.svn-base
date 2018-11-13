package yibao.yiwei.controller.system;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import yibao.yiwei.entity.system.Customer;
import yibao.yiwei.entity.system.Errorlog;
import yibao.yiwei.entity.system.Tzddlog;
import yibao.yiwei.service.IBaseService;
import yibao.yiwei.service.ITimerTask;
import yibao.yiwei.service.ITzddlogManager;

/**
 * 定点日志表
 * @author Administrator
 *
 */
@SuppressWarnings("unchecked")
@Controller
public class TzddlogController {
	
	@Autowired
	private IBaseService baseService;
	
	@Autowired
	private ITzddlogManager tzddlogManager;
	
	@Autowired
	private ITimerTask timerTask;

	/**
	 * 转到定点结算
	 * @return
	 */
	@AuthPassport
	@RequestMapping("/toTzddlog")
	public String toTzddlog() {
		return "/system/tzddlog";
	}
	
	/**
	 * 定点结算
	 * @param request
	 * @param tzCusname
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getAllTzddlog")
	public Map getAllTzddlog(HttpServletRequest request, String tzCusname) {
		Map<String, Object> map = new HashMap<String, Object>();
		String rows = request.getParameter("rows");// 每页显示的记录数
		String page = request.getParameter("page");// 当前第几页
		String hql = "from Tzddlog";
		String countHql = "select count(tz_Id) from TBL_TZDDLOG";
		if (null != tzCusname && !tzCusname.equals("")) {
			tzCusname = tzCusname.trim();
			hql+= " where tzCusname like '%" + tzCusname + "%' or tzYybm = '" + tzCusname + "'";
			countHql+= " where tz_Cusname like '%" + tzCusname + "%' or tz_Yybm = '" + tzCusname + "'";
		}
		hql+=" order by tzDate desc nulls last";//NULLS LAST：字段为null值排在最后，需hibernate4.2.0cr1及以后支持
		int total = baseService.findCountSql(countHql);
		List list = baseService.findByPage(hql, rows, page);
		map.put("total", total);
		map.put("rows", list);
		return map;
	}
	
	/**
	 * 开关医保结算
	 * @param ids
	 * @param operate
	 * @return
	 */
	@ResponseBody
	@RequestMapping("updateTzOperate")
	public Map updateTzOperate(String tzId,String operate){
		Map<String, Object> map = new HashMap<String, Object>();
		String errflag = tzddlogManager.updateTzOperate(tzId, operate);
		String status;
		if(operate.equals("0")){
			status = "暂停医保结算";
		} else {
			status = "开启医保结算";
		}
		if(errflag.equals("0")){
			status+="成功";
		} else {
			status+="失败";
		}
		map.put("status", status);
		return map;
	}
	
	/**
	 * 查找已暂停医保结算定点
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getTzOperateStop")
	public Map getTzOperateStop(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String rows = request.getParameter("rows");// 每页显示的记录数
		String page = request.getParameter("page");// 当前第几页
		String hql = "from Tzddlog where tzOperate=0 and tzStatus=0 order by tzDate desc nulls last";//操作类型是停止0，响应结果是成功0
		String countHql = "select count(tzId) from Tzddlog where tzOperate=0 and tzStatus=0";
		List list = baseService.findByPage(hql, rows, page);
		int total = baseService.findCount(countHql);
		map.put("total", total);
		map.put("rows", list);
		return map;
	}

	/**
	 * 开关扫描
	 * @param ids
	 * @param scan
	 * @return
	 */
	@ResponseBody
	@RequestMapping("updateTzScan")
	public Map updateTzScan(String ids, String scan){
		Map<String, Object> map = new HashMap<String, Object>();
		String arr[] = ids.split(",");
		String hql = "update Tzddlog set tzScan=?0 where tzId=?1";
		for (int i = 0; i < arr.length; i++) {
			baseService.updateOrDelete(hql, scan, arr[i]);
		}
		String status = null;
		if(scan.equals("0")){
			status = "开启";
		} else {
			status = "停止";
		}
		status+="扫描成功";
		map.put("status", status);
		return map;
	}
	
	/**
	 * 查询已停止扫描定点
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getTzScanStop")
	public Map getTzScanStop(HttpServletRequest request) {
		String rows = request.getParameter("rows");// 每页显示的记录数
		String page = request.getParameter("page");// 当前第几页
		String hql = "from Tzddlog where tzScan=1 order by tzDate desc nulls last";
		String countHql = "select count(tzId) from Tzddlog where tzScan=1";
		List list = baseService.findByPage(hql, rows, page);
		int total = baseService.findCount(countHql);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("total", total);
		map.put("rows", list);
		return map;
	}
	
	/**
	 * 开关医保标记
	 * @param ids
	 * @param ybcx
	 * @return
	 */
	@ResponseBody
	@RequestMapping("updateTzYbcx")
	public Map updateTzYbcx(String ids,String ybcx){
		Map<String, Object> map = new HashMap<String, Object>();
		String arr[] = ids.split(",");
		String hql = "update Tzddlog set tzYbcx=?0 where tzId=?1";
		for (int i = 0; i < arr.length; i++) {
			baseService.updateOrDelete(hql, ybcx, arr[i]);
		}
		String status = null;
		if(ybcx.equals("0")){
			status = "标记";
		} else {
			status = "取消标记";
		}
		status+="医保成功";
		map.put("status", status);
		return map;
		
	}
	
	/**
	 * 查询使用医保程序定点
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getTzYbcxStart")
	public Map getTzYbcxStart(HttpServletRequest request) {
		String rows = request.getParameter("rows");// 每页显示的记录数
		String page = request.getParameter("page");// 当前第几页
		String hql = "from Tzddlog where tzYbcx=0 order by tzDate desc nulls last";
		List list = baseService.findByPage(hql, rows, page);
		String countHql = "select count(tzId) from Tzddlog where tzYbcx=0";
		int total = baseService.findCount(countHql);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("total", total);
		map.put("rows", list);
		return map;
	}
	
	/**
	 * 添加定点
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/addTzddlog")
	public Map addTzddlog() {
		String hql = "select c.* from TBL_CUSTOMER c left join TBL_TZDDLOG t on c.CUS_DAREWAY=t.TZ_YYBM where LENGTH(c.CUS_DAREWAY)=6 and(c.CUS_STATUS=1 or c.CUS_STATUS=2) and t.TZ_YYBM is null";
		List<Customer> list = baseService.findEntitySql(hql,Customer.class);
		int count = 0;
		for (int i = 0; i < list.size(); i++) {
			Customer customer = list.get(i);
			Tzddlog tzddlog = new Tzddlog();
			tzddlog.setTzCusid(customer.getCusId());
			tzddlog.setTzCusname(customer.getCusName());
			tzddlog.setTzYybm(customer.getCusDareway());
			tzddlog.setTzDate(new Date());
			tzddlog.setTzOperate("9");// 初始化赋值
			tzddlog.setTzStatus("9");
			tzddlog.setTzScan("0");
			tzddlog.setTzYbcx("1");
			tzddlog.setTzNote("<font color='blue'>初始化</font>");
			baseService.save(tzddlog);
			count++;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", "添加成功了"+count+"个定点");
		return map;
	}
	
	/**
	 * 修改定点信息
	 * @param tzddlog
	 * @return
	 */
	@ResponseBody
	@RequestMapping("updateTzddlog")
	public Map updateTzddlog(Tzddlog tzddlog) {
		Map<String, Object> map = new HashMap<String, Object>();
		baseService.update(tzddlog);
		map.put("status", "定点名称修改成功");
		return map;
	}
	
	/**
	 * 删除定点
	 * @param ids
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/deleteTzddlog")
	public Map deleteTzddlog(String ids) {
		String arr[] = ids.split(",");
		for (int i = 0; i < arr.length; i++) {
			baseService.updateOrDelete("delete Tzddlog where tzId=?0",arr[i]);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", "定点删除成功");
		return map;
	}
	
	/**
	 * 停止定点
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/stopTzddlog")
	public Map stopTzddlog(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", "正在停止不符合数据上传要求的定点的医保结算...");
		TimerTask task = new TimerTask(){
			public void run() {
				try {
					timerTask.stopTzddlog();
				} catch (Exception e) {
					Errorlog errorlog = new Errorlog();
    				errorlog.setErrAddtime(new Date());
    				errorlog.setErrLog(e.toString());
    				errorlog.setErrType("task停止定点异常");
    				baseService.save(errorlog);
				}
			}
		};
		ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);
		pool.scheduleAtFixedRate(task, 0, 1, TimeUnit.DAYS);//0秒后开始执行，任务开始时间+1天，循环执行
		return map;
	}
	
}
