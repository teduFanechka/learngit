package yibao.yiwei.entity.system;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 角色表
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "SYS_ROLE", schema = "YIWEI")
public class Role implements java.io.Serializable {
	
	private String roId;
	private String roName;//权限名称
	private Integer roOrder;//排序
	private String roStatus;//状态：1启用，0禁用

	@Id
	@GeneratedValue(generator = "role_id")
	@GenericGenerator(name = "role_id", strategy = "guid")
	@Column(name = "RO_ID", unique = true, nullable = false, length = 32)
	public String getRoId() {
		return roId;
	}

	public void setRoId(String roId) {
		this.roId = roId;
	}

	@Column(name = "RO_NAME")
	public String getRoName() {
		return roName;
	}

	public void setRoName(String roName) {
		this.roName = roName;
	}

	@Column(name = "RO_ORDER")
	public Integer getRoOrder() {
		return roOrder;
	}

	public void setRoOrder(Integer roOrder) {
		this.roOrder = roOrder;
	}

	@Column(name = "RO_STATUS")
	public String getRoStatus() {
		return roStatus;
	}

	public void setRoStatus(String roStatus) {
		this.roStatus = roStatus;
	}
	
}