package yibao.yiwei.service;

import java.util.List;

@SuppressWarnings("unchecked")
public interface ICustomerManager {

	/**
	 * 
	 * @param hql
	 *            查询id hql
	 * @param cusIds
	 *            客户id字符串
	 * @return 对应的药店名称集合
	 */
	public List getCusNames(String hql, List cusIds);

	/**
	 * @param cusName
	 * @return cusName 对应的cusID
	 */
	public String getCusId(String cusName);

	/**
	 * 
	 * @param cusFlag 
	 * @param CusareaRelate 区划编码
	 * @param hql
	 * @return 所有客户名称列表
	 */
	public List getAllCusName(String cusFlag, String CusareaRelate);


	/**
	 * 
	 * @param cusIds
	 * @param cusFlag
	 * @return 过滤后的cusIds
	 */
	public List getRealCusIds(List cusIds, String cusFlag);
	/**
	 * 根据客户标识过滤list
	 * @param list Object[cusId,dcCode]元素类型
	 * @param cusFlag 客户标识
	 * @return 符合标识条件的结果
	 */
	public List getArrayCusIds(List arrList, String cusFlag);
	/**
	 * 
	 * @param idHql
	 * @param arrList Object[cusId,dcCode]元素类型 集合
	 * @return 对应的药店名称集合
	 */
	public List getArrCusNames(String idHql, List realarrList);

}
