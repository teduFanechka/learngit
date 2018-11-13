package yibao.yiwei.webservice;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.Endpoint;
//服务器端 类名为: TestWebService
/**
 * jaxb-api-2.1.jar jaxws-api-2.1-1.jar 所需包 总结：
 * 
 * 如何发布一个Web服务：
 * 
 * a,在类上添加@WebService注解 （注：此注解是jdk1.6提供的，位于javax.jws.WebService包中）
 * 
 * b,通过EndPoint(端点服务)发布一个WebService
 * 
 * （注：EndPoint是jdk提供的一个专门用于发布服务的类，该类的publish方法接收两个参数，一个是本地的服务地址，二是提供服务的类。位于
 * javax.xml.ws.Endpoint包中）
 * 
 * c，注：
 * 
 * 类上添加注解@WebService，类中所有非静态方法都会被发布；
 * 
 * 静态方法和final方法不能被发布；
 * 
 * 方法上加@WebMentod(exclude=true)后，此方法不被发布；
 */
@SOAPBinding(style = SOAPBinding.Style.RPC)
@WebService
public class XXXTestWebService {
	public String doTest1(String name) {
		return "hello" + name;
	}

	/**
	 *添加exclude=true后，HelloWord2()方法不会被发布
	 * 
	 * @param name
	 * @return
	 */
	@WebMethod(exclude = true)
	public String doTest2(String name) {
		return "hello" + name;
	}

	public static void main(String[] args) {
		/**
		 * 参数1:服务器的发布地址 参数2:服务器的实现者
		 */
		Endpoint.publish("http://localhost:7848/TestWebService?wsdl", new XXXTestWebService());
		System.out.println("dook");
	}

}
