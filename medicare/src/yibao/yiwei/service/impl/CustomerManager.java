package yibao.yiwei.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import yibao.yiwei.dao.IBaseDao;
import yibao.yiwei.service.ICustomerManager;

/**
 * 客户端记录表
 * @author Administrator
 *
 */
@SuppressWarnings("unchecked")
@Service
@Transactional
public class CustomerManager implements ICustomerManager {
	
	@Autowired
	private IBaseDao baseDao;
	
	// 根据药品入库统计ids 查询对应的药店名称集合
	@Override
	public List getCusNames(String hql, List cusIds) {
		List list = new ArrayList();
		if (cusIds.size() > 0) {
			for (int i = 0; i < cusIds.size(); i++) {
				String cusId = (String) cusIds.get(i);
				List ltem = baseDao.find(hql, cusId);
				if (ltem.size() > 0) {
					String cusName = ltem.get(0).toString();
					list.add(i, cusName);
				}
			}
		}
		return list;
	}

	// 获取cusName 对应的cusID
	@Override
	public String getCusId(String cusName) {
		String cusId = "";
		String hql = "select c.cusId from Customer c where c.cusName=?0";
		List list = baseDao.find(hql, cusName);
		if (list.size() > 0) {
			cusId = list.get(0).toString();
		}
		return cusId;
	}

	// 根据客户标识获取所有客户名称列表
	@Override
	public List getAllCusName(String cusFlag, String acAreacode) {
		String hql;
		List list = new ArrayList();
		if (acAreacode != null) {
			hql = "select c.cusName,c.cusId  from Customer c ,CusareaRelate r where c.cusFlag=?0 and (c.cusStatus=1 or c.cusStatus=2) "
				+ "and c.cusId=r.cusId and r.acAreacode in("+ acAreacode+ ") order by nlssort(c.cusName,'NLS_SORT=SCHINESE_PINYIN_M') ";
			list = baseDao.find(hql, cusFlag);
			return list;
		} else {
			return null;
		}
	}

	// 根据cusflag类型过滤cusids得到当前cusId集合中元素等于当于cusFlag *未应用*
	@Override
	public List getRealCusIds(List cusIds, String cusFlag) {
		List list = new ArrayList();
		if (cusIds.size() > 0) {
			int count = 0;
			for (int i = 0; i < cusIds.size(); i++) {
				Object cusId = cusIds.get(i);
				String hql = "select c.cusFlag from Customer c where c.cusId=?0";
				List l = baseDao.find(hql, cusId);
				String tem = l.get(0).toString();
				if (cusFlag.equals(tem)) {
					list.add(count, cusId);
					count++;
				}
			}
		}
		return list;
	}

	/**
	 * 符合标识的list 方法略同上个方法,ArrayList元素是数组需要注意 根据客户标识过滤list
	 */
	@Override
	public List getArrayCusIds(List arrList, String cusFlag) {
		List list = new ArrayList();
		if (arrList.size() > 0) {
			int count = 0;
			for (int i = 0; i < arrList.size(); i++) {
				Object[] cusId = null;
				// 是否是数组
				if (arrList.get(i) instanceof Object[]) {
					cusId = (Object[]) arrList.get(i);
				}
				String hql = "select c.cusFlag from Customer c where c.cusId=?0";
				// 数组的第一个元素内容是cusId
				List l = baseDao.find(hql, cusId[0]);
				String tem = l.get(0).toString();
				if (cusFlag.equals(tem)) {
					list.add(count, cusId);
					count++;
				}
			}
		}
		return list;
	}

	// 略同(getCusNames)查询对应的药店名称集合, ArrayList元素是数组需要注意
	@Override
	public List getArrCusNames(String idHql, List realarrList) {
		List list = new ArrayList();
		if (realarrList.size() > 0) {
			for (int i = 0; i < realarrList.size(); i++) {
				Object[] obj = (Object[]) realarrList.get(i);
				List l = baseDao.find(idHql, obj[0]);
				if (l.size() > 0) {
					String tem = l.get(0).toString();
					list.add(i, tem);
				}
			}
		}
		return list;
	}

}
