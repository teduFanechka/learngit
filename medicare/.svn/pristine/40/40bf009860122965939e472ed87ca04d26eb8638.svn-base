package yibao.yiwei.service;

import java.text.ParseException;
import java.util.List;

@SuppressWarnings("unchecked")
public interface IWarehouseitemManager {

	/**
	 * 
	 * @param cusId
	 *            药店编码
	 * @param days
	 *            1天/一周/一月
	 * @return 药品入库数量统计集合
	 */
	public List getWIAmountTotals(String cusId, String days, List drugCodes) throws ParseException;

	/**
	 * 
	 * @param cusId
	 *            药店id
	 * @param days
	 *            一天/周/月
	 * @return 对应时间段内药品编码集合去重
	 * @throws ParseException
	 */
	public List getgetDrugCodes(String cusId, String days) throws ParseException;
	/**
	 * 
	 * @param cusId 定点id
	 * @param drugCodes 项目编码
	 * @return 项目名称list
	 */
	public List getAlldrugNames(String cusId, List drugCodes);
	
}
