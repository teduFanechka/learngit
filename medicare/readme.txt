customercontroller  http://localhost:8080/medicare/dotest   测试jsp
AreacodeController localhost:8080/medicare/setCusAreaOrg	添加定点对应区划,组织关联表
search:判断是否为连锁药店  DrugcatalogController line:342
addOrUpdate   字段调整方法全部失效
***BaseDao find 方法只能用于查询语句,其它改,删方法不能用于find查询
如果调用接口的改,删方法,方法名必须以update\delete 开头,因为事务声明中已限制,否则改删方法不成功
一. web.xml   <servlet-name>medicare</servlet-name>  要改

二.//直接写在jsp页面中用这种方法
		<script type="text/javascript">
			function dodelete(id) {
				var a = window.confirm("确认删除吗?");
				if (a) {
								window.location.href = "${pageContext.request.contextPath }/delHistory?id="+ id;
		//如果写在js中必须用  :	window.location.href = "./delHistory?id="+id;
				}
			}
		</script>
		未测试:$.post(url).success( function() {})
						  .error( function(){});

 三. <a href="<%=request.getContextPath()%>/delHistory?id=${c.id}">删除</a>  //一般方法
		 <a href="javascript:dodelete('${c.id }')">删除</a>  //js 方法
 		下面文件上传时用如果导入 jquery.mis.js 会出现ie8 不支持现象 
 		<script src="${path}/js/jquery.js"></script>
 
 四. <!-- manager.jsp 切换tabs 最后一个tab内容会替换掉第一个tab内容,href 换为ghref属性 并在load.js作修改 -->
 
 	 alert("有几个元素:"+deList.length);

			<script language="script">	$(document).ready(function(){}); </script>
	 		 js 中  $.post 如果需要回调函数获取取后台传来的json 数据 ,要这样写:
					$.post("/medicare/addOrUpdateHistory", {
						json : json							//datagrid 行数据
					}, function(data) {
							if (data.status == "AA") {
								$.messager.alert('提示', "保存成功!", 'info');
								datagrid.datagrid('reload');
							} else if (data.status == "BB") {
								$.messager.alert('提示', "修改成功!", 'info');
								datagrid.datagrid('reload');
							}
					}, "json");
五.		注意事项:1.定时任务获取bean 需要注入
			 2.批量执行save 语句时,要手工操作.
		AnalysisTimerTask  定时任务有些注意事项			 
			 
		 (function(){})表示一个匿名函数。function(arg){...}定义了一个参数为arg的匿名函数，然后使用(function(arg){...})(param)来调用这个匿名函数。其中param是传入这个匿名函数的参数。
		　　需要注意与$(function(){})的区别：$(function(){}) 是 $(document).ready(function(){}) 的简写，用来在DOM加载完成之后执行一系列预先定义好的函数。   
		  <th field="dcId" width="80" align="center" hidden=true > datagrid隐藏列 
  
  六.		对象(含有java.util.Date)，集合等转换成json
		  1.新建工具类 JsonDateValueProcessor
		  2.UploadfileController >toBeJson() 创建 jsonConfig
		  3.修改datagrid 中的  Date: controller >addOrUpdateUploadfile>
		 				String date = (String) jsonObject.get("upDate");// 上传时间
						DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						Date upDate = df.parse(date);

七.	jsonObject				
		  1 jsp 中:<th field="stStatus" align="center" editor="{type:'numberbox'}">分类状态</th>
		  2.controller addOrUpdateSystypes方法中:  
		 			 Object stStatus=  jsonObject.get("stStatus");//stStatus 是int类型
		 			 //如果修改的时候在datagrid中为空
						if(stStatus.equals("")){
						systypes.setStStatus(1);
					}else{//如果不为空将Object 转换为int
						systypes.setStStatus(Integer.parseInt(String.valueOf(stStatus)));
					}
		  	//jsonObject instanceof JSONNull 判断jsonObject是否为空 //jsonObject.containsKey("stStatus")判断jsonObject中的key是否存在
		   				String date = (String) jsonObject.get("upDate");// 上传时间bean中为Date类型,从datagrid中获取到String 类型转换后再存储
						DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						Date upDate = df.parse(date);
				
