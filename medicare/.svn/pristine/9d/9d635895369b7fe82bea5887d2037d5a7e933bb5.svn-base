package yibao.yiwei.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * 住院记录
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "TBL_HOSPITALIZED", schema = "YIWEI")
public class Hospitalized implements java.io.Serializable {

	private String hospId;	//主键
	private String cusId;	//客户id
	private String cusDareway;//医院编码
	private Integer doctorType;//就医类别	1
	private String caseNo;//病案号2
	private String hospNo;//住院号3
	private Integer diagType;//接诊方式4
	private String diagDept;//门诊科室编码	5
	private String diagDeptname;//门诊科室名称6
	private String diagDoctor;//确诊医师编码7
	private String diagDoctorname;//确认医师姓名8
	private Date diagDatetime;//确诊日期9
	private String icdCode;//疾病编码	10
	private String icdName;//疾病名称	11
	private String hospType;//住院类别	12
	private String hospWay;//入院方式	13
	private String hospDept;//入院科室编码	14
	private String hospDeptname;//入院科室名称15
	private String hospDoctor;//主冶医师编码16
	private String hospDoctorname;//主冶医师姓名17
	private String hospAreas;//病区名称18
	private String hospWardno;//房间号19
	private String hospBedno;//床位号	20
	private Date hospIntime;//住院日期	21
	private String siPtsname;//患者姓名22
	private Integer siPtssex;//性别23
	private String siPtsage;//年龄24
	private String siPtsidcard;//身份证号	25
	private String siPtshealthcard;//医保卡号26
	private Date hospPicktime;//采集日期
	private Date hospAddtime;//创建日期

	@GenericGenerator(name = "Hospitalized_id", strategy = "guid")
	@Id
	@GeneratedValue(generator = "Hospitalized_id")
	@Column(name = "HOSP_ID", unique = true, nullable = false, length = 32)
	public String getHospId() {
		return this.hospId;
	}

	public void setHospId(String hospId) {
		this.hospId = hospId;
	}

	@Column(name = "CUS_ID", length = 32)
	public String getCusId() {
		return this.cusId;
	}

	public void setCusId(String cusId) {
		this.cusId = cusId;
	}

	@Column(name = "CUS_DAREWAY", length = 32)
	public String getCusDareway() {
		return this.cusDareway;
	}

	public void setCusDareway(String cusDareway) {
		this.cusDareway = cusDareway;
	}

	@Column(name = "DOCTOR_TYPE", precision = 22, scale = 0)
	public Integer getDoctorType() {
		return this.doctorType;
	}

