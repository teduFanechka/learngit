package yibao.yiwei.entity.system;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import yibao.yiwei.utils.JacksonDateHMSSerializer;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 定点日志表
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "TBL_TZDDLOG", schema = "YIWEI")
public class Tzddlog implements java.io.Serializable {

	private String tzId; // 主键
	private String tzCusid; // 定点cusid
	private String tzCusname;// 定点名称
	private String tzYybm; // 定点编码(不可为空)
	private Date tzDate; // 日期
	private String tzOperate; //定点资格操作类型：0暂停，1开启，9初始化
	private String tzStatus;//地维接口调用结果：0成功，1失败，9初始化
	private String tzScan;//0扫描，1不扫描
	private String tzYbcx;//是否使用医保进销存软件：0是(不要停止资格)，1否
	private String tzNote;// 备注
	
	@GenericGenerator(name = "Tzddlog_id", strategy = "guid")
	@Id
	@GeneratedValue(generator = "Tzddlog_id")
	@Column(name = "TZ_ID", unique = true, nullable = false, length = 32)
	public String getTzId() {
		return this.tzId;
	}

	public void setTzId(String tzId) {
		this.tzId = tzId;
	}

	@Column(name = "TZ_CUSID", length = 32)
	public String getTzCusid() {
		return this.tzCusid;
	}

	public void setTzCusid(String tzCusid) {
		this.tzCusid = tzCusid;
	}

	@Column(name = "TZ_CUSNAME", length = 100)
	public String getTzCusname() {
		return this.tzCusname;
	}

	public void setTzCusname(String tzCusname) {
		this.tzCusname = tzCusname;
	}

	@Column(name = "TZ_YYBM", nullable = false, length = 32)
	public String getTzYybm() {
		return this.tzYybm;
	}

	public void setTzYybm(String tzYybm) {
		this.tzYybm = tzYybm;
	}

	@Column(name = "TZ_OPERATE", length = 1)
	public String getTzOperate() {
		return this.tzOperate;
	}

	public void setTzOperate(String tzOperate) {
		this.tzOperate = tzOperate;
	}

	@Column(name = "TZ_STATUS", length = 1)
	public String getTzStatus() {
		return this.tzStatus;
	}

	public void setTzStatus(String tzStatus) {
		this.tzStatus = tzStatus;
	}

	@Column(name = "TZ_SCAN", length = 1)
	public String getTzScan() {
		return tzScan;
	}

	public void setTzScan(String tzScan) {
		this.tzScan = tzScan;
	}

	@JsonSerialize(using = JacksonDateHMSSerializer.class)
	@Column(name = "TZ_DATE", length = 7)
	public Date getTzDate() {
		return this.tzDate;
	}

	public void setTzDate(Date tzDate) {
		this.tzDate = tzDate;
	}

	@Column(name = "TZ_NOTE", length = 200)
	public String getTzNote() {
		return tzNote;
	}

	public void setTzNote(String tzNote) {
		this.tzNote = tzNote;
	}

	@Column(name = "TZ_YBCX", length = 1)
	public String getTzYbcx() {
		return tzYbcx;
	}

	public void setTzYbcx(String tzYbcx) {
		this.tzYbcx = tzYbcx;
	}

}