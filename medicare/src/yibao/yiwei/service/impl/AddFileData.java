package yibao.yiwei.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.hibernate.exception.DataException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import yibao.yiwei.dao.IBaseDao;
import yibao.yiwei.entity.Checkdetail;
import yibao.yiwei.entity.Checkresult;
import yibao.yiwei.entity.Clinicrecords;
import yibao.yiwei.entity.Deliveryitem;
import yibao.yiwei.entity.Department;
import yibao.yiwei.entity.Discharged;
import yibao.yiwei.entity.Dischargediag;
import yibao.yiwei.entity.Doctororder;
import yibao.yiwei.entity.Drugcatalog;
import yibao.yiwei.entity.Employee;
import yibao.yiwei.entity.Hospitalized;
import yibao.yiwei.entity.Itemstock;
import yibao.yiwei.entity.Medrecords;
import yibao.yiwei.entity.Operation;
import yibao.yiwei.entity.Orderperform;
import yibao.yiwei.entity.Prescribe;
import yibao.yiwei.entity.Salesitem;
import yibao.yiwei.entity.Supplier;
import yibao.yiwei.entity.Warehouse;
import yibao.yiwei.entity.Warehouseitem;
import yibao.yiwei.entity.system.CusareaRelate;
import yibao.yiwei.entity.system.Customer;
import yibao.yiwei.entity.system.Errorlog;
import yibao.yiwei.entity.wrongInfo.AllWrongInfo;
import yibao.yiwei.entity.wrongInfo.SettingCheck;
import yibao.yiwei.service.IAddFileData;
import yibao.yiwei.service.IBaseService;
import yibao.yiwei.utils.ChineseCharToEn;

@SuppressWarnings("unchecked")
@Service
@Transactional
public class AddFileData implements IAddFileData {

	@Autowired
	private IBaseDao baseDao;
	@Autowired
	private IBaseService baseService;
	
	/**
	 * 解析结果
	 * @param count
	 * @param error
	 * @return
	 */
	public String getInfo(int count, int error){
		String info;
		if(count > 0 && error == 0){
			info = "<font color='green'>全部成功"+count+"条</font>";
		} else if(count ==0 && error > 0){
			info = "<font color='red'>全部失败"+error+"条</font>";
		} else {
			info = "<font color='green'>成功"+count+"条，</font><font color='red'>失败"+error+"条</font>";
		}
		return info;
	}
	
	/**
	 * 检查是否有错误数据，如空值或逻辑错误
	 * @param name 解析出的数据数组
	 * @param setId SETTINGCHECK表主键id
	 */
	private void checkWrongInfo(String[] name, Integer setId, String cusId, String path, String filename){
		String hql = "from SettingCheck where parentId=?0 order by setCode asc";
		List<SettingCheck> list = baseService.find(hql, setId);
		List<Customer> cuss = baseService.find("from Customer where cusId=?0",cusId);
		Customer cus = cuss.get(0);
		AllWrongInfo awi = new AllWrongInfo();
		for(int i=0;i<list.size();i++){
			if(list.get(i).getSetNull()!=0){
				if(name[list.get(i).getSetCode()] == null || name[list.get(i).getSetCode()].equals("0") || name[list.get(i).getSetCode()].equals("")){
					awi.setCusId(cusId);
					awi.setCusPid(cus.getCusParentid());
					awi.setCusDareway(cus.getCusName());
					awi.setWriCode(101);
					awi.setWriName("数据中出现空值或零值");
					awi.setWriFile(filename);
					awi.setWriDescription(list.get(i).getSetName()+"项出现空值或0值!!");
					awi.setWriStatus(0);
					awi.setWriAddtime(new Date());
					awi.setCusContact(cus.getCusContact());
					awi.setCusPhone(cus.getCusPhone());
					awi.setCusAddress(cus.getCusAddr());
					awi.setWriFilepath(path);
					awi.setCusNo(cus.getCusDareway());
					baseDao.save(awi);
				}
			}
			if(list.get(i).getSetPositive()!=0){
				if(Integer.parseInt(name[list.get(i).getSetCode()])<0){
					awi.setCusId(cusId);
					awi.setCusPid(cus.getCusParentid());
					awi.setCusDareway(cus.getCusName());
					awi.setWriCode(101);
					awi.setWriName("数据逻辑错误");
					awi.setWriFile(filename);
					awi.setWriDescription(list.get(i).getSetName()+"项出现小于零的数据!!");
					awi.setWriStatus(0);
					awi.setWriAddtime(new Date());
					awi.setCusContact(cus.getCusContact());
					awi.setCusPhone(cus.getCusPhone());
					awi.setCusAddress(cus.getCusAddr());
					awi.setWriFilepath(path);
					awi.setCusNo(cus.getCusDareway());
					baseDao.save(awi);
				}
			}
		}
	}
	
	/**
	 * 添加项目编码
	 */
	@Override
	public void addDrugcatalog(String path, String cusId, String cusName, Date upCollectdate, int upId,String fileName) throws IOException {
		BufferedReader bufr = null;
		int count = 0;// 定义当行行号
		int error = 0;
		String line = null;
		String[] name = null;// 定义当前行String数组
		Drugcatalog drugcatalog;
		Errorlog errorlog;
		String qsql = "update Uploadfile set isHandle=?0,upCount=?1,upName=?2 where upId=?3";
		int a = 0;
		try {
			File file = new File(path);
			if (file.isFile()) {
				bufr = new BufferedReader(new FileReader(file));
				while ((line = bufr.readLine()) != null) {
					name = line.split("##");
					if (name.length < 26) {// 上传文件解析不正确,可能为数据不全
						throw new DataException("正确字段数:26,实际解析字段数:" + name.length + ",字段不全解析停止", null);
					}
					for (int i = 0; i < name.length; i++) {
						if (null != name[i] && name[i].trim().equals("NULL")){
							name[i] = "";
							//TODO 预警
						}
					}
					try{
						drugcatalog = new Drugcatalog();//项目编码，连锁药店都解析在总店，查询时也是查总店
						DecimalFormat def = new DecimalFormat("#.00");
						drugcatalog.setCusId(cusId);
						drugcatalog.setCusParentid("0");
						drugcatalog.setDcCode(name[0].trim());
						drugcatalog.setDcCategory(name[1].trim());
						drugcatalog.setDcCommercial(name[2].trim());// 商品名 3
						drugcatalog.setDcGenericname(name[3].trim());
						drugcatalog.setDcMnemoniccode(name[4].trim());
						drugcatalog.setDcSpecification(name[5].trim());
						drugcatalog.setDcUnit(name[6].trim());
						drugcatalog.setDcDosageform(name[11].trim());
						drugcatalog.setDcPermission(name[12].trim());
						if(name[13].trim().equals("")){
							drugcatalog.setDcMfrs(name[14].trim());
						} else {
							drugcatalog.setDcMfrs(name[13].trim());
						}
						if(name[14].trim().equals("")){
							drugcatalog.setDcMakein(name[13].trim());
						} else {
							drugcatalog.setDcMakein(name[14].trim());
						}
						drugcatalog.setDcHcscode(name[15].trim());
						drugcatalog.setDcPrice(def.format(Double.parseDouble(name[7].trim())));// 8 最小包装单价(零售) 小数
						drugcatalog.setDcPackagingunit(name[8].trim());// 9 大包装单位 字符串
						drugcatalog.setDcPackagingprice(def.format(Double.parseDouble(name[9].trim())));// 10 大包装单价 小数
						drugcatalog.setDcPackagingnum(name[10].trim());// 11 大包装数量 小数
						drugcatalog.setDcCatcode(name[16].trim());// 17 目录类别 整数
						drugcatalog.setDcSettlementcode(name[17].trim());// 18 结算项目编码
						drugcatalog.setDcIsrx(name[18].trim());// 19 处方药标识
						drugcatalog.setDcIsspecial(name[19].trim());// 20 特药标识
						drugcatalog.setDcIsephedradine(name[20].trim());// 21 含麻黄碱标识
						drugcatalog.setDcIsspirit(name[21].trim());// 22 精神药品标识
						drugcatalog.setDcIsimport(name[22].trim());// 23 进口药标识
						drugcatalog.setDcTaboo(name[23].trim());// 24 禁忌描述
						drugcatalog.setDcIndication(name[24].trim());// 25 适应症
						drugcatalog.setDcStatus(name[25].trim());// 26 状态
						drugcatalog.setCusDareway("");// 医院编码
						drugcatalog.setDcPicktime(upCollectdate);// 采集日期
						drugcatalog.setDcAddtime(new Date());// 创建日期
						if(a<2){
							checkWrongInfo(name, 1001, cusId, path, fileName);
							a++;
						}
						baseDao.save(drugcatalog);
						count++;
					} catch (Exception e) {
						error++;
						if(error <= 2){//只记录文件的2行错误
							String log = e.toString();
							if (log.length() > 300){
								log = log.substring(0, 300);
							}
							if(log.indexOf("input string")!=-1){
								log = "数字解析出错，数字中包含了字符!";
							}
							if(log.indexOf("empty String")!=-1){
								log = "数字解析出错，存在空值!";
							}
							if(log.indexOf("Unparseable")!=-1){
								log = "日期解析出错，日期格式不正确!";
							}
							errorlog = new Errorlog();
							errorlog.setCusId(cusId);
							errorlog.setCusName(cusName);
							errorlog.setErrAddtime(new Date());
							errorlog.setErrFileflag("项目编码");
							errorlog.setErrFilepath(path);
							errorlog.setErrLog(log + "  \n错误行号:" + (count+error));
							errorlog.setErrType("数据解析错误");
							baseDao.save(errorlog);
						}
					}
				}
				if(count ==0 && error > 0){
					baseDao.updateOrDelete(qsql, -1,0,getInfo(count, error),upId);//全部失败
				} else {
					baseDao.updateOrDelete(qsql, 1,count,getInfo(count, error),upId);//全部成功/有成功有失败
				}
			} else {
				baseDao.updateOrDelete(qsql, -1, 0,"<font color='red'>文件不存在，请使用服务器地址解析</font>", upId);
			}
		} catch (Exception e) {
			String log = e.toString();
			if (log.length() > 300){
				log = log.substring(0, 300);
			}
			errorlog = new Errorlog();
			errorlog.setCusId(cusId);
			errorlog.setCusName(cusName);
			errorlog.setErrAddtime(new Date());
			errorlog.setErrFileflag("项目编码");
			errorlog.setErrFilepath(fileName);
			if (null !=name && name.length < 26) {
				errorlog.setErrType("数据上传字段不全错误");
				errorlog.setErrLog(log);
				baseDao.updateOrDelete(qsql,-1,0,"<font color='red'>数据上传字段不全错误,正确字段数:26,实际解析字段数:" + name.length + "</font>", upId);
			} else {
				errorlog.setErrType("数据解析错误");
				errorlog.setErrLog(log + "\n错误行号:" + (count+1) + "  当前错误行内容:\n" + line);
				baseDao.updateOrDelete(qsql,-1,0,"<font color='red'>数据解析错误</font>", upId);
			}
			baseDao.save(errorlog);
		} finally {
			if (null != bufr) {
				bufr.close();
			}
		}
	}
	
	/**
	 * 添加供应商/生产商信息
	 */
	@Override
	public void addSuppliers(String path, String cusId, String cusName, Date upCollectdate, int upId,String fileName) throws IOException {
		BufferedReader bufr = null;
		int count = 0;// 定义当行行号
		int error = 0;
		String line = null;// 定义当前行
		String[] name = null;// 定义当前行String数组
		Supplier supplier;
		String qsql = "update Uploadfile set isHandle=?0,upCount=?1,upName=?2 where upId=?3";
		Errorlog errorlog;
		int a = 0;
		try {
			File file = new File(path);
			if (file.isFile()) {
				bufr = new BufferedReader(new FileReader(file));
				while ((line = bufr.readLine()) != null) {
					name = line.split("##");
					if (name.length < 14) {
						throw new DataException("正确字段数:14,实际解析字段数:" + name.length + ",字段不全解析停止", null);
					}
					for (int i = 0; i < name.length; i++) {
						if (null != name[i] && name[i].trim().equals("NULL")){
							name[i] = "";
						}
					}
					try{
						supplier = new Supplier();//供应商/生产商，连锁药店都解析在总店，查询时也是查总店
						supplier.setCusId(cusId);
						supplier.setCusParentid("0");
						supplier.setSpCode(name[0].trim());
						supplier.setSpName(name[1].trim());
						supplier.setSpContact(name[2].trim());
						supplier.setSpPhone(name[3].trim());
						supplier.setSpCertificateno(name[4].trim());
						supplier.setSpQuality(name[5].trim());
						supplier.setSpAnnex(name[6].trim());
						supplier.setSpRemark("");
						supplier.setSpCat(name[6].trim());// 7 企业类别
						supplier.setSpPostcode(name[7].trim());// 8 邮政编码
						supplier.setSpEmail(name[8].trim());// 9 电子邮件
						supplier.setSpAddress(name[9].trim());// 10 详细地址
						supplier.setSpFax(name[10].trim());// 11 企业传真
						supplier.setSpStatus(name[13].trim());// 14 状态
						supplier.setCusDareway("");// 医院编码
						supplier.setAcCode("");// 统筹区划编码
						supplier.setAcName("");// 统筹区划名称
						supplier.setSpPicktime(upCollectdate);// 采集时间
						supplier.setSpAddtime(new Date());// 创建时间
						if(a<2){
							checkWrongInfo(name, 1002, cusId, path, fileName);
							a++;
						}
						baseDao.save(supplier);
						count++;
					} catch (Exception e) {
						error++;
						if(error <= 2){//只记录文件的2行错误
							String log = e.toString();
							if (log.length() > 300){
								log = log.substring(0, 300);
							}
							if(log.indexOf("input string")!=-1){
								log = "数字解析出错，数字中包含了字符!";
							}
							if(log.indexOf("empty String")!=-1){
								log = "数字解析出错，存在空值!";
							}
							if(log.indexOf("Unparseable")!=-1){
								log = "日期解析出错，日期格式不正确!";
							}
							errorlog = new Errorlog();
							errorlog.setCusId(cusId);
							errorlog.setCusName(cusName);
							errorlog.setErrAddtime(new Date());
							errorlog.setErrFileflag("供应商/生产商");
							errorlog.setErrFilepath(path);
							errorlog.setErrLog(log + "  \n错误行号:" + (count+error));
							errorlog.setErrType("数据解析错误");
							baseDao.save(errorlog);
						}
					}
				}
				if(count ==0 && error > 0){
					baseDao.updateOrDelete(qsql, -1,0,getInfo(count, error),upId);//全部失败
				} else {
					baseDao.updateOrDelete(qsql, 1,count,getInfo(count, error),upId);//全部成功/有成功有失败
				}
			} else {
				baseDao.updateOrDelete(qsql,-1, 0,"<font color='red'>文件不存在，请使用服务器地址解析</font>", upId);
			}
		} catch (Exception e) {
			String log = e.toString();
			if (log.length() > 300){
				log = log.substring(0, 300);
			}
			errorlog = new Errorlog();
			errorlog.setCusId(cusId);
			errorlog.setCusName(cusName);
			errorlog.setErrAddtime(new Date());
			errorlog.setErrFileflag("供应商/生产商");
			errorlog.setErrFilepath(fileName);
			if (null !=name && name.length < 14) {
				errorlog.setErrType("数据上传字段不全错误");
				errorlog.setErrLog(log);
				baseDao.updateOrDelete(qsql,-1,0,"<font color='red'>数据上传字段不全错误,正确字段数:14,实际解析字段数:" + name.length + "</font>", upId);
			} else {
				errorlog.setErrType("数据解析错误");
				errorlog.setErrLog(log + "\n错误行号:" + (count+1) + "  当前错误行内容:\n" + line);
				baseDao.updateOrDelete(qsql,-1,0,"<font color='red'>数据解析错误</font>", upId);
			}
			baseDao.save(errorlog);
		} finally {
			if (null != bufr) {
				bufr.close();
			}
		}
	}
	
