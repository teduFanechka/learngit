<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>综合查询</title>
		<c:set value="${pageContext.request.contextPath}" var="path" scope="page" />
		<link rel="stylesheet" href="${path}/easyui/themes/default/easyui.css" />
		<link rel="stylesheet" href="${path}/easyui/themes/icon.css" />
		<link rel="stylesheet" type="text/css" href="${path}/css/mystyle.css" />
		<script src="${path}/easyui/jquery-1.7.2.min.js"></script>
		<script src="${path}/easyui/jquery.easyui.min.js"></script>
		<script src="${path}/easyui/locale/easyui-lang-zh_CN.js"></script>
		<script src="${path}/js/yiwei.js"></script>
		<script type="text/javascript">
			$(function() {
				var cusFlag=$('#customer').attr('cusFlag');
				var areacode=$('#customer').attr('areacode');
				var url = "getAllAreaCustomer?cusFlag="+cusFlag+"&areacode="+areacode;
			    datagrid2("customer", url);
			});

			// 根据值显示图片
			function fmtStatus(value, row, index) {
				if (value == 1 || value == 2) { // 这里是判断哪些行
			        return '<img src="images/accept.png"/>';
			    } else {
			        return '<img src="images/warning.png"/>';
			    }
			}
			
			//显示定点类型
			function fmtFlag(value,row,index){
				if(value==101){
					return '单体药店';
				}else if(value==102){
					return '连锁药店';
				}else if(value==201){
					return '门诊';
				}else {
					return '医院';
				}
			}

			//显示区县
			function fmtArea(value, row, index) {
				var cusId = row.cusId;
				var text;
				$.ajax({
					type: "POST",
					url:"cusArea",
					data:{cusId:cusId},
					async: false,//默认true异步，false同步
					success: function(data){
						text = data.areaName;
					}
				})
				if(text==null){
					return "<font color='red'>请设置区划</font>";
				} else {
					return text;
				}
			}
	
			//显示当前定点进销存数据
			function fmtCusname(value,row,index){
				var value2 = escape(value);//escape编码（防止空格）,unescape解码
				var cusId = row.cusId;
				var cusFlag = row.cusFlag;
				var url = "getCustomerList?cusId="+cusId+"&cusFlag="+cusFlag;
				return "<a href='javascript:void(0)' onclick=show('"+url+"','"+value2+"'); >"+unescape(value2)+"</a>";
			}
			
			//添加新选项卡显示进销存数据
			function show(url,value){
				window.parent.addTab(unescape(value), url);//调用父类方法添加选项卡
			}
	
			// 数据检索
			function findUser() {
			    // 先取得 datagrid 的查询参数
			    var params = $('#customer').datagrid('options').queryParams;
			    var fields = $('#searchForm').serializeArray(); // 自动序列化表单元素为JSON对象
			    $.each(fields, function(i, field) {
			       	params[field.name] = field.value; // 设置查询参数
			   	});
			    $('#customer').datagrid('reload'); // 设置好查询参数 reload
			}

			//点击清空按钮出发事件
			function clearForm() {
			    $('#searchForm').form('clear');
			}

			//检查上传情况
			function checkUpload(){
				var array = $('#customer').datagrid('getSelections'); // 获取选中的行
			    if (array.length > 1) {
			    	$.messager.alert('警告','只能检查一个定点的上传情况','warning');
			    } else if (array.length < 1) {
			        $.messager.alert('警告','请选择需要检查的定点','warning');
			    } else {
			    	$.messager.show({
	                 	title: '系统提示',
	                    msg: "正在检查定点上传情况，请稍候"
		             });
			        var cusId = $('#customer').datagrid('getSelected').cusId; 
			        $.post("checkUpload",{cusId:cusId}, function(data) { 
						 $.messager.show({
		                 	title: '系统提示',
		                    msg: data.status
			             });	
					});
			    }
			}
			
		</script>
	</head>
	<body>
		<div class="easyui-layout" data-options="fit:'true'">
			<div class="easyui-panel" title="综合查询" data-options="border:true,fit:true" iconCls="icon-zc">
				<table id="customer" cusFlag="${cusFlag }" areacode="${areacode}" data-options="checkOnSelect:true,singleSelect:true,fit:true,rownumbers:true,pagination:true" cellpadding="2" cellspacing="1" toolbar="#toolbar">
					<thead>
						<tr>
							<th field="cusId" hidden="true" align="center">客户ID</th>
							<th field="cusParentid" hidden="true" align="center" editor="{type:'text'}">客户上级ID</th>
							<th field="cusRegip" hidden="true" align="center" editor="{type:'text'}">注册ip</th>
							<th field="cusUniqure" hidden="true" align="center" editor="{type:'text'}">唯一标识</th>
							<th data-options="field:'cusName',align:'left',width:300,formatter:fmtCusname">定点名称</th>
							<th data-options="field:'cusDareway',align:'center',width:100">定点编码</th>
							<th field="cusBranchcode" align="center" editor="{type:'text'}">分店编码</th>
							<th data-options="field:'cusPcode',align:'center',width:150,hidden:true">简码</th>
							<th data-options="field:'b',align:'center',formatter:fmtArea">所在区县</th>
							<th data-options="field:'cusAddr',align:'left',width:200">联系地址</th>
							<th data-options="field:'cusContact',align:'center',width:100">联系人</th>
							<th data-options="field:'cusPhone',align:'center',width:100">联系电话</th>
							<th data-options="field:'cusRegdate',align:'center',width:130" editor="{type:'datetimebox'}">注册日期</th>
							<th data-options="field:'cusFlag',align:'center',width:100,formatter:fmtFlag">定点类型</th>
							<th data-options="field:'cusStatus',align:'center',width:100,formatter:fmtStatus">状态</th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>
			
			<div id="toolbar" style="width: 200%; height: 25px; padding: 2px 0;">
				<div class="barbox1-1">
					<form id="searchForm" onkeydown="if(event.keyCode==13){queryForm();}">
						<div style="float:left;margin:0 2px;">
							<input id="searchname" name="cusName" class="easyui-searchbox" data-options="searcher:findUser,prompt:'请输入查询条件',"></input>
						</div>
						<div style="float:left;margin:0 2px;">
							<a href="#" class="clear" onClick="clearForm()"><img style="margin-top: 2px;" class="cl-xx-child" src="images/xx.png" /></a>
						</div>
					</form>
					<a href="#" class="easyui-linkbutton" onClick="checkUpload()" iconCls="icon-zoom" style="padding: 0 10px; margin: 0 2px;">检查上传情况</a>
				</div>
			</div>
			</div>
		</div>
	</body>
</html>
