package yibao.yiwei.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * 医嘱信息
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "TBL_DOCTORORDER", schema = "YIWEI")
public class Doctororder implements java.io.Serializable {

	private String doId;//主键
	private String cusId;//客户id
	private String cusPid;//客户父id
	private String cusDareway;//医院编码
	private String caseNo;//病案号1
	private String hospNo;//住院号2
	private String doNo;//医嘱号3
	private String doType;//医嘱类型4
	private Date doBegintime;//开立日期5
	private String doDept;//开立科室编码6
	private String doDeptname;//开立科室名称7
	private String doDoctor;//开立医师编码8
	private String doDoctorname;//开立医师姓名9
	private String doCheck;//审核护士编码10
	private String doCheckname;//审核护士姓名11
	private String itemCode;//项目编码12
	private String itemName;//项目名称13
	private Double itemPrice;//单价14
	private String itemFreq;//频次15
	private String itemAmount;//用量16
	private String itmeRoute;//用法17
	private String doEnddr;//停止医师编码18
	private String doEnddrrname;//停止医师姓名19
	private String doEndnurse;//停止护士编码20
	private String doEndnursename;//停止护士姓名21
	private Date doEndtime;//停止日期22
	private Date doPicktime;//采集日期
	private Date doAddtime;//创建日期

	@GenericGenerator(name = "generator", strategy = "guid")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "DO_ID", unique = true, nullable = false, length = 32)
	public String getDoId() {
		return this.doId;
	}

	public void setDoId(String doId) {
		this.doId = doId;
	}

	@Column(name = "CUS_ID", length = 32)
	public String getCusId() {
		return this.cusId;
	}

	public void setCusId(String cusId) {
		this.cusId = cusId;
	}

	@Column(name = "CUS_PID", length = 32)
	public String getCusPid() {
		return this.cusPid;
	}

	public void setCusPid(String cusPid) {
		this.cusPid = cusPid;
	}

	@Column(name = "CUS_DAREWAY", length = 32)
	public String getCusDareway() {
		return this.cusDareway;
	}

	public void setCusDareway(String cusDareway) {
		this.cusDareway = cusDareway;
	}

	@Column(name = "CASE_NO", length = 32)
	public String getCaseNo() {
		return this.caseNo;
	}

	public void setCaseNo(String caseNo) {
		this.caseNo = caseNo;
	}

	@Column(name = "HOSP_NO", length = 32)
	public String getHospNo() {
		return this.hospNo;
	}

	public void setHospNo(String hospNo) {
		this.hospNo = hospNo;
	}

	@Column(name = "DO_NO", length = 32)
	public String getDoNo() {
		return this.doNo;
	}

	public void setDoNo(String doNo) {
		this.doNo = doNo;
	}

	@Column(name = "DO_TYPE", length = 32)
	public String getDoType() {
		return this.doType;
	}

	public void setDoType(String doType) {
		this.doType = doType;
	}

	@Column(name = "DO_BEGINTIME", length = 7)
	public Date getDoBegintime() {
		return this.doBegintime;
	}

	public void setDoBegintime(Date doBegintime) {
		this.doBegintime = doBegintime;
	}

	@Column(name = "DO_DEPT", length = 32)
	public String getDoDept() {
		return this.doDept;
	}

	public void setDoDept(String doDept) {
		this.doDept = doDept;
	}

	@Column(name = "DO_DEPTNAME", length = 32)
	public String getDoDeptname() {
		return this.doDeptname;
	}

	public void setDoDeptname(String doDeptname) {
		this.doDeptname = doDeptname;
	}

	@Column(name = "DO_DOCTOR", length = 32)
	public String getDoDoctor() {
		return this.doDoctor;
	}

	public void setDoDoctor(String doDoctor) {
		this.doDoctor = doDoctor;
	}

	@Column(name = "DO_DOCTORNAME", length = 32)
	public String getDoDoctorname() {
		return this.doDoctorname;
	}

	public void setDoDoctorname(String doDoctorname) {
		this.doDoctorname = doDoctorname;
	}

	@Column(name = "DO_CHECK", length = 32)
	public String getDoCheck() {
		return this.doCheck;
	}

	public void setDoCheck(String doCheck) {
		this.doCheck = doCheck;
	}

	@Column(name = "DO_CHECKNAME", length = 32)
	public String getDoCheckname() {
		return this.doCheckname;
	}

	public void setDoCheckname(String doCheckname) {
		this.doCheckname = doCheckname;
	}

	@Column(name = "ITEM_CODE", length = 32)
	public String getItemCode() {
		return this.itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	@Column(name = "ITEM_NAME", length = 64)
	public String getItemName() {
		return this.itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	@Column(name = "ITEM_PRICE", precision = 8)
	public Double getItemPrice() {
		return this.itemPrice;
	}

	public void setItemPrice(Double itemPrice) {
		this.itemPrice = itemPrice;
	}

	@Column(name = "ITEM_FREQ", length = 32)
	public String getItemFreq() {
		return this.itemFreq;
	}

	public void setItemFreq(String itemFreq) {
		this.itemFreq = itemFreq;
	}

	@Column(name = "ITEM_AMOUNT", length = 32)
	public String getItemAmount() {
		return this.itemAmount;
	}

	public void setItemAmount(String itemAmount) {
		this.itemAmount = itemAmount;
	}

	@Column(name = "ITME_ROUTE", length = 32)
	public String getItmeRoute() {
		return this.itmeRoute;
	}

	public void setItmeRoute(String itmeRoute) {
		this.itmeRoute = itmeRoute;
	}

	@Column(name = "DO_ENDDR", length = 32)
	public String getDoEnddr() {
		return this.doEnddr;
	}

	public void setDoEnddr(String doEnddr) {
		this.doEnddr = doEnddr;
	}

	@Column(name = "DO_ENDDRRNAME", length = 32)
	public String getDoEnddrrname() {
		return this.doEnddrrname;
	}

	public void setDoEnddrrname(String doEnddrrname) {
		this.doEnddrrname = doEnddrrname;
	}

	@Column(name = "DO_ENDNURSE", length = 32)
	public String getDoEndnurse() {
		return this.doEndnurse;
	}

	public void setDoEndnurse(String doEndnurse) {
		this.doEndnurse = doEndnurse;
	}

	@Column(name = "DO_ENDNURSENAME", length = 32)
	public String getDoEndnursename() {
		return this.doEndnursename;
	}

	public void setDoEndnursename(String doEndnursename) {
		this.doEndnursename = doEndnursename;
	}

	@Column(name = "DO_ENDTIME", length = 7)
	public Date getDoEndtime() {
		return this.doEndtime;
	}

	public void setDoEndtime(Date doEndtime) {
		this.doEndtime = doEndtime;
	}

	@Column(name = "DO_PICKTIME", length = 7)
	public Date getDoPicktime() {
		return this.doPicktime;
	}

	public void setDoPicktime(Date doPicktime) {
		this.doPicktime = doPicktime;
	}

	@Column(name = "DO_ADDTIME", length = 7)
	public Date getDoAddtime() {
		return this.doAddtime;
	}

	public void setDoAddtime(Date doAddtime) {
		this.doAddtime = doAddtime;
	}

}