八.		datagrid 中numberbox 有小数点的情况 :	<!-- double数据类型 -->
		<th field="drugSalesprice" align="center" editor="{type:'numberbox',options:{min:0,precision:2}}">
九. datagrid 验证 
		editor="{type:'validatebox',options:{required:true}}"  //必添项
		<th field="muEmail" align="center" editor="{type:'validatebox',options:{validType:'email'}}"> 电子邮件 </th>	               //可以为空,如不为空必须为邮件格式		
		<th field="muEmail" align="center" 	editor="{type:'validatebox',options:{required:true,validType:'email'}}">电子邮件	</th>	//不能为空并且为邮件格式	
十. 获取应用路径 
	//ServletContext servletContext = request.getSession().getServletContext();
			//String uploadPath = servletContext.getRealPath("\\")+ "uploadFile\\";		
十一java 中 .json 字符串   
	json = "{ \"errcode\": "   + errcode + ",  \"errmsg\": \"" + errmsg	+ "\",\"id\": \"" + id + "\" }";  //errcode int类型 
	json = "{ \"errcode\": \"" + errcode + "\",\"errmsg\": \"" + errmsg	+ "\",\"id\": \"" + id + "\" }";   //errcode String类型 
	// json = "{ \"A\": 12 ,\"B\": 9 ,\"C\": 8 }";

十二.异常处理  	oracle 字符集
	 程序在运行过程中,如果抛出异常不处理,虚拟机将停止,反之异常被catch到,主线程将会继续进行
	 文本内容看着正确,实际用16进制打开发现0D 0A 为换行符,需要注意
十三. 折线图 warehouseitemchart.jsp
	    window.setInterval(getForm, 3000); 	 // 每隔3秒自动调用方法，实现图表的实时更新
	    chart.series[0].setData(temp);   //只修改值 
	  console.log("");//打印至浏览器控制台  
十四.	  服务端需要返回一段json串给客户端    解决非良好格式
			response.setContentType("text/html; charset=utf-8"); 
		 response.setContentType("application/json;charset=UTF-8");
十五.  向List 集合中添加list数据类型 元素   注意先要初始化   oracle 判断 时间为空用 is null (not)
		List list=new ArrayList() ;   List l=.......;  list.add(l);		
十六. 	?1 Date 为String 类型 2016-02-20 如果数据库中为Date 类型 也可以进行比较,需要转移:to_date('2016-02-20','yyyy-mm-dd')
		hql1="select c.cwiUploadtime from CouWarehouseitem c where c.cusId=?0 and c.cwiUploadtime = to_date(?1,'yyyy-mm-dd')"; 
		oracle 时间区间语句  // 早的时间在前 晚的时间在后  where between  x and y ;
		从一个表导入另一个表  注意时间格式,主键guid
		insert into 要导入 的表名 t (t.guid主键, t.字段名	,	t.Date格式日期字段
		)                 select  			SYS_GUID() ,查询字段,	to_date(字符串格式日期字段,'yyyy-MM-dd HH24:mi:ss')
		from 查询表名 ; 
				
十七.	highChart 饼图  js 中将json 中的map 转换为array  /js/warehouseitem/warehouseitemAmountChart.js
		jQuery.each(data.map, function(k, v) {
			var arr = [ k, v ];
			map.push(arr);                     });
			
十八. 文件上传功能模块在  DrugcatalogController
十九 .重要 
	从tomcat 迁移到weblogic 出错:  
	1 .web-inf 添加文件:  weblogic.xml 调用地维接口包冲突
	2. 添加数据源
