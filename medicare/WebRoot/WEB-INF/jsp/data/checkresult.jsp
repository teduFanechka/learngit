<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>检查、检验结果</title>

		<c:set value="${pageContext.request.contextPath}" var="path" scope="page" />
		<link rel="stylesheet" href="${path}/easyui/themes/default/easyui.css" />
		<link rel="stylesheet" href="${path}/easyui/themes/icon.css" />
		<script src="${path}/easyui/jquery-1.7.2.min.js"></script>
		<script src="${path}/easyui/jquery.easyui.min.js"></script>
		<script src="${path}/easyui/locale/easyui-lang-zh_CN.js"></script>
		<script src="${path}/js/yiwei.js"></script>
		<script type="text/javascript">
			$(function() {
				var cusId = $("#checkresult").attr("name");
				var url = 'getAllCheckresult?cusId='+cusId;
				datagrid('checkresult', '检查、检验结果', 'crId', url);

			});

			//查询
			function queryForm(){  
			  	var cusId=$("#checkresult").attr("name");
				var crCheckno = $("#crCheckno").val(); 
				var crHospno = $("#crHospno").val(); 
				var crPtsname = $("#crPtsname").val(); 
				var crItemcode = $("#crItemcode").val(); 
			  	$('#checkresult').datagrid({
					url:'getAllCheckresult',
					queryParams:{
			  			cusId:cusId,
			  			crCheckno:crCheckno,
			  			crHospno:crHospno,
			  			crPtsname:crPtsname,
			  			crItemcode:crItemcode
				    },
				});
			} 
	
			//清空
			function clearForm() {
				 $('#searchForm').form('clear');  
			}

			//删除重复数据
			function deleteRepeat(type){
				var cusId=$("#checkresult").attr("name");
				$.messager.show({
					title: "系统提示",
	                msg: "正在删除检查、检验结果重复数据..."
				});	
				$.ajax({
					type: "POST",
			        url: "deleteRepeat",
			        data:{type:type,cusId:cusId,name:'检查、检验结果'},
			        success: function(data) {
			        	$("#checkresult").datagrid('reload');
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
							<td><input id="crCheckno" name="crCheckno" style="width:100px" /></td>
							<th>住院号/门诊号</th>
							<td><input id="crHospno" name="crHospno" style="width:100px" /></td>
							<th>患者姓名</th>
							<td><input id="crPtsname" name="crPtsname" style="width:100px" /></td>
							<th>项目编码/项目名称</th>
							<td><input id="crItemcode" name="crItemcode" style="width:100px" /></td>
							<td><a class="easyui-linkbutton" href="javascript:void(0);" iconCls="icon-search" onclick="queryForm();">查询</a></td>
							<td><a class="easyui-linkbutton" href="javascript:void(0);" iconCls="icon-remove" onclick="clearForm();">清空</a></td>
							<td><a class="easyui-linkbutton" href="javascript:void(0);" iconCls="icon-cancel" onclick="deleteRepeat('checkresult');">删除重复数据</a></td>
						</tr>
					</table>
				</form>
			</div>
			
			<div data-options="region:'center',split:false">
				<table id="checkresult" name="${cusId }">
					<thead>
						<tr>
							<th field="crId" align="center" hidden=true></th>
							<th field="cusId" hidden=true align="center">客户ID</th>
							<th data-options="field:'crCheckres',align:'center',formatter:formatcrCheckres" hidden="true">检查来源</th>
							<th field="crCheckno" align="center">检查流水号</th>
							<th field="crCaseno" align="center">病案号</th>
							<th field="crHospno" align="center">住院号/门诊号</th>
							<th field="crPtsname" align="center" >患者姓名</th>
							<th data-options="field:'crPtssex',align:'center',formatter:formatOperSex" hidden="true">患者性别</th>
							<th field="crAge" align="center" hidden="true">年龄</th>
							<th field="crIdnum" align="center" hidden="true">身份证号</th>
							<th field="crHealthcard" hidden="true">社保卡号</th>
							<th field="crDeptcode" hidden="true" align="center">科室编码</th>
							<th field="crDept" align="center">科室名称</th>
							<th field="crRoom" align="center" hidden="true">房间号</th>
							<th field="crBed" align="center" hidden="true">床位号</th>
							<th field="crClinicdiag" align="center" hidden="true">临床诊断</th>
							<th field="crCheckposi" align="center" hidden="true">检查部位</th>
							<th field="crModel" align="center" hidden="true">标本号</th>
							<th field="crModeltype" align="center" hidden="true">标本种类</th>
							<th field="crItemcode" align="center">项目编码</th>
							<th field="crItemname" align="center">项目名称</th>
							<th field="crTool" align="center" hidden="true">使用仪器</th>
							<th field="crCheckresult" align="center" hidden="true">检查结果</th>
							<th field="crSenddate" align="center">送检日期</th>
							<th field="crCheckdate" align="center">检验日期</th>
							<th field="crReportdate" align="center">报告日期</th>
							<th field="crSenddoc" hidden=true align="center">送检医生编码</th>
							<th field="crSenddocno" align="center">送检医生名称</th>
							<th field="crCheckdoc"  hidden=true align="center" hidden="true">检验医生编码</th>
							<th field="crCheckdocno" align="center" hidden="true">检验医生名称</th>
							<th field="crAuditdoc"  hidden=true align="center" hidden="true">审核医生编码</th>
							<th field="crAuditdocno" align="center" hidden="true">审核医生名称</th>
							<th field="crComment"  hidden="true" align="center">备注</th>
							<th field="crPicktime" hidden="true" align="center">采集时间</th>
							<th field="crAddtime" align="center">创建时间</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
	</body>
</html>