<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>上传统计详情</title>

		<c:set value="${pageContext.request.contextPath}" var="path" scope="page" />
		<link rel="stylesheet" href="${path}/easyui/themes/default/easyui.css" />
		<link rel="stylesheet" href="${path}/easyui/themes/icon.css" />
		<script src="${path}/easyui/jquery-1.7.2.min.js"></script>
		<script src="${path}/easyui/jquery.easyui.min.js"></script>
		<script src="${path}/easyui/locale/easyui-lang-zh_CN.js"></script>
		<script src="${path}/js/yiwei.js"></script>
		<script type="text/javascript">
			$(function() {
				var cusId = $("#uploadfileDetail").attr("cusId");
				var url = 'getCouUploadfileDetail?cusId=' + cusId;
				datagrid('uploadfileDetail', '上传统计详情', 'upId', url);
			});
				
		</script>
	</head>
	<body>
		<div class="easyui-layout" fit="true" border="false">
			<div data-options="region:'center',split:false">
				<table id="uploadfileDetail" cusId="${cusId }">
					<thead>
						<tr>
							<th data-options="field:'sb' "></th>
							<th field="upId" align="center" hidden=true></th>
							<th field="cusId" hidden=true align="center" editor="{type:'text'}">客户ID</th>
							<th field=typeName  align="left" editor="{type:'text'}">类型名称</th>
							<th field="lastUpdatedate" align="center" editor="{type:'text'}">最后上传日期</th>
							<th field="upCount" align="center" editor="{type:'text'}">上传数据条数</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
	</body>
</html>