	/**
	 * 添加医护人员信息
	 */
	@Override
	public void addEmployees(String path, String cusId, String cusName,Date upCollectdate, int upId,String cusFlag,String fileName) throws IOException {
		BufferedReader bufr = null;
		int count = 0;// 定义当行行号
		int error = 0;
		String line = null;// 定义当前行
		String[] name = null;// 定义当前行String数组
		Employee employee;
		Errorlog errorlog;
		String qsql = "update Uploadfile set isHandle=?0,upCount=?1,upName=?2 where upId=?3";
		int a = 0;
		try {
			File file = new File(path);
			if (file.isFile()) {
				bufr = new BufferedReader(new FileReader(file));
				while ((line = bufr.readLine()) != null) {
					name = line.split("##");
					if (name.length < 22) {// 上传文件解析不正确,可能为数据不全
						throw new DataException("正确字段数:22,实际解析字段数:" + name.length + ",字段不全解析停止", null);
					}
					for (int i = 0; i < name.length; i++) {
						if (null != name[i] && name[i].trim().equals("NULL")){
							name[i] = "";
						}
					}
					try{
						employee = new Employee();
						//判断定点是否连锁,数据解析至分店cusid下开始,判断定点类型是连锁药店并且上传有最后一位分店编码
						if (cusFlag.equals("102") && name.length > 22) {// length:23
							// 分店标识为零,则保存在总店id下
							if (name[22].trim().equals("0")) {
								employee.setCusId(cusId);
							} else {
								// 查找分店id,父id是当前cusid,分店编码为name[]最后一位
								String hql = "select cusId from Customer where cusParentid =?0 and cusBranchcode=?1 and cusStatus !=-1";
								List list = baseDao.find(hql, cusId, name[22].trim());
								if (list.size() > 0) {
									// 分店已注册查找到分店id
									employee.setCusId(list.get(0).toString());
								} else {// 如果当前分店编码未注册,执行下次循环
									error++;
									continue;
								}
							}
						} else {
							employee.setCusId(cusId);
						}
						employee.setCusParentid("0");
						employee.setEmCode(name[0].trim());
						employee.setEmName(name[1].trim());
						employee.setEmSex(name[2].trim());
						employee.setEmIdnum(name[3].trim());
						employee.setEmTitlename(name[5].trim());
						employee.setEmQualification(name[14].trim());
						employee.setEmPhone(name[15].trim());
						employee.setEmAnnex(name[20].trim());
						employee.setEmRemark(name[21].trim());
						employee.setEmPicktime(upCollectdate);// 采集时间
						employee.setEmAddtime(new Date());// 创建时间
						employee.setAcCode("");// 统筹区编码++
						employee.setAcName("");// 统筹区名称++
						employee.setEmTitlecode(name[4].trim());// 5 职称编码
						employee.setEmJobcode(name[6].trim());// 7 职务编码
						employee.setEmJobname(name[7].trim());// 8 职务名称
						employee.setEmCertified(name[8].trim());// 9 医师资格证编号
						employee.setEmLicence(name[9].trim());// 10 医师执业证编号
						employee.setEmClassifycode(name[10].trim());// 11 执业类别编码
						employee.setEmClassifyname(name[11].trim());// 12 执业类别名称
						employee.setEmScopecode(name[12].trim());// 13 执业范围编码
						employee.setEmScopename(name[13].trim());// 14 执业范围名称
						employee.setDeptCode(name[16].trim());// 17 科室代码
						employee.setDeptName(name[17].trim());// 18 科室名称
						employee.setEmIsexpert(name[18].trim());// 19 是否专家
						employee.setEmStatus(name[19].trim());// 20人员状态
						if(a<2){
							checkWrongInfo(name, 1003, cusId, path, fileName);
							a++;
						}
						baseDao.save(employee);
						count++;
					} catch (Exception e) {
						error++;
						if(error <= 2){//只记录文件的2行错误
							String log = e.toString();
							if (log.length() > 300){
								log = log.substring(0, 300);
							}
							errorlog = new Errorlog();
							errorlog.setCusId(cusId);
							errorlog.setCusName(cusName);
							errorlog.setErrAddtime(new Date());
							errorlog.setErrFileflag("医护人员");
							errorlog.setErrFilepath(fileName);
							errorlog.setErrLog(log + "\n错误行号:" + (count+error) + ",当前错误行内容:\n" + line);
							errorlog.setErrType("数据解析错误");
							baseDao.save(errorlog);
						}
					}
				}
				if(count ==0 && error > 0){
					baseDao.updateOrDelete(qsql, -1,0,getInfo(count, error),upId);//全部失败
				} else {
					baseDao.updateOrDelete(qsql, 1,count,getInfo(count, error),upId);//全部成功/有成功有失败
				}
			} else {
				baseDao.updateOrDelete(qsql,-1,0,"<font color='red'>文件不存在，请使用服务器地址解析</font>", upId);
			}
		} catch (Exception e) {
			String log = e.toString();
			if (log.length() > 300){
				log = log.substring(0, 300);
			}
			errorlog = new Errorlog();
			errorlog.setCusId(cusId);
			errorlog.setCusName(cusName);
			errorlog.setErrAddtime(new Date());
			errorlog.setErrFileflag("医护人员");
			errorlog.setErrFilepath(fileName);
			if (null !=name && name.length < 22) {
				errorlog.setErrType("数据上传字段不全错误");
				errorlog.setErrLog(log);
				baseDao.updateOrDelete(qsql,-1,0,"<font color='red'>数据上传字段不全错误,正确字段数:22,实际解析字段数:" + name.length + "</font>", upId);
			} else {
				errorlog.setErrType("数据解析错误");
				errorlog.setErrLog(log + "\n错误行号:" + (count+1) + "  当前错误行内容:\n" + line);
				baseDao.updateOrDelete(qsql,-1,0,"<font color='red'>数据解析错误</font>", upId);
			}
			baseDao.save(errorlog);
		} finally {
			if (bufr != null) {
				bufr.close();
			}
		}
	}
	
	/**
	 * 添加入库信息
	 */
	@Override
	public void addWarehouseitems(String path, String cusId, String cusName, Date upCollectdate, int upId, String cusFlag,String fileName) throws IOException,ParseException {
		BufferedReader bufr = null;
		int count = 0;// 定义当行行号
		int error = 0;
		String line = null;// 定义当前行
		String[] name = null;// 定义当前行String数组
		Warehouseitem warehouseitem;
		Customer customer;
		Errorlog errorlog;
		String qsql = "update Uploadfile set isHandle=?0,upCount=?1,upName=?2 where upId=?3";
		int a = 0;
		try {
			File file = new File(path);
			if (file.isFile()) {
				bufr = new BufferedReader(new FileReader(file));
				while ((line = bufr.readLine()) != null) {
					name = line.split("##");
					if (name.length < 24) {// 上传文件解析不正确,可能为数据不全
						throw new DataException("正确字段数:24,实际解析字段数:" + name.length + ",字段不全解析停止", null);
					}
					for (int i = 0; i < name.length; i++) {
						if (null != name[i] && name[i].trim().equals("NULL")){
							name[i] = "";
						}
					}
					try{
						String yybm;//医院编码
						String parentid = "";// 父id
						warehouseitem = new Warehouseitem();
						//判断定点是否连锁,数据解析至分店cusid下
						// 判断定点类型是连锁药店并且上传有最后一位分店编码
						if (cusFlag.equals("102") && name.length > 24) {// length:25
							// 分店标识为零,则保存在总店id下
							if (name[24].trim().equals("0")) {
								warehouseitem.setCusId(cusId);
								customer = (Customer)baseDao.get(Customer.class, cusId);
								yybm = customer.getCusDareway();
							} else {
								// 查找分店id,父id是当前cusid,分店编码为name[]最后一位
								String hql = "select cusId from Customer where cusParentid =?0 and cusBranchcode=?1 and cusStatus !=-1";
								List list = baseDao.find(hql, cusId, name[24].trim());
								if (list.size() > 0) {
									// 分店已注册查找到分店id
									warehouseitem.setCusId(list.get(0).toString());
									// 根据分店cusid查找对应的医院编码
									customer = (Customer)baseDao.get(Customer.class, list.get(0).toString());
									yybm = customer.getCusDareway();
									parentid = cusId;
								} else {// 如果当前分店编码未注册,执行下次循环
									error++;
									continue;
								}
							}
						} else {// 最后一个##后为空则length=24
							warehouseitem.setCusId(cusId);
							customer = (Customer)baseDao.get(Customer.class, cusId);
							yybm = customer.getCusDareway();
						}
						DecimalFormat def = new DecimalFormat("#.00");
						warehouseitem.setCusDareway(yybm);// 医院编码
						warehouseitem.setCusParentid(parentid);
						warehouseitem.setDrugCode(name[3].trim());// 项目编码(药品编码) 4
						if (name[5].trim().equals("NULL") || name[5].trim().equals("")) {
							warehouseitem.setDrugNum(0.0);// 数量(入库) 6
						} else {
							warehouseitem.setDrugNum(Double.parseDouble(def.format(Double.parseDouble(name[5].trim()))));
						}
						if (name[6].trim().equals("NULL") || name[6].trim().equals("")) {
							warehouseitem.setDrugPurchaseprice(0.0);// 采购价 7
						} else {
							warehouseitem.setDrugPurchaseprice(Double.parseDouble(def.format(Double.parseDouble(name[6].trim()))));
						}
						warehouseitem.setDrugBatchno(name[7].trim());// 生产批次 8
						warehouseitem.setDrugMfrs(name[8].trim());// 生产商 9
						warehouseitem.setDrugMadein(name[9].trim());// 生产地 10
						DateFormat df;
						if (name[10].trim().equals("NULL") || name[10].trim().equals("") || name[10].trim().equals("1900-1-1")) {
							warehouseitem.setDrugExpdate(null);
						} else {
							if (name[10].trim().length() == 10) {
								df = new SimpleDateFormat("yyyy-MM-dd");
							} else if(name[10].trim().length() == 18){
								df = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
							} else if(name[10].trim().length() == 17){
								df = new SimpleDateFormat("dd-MMM-yyHH:mm:ss",Locale.ENGLISH);
							} else {
								df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							}
							warehouseitem.setDrugExpdate(df.parse(name[10].trim()));// 有效期 11
						}
						if (name[11].trim().equals("NULL") || name[11].trim().equals("") || name[11].trim().equals("1900-1-1")) {
							warehouseitem.setDrugMfgdate(null);
						} else {
							if (name[11].trim().length() == 10) {
								df = new SimpleDateFormat("yyyy-MM-dd");
							} else if(name[11].trim().length() == 18){
								df = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
							} else if(name[11].trim().length() == 17){
								df = new SimpleDateFormat("dd-MMM-yyHH:mm:ss",Locale.ENGLISH);
							} else {
								df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							}
							warehouseitem.setDrugMfgdate(df.parse(name[11].trim()));// 生产日期 12
						}
						warehouseitem.setDrugBuyer(name[20].trim());// 采购员编码 21
						warehouseitem.setDrugExaminer(name[22].trim());// 验收员编码 23
						warehouseitem.setDrugEid(name[12].trim());// 电子监管码 13
						warehouseitem.setDrugCollectdate(upCollectdate);// 采集日期， 药品采集时间=参数 文件解析时间
						warehouseitem.setWiCode(name[0].trim());// 入库单号1
						if (name[1].trim().equals("NULL") || name[1].trim().equals("") || name[1].trim().equals("1900-1-1")) {
							warehouseitem.setWiDatetime(null);
						} else {
							if (name[1].trim().length() == 10) {
								df = new SimpleDateFormat("yyyy-MM-dd");
							} else if(name[1].trim().length() == 18){
								df = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
							} else if(name[1].trim().length() == 17){
								df = new SimpleDateFormat("dd-MMM-yyHH:mm:ss",Locale.ENGLISH);
							} else {
								df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							}
							warehouseitem.setWiDatetime(df.parse(name[1].trim()));// 入库日期2
						}
						warehouseitem.setWiType(name[2].trim());// 入库类别3
						warehouseitem.setDrugName(name[4].trim());// 项目名称(药品名称)5
						if (name[13].trim().equals("NULL") || name[13].trim().equals("")) {// 零售价14
							warehouseitem.setWiPrice(0.0);
						} else {
							warehouseitem.setWiPrice(Double.parseDouble(def.format(Double.parseDouble(name[13].trim()))));
						}
						warehouseitem.setDrugSpecification(name[14].trim());// 规格15
						warehouseitem.setDrugUnit(name[15].trim());// 单位16
						warehouseitem.setDrugHcscode(name[16].trim());// 医保编码17
						warehouseitem.setWiWhcode(name[17].trim());// 仓库编码18
						warehouseitem.setWiWhname(name[18].trim());// 仓库名称19
						warehouseitem.setWiLocation(name[19].trim());// 货位/货架号20
						warehouseitem.setDrugBuyername(name[21].trim());// 采购员姓名22
						warehouseitem.setDrugExaminername(name[23].trim());// 验收员姓名24
						warehouseitem.setWiAddtime(new Date());// 创建时间
						if(a<2){
							checkWrongInfo(name, 1004, cusId, path, fileName);
							a++;
						}
						baseDao.save(warehouseitem);
						count++;
					} catch (Exception e) {
						error++;
						if(error <= 2){//只记录文件的2行错误
							String log = e.toString();
							if (log.length() > 300){
								log = log.substring(0, 300);
							}
							if(log.indexOf("input string")!=-1){
								log = "数字解析出错，数字中包含了字符!";
							}
							if(log.indexOf("empty String")!=-1){
								log = "数字解析出错，存在空值!";
							}
							if(log.indexOf("Unparseable")!=-1){
								log = "日期解析出错，日期格式不正确!";
							}
							errorlog = new Errorlog();
							errorlog.setCusId(cusId);
							errorlog.setCusName(cusName);
							errorlog.setErrAddtime(new Date());
							errorlog.setErrFileflag("入库信息");
							errorlog.setErrFilepath(path);
							errorlog.setErrLog(log + "  \n错误行号:" + (count+error));
							errorlog.setErrType("数据解析错误");
							baseDao.save(errorlog);
						}
					}
				}
				if(count ==0 && error > 0){
					baseDao.updateOrDelete(qsql, -1,0,getInfo(count, error),upId);//全部失败
				} else {
					baseDao.updateOrDelete(qsql, 1,count,getInfo(count, error),upId);//全部成功/有成功有失败
				}
			} else {
				baseDao.updateOrDelete(qsql,-1,0,"<font color='red'>文件不存在，请使用服务器地址解析</font>", upId);
			}
		} catch (Exception e) {
			String log = e.toString();
			if (log.length() > 300){
				log = log.substring(0, 300);
			}
			errorlog = new Errorlog();
			errorlog.setCusId(cusId);
			errorlog.setCusName(cusName);
			errorlog.setErrAddtime(new Date());
			errorlog.setErrFileflag("入库信息");
			errorlog.setErrFilepath(fileName);
			if (null !=name && name.length < 24) {
				errorlog.setErrType("数据上传字段不全错误");
				errorlog.setErrLog(log);
				baseDao.updateOrDelete(qsql,-1,0,"<font color='red'>数据上传字段不全错误,正确字段数:24,实际解析字段数:" + name.length + "</font>", upId);
			} else {
				errorlog.setErrType("数据解析错误");
				errorlog.setErrLog(log + "\n错误行号:" + (count+1) + "  当前错误行内容:\n" + line);
				baseDao.updateOrDelete(qsql,-1,0,"<font color='red'>数据解析错误</font>", upId);
			}
			baseDao.save(errorlog);
		} finally {
			if (bufr != null) {
				bufr.close();
			}
		}
	}
	
	/**
	 * 添加出库信息
	 */
	@Override
	public void addDeliveryitems(String path, String cusId,String cusName, Date upCollectdate, int upId, String cusFlag,String fileName) throws IOException, ParseException {
		BufferedReader bufr = null;
		int count = 0;// 定义当行行号
		int error = 0;
		String line = null;// 定义当前行
		String[] name = null;// 定义当前行String数组
		Deliveryitem deliveryitem;
		Errorlog errorlog;
		String qsql = "update Uploadfile set isHandle=?0,upCount=?1,upName=?2 where upId=?3";
		int a = 0;
		try {
			File file = new File(path);
			if (file.isFile()) {
				bufr = new BufferedReader(new FileReader(file));
				while ((line = bufr.readLine()) != null) {
					name = line.split("##");
					// 上传文件解析不正确,可能为数据不全
					if (name.length < 21) {
						throw new DataException("正确字段数:21,实际解析字段数:" + name.length + ",字段不全解析停止", null);
					}
					for (int i = 0; i < name.length; i++) {
						if (null != name[i] && name[i].trim().equals("NULL")){
							name[i] = "";
						}
					}
					try{
						//判断定点是否连锁,数据解析至分店cusid下
						// 判断定点类型是连锁药店并且上传有最后一位分店编码
						deliveryitem = new Deliveryitem();
						if (cusFlag.equals("102") && name.length > 21) {// length:25
							String branchcode = name[21].trim();
							// 分店标识为零,则保存在总店id下
							if (branchcode.equals("0")) {
								deliveryitem.setCusId(cusId);
							} else {
								// 查找分店id,父id是当前cusid,分店编码为name[]最后一位
								String hql = "select cusId from Customer where cusParentid =?0 and cusBranchcode=?1 and cusStatus !=-1";
								List list = baseDao.find(hql, cusId, branchcode);
								if (list.size() > 0) {
									// 分店已注册查找到分店id
									deliveryitem.setCusId(list.get(0).toString());
								} else {// 如果当前分店编码未注册,执行下次循环
									error++;
									continue;
								}
							}
						} else {
							deliveryitem.setCusId(cusId);
						}
						DecimalFormat def = new DecimalFormat("#.00");
						deliveryitem.setCusParentid("0");
						deliveryitem.setDiNo(name[0].trim());// 出库编号 1
						if (name[5].trim().equals("NULL") || name[5].trim().equals("")) {
							deliveryitem.setDrugNum(0.0);
						} else {
							deliveryitem.setDrugNum(Double.parseDouble(def.format(Double.parseDouble(name[5].trim()))));// 数量(出库 )6
						}
						DateFormat df;
						if (name[11].trim().equals("NULL") || name[11].trim().equals("") || name[11].trim().equals("1900-1-1")) {
							deliveryitem.setDrugMfgdate(null);
						} else {
							if (name[11].trim().length() == 10) {
								df = new SimpleDateFormat("yyyy-MM-dd");
							} else if(name[11].trim().length() == 18){
								df = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
							} else if(name[11].trim().length() == 17){
								df = new SimpleDateFormat("dd-MMM-yyHH:mm:ss",Locale.ENGLISH);
							} else {
								df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							}
							deliveryitem.setDrugMfgdate(df.parse(name[11].trim()));// 生产日期 12
						}
						if (name[10].trim().equals("NULL") || name[10].trim().equals("") || name[10].trim().equals("1900-1-1")) {
							deliveryitem.setDrugExpdate(null);
						} else {
							if (name[10].trim().length() == 10) {
								df = new SimpleDateFormat("yyyy-MM-dd");
							} else if(name[10].trim().length() == 18){
								df = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
							} else if(name[10].trim().length() == 17){
								df = new SimpleDateFormat("dd-MMM-yyHH:mm:ss",Locale.ENGLISH);
							} else {
								df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							}
							deliveryitem.setDrugExpdate(df.parse(name[10].trim()));// 有效期 11
						}
						deliveryitem.setDiReason(name[2].trim());// 出库原因 3
						deliveryitem.setDrugCode(name[3].trim());// 项目编码(药品编码) 4
						deliveryitem.setDrugBatchno(name[7].trim());// 生产批次 8
						deliveryitem.setDrugMfrs(name[8].trim());// 生产商 9
						deliveryitem.setDrugMadein(name[9].trim());// 药品产地 10
						deliveryitem.setDrugEid(name[12].trim());// 电子监管码 13
						deliveryitem.setDiType(name[1].trim());// 出库类别2
						deliveryitem.setDrugName(name[4].trim());// 项目名称(药品名称)5
						deliveryitem.setDiSpecification(name[6].trim());// 规格7
						deliveryitem.setDiHcscode(name[13].trim());// 医保编码14
						deliveryitem.setDiWhcode(name[14].trim());// 仓库编码15
						deliveryitem.setDiWhname(name[15].trim());// 仓库名称16
						deliveryitem.setDiLocation(name[16].trim());// 货位/货架号17
						deliveryitem.setDiOpcode(name[17].trim());// 操作员编码18
						deliveryitem.setDiOpname(name[18].trim());// 操作员名称19
						if (name[19].trim().equals("NULL") || name[19].trim().equals("") || name[19].trim().equals("1900-1-1")) {
							deliveryitem.setDiOpdatetime(null);
						} else {
							if (name[19].trim().length() == 10) {
								df = new SimpleDateFormat("yyyy-MM-dd");
							} else if(name[19].trim().length() == 18){
								df = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
							} else if(name[19].trim().length() == 17){
								df = new SimpleDateFormat("dd-MMM-yyHH:mm:ss",Locale.ENGLISH);
							} else {
								df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							}
							deliveryitem.setDiOpdatetime(df.parse(name[19].trim()));
						}
						deliveryitem.setDiUnit(name[20].trim());// 单位21
						deliveryitem.setDiPicktime(upCollectdate);// 采集时间
						deliveryitem.setDiAddtime(new Date());// 创建时间
						if(a<2){
							checkWrongInfo(name, 1005, cusId, path, fileName);
							a++;
						}
						baseDao.save(deliveryitem);
						count++;
					} catch (Exception e) {
						error++;
						if(error <= 2){//只记录文件的2行错误
							String log = e.toString();
							if (log.length() > 300){
								log = log.substring(0, 300);
							}
							if(log.indexOf("input string")!=-1){
								log = "数字解析出错，数字中包含了字符!";
							}
							if(log.indexOf("empty String")!=-1){
								log = "数字解析出错，存在空值!";
							}
							if(log.indexOf("Unparseable")!=-1){
								log = "日期解析出错，日期格式不正确!";
							}
							errorlog = new Errorlog();
							errorlog.setCusId(cusId);
							errorlog.setCusName(cusName);
							errorlog.setErrAddtime(new Date());
							errorlog.setErrFileflag("出库信息");
							errorlog.setErrFilepath(path);
							errorlog.setErrLog(log + "  \n错误行号:" + (count+error));
							errorlog.setErrType("数据解析错误");
							baseDao.save(errorlog);
						}
					}
				}
				if(count ==0 && error > 0){
					baseDao.updateOrDelete(qsql, -1,0,getInfo(count, error),upId);//全部失败
				} else {
					baseDao.updateOrDelete(qsql, 1,count,getInfo(count, error),upId);//全部成功/有成功有失败
				}
			} else {
				baseDao.updateOrDelete(qsql,-1,0,"<font color='red'>文件不存在，请使用服务器地址解析</font>", upId);
			}
		} catch (Exception e) {
			String log = e.toString();
			if (log.length() > 300){
				log = log.substring(0, 300);
			}
			errorlog = new Errorlog();
			errorlog.setCusId(cusId);
			errorlog.setCusName(cusName);
			errorlog.setErrAddtime(new Date());
			errorlog.setErrFileflag("出库信息");
			errorlog.setErrFilepath(fileName);
			if (null !=name && name.length < 21) {
				errorlog.setErrType("数据上传字段不全错误");
				errorlog.setErrLog(log);
				baseDao.updateOrDelete(qsql,-1,0,"<font color='red'>数据上传字段不全错误,正确字段数:21,实际解析字段数:" + name.length + "</font>", upId);
			} else {
				errorlog.setErrType("数据解析错误");
				errorlog.setErrLog(log + "\n错误行号:" + (count+1) + "  当前错误行内容:\n" + line);
				baseDao.updateOrDelete(qsql,-1,0,"<font color='red'>数据解析错误</font>", upId);
			}
			baseDao.save(errorlog);
		} finally {
			if (bufr != null) {
				bufr.close();
			}
		}
	}
	
