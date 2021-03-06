<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>科室信息</title>

		<c:set value="${pageContext.request.contextPath}" var="path" scope="page" />
		<link rel="stylesheet" href="${path}/easyui/themes/default/easyui.css" />
		<link rel="stylesheet" href="${path}/easyui/themes/icon.css" />
		<script src="${path}/easyui/jquery-1.7.2.min.js"></script>
		<script src="${path}/easyui/jquery.easyui.min.js"></script>
		<script src="${path}/easyui/locale/easyui-lang-zh_CN.js"></script>
		<script src="${path}/js/yiwei.js"></script>
		<script type="text/javascript">
			$(function() {
				var cusId = $("#department").attr("name");
				var url = 'getAllDepartment?cusId='+cusId;
				datagrid('department', '科室信息', 'deptId', url);
			});

			//删除重复数据
			function deleteRepeat(type){
				var cusId=$("#department").attr("name");
				$.messager.show({
					title: "系统提示",
	                msg: "正在删除科室信息重复数据..."
				});	
				$.ajax({
					type: "POST",
			        url: "deleteRepeat",
			        data:{type:type,cusId:cusId,name:'科室信息'},
			        success: function(data) {
			        	$("#department").datagrid('reload');
			        	$.messager.show({
							title: "系统提示",
			                msg: data.info
						});
					}
				});
			}
			
		</script>
	</head>
	<body>
		<div class="easyui-layout" fit="true" border="false">
			<div data-options="region:'north',title:'高级查询'" style="height: 65px; background: #F4F4F4;">
				<form id="searchForm" onkeydown="if(event.keyCode==13){ queryForm();}">
					<table>
						<tr>
							<td><a class="easyui-linkbutton" href="javascript:void(0);" iconCls="icon-cancel" onclick="deleteRepeat('department');">删除重复数据</a></td>
						</tr>
					</table>
				</form>
			</div>
			
			<div data-options="region:'center',split:false">
				<table id="department" name="${cusId }">
					<thead>
						<tr>
							<th field="deptId" align="center" hidden=true></th>
							<th field="cusId" hidden=true align="center">客户ID</th>
							<th field="cusPid" hidden=true align="center">客户上级id</th>
							<th field="cusDareway" hidden=true align="center">医院编码</th>
							<th field="deptCode" align="center">科室编码</th>
							<th field="deptName" align="left">科室名称</th>
							<th field="deptBeds" align="center" formatter="fmtNull">床位数</th>
							<th field="deptEmps" align="center" formatter="fmtNull">医护人员数</th>
							<th data-options="field:'deptStatus',align:'center',formatter:formatOperStatus">状态</th>
							<th field="deptPicktime" hidden=true align="center">采集日期</th>
							<th field="deptAddtime" align="center">创建日期</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
	</body>
</html>
