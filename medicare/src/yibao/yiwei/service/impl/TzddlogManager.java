package yibao.yiwei.service.impl;

import java.util.Date;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import yibao.yiwei.dao.IBaseDao;
import yibao.yiwei.entity.system.Errorlog;
import yibao.yiwei.entity.system.Tzddlog;
import yibao.yiwei.service.ITzddlogManager;
import yibao.yiwei.webservice.DarewayWebServiceClient;

/**
 * 停止定点日志
 * @author Administrator
 *
 */
@SuppressWarnings("unchecked")
@Service
@Transactional
public class TzddlogManager implements ITzddlogManager {
	
	@Autowired
	private IBaseDao baseDao;
	
	/**
	 * 开关医保结算(0暂停，1开启)
	 */
	@Override
	public String updateTzOperate(String tzId, String operate) {
		Tzddlog tzddlog = (Tzddlog) baseDao.get(Tzddlog.class, tzId);
		String result = DarewayWebServiceClient.getDWResult(tzddlog.getTzYybm(), Integer.parseInt(operate));//调用地纬接口
		if(null == result){
			result = "调用地纬接口异常!";
		}
		System.out.println(result);
		String errflag = null;
		String errtext = null;
		try {
			Document document = DocumentHelper.parseText(result);
			Node node1 = document.selectSingleNode("//s[@errflag]");
			Element element1 = (Element)node1;
			errflag = element1.attributeValue("errflag");
			Node node2 = document.selectSingleNode("//s[@errtext]");
		    Element element2 = (Element)node2;
		    errtext = element2.attributeValue("errtext");
		} catch (DocumentException e) {
			e.printStackTrace();
		}  
		
		tzddlog.setTzDate(new Date());
		tzddlog.setTzOperate(operate);
		tzddlog.setTzStatus(errflag);
		tzddlog.setTzNote(errtext);
		baseDao.update(tzddlog);
		
		Errorlog errorlog = new Errorlog();
		errorlog.setCusId(tzId);
		errorlog.setCusName(tzddlog.getTzCusname());
		errorlog.setErrAddtime(new Date());
		errorlog.setErrLog(errtext);
		if(operate.equals("0")){
			errorlog.setErrType("暂停医保结算");
		} else {
			errorlog.setErrType("开启医保结算");
		}
		baseDao.save(errorlog);
		return errflag;
	}
}