	/**
	 * 添加库存信息
	 */
	@Override
	public void addItemstock(String path, String cusId, String cusName, Date upCollectdate, int upId, String cusFlag,String fileName) throws IOException {
		BufferedReader bufr = null;
		int count = 0;// 定义当行行号
		int error = 0;
		String line = null;// 定义当前行
		String[] name = null;// 定义当前行String数组
		Itemstock itemstock;
		Errorlog errorlog;
		String qsql = "update Uploadfile set isHandle=?0,upCount=?1,upName=?2 where upId=?3";
		int a = 0;
		try {
			File file = new File(path);
			if (file.isFile()) {
				bufr = new BufferedReader(new FileReader(file));
				while ((line = bufr.readLine()) != null) {
					name = line.split("##");
					// 上传文件解析不正确,可能为数据不全
					if (name.length < 15) {
						throw new DataException("正确字段数:15,实际解析字段数:" + name.length + ",字段不全解析停止", null);
					}
					for (int i = 0; i < name.length; i++) {
						if (null != name[i] && name[i].trim().equals("NULL")){
							name[i] = "";
						}
					}
					try{
						//判断定点是否连锁,数据解析至分店cusid下
						// 判断定点类型是连锁药店并且上传有最后一位分店编码
						itemstock = new Itemstock();
						if (cusFlag.equals("102") && name.length > 15) {// length:25
							// 分店标识为零,则保存在总店id下
							if (name[15].trim().equals("0")) {
								itemstock.setCusId(cusId);
							} else {
								// 查找分店id,父id是当前cusid,分店编码为name[]最后一位
								String hql = "select cusId from Customer where cusParentid =?0 and cusBranchcode=?1 and cusStatus !=-1";
								List list = baseDao.find(hql, cusId, name[15].trim());
								if (list.size() > 0) {
									// 分店已注册查找到分店id
									itemstock.setCusId(list.get(0).toString());
								} else {// 如果当前分店编码未注册,执行下次循环
									error++;
									continue;
								}
							}
						} else {
							itemstock.setCusId(cusId);
						}
						DecimalFormat def = new DecimalFormat("#.00");
						itemstock.setCusDareway(""); // 医院编码
						itemstock.setCusPid("");// 客户上级id
						itemstock.setItemAddtime(new Date());// 创建日期
						itemstock.setItemPicktime(upCollectdate);// 采集日期从传来的文件采集时间获取
						itemstock.setItemCode(name[0].trim());// 项目编码1
						itemstock.setItemName(name[1].trim());// 项目名称2
						if (name[2].trim().equals("NULL") || name[2].trim().equals("")) {// 数量3
							itemstock.setItemNum(0.0);
						} else {
							itemstock.setItemNum(Double.parseDouble(def.format(Double.parseDouble(name[2].trim()))));
						}
						itemstock.setItemUnit(name[3].trim());// 单位4
						itemstock.setItemSpecification(name[4].trim());// 规格5
						itemstock.setItemBatchno(name[5].trim());// 批号6
						itemstock.setItemPermission(name[6].trim());// 批准文号7
						DateFormat df;
						if (name[7].trim().equals("NULL") || name[7].trim().equals("") || name[7].trim().equals("1900-1-1")) {
							itemstock.setItemMfgdate(null);
						} else {
							if (name[7].trim().length() == 10) {
								df = new SimpleDateFormat("yyyy-MM-dd");
							} else if(name[7].trim().length() == 18){
								df = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
							} else if(name[7].trim().length() == 17){
								df = new SimpleDateFormat("dd-MMM-yyHH:mm:ss",Locale.ENGLISH);
							} else {
								df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							}
							itemstock.setItemMfgdate(df.parse(name[7].trim()));// 生产日期8
						}
						if (name[8].trim().equals("NULL") || name[8].trim().equals("") || name[8].trim().equals("1900-1-1")) {
							itemstock.setItemExpdate(null);
						} else {
							if (name[8].trim().length() == 10) {
								df = new SimpleDateFormat("yyyy-MM-dd");
							} else if(name[8].trim().length() == 18){
								df = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
							} else if(name[8].trim().length() == 17){
								df = new SimpleDateFormat("dd-MMM-yyHH:mm:ss",Locale.ENGLISH);
							} else {
								df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							}
							itemstock.setItemExpdate(df.parse(name[8].trim()));// 有效期9
						}
						itemstock.setItemMfrs(name[9].trim());// 生产商10
						itemstock.setItemMakein(name[10].trim());// 生产地11
						itemstock.setItemHcscode(name[11].trim());// 医保编码12
						itemstock.setItemWhcode(name[12].trim());// 仓库编码13
						itemstock.setItemWhname(name[13].trim());// 仓库名称14
						itemstock.setItemLocation(name[14].trim());// 货位/货架号15
						if(a<2){
							checkWrongInfo(name, 1006, cusId, path, fileName);
							a++;
						}
						baseDao.save(itemstock);
						count++;
					} catch (Exception e) {
						error++;
						if(error <= 2){//只记录文件的2行错误
							String log = e.toString();
							if (log.length() > 300){
								log = log.substring(0, 300);
							}
							if(log.indexOf("input string")!=-1){
								log = "数字解析出错，数字中包含了字符!";
							}
							if(log.indexOf("empty String")!=-1){
								log = "数字解析出错，存在空值!";
							}
							if(log.indexOf("Unparseable")!=-1){
								log = "日期解析出错，日期格式不正确!";
							}
							errorlog = new Errorlog();
							errorlog.setCusId(cusId);
							errorlog.setCusName(cusName);
							errorlog.setErrAddtime(new Date());
							errorlog.setErrFileflag("库存信息");
							errorlog.setErrFilepath(path);
							errorlog.setErrLog(log + "  \n错误行号:" + (count+error));
							errorlog.setErrType("数据解析错误");
							baseDao.save(errorlog);
						}
					}
				}
				if(count ==0 && error > 0){
					baseDao.updateOrDelete(qsql, -1,0,getInfo(count, error),upId);//全部失败
				} else {
					baseDao.updateOrDelete(qsql, 1,count,getInfo(count, error),upId);//全部成功/有成功有失败
				}
			} else {
				baseDao.updateOrDelete(qsql,-1,0,"<font color='red'>文件不存在，请使用服务器地址解析</font>", upId);
			}
		} catch (Exception e) {
			String log = e.toString();
			if (log.length() > 300){
				log = log.substring(0, 300);
			}
			errorlog = new Errorlog();
			errorlog.setCusId(cusId);
			errorlog.setCusName(cusName);
			errorlog.setErrAddtime(new Date());
			errorlog.setErrFileflag("库存信息");
			errorlog.setErrFilepath(fileName);
			if (null !=name && name.length < 15) {
				errorlog.setErrType("数据上传字段不全错误");
				errorlog.setErrLog(log);
				baseDao.updateOrDelete(qsql,-1,0,"<font color='red'>数据上传字段不全错误,正确字段数:15,实际解析字段数:" + name.length + "</font>", upId);
			} else {
				errorlog.setErrType("数据解析错误");
				errorlog.setErrLog(log + "\n错误行号:" + (count+1) + "  当前错误行内容:\n" + line);
				baseDao.updateOrDelete(qsql,-1,0,"<font color='red'>数据解析错误</font>", upId);
			}
			baseDao.save(errorlog);
		} finally {
			if (bufr != null) {
				bufr.close();
			}
		}
	}
	
	/**
	 * 添加销售信息
	 */
	@Override
	public void addSalesitems(String path, String cusId, String cusName, Date upCollectdate, int upId, String cusFlag,String fileName) throws IOException, FileNotFoundException, ParseException {
		BufferedReader bufr = null;
		int count = 0;// 定义当行行号
		int error = 0;
		String line = null;// 定义当前行
		String[] name = null;// 定义当前行String数组
		Salesitem salesitem;
		Customer customer;
		Errorlog errorlog;
		String qsql = "update Uploadfile set isHandle=?0,upCount=?1,upName=?2 where upId=?3";
		int a = 0;
		try {
			File file = new File(path);
			if (file.isFile()) {
				bufr = new BufferedReader(new FileReader(file));
				while ((line = bufr.readLine()) != null) {
					String yybm = "", parentid = "";// 定义医院编码与父id
					name = line.split("##");
					// 上传文件解析不正确,可能为数据不全
					if (name.length < 24) {
						throw new DataException("正确字段数:24,实际解析字段数:" + name.length + ",字段不全解析停止", null);
					}
					for (int i = 0; i < name.length; i++) {
						if (null != name[i] && name[i].trim().equals("NULL")){
							name[i] = "";
						}
					}
					try{
						//判断定点是否连锁,数据解析至分店cusid下
						salesitem = new Salesitem();
						// 判断定点类型是连锁药店并且上传有最后一位分店编码
						if (cusFlag.equals("102") && name.length > 24) {// length:25
							String branchcode = name[24].trim();
							// 分店标识为零,则保存在总店id下
							if (branchcode.equals("0")) {
								salesitem.setCusId(cusId);
								customer = (Customer)baseDao.get(Customer.class, cusId);
								yybm = customer.getCusDareway();
							} else {
								// 查找分店id,父id是当前cusid,分店编码为name[]最后一位
								String hql = "select cusId from Customer where cusParentid =?0 and cusBranchcode=?1 and cusStatus !=-1";
								// 注意双重事物提交
								List list = baseDao.find(hql, cusId, branchcode);
								if (list.size() > 0) {
									// 分店已注册查找到分店id
									salesitem.setCusId(list.get(0).toString());
									// 根据分店cusid查找对应的医院编码
									customer = (Customer)baseDao.get(Customer.class, list.get(0).toString());
									yybm = customer.getCusDareway();
									parentid = cusId;
								} else {// 如果当前分店编码未注册,执行下次循环
									error++;
									continue;
								}
							}
						} else {
							salesitem.setCusId(cusId);
							customer = (Customer)baseDao.get(Customer.class, cusId);
							yybm = customer.getCusDareway();
						}
						DecimalFormat def = new DecimalFormat("#.00");
						salesitem.setCusDareway(yybm);// 医院编码
						salesitem.setCusParentid(parentid);// 客户上级id
						salesitem.setSoNo(name[0].trim());// 销售单号 1
						salesitem.setDrugCode(name[6].trim());// 药品编码(项目编码)7
						if (name[8].trim().equals("NULL") || name[8].trim().equals("")) {
							salesitem.setDrugNum(0.0);
						} else {
							salesitem.setDrugNum(Double.parseDouble(def.format(Double.parseDouble(name[8].trim()))));// 销售数量 9
						}
						if (name[9].trim().equals("NULL") || name[9].trim().equals("")) {
							salesitem.setDrugSalesprice(0.0);
						} else {
							salesitem.setDrugSalesprice(Double.parseDouble(def.format(Double.parseDouble(name[9].trim()))));// 销售价 10
						}
						salesitem.setDrugBatchno(name[11].trim());// 药品批号 12
						salesitem.setDrugMfrs(name[12].trim());// 生产商 13
						salesitem.setDrugMadein(name[13].trim());// 药品产地 14
						DateFormat df;
						if (name[14].trim().equals("NULL") || name[14].trim().equals("") || name[14].trim().equals("1900-1-1")) {
							salesitem.setDrugMfgdate(null);
						} else {
							// 判断drugMfgdate时间类型长度有没有分时秒
							if (name[14].trim().length() == 10) {
								df = new SimpleDateFormat("yyyy-MM-dd");
							} else if(name[14].trim().length() == 18){
								df = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
							} else if(name[14].trim().length() == 17){
								df = new SimpleDateFormat("dd-MMM-yyHH:mm:ss",Locale.ENGLISH);
							} else {
								df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							}
							salesitem.setDrugMfgdate(df.parse(name[14].trim()));// 生产日期 15
						}
						if (name[15].trim().equals("NULL") || name[15].trim().equals("") || name[15].trim().equals("1900-1-1")) {
							salesitem.setDrugExpdate(null);
						} else {
							// 判断drugExpdate时间类型长度有没有分时秒
							if (name[15].trim().length() == 10) {
								df = new SimpleDateFormat("yyyy-MM-dd");
							} else if(name[15].trim().length() == 18){
								df = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
							} else if(name[15].trim().length() == 17){
								df = new SimpleDateFormat("dd-MMM-yyHH:mm:ss",Locale.ENGLISH);
							} else {
								df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							}
							salesitem.setDrugExpdate(df.parse(name[15].trim()));// 有效期 16
						}
						salesitem.setDrugEid(name[16].trim());// 电子监管码 17
						salesitem.setSoSalespsnname(name[1].trim());// 姓名..2
						salesitem.setSoPaytype(name[17].trim());// 结算方式.. 18
						if (name[22].trim().equals("NULL") || name[22].trim().equals("") || name[22].trim().equals("1900-1-1")) {
							salesitem.setSoDatetime(null);
						} else {
							if (name[22].trim().length() == 10) {
								df = new SimpleDateFormat("yyyy-MM-dd");
							} else if(name[22].trim().length() == 18){
								df = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
							} else if(name[22].trim().length() == 17){
								df = new SimpleDateFormat("dd-MMM-yyHH:mm:ss",Locale.ENGLISH);
							} else {
								df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							}
							salesitem.setSoDatetime(df.parse(name[22].trim()));// 销售日期.. 23
						}
						salesitem.setSiPtssex(name[2].trim());// 性别 3
						salesitem.setSiPtsage(name[3].trim());// 年龄 4
						salesitem.setSiPtsidcard(name[4].trim());// 身份证号 5
						salesitem.setSiPtshealthcard(name[5].trim());// 医保卡号 6
						salesitem.setDrugName(name[7].trim());// 项目名称(药品名称) 8
						salesitem.setDrugSpecification(name[10].trim());// 规格 11
						salesitem.setSiSettlementname(name[18].trim());// 结算方式名称19
						salesitem.setSiStatus(name[19].trim());// 结算状态 20
						salesitem.setSiOpcode(name[20].trim());// 操作员编码 21
						salesitem.setSiOpname(name[21].trim());// 操作员名称 22
						salesitem.setSiUnit(name[23].trim());// 单位24
						if(name.length>24){
							salesitem.setSiTracecode(name[24].trim());//追溯码 25
						}
						salesitem.setDrugPicktime(upCollectdate);// 采集时间
						salesitem.setSoCreatedatetime(new Date());// 创建日期
						if(a<2){
							checkWrongInfo(name, 1007, cusId, path, fileName);
							a++;
						}
						baseDao.save(salesitem);
						count++;
					} catch (Exception e) {
						error++;
						if(error <= 2){//只记录文件的2行错误
							String log = e.toString();
							if (log.length() > 300){
								log = log.substring(0, 300);
							}
							if(log.indexOf("input string")!=-1){
								log = "数字解析出错，数字中包含了字符!";
							}
							if(log.indexOf("empty String")!=-1){
								log = "数字解析出错，存在空值!";
							}
							if(log.indexOf("Unparseable")!=-1){
								log = "日期解析出错，日期格式不正确!";
							}
							errorlog = new Errorlog();
							errorlog.setCusId(cusId);
							errorlog.setCusName(cusName);
							errorlog.setErrAddtime(new Date());
							errorlog.setErrFileflag("销售信息");
							errorlog.setErrFilepath(path);
							errorlog.setErrLog(log + "  \n错误行号:" + (count+error));
							errorlog.setErrType("数据解析错误");
							baseDao.save(errorlog);
						}
					}
				}
				if(count ==0 && error > 0){
					baseDao.updateOrDelete(qsql, -1,0,getInfo(count, error),upId);//全部失败
				} else {
					baseDao.updateOrDelete(qsql, 1,count,getInfo(count, error),upId);//全部成功/有成功有失败
				}
			} else {
				baseDao.updateOrDelete(qsql,-1,0,"<font color='red'>文件不存在，请使用服务器地址解析</font>", upId);
			}
		} catch (Exception e) {
			String log = e.toString();
			if (log.length() > 300){
				log = log.substring(0, 300);
			}
			errorlog = new Errorlog();
			errorlog.setCusId(cusId);
			errorlog.setCusName(cusName);
			errorlog.setErrAddtime(new Date());
			errorlog.setErrFileflag("销售信息");
			errorlog.setErrFilepath(fileName);
			if (null !=name && name.length < 24) {
				errorlog.setErrType("数据上传字段不全错误");
				errorlog.setErrLog(log);
				baseDao.updateOrDelete(qsql,-1,0,"<font color='red'>数据上传字段不全错误,正确字段数:24,实际解析字段数:" + name.length + "</font>", upId);
			} else {
				errorlog.setErrType("数据解析错误");
				errorlog.setErrLog(log + "\n错误行号:" + (count+1) + "  当前错误行内容:\n" + line);
				baseDao.updateOrDelete(qsql,-1,0,"<font color='red'>数据解析错误</font>", upId);
			}
			baseDao.save(errorlog);
		} finally {
			if (bufr != null) {
				bufr.close();
			}
		}
	}
	