二十. web.xml  <context-param> 不使用通配符方式   					逗号分隔
	<	param-value>classpath:config/springAnnotation-hibernate.xml,classpath:config/springAnnotation-servlet.xml</param-value>
二十一. 时间循环倒数与两个日期之间查询在  SalesitemManager 类中.

二十二. JS中页面跳转，传值包含中文时乱码解决方案   CustomerController 类getCustomerList()中遇到此问题
		js(listload.js) : 	var cusName = $("#searchname").val();	var c = encodeURI(encodeURI(cusName));
						var cusFlag = $("#searchname").attr("cusFlag");	location.href = "/medicare/getCustomerList?cusFlag=" + cusFlag + "&cusName=" + c;
		java :  String cusName = request.getParameter("cusName");
				cusName=URLDecoder.decode(cusName,"UTF-8"); (非空状态下)
二十三. 见LoginController类    
		HttpSession session java后台中  session.setAttribute("userName", "所传的值");
		其它类获取两种 方式: 1. request.getSession().getAttribute("userName");		2. session.getAttribute("userName"); //HttpSession session
		jsp 中获取两种方式: 1.<%=session.getAttribute("userName") %>	 			2.${sessionScope.userName}
二十四. 更新 BaseDao find()方法
		if (params[i] instanceof List) {//判断参数是否为集合
				query.setParameterList(query.getNamedParameters()[i],
						(Collection) params[i]);
			}
二十五. TreeMap 倒序  Map map=new TreeMap().descendingMap();	
		DrugUsedCountController.java(line:210)  应用于按 对treeMap value比较器排序
二十六.	柱状条形图 		
		图例:chart.yAxis[0].setTitle( {text : '内容' }, true);
二十七.  定义数组 	Object[] row = new Object[5];	 row[0] = cusId;...
		Object[] keys = map.keySet().toArray(); 将map的key或者value转化为数组
		当查询操作中遇到条件不确定将不使用占位符而是直接进行赋值(ex:SalesitemController)
二十八. 在定时任务单线程操作时注意
		1. bean		如果 只用工具类SpringContextUtils 获取到了一个XXXManager 的bean,就只能用这个,下面的XXXDao 如果要用还是要手工获取
		2. session  每个方法中最好使用HibernateUtils 来获取session,使用完成 后要关闭session
		3   解析文件中用到了事物,可以使用BaseDao sessionFactory中的openSession() 或getCurrentSession()
二十九. 底层数据库oracle  用char() 字符类型时,定义固定长度为6位char(6)时 , 如果 保存的数据  长度小说六位("1234"),会自动补空字符, 这时查询 字段="1234" 就会查不到
		所以如果赋值时确保长度正好.反之查询时补足空字符:"1234  "
三十.  修改powerdesigner的DBMS配置?  	菜单 database -〉 change current dbms 
三十一. @SuppressWarnings({"unchecked","unused"})
三十二 . Session 登录不超时	web.xml 设置 session-timeout=-1
三十三.	HibernateUtils		config/HibernateUtils.xml		(原来用hibernate.cfg.xml  bean 要拷贝)
三十四. java 文件分隔符	System.getProperty("file.separator");	
			 操作系统的名称					os.name 			书签 system
三十五. 获取服务器集群jvm 当前服务器名称  System.getProperty("weblogic.Name");
三十六.datagrid 行自定义方法: formatter:formatOper  <th data-options="field:'',align:'center',formatter:formatOper">
		 /medicare/WebRoot/WEB-INF/jsp/uploadfilewarning/listUploadfileByDate.jsp
三十七. hibernate 反向生成时用java 类型, 实体类中将int 替换为 Integer, double 替换 为 Double 防止数据库int字段为空时,查询出现  IllegalArgumentException
		对象设计，不做数据库设计，设计类的时候，类的属性都应该设为java基本类型对应的包装类，防止查询数据时报错
