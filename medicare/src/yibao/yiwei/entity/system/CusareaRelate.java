package yibao.yiwei.entity.system;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 定点区划关联表
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "SYS_CUSAREA_RELATE", schema = "YIWEI")
public class CusareaRelate implements java.io.Serializable {
	
	private String acAreacode;
	private String cusId;

	/**
	 * 定义地区编码 acAreacode属性,作为标识属性的成员(联合主键A)
	 * @return
	 */
	@Id
	@Column(name = "AC_AREACODE", nullable = false, length = 6)
	public String getAcAreacode() {
		return this.acAreacode;
	}

	public void setAcAreacode(String acAreacode) {
		this.acAreacode = acAreacode;
	}

	/**
	 * 定义定点id cus_id 属性,作为标识属性的成员(联合主键B)
	 * @return
	 */
	@Id
	@Column(name = "CUS_ID", nullable = false, length = 32)
	public String getCusId() {
		return this.cusId;
	}

	public void setCusId(String cusId) {
		this.cusId = cusId;
	}

}