	/**
	 * 添加库房信息
	 */
	@Override
	public void addWarehouse(String path, String cusId, String cusName, Date upCollectdate, int upId, String cusFlag,String fileName) throws IOException {
		BufferedReader bufr = null;
		int count = 0;// 定义当行行号
		int error = 0;
		String line = null;// 定义当前行
		String[] name = null;// 定义当前行String数组
		Warehouse warehouse;
		Errorlog errorlog;
		String qsql = "update Uploadfile set isHandle=?0,upCount=?1,upName=?2 where upId=?3";
		int a = 0;
		try {
			File file = new File(path);
			if (file.isFile()) {
				bufr = new BufferedReader(new FileReader(file));
				while ((line = bufr.readLine()) != null) {
					name = line.split("##");
					// 上传文件解析不正确,可能为数据不全
					if (name.length < 6) {
						throw new DataException("正确字段数:6,实际解析字段数:" + name.length + ",字段不全解析停止", null);
					}
					for (int i = 0; i < name.length; i++) {
						if (null != name[i] && name[i].trim().equals("NULL")){
							name[i] = "";
						}
					}
					try{
						//判断定点是否连锁,数据解析至分店cusid下
						// 判断定点类型是连锁药店并且上传有最后一位分店编码
						warehouse = new Warehouse();
						if (cusFlag.equals("102") && name.length > 6) {// length:7
							// 分店标识为零,则保存在总店id下
							if (name[6].trim().equals("0")) {
								warehouse.setCusId(cusId);
							} else {
								// 查找分店id,父id是当前cusid,分店编码为name[]最后一位
								String hql = "select cusId from Customer where cusParentid =?0 and cusBranchcode=?1 and cusStatus !=-1";
								List list = baseDao.find(hql, cusId, name[6].trim());
								if (list.size() > 0) {
									// 分店已注册查找到分店id
									warehouse.setCusId(list.get(0).toString());
								} else {// 如果当前分店编码未注册,执行下次循环
									error++;
									continue;
								}
							}
						} else {
							warehouse.setCusId(cusId);
						}
						warehouse.setCusDareway(""); // 医院编码
						warehouse.setCusPid("0");// 客户上级id
						warehouse.setWhAddtime(new Date());// 创建日期
						warehouse.setWhPicktime(upCollectdate);// 采集日期从传来的文件采集时间获取
						warehouse.setWhCode(name[0].trim());// 1 仓库编码
						warehouse.setWhName(name[1].trim());// 2 仓库名称
						warehouse.setWhMancode(name[2].trim());// 3 负责人编码
						warehouse.setWhManname(name[3].trim());// 4 负责人名称
						warehouse.setWhLocation(name[4].trim());// 5 仓库位置
						warehouse.setWhStatus(name[5].trim());// 6 状态
						if(a<2){
							checkWrongInfo(name, 1008, cusId, path, fileName);
							a++;
						}
						baseDao.save(warehouse);
						count++;
					} catch (Exception e) {
						error++;
						if(error <= 2){//只记录文件的2行错误
							String log = e.toString();
							if (log.length() > 300){
								log = log.substring(0, 300);
							}
							if(log.indexOf("input string")!=-1){
								log = "数字解析出错，数字中包含了字符!";
							}
							if(log.indexOf("empty String")!=-1){
								log = "数字解析出错，存在空值!";
							}
							if(log.indexOf("Unparseable")!=-1){
								log = "日期解析出错，日期格式不正确!";
							}
							errorlog = new Errorlog();
							errorlog.setCusId(cusId);
							errorlog.setCusName(cusName);
							errorlog.setErrAddtime(new Date());
							errorlog.setErrFileflag("库房信息");
							errorlog.setErrFilepath(path);
							errorlog.setErrLog(log + "  \n错误行号:" + (count+error));
							errorlog.setErrType("数据解析错误");
							baseDao.save(errorlog);
						}
					}
				}
				if(count ==0 && error > 0){
					baseDao.updateOrDelete(qsql, -1,0,getInfo(count, error),upId);//全部失败
				} else {
					baseDao.updateOrDelete(qsql, 1,count,getInfo(count, error),upId);//全部成功/有成功有失败
				}
			} else {
				baseDao.updateOrDelete(qsql,-1,0,"<font color='red'>文件不存在，请使用服务器地址解析</font>", upId);
			}
		} catch (Exception e) {
			String log = e.toString();
			if (log.length() > 300){
				log = log.substring(0, 300);
			}
			errorlog = new Errorlog();
			errorlog.setCusId(cusId);
			errorlog.setCusName(cusName);
			errorlog.setErrAddtime(new Date());
			errorlog.setErrFileflag("库房信息");
			errorlog.setErrFilepath(fileName);
			if (null !=name && name.length < 6) {
				errorlog.setErrType("数据上传字段不全错误");
				errorlog.setErrLog(log);
				baseDao.updateOrDelete(qsql,-1,0,"<font color='red'>数据上传字段不全错误,正确字段数:6,实际解析字段数:" + name.length + "</font>", upId);
			} else {
				errorlog.setErrType("数据解析错误");
				errorlog.setErrLog(log + "\n错误行号:" + (count+1) + "  当前错误行内容:\n" + line);
				baseDao.updateOrDelete(qsql,-1,0,"<font color='red'>数据解析错误</font>", upId);
			}
			baseDao.save(errorlog);
		} finally {
			if (bufr != null) {
				bufr.close();
			}
		}
	}
	
	/**
	 * 添加处方信息
	 */
	@Override
	public void addPrescribes(String path, String cusId,String cusName, Date upCollectdate, int upId,String cusFlag,String fileName) throws IOException, ParseException {
		BufferedReader bufr = null;
		int count = 0;// 定义当行行号
		int error = 0;
		String line = null;// 定义当前行
		String[] name = null;// 定义当前行String数组
		Prescribe prescribe;
		Errorlog errorlog;
		String qsql = "update Uploadfile set isHandle=?0,upCount=?1,upName=?2 where upId=?3";
		int a = 0;
		try {
			File file = new File(path);
			if (file.isFile()) {
				bufr = new BufferedReader(new FileReader(file));
				while ((line = bufr.readLine()) != null) {
					name = line.split("##");
					// 上传文件解析不正确,可能为数据不全
					if (name.length < 35) {
						throw new DataException("正确字段数:35,实际解析字段数:" + name.length + ",字段不全解析停止", null);
					}
					for (int i = 0; i < name.length; i++) {
						if (null != name[i] && name[i].trim().equals("NULL")){
							name[i] = "";
						}
					}
					try{
						//判断定点是否连锁,数据解析至分店cusid下
						// 判断定点类型是连锁药店并且上传有最后一位分店编码
						prescribe = new Prescribe();
						if (cusFlag.equals("102") && name.length > 35) {// length:36
							// 分店标识为零,则保存在总店id下
							if (name[35].trim().equals("0")) {
								prescribe.setCusId(cusId);
							} else {
								// 查找分店id,父id是当前cusid,分店编码为name[]最后一位
								String hql = "select cusId from Customer where cusParentid =?0 and cusBranchcode=?1 and cusStatus !=-1";
								List list = baseDao.find(hql, cusId, name[35].trim());
								if (list.size() > 0) {
									// 分店已注册查找到分店id
									prescribe.setCusId(list.get(0).toString());
								} else {// 如果当前分店编码未注册,执行下次循环
									error++;
									continue;
								}
							}
						} else {
							prescribe.setCusId(cusId);
						}
						DecimalFormat def = new DecimalFormat("#.00");
						prescribe.setCusPid("0");// 客户上级id
						prescribe.setCusDareway("");// 医院编码
						prescribe.setRpNo(name[0].trim());// 处方单号
						prescribe.setRpDeptno(name[1].trim());// 处方科别
						prescribe.setRpDeptname(name[2].trim());// 处方科别名称
						prescribe.setRpPtsname(name[3].trim());// 患者姓名
						prescribe.setRpPtssex(name[4].trim());// 性别
						prescribe.setRpPtsage(name[5].trim());// 年龄
						prescribe.setRpPtshealthcard(name[6].trim());// 医保卡号
						prescribe.setRpPtsidcard(name[7].trim());// 身份证号
						prescribe.setRpPtsbirthday(name[8].trim());// 出生日期
						prescribe.setRpItemcode(name[9].trim());// 项目编码
						prescribe.setRpItemname(name[10].trim());// 项目名称
						prescribe.setRpItemspecification(name[13].trim());// 规格
						prescribe.setRpItemdosageform(name[14].trim());// 剂型
						prescribe.setRpItemmfrs(name[15].trim());// 生产商
						prescribe.setRpItemmakein(name[16].trim());// 生产地
						prescribe.setRpItembatchno(name[17].trim());// 批号
						prescribe.setRpWhcode(name[18].trim());// 仓库编码
						prescribe.setRpWhname(name[19].trim());// 仓库名称
						prescribe.setRpLocation(name[20].trim());// 货位/货架号
						prescribe.setRpDrugfreq(name[21].trim());// 用药频次
						prescribe.setRpDrugroute(name[22].trim());// 用药途径
						prescribe.setRpDrugtime(name[23].trim());// 用药时间
						prescribe.setRpDrugamount(name[24].trim());// 用药量
						prescribe.setRpDrcode(name[25].trim());// 处方医师
						prescribe.setRpDrname(name[26].trim());// 处方医师名称
						prescribe.setRpAuditcode(name[28].trim());// 审核医师
						prescribe.setRpAuditname(name[29].trim());// 审核医师名称
						prescribe.setRpCheckcode(name[31].trim());// 核对医师
						prescribe.setRpCheckname(name[32].trim());// 核对医师名称
						prescribe.setRpAnnex(name[34].trim());// 附件
						if (name[11].trim().equals("NULL") || name[11].trim().equals("")) {
							prescribe.setRpItemprice(null);
						} else {
							prescribe.setRpItemprice(Double.parseDouble(def.format(Double.parseDouble(name[11].trim()))));// 销售价
						}
						if (name[11].trim().equals("NULL") || name[11].trim().equals("")) {
							prescribe.setRpItemnum(null);
						} else {
							prescribe.setRpItemnum(Double.parseDouble(def.format(Double.parseDouble(name[11].trim()))));// 销售价
						}
						DateFormat df;
						if (name[27].trim().equals("NULL") || name[27].trim().equals("") || name[27].trim().equals("1900-1-1")) {
							prescribe.setRpDrtime(null);
						} else {
							if (name[27].trim().length() == 10) {
								df = new SimpleDateFormat("yyyy-MM-dd");
							} else if(name[27].trim().length() == 18){
								df = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
							} else if(name[27].trim().length() == 17){
								df = new SimpleDateFormat("dd-MMM-yyHH:mm:ss",Locale.ENGLISH);
							} else {
								df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							}
							prescribe.setRpDrtime(df.parse(name[27].trim()));// 开具日期
						}
						if (name[30].trim().equals("NULL") || name[30].trim().equals("") || name[30].trim().equals("1900-1-1")) {
							prescribe.setRpDrtime(null);
						} else {
							if (name[30].trim().length() == 10) {
								df = new SimpleDateFormat("yyyy-MM-dd");
							} else if(name[30].trim().length() == 18){
								df = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
							} else if(name[30].trim().length() == 17){
								df = new SimpleDateFormat("dd-MMM-yyHH:mm:ss",Locale.ENGLISH);
							} else {
								df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							}
							prescribe.setRpDrtime(df.parse(name[30].trim()));// 审核日期
						}
						if (name[33].trim().equals("NULL") || name[33].trim().equals("") || name[33].trim().equals("1900-1-1")) {
							prescribe.setRpDrtime(null);
						} else {
							if (name[33].trim().length() == 10) {
								df = new SimpleDateFormat("yyyy-MM-dd");
							} else if(name[33].trim().length() == 18){
								df = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
							} else if(name[33].trim().length() == 17){
								df = new SimpleDateFormat("dd-MMM-yyHH:mm:ss",Locale.ENGLISH);
							} else {
								df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							}
							prescribe.setRpDrtime(df.parse(name[33].trim()));// 核对日期
						}
						prescribe.setCusDareway("");// 医院编码
						prescribe.setRpPicktime(upCollectdate);// 采集日期
						prescribe.setRpAddtime(new Date());// 创建日期
						if(a<2){
							checkWrongInfo(name, 1010, cusId, path, fileName);
							a++;
						}
						baseDao.save(prescribe);
						count++;
					} catch (Exception e) {
						error++;
						if(error <= 2){//只记录文件的2行错误
							String log = e.toString();
							if (log.length() > 300){
								log = log.substring(0, 300);
							}
							if(log.indexOf("input string")!=-1){
								log = "数字解析出错，数字中包含了字符!";
							}
							if(log.indexOf("empty String")!=-1){
								log = "数字解析出错，存在空值!";
							}
							if(log.indexOf("Unparseable")!=-1){
								log = "日期解析出错，日期格式不正确!";
							}
							errorlog = new Errorlog();
							errorlog.setCusId(cusId);
							errorlog.setCusName(cusName);
							errorlog.setErrAddtime(new Date());
							errorlog.setErrFileflag("处方信息");
							errorlog.setErrFilepath(path);
							errorlog.setErrLog(log + "  \n错误行号:" + (count+error));
							errorlog.setErrType("数据解析错误");
							baseDao.save(errorlog);
						}
					}
				}
				if(count ==0 && error > 0){
					baseDao.updateOrDelete(qsql, -1,0,getInfo(count, error),upId);//全部失败
				} else {
					baseDao.updateOrDelete(qsql, 1,count,getInfo(count, error),upId);//全部成功/有成功有失败
				}
			} else {
				baseDao.updateOrDelete(qsql,-1,0,"<font color='red'>文件不存在，请使用服务器地址解析</font>", upId);
			}
		} catch (Exception e) {
			String log = e.toString();
			if (log.length() > 300){
				log = log.substring(0, 300);
			}
			errorlog = new Errorlog();
			errorlog.setCusId(cusId);
			errorlog.setCusName(cusName);
			errorlog.setErrAddtime(new Date());
			errorlog.setErrFileflag("处方信息");
			errorlog.setErrFilepath(fileName);
			if (null !=name && name.length < 35) {
				errorlog.setErrType("数据上传字段不全错误");
				errorlog.setErrLog(log);
				baseDao.updateOrDelete(qsql,-1,0,"<font color='red'>数据上传字段不全错误,正确字段数:35,实际解析字段数:" + name.length + "</font>", upId);
			} else {
				errorlog.setErrType("数据解析错误");
				errorlog.setErrLog(log + "\n错误行号:" + (count+1) + "  当前错误行内容:\n" + line);
				baseDao.updateOrDelete(qsql,-1,0,"<font color='red'>数据解析错误</font>", upId);
			}
			baseDao.save(errorlog);
		} finally {
			if (bufr != null) {
				bufr.close();
			}
		}
	}
	