三十八. datagrid salesitem.jsp		<th field="drugSalesprice" align="center"	editor="{type:'numberbox',options:{min:0,precision:2}}">	<!-- double数据类型    editor="{type:'datetimebox'}"-->	
三十九 .拦截器 	
			如果不定义 mvc:mapping path 将拦截所有的URL请求 ,如果有(例外)exclude-mapping ,则必须有		mapping     注意版本:spring-mvc-3.2.xsd
			<mvc:view-controller path="/" view-name="forward:/WEB-INF/jsp/login" />  也可以这样设置默认首页
四十.	log4j日志    private static final Log log=LogFactory.getLog(LoginController.class);
四十一. highcharts 显示加载中   eachPointTotalChart.js
		chart.showLoading(); chart.hideLoading();  loading : {}
四十二 . jackson 使用方法(详见书签jackson)  CustomerController 方法:getConditionCustomer
		1.springAnnotation-servlet.xml  json转换配置
	  	2.在pojo 时间get方法上加入注解	@JsonSerialize(using=JacksonDateSerializer.class)  用于转换成自定义时间格式,如果没有默认返回timestamp 格式
	  														JacksonDateHMSSerializer 带时分秒
		3.调用时	@ResponseBody   直接返回Map 或 List  
四十三. buttun 样式  	input 样式
		<input type=button 	onclick="location.href=('URL')"  value="查询" />		//当前页面打开新url				
		<button onclick="window.open('url')">查询</button>		//新窗口打开url
		<input name="" value="" type="hidden" style="visibility: hidden" /> //隐藏 与  不可见(占位置)
四十四. js 获取jsp id value,name 
		jsp:	<input id="searchname" iva=8 size=15></input>
		js:		var cusName=$("#searchname").val();	//按id 获取值:输入值
				var iva=$("#searchname").attr("iva");//按属性名称获取值:8
				
		window.location.href="URL";	//重新加载当前指定url页面(windown可省略)		
		parent.location.reload(); 刷新父页面
