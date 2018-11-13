package yibao.yiwei.entity.system;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 区划
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "TBL_AREACODE", schema = "YIWEI")
public class Areacode implements java.io.Serializable {
	
	private String acId;
	private String acAreacode;//区划编码
	private String acAreaname;//区划名称
	private String acStatus;//状态: 0停用，1 启用
	private Integer acOrder;//排序

	@GenericGenerator(name = "areacode_id", strategy = "guid")
	@Id
	@GeneratedValue(generator = "areacode_id")
	@Column(name = "AC_ID", unique = true, nullable = false, length = 32)
	public String getAcId() {
		return acId;
	}

	public void setAcId(String acId) {
		this.acId = acId;
	}

	@Column(name = "AC_AREACODE", length = 6)
	public String getAcAreacode() {
		return acAreacode;
	}

	public void setAcAreacode(String acAreacode) {
		this.acAreacode = acAreacode;
	}

	@Column(name = "AC_AREANAME", length = 32)
	public String getAcAreaname() {
		return acAreaname;
	}

	public void setAcAreaname(String acAreaname) {
		this.acAreaname = acAreaname;
	}

	@Column(name = "AC_STATUS", length = 32)
	public String getAcStatus() {
		return acStatus;
	}

	public void setAcStatus(String acStatus) {
		this.acStatus = acStatus;
	}

	@Column(name = "AC_ORDER")
	public Integer getAcOrder() {
		return acOrder;
	}

	public void setAcOrder(Integer acOrder) {
		this.acOrder = acOrder;
	}

}
