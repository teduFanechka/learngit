package yibao.yiwei.test;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import yibao.yiwei.dao.impl.BaseDao;
import yibao.yiwei.entity.system.Manager;

public class ManagerTestQuery {
	
	@Autowired
	private BaseDao<Manager> dao;
	
	@Test
	public void test1(){
		AbstractApplicationContext ac = new ClassPathXmlApplicationContext("classpath*:spring-beans.xml","classpath*:spring-servlet.xml");
		String sql = "select MU_USERCODE,MU_PASSWD from TBL_MANAGER where MU_STATUS=1";
		List<Manager> list = dao.findSql(sql);
		for(Manager man : list){
			System.out.println(man.toString());
		}
		ac.close();
	}
}