	/**
	 * 添加分店信息
	 */
	@Override
	public void addCustomers(String path, String cusId,String cusName, Date upCollectdate, int upId, String fileName) throws IOException, ParseException {
		BufferedReader bufr = null;
		int count = 0;// 定义当行行号
		int error = 0;
		String line = null;// 定义当前行
		String[] name = null;// 定义当前行String数组
		Customer customer;
		Errorlog errorlog;
		String qsql = "update Uploadfile set isHandle=?0,upCount=?1,upName=?2 where upId=?3";
		int a = 0;
		try {
			File file = new File(path);
			if (file.isFile()) {
				bufr = new BufferedReader(new FileReader(file));
				while ((line = bufr.readLine()) != null) {
					name = line.split("##");
					// 上传文件解析不正确,可能为数据不全
					if (name.length != 8) {
						throw new DataException("正确字段数:8,实际解析字段数:" + name.length + ",字段不全解析停止", null);
					}
					for (int i = 0; i < name.length; i++) {
						if (null != name[i] && name[i].trim().equals("NULL")){
							name[i] = "";
						}
					}
					try{
						String hql = "select count(c.cusId) from Customer c where c.cusParentid=?0 and c.cusBranchcode=?1 and c.cusStatus=2";
						int sum = baseDao.findCount(hql, cusId, name[0].trim());
						if (sum > 0) {//已注册分店跳过
							error++;
							continue;
						}
						customer = new Customer();
						customer.setCusParentid(cusId);// 获取上级编码
						customer.setCusStatus(0);//状态：注销-1、待审核0、连锁分店2、其他1(包括连锁总店)
						customer.setCusFlag("102");
						customer.setCusUniqure("");
						customer.setCusRegdate(new Date());
						if (name[0].trim().equals("NULL") || name[0].trim().equals("")) {//分店编码为空跳过
							error++;
							continue;
						} else {
							customer.setCusBranchcode(name[0].trim());
						}
						if (name[1].trim().contains(cusName)) {// 机构名称2
							customer.setCusName(name[1].trim());
						} else {// 加上全称
							customer.setCusName(cusName + "-" + name[1].trim());
						}
						// 获取客户拼音简码
						ChineseCharToEn ctn = new ChineseCharToEn();
						customer.setCusPcode(ctn.getAllFirstLetter(name[1].trim()));//拼音简码
						customer.setCusAddr(name[2].trim());// 联系地址3
						customer.setCusContact(name[3].trim());// 联系人4
						customer.setCusPhone(name[4].trim());// 联系电话5
						if (name[5].trim().equals("NULL") || name[5].trim().equals("")) {// 医院编码6
							customer.setCusRemark("");
						} else {
							customer.setCusDareway(name[5].trim());
						}
						if (name[6].trim().equals("NULL") || name[6].trim().equals("")) {// 7 : ip
							customer.setCusRegip("");
						} else {
							customer.setCusRegip(name[6].trim());
						}
						if (name[7].trim().equals("NULL") || name[7].trim().equals("")) {
							customer.setCusRemark("");
						} else {
							customer.setCusRemark(name[7].trim());// 备注8
						}
						baseDao.save(customer);
						count++;
						
						//添加区划
						String cusname = null != customer.getCusName() ? customer.getCusName().trim() : "";
						String cusAddr = null != customer.getCusAddr() ? customer.getCusAddr().trim() : "";
						String cusid = customer.getCusId();
						String areacode;
						if (cusname.contains("张店") || cusAddr.contains("张店")) {
							areacode = "370303";
						} else if (cusname.contains("淄川") || cusAddr.contains("淄川")) {
							areacode = "370302";
						} else if (cusname.contains("桓台") || cusAddr.contains("桓台")) {
							areacode = "370321";
						} else if (cusname.contains("临淄") || cusAddr.contains("临淄")) {
							areacode = "370305";
						} else if (cusname.contains("高青") || cusAddr.contains("高青")) {
							areacode = "370322";
						} else if (cusname.contains("周村") || cusAddr.contains("周村")) {
							areacode = "370306";
						} else if (cusname.contains("博山") || cusAddr.contains("博山")) {
							areacode = "370304";
						} else if (cusname.contains("沂源") || cusAddr.contains("沂源")) {
							areacode = "370323";
						} else if (cusname.contains("市直") || cusAddr.contains("市直")) {
							areacode = "370300";
						} else if (cusname.contains("高新") || cusAddr.contains("高新")) {
							areacode = "370313";
						} else {
							areacode = "370303";
						}
						CusareaRelate cusareaRelate = new CusareaRelate();
						cusareaRelate.setAcAreacode(areacode);
						cusareaRelate.setCusId(cusid);
						baseDao.save(cusareaRelate);
					} catch (Exception e) {
						error++;
						if(error <= 2){//只记录文件的2行错误
							String log = e.toString();
							if (log.length() > 300){
								log = log.substring(0, 300);
							}
							if(log.indexOf("input string")!=-1){
								log = "数字解析出错，数字中包含了字符!";
							}
							if(log.indexOf("empty String")!=-1){
								log = "数字解析出错，存在空值!";
							}
							if(log.indexOf("Unparseable")!=-1){
								log = "日期解析出错，日期格式不正确!";
							}
							errorlog = new Errorlog();
							errorlog.setCusId(cusId);
							errorlog.setCusName(cusName);
							errorlog.setErrAddtime(new Date());
							errorlog.setErrFileflag("分店信息");
							errorlog.setErrFilepath(path);
							errorlog.setErrLog(log + "  \n错误行号:" + (count+error));
							errorlog.setErrType("数据解析错误");
							baseDao.save(errorlog);
						}
					}
				}
				if(count ==0 && error > 0){
					baseDao.updateOrDelete(qsql, -1,0,getInfo(count, error),upId);//全部失败
				} else {
					baseDao.updateOrDelete(qsql, 1,count,getInfo(count, error),upId);//全部成功/有成功有失败
				}
			} else {
				baseDao.updateOrDelete(qsql,-1,0,"<font color='red'>文件不存在，请使用服务器地址解析</font>", upId);
			}
		} catch (Exception e) {
			String log = e.toString();
			if (log.length() > 300){
				log = log.substring(0, 300);
			}
			errorlog = new Errorlog();
			errorlog.setCusId(cusId);
			errorlog.setCusName(cusName);
			errorlog.setErrAddtime(new Date());
			errorlog.setErrFileflag("分店信息");
			errorlog.setErrFilepath(fileName);
			if (null !=name && name.length != 8) {
				errorlog.setErrType("数据上传字段不全错误");
				errorlog.setErrLog(log);
				baseDao.updateOrDelete(qsql,-1,0,"<font color='red'>数据上传字段不全错误,正确字段数:8,实际解析字段数:" + name.length + "</font>", upId);
			} else {
				errorlog.setErrType("数据解析错误");
				errorlog.setErrLog(log + "\n错误行号:" + (count+1) + "  当前错误行内容:\n" + line);
				baseDao.updateOrDelete(qsql,-1,0,"<font color='red'>数据解析错误</font>", upId);
			}
			baseDao.save(errorlog);
		} finally {
			if (bufr != null) {
				bufr.close();
			}
		}
	}
	
	/**
	 * 添加住院记录
	 */
	@Override
	public void addHospitalized(String path, String cusId, String cusName, Date upCollectdate, int upId,String fileName) throws IOException {
		BufferedReader bufr = null;
		int count = 0;// 定义当行行号
		int error = 0;
		String line = null;// 定义当前行
		String[] name = null;// 定义当前行String数组
		Hospitalized hospitalized;
		Errorlog errorlog;
		String qsql = "update Uploadfile set isHandle=?0,upCount=?1,upName=?2 where upId=?3";
		int a = 0;
		try {
			File file = new File(path);
			if (file.isFile()) {
				bufr = new BufferedReader(new FileReader(file));
				while ((line = bufr.readLine()) != null) {
					name = line.split("##");
					// 上传文件解析不正确,可能为数据不全
					if (name.length < 26) {
						throw new DataException("正确字段数:26,实际解析字段数:" + name.length + ",字段不全解析停止", null);
					}
					for (int i = 0; i < name.length; i++) {
						if (null != name[i] && name[i].trim().equals("NULL")){
							name[i] = "";
						}
					}
					try{
						hospitalized = new Hospitalized();
						hospitalized.setCusId(cusId);// 客户id
						hospitalized.setCusDareway(""); // 医院编码
						hospitalized.setHospPicktime(upCollectdate);// 采集日期从传来的文件采集时间获取
						hospitalized.setHospAddtime(new Date());// 创建日期
						hospitalized.setSiPtshealthcard(name[25].trim());// 医保卡号26
						hospitalized.setSiPtsidcard(name[24].trim());// 身份证号 25
						hospitalized.setSiPtsage(name[23].trim());// 年龄24
						if (name[22].trim().equals("NULL") || name[22].trim().equals("")) {// 性别23
							hospitalized.setSiPtssex(0);
						} else if (name[22].trim().contains("男")) {
							hospitalized.setSiPtssex(1);
						} else if (name[22].trim().contains("女")) {
							hospitalized.setSiPtssex(2);
						} else {
							hospitalized.setSiPtssex(Integer.parseInt(name[22].trim()));
						}
						hospitalized.setSiPtsname(name[21].trim());// 姓名 22
						DateFormat df;
						if (name[20].trim().equals("NULL") || name[20].trim().equals("") || name[20].trim().equals("1900-1-1")) {
							hospitalized.setHospIntime(null);
						} else {
							if (name[20].trim().length() == 10) {
								df = new SimpleDateFormat("yyyy-MM-dd");
							} else if(name[20].trim().length() == 18){
								df = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
							} else if(name[20].trim().length() == 17){
								df = new SimpleDateFormat("dd-MMM-yyHH:mm:ss",Locale.ENGLISH);
							} else {
								df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							}
							hospitalized.setHospIntime(df.parse(name[20].trim()));
						}
						hospitalized.setHospBedno(name[19].trim());// 床位号 20
						hospitalized.setHospWardno(name[18].trim());// 房间号 19
						hospitalized.setHospAreas(name[17].trim());// 病区名称 18
						hospitalized.setHospDoctorname(name[16].trim());// 主冶医师名称17
						hospitalized.setHospDoctor(name[15].trim());// 主治医师 16
						hospitalized.setHospDeptname(name[14].trim());// 入院科室名称15
						hospitalized.setHospDept(name[13].trim());// 入院科室 14
						hospitalized.setHospWay(name[12].trim());// 住院方式 13
						hospitalized.setHospType(name[11].trim());// 住院类别 12
						hospitalized.setIcdName(name[10].trim());// 疾病名称 11
						hospitalized.setIcdCode(name[9].trim());// 疾病编码 10
						if (name[8].trim().equals("NULL") || name[8].trim().equals("") || name[8].trim().equals("1900-1-1")) {
							hospitalized.setDiagDatetime(null);
						} else {
							if (name[8].trim().length() == 10) {
								df = new SimpleDateFormat("yyyy-MM-dd");
							} else if(name[8].trim().length() == 18){
								df = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
							} else if(name[8].trim().length() == 17){
								df = new SimpleDateFormat("dd-MMM-yyHH:mm:ss",Locale.ENGLISH);
							} else {
								df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							}
							hospitalized.setDiagDatetime(df.parse(name[8].trim()));
						}
						hospitalized.setDiagDoctorname(name[7].trim());// 确认医师名称8
						hospitalized.setDiagDoctor(name[6].trim());// 确诊医师 7
						hospitalized.setDiagDeptname(name[5].trim());// 门诊科室名称6
						hospitalized.setDiagDept(name[4].trim());// 门诊科室 5
						if (name[3].trim().equals("NULL") || name[3].trim().equals("")) {
							hospitalized.setDiagType(0);
						} else {
							hospitalized.setDiagType(Integer.parseInt(name[3].trim()));// 接诊方式
						}
						hospitalized.setHospNo(name[2].trim());// 住院号 3
						hospitalized.setCaseNo(name[1].trim());// 病案号 2
						if (name[0].trim().equals("NULL") || name[0].trim().equals("")) {
							hospitalized.setDoctorType(0);
						} else {
							hospitalized.setDoctorType(Integer.parseInt(name[0].trim()));// 就医类别
						}
						if(a<2){
							checkWrongInfo(name, 1011, cusId, path, fileName);
							a++;
						}
						baseDao.save(hospitalized);
						count++;
					} catch (Exception e) {
						error++;
						if(error <= 2){//只记录文件的2行错误
							String log = e.toString();
							if (log.length() > 300){
								log = log.substring(0, 300);
							}
							if(log.indexOf("input string")!=-1){
								log = "数字解析出错，数字中包含了字符!";
							}
							if(log.indexOf("empty String")!=-1){
								log = "数字解析出错，存在空值!";
							}
							if(log.indexOf("Unparseable")!=-1){
								log = "日期解析出错，日期格式不正确!";
							}
							errorlog = new Errorlog();
							errorlog.setCusId(cusId);
							errorlog.setCusName(cusName);
							errorlog.setErrAddtime(new Date());
							errorlog.setErrFileflag("住院信息");
							errorlog.setErrFilepath(path);
							errorlog.setErrLog(log + "  \n错误行号:" + (count+error));
							errorlog.setErrType("数据解析错误");
							baseDao.save(errorlog);
						}
					}
				}
				if(count ==0 && error > 0){
					baseDao.updateOrDelete(qsql, -1,0,getInfo(count, error),upId);//全部失败
				} else {
					baseDao.updateOrDelete(qsql, 1,count,getInfo(count, error),upId);//全部成功/有成功有失败
				}
			} else {
				baseDao.updateOrDelete(qsql,-1,0,"<font color='red'>文件不存在，请使用服务器地址解析</font>", upId);
			}
		} catch (Exception e) {
			String log = e.toString();
			if (log.length() > 300){
				log = log.substring(0, 300);
			}
			errorlog = new Errorlog();
			errorlog.setCusId(cusId);
			errorlog.setCusName(cusName);
			errorlog.setErrAddtime(new Date());
			errorlog.setErrFileflag("住院信息");
			errorlog.setErrFilepath(fileName);
			if (null !=name && name.length < 26) {
				errorlog.setErrType("数据上传字段不全错误");
				errorlog.setErrLog(log);
				baseDao.updateOrDelete(qsql,-1,0,"<font color='red'>数据上传字段不全错误,正确字段数:26,实际解析字段数:" + name.length + "</font>", upId);
			} else {
				errorlog.setErrType("数据解析错误");
				errorlog.setErrLog(log + "\n错误行号:" + (count+1) + "  当前错误行内容:\n" + line);
				baseDao.updateOrDelete(qsql,-1,0,"<font color='red'>数据解析错误</font>", upId);
			}
			baseDao.save(errorlog);
		} finally {
			if (bufr != null) {
				bufr.close();
			}
		}
	}
	
	/**
	 * 添加出院记录
	 */
	@Override
	public void addDischarged(String path, String cusId, String cusName, Date upCollectdate, int upId,String fileName) throws IOException {
		BufferedReader bufr = null;
		int count = 0;// 定义当行行号
		int error = 0;
		String line = null;// 定义当前行
		String[] name = null;// 定义当前行String数组
		Discharged discharged;
		Errorlog errorlog;
		String qsql = "update Uploadfile set isHandle=?0,upCount=?1,upName=?2 where upId=?3";
		int a = 0;
		try {
			File file = new File(path);
			if (file.isFile()) {
				bufr = new BufferedReader(new FileReader(file));
				while ((line = bufr.readLine()) != null) {
					name = line.split("##");
					// 上传文件解析不正确,可能为数据不全
					if (name.length < 16) {
						throw new DataException("正确字段数:16,实际解析字段数:" + name.length + ",字段不全解析停止", null);
					}
					for (int i = 0; i < name.length; i++) {
						if (null != name[i] && name[i].trim().equals("NULL")){
							name[i] = "";
						}
					}
					try{
						discharged = new Discharged();
						discharged.setDcDoctorname(name[15].trim());// 医师名称16
						discharged.setDcDoctorcode(name[14].trim());// 医师编码15
						discharged.setDcDiagname(name[13].trim());// 出院诊断名称14
						discharged.setDcDiagcode(name[12].trim());// 出院诊断编码13
						if (name[11].trim().equals("NULL") || name[11].trim().equals("")) {
							discharged.setDcHosptimes(0);
						} else {
							discharged.setDcHosptimes(Integer.parseInt(name[11].trim()));// 住院次数12
						}
						discharged.setDcOutype(name[10].trim());// 出院方式11
						if (name[9].trim().equals("NULL") || name[9].trim().equals("")) {
							discharged.setDcHospdays(0);
						} else {
							discharged.setDcHospdays(Integer.parseInt(name[9].trim()));// 住院天数10
						}
						DateFormat df;
						if (name[8].trim().equals("NULL") || name[8].trim().equals("") || name[8].trim().equals("1900-1-1")) {
							discharged.setDcOutdate(null);
						} else {
							if (name[8].trim().length() == 10) {
								df = new SimpleDateFormat("yyyy-MM-dd");
							} else if(name[8].trim().length() == 18){
								df = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
							} else if(name[8].trim().length() == 17){
								df = new SimpleDateFormat("dd-MMM-yyHH:mm:ss",Locale.ENGLISH);
							} else {
								df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							}
							discharged.setDcOutdate(df.parse(name[8].trim()));
						}
						if (name[7].trim().equals("NULL") || name[7].trim().equals("") || name[7].trim().equals("1900-1-1")) {
							discharged.setDcIndate(null);
						} else {
							if (name[7].trim().length() == 10) {
								df = new SimpleDateFormat("yyyy-MM-dd");
							} else if(name[7].trim().length() == 18){
								df = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
							} else if(name[7].trim().length() == 17){
								df = new SimpleDateFormat("dd-MMM-yyHH:mm:ss",Locale.ENGLISH);
							} else {
								df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							}
							discharged.setDcIndate(df.parse(name[7].trim()));// 入院日期8
						}
						discharged.setSiPtsage(name[6].trim());// 年龄7
						if (name[5].trim().equals("NULL") || name[5].trim().equals("")) {
							discharged.setSiPtssex(0);
						} else if (name[5].trim().contains("男")) {
							discharged.setSiPtssex(1);
						} else if (name[5].trim().contains("女")) {
							discharged.setSiPtssex(2);
						} else {
							discharged.setSiPtssex(Integer.parseInt(name[5].trim()));// 性别6
						}
						discharged.setSiPtsname(name[4].trim());// 姓名 5
						discharged.setSiPtshealthcard(name[3].trim());// 医保卡号4
						discharged.setSiPtsidcard(name[2].trim());// 个人编号3
						discharged.setHospNo(name[1].trim());// 住院号 2
						discharged.setCaseNo(name[0].trim());// 病案号 1
						discharged.setDcPickime(upCollectdate);// 采集日期从传来的文件采集时间获取
						discharged.setDcAddtime(new Date());// 创建日期
						discharged.setCusDareway(""); // 医院编码
						discharged.setCusId(cusId);// 获取上级编码
						if(a<2){
							checkWrongInfo(name, 1012, cusId, path, fileName);
							a++;
						}
						baseDao.save(discharged);
						count++;
					} catch (Exception e) {
						error++;
						if(error <= 2){//只记录文件的2行错误
							String log = e.toString();
							if (log.length() > 300){
								log = log.substring(0, 300);
							}
							if(log.indexOf("input string")!=-1){
								log = "数字解析出错，数字中包含了字符!";
							}
							if(log.indexOf("empty String")!=-1){
								log = "数字解析出错，存在空值!";
							}
							if(log.indexOf("Unparseable")!=-1){
								log = "日期解析出错，日期格式不正确!";
							}
							errorlog = new Errorlog();
							errorlog.setCusId(cusId);
							errorlog.setCusName(cusName);
							errorlog.setErrAddtime(new Date());
							errorlog.setErrFileflag("出院信息");
							errorlog.setErrFilepath(path);
							errorlog.setErrLog(log + "  \n错误行号:" + (count+error));
							errorlog.setErrType("数据解析错误");
							baseDao.save(errorlog);
						}
					}
				}
				if(count ==0 && error > 0){
					baseDao.updateOrDelete(qsql, -1,0,getInfo(count, error),upId);//全部失败
				} else {
					baseDao.updateOrDelete(qsql, 1,count,getInfo(count, error),upId);//全部成功/有成功有失败
				}
			} else {
				baseDao.updateOrDelete(qsql,-1,0,"<font color='red'>文件不存在，请使用服务器地址解析</font>", upId);
			}
		} catch (Exception e) {
			String log = e.toString();
			if (log.length() > 300){
				log = log.substring(0, 300);
			}
			errorlog = new Errorlog();
			errorlog.setCusId(cusId);
			errorlog.setCusName(cusName);
			errorlog.setErrAddtime(new Date());
			errorlog.setErrFileflag("出院信息");
			errorlog.setErrFilepath(fileName);
			if (null !=name && name.length < 16) {
				errorlog.setErrType("数据上传字段不全错误");
				errorlog.setErrLog(log);
				baseDao.updateOrDelete(qsql,-1,0,"<font color='red'>数据上传字段不全错误,正确字段数:16,实际解析字段数:" + name.length + "</font>", upId);
			} else {
				errorlog.setErrType("数据解析错误");
				errorlog.setErrLog(log + "\n错误行号:" + (count+1) + "  当前错误行内容:\n" + line);
				baseDao.updateOrDelete(qsql,-1,0,"<font color='red'>数据解析错误</font>", upId);
			}
			baseDao.save(errorlog);
		} finally {
			if (bufr != null) {
				bufr.close();
			}
		}
	}
	
