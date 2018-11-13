package yibao.yiwei.entity.system;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 药品本位码
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "TBL_YPBWM", schema = "YIWEI")
public class Ypbwm implements java.io.Serializable {
	
	private String ypBwm;//本位码
	private Integer ypXh;//序号
	private String ypMc;//名称
	private String ypTymbh;//通用名编号
	private Integer ypBxbl;//报销比例
	private String ypTyymc;//通用名称
	private String ypGg;//规格
	private String ypJx;//剂型
	private String ypPzwh;//批准文号
	private String ypScqy;//生产企业
	private String ypSpm;//商品名
	private String ypPym;//拼音码
	private String ypLx;//类型
	
	@GenericGenerator(name = "ypbwm_id", strategy = "guid")
	@Id
	@GeneratedValue(generator = "ypbwm_id")
	@Column(name = "BWM", unique = true, nullable = false, length = 20)
	public String getYpBwm() {
		return ypBwm;
	}

	public void setYpBwm(String ypBwm) {
		this.ypBwm = ypBwm;
	}

	@Column(name = "XH")
	public Integer getYpXh() {
		return ypXh;
	}

	public void setYpXh(Integer ypXh) {
		this.ypXh = ypXh;
	}

	@Column(name = "MC", length = 80)
	public String getYpMc() {
		return ypMc;
	}

	public void setYpMc(String ypMc) {
		this.ypMc = ypMc;
	}

	@Column(name = "TYMBH", length = 20)
	public String getYpTymbh() {
		return ypTymbh;
	}

	public void setYpTymbh(String ypTymbh) {
		this.ypTymbh = ypTymbh;
	}

	@Column(name = "BXBL")
	public Integer getYpBxbl() {
		return ypBxbl;
	}

	public void setYpBxbl(Integer ypBxbl) {
		this.ypBxbl = ypBxbl;
	}

	@Column(name = "TYYMC", length = 70)
	public String getYpTyymc() {
		return ypTyymc;
	}

	public void setYpTyymc(String ypTyymc) {
		this.ypTyymc = ypTyymc;
	}

	@Column(name = "GG", length = 280)
	public String getYpGg() {
		return ypGg;
	}

	public void setYpGg(String ypGg) {
		this.ypGg = ypGg;
	}

	@Column(name = "JX", length = 40)
	public String getYpJx() {
		return ypJx;
	}

	public void setYpJx(String ypJx) {
		this.ypJx = ypJx;
	}

	@Column(name = "PZWH", length = 50)
	public String getYpPzwh() {
		return ypPzwh;
	}

	public void setYpPzwh(String ypPzwh) {
		this.ypPzwh = ypPzwh;
	}

	@Column(name = "SCQY", length = 100)
	public String getYpScqy() {
		return ypScqy;
	}

	public void setYpScqy(String ypScqy) {
		this.ypScqy = ypScqy;
	}

	@Column(name = "SPM", length = 30)
	public String getYpSpm() {
		return ypSpm;
	}

	public void setYpSpm(String ypSpm) {
		this.ypSpm = ypSpm;
	}

	@Column(name = "PYM", length = 20)
	public String getYpPym() {
		return ypPym;
	}

	public void setYpPym(String ypPym) {
		this.ypPym = ypPym;
	}
	
	@Column(name = "LX", length = 1)
	public String getYpLx() {
		return ypLx;
	}

	public void setYpLx(String ypLx) {
		this.ypLx = ypLx;
	}

}