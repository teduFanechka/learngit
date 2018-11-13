package yibao.yiwei.webservice;

public class MyClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	TestWebServiceService twss=new TestWebServiceService();
	TestWebService tws=twss.getTestWebServicePort();
	String result=tws.doTest1("marry");
	System.out.println(result);

	}

}