	/**
	 * 添加医嘱信息
	 */
	@Override
	public void addDoctororder(String path, String cusId, String cusName, Date upCollectdate, int upId,String fileName) throws IOException {
		BufferedReader bufr = null;
		int count = 0;// 定义当行行号
		int error = 0;
		String line = null;// 定义当前行
		String[] name = null;// 定义当前行String数组
		Doctororder doctororder;
		Errorlog errorlog;
		String qsql = "update Uploadfile set isHandle=?0,upCount=?1,upName=?2 where upId=?3";
		int a = 0;
		try {
			File file = new File(path);
			if (file.isFile()) {
				bufr = new BufferedReader(new FileReader(file));
				while ((line = bufr.readLine()) != null) {
					name = line.split("##");
					// 上传文件解析不正确,可能为数据不全
					if (name.length < 22) {
						throw new DataException("正确字段数:22,实际解析字段数:" + name.length + ",字段不全解析停止", null);
					}
					for (int i = 0; i < name.length; i++) {
						if (null != name[i] && name[i].trim().equals("NULL")){
							name[i] = "";
						}
					}
					try{//某行有错误，捕获异常后继续解析下面
						doctororder = new Doctororder();
						DecimalFormat def = new DecimalFormat("#.00");
						doctororder.setCaseNo(name[0].trim());// 病案号1
						doctororder.setHospNo(name[1].trim());// 住院号2
						doctororder.setDoNo(name[2].trim());// 医嘱号3
						doctororder.setDoType(name[3].trim());// 医嘱类型4
						if(null != name[3] && (name[3].trim().equals("001") || name[3].trim().equals("01") || name[3].trim().equals("1") || name[3].trim().equals("长期医嘱") || name[3].trim().equals("长期"))){
							doctororder.setDoType("长期医嘱");//医嘱类型4
						} else if(null != name[3] && (name[3].trim().equals("002") || name[3].trim().equals("02") || name[3].trim().equals("2") || name[3].trim().equals("临时医嘱") || name[3].trim().equals("临时"))){
							doctororder.setDoType("临时医嘱");
						} else if(null != name[3] && (name[3].trim().equals("003") || name[3].trim().equals("03") || name[3].trim().equals("3") || name[3].trim().equals("备用医嘱") || name[3].trim().equals("备用"))){
							doctororder.setDoType("备用医嘱");
						} else if(null != name[3] && (name[3].trim().equals("") || name[3].trim().equals("NULL"))){
							doctororder.setDoType("");
						} else {
							doctororder.setDoType("其他");
						}
						
						DateFormat df;
						if (name[4].trim().equals("NULL") || name[4].trim().equals("") || name[4].trim().equals("1900-1-1")) {
							doctororder.setDoBegintime(null);
						} else {
							if (name[4].trim().length() == 10) {
								df = new SimpleDateFormat("yyyy-MM-dd");
							} else if(name[4].trim().length() == 18){
								df = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
							} else if(name[4].trim().length() == 17){
								df = new SimpleDateFormat("dd-MMM-yyHH:mm:ss",Locale.ENGLISH);
							} else {
								df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							}
							doctororder.setDoBegintime(df.parse(name[4].trim()));
						}
						doctororder.setDoDept(name[5].trim());// 开立科室6
						doctororder.setDoDeptname(name[6].trim());// 开立科室名称7
						doctororder.setDoDoctor(name[7].trim());// 开立医师8
						doctororder.setDoDoctorname(name[8].trim());// 开立医师名称9
						doctororder.setDoCheck(name[9].trim());// 审核护士10
						doctororder.setDoCheckname(name[10].trim());// 审核护士名称11
						doctororder.setItemCode(name[11].trim());// 项目编码12
						doctororder.setItemName(name[12].trim());// 项目名称13
						if (name[13].trim().equals("NULL") || name[13].trim().equals("")) {// 单价14
							doctororder.setItemPrice(null);
						} else {
							doctororder.setItemPrice(Double.parseDouble(def.format(Double.parseDouble(name[13].trim()))));
						}
						doctororder.setItemFreq(name[14].trim());// 频次15
						doctororder.setItemAmount(name[15].trim());// 用量16
						doctororder.setItmeRoute(name[16].trim());// 用法17
						doctororder.setDoEnddr(name[17].trim());// 停止医师18
						doctororder.setDoEnddrrname(name[18].trim());// 停止医师名称19
						doctororder.setDoEndnurse(name[19].trim());// 停止护士20
						doctororder.setDoEndnursename(name[20].trim());// 停止护士名称21
						if (name[21].trim().equals("NULL") || name[21].trim().equals("") || name[21].trim().equals("1900-1-1")) {
							doctororder.setDoEndtime(null);
						} else {
							if (name[21].trim().length() == 10) {
								df = new SimpleDateFormat("yyyy-MM-dd");
							} else if(name[21].trim().length() == 18){
								df = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
							} else if(name[21].trim().length() == 17){
								df = new SimpleDateFormat("dd-MMM-yyHH:mm:ss",Locale.ENGLISH);
							} else {
								df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							}
							doctororder.setDoEndtime(df.parse(name[21].trim()));
						}
						doctororder.setDoPicktime(upCollectdate);// 采集日期从传来的文件采集时间获取
						doctororder.setDoAddtime(new Date());// 创建日期
						doctororder.setCusDareway(""); // 医院编码
						doctororder.setCusId(cusId);// 获取上级编码
						if(a<2){
							checkWrongInfo(name, 1013, cusId, path, fileName);
							a++;
						}
						baseDao.save(doctororder);
						count++;
					} catch (Exception e) {
						error++;
						if(error <= 2){//只记录文件的2行错误
							String log = e.toString();
							if (log.length() > 300){
								log = log.substring(0, 300);
							}
							if(log.indexOf("input string")!=-1){
								log = "数字解析出错，数字中包含了字符!";
							}
							if(log.indexOf("empty String")!=-1){
								log = "数字解析出错，存在空值!";
							}
							if(log.indexOf("Unparseable")!=-1){
								log = "日期解析出错，日期格式不正确!";
							}
							errorlog = new Errorlog();
							errorlog.setCusId(cusId);
							errorlog.setCusName(cusName);
							errorlog.setErrAddtime(new Date());
							errorlog.setErrFileflag("医嘱信息");
							errorlog.setErrFilepath(path);
							errorlog.setErrLog(log + "  \n错误行号:" + (count+error));
							errorlog.setErrType("数据解析错误");
							baseDao.save(errorlog);
						}
					}
					
				}
				if(count ==0 && error > 0){
					baseDao.updateOrDelete(qsql, -1,0,getInfo(count, error),upId);//全部失败
				} else {
					baseDao.updateOrDelete(qsql, 1,count,getInfo(count, error),upId);//全部成功/有成功有失败
				}
			} else {
				baseDao.updateOrDelete(qsql,-1,0,"<font color='red'>文件不存在，请使用服务器地址解析</font>", upId);
			}
		} catch (Exception e) {
			String log = e.toString();
			if (log.length() > 300){
				log = log.substring(0, 300);
			}
			errorlog = new Errorlog();
			errorlog.setCusId(cusId);
			errorlog.setCusName(cusName);
			errorlog.setErrAddtime(new Date());
			errorlog.setErrFileflag("医嘱信息");
			errorlog.setErrFilepath(fileName);
			if (null !=name && name.length < 22) {
				errorlog.setErrType("数据上传字段不全错误");
				errorlog.setErrLog(log);
				baseDao.updateOrDelete(qsql,-1,0,"<font color='red'>数据上传字段不全错误,正确字段数:22,实际解析字段数:" + name.length + "</font>", upId);
			} else {
				errorlog.setErrType("数据解析错误");
				errorlog.setErrLog(log + "\n错误行号:" + (count+1) + "  当前错误行内容:\n" + line);
				baseDao.updateOrDelete(qsql,-1,0,"<font color='red'>数据解析错误</font>", upId);
			}
			baseDao.save(errorlog);
		} finally {
			if (bufr != null) {
				bufr.close();
			}
		}
	}
	
	/**
	 * 添加医嘱执行记录
	 */
	@Override
	public void addOrderperform(String path, String cusId, String cusName, Date upCollectdate, int upId,String fileName) throws IOException {
		BufferedReader bufr = null;
		int count = 0;// 定义当行行号
		int error = 0;
		String line = null;// 定义当前行
		String[] name = null;// 定义当前行String数组
		Orderperform orderperform;
		Errorlog errorlog;
		String qsql = "update Uploadfile set isHandle=?0,upCount=?1,upName=?2 where upId=?3";
		int a = 0;
		try {
			File file = new File(path);
			if (file.isFile()) {
				bufr = new BufferedReader(new FileReader(file));
				while ((line = bufr.readLine()) != null) {
					name = line.split("##");
					// 上传文件解析不正确,可能为数据不全
					if (name.length < 13) {
						throw new DataException("正确字段数:13,实际解析字段数:" + name.length + ",字段不全解析停止", null);
					}
					for (int i = 0; i < name.length; i++) {
						if (null != name[i] && name[i].trim().equals("NULL")){
							name[i] = "";
						}
					}
					try{
						orderperform = new Orderperform();
						DecimalFormat def = new DecimalFormat("#.00");
						orderperform.setCusId(cusId);// 获取上级编码
						orderperform.setCusDareway(""); // 医院编码
						orderperform.setCusPid("");// 客户上级id
						orderperform.setDoAddtime(new Date());// 创建日期
						orderperform.setDoPicktime(upCollectdate);// 采集日期从传来的文件采集时间获取
						orderperform.setCaseNo(name[0].trim());// 病案号1
						orderperform.setHospNo(name[1].trim());// 住院号2
						orderperform.setDoNo(name[2].trim());// 医嘱号3
						if(null != name[3] && (name[3].trim().equals("001") || name[3].trim().equals("01") || name[3].trim().equals("1") || name[3].trim().equals("长期医嘱") || name[3].trim().equals("长期"))){
							orderperform.setDoType("长期医嘱");//医嘱类型4
						} else if(null != name[3] && (name[3].trim().equals("002") || name[3].trim().equals("02") || name[3].trim().equals("2") || name[3].trim().equals("临时医嘱") || name[3].trim().equals("临时"))){
							orderperform.setDoType("临时医嘱");
						} else if(null != name[3] && (name[3].trim().equals("003") || name[3].trim().equals("03") || name[3].trim().equals("3") || name[3].trim().equals("备用医嘱") || name[3].trim().equals("备用"))){
							orderperform.setDoType("备用医嘱");
						} else if(null != name[3] && (name[3].trim().equals("") || name[3].trim().equals("NULL"))){
							orderperform.setDoType("");
						} else {
							orderperform.setDoType("其他");
						}
						orderperform.setItemCode(name[4].trim());// 项目编码5
						orderperform.setItemName(name[5].trim());// 项目名称6
						if (name[6].trim().equals("NULL") || name[6].trim().equals("")) {// 单价7
							orderperform.setItemPrice(0.0);
						} else {
							orderperform.setItemPrice(Double.parseDouble(def.format(Double.parseDouble(name[6].trim()))));
						}
						orderperform.setItemFreq(name[7].trim());// 频次8
						orderperform.setItemAmount(name[8].trim());// 用量9
						orderperform.setItmeRoute(name[9].trim());// 用法10
						orderperform.setDoPerform(name[10].trim());// 执行护士11
						orderperform.setDoPerformname(name[11].trim());// 执行护士名称12
						DateFormat df;
						if (name[12].trim().equals("NULL") || name[12].trim().equals("") || name[12].trim().equals("1900-1-1")) {
							orderperform.setDoPerformtime(null);
						} else {
							if (name[12].trim().length() == 10) {
								df = new SimpleDateFormat("yyyy-MM-dd");
							} else if(name[12].trim().length() == 18){
								df = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
							} else if(name[12].trim().length() == 17){
								df = new SimpleDateFormat("dd-MMM-yyHH:mm:ss",Locale.ENGLISH);
							} else {
								df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							}
							orderperform.setDoPerformtime(df.parse(name[12].trim()));// 执行时期13
						}
						if(a<2){
							checkWrongInfo(name, 1014, cusId, path, fileName);
							a++;
						}
						baseDao.save(orderperform);
						count++;
					} catch (Exception e) {
						error++;
						if(error <= 2){//只记录文件的2行错误
							String log = e.toString();
							if (log.length() > 300){
								log = log.substring(0, 300);
							}
							if(log.indexOf("input string")!=-1){
								log = "数字解析出错，数字中包含了字符!";
							}
							if(log.indexOf("empty String")!=-1){
								log = "数字解析出错，存在空值!";
							}
							if(log.indexOf("Unparseable")!=-1){
								log = "日期解析出错，日期格式不正确!";
							}
							errorlog = new Errorlog();
							errorlog.setCusId(cusId);
							errorlog.setCusName(cusName);
							errorlog.setErrAddtime(new Date());
							errorlog.setErrFileflag("医嘱执行记录");
							errorlog.setErrFilepath(path);
							errorlog.setErrLog(log + "  \n错误行号:" + (count+error));
							errorlog.setErrType("数据解析错误");
							baseDao.save(errorlog);
						}
					}
				}
				if(count ==0 && error > 0){
					baseDao.updateOrDelete(qsql, -1,0,getInfo(count, error),upId);//全部失败
				} else {
					baseDao.updateOrDelete(qsql, 1,count,getInfo(count, error),upId);//全部成功/有成功有失败
				}
			} else {
				baseDao.updateOrDelete(qsql,-1,0,"<font color='red'>文件不存在，请使用服务器地址解析</font>", upId);
			}
		} catch (Exception e) {
			String log = e.toString();
			if (log.length() > 300){
				log = log.substring(0, 300);
			}
			
			errorlog = new Errorlog();
			errorlog.setCusId(cusId);
			errorlog.setCusName(cusName);
			errorlog.setErrAddtime(new Date());
			errorlog.setErrFileflag("医嘱执行记录");
			errorlog.setErrFilepath(fileName);
			if (null !=name && name.length < 13) {
				errorlog.setErrType("数据上传字段不全错误");
				errorlog.setErrLog(log);
				baseDao.updateOrDelete(qsql,-1,0,"<font color='red'>数据上传字段不全错误,正确字段数:13,实际解析字段数:" + name.length + "</font>", upId);
			} else {
				errorlog.setErrType("数据解析错误");
				errorlog.setErrLog(log + "\n错误行号:" + (count+1) + "  当前错误行内容:\n" + line);
				baseDao.updateOrDelete(qsql,-1,0,"<font color='red'>数据解析错误</font>", upId);
			}
			baseDao.save(errorlog);
		} finally {
			if (bufr != null) {
				bufr.close();
			}
		}
	}
	
	/**
	 * 添加科室信息
	 */
	@Override
	public void addDepartment(String path, String cusId, String cusName, Date upCollectdate, int upId, String cusFlag,String fileName) throws IOException {
		BufferedReader bufr = null;
		int count = 0;// 定义当行行号
		int error = 0;
		String line = null;// 定义当前行
		String[] name = null;// 定义当前行String数组
		Department department;
		Errorlog errorlog;
		String qsql = "update Uploadfile set isHandle=?0,upCount=?1,upName=?2 where upId=?3";
		int a = 0;
		try {
			File file = new File(path);
			if (file.isFile()) {
				bufr = new BufferedReader(new FileReader(file));
				while ((line = bufr.readLine()) != null) {
					name = line.split("##");
					// 上传文件解析不正确,可能为数据不全
					if (name.length < 5) {
						throw new DataException("正确字段总数:5,实际解析字段数:" + name.length + ",字段不全解析停止", null);
					}
					for (int i = 0; i < name.length; i++) {
						if (null != name[i] && name[i].trim().equals("NULL")){
							name[i] = "";
						}
					}
					try{
						//判断定点是否连锁,数据解析至分店cusid下
						// 判断定点类型是连锁药店并且上传有最后一位分店编码
						department = new Department();
						if (cusFlag.equals("102") && name.length > 5) {// length:25
							// 分店标识为零,则保存在总店id下
							if (name[5].trim().equals("0")) {
								department.setCusId(cusId);
							} else {
								// 查找分店id,父id是当前cusid,分店编码为name[]最后一位
								String hql = "select cusId from Customer where cusParentid =?0 and cusBranchcode=?1 and cusStatus !=-1";
								List list = baseDao.find(hql, cusId,name[5].trim());
								if (list.size() > 0) {
									// 分店已注册查找到分店id
									department.setCusId(list.get(0).toString());
								} else {// 如果当前分店编码未注册,执行下次循环
									error++;
									continue;
								}
							}
						} else {
							department.setCusId(cusId);
						}
						department.setCusDareway(""); // 医院编码
						department.setCusPid("");// 客户上级id
						department.setDeptAddtime(new Date());// 创建日期
						department.setDeptPicktime(upCollectdate);// 采集日期从传来的文件采集时间获取
						department.setDeptCode(name[0].trim());// 科室编码1
						department.setDeptName(name[1].trim());// 科室名称2
						department.setDeptBeds(name[2].trim());// 床位数3
						department.setDeptEmps(name[3].trim());// 医护人员数4
						department.setDeptStatus(name[4].trim());// 状态5
						if(a<2){
							checkWrongInfo(name, 1009, cusId, path, fileName);
							a++;
						}
						baseDao.save(department);
						count++;
					} catch (Exception e) {
						error++;
						if(error <= 2){//只记录文件的2行错误
							String log = e.toString();
							if (log.length() > 300){
								log = log.substring(0, 300);
							}
							if(log.indexOf("input string")!=-1){
								log = "数字解析出错，数字中包含了字符!";
							}
							if(log.indexOf("empty String")!=-1){
								log = "数字解析出错，存在空值!";
							}
							if(log.indexOf("Unparseable")!=-1){
								log = "日期解析出错，日期格式不正确!";
							}
							errorlog = new Errorlog();
							errorlog.setCusId(cusId);
							errorlog.setCusName(cusName);
							errorlog.setErrAddtime(new Date());
							errorlog.setErrFileflag("科室信息");
							errorlog.setErrFilepath(path);
							errorlog.setErrLog(log + "  \n错误行号:" + (count+error));
							errorlog.setErrType("数据解析错误");
							baseDao.save(errorlog);
						}
					}
				}
				if(count ==0 && error > 0){
					baseDao.updateOrDelete(qsql, -1,0,getInfo(count, error),upId);//全部失败
				} else {
					baseDao.updateOrDelete(qsql, 1,count,getInfo(count, error),upId);//全部成功/有成功有失败
				}
			} else {
				baseDao.updateOrDelete(qsql,-1,0,"<font color='red'>文件不存在，请使用服务器地址解析</font>", upId);
			}
		} catch (Exception e) {
			String log = e.toString();
			if (log.length() > 300){
				log = log.substring(0, 300);
			}
			errorlog = new Errorlog();
			errorlog.setCusId(cusId);
			errorlog.setCusName(cusName);
			errorlog.setErrAddtime(new Date());
			errorlog.setErrFileflag("科室信息");
			errorlog.setErrFilepath(fileName);
			if (null !=name && name.length < 5) {
				errorlog.setErrType("数据上传字段不全错误");
				errorlog.setErrLog(log);
				baseDao.updateOrDelete(qsql,-1,0,"<font color='red'>数据上传字段不全错误,正确字段数:5,实际解析字段数:" + name.length + "</font>", upId);
			} else {
				errorlog.setErrType("数据解析错误");
				errorlog.setErrLog(log + "\n错误行号:" + (count+1) + "  当前错误行内容:\n" + line);
				baseDao.updateOrDelete(qsql,-1,0,"<font color='red'>数据解析错误</font>", upId);
			}
			baseDao.save(errorlog);
		} finally {
			if (bufr != null) {
				bufr.close();
			}
		}
	}
	
