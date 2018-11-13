<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>检查、检验结果详细</title>

		<c:set value="${pageContext.request.contextPath}" var="path" scope="page" />
		<link rel="stylesheet" href="${path}/easyui/themes/default/easyui.css" />
		<link rel="stylesheet" href="${path}/easyui/themes/icon.css" />
		<script src="${path}/easyui/jquery-1.7.2.min.js"></script>
		<script src="${path}/easyui/jquery.easyui.min.js"></script>
		<script src="${path}/easyui/locale/easyui-lang-zh_CN.js"></script>
		<script src="${path}/js/yiwei.js"></script>
		<script type="text/javascript">
			$(function() {
				var cusId = $("#checkdetail").attr("name");
				var url = "getAllCheckdetail?cusId="+cusId;
				datagrid('checkdetail', '检查、检验结果详细', 'ctId', url);
			})
			
			//查询
			function queryForm(){  
			  	var cusId=$("#checkdetail").attr("name");
				var ctCheckno = $("#ctCheckno").val(); 
				var ctHospno = $("#ctHospno").val(); 
				var ctItemcode = $("#ctItemcode").val(); 
			  	$('#checkdetail').datagrid({
					url:'getAllCheckdetail',
					queryParams:{
			  			cusId:cusId,
			  			ctCheckno:ctCheckno,
			  			ctHospno:ctHospno,
			  			ctItemcode:ctItemcode
				    },
				});
			} 
	
			//清空
			function clearForm() {
				 $('#searchForm').form('clear');  
			}
			
			//删除重复数据
			function deleteRepeat(type){
				var cusId=$("#checkdetail").attr("name");
				$.messager.show({
					title: "系统提示",
	                msg: "正在删除检查、检验结果详细重复数据..."
				});	
				$.ajax({
					type: "POST",
			        url: "deleteRepeat",
			        data:{type:type,cusId:cusId,name:'检查、检验结果详细'},
			        success: function(data) {
			        	$("#checkdetail").datagrid('reload');
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
							<th>检查流水号</th>
							<td><input id="ctCheckno" name="ctCheckno" style="width:100px" /></td>
							<th>住院号/门诊号</th>
							<td><input id="ctHospno" name="ctHospno" style="width:100px" /></td>
							<th>项目编码/项目名称</th>
							<td><input id="ctItemcode" name="ctItemcode" style="width:100px" /></td>
							<td><a class="easyui-linkbutton" href="javascript:void(0);" iconCls="icon-search" onclick="queryForm();">查询</a></td>
							<td><a class="easyui-linkbutton" href="javascript:void(0);" iconCls="icon-remove" onclick="clearForm();">清空</a></td>
							<td><a class="easyui-linkbutton" href="javascript:void(0);" iconCls="icon-cancel" onclick="deleteRepeat('checkdetail');">删除重复数据</a></td>
						</tr>
					</table>
				</form>
			</div>
			
			<div data-options="region:'center',split:false">
				<table id="checkdetail" name="${cusId }">
					<thead>
						<tr>
							<th field="ctId" align="center" hidden=true></th>
							<th field="cusId" hidden=true align="center">客户ID</th>
							<th field="ctCheckno" align="center">检查流水号</th>
							<th field="ctHospno" align="center">住院号/门诊号</th>
							<th field="ctItemcode" align="center">项目编码</th>
							<th field="ctItemname" align="center">项目名称</th>
							<th field="ctResult" align="center">结果</th>
							<th field="ctUnit" align="center">单位</th>
							<th field="ctPoint" align="center">提示</th>
							<th field="ctRange" align="center">参考范围</th>
							<th field="ctComment" align="center">备注</th>
							<th field="ctPicktime" hidden=true align="center">采集时间</th>
							<th field="ctAddtime" align="center">创建时间</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
	</body>
</html>
