package yibao.yiwei.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * 病案首页(出院诊断)
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "TBL_DISCHARGEDIAG", schema = "YIWEI")
public class Dischargediag implements java.io.Serializable {

	private String ddId;
	private String cusId;
	private String cusDareway;
	private Date ddPicktime;
	private Date ddAddtime;
	private String ddCaseno;//病案号
	private String ddHospno;//住院号 
	private String ddDisdiag;//出院诊断  
	private String ddDiseasecode;//疾病编码 
	private String ddOutstatus;//出院情况 
	private String ddDiagtype;//诊断类别 

	@GenericGenerator(name = "Dischargediag_id", strategy = "guid")
	@Id
	@GeneratedValue(generator = "Dischargediag_id")
	@Column(name = "DD_ID", unique = true, nullable = false, length = 32)
	public String getDdId() {
		return this.ddId;
	}

	public void setDdId(String ddId) {
		this.ddId = ddId;
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

	@Column(name = "DD_PICKTIME", length = 7)
	public Date getDdPicktime() {
		return this.ddPicktime;
	}

	public void setDdPicktime(Date ddPicktime) {
		this.ddPicktime = ddPicktime;
	}

	@Column(name = "DD_ADDTIME", length = 7)
	public Date getDdAddtime() {
		return this.ddAddtime;
	}

	public void setDdAddtime(Date ddAddtime) {
		this.ddAddtime = ddAddtime;
	}

	@Column(name = "DD_CASENO", length = 32)
	public String getDdCaseno() {
		return this.ddCaseno;
	}

	public void setDdCaseno(String ddCaseno) {
		this.ddCaseno = ddCaseno;
	}

	@Column(name = "DD_HOSPNO", length = 32)
	public String getDdHospno() {
		return this.ddHospno;
	}

	public void setDdHospno(String ddHospno) {
		this.ddHospno = ddHospno;
	}

	@Column(name = "DD_DISDIAG", length = 400)
	public String getDdDisdiag() {
		return this.ddDisdiag;
	}

	public void setDdDisdiag(String ddDisdiag) {
		this.ddDisdiag = ddDisdiag;
	}

	@Column(name = "DD_DISEASECODE", length = 32)
	public String getDdDiseasecode() {
		return this.ddDiseasecode;
	}

	public void setDdDiseasecode(String ddDiseasecode) {
		this.ddDiseasecode = ddDiseasecode;
	}

	@Column(name = "DD_OUTSTATUS", length = 400)
	public String getDdOutstatus() {
		return this.ddOutstatus;
	}

	public void setDdOutstatus(String ddOutstatus) {
		this.ddOutstatus = ddOutstatus;
	}

	@Column(name = "DD_DIAGTYPE", length = 64)
	public String getDdDiagtype() {
		return this.ddDiagtype;
	}

	public void setDdDiagtype(String ddDiagtype) {
		this.ddDiagtype = ddDiagtype;
	}

}