	/**
	 * 添加诊断记录
	 */
	@Override
	public void addClinicrecords(String path, String cusId, String cusName, Date upCollectdate, int upId,String fileName) throws IOException {
		BufferedReader bufr = null;
		int count = 0;// 定义当行行号
		int error = 0;
		String line = null;// 定义当前行
		String[] name = null;// 定义当前行String数组
		Clinicrecords clinicrecords;
		Errorlog errorlog;
		String qsql = "update Uploadfile set isHandle=?0,upCount=?1,upName=?2 where upId=?3";
		int a = 0;
		try {
			File file = new File(path);
			if (file.isFile()) {
				bufr = new BufferedReader(new FileReader(file));
				while ((line = bufr.readLine()) != null) {
					name = line.split("##");
					// 上传文件解析不正确,可能为数据不全
					if (name.length < 18) {
						throw new DataException("正确字段数:18,实际解析字段数:" + name.length + ",字段不全解析停止", null);
					}
					for (int i = 0; i < name.length; i++) {
						if (null != name[i] && name[i].trim().equals("NULL")){
							name[i] = "";
						}
					}
					try{
						clinicrecords = new Clinicrecords();
						clinicrecords.setCusId(cusId);// 获取上级编码
						clinicrecords.setCusDareway(""); // 医院编码
						clinicrecords.setCusPid("");// 客户上级id
						clinicrecords.setDiagAddtime(new Date());// 创建日期
						clinicrecords.setDiagPicktime(upCollectdate);// 采集日期从传来的文件采集时间获取
						clinicrecords.setDiagNo(name[0].trim());// 门诊编号 1
						DateFormat df;
						if (name[1].trim().equals("NULL") || name[1].trim().equals("") || name[1].trim().equals("1900-1-1")) {
							clinicrecords.setDiagDatetime(null);
							clinicrecords.setDiagDoctortime(null);
						} else {
							if (name[1].trim().length() == 10) {
								df = new SimpleDateFormat("yyyy-MM-dd");
							} else if(name[1].trim().length() == 18){
								df = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
							} else if(name[1].trim().length() == 17){
								df = new SimpleDateFormat("dd-MMM-yyHH:mm:ss",Locale.ENGLISH);
							} else {
								df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							}
							clinicrecords.setDiagDatetime(df.parse(name[1].trim()));// 挂号日期 2
							clinicrecords.setDiagDoctortime(df.parse(name[1].trim()));// 诊断日期 7
						}
						clinicrecords.setDiagDeptcode(name[2].trim());// 科室代码 3
						clinicrecords.setDiagDeptname(name[3].trim());// 科室名称 4
						clinicrecords.setDiagDoctor(name[4].trim());// 医师代码 5
						clinicrecords.setDiagDoctorname(name[5].trim());// 医师名称 6
						clinicrecords.setSiPtsidcard(name[7].trim());// 个人编号 8
						clinicrecords.setSiPtshealthcard(name[8].trim());// 医保卡号9
						clinicrecords.setSiPtsname(name[9].trim());// 姓名 10
						if (name[10].trim().equals("NULL") || name[10].trim().equals("")) {// 性别11
							clinicrecords.setSiPtssex(0);
						} else if (name[10].trim().contains("男")) {
							clinicrecords.setSiPtssex(1);
						} else if (name[10].trim().contains("女")) {
							clinicrecords.setSiPtssex(2);
						} else {
							clinicrecords.setSiPtssex(Integer.parseInt(name[10].trim()));
						}
						clinicrecords.setSiPtsage(name[11].trim());// 年龄 12
						clinicrecords.setDiagIcdcode(name[12].trim());// 诊断代码 13
						clinicrecords.setDiagIcdname(name[13].trim());// 诊断名称14
						clinicrecords.setRegType(name[14].trim());// 挂号类型 15
						clinicrecords.setDiagType(name[15].trim());// 就诊方式 16
						clinicrecords.setSecType(name[16].trim());// 险种类别 17
						clinicrecords.setDiagPaytype(name[17].trim());// 医疗类别 18
						if(a<2){
							checkWrongInfo(name, 1015, cusId, path, fileName);
							a++;
						}
						baseDao.save(clinicrecords);
						count++;
					} catch (Exception e) {
						error++;
						if(error <= 2){//只记录文件的2行错误
							String log = e.toString();
							if (log.length() > 300){
								log = log.substring(0, 300);
							}
							if(log.indexOf("input string")!=-1){
								log = "数字解析出错，数字中包含了字符!";
							}
							if(log.indexOf("empty String")!=-1){
								log = "数字解析出错，存在空值!";
							}
							if(log.indexOf("Unparseable")!=-1){
								log = "日期解析出错，日期格式不正确!";
							}
							errorlog = new Errorlog();
							errorlog.setCusId(cusId);
							errorlog.setCusName(cusName);
							errorlog.setErrAddtime(new Date());
							errorlog.setErrFileflag("诊断记录");
							errorlog.setErrFilepath(path);
							errorlog.setErrLog(log + "  \n错误行号:" + (count+error));
							errorlog.setErrType("数据解析错误");
							baseDao.save(errorlog);
						}
					}
				}
				if(count ==0 && error > 0){
					baseDao.updateOrDelete(qsql, -1,0,getInfo(count, error),upId);//全部失败
				} else {
					baseDao.updateOrDelete(qsql, 1,count,getInfo(count, error),upId);//全部成功/有成功有失败
				}
			} else {
				baseDao.updateOrDelete(qsql,-1,0,"<font color='red'>文件不存在，请使用服务器地址解析</font>", upId);
			}
		} catch (Exception e) {
			String log = e.toString();
			if (log.length() > 300){
				log = log.substring(0, 300);
			}
			errorlog = new Errorlog();
			errorlog.setCusId(cusId);
			errorlog.setCusName(cusName);
			errorlog.setErrAddtime(new Date());
			errorlog.setErrFileflag("诊断记录");
			errorlog.setErrFilepath(fileName);
			if (null !=name && name.length < 18) {
				errorlog.setErrType("数据上传字段不全错误");
				errorlog.setErrLog(log);
				baseDao.updateOrDelete(qsql,-1,0,"<font color='red'>数据上传字段不全错误,正确字段数:18,实际解析字段数:" + name.length + "</font>", upId);
			} else {
				errorlog.setErrType("数据解析错误");
				errorlog.setErrLog(log + "\n错误行号:" + (count+1) + "  当前错误行内容:\n" + line);
				baseDao.updateOrDelete(qsql,-1,0,"<font color='red'>数据解析错误</font>", upId);
			}
			baseDao.save(errorlog);
		} finally {
			if (bufr != null) {
				bufr.close();
			}
		}
	}
	
	/**
	 * 添加病案首页(概要)
	 */
	@Override
	public void addMedrecords(String path, String cusId, String cusName, Date upCollectdate, int upId,String fileName) throws IOException, ParseException {
		BufferedReader bufr = null;
		int count = 0;// 定义当行行号
		int error = 0;
		String line = null;// 定义当前行
		String[] name = null;// 定义当前行String数组
		Medrecords medrecords;
		Errorlog errorlog;
		String qsql = "update Uploadfile set isHandle=?0,upCount=?1,upName=?2 where upId=?3";
		try {
			File file = new File(path);
			if (file.isFile()) {
				bufr = new BufferedReader(new FileReader(file));
				while ((line = bufr.readLine()) != null) {
					name = line.split("##");
					// 上传文件解析不正确,可能为数据不全
					if (name.length < 97) {
						throw new DataException("正确字段数:97,实际解析字段数:" + name.length + ",字段不全解析停止", null);
					}
					for (int i = 0; i < name.length; i++) {
						if (null != name[i] && name[i].trim().equals("NULL")){
							name[i] = "";
						}
					}
					try{
						medrecords = new Medrecords();
						medrecords.setCusId(cusId);// 获取上级编码
						medrecords.setCusDareway(""); // 医院编码
						medrecords.setMerAddtime(new Date());// 创建日期
						medrecords.setMerPicktime(upCollectdate);// 采集日期从传来的文件采集时间获取
						medrecords.setMerOrgan(name[0].trim());
						medrecords.setMerOrgancode(name[1].trim());
						medrecords.setMerPaytype(name[2].trim());
						medrecords.setMerHosptimes(name[3].trim());
						medrecords.setMerCaseno(name[4].trim());
						medrecords.setMerHospno(name[5].trim());
						medrecords.setMerPtsname(name[6].trim());
						medrecords.setMerPtssex(name[7].trim());
						medrecords.setMerBirthday(name[8].trim());
						medrecords.setMerAge(name[9].trim());
						medrecords.setMerCountry(name[10].trim());
						medrecords.setMerWeight(name[11].trim());
						medrecords.setMerBirplace(name[12].trim());
						medrecords.setMerProplace(name[13].trim());
						medrecords.setMerNation(name[14].trim());
						medrecords.setMerIdnumber(name[15].trim());
						medrecords.setMerProfession(name[16].trim());
						medrecords.setMerMarstatus(name[17].trim());
						medrecords.setMerNowaddr(name[18].trim());
						medrecords.setMerNowphone(name[19].trim());
						medrecords.setMerNowpost(name[20].trim());
						medrecords.setMerHukouaddr(name[21].trim());
						medrecords.setMerHukoupost(name[22].trim());
						medrecords.setMerWorkaddr(name[23].trim());
						medrecords.setMerWorkphone(name[24].trim());
						medrecords.setMerWorkpost(name[25].trim());
						medrecords.setMerContactname(name[26].trim());
						medrecords.setMerRelationship(name[27].trim());
						medrecords.setMerContactaddr(name[28].trim());
						medrecords.setMerContactphone(name[29].trim());
						medrecords.setMerIntype(name[30].trim());
						medrecords.setMerIntime(name[31].trim());
						medrecords.setMerIndept(name[32].trim());
						medrecords.setMerInward(name[33].trim());
						medrecords.setMerTurndept(name[34].trim());
						medrecords.setMerOuttime(name[35].trim());
						medrecords.setMerOutdept(name[36].trim());
						medrecords.setMerOutward(name[37].trim());
						medrecords.setMerIndays(name[38].trim());
						medrecords.setMerClinicdiag(name[39].trim());
						medrecords.setMerClinicdiagcode(name[40].trim());
						medrecords.setMerIndiag(name[41].trim());
						medrecords.setMerInstatus(name[42].trim());
						medrecords.setMerIndiagdate(name[43].trim());
						medrecords.setMerDemagecause(name[44].trim());
						medrecords.setMerDamagecode(name[45].trim());
						medrecords.setMerPathological(name[46].trim());
						medrecords.setMerPathcode(name[47].trim());
						medrecords.setMerPathnum(name[48].trim());
						medrecords.setMerAllergic(name[49].trim());
						medrecords.setMerAllergicname(name[50].trim());
						medrecords.setMerClnicout(name[51].trim());
						medrecords.setMerInout(name[52].trim());
						medrecords.setMerBeforafter(name[53].trim());
						medrecords.setMerClinicpath(name[54].trim());
						medrecords.setMerRadiopath(name[55].trim());
						medrecords.setMerDirector(name[56].trim());
						medrecords.setMerChief(name[57].trim());
						medrecords.setMerAttend(name[58].trim());
						medrecords.setMerResident(name[59].trim());
						medrecords.setMerStudaydoc(name[60].trim());
						medrecords.setMerIntern(name[61].trim());
						medrecords.setMerNurse(name[62].trim());
						medrecords.setMerCoder(name[63].trim());
						medrecords.setMerQualty(name[64].trim());
						medrecords.setMerQcdoc(name[65].trim());
						medrecords.setMerQcnur(name[66].trim());
						medrecords.setMerQcdate(name[67].trim());
						medrecords.setMerOutway(name[68].trim());
						medrecords.setMerReccusname(name[69].trim());
						medrecords.setMerBloodtype(name[70].trim());
						medrecords.setMerRh(name[71].trim());
						medrecords.setMerImporttype(name[72].trim());
						medrecords.setMerImportcount(name[73].trim());
						medrecords.setMerImportresp(name[74].trim());
						medrecords.setMerIncount(name[75].trim());
						medrecords.setMerSelfmoney(name[76].trim());
						medrecords.setMerBedbill(name[77].trim());
						medrecords.setMerNursecost(name[78].trim());
						medrecords.setMerWestern(name[79].trim());
						medrecords.setMerChinese(name[80].trim());
						medrecords.setMerHerb(name[81].trim());
						medrecords.setMerRadiocost(name[82].trim());
						medrecords.setMerTestcost(name[83].trim());
						medrecords.setMerOxycost(name[84].trim());
						medrecords.setMerBloodcost(name[85].trim());
						medrecords.setMerDiagcost(name[86].trim());
						medrecords.setMerOperatcost(name[87].trim());
						medrecords.setMerDeliverycost(name[88].trim());
						medrecords.setMerCheckcost(name[89].trim());
						medrecords.setMerDrugcost(name[90].trim());
						medrecords.setMerInfantcost(name[91].trim());
						medrecords.setMerLacost(name[92].trim());
						medrecords.setMerOther(name[93].trim());
						medrecords.setMerAutopsy(name[94].trim());
						medrecords.setMerSafenum(name[95].trim());
						medrecords.setMerSuccnum(name[96].trim());
						baseDao.save(medrecords);
						count++;
					} catch (Exception e) {
						error++;
						if(error <= 2){//只记录文件的2行错误
							String log = e.toString();
							if (log.length() > 300){
								log = log.substring(0, 300);
							}
							if(log.indexOf("input string")!=-1){
								log = "数字解析出错，数字中包含了字符!";
							}
							if(log.indexOf("empty String")!=-1){
								log = "数字解析出错，存在空值!";
							}
							if(log.indexOf("Unparseable")!=-1){
								log = "日期解析出错，日期格式不正确!";
							}
							errorlog = new Errorlog();
							errorlog.setCusId(cusId);
							errorlog.setCusName(cusName);
							errorlog.setErrAddtime(new Date());
							errorlog.setErrFileflag("病案首页(概要)");
							errorlog.setErrFilepath(path);
							errorlog.setErrLog(log + "  \n错误行号:" + (count+error));
							errorlog.setErrType("数据解析错误");
							baseDao.save(errorlog);
						}
					}
				}
				if(count ==0 && error > 0){
					baseDao.updateOrDelete(qsql, -1,0,getInfo(count, error),upId);//全部失败
				} else {
					baseDao.updateOrDelete(qsql, 1,count,getInfo(count, error),upId);//全部成功/有成功有失败
				}
			} else {
				baseDao.updateOrDelete(qsql,-1,0,"<font color='red'>文件不存在，请使用服务器地址解析</font>", upId);
			}
		} catch (Exception e) {
			String log = e.toString();
			if (log.length() > 300){
				log = log.substring(0, 300);
			}
			errorlog = new Errorlog();
			errorlog.setCusId(cusId);
			errorlog.setCusName(cusName);
			errorlog.setErrAddtime(new Date());
			errorlog.setErrFileflag("病案首页(概要)");
			errorlog.setErrFilepath(fileName);
			if (null !=name && name.length < 97) {
				errorlog.setErrType("数据上传字段不全错误");
				errorlog.setErrLog(log);
				baseDao.updateOrDelete(qsql,-1,0,"<font color='red'>数据上传字段不全错误,正确字段数:97,实际解析字段数:" + name.length + "</font>", upId);
			} else {
				errorlog.setErrType("数据解析错误");
				errorlog.setErrLog(log + "\n错误行号:" + (count+1) + "  当前错误行内容:\n" + line);
				baseDao.updateOrDelete(qsql,-1,0,"<font color='red'>数据解析错误</font>", upId);
			}
			baseDao.save(errorlog);
		} finally {
			if (bufr != null) {
				bufr.close();
			}
		}
	}
	
