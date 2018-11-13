package yibao.yiwei.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * 处方信息
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "TBL_PRESCRIBE", schema = "YIWEI")
public class Prescribe implements java.io.Serializable {

	private String rpId;//处方id
	private String cusId;//客户id
	private String cusPid;//客户上级id
	private String cusDareway;//医院编码
	private String rpNo;//1 处方单号
	private String rpDeptno;//2处方科别
	private String rpDeptname;//3处方科别名称
	private String rpPtsname;//4患者姓名
	private String rpPtssex;//5性别
	private String rpPtsage;//6年龄
	private String rpPtshealthcard;//7医保卡号
	private String rpPtsidcard;//8身份证号
	private String rpPtsbirthday;//9出生日期
	private String rpItemcode;//10项目编码
	private String rpItemname;//11项目名称
	private Double rpItemprice;//12单价
	private Double rpItemnum;//13数量
	private String rpItemspecification;//14规格
	private String rpItemdosageform;//15剂型
	private String rpItemmfrs;//16生产商
	private String rpItemmakein;//17生产地
	private String rpItembatchno;//18批号
	private String rpWhcode;//19仓库编码
	private String rpWhname;//20仓库名称
	private String rpLocation;//21货位/货架号
	private String rpDrugfreq;//22用药频次
	private String rpDrugroute;//23用药途径
	private String rpDrugtime;//24用药时间
	private String rpDrugamount;//25用药量
	private String rpDrcode;//26处方医师
	private String rpDrname;//27处方医师名称
	private Date rpDrtime;//28开具日期
	private String rpAuditcode;//29审核医师
	private String rpAuditname;//30审核医师名称
	private Date rpAudittime;//31审核日期
	private String rpCheckcode;//32核对医师
	private String rpCheckname;//33核对医师名称
	private Date rpChecktime;//34核对日期
	private String rpAnnex;//35附件
	private Date rpPicktime;//采集日期
	private Date rpAddtime;//添加日期

	@GenericGenerator(name = "Prescribe_id", strategy = "guid")
	@Id
	@GeneratedValue(generator = "Prescribe_id")
	@Column(name = "RP_ID", unique = true, nullable = false, length = 32)
	public String getRpId() {
		return this.rpId;
	}

