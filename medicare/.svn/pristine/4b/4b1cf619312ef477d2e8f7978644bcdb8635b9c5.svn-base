package yibao.yiwei.service;

import java.util.List;

import yibao.yiwei.entity.system.QuartzJob;

public interface IQuartzJobManager<T> {
	
	//查询所有定时任务
	public List<QuartzJob> selectAll();
	
	//添加定时任务
	public int addjobgroup(QuartzJob quartzjob,Class<T> entity) throws Exception;

	//删除任务
	public int deljobgroup(String trigger_name);

	//暂停某任务
	public int pauseTrigger(String triggerName);
	
	//重启某任务
	public int resumeTriggers(String triggerName);

	
}
