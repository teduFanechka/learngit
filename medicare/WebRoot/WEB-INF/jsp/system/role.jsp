<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>角色管理</title>
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
		<script src="${path}/js/yiwei.js"></script>
		<script type="text/javascript">
			$(function() {
				datagrid2("role", "getAllRole");

			    $('#privilege').treegrid({
					url : 'getPrivilege',
					idField : 'priId',
					treeField : 'priName',
					fitColumns : true,
					border : false,
					columns : [ [ 
						{field : 'priId',title : '权限id',hidden : true }, 
						{field : 'priName',title : '权限名称',
							formatter:function(value,row,index){
								return "<input type='checkbox' name='pri' value='"+row.priId+"' />"+row.priName;
				        	}
						}, 
					] ]
				});
			})
		
			//显示状态
			function status(value, row, index) {
			    if (row.roStatus == 1) { // 这里是判断哪些行
			        return '<a href="#"></a><img src="images/accept.png"  title="已启用" alt="" />';
			    } else {
			        return '<a href="#"></a><img src="images/delete.png" title="已停用" alt="" />';
			    }
			}

			//打开添加角色
			function openAddRole() {
			    $('#addRole').dialog('open').dialog('center').dialog('setTitle', '添加角色');
			    $('#form').form('clear');
			}

			//打开修改角色
			function openUpdateRole() {
			    var array = $('#role').datagrid('getSelections');
			    if (array.length > 1) {
			    	$.messager.alert('系统提示','只能选择一个角色修改','warning');
			    } else if (array.length < 1) {
			        $.messager.alert('系统提示','请选择需要修改的角色','warning');
			    } else {
			        var row = $('#role').datagrid('getSelected'); // 获取选中的行
			        $('#addRole').dialog('open').dialog('center').dialog('setTitle', '修改角色');
			        $('#form').form('load', row);
			    }
			}

			//保存角色
			function saveRole() {
			    $.ajax({
			        cache: true,
			        type: "POST",
			        url: "addOrUpdateRole",
			        data: $('#form').serialize(),
			        async: false,
			        success: function(data) {
			            $.messager.show({
			                title: '系统提示',
			                msg: data.status
			            });
			            $('#role').datagrid('reload'); // 刷新
			            $('#addRole').dialog('close');
			        },
			        error: function(request) {
			            $.messager.alert('系统提示','操作错误','error');
			        }
			    });
			}

			//删除角色
			function deleteRole() {
			    var array = $('#role').datagrid('getSelections');
			    var ids = "";
			    for (var i = 0; i < array.length; i++) {
			    	if (i != array.length - 1) {
			            ids += array[i].roId + ",";
			        } else {
			            ids += array[i].roId;
			        }
			    }
			    if (array != "") {
			        $.messager.confirm('系统提示', '是否要删除选中角色?', function(r) {
			            if (r) {
			                $.post("delRole", {ids: ids}, function(data) { 
			                	$('#role').datagrid('reload');
			                    $.messager.show({
			                        title: '系统提示',
			                        msg: data.status
			                    });
			                });
			            }
			        });
			    } else {
			        $.messager.alert("系统提示", "请选择要删除的角色！", "warning");
			    }
			}

			//启用或禁用
			function updateStatus(status){
				var array = $('#role').datagrid('getSelections'); // 获取选中行数组
			    if (array.length > 1) {
			    	$.messager.alert('系统提示','只能选择一个角色进行状态修改','warning');
			    } else if (array.length < 1) {
			        $.messager.alert('系统提示','请选择需要修改状态的角色','warning');
			    } else {
				    var info;
				    if(status==1){
				    	info = "是否启用该角色？";
				    } else {
				    	info = "是否禁用该角色？禁用后相关用户将不能使用部分功能";
				    }
			    	$.messager.confirm('系统提示', info, function(r) {
			    		if (r) {
					    	var roId = $('#role').datagrid('getSelected').roId;
					        $.post("updateRoleStatus", {status:status,roId:roId}, function(data){
								$('#role').datagrid('reload');
								$.messager.show({
					                title: '系统提示',
					                msg: data.status
					            });
							});
				        }
			        });
			    }
			}

			//打开权限设置
			function openPriSet(){
				$("input[name='pri']").removeAttr('checked');// 清除弹出框内的checkbox 勾选状态
			    var array = $('#role').datagrid('getSelections'); 
			    if (array.length > 1) {
			    	$.messager.alert('系统提示','只能选择一个角色进行设置', 'warning');
			    } else if (array.length < 1) {
			    	$.messager.alert('系统提示','请选择一个要设置权限的角色', 'warning');
			    } else {
			    	var roId=$('#role').datagrid('getSelected').roId;
			    	$.get("getPriRole",{roId:roId},function(data){
			    		var priList = data.list;
		    			$("input[name='pri']").each(function(){//遍历所有checkbox
		    				for ( var i = 0; i < priList.length; i++) {//遍历辖区数组   	
			    				if(priList[i] == $(this).val()){
			    					$(this).attr("checked","checked"); 
			    				 }
			    			}
		    			});
			    	})
			    	$('#priSet').dialog('open');
			    }
			}

			//保存权限设置
			function savePriSet(){
				var roId = $('#role').datagrid('getSelected').roId;
				var items = [];
				$("input:checkbox[name='pri']:checked").each(function() {
					items.push($(this).val());  // 每一个被选中项的值push到数组中
				});
				var priList = items.toString();// 定义选中的checkbox value 以,分隔
				$.post("addPriRole",{priList:priList,roId:roId}, function(data){
					$('#priSet').dialog('close');
					$('#role').datagrid('reload');
					$.messager.show({
				    	title: '系统提示',
				        msg: data.status
				    });
				});
			}

			//全选
			function selectAll(){
				var ids = document.getElementsByName('pri');
				for(var i = 0; i < ids.length; i++){
					ids[i].checked = document.getElementById('priAll').checked;
				}
			}
			
		</script>
	</head>
	<body>
		<div class="easyui-layout" data-options="fit:'true',">
			<div class="easyui-panel" title="角色管理" data-options="border:true,fit:true" iconCls="icon-zc">
				<table id="role" data-options="checkOnSelect:true,singleSelect:true,fit:true,rownumbers:true,pagination:true" width="95%" cellpadding="2" cellspacing="1" toolbar="#toolbar">
					<thead>
						<tr>
							<th data-options="field:'roId',halign:'center',align:'left',hidden:true,width:160">角色ID</th>
							<th data-options="field:'roName',halign:'center',width:160">角色名称</th>
							<th data-options="field:'roOrder',halign:'center',align:'center',width:200">排序</th>
							<th data-options="field:'roStatus',halign:'center',align:'center',width:60,formatter:status">启用状态</th>
						</tr>
					</thead>
				</table>
			</div>
			<div id="toolbar" style="width: 100%; height: 25px; padding: 2px 0;">
				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="openAddRole()" data-options="iconCls:'icon-add',plain:true" style="float: left;">添加角色</a>
				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="openUpdateRole()" data-options="iconCls:'icon-edit',plain:true" style="float: left;" >修改角色</a>
				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="deleteRole()" data-options="iconCls:'icon-remove',plain:true" style="float: left;" >删除角色</a>
				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="updateStatus('1')" data-options="iconCls:'icon-accept3',plain:true" style="float: left;"  >启用</a>
				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="updateStatus('0')" data-options="iconCls:'icon-delete',plain:true" style="float: left;"  >禁用</a>
				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="openPriSet()" data-options="iconCls:'icon-xqwh',plain:true" >权限设置</a>
			</div>
		</div>
		
		<!-- 添加角色 -->
		<div id="addRole" class="easyui-dialog" title="添加角色" style="width: 400px; height:300px;" data-options="iconCls:'icon-add',buttons:'#addRole_btn',closed:true,modal:true,">
			<form id="form" action="" method="">
				<div class="input_box">   
			        <h1 style="margin:25px 0 20px 0">角色信息</h1>
			    </div>  
				<div class="input_box">   
			        <label>角色名称：</label>   
			        <input id="roName" name="roName" class="easyui-textbox w_200"> 
			    </div>  
			    <div class="input_box">   
			        <label>　　排序：</label>   
			        <input id="roOrder" name="roOrder" class="easyui-textbox w_200"> 
			    </div>  
				<input id="roId" name="roId" type="hidden" value="" />
				<input id="roStatus" name="roStatus" type="hidden" value="" />
			</form>
		</div>
		<div id="addRole_btn">
			<a href="javascript:void(0)" onclick="saveRole()" class="easyui-linkbutton" data-options="iconCls:'icon-ok'">保存</a>
			<a href="javascript:void(0)" onclick="javascript: $('#addRole').dialog('close');" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">取消</a>
		</div>

		<!-- 权限设置 -->
		<div id="priSet" class="easyui-dialog" title="权限设置" style="height:750px;" data-options="iconCls:'icon-xqwh',buttons:'#priSet_btn',closed:true,modal:true,">
			<div id="privilege"></div>
		</div>
		<div id="priSet_btn">
			<input type="checkbox" id="priAll" onclick="selectAll()" />&nbsp;全选&nbsp;
			<a href="javascript:void(0)" onclick="savePriSet()" class="easyui-linkbutton" data-options="iconCls:'icon-ok'">保存</a>
			<a href="javascript:void(0)" onclick="javascript: $('#priSet').dialog('close');" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">取消</a>
		</div>
		
	</body>
</html>