	public void setDoctorType(Integer doctorType) {
		this.doctorType = doctorType;
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

	@Column(name = "DIAG_TYPE", precision = 22, scale = 0)
	public Integer getDiagType() {
		return this.diagType;
	}

	public void setDiagType(Integer diagType) {
		this.diagType = diagType;
	}

	@Column(name = "DIAG_DEPT", length = 32)
	public String getDiagDept() {
		return this.diagDept;
	}

	public void setDiagDept(String diagDept) {
		this.diagDept = diagDept;
	}

	@Column(name = "DIAG_DEPTNAME", length = 32)
	public String getDiagDeptname() {
		return this.diagDeptname;
	}

	public void setDiagDeptname(String diagDeptname) {
		this.diagDeptname = diagDeptname;
	}

	@Column(name = "DIAG_DOCTOR", length = 32)
	public String getDiagDoctor() {
		return this.diagDoctor;
	}

	public void setDiagDoctor(String diagDoctor) {
		this.diagDoctor = diagDoctor;
	}

	@Column(name = "DIAG_DOCTORNAME", length = 32)
	public String getDiagDoctorname() {
		return this.diagDoctorname;
	}

	public void setDiagDoctorname(String diagDoctorname) {
		this.diagDoctorname = diagDoctorname;
	}

	@Column(name = "DIAG_DATETIME", length = 7)
	public Date getDiagDatetime() {
		return this.diagDatetime;
	}

	public void setDiagDatetime(Date diagDatetime) {
		this.diagDatetime = diagDatetime;
	}

	@Column(name = "ICD_CODE", length = 32)
	public String getIcdCode() {
		return this.icdCode;
	}

	public void setIcdCode(String icdCode) {
		this.icdCode = icdCode;
	}

	@Column(name = "ICD_NAME", length = 64)
	public String getIcdName() {
		return this.icdName;
	}

	public void setIcdName(String icdName) {
		this.icdName = icdName;
	}

	@Column(name = "HOSP_TYPE", length = 32)
	public String getHospType() {
		return this.hospType;
	}

	public void setHospType(String hospType) {
		this.hospType = hospType;
	}

	@Column(name = "HOSP_WAY", length = 32)
	public String getHospWay() {
		return this.hospWay;
	}

	public void setHospWay(String hospWay) {
		this.hospWay = hospWay;
	}

	@Column(name = "HOSP_DEPT", length = 32)
	public String getHospDept() {
		return this.hospDept;
	}

	public void setHospDept(String hospDept) {
		this.hospDept = hospDept;
	}

	@Column(name = "HOSP_DEPTNAME", length = 32)
	public String getHospDeptname() {
		return this.hospDeptname;
	}

	public void setHospDeptname(String hospDeptname) {
		this.hospDeptname = hospDeptname;
	}

	@Column(name = "HOSP_DOCTOR", length = 32)
	public String getHospDoctor() {
		return this.hospDoctor;
	}

	public void setHospDoctor(String hospDoctor) {
		this.hospDoctor = hospDoctor;
	}

	@Column(name = "HOSP_DOCTORNAME", length = 32)
	public String getHospDoctorname() {
		return this.hospDoctorname;
	}

	public void setHospDoctorname(String hospDoctorname) {
		this.hospDoctorname = hospDoctorname;
	}

	@Column(name = "HOSP_AREAS", length = 64)
	public String getHospAreas() {
		return this.hospAreas;
	}

	public void setHospAreas(String hospAreas) {
		this.hospAreas = hospAreas;
	}

	@Column(name = "HOSP_WARDNO", length = 32)
	public String getHospWardno() {
		return this.hospWardno;
	}

	public void setHospWardno(String hospWardno) {
		this.hospWardno = hospWardno;
	}

	@Column(name = "HOSP_BEDNO", length = 32)
	public String getHospBedno() {
		return this.hospBedno;
	}

	public void setHospBedno(String hospBedno) {
		this.hospBedno = hospBedno;
	}

	@Column(name = "HOSP_INTIME", length = 7)
	public Date getHospIntime() {
		return this.hospIntime;
	}

	public void setHospIntime(Date hospIntime) {
		this.hospIntime = hospIntime;
	}

	@Column(name = "SI_PTSNAME", length = 32)
	public String getSiPtsname() {
		return this.siPtsname;
	}

	public void setSiPtsname(String siPtsname) {
		this.siPtsname = siPtsname;
	}

	@Column(name = "SI_PTSSEX", precision = 22, scale = 0)
	public Integer getSiPtssex() {
		return this.siPtssex;
	}

	public void setSiPtssex(Integer siPtssex) {
		this.siPtssex = siPtssex;
	}

	@Column(name = "SI_PTSAGE", length = 32)
	public String getSiPtsage() {
		return this.siPtsage;
	}

	public void setSiPtsage(String siPtsage) {
		this.siPtsage = siPtsage;
	}

	@Column(name = "SI_PTSIDCARD", length = 32)
	public String getSiPtsidcard() {
		return this.siPtsidcard;
	}

	public void setSiPtsidcard(String siPtsidcard) {
		this.siPtsidcard = siPtsidcard;
	}

	@Column(name = "SI_PTSHEALTHCARD", length = 32)
	public String getSiPtshealthcard() {
		return this.siPtshealthcard;
	}

	public void setSiPtshealthcard(String siPtshealthcard) {
		this.siPtshealthcard = siPtshealthcard;
	}

	@Column(name = "HOSP_PICKTIME", length = 7)
	public Date getHospPicktime() {
		return this.hospPicktime;
	}

	public void setHospPicktime(Date hospPicktime) {
		this.hospPicktime = hospPicktime;
	}

	@Column(name = "HOSP_ADDTIME", length = 7)
	public Date getHospAddtime() {
		return this.hospAddtime;
	}

	public void setHospAddtime(Date hospAddtime) {
		this.hospAddtime = hospAddtime;
	}

}