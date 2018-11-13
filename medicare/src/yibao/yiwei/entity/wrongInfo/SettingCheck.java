package yibao.yiwei.entity.wrongInfo;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;


@SuppressWarnings("serial")
@Entity
@Table(name="SYS_SETTINGCHECK", schema="YIWEI")
public class SettingCheck implements Serializable{

	private Integer setId;
	private Integer parentId;
	private String setName;
	private Integer setCode;
	private Integer setNull;
	private Integer setPositive;
	private List<SettingCheck> children;
	
	@GenericGenerator(name="Set_id", strategy="guid")
	@Id
	@GeneratedValue(generator="Set_id")
	@Column(name="SET_ID", unique=true, nullable=false, length=32)
	public Integer getSetId() {
		return setId;
	}
	
	public void setSetId(Integer setId) {
		this.setId = setId;
	}
	
	@Column(name="PARENT_ID", length=32)
	public Integer getParentId() {
		return parentId;
	}
	
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	
	@Column(name="SET_NAME", length=32)
	public String getSetName() {
		return setName;
	}
	
	public void setSetName(String setName) {
		this.setName = setName;
	}
	
	@Column(name="SET_CODE", length=32)
	public Integer getSetCode() {
		return setCode;
	}
	
	public void setSetCode(Integer setCode) {
		this.setCode = setCode;
	}
	
	@Column(name="SET_NULL", length=32)
	public Integer getSetNull() {
		return setNull;
	}
	
	public void setSetNull(Integer setNull) {
		this.setNull = setNull;
	}
	
	@Column(name="SET_POSITIVE", length=32)
	public Integer getSetPositive() {
		return setPositive;
	}
	
	public void setSetPositive(Integer setPositive) {
		this.setPositive = setPositive;
	}
	
	@Transient
	public List<SettingCheck> getChildren() {
		return children;
	}

	public void setChildren(List<SettingCheck> children) {
		this.children = children;
	}
	
	
}
