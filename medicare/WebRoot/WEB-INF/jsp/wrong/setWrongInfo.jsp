<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>错误信息状态设置</title>
		<c:set value="${pageContext.request.contextPath}" var="path" scope="page" />
		<link rel="stylesheet" href="${path}/easyui/themes/default/easyui.css" />
		<link rel="stylesheet" href="${path}/easyui/themes/icon.css" />
		<link rel="stylesheet" type="text/css" href="${path}/css/mystyle.css" />
		<style>
			.w_200{width:200px}
			#form .input_box{text-align:center;margin-bottom:15px}
		</style>
		<script src="${path}/easyui/jquery-1.7.2.min.js"></script>
		<script src="${path}/easyui/jquery.easyui.min.js"></script>
		<script src="${path}/easyui/locale/easyui-lang-zh_CN.js"></script>
		<script type="text/javascript">
			$(function() {
				$('#privilege').treegrid({
					url : 'getSettingCheck',
					idField : 'setId',
					treeField : 'setName',
					fitColumns : true,
					border : false,
					columns : [ [ 
						{field : 'setId',title : '上级id',hidden : true },
						{field : 'setCode',title : '字段id',hidden : true },  
						{field : 'setName',title : '权限名称' }, 
						{field : 'priStatus',title : '状态',
							formatter : function(value, row, index) {
								if (row.setNull==1) {
									return '<a href="#"></a><img src="images/accept.png"  title="已启用" alt="" />';
								} else {
									return '<a href="#"></a><img src="images/delete.png" title="已停用" alt="" />';
								}
							}
						}
					] ]
				});
			});
			


			//启用或禁用
			function updateStatus(setNull){
				var array = $('#privilege').datagrid('getSelections'); // 获取选中行数组
			    if (array.length > 1) {
			    	$.messager.alert('系统提示','只能选择一个权限进行状态修改','warning');
			    } else if (array.length < 1) {
			        $.messager.alert('系统提示','请选择需要修改状态的权限','warning');
			    } else {
				    var info;
				    if(setNull==1){
				    	info = "是否启用该权限？";
				    } else {
				    	info = "是否禁用该权限？禁用后用户将不能使用该权限";
				    }
			    	$.messager.confirm('系统提示', info, function(r) {
			    		if (r) {
					    	var priId = $('#privilege').datagrid('getSelected').setId;
					    	var setId = $('#privilege').datagrid('getSelected').setCode;
					        $.post("updateStatus", {setId:priId,setCode:setId,setNull:setNull}, function(data){
								$('#privilege').treegrid('reload');
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
			<div class="easyui-panel" title="错误规则设置" data-options="border:true,fit:true" iconCls="icon-zc">
				<table id="privilege" data-options="checkOnSelect:true,singleSelect:true,fit:true,rownumbers:true,pagination:true" cellpadding="2" cellspacing="1" toolbar="#toolbar"></table>
			</div>
			<div id="toolbar" style="width: 100%; height: 25px; padding: 2px 0;">
				
				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="updateStatus('1')" data-options="iconCls:'icon-accept3',plain:true" style="float: left;"  >设置为非空值/0值</a>
				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="updateStatus('0')" data-options="iconCls:'icon-delete',plain:true" style="float: left;"  >可以为空值/0值</a>
			</div>
		</div>
	</body>
</html>
