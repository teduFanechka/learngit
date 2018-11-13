package yibao.yiwei.webservice;


import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.xml.namespace.QName;

import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.rpc.client.RPCServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import yibao.yiwei.entity.system.Parameter;
import yibao.yiwei.service.IBaseService;
import yibao.yiwei.utils.Utils;

/**
 * 地纬停止定点接口
 * 
 * @author Administrator
 */
@SuppressWarnings("unchecked")
@Component
public class DarewayWebServiceClient {

	@Autowired
	private IBaseService baseService;

	private static DarewayWebServiceClient darewayWebServiceClient;
	
	@PostConstruct  
	public void init() { 
		 darewayWebServiceClient = this;
		 darewayWebServiceClient.baseService = this.baseService;
	}
	
	@PreDestroy    
	public void  dostory(){    

	} 
	
	/**
	 * 停止或开启地纬定点资格
	 * @param yybm 医院编码 331067
	 * @param operate 0停止 1 启用
	 * @return String
	 */
	public static String getDWResult(String yybm, int operate) {
		String hql = "from Parameter where paramKey=?0";
		Parameter parameter = (Parameter)darewayWebServiceClient.baseService.findUnique(hql,"dwnote");
		String note = "";
		if(null != parameter){
			note = parameter.getParamValue();
		}
		
		String serviceProxyNameSpace = "http://uddi.dareway.com"; // 命名空间。《固定值》
		String serviceMethod = "invokeService"; // 总线调用方法。《固定值》

		// 总线地址 http://IP:端口/dwlesbserver/services/uddi
		//String serviceUrl = "http://10.115.210.82:8001/dwlesbserver/services/uddi?wsdl";

		// 总线中注册的服务
		String serviceName = "SiService";
		// 总线中提供的操作名
		String operationName;
		// 操作名的入参
		String xmlPara;
		// 昨天日期String
		String 	date = Utils.getStrDate(-1);
		//String note = "";
		if (operate == 0) {
			operationName = "stopInstitution";
			xmlPara = "<?xml version=\"1.0\" encoding=\"GBK\"?><p><s yybm=\"" + yybm + "\" /><s qsrq=\"" + date + "\" /><s bz=\"" + note + "\" /><s zzrq=\"\" /></p>";
		} else {
			operationName = "cancleStopInstitution";
			note = "恢复医保联网结算业务";
			xmlPara = "<?xml version=\"1.0\" encoding=\"GBK\"?><p><s yybm=\"" + yybm + "\" /><s zzrq=\"" + date + "\" /><s bz=\"" + note + "\" /></p>";
		}
		String strResult = null;// 返回操作信息
		// 超时时间
		long waitTime = 600000L;

		// webservice 调用客户端
		RPCServiceClient serviceClient = null;
		try {
			serviceClient = new RPCServiceClient();
			Options options = serviceClient.getOptions();
			options.setTimeOutInMilliSeconds(waitTime); // 设置超时时间
			EndpointReference targetEPR = new EndpointReference("http://10.115.232.21:8001/dwlesbserver/services/uddi?wsdl");
			options.setTo(targetEPR);
			QName qName = new QName(serviceProxyNameSpace, serviceMethod);
			Object[] paras = new Object[] { serviceName, operationName, xmlPara};
			Class<?>[] returnTypes = new Class[] { String.class };
			Object[] response = serviceClient.invokeBlocking(qName, paras, returnTypes);
			Object[] result = response.clone();
			if (result[0] instanceof String) {
				strResult = (String) result[0];
				//strResult = strResult.replace("<", "(").replace(">", ")");  //add by fd 0516 去掉
			}
			//System.out.println(yybm + "操作:" + operate + " " + strResult);
			// 返回值，格式与入参格式相同
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (serviceClient != null) {
				try {
					serviceClient.cleanup();
					serviceClient.cleanupTransport();
					serviceClient = null;
				} catch (AxisFault e) {
					e.printStackTrace();
				}
			}
		}
		return strResult;
	}
}