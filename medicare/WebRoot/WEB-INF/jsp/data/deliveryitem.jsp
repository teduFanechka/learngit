<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>出库信息</title>

		<c:set value="${pageContext.request.contextPath}" var="path" scope="page" />
		<link rel="stylesheet" href="${path}/easyui/themes/default/easyui.css" />
		<link rel="stylesheet" href="${path}/easyui/themes/icon.css" />
		<script src="${path}/easyui/jquery-1.7.2.min.js"></script>
		<script src="${path}/easyui/jquery.easyui.min.js"></script>
		<script src="${path}/easyui/locale/easyui-lang-zh_CN.js"></script>
		<script src="${path}/js/yiwei.js"></script>
		<script type="text/javascript">
			$(function() {
				var startDate = '${startDate}';
				var endDate = '${endDate}';
				$("#startDate").datebox("setValue", startDate);  
				$("#endDate").datebox("setValue", endDate);
				var cusId=$("#deliveryitem").attr("name");
				var url = 'getAllDeliveryitem?cusId='+cusId+"&startDate="+startDate+"&endDate="+endDate;
				datagrid('deliveryitem', '出库信息', 'diId', url);
			});
		
			//表格查询  
			function queryForm(){  
			  	var cusId=$("#deliveryitem").attr("name");
				var startDate=$("#startDate").datebox("getValue"); 
				var endDate=$("#endDate").datebox("getValue"); 
				var diNo = $("#diNo").val();
				var drugCode = $("#drugCode").val();
			  	$('#deliveryitem').datagrid({
					url:'getAllDeliveryitem',
					queryParams:{
			  			cusId:cusId,
			  			startDate:startDate,
			  			endDate:endDate,
			  			diNo:diNo,
			  			drugCode:drugCode
				    },
				});
			} 
	
			//点击清空按钮出发事件
			function clearForm() {
				 $('#searchForm').form('clear');  
			}

			//删除重复数据
			function deleteRepeat(type){
				var cusId=$("#deliveryitem").attr("name");
				$.messager.show({
					title: "系统提示",
	                msg: "正在删除出库信息重复数据..."
				});	
				$.ajax({
					type: "POST",
			        url: "deleteRepeat",
			        data:{type:type,cusId:cusId,name:'出库信息'},
			        success: function(data) {
			        	$("#deliveryitem").datagrid('reload');
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
							<th>出库开始日期</th>
							<td><input class="easyui-datebox" id="startDate" data-options="editable:false" name="startDate" style="width:100px" /></td>
							<th>出库结束日期</th>
							<td><input class="easyui-datebox" id="endDate" data-options="editable:false" name="endDate" style="width:100px" /></td>
							<th>出库编号</th>
							<td><input id="diNo" name="diNo" style="width:100px" /></td>
							<th>项目编码/项目名称</th>
							<td><input id="drugCode" name="drugCode" style="width:100px" /></td>
							<td><a class="easyui-linkbutton" href="javascript:void(0);" iconCls="icon-search" onclick="queryForm();">查询</a></td>
							<td><a class="easyui-linkbutton" href="javascript:void(0);" iconCls="icon-remove" onclick="clearForm();">清空</a></td>
							<td><a class="easyui-linkbutton" href="javascript:void(0);" iconCls="icon-cancel" onclick="deleteRepeat('deliveryitem');">删除重复数据</a></td>
						</tr>
					</table>
				</form>
			</div>
			<div data-options="region:'center',split:false">
				<table id="deliveryitem" name="${cusId }">
					<thead>
						<tr>
							<th field="diId" align="center" hidden=true></th>
							<th field="cusId" align="center" hidden=true>客户ID</th>
							<th field="cusParentid" align="center" hidden=true>客户上级ID</th>
							<th field="diHcscode" align="center">本位码</th>
							<th field="diNo" align="center">出库编号</th>
							<th field="diOpdatetime" align="center">出库日期</th>
							<th field="drugCode" align="center">项目编码</th>
							<th field="drugName" align="left">项目名称</th>
							<th data-options="field:'diType',align:'center',formatter:formatOperDiType">出库类别</th>
							<th field="diReason" hidden=true align="center">出库原因</th>
							<th field="drugNum" align="center">出库数量</th>
							<th field="diUnit" align="center">单位</th>
							<th field="drugBatchno" align="center">生产批号</th>
							<th field="drugMfrs" align="center">生产商</th>
							<th field="drugMadein" align="center">产地</th>
							<th field="drugEid" hidden=true align="center">电子监管码</th>
							<th field="diSpecification" align="center" formatter="fmtNull">规格</th>
							<th field="diWhcode" hidden=true  align="center">仓库编码</th>
							<th field="drugMfgdate" hidden=true align="center">生产日期</th>
							<th field="diWhname" align="center">库房位置</th>
							<th field="diLocation" hidden=true align="center">货位/货架号</th>
							<th field="drugExpdate" align="center">有效期</th>
							<th field="diOpcode" hidden=true  align="center">操作员编码</th>
							<th field="diOpname" align="center" formatter="fmtNull">操作员</th>
							<th field="diPicktime" align="center" hidden=true>采集日期</th>
							<th field="diAddtime" align="center">创建日期</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
	</body>
</html>
