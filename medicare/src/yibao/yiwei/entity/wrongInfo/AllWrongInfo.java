package yibao.yiwei.entity.wrongInfo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@SuppressWarnings("serial")
@Entity
@Table(name="TBL_ALLWRONGINFO", schema="YIWEI")
public class AllWrongInfo implements Serializable{
	
	private String wriId;//主键
	private String cusId;//客户id
	private String cusPid;//客户上级id
	private String cusDareway;//医院编码
	private Integer wriCode;//错误类型
	private String wriName;//错误名称
	private String wriFile;//出错文件
	private String wriDescription;//错误描述
	private Integer wriStatus;//当前状态：1---已处理/0---未处理
	private Date wriAddtime;//添加时间
	private String cusContact;//联系人
	private String cusPhone;//联系电话
	private String cusAddress;//联系地址
	private String wriFilepath;//出错的文件路径
	private String cusNo;
	
	@GenericGenerator(name="Allwronginfo_id", strategy="guid")
	@Id
	@GeneratedValue(generator="Allwronginfo_id")
	@Column(name="WRI_ID", unique=true, nullable=false, length=200)
	public String getWriId() {
		return this.wriId;
	}
	
	public void setWriId(String wriId) {
		this.wriId = wriId;
	}
	
	@Column(name="CUS_ID", length=200)
	public String getCusId() {
		return cusId;
	}
	
	public void setCusId(String cusId) {
		this.cusId = cusId;
	}
	
	@Column(name="CUS_PID", length=200)
	public String getCusPid() {
		return cusPid;
	}
	
	public void setCusPid(String cusPid) {
		this.cusPid = cusPid;
	}
	
	@Column(name="CUS_DAREWAY", length=400)
	public String getCusDareway() {
		return cusDareway;
	}
	
	public void setCusDareway(String cusDareway) {
		this.cusDareway = cusDareway;
	}
	
	@Column(name="WRI_CODE")
	public Integer getWriCode() {
		return wriCode;
	}
	
	public void setWriCode(Integer wriCode) {
		this.wriCode = wriCode;
	}
	
	@Column(name="WRI_NAME", length=200)
	public String getWriName() {
		return wriName;
	}
	
	public void setWriName(String wriName) {
		this.wriName = wriName;
	}
	
	@Column(name="WRI_FILE", length=100)
	public String getWriFile() {
		return wriFile;
	}
	
	public void setWriFile(String wriFile) {
		this.wriFile = wriFile;
	}
	
	@Column(name="WRI_DESCRIPTION", length=200)
	public String getWriDescription() {
		return wriDescription;
	}
	
	public void setWriDescription(String wriDescription) {
		this.wriDescription = wriDescription;
	}
	
	@Column(name="WRI_STATUS")
	public Integer getWriStatus() {
		return wriStatus;
	}
	
	public void setWriStatus(Integer wriStatus) {
		this.wriStatus = wriStatus;
	}
	
	@Column(name="WRI_ADDTIME", length=32)
	public Date getWriAddtime() {
		return wriAddtime;
	}
	
	public void setWriAddtime(Date wriAddtime) {
		this.wriAddtime = wriAddtime;
	}
	
	@Column(name="CUS_CONTACT", length=200)
	public String getCusContact() {
		return cusContact;
	}
	
	public void setCusContact(String cusContact) {
		this.cusContact = cusContact;
	}
	
	@Column(name="CUS_PHONE", length=200)
	public String getCusPhone() {
		return cusPhone;
	}
	
	public void setCusPhone(String cusPhone) {
		this.cusPhone = cusPhone;
	}
	
	@Column(name="CUS_ADDRESS", length=400)
	public String getCusAddress() {
		return cusAddress;
	}
	
	public void setCusAddress(String cusAddress) {
		this.cusAddress = cusAddress;
	}
	
	@Column(name="WRI_FILEPATH", length=100)
	public String getWriFilepath() {
		return wriFilepath;
	}

	public void setWriFilepath(String wriFilepath) {
		this.wriFilepath = wriFilepath;
	}

	@Column(name="CUS_NO", length=32)
	public String getCusNo() {
		return cusNo;
	}

	public void setCusNo(String cusNo) {
		this.cusNo = cusNo;
	}
	
}
