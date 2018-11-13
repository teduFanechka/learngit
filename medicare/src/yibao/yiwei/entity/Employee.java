package yibao.yiwei.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * 医护人员信息
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "TBL_EMPLOYEE", schema = "YIWEI")
public class Employee implements java.io.Serializable {

	private String emId;// 医护人员ID
	private String cusId;// 客户ID
	private String cusParentid;// 客户上级ID
	private String emCode;// 1人员编码
	private String emName;// 2人员姓名
	private String emSex;// 3人员性别
	private String emIdnum;// 4身份证号
	private String emTitlecode;// 5职称编码
	private String emTitlename;// 6职称名称
	private String emJobcode;// 7 职务编码
	private String emJobname;// 8 职务名称
	private String emCertified;// 9 医师资格证编号
	private String emLicence;// 10 医师执业证编号
	private String emClassifycode;// 11 执业类别编码
	private String emClassifyname;// 12 执业类别名称
	private String emScopecode;// 13 执业范围编码
	private String emScopename;// 14 执业范围名称
	private String emQualification;// 15从业资格
	private String emPhone;// 16联系电话
	private String deptCode;// 17 科室编码
	private String deptName;// 18 科室名称
	private String emIsexpert;//19 是否专家
	private String emStatus;// 20人员状态
	private String emAnnex;// 21附件
	private String emRemark;// 22备注
	private String acCode;// 统筹区编码
	private String acName;// 统筹区名称
	private Date emPicktime;// 采集时间
	private Date emAddtime;// 创建时间

	@GenericGenerator(name = "Employee_id", strategy = "guid")
	@Id
	@GeneratedValue(generator = "Employee_id")
	@Column(name = "EM_ID", unique = true, nullable = false, length = 32)
	public String getEmId() {
		return this.emId;
	}

	public void setEmId(String emId) {
		this.emId = emId;
	}

	@Column(name = "CUS_ID", length = 32)
	public String getCusId() {
		return this.cusId;
	}

	public void setCusId(String cusId) {
		this.cusId = cusId;
	}

	@Column(name = "CUS_PARENTID", length = 32)
	public String getCusParentid() {
		return this.cusParentid;
	}

	public void setCusParentid(String cusParentid) {
		this.cusParentid = cusParentid;
	}

	@Column(name = "EM_CODE", length = 32)
	public String getEmCode() {
		return this.emCode;
	}

	public void setEmCode(String emCode) {
		this.emCode = emCode;
	}

	@Column(name = "EM_NAME", length = 16)
	public String getEmName() {
		return this.emName;
	}

	public void setEmName(String emName) {
		this.emName = emName;
	}

	@Column(name = "EM_SEX", length = 16)
	public String getEmSex() {
		return this.emSex;
	}

	public void setEmSex(String emSex) {
		this.emSex = emSex;
	}

	@Column(name = "EM_IDNUM", length = 18)
	public String getEmIdnum() {
		return this.emIdnum;
	}

	public void setEmIdnum(String emIdnum) {
		this.emIdnum = emIdnum;
	}

	@Column(name = "EM_TITLENAME", length = 32)
	public String getEmTitlename() {
		return this.emTitlename;
	}

	public void setEmTitlename(String emTitlename) {
		this.emTitlename = emTitlename;
	}

	@Column(name = "EM_QUALIFICATION", length = 32)
	public String getEmQualification() {
		return this.emQualification;
	}

	public void setEmQualification(String emQualification) {
		this.emQualification = emQualification;
	}

	@Column(name = "EM_PHONE", length = 32)
	public String getEmPhone() {
		return this.emPhone;
	}

	public void setEmPhone(String emPhone) {
		this.emPhone = emPhone;
	}

	@Column(name = "EM_ANNEX", length = 120)
	public String getEmAnnex() {
		return this.emAnnex;
	}

	public void setEmAnnex(String emAnnex) {
		this.emAnnex = emAnnex;
	}

	@Column(name = "EM_REMARK")
	public String getEmRemark() {
		return this.emRemark;
	}

	public void setEmRemark(String emRemark) {
		this.emRemark = emRemark;
	}

	@Column(name = "AC_CODE", length = 120)
	public String getAcCode() {
		return this.acCode;
	}

	public void setAcCode(String acCode) {
		this.acCode = acCode;
	}

	@Column(name = "AC_NAME", length = 120)
	public String getAcName() {
		return this.acName;
	}

	public void setAcName(String acName) {
		this.acName = acName;
	}

	@Column(name = "EM_PICKTIME", length = 7)
	public Date getEmPicktime() {
		return this.emPicktime;
	}

	public void setEmPicktime(Date emPicktime) {
		this.emPicktime = emPicktime;
	}

	@Column(name = "EM_ADDTIME", length = 7)
	public Date getEmAddtime() {
		return this.emAddtime;
	}

	public void setEmAddtime(Date emAddtime) {
		this.emAddtime = emAddtime;
	}

	@Column(name = "EM_STATUS", length = 16)
	public String getEmStatus() {
		return this.emStatus;
	}

	public void setEmStatus(String emStatus) {
		this.emStatus = emStatus;
	}

	@Column(name = "EM_TITLECODE", length = 120)
	public String getEmTitlecode() {
		return this.emTitlecode;
	}

	public void setEmTitlecode(String emTitlecode) {
		this.emTitlecode = emTitlecode;
	}

	@Column(name = "EM_JOBCODE", length = 120)
	public String getEmJobcode() {
		return this.emJobcode;
	}

	public void setEmJobcode(String emJobcode) {
		this.emJobcode = emJobcode;
	}

	@Column(name = "EM_JOBNAME", length = 120)
	public String getEmJobname() {
		return this.emJobname;
	}

	public void setEmJobname(String emJobname) {
		this.emJobname = emJobname;
	}

	@Column(name = "EM_CERTIFIED", length = 120)
	public String getEmCertified() {
		return this.emCertified;
	}

	public void setEmCertified(String emCertified) {
		this.emCertified = emCertified;
	}

	@Column(name = "EM_LICENCE", length = 120)
	public String getEmLicence() {
		return this.emLicence;
	}

	public void setEmLicence(String emLicence) {
		this.emLicence = emLicence;
	}

	@Column(name = "EM_CLASSIFYCODE", length = 120)
	public String getEmClassifycode() {
		return this.emClassifycode;
	}

	public void setEmClassifycode(String emClassifycode) {
		this.emClassifycode = emClassifycode;
	}

	@Column(name = "EM_CLASSIFYNAME", length = 120)
	public String getEmClassifyname() {
		return this.emClassifyname;
	}

	public void setEmClassifyname(String emClassifyname) {
		this.emClassifyname = emClassifyname;
	}

	@Column(name = "EM_SCOPECODE", length = 120)
	public String getEmScopecode() {
		return this.emScopecode;
	}

	public void setEmScopecode(String emScopecode) {
		this.emScopecode = emScopecode;
	}

	@Column(name = "EM_SCOPENAME", length = 120)
	public String getEmScopename() {
		return this.emScopename;
	}

	public void setEmScopename(String emScopename) {
		this.emScopename = emScopename;
	}

	@Column(name = "DEPT_CODE", length = 120)
	public String getDeptCode() {
		return this.deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	@Column(name = "DEPT_NAME", length = 120)
	public String getDeptName() {
		return this.deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	@Column(name = "EM_ISEXPERT", length = 120)
	public String getEmIsexpert() {
		return this.emIsexpert;
	}

	public void setEmIsexpert(String emIsexpert) {
		this.emIsexpert = emIsexpert;
	}

}