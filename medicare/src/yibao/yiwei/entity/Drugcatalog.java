package yibao.yiwei.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * 药品目录表(项目编码表)
 * @author Administrator
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "TBL_DRUGCATALOG", schema = "YIWEI")
public class Drugcatalog implements java.io.Serializable {

	private String dcId; // 主键
	private String cusId;// 客户ID
	private String cusParentid;// 客户上级ID 0单体
	private String dcCode;// 项目编码  1
	private String dcCategory;//  项目类别（药品类别） 2
	private String dcCommercial;// 项目名称 3
	private String dcGenericname;// 通用名 4
	private String dcMnemoniccode;// 助记码 5
	private String dcSpecification;// 规格 6
	private String dcUnit;// 最小包装单位(计量)7
	private String dcPrice;// 最小包装单价(零售)8
	private String dcPackagingunit;//  大包装单位9 字符串
	private String dcPackagingprice;// 大包装单价10 小数
	private String dcPackagingnum;//  大包装数量11 小数
	private String dcDosageform;// 剂型 12
	private String dcPermission;// 批准文号 13
	private String dcMfrs;// 生产商 14
	private String dcMakein;// 产地 15
	private String dcHcscode;// 本位码（医保编码）16
	private String dcCatcode;// 17 目录类别  整数
	private String dcSettlementcode;// 18 结算项目编码
	private String dcIsrx;// 19 处方药标识
	private String dcIsspecial;// 20 特药标识
	private String dcIsephedradine;// 21 含麻黄碱标识
	private String dcIsspirit;// 22 精神药品标识
	private String dcIsimport;// 23 进口药标识
	private String dcTaboo;// 24 禁忌描述
	private String dcIndication;// 25 适应症
	private String dcStatus;// 26 状态
	private String cusDareway;// 医院编码
	private Date dcPicktime;// 采集时间
	private Date dcAddtime;// 创建时间

	@Id
	@GenericGenerator(name = "Drugcatalog_gen", strategy = "guid")
	@GeneratedValue(generator = "Drugcatalog_gen")
	@Column(name = "DC_ID", unique = true, nullable = false, length = 32)
	public String getDcId() {
		return this.dcId;
	}

	public void setDcId(String dcId) {
		this.dcId = dcId;
	}

	@Column(name = "DC_CODE", length = 32)
	public String getDcCode() {
		return this.dcCode;
	}

	public void setDcCode(String dcCode) {
		this.dcCode = dcCode;
	}

	@Column(name = "CUS_ID", length = 32)
	public String getCusId() {
		return this.cusId;
	}

	public void setCusId(String cusId) {
		this.cusId = cusId;
	}

	@Column(name = "DC_COMMERCIAL", length = 32)
	public String getDcCommercial() {
		return this.dcCommercial;
	}

	public void setDcCommercial(String dcCommercial) {
		this.dcCommercial = dcCommercial;
	}

	@Column(name = "DC_GENERICNAME", length = 64)
	public String getDcGenericname() {
		return this.dcGenericname;
	}

	public void setDcGenericname(String dcGenericname) {
		this.dcGenericname = dcGenericname;
	}

	@Column(name = "DC_MNEMONICCODE", length = 32)
	public String getDcMnemoniccode() {
		return this.dcMnemoniccode;
	}

	public void setDcMnemoniccode(String dcMnemoniccode) {
		this.dcMnemoniccode = dcMnemoniccode;
	}

	@Column(name = "DC_SPECIFICATION", length = 64)
	public String getDcSpecification() {
		return this.dcSpecification;
	}

	public void setDcSpecification(String dcSpecification) {
		this.dcSpecification = dcSpecification;
	}

	@Column(name = "DC_UNIT", length = 32)
	public String getDcUnit() {
		return this.dcUnit;
	}

	public void setDcUnit(String dcUnit) {
		this.dcUnit = dcUnit;
	}

	@Column(name = "DC_DOSAGEFORM", length = 32)
	public String getDcDosageform() {
		return this.dcDosageform;
	}

	public void setDcDosageform(String dcDosageform) {
		this.dcDosageform = dcDosageform;
	}

	@Column(name = "DC_PERMISSION", length = 64)
	public String getDcPermission() {
		return this.dcPermission;
	}

	public void setDcPermission(String dcPermission) {
		this.dcPermission = dcPermission;
	}

	@Column(name = "DC_MFRS", length = 120)
	public String getDcMfrs() {
		return this.dcMfrs;
	}

	public void setDcMfrs(String dcMfrs) {
		this.dcMfrs = dcMfrs;
	}

	@Column(name = "DC_MAKEIN", length = 120)
	public String getDcMakein() {
		return this.dcMakein;
	}

	public void setDcMakein(String dcMakein) {
		this.dcMakein = dcMakein;
	}