四十五. ajax提交 		
		java: 需要返回json  两种方式 1. response 2.ResponseBody 方式
		js:	$.post("/medicare/toMonomerPharmacy", {
					cusFlag : cusFlag,
					cusName : cusName
				}, function(data) {// 前台没有返回json,里面就没有data});
				
	   js:	  $("ID").click(function(){
				    $.get("/url",{parm:""},function(data,status){
				      alert("数据：" + data + "\n状态：" + status);
				    });
				  });
					
四十六. 回车事件 
		方式 一:	详见  listmonomerpharmacy.jsp ,listload.js  注意:input 回车事件
		$(function() {	
					$('#id').bind('keypress', function(event) {
						if (event.keyCode == "13") {
							调用事件处理方法();	}});});						
		方式 二: //详见customer.jsp customer.js   注意:form 表单回车事件    需要两个input(一个隐藏不可见) 不然出错
		<form id="searchForm"   onkeydown="if(event.keyCode==13){ 调用处理事件方法();}">	
								onkeydown="if(event.keyCode==13)return false;	//表单回车取消事件
四十七.	PROPAGATION_REQUIRES_NEW--新建事务，如果当前存在事务，
		把当前事务挂起。连锁药店遇到此情形,在定时任务中,单线程,创建双重事物 WarehouseManager runByHqlCST()方法
		<prop key="run*">PROPAGATION_REQUIRES_NEW</prop>
四十八    div  水平居中
		body {
		text-align:center;
		}
		<div align="left" style="width: 70%;margin: 0 auto">
		
		div 垂直居中  ex:登录页面
			
		table 跨行 rowspan  跨列colspan
四十九  	Page 分页中url 中设定一个默认条件比如    XXXXXXXX?1=1   			再加上其它条件时就是&pagenum="+pagenum;
五十           事物的提交 顺序  要有try catch finally 语句
				session=HibernateUtils.getSession();
				session.beginTransaction();
       		    session.save(uploadfile);
				session.flush();
				session.clear();
				session.getTransaction().commit();
				HibernateUtils.closeSession(session);
		新方法:
			Session session =baseService.getSessionFactory().getCurrentSession();
			session.beginTransaction();	
			baseService.save(cusareaRelate);
			session.flush();
			session.clear();	
			session.getTransaction().commit();
五十一. hibernate 更新与查询唯一结果
		query.uniqueResult().toString()
		query.executeUpdate(); //更新 成功为1 失败为0
五十二. 创建webservice 服务
	        本机例子:TestWebService.java  		
	          错误解决:http://jyao.iteye.com/blog/1213664   注意每启动一次端口将被占用,需要停止服务服务
	    JAVA项目中发布WebService服务——简单实例 	    http://blog.csdn.net/hanxuemin12345/article/details/40163757
	          应用 其它人发布的webservice 接口服务 
	          打开jdk 自带的wsimport.exe 工具 在d:/soft/jdk/bin 文件夹内 
	    cmd : d:/soft/jdk/bin >wsimport -keep -d d:\webservice -verbose http://localhost:8128/testService?wsdl  (d:\webservice要有文件夹名称自定义)  
	    cmd : d:/soft/jdk/bin >wsimport -keep -d d:\webservice -verbose http://10.115.210.82:8001/dwlesbserver/services/uddi?wsdl
五十三 . Object[] obj = (Object[]) list.get(i); list 中元素为二维数组
五十四. 更新实体两种 方法: query.executeUpdate();与 session.update(entity);	 
五十五. list 排序 (list 内存放同一数据类型)
		list.add(Utils.getDate(1));
		Collections.sort(list);//按自然排序升序ABC123小时间在前
		Collections.reverse(list);//将原有序列头尾颠倒 可单独使用   
五十六. 清空 重载 tzddlog.js
		$("#searchForm").find("input").val("");// 找到form表单下的所有input标签并清空
		$('#searchForm').form('clear');		
		$('#adtzddInput').val('');  //给id为** Input 赋空值
		$('#customer').datagrid('options').queryParams={};//清空datagrid查询参数 (customer.js)
		$('#tzddlog').datagrid('clearSelections');//清除datagrid行勾选  (tzddlog.js)
		$('#tzddlog').datagrid('reload');//重新加载datagrid
五十七: jstl  customerManager.jsp 
		<c:if test="${!empty page.list}">
				<c:forEach items="${page.list}" var="c">
						<c:choose>
								<c:when test="${c.cusStatus==1}"></c:when>
								<c:otherwise></c:otherwise>
						</c:choose>
				</c:forEach>
		</c:if>	
五十八: manager.js 数组push  checkbox 操作示例
		var array = document.getElementsByName("box");		var items = [];
		$("input:checkbox[name='box']:checked").each(function() { // 遍历name=box的多选框
			items.push($(this).val());  // 每一个被选中项的值
		});   alert(items.toString());	//1,2,3 (结果自动以逗号分隔)
五十九  提示信息
	   JOptionPane.showMessageDialog(null, "无操作权限!","提示信息",	JOptionPane.ERROR_MESSAGE);		
六十        项目发布时省略项目名称方法:	项目属性>Web>Web context-root /	[项目名称]   
六十一 instanceof 运算符可以用来决定某对象的类是否实现了接口
六十二. iframe (areacustomer.js中的show方法演示)父类调用子类方法 window.parent.addTab(, href);
			   子类调用父类方法  document.getElementById("childframe").contentWindow.childtest();
六十三. datagrid 两次加载 去掉 class="easyui-datagrid"		java  判断查询 count(*)>0	   
		获取easyui 选中行
			var array = $('#customer').datagrid('getSelected');// 获取选中行
			var array = $('#customer').datagrid('getSelections');// 获取选中多行项数组
六十四. plsql 配置数据源时格式必须规范			   