	/**
	 * 添加病案首页(出院诊断)
	 */
	@Override
	public void addDischargediag(String path, String cusId, String cusName, Date upCollectdate, int upId,String fileName) throws IOException, ParseException {
		BufferedReader bufr = null;
		int count = 0;// 定义当行行号
		int error = 0;
		String line = null;// 定义当前行
		String[] name = null;// 定义当前行String数组
		Dischargediag dischargediag;
		Errorlog errorlog;
		String qsql = "update Uploadfile set isHandle=?0,upCount=?1,upName=?2 where upId=?3";
		int a = 0;
		try {
			File file = new File(path);
			if (file.isFile()) {
				bufr = new BufferedReader(new FileReader(file));
				while ((line = bufr.readLine()) != null) {
					name = line.split("##");
					// 上传文件解析不正确,可能为数据不全
					if (name.length < 6) {
						throw new DataException("正确字段数:6,实际解析字段数:" + name.length + ",字段不全解析停止", null);
					}
					for (int i = 0; i < name.length; i++) {
						if (null != name[i] && name[i].trim().equals("NULL")){
							name[i] = "";
						}
					}
					try{
						dischargediag = new Dischargediag();
						dischargediag.setCusId(cusId);// 获取上级编码
						dischargediag.setCusDareway(""); // 医院编码
						dischargediag.setDdAddtime(new Date());// 创建日期
						dischargediag.setDdPicktime(upCollectdate);// 采集日期从传来的文件采集时间获取
						dischargediag.setDdCaseno(name[0].trim());// 病案号
						dischargediag.setDdHospno(name[1].trim());// 住院号
						dischargediag.setDdDisdiag(name[2].trim());// 出院诊断
						dischargediag.setDdDiseasecode(name[3].trim());// 疾病编码
						dischargediag.setDdOutstatus(name[4].trim());// 出院情况
						dischargediag.setDdDiagtype(name[5].trim());// 诊断类别
						if(a<2){
							checkWrongInfo(name, 1016, cusId, path, fileName);
							a++;
						}
						baseDao.save(dischargediag);
						count++;
					} catch (Exception e) {
						error++;
						if(error <= 2){//只记录文件的2行错误
							String log = e.toString();
							if (log.length() > 300){
								log = log.substring(0, 300);
							}
							if(log.indexOf("input string")!=-1){
								log = "数字解析出错，数字中包含了字符!";
							}
							if(log.indexOf("empty String")!=-1){
								log = "数字解析出错，存在空值!";
							}
							if(log.indexOf("Unparseable")!=-1){
								log = "日期解析出错，日期格式不正确!";
							}
							errorlog = new Errorlog();
							errorlog.setCusId(cusId);
							errorlog.setCusName(cusName);
							errorlog.setErrAddtime(new Date());
							errorlog.setErrFileflag("病案首页(出院诊断)");
							errorlog.setErrFilepath(path);
							errorlog.setErrLog(log + "  \n错误行号:" + (count+error));
							errorlog.setErrType("数据解析错误");
							baseDao.save(errorlog);
						}
					}
				}
				if(count ==0 && error > 0){
					baseDao.updateOrDelete(qsql, -1,0,getInfo(count, error),upId);//全部失败
				} else {
					baseDao.updateOrDelete(qsql, 1,count,getInfo(count, error),upId);//全部成功/有成功有失败
				}
			} else {
				baseDao.updateOrDelete(qsql,-1,0,"<font color='red'>文件不存在，请使用服务器地址解析</font>", upId);
			}
		} catch (Exception e) {
			String log = e.toString();
			if (log.length() > 300){
				log = log.substring(0, 300);
			}
			errorlog = new Errorlog();
			errorlog.setCusId(cusId);
			errorlog.setCusName(cusName);
			errorlog.setErrAddtime(new Date());
			errorlog.setErrFileflag("病案首页(出院诊断)");
			errorlog.setErrFilepath(fileName);
			if (null !=name && name.length < 6) {
				errorlog.setErrType("数据上传字段不全错误");
				errorlog.setErrLog(log);
				baseDao.updateOrDelete(qsql,-1,0,"<font color='red'>数据上传字段不全错误,正确字段数:6,实际解析字段数:" + name.length + "</font>", upId);
			} else {
				errorlog.setErrType("数据解析错误");
				errorlog.setErrLog(log + "\n错误行号:" + (count+1) + "  当前错误行内容:\n" + line);
				baseDao.updateOrDelete(qsql,-1,0,"<font color='red'>数据解析错误</font>", upId);
			}
			baseDao.save(errorlog);
		} finally {
			if (bufr != null) {
				bufr.close();
			}
		}
	}
	
	/**
	 * 添加病案首页(手术操作)
	 */
	@Override
	public void addOperation(String path, String cusId, String cusName, Date upCollectdate, int upId,String fileName) throws IOException, ParseException {
		BufferedReader bufr = null;
		int count = 0;// 定义当行行号
		int error = 0;
		String line = null;// 定义当前行
		String[] name = null;// 定义当前行String数组
		Operation operation;
		Errorlog errorlog;
		String qsql = "update Uploadfile set isHandle=?0,upCount=?1,upName=?2 where upId=?3";
		int a = 0;
		try {
			File file = new File(path);
			if (file.isFile()) {
				bufr = new BufferedReader(new FileReader(file));
				while ((line = bufr.readLine()) != null) {
					name = line.split("##");
					// 上传文件解析不正确,可能为数据不全
					if (name.length < 17) {
						throw new DataException("正确字段数:17,实际解析字段数:" + name.length + ",字段不全解析停止", null);
					}
					for (int i = 0; i < name.length; i++) {
						if (null != name[i] && name[i].trim().equals("NULL")){
							name[i] = "";
						}
					}
					try{
						operation = new Operation();
						operation.setCusId(cusId);// 获取上级编码
						operation.setCusDareway(""); // 医院编码
						operation.setOpAddtime(new Date());// 创建日期
						operation.setOpPicktime(upCollectdate);// 采集日期从传来的文件采集时间获取
						operation.setOpCaseno(name[0].trim());// 病案号
						operation.setOpHospno(name[1].trim());// 住院号
						operation.setOpDate(name[2].trim());// 手术操作日期
						operation.setOpCode(name[3].trim());// 手术操作编码
						operation.setOpName(name[4].trim());// 手术操作名称
						operation.setOpLevel(name[5].trim());// 手术级别
						operation.setOpPersoncode(name[6].trim());// 术者编码
						operation.setOpPerson(name[7].trim());// 术者姓名
						operation.setOpAide1code(name[8].trim());// I 助编码
						operation.setOpAide1(name[9].trim());// I 助姓名
						operation.setOpAide2code(name[10].trim());// II 助编码
						operation.setOpAide2(name[11].trim());// II 助姓名
						operation.setOpOptype(name[12].trim());// 手术操作类型
						operation.setOpAnesmode(name[13].trim());// 手术麻醉方法
						operation.setOpAnescode(name[14].trim());// 麻醉医师编码
						operation.setOpAnes(name[15].trim());// 麻醉医师姓名
						operation.setOpHeallevel(name[16].trim());// 手术切口愈合等级
						if(a<2){
							checkWrongInfo(name, 1017, cusId, path, fileName);
							a++;
						}
						baseDao.save(operation);
						count++;
					} catch (Exception e) {
						error++;
						if(error <= 2){//只记录文件的2行错误
							String log = e.toString();
							if (log.length() > 300){
								log = log.substring(0, 300);
							}
							if(log.indexOf("input string")!=-1){
								log = "数字解析出错，数字中包含了字符!";
							}
							if(log.indexOf("empty String")!=-1){
								log = "数字解析出错，存在空值!";
							}
							if(log.indexOf("Unparseable")!=-1){
								log = "日期解析出错，日期格式不正确!";
							}
							errorlog = new Errorlog();
							errorlog.setCusId(cusId);
							errorlog.setCusName(cusName);
							errorlog.setErrAddtime(new Date());
							errorlog.setErrFileflag("病案首页(手术操作)");
							errorlog.setErrFilepath(path);
							errorlog.setErrLog(log + "  \n错误行号:" + (count+error));
							errorlog.setErrType("数据解析错误");
							baseDao.save(errorlog);
						}
					}
				}
				if(count ==0 && error > 0){
					baseDao.updateOrDelete(qsql, -1,0,getInfo(count, error),upId);//全部失败
				} else {
					baseDao.updateOrDelete(qsql, 1,count,getInfo(count, error),upId);//全部成功/有成功有失败
				}
			} else {
				baseDao.updateOrDelete(qsql,-1,0,"<font color='red'>文件不存在，请使用服务器地址解析</font>", upId);
			}
		} catch (Exception e) {
			String log = e.toString();
			if (log.length() > 300){
				log = log.substring(0, 300);
			}
			errorlog = new Errorlog();
			errorlog.setCusId(cusId);
			errorlog.setCusName(cusName);
			errorlog.setErrAddtime(new Date());
			errorlog.setErrFileflag("病案首页(手术操作)");
			errorlog.setErrFilepath(fileName);
			if (null !=name && name.length < 17) {
				errorlog.setErrType("数据上传字段不全错误");
				errorlog.setErrLog(log);
				baseDao.updateOrDelete(qsql,-1,0,"<font color='red'>数据上传字段不全错误,正确字段数:17,实际解析字段数:" + name.length + "</font>", upId);
			} else {
				errorlog.setErrType("数据解析错误");
				errorlog.setErrLog(log + "\n错误行号:" + (count+1) + "  当前错误行内容:\n" + line);
				baseDao.updateOrDelete(qsql,-1,0,"<font color='red'>数据解析错误</font>", upId);
			}
			baseDao.save(errorlog);
		} finally {
			if (bufr != null) {
				bufr.close();
			}
		}
	}
	
	/**
	 * 添加检查、检验结果
	 */
	@Override
	public void addCheckresult(String path, String cusId, String cusName, Date upCollectdate, int upId,String fileName) throws IOException, ParseException {
		BufferedReader bufr = null;
		int count = 0;// 定义当行行号
		int error = 0;
		String line = null;// 定义当前行
		String[] name = null;// 定义当前行String数组
		Checkresult checkresult;
		Errorlog errorlog;
		String qsql = "update Uploadfile set isHandle=?0,upCount=?1,upName=?2 where upId=?3";
		int a = 0;
		try {
			File file = new File(path);
			if (file.isFile()) {
				bufr = new BufferedReader(new FileReader(file));
				while ((line = bufr.readLine()) != null) {
					name = line.split("##");
					// 上传文件解析不正确,可能为数据不全
					if (name.length < 31) {
						throw new DataException("正确字段数:31,实际解析字段数:" + name.length + ",字段不全解析停止", null);
					}
					for (int i = 0; i < name.length; i++) {
						if (null != name[i] && name[i].trim().equals("NULL")){
							name[i] = "";
						}
					}
					try{
						checkresult = new Checkresult();
						checkresult.setCusId(cusId);// 获取上级编码
						checkresult.setCusDateway(""); // 医院编码
						checkresult.setCrAddtime(new Date());// 创建日期
						checkresult.setCrPicktime(upCollectdate);// 采集日期从传来的文件采集时间获取
						checkresult.setCrCheckres(name[0].trim());// 检查来源
						checkresult.setCrCheckno(name[1].trim());// 检查流水号
						checkresult.setCrCaseno(name[2].trim());// 病案号
						checkresult.setCrHospno(name[3].trim());// 住院号/门诊号
						checkresult.setCrDeptcode(name[4].trim());// 科室编码
						checkresult.setCrDept(name[5].trim());// 科室名称
						checkresult.setCrRoom(name[6].trim());// 房间号
						checkresult.setCrBed(name[7].trim());// 床位号
						checkresult.setCrClinicdiag(name[8].trim());// 临床诊断
						checkresult.setCrCheckposi(name[9].trim());// 检查部位
						checkresult.setCrModel(name[10].trim());// 标本号
						checkresult.setCrModeltype(name[11].trim());// 标本种类
						checkresult.setCrItemcode(name[12].trim());// 项目编码
						checkresult.setCrItemname(name[13].trim());// 项目名称
						checkresult.setCrTool(name[14].trim());// 使用仪器
						checkresult.setCrCheckresult(name[15].trim());// 检查结果
						checkresult.setCrPtsname(name[16].trim());// 患者姓名
						checkresult.setCrPtssex(name[17].trim());// 患者性别
						checkresult.setCrAge(name[18].trim());// 年龄
						checkresult.setCrIdnum(name[19].trim());// 身份证号
						checkresult.setCrHealthcard(name[20].trim());// 医保卡号
						checkresult.setCrSenddate(name[21].trim());// 送检日期
						checkresult.setCrCheckdate(name[22].trim());// 检验日期
						checkresult.setCrReportdate(name[23].trim());// 报告日期
						checkresult.setCrSenddoc(name[24].trim());// 送检医生编码
						checkresult.setCrSenddocno(name[25].trim());// 送检医生名称
						checkresult.setCrCheckdoc(name[26].trim());// 检验医生编码
						checkresult.setCrCheckdocno(name[27].trim());// 检验医生名称
						checkresult.setCrAuditdoc(name[28].trim());// 审核医生编码
						checkresult.setCrAuditdocno(name[29].trim());// 审核医生名称
						checkresult.setCrComment(name[30].trim());// 备注
						if(a<2){
							checkWrongInfo(name, 1018, cusId, path, fileName);
							a++;
						}
						baseDao.save(checkresult);
						count++;
					} catch (Exception e) {
						error++;
						if(error <= 2){//只记录文件的2行错误
							String log = e.toString();
							if (log.length() > 300){
								log = log.substring(0, 300);
							}
							if(log.indexOf("input string")!=-1){
								log = "数字解析出错，数字中包含了字符!";
							}
							if(log.indexOf("empty String")!=-1){
								log = "数字解析出错，存在空值!";
							}
							if(log.indexOf("Unparseable")!=-1){
								log = "日期解析出错，日期格式不正确!";
							}
							errorlog = new Errorlog();
							errorlog.setCusId(cusId);
							errorlog.setCusName(cusName);
							errorlog.setErrAddtime(new Date());
							errorlog.setErrFileflag("检查检验结果");
							errorlog.setErrFilepath(path);
							errorlog.setErrLog(log + "  \n错误行号:" + (count+error));
							errorlog.setErrType("数据解析错误");
							baseDao.save(errorlog);
						}
					}
				}
				if(count ==0 && error > 0){
					baseDao.updateOrDelete(qsql, -1,0,getInfo(count, error),upId);//全部失败
				} else {
					baseDao.updateOrDelete(qsql, 1,count,getInfo(count, error),upId);//全部成功/有成功有失败
				}
			} else {
				baseDao.updateOrDelete(qsql,-1,0,"<font color='red'>文件不存在，请使用服务器地址解析</font>", upId);
			}
		} catch (Exception e) {
			String log = e.toString();
			if (log.length() > 300){
				log = log.substring(0, 300);
			}
			errorlog = new Errorlog();
			errorlog.setCusId(cusId);
			errorlog.setCusName(cusName);
			errorlog.setErrAddtime(new Date());
			errorlog.setErrFileflag("检查检验结果");
			errorlog.setErrFilepath(fileName);
			if (null !=name && name.length < 31) {
				errorlog.setErrType("数据上传字段不全错误");
				errorlog.setErrLog(log);
				baseDao.updateOrDelete(qsql,-1,0,"<font color='red'>数据上传字段不全错误,正确字段数:,实际解析字段数:31" + name.length + "</font>", upId);
			} else {
				errorlog.setErrType("数据解析错误");
				errorlog.setErrLog(log + "\n错误行号:" + (count+1) + "  当前错误行内容:\n" + line);
				baseDao.updateOrDelete(qsql,-1,0,"<font color='red'>数据解析错误</font>", upId);
			}
			baseDao.save(errorlog);
		} finally {
			if (bufr != null) {
				bufr.close();
			}
		}
	}
	
	/**
	 * 添加检查、检验结果详细
	 */
	@Override
	public void addCheckdetail(String path, String cusId, String cusName, Date upCollectdate, int upId,String fileName) throws IOException, ParseException {
		BufferedReader bufr = null;
		int count = 0;// 定义当行行号
		int error = 0;
		String line = null;// 定义当前行
		String[] name = null;// 定义当前行String数组
		Checkdetail checkdetail;
		Errorlog errorlog;
		String qsql = "update Uploadfile set isHandle=?0,upCount=?1,upName=?2 where upId=?3";
		int a = 0;
		try {
			File file = new File(path);
			if (file.isFile()) {
				bufr = new BufferedReader(new FileReader(file));
				while ((line = bufr.readLine()) != null) {
					name = line.split("##");
					// 上传文件解析不正确,可能为数据不全
					if (name.length < 9) {
						throw new DataException("正确字段总数:9,实际解析字段数:" + name.length + ",字段不全解析停止", null);
					}
					for (int i = 0; i < name.length; i++) {
						if (null != name[i] && name[i].trim().equals("NULL")){
							name[i] = "";
						}
					}
					try{
						checkdetail = new Checkdetail();
						checkdetail.setCusId(cusId);// 获取上级编码
						checkdetail.setCusDareway(""); // 医院编码
						checkdetail.setCtAddtime(new Date());// 创建日期
						checkdetail.setCtPicktime(upCollectdate);// 采集日期从传来的文件采集时间获取
						checkdetail.setCtCheckno(name[0].trim());// 检查流水号
						checkdetail.setCtHospno(name[1].trim());// 住院号/门诊号
						checkdetail.setCtItemcode(name[2].trim());// 项目编码
						checkdetail.setCtItemname(name[3].trim());// 项目名称
						checkdetail.setCtResult(name[4].trim());// 结果
						checkdetail.setCtUnit(name[5].trim());// 单位
						checkdetail.setCtPoint(name[6].trim());// 提示
						checkdetail.setCtRange(name[7].trim());// 参考范围
						checkdetail.setCtComment(name[8].trim());// 备注
						if(a<2){
							checkWrongInfo(name, 1019, cusId, path, fileName);
							a++;
						}
						baseDao.save(checkdetail);
						count++;
					} catch (Exception e) {
						error++;
						if(error <= 2){//只记录文件的2行错误
							String log = e.toString();
							if (log.length() > 300){
								log = log.substring(0, 300);
							}
							errorlog = new Errorlog();
							errorlog.setCusId(cusId);
							errorlog.setCusName(cusName);
							errorlog.setErrAddtime(new Date());
							errorlog.setErrFileflag("检查检验详细");
							errorlog.setErrFilepath(path);
							errorlog.setErrLog(log + "  \n错误行号:" + (count+error));
							errorlog.setErrType("数据解析错误");
							baseDao.save(errorlog);
						}
					}
				}
				if(count ==0 && error > 0){
					baseDao.updateOrDelete(qsql, -1,0,getInfo(count, error),upId);//全部失败
				} else {
					baseDao.updateOrDelete(qsql, 1,count,getInfo(count, error),upId);//全部成功/有成功有失败
				}
			} else {
				baseDao.updateOrDelete(qsql,-1,0,"<font color='red'>文件不存在，请使用服务器地址解析</font>", upId);
			}
		} catch (Exception e) {
			String log = e.toString();
			if (log.length() > 300){
				log = log.substring(0, 300);
			}
			errorlog = new Errorlog();
			errorlog.setCusId(cusId);
			errorlog.setCusName(cusName);
			errorlog.setErrAddtime(new Date());
			errorlog.setErrFileflag("检查检验详细");
			errorlog.setErrFilepath(fileName);
			if (null !=name && name.length < 9) {
				errorlog.setErrType("数据上传字段不全错误");
				errorlog.setErrLog(log);
				baseDao.updateOrDelete(qsql,-1,0,"<font color='red'>数据上传字段不全错误,正确字段数:,实际解析字段数:9" + name.length + "</font>", upId);
			} else {
				errorlog.setErrType("数据解析错误");
				errorlog.setErrLog(log + "\n错误行号:" + (count+1) + "  当前错误行内容:\n" + line);
				baseDao.updateOrDelete(qsql,-1,0,"<font color='red'>数据解析错误</font>", upId);
			}
			baseDao.save(errorlog);
		} finally {
			if (bufr != null) {
				bufr.close();
			}
		}
	}
	
}
