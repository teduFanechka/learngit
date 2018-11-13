package yibao.yiwei.entity.system;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * 升级方案配置表(有关联表) 
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "TBL_UPDATEPROFILE", schema = "YIWEI")
public class Updateprofile implements java.io.Serializable {

	private String upId;//方案ID
	private String upType;//方案类型 1xx药店  2xx门诊  3xx医院
	private String upVersion;//版本代号
	private String upDescription;//版本描述
	private Date upUpdatetime;//方案默认更新时间
	private int upIsenabled;//是否启用 0否 1是
	private int upIsdefault;//是否默认 0否 1是
	private Date upCreatetime;//创建日期

	@GenericGenerator(name = "Updateprofile_id", strategy = "guid")
	@Id
	@GeneratedValue(generator = "Updateprofile_id")
	@Column(name = "UP_ID", unique = true, nullable = false, length = 32)
	public String getUpId() {
		return this.upId;
	}

	public void setUpId(String upId) {
		this.upId = upId;
	}

	@Column(name = "UP_TYPE", length = 3)
	public String getUpType() {
		return this.upType;
	}

	public void setUpType(String upType) {
		this.upType = upType;
	}

	@Column(name = "UP_VERSION", length = 32)
	public String getUpVersion() {
		return this.upVersion;
	}

	public void setUpVersion(String upVersion) {
		this.upVersion = upVersion;
	}

	@Column(name = "UP_DESCRIPTION")
	public String getUpDescription() {
		return this.upDescription;
	}

	public void setUpDescription(String upDescription) {
		this.upDescription = upDescription;
	}

	@Column(name = "UP_UPDATETIME", length = 7)
	public Date getUpUpdatetime() {
		return this.upUpdatetime;
	}

	public void setUpUpdatetime(Date upUpdatetime) {
		this.upUpdatetime = upUpdatetime;
	}

	@Column(name = "UP_ISENABLED", precision = 22, scale = 0)
	public int getUpIsenabled() {
		return this.upIsenabled;
	}

	public void setUpIsenabled(int upIsenabled) {
		this.upIsenabled = upIsenabled;
	}

	@Column(name = "UP_ISDEFAULT", precision = 22, scale = 0)
	public int getUpIsdefault() {
		return this.upIsdefault;
	}

	public void setUpIsdefault(int upIsdefault) {
		this.upIsdefault = upIsdefault;
	}

	@Column(name = "UP_CREATETIME", length = 7)
	public Date getUpCreatetime() {
		return this.upCreatetime;
	}

	public void setUpCreatetime(Date upCreatetime) {
		this.upCreatetime = upCreatetime;
	}

}