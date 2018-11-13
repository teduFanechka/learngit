package yibao.yiwei.service;

import java.text.ParseException;
import java.util.List;

@SuppressWarnings("unchecked")
public interface ISalesitemManager {
	
	/**
	 * 
	 * @param cusIds
	 *            客户id字符串
	 * @param startDate
	 *            要循环相减的起始日期
	 * @param count
	 *            循环的天数
	 * @return 各定点销售总额统计list
	 */
	public List getEachPointTotal(List cusIds, int count, String startDate) throws ParseException;

	/**
	 * 内部只取了10条数据
	 * @param cusId
	 * @param secondDate
	 *            第一个日期
	 * @param firstDate
	 *            第二个日期
	 * @return 对应的药品编码集合
	 * @throws ParseException
	 */
	public List getDrugCodes(String cusId, String firstDate, String secondDate) throws ParseException;

	/**
	 * 
	 * @param cusId
	 *            客户id
	 * @param drugCodes
	 *            药品编码list
	 * @param count
	 *            循环的天数
	 * @param startDate
	 *            要循环相减的起始日期
	 * @return 计算条件查询的各药品销售量
	 * @throws ParseException
	 */
	public List getEachDrugTotal(String cusId, List drugCodes, int count, String startDate) throws ParseException;

	/**
	 * 
	 * @param cusId
	 *            客户id
	 * @param drugCodes
	 *            药品编码list
	 * @param startDate
	 *            要循环相减的起始日期
	 * @param count
	 *            循环的天数
	 * @return 计算条件查询的各药品销售量
	 * @throws ParseException
	 */
	public List getEachDrugNumTotal(String cusId, List drugCodes, int count, String startDate) throws ParseException;

	/**
	 * 获取定点项目名称集合(药品名称)
	 * @param drugCodes
	 * @param cusId
	 * @return list
	 */
	public List getAllDrugNames(String cusId,List drugCodes);

}
