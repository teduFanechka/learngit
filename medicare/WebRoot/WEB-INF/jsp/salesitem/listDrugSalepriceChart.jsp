<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>药品使用量统计排序</title>
		<c:set value="${pageContext.request.contextPath}" var="path" scope="page" />
		<script src="${path}/easyui/jquery-1.7.2.min.js"></script>
		<script src="${path}/js/highcharts.js"></script>
		<script src="${path}/js/public.js"></script>
		<script type="text/javascript">
			// 查询事件
			function search() {
				//var drugName=$("input[name='drugName'").val();
	
				cusFlag = $("#container").attr("cusFlag");
				document.getElementById("myForm").action="toDrugSalepriceChart?cusFlag="+cusFlag;
				document.getElementById("myForm").submit();
	
			}
		</script>
	</head>
	<body>
		<div id="container" cusFlag="${cusFlag }">
			<div style="height: 50px; margin: 30px 0 0 30px;">
				<form id="myForm" method="post">
					药品名称:
					<input  type="text" name="drugName" />
					<input type="button" onclick="search();" value="搜索" />
					<input type="reset" value="清空" />
				</form>
			</div>
		</div>
	</body>
</html>
