package yibao.yiwei.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import yibao.yiwei.dao.IBaseDao;
import yibao.yiwei.service.ISalesitemManager;

/**
 * 销售信息
 * @author Administrator
 *
 */
@SuppressWarnings("unchecked")
@Service
@Transactional
public class SalesitemManager implements ISalesitemManager {
	
	@Autowired
	private IBaseDao baseDao;

	// 根据cusid字符串获取各定点销售总额统计list
	@Override
	public List getEachPointTotal(List cusIds, int count, String startDate) throws ParseException {
		List list = new ArrayList();
		if (cusIds.size() > 0) {
			for (int i = 0; i < cusIds.size(); i++) {
				Object cusId = cusIds.get(i);
				// 定义存放当前cusId 15天每天的totals 集合
				List l = new ArrayList();
				//循环some天倒计
				for (int y = 0; y <= count; y++) {
					SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
					Date date;
					if (startDate != null) {// 有查询条件
						date = sf.parse(startDate);
					} else {
						date = new Date();
					}
					GregorianCalendar gc = new GregorianCalendar();
					gc.setTime(date);
					gc.add(5, -y);
					gc.set(gc.get(Calendar.YEAR), gc.get(Calendar.MONTH), gc.get(Calendar.DATE));
					String dateString = sf.format(gc.getTime());
					date = sf.parse(dateString);
					String hqlDates = "select s.drugPicktime from Salesitem s where s.cusId=?0 and s.drugPicktime = ?1";
					// 定义获取循环一天 有没有数据
					List dates = baseDao.find(hqlDates, cusId, date);
					if (dates.size() > 0) {
						// 统计相加 当前id所有销售单内的 药品价格 乘以 药品数据
						String hql = "select sum(s.drugSalesprice * s.drugNum) from Salesitem s where s.cusId= ?0 and s.drugPicktime = ?1";
						// 定义临时集合存储当前cusId当前日期计算到的总额统计
						List temp = baseDao.find(hql, cusId, date);
						l.add(y, temp.get(0));
					} else {
						l.add(y, 0);
					}
				}
				list.add(i, l);
			}
		} else {
			// System.out.println("未获取到药品入库种类统计ids!");
		}
		return list;
	}

	// 根据cusId[日期查询]获取对应的药品编码集合
	@Override
	public List getDrugCodes(String cusId, String firstDate, String secondDate) throws ParseException {
		List list = new ArrayList();
		//两个日期之间
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		Date bigDate, smaDate;
		// 查询条件 不为空时
		if (firstDate != null && secondDate != null) {
			smaDate = sf.parse(firstDate);
			bigDate = sf.parse(secondDate);
		} else {
			// 没有条件默认最近15天
			bigDate = new Date();// 当前日期
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(bigDate);
			gc.add(5, -15);// 减去15天
			gc.set(gc.get(Calendar.YEAR), gc.get(Calendar.MONTH), gc.get(Calendar.DATE));
			String todayStr = sf.format(bigDate);
			String dateStr = sf.format(gc.getTime());
			bigDate = sf.parse(todayStr);
			smaDate = sf.parse(dateStr);// 今天减去后的日期
		}
		// 查询15天内有没有数据
		// String hqlDates =
		// "select s.drugCode from Salesitem s where s.cusId=?0 and s.drugPicktime between ?1 and ?2";
		// List dates = salesitemDao.find(hqlDates, cusId, smaDate, bigDate);
		// 统计相加 当前id所有销售单内的 药品价格 乘以 药品数据
		String hql = "select s.drugCode from Salesitem s where s.cusId= ?0 and s.drugPicktime between ?1 and ?2  group by s.drugCode";
		// 定义临时集合存储当前cusId当前日期计算到的总额统计
		list = baseDao.find(hql, cusId, smaDate, bigDate);
		if (list.size() > 10){// 只取10种药品
			list = list.subList(0, 10);
		}
		return list;
	}

	// 根据cusId,编码集合 计算时间条件查询各药品销售金额
	@Override
	public List getEachDrugTotal(String cusId, List drugCodes, int count, String startDate) throws ParseException {
		List list = new ArrayList();
		for (int i = 0; i < drugCodes.size(); i++) {
			Object drugCode = drugCodes.get(i);
			// 定义存放当前日期的totals 集合
			List l = new ArrayList();
			for (int y = 0; y <= count; y++) {
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
				Date date;
				if (startDate != null) {// 有查询条件
					date = sf.parse(startDate);
				} else {
					date = new Date();
				}
				GregorianCalendar gc = new GregorianCalendar();
				gc.setTime(date);
				gc.add(5, -y);
				gc.set(gc.get(Calendar.YEAR), gc.get(Calendar.MONTH), gc.get(Calendar.DATE));
				String dateString = sf.format(gc.getTime());
				date = sf.parse(dateString);
				String hql = "select sum(s.drugSalesprice * s.drugNum) from Salesitem s where s.cusId=?0 and s.drugCode=?1 and s.drugPicktime=?2 ";
				List tem = baseDao.find(hql, cusId, drugCode, date);
				if (tem.get(0) != null) {
					l.add(y, tem.get(0));
				} else if (tem.get(0) == null) {
					l.add(y, 0);
				}
			}
			list.add(i, l);
		}
		return list;
	}

	// 根据cusId,编码集合 计算时间条件查询各药品销售量
	@Override
	public List getEachDrugNumTotal(String cusId, List drugCodes, int count, String startDate) throws ParseException {
		List list = new ArrayList();
		for (int i = 0; i < drugCodes.size(); i++) {
			Object drugCode = drugCodes.get(i);
			// 定义存放当前日期的totals 集合
			List l = new ArrayList();
			for (int y = 0; y <= count; y++) {
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
				Date date;
				if (startDate != null) {// 有查询条件
					date = sf.parse(startDate);
				} else {
					date = new Date();
				}
				GregorianCalendar gc = new GregorianCalendar();
				gc.setTime(date);
				gc.add(5, -y);
				gc.set(gc.get(Calendar.YEAR), gc.get(Calendar.MONTH), gc.get(Calendar.DATE));
				String dateString = sf.format(gc.getTime());
				date = sf.parse(dateString);
				String hql = "select sum(s.drugNum) from Salesitem s where s.cusId=?0 and s.drugCode=?1 and s.drugPicktime=?2 ";
				List tem = baseDao.find(hql, cusId, drugCode, date);
				if (tem.get(0) != null) {
					l.add(y, tem.get(0));
				} else if (tem.get(0) == null) {
					l.add(y, 0);
				}
			}
			list.add(i, l);
		}
		return list;
	}

	// 获取当前cusid 项目名称集合
	@Override
	public List getAllDrugNames(String cusId, List drugCodes) {
		int count = 0;
		List list = new ArrayList();
		if (drugCodes.size() > 0) {
			for (int i = 0; i < drugCodes.size(); i++) {
				Object drugCode = drugCodes.get(i);
				String hql = "select s.drugName from Salesitem s where s.cusId=?0 and s.drugCode=?1 ";
				List temp = baseDao.find(hql, cusId, drugCode);
				if (temp.size() > 0) {
					// 如果查找到编码对应的药品名称才会添加到list中
					list.add(count, temp.get(0));
					count++;
				}
			}
		}
		return list;
	}

}
