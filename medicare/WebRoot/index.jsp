<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title>淄博市医保进销存数据监管平台</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta name="description" content="淄博,医保,监管" />
	<meta name="keywords" content="平台" />
	<meta http-equiv="refresh" content="0;url=/toLogin">
	<link rel="shortcut icon" href="${path}/images/21.ico"/>
  </head>
  
  <body>
  
    系统加载中...
    
  </body>
</html>
