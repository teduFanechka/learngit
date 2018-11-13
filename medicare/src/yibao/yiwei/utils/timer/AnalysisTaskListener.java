package yibao.yiwei.utils.timer;

import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.quartz.CronTrigger;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
/**
 * web.xml配置的定时任务监听
 * @author Administrator
 *
 */
public class AnalysisTaskListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		System.out.println("contextInitialized()方法在Web应用创建时被触发");
		try {
			SchedulerFactory schedulerFactory = new StdSchedulerFactory();
		    Scheduler scheduler = schedulerFactory.getScheduler();
		    List<String> triggerGroups = scheduler.getTriggerGroupNames();//获取调度器中所有的触发器组
		    for (int i = 0; i < triggerGroups.size(); i++) {//重新恢复在tgroup1组中，名为trigger1触发器的运行
		        List<String> triggers = scheduler.getTriggerGroupNames();
		        for (int j = 0; j < triggers.size(); j++) {//这里使用了两次遍历，针对每一组触发器里的每一个触发器名，和每一个触发组名进行逐次匹配
		            Trigger tg = scheduler.getTrigger(new TriggerKey(triggers.get(j), triggerGroups.get(i)));
		            
		            if (tg instanceof CronTrigger) {//根据名称判断   /* && tg.getDescription().equals("jgroup1.DEFAULT")*/
		            	scheduler.resumeJob(new JobKey(triggers.get(j),triggerGroups.get(i)));//恢复运行//由于我们之前测试没有设置触发器所在组，所以默认为DEFAULT
		            }
		        }
		    }
		    scheduler.start();
		    System.out.println("scheduler调度器启动");
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		System.out.println("contextDestroyed()方法，Web应用被销毁");
	}

}
