package yibao.yiwei.entity.system;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 系统参数
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "SYS_PARAMETER", schema = "YIWEI")
public class Parameter implements java.io.Serializable {

	private String paramId;
	private String paramKey;
	private String paramValue;

	@Id
	@Column(name = "PARAM_ID", unique = true, nullable = false, length = 32)
	@GenericGenerator(name = "parameter_id", strategy = "guid")
	@GeneratedValue(generator = "parameter_id")
	public String getParamId() {
		return paramId;
	}

	public void setParamId(String paramId) {
		this.paramId = paramId;
	}
	
	
	@Column(name = "PARAM_KEY")
	public String getParamKey() {
		return paramKey;
	}

	public void setParamKey(String paramKey) {
		this.paramKey = paramKey;
	}
	
	@Column(name = "PARAM_VALUE")
	public String getParamValue() {
		return paramValue;
	}

	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}
	
}
