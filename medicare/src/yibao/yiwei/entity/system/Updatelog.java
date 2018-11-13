package yibao.yiwei.entity.system;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * 更新日志表
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "TBL_UPDATELOG", schema = "YIWEI")
public class Updatelog implements java.io.Serializable {

	private String ulId;
	private Date ulLasttime;// 上次更新时间
	private String ulVersions;//程序版本号
	private String cusId;//客户id
	private Date ulUpdatetime;// 本次更新时间

	@GenericGenerator(name = "Updatelog_Nid", strategy = "guid")
	@Id
	@GeneratedValue(generator = "Updatelog_Nid")
	@Column(name = "UL_ID", unique = true, nullable = false, length = 32)
	public String getUlId() {
		return this.ulId;
	}

	public void setUlId(String ulId) {
		this.ulId = ulId;
	}

	@Column(name = "UL_LASTTIME", length = 7)
	public Date getUlLasttime() {
		return this.ulLasttime;
	}

	public void setUlLasttime(Date ulLasttime) {
		this.ulLasttime = ulLasttime;
	}

	@Column(name = "UL_VERSIONS", length = 32)
	public String getUlVersions() {
		return this.ulVersions;
	}

	public void setUlVersions(String ulVersions) {
		this.ulVersions = ulVersions;
	}

	@Column(name = "CUS_ID", length = 32)
	public String getCusId() {
		return this.cusId;
	}

	public void setCusId(String cusId) {
		this.cusId = cusId;
	}

	@Column(name = "UL_UPDATETIME", length = 7)
	public Date getUlUpdatetime() {
		return this.ulUpdatetime;
	}

	public void setUlUpdatetime(Date ulUpdatetime) {
		this.ulUpdatetime = ulUpdatetime;
	}

}