	@Column(name = "DC_HCSCODE", length = 32)
	public String getDcHcscode() {
		return this.dcHcscode;
	}

	public void setDcHcscode(String dcHcscode) {
		this.dcHcscode = dcHcscode;
	}

	@Column(name = "DC_CATEGORY", length = 32)
	public String getDcCategory() {
		return this.dcCategory;
	}

	public void setDcCategory(String dcCategory) {
		this.dcCategory = dcCategory;
	}

	@Column(name = "CUS_PARENTID", length = 32)
	public String getCusParentid() {
		return this.cusParentid;
	}

	public void setCusParentid(String cusParentid) {
		this.cusParentid = cusParentid;
	}

	@Column(name = "CUS_DAREWAY", length = 32)
	public String getCusDareway() {
		return this.cusDareway;
	}

	public void setCusDareway(String cusDareway) {
		this.cusDareway = cusDareway;
	}

	@Column(name = "DC_PICKTIME", length = 7)
	public Date getDcPicktime() {
		return this.dcPicktime;
	}

	public void setDcPicktime(Date dcPicktime) {
		this.dcPicktime = dcPicktime;
	}

	@Column(name = "DC_ADDTIME", length = 7)
	public Date getDcAddtime() {
		return this.dcAddtime;
	}

	public void setDcAddtime(Date dcAddtime) {
		this.dcAddtime = dcAddtime;
	}

	@Column(name = "DC_PRICE", length = 32)
	public String getDcPrice() {
		return this.dcPrice;
	}

	public void setDcPrice(String dcPrice) {
		this.dcPrice = dcPrice;
	}

	@Column(name = "DC_PACKAGINGUNIT", length = 32)
	public String getDcPackagingunit() {
		return this.dcPackagingunit;
	}

	public void setDcPackagingunit(String dcPackagingunit) {
		this.dcPackagingunit = dcPackagingunit;
	}

	@Column(name = "DC_PACKAGINGPRICE", length = 32)
	public String getDcPackagingprice() {
		return this.dcPackagingprice;
	}

	public void setDcPackagingprice(String dcPackagingprice) {
		this.dcPackagingprice = dcPackagingprice;
	}

	@Column(name = "DC_PACKAGINGNUM", length = 32)
	public String getDcPackagingnum() {
		return this.dcPackagingnum;
	}

	public void setDcPackagingnum(String dcPackagingnum) {
		this.dcPackagingnum = dcPackagingnum;
	}

	@Column(name = "DC_CATCODE", length = 32)
	public String getDcCatcode() {
		return this.dcCatcode;
	}

	public void setDcCatcode(String dcCatcode) {
		this.dcCatcode = dcCatcode;
	}

	@Column(name = "DC_SETTLEMENTCODE", length = 32)
	public String getDcSettlementcode() {
		return this.dcSettlementcode;
	}

	public void setDcSettlementcode(String dcSettlementcode) {
		this.dcSettlementcode = dcSettlementcode;
	}

	@Column(name = "DC_ISRX", length = 32)
	public String getDcIsrx() {
		return this.dcIsrx;
	}

	public void setDcIsrx(String dcIsrx) {
		this.dcIsrx = dcIsrx;
	}

	@Column(name = "DC_ISSPECIAL", length = 32)
	public String getDcIsspecial() {
		return this.dcIsspecial;
	}

	public void setDcIsspecial(String dcIsspecial) {
		this.dcIsspecial = dcIsspecial;
	}

	@Column(name = "DC_ISEPHEDRADINE", length = 32)
	public String getDcIsephedradine() {
		return this.dcIsephedradine;
	}

	public void setDcIsephedradine(String dcIsephedradine) {
		this.dcIsephedradine = dcIsephedradine;
	}

	@Column(name = "DC_ISSPIRIT", length = 32)
	public String getDcIsspirit() {
		return this.dcIsspirit;
	}

	public void setDcIsspirit(String dcIsspirit) {
		this.dcIsspirit = dcIsspirit;
	}

	@Column(name = "DC_ISIMPORT", length = 32)
	public String getDcIsimport() {
		return this.dcIsimport;
	}

	public void setDcIsimport(String dcIsimport) {
		this.dcIsimport = dcIsimport;
	}

	@Column(name = "DC_TABOO", length = 32)
	public String getDcTaboo() {
		return this.dcTaboo;
	}

	public void setDcTaboo(String dcTaboo) {
		this.dcTaboo = dcTaboo;
	}

	@Column(name = "DC_INDICATION", length = 32)
	public String getDcIndication() {
		return this.dcIndication;
	}

	public void setDcIndication(String dcIndication) {
		this.dcIndication = dcIndication;
	}

	@Column(name = "DC_STATUS", length = 32)
	public String getDcStatus() {
		return this.dcStatus;
	}

	public void setDcStatus(String dcStatus) {
		this.dcStatus = dcStatus;
	}

}