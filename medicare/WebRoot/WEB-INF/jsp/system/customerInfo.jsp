<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>定点信息</title>
		<c:set value="${pageContext.request.contextPath}" var="path" scope="page" />
		<link rel="stylesheet" href="${path }/css/ew-main.css" />
		<link rel="stylesheet" href="${path }/css/ew-home.css" />
		<link rel="stylesheet" href="${path}/css/icon.css" />
	</head>
	<body Style="background-color:white;">
         <div title="定点信息" data-options="iconCls:'icon_user'" style="padding:20px; ">   
           <div style="padding: 0px; width: 100%; height: 100%;">
           	<table class="ew_table" style="width: 100%; height: 100%;">
	        	<tbody>
                      <tr><th colspan="2">${customer.cusName } - 基本信息</th></tr>
                      <tr><th>定点名称：</th><td>${customer.cusName }</td></tr>
                      <tr><th>定点编码：</th><td>${customer.cusDareway }</td></tr>
                      <tr><th>定点类型：</th><td>${cusFlag }</td></tr>
                      <tr><th>定店ID：</th><td>${customer.cusId }</td></tr>
                      <tr><th>分店编码：</th><td>${customer.cusBranchcode }</td></tr>
                      <tr><th>联系人：</th><td>${customer.cusContact }</td></tr>
                      <tr><th>联系电话：</th><td>${customer.cusPhone }</td></tr>
                      <tr><th>联系地址：</th><td>${customer.cusAddr }</td></tr>
                      <tr><th>唯一标识：</th><td>${customer.cusUniqure }</td></tr>
                      <tr><th>注册日期：</th><td>${customer.cusRegdate }</td></tr>
                      <tr><th>备注：</th><td>${customer.cusRemark }</td></tr>
                  </tbody>
               </table>
             </div>   
        </div> 
	</body>
</html>
