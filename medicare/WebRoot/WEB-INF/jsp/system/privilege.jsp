<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>权限管理</title>
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
					url : 'getPrivilege',
					idField : 'priId',
					treeField : 'priName',
					fitColumns : true,
					border : false,
					columns : [ [ 
						{field : 'priId',title : '权限id',hidden : true }, 
						{field : 'priName',title : '权限名称' }, 
						{field : 'priUrl',title : '权限路径' }, 
						{field : 'priIcon',title : '页面图标',
							formatter : function(value, row, index) {
								return '<img src="'+row.priIcon+'" />';
							}
						}, 
						{field : 'priOrder',title : '排序' }, 
						{field : 'priStatus',title : '状态',
							formatter : function(value, row, index) {
								if (row.priStatus==1) {
									return '<a href="#"></a><img src="images/accept.png"  title="已启用" alt="" />';
								} else {
									return '<a href="#"></a><img src="images/delete.png" title="已停用" alt="" />';
								}
							}
						}
					] ]
				});
			});
			
			//打开添加权限
			function openAddPri() {
			    $('#addPri').dialog('open').dialog('center').dialog('setTitle', '添加权限');
			    $('#form').form('clear');
			}
	
			//打开修改权限
			function openUpdatePri() {
			    var array = $('#privilege').datagrid('getSelections');
			    if (array.length > 1) {
			    	$.messager.alert('系统提示','只能选择一个权限修改','warning');
			    } else if (array.length < 1) {
			        $.messager.alert('系统提示','请选择需要修改的权限','warning');
			    } else {
			        var row = $('#privilege').datagrid('getSelected'); // 获取选中的行
			        $('#addPri').dialog('open').dialog('center').dialog('setTitle', '修改权限');
			        $('#form').form('load', row);
			    }
			}
	
			//保存权限
			function savePri() {
			    $.ajax({
			        cache: true,
			        type: "POST",
			        url: "addOrUpdatePrivilege",
			        data: $('#form').serialize(),
			        async: false,
			        success: function(data) {
			            $.messager.show({
			                title: '系统提示',
			                msg: data.status
			            });
			            $('#privilege').treegrid('reload');
			            $('#addPri').dialog('close');
			            
			        },
			        error: function(request) {
			            $.messager.alert('系统提示','操作错误','error');
			        }
			    });
			}
	
			//删除权限
			function deletePri() {
			    var array = $('#privilege').datagrid('getSelections'); 
			    var ids = ""; 
			    for (var i = 0; i < array.length; i++) { 
			    	if (i != array.length - 1) {
			            ids += array[i].priId + ",";
			        } else {
			            ids += array[i].priId;
			        }
			    }
			    if (array != "") {
			        $.messager.confirm('提示', '是否要删除选中权限?', function(r) {
			            if (r) {
			                $.post('delPrivilege', {ids: ids}, function(data) {
			                	$("#privilege").treegrid('reload');
			                    $.messager.show({
			                        title: '系统提示',
			                        msg: data.status
			                    });
			                });
			            }
			        });
			    } else {
			        $.messager.alert("系统提示", "请先选择要删除的权限！", "warning");
			    }
			}

			//启用或禁用
			function updateStatus(status){
				var array = $('#privilege').datagrid('getSelections'); // 获取选中行数组
			    if (array.length > 1) {
			    	$.messager.alert('系统提示','只能选择一个权限进行状态修改','warning');
			    } else if (array.length < 1) {
			        $.messager.alert('系统提示','请选择需要修改状态的权限','warning');
			    } else {
				    var info;
				    if(status==1){
				    	info = "是否启用该权限？";
				    } else {
				    	info = "是否禁用该权限？禁用后用户将不能使用该权限";
				    }
			    	$.messager.confirm('系统提示', info, function(r) {
			    		if (r) {
					    	var priId = $('#privilege').datagrid('getSelected').priId;
					        $.post("updatePriStatus", {status:status,priId:priId}, function(data){
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
			<div class="easyui-panel" title="权限管理" data-options="border:true,fit:true" iconCls="icon-zc">
				<table id="privilege" data-options="checkOnSelect:true,singleSelect:true,fit:true,rownumbers:true,pagination:true" cellpadding="2" cellspacing="1" toolbar="#toolbar"></table>
			</div>
			<div id="toolbar" style="width: 100%; height: 25px; padding: 2px 0;">
				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="openAddPri()" data-options="iconCls:'icon-add',plain:true" style="float: left;">添加权限</a>
				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="openUpdatePri()" data-options="iconCls:'icon-edit',plain:true" style="float: left;" >修改权限</a>
				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="deletePri()" data-options="iconCls:'icon-remove',plain:true" style="float: left;">删除权限</a>
				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="updateStatus('1')" data-options="iconCls:'icon-accept3',plain:true" style="float: left;"  >启用</a>
				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="updateStatus('0')" data-options="iconCls:'icon-delete',plain:true" style="float: left;"  >禁用</a>
			</div>
		</div>
		
		<div id="addPri" class="easyui-dialog" title="添加权限" style="width: 430px; height:400px;" data-options="iconCls:'icon-add',buttons:'#addPri_btn',closed:true,modal:true,">
			<form id="form">
				<div class="input_box">   
			        <h1 style="margin:25px 0 20px 0">权限信息</h1>
			    </div>  
				<div class="input_box">   
			        <label>权限名称：</label>   
			        <input id="priName" name="priName" class="easyui-textbox w_200"> 
			    </div>  
			    <div class="input_box">   
			        <label> Url链接：</label>   
			        <input id="priUrl" name="priUrl" class="easyui-textbox w_200"> 
			    </div>  
			    <div class="input_box">   
			        <label>页面图标：</label>   
			        <input id="priIcon" name="priIcon" class="easyui-textbox w_200"> 
			    </div>  
			    <div class="input_box">   
			        <label>　　排序：</label>   
			        <input id="priOrder" name="priOrder" class="easyui-textbox w_200"> 
			    </div>  
			     <div class="input_box">   
			        <label>　　上级：</label>
			        <select id="priParent" name="priParent" class="easyui-combobox" style="width:205px">   
					    <option value="0" >根目录</option> 
					    <c:forEach items="${privilegeList}" var="pri">
					    	<option value="${pri.priId }">${pri.priName }</option> 
					    </c:forEach>
					</select>
			    </div>
				<input id="priId" name="priId" type="hidden" value="" />
				<input id="priStatus" name="priStatus" type="hidden" value="" />
			</form>
		</div>
		<div id="addPri_btn">
			<a href="javascript:void(0)" onclick="savePri()" class="easyui-linkbutton" data-options="iconCls:'icon-ok'">保存</a>
			<a href="javascript:void(0)" onclick="javascript: $('#addPri').dialog('close');" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">取消</a>
		</div>
	</body>
</html>