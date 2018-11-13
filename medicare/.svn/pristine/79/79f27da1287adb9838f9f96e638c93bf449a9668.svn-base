<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>区划管理</title>
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
			    datagrid2("areacode", "getAllAreacode");
			});
			
			//根据状态显示图片
			function areaStatus(value, row, index) {
			    if (row.acStatus == 1) { // 这里是判断哪些行
			        return '<a href="#"></a><img src="images/accept.png"  title="已启用" alt="" />';
			    } else {
			        return '<a href="#"></a><img src="images/delete.png" title="已停用" alt="" />';
			    }
			}
	
			//添加区划
			function addAreacode() {
			    $('#addArea').dialog('open').dialog('center').dialog('setTitle', '添加区划');
			    $('#areaform').form('clear'); // 清除表单数据
			}
			
			//修改区划
			function updateAreacode() {
			    var array = $('#areacode').datagrid('getSelections'); // 获取编辑选中项数组
			    if (array.length > 1) {
			        $.messager.alert('系统提示','只能选择一个区划修改','warning');  
			    } else if (array.length < 1) {
			        $.messager.alert('系统提示','请选择需要修改的区划','warning'); 
			    } else {
			        var row = $('#areacode').datagrid('getSelected'); // 获取选中的行
			        $('#addArea').dialog('open').dialog('center').dialog('setTitle', '修改区划');
			        $('#areaform').form('load', row);
			    }
			}
			
			//保存区划
			function saveAreacode() {
			    $.ajax({
			        cache: true,
			        type: "POST",
			        url: 'addOrUpdateAreacode',
			        data: $('#areaform').serialize(),
			        async: false,
			        success: function(data) {
			            $.messager.show({
			                title: '系统提示',
			                msg: data.status
			            });
			            $('#areacode').datagrid('reload'); // 刷新
			            $('#addArea').dialog('close');
			        },
			        error: function(request) {
			            $.messager.alert('系统提示','区划编码不能重复！','error');
			        }
			    });
			}
			
			//删除区划
			function deleteAreacode() {
			    var array = $('#areacode').datagrid('getSelections'); // 获取删除选中项数组
			    var ids = "";
			    for (var i = 0; i < array.length; i++) {
			    	if (i != array.length - 1) {
			            ids += array[i].acId + ",";
			        } else {
			            ids += array[i].acId;
			        }
			    }
			    if (array != "") {
			        $.messager.confirm('系统提示', '是否要删除选中区划?', function(r) {
			            if (r) {
			                $.post("delAreacode", {ids: ids}, function(data) { 
			                	$('#areacode').datagrid('reload');
			                    $.messager.show({
			                        title: '系统提示',
			                        msg: data.status
			                    });
			                });
			            }
			        });
			    } else {
			        $.messager.alert("系统提示", "请选择要删除的区划！", "warning");
			    }
			}

			//启用或禁用
			function updateStatus(status){
				var array = $('#areacode').datagrid('getSelections'); // 获取选中行数组
			    if (array.length > 1) {
			    	$.messager.alert('系统提示','只能选择一个区划进行状态修改','warning');
			    } else if (array.length < 1) {
			        $.messager.alert('系统提示','请选择需要修改状态的区划','warning');
			    } else {
				    var info;
				    if(status==1){
				    	info = "是否启用该区划？";
				    } else {
				    	info = "是否禁用该区划？禁用后用户将不能查询该区划的定点信息";
				    }
			    	$.messager.confirm('系统提示', info, function(r) {
			    		if (r) {
					    	var acId = $('#areacode').datagrid('getSelected').acId;
					        $.post("updateAreacodeStatus", {status:status,acId:acId}, function(data){
								$('#areacode').datagrid('reload');
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
			<div class="easyui-panel" title="区划管理" data-options="border:true,fit:true" iconCls="icon-zc">
				<table id="areacode" data-options="checkOnSelect:true,fit:true,singleSelect:true,rownumbers:true,pagination:true" cellpadding="2" cellspacing="1" toolbar="#toolbar">
					<thead>
						<tr>
							<th data-options="field:'acId',align:'center',hidden:true">区划ID</th>
							<th data-options="field:'acAreacode',align:'center',width:80">区划编码</th>
							<th data-options="field:'acAreaname',align:'center',width:140">区划名称</th>
							<th data-options="field:'acOrder',align:'center',width:140">排序</th>
							<th data-options="field:'acStatus',align:'center',width:60,formatter:areaStatus">状态</th>
						</tr>
					</thead>
				</table>
			</div>
			<div id="toolbar" style="width: 100%; height: 25px; padding: 2px 0;">
				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="addAreacode();" data-options="iconCls:'icon-add',plain:true" style="float: left;">添加区划</a>
				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="updateAreacode();" data-options="iconCls:'icon-edit',plain:true" style="float: left;">修改区划</a>
				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="deleteAreacode()"  data-options="iconCls:'icon-remove',plain:true" style="float: left;">删除区划</a>
				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="updateStatus('1')" data-options="iconCls:'icon-accept3',plain:true" style="float: left;"  >启用</a>
				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="updateStatus('0')" data-options="iconCls:'icon-delete',plain:true" style="float: left;"  >禁用</a>
			</div>
		</div>
		
		<!-- 添加区划 -->
		<div id="addArea" class="easyui-dialog" title="添加区划" style="width: 430px; height: 300px;" data-options="iconCls:'icon-addd',buttons:'#addArea_btn',closed:true,modal:true,">
			<form id="areaform">
				<div class="input_box">   
			        <h1 style="margin:25px 0 20px 0">区划信息</h1>
			    </div>  
				<div class="input_box">   
			        <label>区划编码：</label>   
			        <input id="acAreacode" name="acAreacode" class="easyui-textbox w_200"> 
			    </div>  
			    <div class="input_box">   
			        <label>区划名称：</label>   
			        <input id="acAreaname" name="acAreaname" class="easyui-textbox w_200"> 
			    </div>
			    <div class="input_box">   
			        <label>排序：</label>   
			        <input id="acOrder" name="acOrder" class="easyui-textbox w_200"> 
			    </div>  
			    <input id="acId" name="acId" type="hidden" value="" />
			    <input id="acStatus" name="acStatus" type="hidden" value="" />
			</form>
		</div>
		<div id="addArea_btn">
			<a href="#" onclick="javascript: saveAreacode();" class="easyui-linkbutton" data-options="iconCls:'icon-ok'">保存</a>
			<a href="#" onclick="javascript: $('#addArea').dialog('close');" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">取消</a>
		</div>
	</body>
</html>