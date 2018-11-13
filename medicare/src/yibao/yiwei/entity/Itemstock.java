package yibao.yiwei.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * 库存信息
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "TBL_ITEMSTOCK", schema = "YIWEI")
public class Itemstock implements java.io.Serializable {

	private String isId;//主键
	private String cusId;//客户id
	private String cusPid;//客户上级id
	private String cusDareway;//医院编码
	private String itemCode;//项目编码1
	private String itemName;//项目名称2
	private Double itemNum;//库存数量3
	private String itemUnit;//单位4
	private String itemSpecification;//规格5
	private String itemBatchno;//生产批号6
	private String itemPermission;//批准文号7
	private Date itemMfgdate;//生产日期8
	private Date itemExpdate;//有效期9
	private String itemMfrs;//生产商10
	private String itemMakein;//产地11
	private String itemHcscode;//本位码（医保编码）12
	private String itemWhcode;//库房编码13
	private String itemWhname;//库房名称14
	private String itemLocation;//货位/货架号15
	private Date itemPicktime;//采集时间
	private Date itemAddtime;//创建时间

	@GenericGenerator(name = "ItemstockId", strategy = "guid")
	@Id
	@GeneratedValue(generator = "ItemstockId")
	@Column(name = "IS_ID", unique = true, nullable = false, length = 32)
	public String getIsId() {
		return this.isId;
	}

	public void setIsId(String isId) {
		this.isId = isId;
	}

	@Column(name = "CUS_ID", length = 32)
	public String getCusId() {
		return this.cusId;
	}

	public void setCusId(String cusId) {
		this.cusId = cusId;
	}

	@Column(name = "CUS_PID", length = 32)
	public String getCusPid() {
		return this.cusPid;
	}

	public void setCusPid(String cusPid) {
		this.cusPid = cusPid;
	}

	@Column(name = "CUS_DAREWAY", length = 32)
	public String getCusDareway() {
		return this.cusDareway;
	}

	public void setCusDareway(String cusDareway) {
		this.cusDareway = cusDareway;
	}

	@Column(name = "ITEM_CODE", length = 32)
	public String getItemCode() {
		return this.itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	@Column(name = "ITEM_NAME", length = 64)
	public String getItemName() {
		return this.itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	@Column(name = "ITEM_NUM", precision = 126, scale = 0)
	public Double getItemNum() {
		return this.itemNum;
	}

	public void setItemNum(Double itemNum) {
		this.itemNum = itemNum;
	}

	@Column(name = "ITEM_UNIT", length = 32)
	public String getItemUnit() {
		return this.itemUnit;
	}

	public void setItemUnit(String itemUnit) {
		this.itemUnit = itemUnit;
	}

	@Column(name = "ITEM_SPECIFICATION", length = 32)
	public String getItemSpecification() {
		return this.itemSpecification;
	}

	public void setItemSpecification(String itemSpecification) {
		this.itemSpecification = itemSpecification;
	}

	@Column(name = "ITEM_BATCHNO", length = 32)
	public String getItemBatchno() {
		return this.itemBatchno;
	}

	public void setItemBatchno(String itemBatchno) {
		this.itemBatchno = itemBatchno;
	}

	@Column(name = "ITEM_PERMISSION", length = 32)
	public String getItemPermission() {
		return this.itemPermission;
	}

	public void setItemPermission(String itemPermission) {
		this.itemPermission = itemPermission;
	}

	@Column(name = "ITEM_MFGDATE", length = 7)
	public Date getItemMfgdate() {
		return this.itemMfgdate;
	}

	public void setItemMfgdate(Date itemMfgdate) {
		this.itemMfgdate = itemMfgdate;
	}

	@Column(name = "ITEM_EXPDATE", length = 7)
	public Date getItemExpdate() {
		return this.itemExpdate;
	}

	public void setItemExpdate(Date itemExpdate) {
		this.itemExpdate = itemExpdate;
	}

	@Column(name = "ITEM_MFRS", length = 120)
	public String getItemMfrs() {
		return this.itemMfrs;
	}

	public void setItemMfrs(String itemMfrs) {
		this.itemMfrs = itemMfrs;
	}

	@Column(name = "ITEM_MAKEIN", length = 120)
	public String getItemMakein() {
		return this.itemMakein;
	}

	public void setItemMakein(String itemMakein) {
		this.itemMakein = itemMakein;
	}

	@Column(name = "ITEM_HCSCODE", length = 32)
	public String getItemHcscode() {
		return this.itemHcscode;
	}

	public void setItemHcscode(String itemHcscode) {
		this.itemHcscode = itemHcscode;
	}

	@Column(name = "ITEM_WHCODE", length = 32)
	public String getItemWhcode() {
		return this.itemWhcode;
	}

	public void setItemWhcode(String itemWhcode) {
		this.itemWhcode = itemWhcode;
	}

	@Column(name = "ITEM_WHNAME", length = 64)
	public String getItemWhname() {
		return this.itemWhname;
	}

	public void setItemWhname(String itemWhname) {
		this.itemWhname = itemWhname;
	}

	@Column(name = "ITEM_LOCATION", length = 32)
	public String getItemLocation() {
		return this.itemLocation;
	}

	public void setItemLocation(String itemLocation) {
		this.itemLocation = itemLocation;
	}

	@Column(name = "ITEM_PICKTIME", length = 7)
	public Date getItemPicktime() {
		return this.itemPicktime;
	}

	public void setItemPicktime(Date itemPicktime) {
		this.itemPicktime = itemPicktime;
	}

	@Column(name = "ITEM_ADDTIME", length = 7)
	public Date getItemAddtime() {
		return this.itemAddtime;
	}

	public void setItemAddtime(Date itemAddtime) {
		this.itemAddtime = itemAddtime;
	}

}