	public void setRpId(String rpId) {
		this.rpId = rpId;
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

	@Column(name = "RP_NO", length = 32)
	public String getRpNo() {
		return this.rpNo;
	}

	public void setRpNo(String rpNo) {
		this.rpNo = rpNo;
	}

	@Column(name = "RP_DEPTNO", length = 32)
	public String getRpDeptno() {
		return this.rpDeptno;
	}

	public void setRpDeptno(String rpDeptno) {
		this.rpDeptno = rpDeptno;
	}

	@Column(name = "RP_DEPTNAME", length = 32)
	public String getRpDeptname() {
		return this.rpDeptname;
	}

	public void setRpDeptname(String rpDeptname) {
		this.rpDeptname = rpDeptname;
	}

	@Column(name = "RP_PTSNAME", length = 32)
	public String getRpPtsname() {
		return this.rpPtsname;
	}

	public void setRpPtsname(String rpPtsname) {
		this.rpPtsname = rpPtsname;
	}

	@Column(name = "RP_PTSSEX", length = 32)
	public String getRpPtssex() {
		return this.rpPtssex;
	}

	public void setRpPtssex(String rpPtssex) {
		this.rpPtssex = rpPtssex;
	}

	@Column(name = "RP_PTSAGE", length = 32)
	public String getRpPtsage() {
		return this.rpPtsage;
	}

	public void setRpPtsage(String rpPtsage) {
		this.rpPtsage = rpPtsage;
	}

	@Column(name = "RP_PTSHEALTHCARD", length = 32)
	public String getRpPtshealthcard() {
		return this.rpPtshealthcard;
	}

	public void setRpPtshealthcard(String rpPtshealthcard) {
		this.rpPtshealthcard = rpPtshealthcard;
	}

	@Column(name = "RP_PTSIDCARD", length = 32)
	public String getRpPtsidcard() {
		return this.rpPtsidcard;
	}

	public void setRpPtsidcard(String rpPtsidcard) {
		this.rpPtsidcard = rpPtsidcard;
	}

	@Column(name = "RP_PTSBIRTHDAY", length = 32)
	public String getRpPtsbirthday() {
		return this.rpPtsbirthday;
	}

	public void setRpPtsbirthday(String rpPtsbirthday) {
		this.rpPtsbirthday = rpPtsbirthday;
	}

	@Column(name = "RP_ITEMCODE", length = 32)
	public String getRpItemcode() {
		return this.rpItemcode;
	}

	public void setRpItemcode(String rpItemcode) {
		this.rpItemcode = rpItemcode;
	}

	@Column(name = "RP_ITEMNAME", length = 128)
	public String getRpItemname() {
		return this.rpItemname;
	}

	public void setRpItemname(String rpItemname) {
		this.rpItemname = rpItemname;
	}

	@Column(name = "RP_ITEMPRICE", precision = 8)
	public Double getRpItemprice() {
		return this.rpItemprice;
	}

	public void setRpItemprice(Double rpItemprice) {
		this.rpItemprice = rpItemprice;
	}

	@Column(name = "RP_ITEMNUM", precision = 126, scale = 0)
	public Double getRpItemnum() {
		return this.rpItemnum;
	}

	public void setRpItemnum(Double rpItemnum) {
		this.rpItemnum = rpItemnum;
	}

	@Column(name = "RP_ITEMSPECIFICATION", length = 256)
	public String getRpItemspecification() {
		return this.rpItemspecification;
	}

	public void setRpItemspecification(String rpItemspecification) {
		this.rpItemspecification = rpItemspecification;
	}

	@Column(name = "RP_ITEMDOSAGEFORM", length = 64)
	public String getRpItemdosageform() {
		return this.rpItemdosageform;
	}

	public void setRpItemdosageform(String rpItemdosageform) {
		this.rpItemdosageform = rpItemdosageform;
	}

	@Column(name = "RP_ITEMMFRS", length = 240)
	public String getRpItemmfrs() {
		return this.rpItemmfrs;
	}

	public void setRpItemmfrs(String rpItemmfrs) {
		this.rpItemmfrs = rpItemmfrs;
	}

	@Column(name = "RP_ITEMMAKEIN", length = 120)
	public String getRpItemmakein() {
		return this.rpItemmakein;
	}

	public void setRpItemmakein(String rpItemmakein) {
		this.rpItemmakein = rpItemmakein;
	}

	@Column(name = "RP_ITEMBATCHNO", length = 32)
	public String getRpItembatchno() {
		return this.rpItembatchno;
	}

	public void setRpItembatchno(String rpItembatchno) {
		this.rpItembatchno = rpItembatchno;
	}

	@Column(name = "RP_WHCODE", length = 32)
	public String getRpWhcode() {
		return this.rpWhcode;
	}

	public void setRpWhcode(String rpWhcode) {
		this.rpWhcode = rpWhcode;
	}

	@Column(name = "RP_WHNAME", length = 64)
	public String getRpWhname() {
		return this.rpWhname;
	}

	public void setRpWhname(String rpWhname) {
		this.rpWhname = rpWhname;
	}

	@Column(name = "RP_LOCATION", length = 32)
	public String getRpLocation() {
		return this.rpLocation;
	}

	public void setRpLocation(String rpLocation) {
		this.rpLocation = rpLocation;
	}

	@Column(name = "RP_DRUGFREQ", length = 32)
	public String getRpDrugfreq() {
		return this.rpDrugfreq;
	}

	public void setRpDrugfreq(String rpDrugfreq) {
		this.rpDrugfreq = rpDrugfreq;
	}

	@Column(name = "RP_DRUGROUTE", length = 32)
	public String getRpDrugroute() {
		return this.rpDrugroute;
	}

	public void setRpDrugroute(String rpDrugroute) {
		this.rpDrugroute = rpDrugroute;
	}

	@Column(name = "RP_DRUGTIME", length = 32)
	public String getRpDrugtime() {
		return this.rpDrugtime;
	}

	public void setRpDrugtime(String rpDrugtime) {
		this.rpDrugtime = rpDrugtime;
	}

	@Column(name = "RP_DRUGAMOUNT", length = 32)
	public String getRpDrugamount() {
		return this.rpDrugamount;
	}

	public void setRpDrugamount(String rpDrugamount) {
		this.rpDrugamount = rpDrugamount;
	}

	@Column(name = "RP_ANNEX", length = 600)
	public String getRpAnnex() {
		return this.rpAnnex;
	}

	public void setRpAnnex(String rpAnnex) {
		this.rpAnnex = rpAnnex;
	}

	@Column(name = "RP_DRCODE", length = 32)
	public String getRpDrcode() {
		return this.rpDrcode;
	}

	public void setRpDrcode(String rpDrcode) {
		this.rpDrcode = rpDrcode;
	}

	@Column(name = "RP_DRNAME", length = 32)
	public String getRpDrname() {
		return this.rpDrname;
	}

	public void setRpDrname(String rpDrname) {
		this.rpDrname = rpDrname;
	}

	@Column(name = "RP_DRTIME", length = 7)
	public Date getRpDrtime() {
		return this.rpDrtime;
	}

	public void setRpDrtime(Date rpDrtime) {
		this.rpDrtime = rpDrtime;
	}

	@Column(name = "RP_AUDITCODE", length = 32)
	public String getRpAuditcode() {
		return this.rpAuditcode;
	}

	public void setRpAuditcode(String rpAuditcode) {
		this.rpAuditcode = rpAuditcode;
	}

	@Column(name = "RP_AUDITNAME", length = 32)
	public String getRpAuditname() {
		return this.rpAuditname;
	}

	public void setRpAuditname(String rpAuditname) {
		this.rpAuditname = rpAuditname;
	}

	@Column(name = "RP_AUDITTIME", length = 7)
	public Date getRpAudittime() {
		return this.rpAudittime;
	}

	public void setRpAudittime(Date rpAudittime) {
		this.rpAudittime = rpAudittime;
	}

	@Column(name = "RP_CHECKCODE", length = 32)
	public String getRpCheckcode() {
		return this.rpCheckcode;
	}

	public void setRpCheckcode(String rpCheckcode) {
		this.rpCheckcode = rpCheckcode;
	}

	@Column(name = "RP_CHECKNAME", length = 32)
	public String getRpCheckname() {
		return this.rpCheckname;
	}

	public void setRpCheckname(String rpCheckname) {
		this.rpCheckname = rpCheckname;
	}

	@Column(name = "RP_CHECKTIME", length = 7)
	public Date getRpChecktime() {
		return this.rpChecktime;
	}

	public void setRpChecktime(Date rpChecktime) {
		this.rpChecktime = rpChecktime;
	}

	@Column(name = "RP_PICKTIME", length = 7)
	public Date getRpPicktime() {
		return this.rpPicktime;
	}

	public void setRpPicktime(Date rpPicktime) {
		this.rpPicktime = rpPicktime;
	}

	@Column(name = "RP_ADDTIME", length = 7)
	public Date getRpAddtime() {
		return this.rpAddtime;
	}

	public void setRpAddtime(Date rpAddtime) {
		this.rpAddtime = rpAddtime;
	}

}