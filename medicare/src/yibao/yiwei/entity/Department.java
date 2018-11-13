package yibao.yiwei.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * 科室信息
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "TBL_DEPARTMENT", schema = "YIWEI")
public class Department implements java.io.Serializable {

	private String deptId;//主键
	private String cusId;//客户id
	private String cusPid;//客户上级id
	private String cusDareway;//医院编码
	private String deptCode;//科室编码1
	private String deptName;//科室名称2
	private String deptBeds;//床位数3
	private String deptEmps;//医护人员数4
	private String deptStatus;//状态5
	private Date deptPicktime;//采集时间
	private Date deptAddtime;//创建时间

	@GenericGenerator(name = "DepartmentId", strategy = "guid")
	@Id
	@GeneratedValue(generator = "DepartmentId")
	@Column(name = "DEPT_ID", unique = true, nullable = false, length = 32)
	public String getDeptId() {
		return this.deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	@Column(name = "DEPT_CODE", length = 32)
	public String getDeptCode() {
		return this.deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	@Column(name = "DEPT_NAME", length = 32)
	public String getDeptName() {
		return this.deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
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

	@Column(name = "DEPT_BEDS", length = 32)
	public String getDeptBeds() {
		return this.deptBeds;
	}

	public void setDeptBeds(String deptBeds) {
		this.deptBeds = deptBeds;
	}

	@Column(name = "DEPT_EMPS", length = 32)
	public String getDeptEmps() {
		return this.deptEmps;
	}

	public void setDeptEmps(String deptEmps) {
		this.deptEmps = deptEmps;
	}

	@Column(name = "DEPT_PICKTIME", length = 7)
	public Date getDeptPicktime() {
		return this.deptPicktime;
	}

	public void setDeptPicktime(Date deptPicktime) {
		this.deptPicktime = deptPicktime;
	}

	@Column(name = "DEPT_ADDTIME", length = 7)
	public Date getDeptAddtime() {
		return this.deptAddtime;
	}

	public void setDeptAddtime(Date deptAddtime) {
		this.deptAddtime = deptAddtime;
	}

	@Column(name = "DEPT_STATUS", length = 32)
	public String getDeptStatus() {
		return this.deptStatus;
	}

	public void setDeptStatus(String deptStatus) {
		this.deptStatus = deptStatus;
	}

}