<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>错误信息处理</title>
		<c:set value="${pageContext.request.contextPath}" var="path" scope="page" />
		<link rel="stylesheet" href="${path}/easyui/themes/default/easyui.css" />
		<link rel="stylesheet" href="${path}/easyui/themes/icon.css" />
		<link rel="stylesheet" type="text/css" href="${path}/css/mystyle.css" />
		<style>
			.w_200{width:200px}
			#areaform .input_box{text-align:center;margin-bottom:15px}
		</style>
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
				var url = 'getAllWrongInfo?startDate='+startDate+"&endDate="+endDate;
				datagrid('allwronginfo', '错误信息处理', 'wriId', url);
			});

			//表格查询  
			function queryForm(){  
				var startDate=$("#startDate").datebox("getValue"); 
				var endDate=$("#endDate").datebox("getValue"); 
				var cusDareway = $("#cusDareway").val();
			  	$('#allwronginfo').datagrid({
					url:'getAllWrongInfo',
					queryParams:{
			  			startDate:startDate,
			  			endDate:endDate,
			  			cusDareway:cusDareway
				    },
				});
			} 
			
			//根据状态显示图片
			function areaStatus(value, row, index) {
			    if (row.wriStatus == 1) { // 这里是判断哪些行
			        return '<a href="#"></a><p style="color:green;">已处理</p>';
			    } else {
			        return '<a href="#"></a><p style="color:red;">未处理</p>';
			    }
			}
	
			//点击清空按钮出发事件
			function clearForm() {
				 $('#searchForm').form('clear');  
			}

			function updateStatus(status){
				var array = $('#allwronginfo').datagrid('getSelections'); // 获取选中行数组
			    if (array.length > 1) {
			    	$.messager.alert('系统提示','只能选择一个区划进行状态修改','warning');
			    } else if (array.length < 1) {
			        $.messager.alert('系统提示','请选择需要修改状态的区划','warning');
			    } else {
				    var info;
				    if(status==1){
				    	info = "是否更改处理状态？";
				    }
			    	$.messager.confirm('系统提示', info, function(r) {
			    		if (r) {
					    	var wriId = $('#allwronginfo').datagrid('getSelected').wriId;
					        $.post("updateWrongInfoStatus", {status:status,wriId:wriId}, function(data){
								$('#allwronginfo').datagrid('reload');
								$.messager.show({
					                title: '系统提示',
					                msg: data.status
					            });
							});
				        }
			        });
			    }
			}
			
		</script>
	</head>
	<body>
		<div class="easyui-layout" data-options="fit:'true',">
		<div data-options="region:'north',title:'高级查询'" style="height: 65px; background: #F4F4F4;">
				<form id="searchForm" onkeydown="if(event.keyCode==13){ queryForm();}">
					<table>
						<tr>
							<th>查询开始日期</th> 
							<td><input id="startDate" class="easyui-datebox" data-options="editable:false" name="startDate" style="width:100px" /></td><!-- editable:false禁止用户手动输入,easyui-datetimebox带时分秒 -->
							<th>查询结束日期</th>
							<td><input id="endDate" class="easyui-datebox" data-options="editable:false" name="endDate" style="width:100px" /></td>
							<th>定点名称检索</th>
							<td><input id="cusDareway" name="cusDareway" style="width:100px" /></td>
							<td><a class="easyui-linkbutton" href="javascript:void(0);" iconCls="icon-search" onclick="queryForm();">查询</a></td>
							<td><a class="easyui-linkbutton" href="javascript:void(0);" iconCls="icon-remove" onclick="clearForm();">清空</a></td>
							<td><a href="javascript:void(0)" class="easyui-linkbutton" onclick="updateStatus('1')" data-options="iconCls:'icon-accept3',plain:true" style="float: left;"  >更改为已处理</a></td>
						</tr>
					</table>
				</form>
			</div>
			<br/>
			<br/>
			<div data-options="region:'center',split:false"><!--class="easyui-panel" title="错误信息处理" data-options="border:true,fit:true" iconCls="icon-zc"-->
				<table id="allwronginfo" data-options="checkOnSelect:true,fit:true,singleSelect:true,rownumbers:true,pagination:true" cellpadding="2" cellspacing="1">
					<thead>
						<tr>
							<th data-options="field:'sb',checkbox:true"></th>
							<th data-options="field:'cusDareway',align:'center',width:140">定点名称</th>
							<th data-options="field:'cusNo',align:'center',width:140">定点编号</th>
							<th data-options="field:'wriStatus',align:'center',width:60,formatter:areaStatus">当前状态</th>
							<th data-options="field:'wriName',align:'center',width:140">错误类型</th>
							<th data-options="field:'wriFile',align:'center',width:140">出错的文件</th>
							<th data-options="field:'wriDescription',align:'center',width:140">错误详情</th>
							<th data-options="field:'wriCode',align:'center',width:140">错误编码</th>
							<th data-options="field:'wriAddtime',align:'center',width:140">添加时间</th>
							<th data-options="field:'cusContact',align:'center',width:140">联系人</th>
							<th data-options="field:'cusPhone',align:'center',width:140">联系电话</th>
							<th data-options="field:'cusAddress',align:'center',width:140">联系地址</th>
							<th data-options="field:'wriFilepath',align:'center',width:140">出错文件路径</th>
							<th data-options="field:'wriId',align:'center',width:1">id</th>
						</tr>
					</thead>
				</table>
			</div>
			
		</div>
		
	</body>
</html>