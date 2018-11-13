<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>系统参数</title>
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
			    datagrid2("parameter", "getAllParameter");
			});
	
	
			//添加参数
			function addParameter() {
			    $('#addParam').dialog('open').dialog('center').dialog('setTitle', '添加参数');
			    $('#form').form('clear'); // 清除表单数据
			}
			
			//修改参数
			function updateParameter() {
			    var array = $('#parameter').datagrid('getSelections');
			    var len = array.length;
			    if (array > 1) {
			        $.messager.alert('系统提示','只能选择一个参数修改','warning');  
			    } else if (array.length < 1) {
			        $.messager.alert('系统提示','请选择需要修改的参数','warning'); 
			    } else {
			        var row = $('#parameter').datagrid('getSelected'); // 获取选中的行
			        $('#addParam').dialog('open').dialog('center').dialog('setTitle', '修改参数');
			        $('#form').form('load', row);
			    }
			}
			
			//保存参数
			function saveParameter() {
			    $.ajax({
			        cache: true,
			        type: "POST",
			        url: 'addOrUpdateParameter',
			        data: $('#form').serialize(),
			        async: false,
			        success: function(data) {
			            $.messager.show({
			                title: '系统提示',
			                msg: data.status
			            });
			            $('#parameter').datagrid('reload'); // 刷新
			            $('#addParam').dialog('close');
			        },
			        error: function(request) {
			            $.messager.alert('系统提示','参数名不能重复！','error');
			        }
			    });
			}
			
			//删除参数
			function deleteParameter() {
				var array = $('#parameter').datagrid('getSelections'); 
			    var ids = ""; 
			    for (var i = 0; i < array.length; i++) { 
			    	if (i != array.length - 1) {
			            ids += array[i].paramId + ",";
			        } else {
			            ids += array[i].paramId;
			        }
			    }
			    if (array != "") {
			        $.messager.confirm('系统提示', '是否要删除选中参数？', function(r) {
			            if (r) {
			                $.post("delParameter", {ids:ids}, function(data) {
			                    $('#parameter').datagrid('reload');
			                    $.messager.show({
			                        title: '系统提示',
			                        msg: data.status
			                    });
			                });
			            }
			        });
			    } else {
			    	$.messager.alert("系统提示", "请先选择要删除的参数！", "warning");
			    }
			}
			
		</script>
	</head>
	
	<body>
		<div class="easyui-layout" data-options="fit:'true',">
			<div class="easyui-panel" title="系统参数" data-options="border:true,fit:true" iconCls="icon-zc">
				<table id="parameter" data-options="checkOnSelect:true,fit:true,singleSelect:true,rownumbers:true,pagination:true" width="95%" cellpadding="2" cellspacing="1" toolbar="#toolbar">
					<thead>
						<th data-options="field:'paramKey',halign:'center',align:'center'">参数名</th>
						<th data-options="field:'paramValue',halign:'center',align:'center'">参数值</th>
						</tr>
					</thead>
				</table>
			</div>
			<div id="toolbar" style="width: 100%; height: 25px; padding: 2px 0;">
				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="addParameter()"  data-options="iconCls:'icon-add',plain:true" style="float: left;">添加参数</a>
				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="updateParameter()" data-options="iconCls:'icon-edit',plain:true" style="float: left;">修改参数</a>
				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="deleteParameter()" data-options="iconCls:'icon-remove',plain:true" >删除参数</a>
			</div>
		</div>
		
		<!-- 添加参数 -->
		<div id="addParam" class="easyui-dialog" title="添加参数" style="width: 430px; height: 300px;" data-options="iconCls:'icon-add',buttons:'#addParam_btn',closed:true,modal:true,">
			<form id="form">
				<div class="input_box">   
			        <h1 style="margin:25px 0 20px 0">参数信息</h1>
			    </div>  
			    <div class="input_box">   
			        <label>参数名：</label>   
			        <input id="paramKey" name="paramKey" class="easyui-textbox w_200"> 
			    </div>  
			    <div class="input_box">   
			        <label>参数值：</label>   
			        <input id="paramValue" name="paramValue" class="easyui-textbox w_200"> 
			    </div> 
			    <input id="paramId" name="paramId" type="hidden" value="" />
			</form>
		</div>
		<div id="addParam_btn">
			<a href="#" onclick="javascript: saveParameter();" class="easyui-linkbutton" data-options="iconCls:'icon-ok'">保存</a>
			<a href="#" onclick="javascript: $('#addParam').dialog('close');" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">取消</a>
		</div>
	</body>
</html>