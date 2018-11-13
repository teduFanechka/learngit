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
import yibao.yiwei.service.IWarehouseitemManager;

/**
 * 入库信息
 * @author Administrator
 *
 */
@SuppressWarnings("unchecked")
@Service
@Transactional
public class WarehouseitemManager implements IWarehouseitemManager {
	
	@Autowired
	private IBaseDao baseDao;
	
	/**
	 * @param cusIds
	 * @param days 1天/一周/一月
	 * @return 药品入库数量统计集合
	 */
	@Override
	public List getWIAmountTotals(String cusId, String days, List drugCodes) throws ParseException {
		List list = new ArrayList();
		// 定义存放当前cusId 15天每天的totals 集合
		// 调用方法获取当前cusId 药品编码集合
		if (drugCodes.size() > 0) {
			for (int y = 0; y < drugCodes.size(); y++) {
				Object drugCode = drugCodes.get(y);
				Date today = new Date();
				GregorianCalendar gc = new GregorianCalendar();
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
				gc.setTime(today);
				gc.add(5, -Integer.parseInt(days));// 减去的天数(days)
				gc.set(gc.get(Calendar.YEAR), gc.get(Calendar.MONTH), gc.get(Calendar.DATE));
				String todayStr = sf.format(today);
				String dateStr = sf.format(gc.getTime());
				today = sf.parse(todayStr);
				Date date = sf.parse(dateStr);
				// 定义集合存储当前cusId当前日期当前药品代码下药品数量
				List drugNumList = new ArrayList();
				if (Integer.parseInt(days) == 1) { // 表示取当天
					String hqlDates = "select sum(w.drugNum) from Warehouseitem w where w.cusId=?0 "
							+ " and w.drugCollectdate = ?1 and w.drugCode=?2";
					// 定义获取循环一天 有没有数据
					drugNumList = baseDao.find(hqlDates, cusId, today,
							drugCode);
				} else {// 取一周或一月
					String hqlDates = "select sum(w.drugNum) from Warehouseitem w where w.cusId=?0 "
							+ " and w.drugCollectdate between ?1 and ?2 and w.drugCode=?3";
					drugNumList = baseDao.find(hqlDates, cusId, date, today, drugCode);
				}
				if (drugNumList.size() > 0) {
					list.add(y, drugNumList.get(0));
				}
			}
		}
		return list;
	}

	// 获取当前cusId 对应时间段内药品编码集合去重
	@Override
	public List getgetDrugCodes(String cusId, String days) throws ParseException {
		List list = new ArrayList();
		Date today = new Date();
		GregorianCalendar gc = new GregorianCalendar();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		gc.setTime(today);
		gc.add(5, -Integer.parseInt(days));
		gc.set(gc.get(Calendar.YEAR), gc.get(Calendar.MONTH), gc  .get(Calendar.DATE));
		String todayStr = sf.format(today);
		String dateStr = sf.format(gc.getTime());
		today = sf.parse(todayStr);
		Date date = sf.parse(dateStr);
		// 定义集合存储当前cusId当前日期当前药品代码下药品数量
		if (Integer.parseInt(days) == 1) { // 表示取当天
			String hqlDates = "select w.drugCode from Warehouseitem w where w.cusId=?0 "
					+ " and w.drugCollectdate = ?1 group by w.drugCode";
			// 定义获取循环一天 有没有数据
			list = baseDao.find(hqlDates, cusId, today);
		} else {// 取一周或一月
			String hqlDates = "select w.drugCode from Warehouseitem w where w.cusId=?0 "
					+ " and w.drugCollectdate between ?1 and ?2 group by w.drugCode";
			list = baseDao.find(hqlDates, cusId, date, today);

		}
		return list;
	}

	// 获取当前cusid 项目名称集合
	@Override
	public List getAlldrugNames(String cusId, List drugCodes) {
		int count = 0;
		List list = new ArrayList();
		if (drugCodes.size() > 0) {
			for (int i = 0; i < drugCodes.size(); i++) {
				Object drugCode = drugCodes.get(i);
				String hql = "select w.drugName from Warehouseitem w where w.cusId=?0 and w.drugCode=?1 ";
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
