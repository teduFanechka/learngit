<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>销售记录</title>
		<c:set value="${pageContext.request.contextPath}" var="path" scope="page" />
		<link rel="stylesheet" href="${path}/easyui/themes/default/easyui.css" />
		<link rel="stylesheet" href="${path }/css/ew-main.css" />
		<link rel="stylesheet" href="${path }/css/ew-home.css" />
		<link rel="stylesheet" href="${path}/easyui/themes/icon.css" />
		<script src="${path}/easyui/jquery-1.7.2.min.js"></script>
		<script src="${path}/easyui/jquery.easyui.min.js"></script>
		<script src="${path}/easyui/locale/easyui-lang-zh_CN.js"></script>
		<script src="${path}/js/yiwei.js"></script>
		<script type="text/javascript">
	
			$(function() {
				var drugcode = $("#drugcode").val();
				var begin_time = $("#begindt").text();  
				var end_time = $("#enddt").text();
			    var ahref = 'getSaleslist2?dc='+drugcode+ '&bgdt=' + begin_time + '&eddt=' + end_time; 
			    $('#saleslist').datagrid({url:ahref,
			    	 onLoadSuccess:function(data){
				        if(data.total == 0){
				            var body = $(this).data().datagrid.dc.body2;
				            body.find('table tbody').append('<tr><td width="'+body.width()+'" style="height: 25px; text-align: center;" colspan="16">没有查询到满足条件的数据</td></tr>');
				        }
				    }
			    });  
			});

			//医院名称
			function fmtCusName(value, row, index) {
				var text;
				$.ajax({
					type: "POST",
					url:"findCustomerName?cusId="+value,
					async: false,//默认true异步，false同步
					success: function(data){
						text = data.cusName;
					}
				})
				return text;
			}
			
		</script>
	</head>

	<body>
		<div class="easyui-layout" title="销售记录列表" data-options="noheader:true,border:false,fit:true" >
			<div id="cusinfo" data-options="collapsible:true,region:'north',border:false" style="padding:5px; height:85px;">
                  <input type="hidden" id="drugcode" value="${drug}" />
                  <table class="ew_table" style="width: 100%; ">
                  <tbody>
                       <tr><th colspan="4">销售时段&nbsp;<!-- <a href="javascript:void(0);" style="width: 80px" title="导出销售记录明细" onclick="javascript:exportSales();">[导出]</a> --></th></tr>
                       <tr><th>开始日期</th><td id="begindt">${begindt}</td><th>结束日期</th><td id="enddt">${enddt}</td></tr>
                  </tbody>
                  </table>
            </div>  
			<div id="listdata" data-options="region:'center',noheader:true,border:false" >
				<!-- datagrid表格 -->
				<table id="saleslist" name="${cusId}" class="easyui-datagrid" data-options="loadMsg:'数据装载中......',pagination:true,singleSelect:true,fit:true,border:false,showFooter:true,rownumbers:true">
				<thead>
					<tr>
						<th field="siId" align="center" hidden=true></th>
						<th field="cusId" align="center" formatter="fmtCusName">客户名称</th>
						<th field="cusParentid" hidden=true align="center">客户上级ID</th>
						<th field="soNo" align="center">销售编号</th>
						<th field="drugCode" align="center">项目编码</th>
						<th field="drugName" align="left">项目名称</th>
						<th field="drugNum" align="center">销售数量</th>
						<th field="drugSalesprice" align="center">销售价</th>
						<th field="siUnit" align="center">单位</th>
						<th field="drugSpecification" align="center">规格</th>
						<th field="drugBatchno" hidden=true align="center">生产批号</th>
						<th field="drugMfrs" hidden=true align="center">生产商</th>
						<th field="drugMadein" hidden=true align="center">产地</th>
						<th field="drugEid" hidden=true align="center">电子监管码</th>
						<th field="drugMfgdate" hidden=true align="center">生产日期</th>
						<th field="drugExpdate" hidden=true align="center">有效期</th>
						<th field="siOpcode" hidden=true align="center">销售员编码</th>
						<th field="soSalespsnname" align="center">患者姓名</th>
						<th data-options="field:'siPtssex',align:'center',formatter:formatOperSex">性别</th>
						<th field="siPtsage" align="center">年龄</th>
						<th field="siPtsidcard" hidden=true align="center">身份证号</th>
						<th field="siPtshealthcard" hidden=true align="center">医保卡号</th>
						<th data-options="field:'soPaytype',hidden:true,align:'center',formatter:formatOperPaytype">结算方式</th>
						<th field="cusDareway" hidden=true align="center">医院编码</th>
						<th field="siSettlementname" align="center">结算方式</th>
						<th data-options="field:'siStatus',align:'center',formatter:formatOpersiStatus">结算状态</th>
						<th field="siOpcode" hidden=true align="center">操作员编码</th>
						<th field="siOpname" align="center">操作员</th>
						<th field="soDatetime" align="center">销售日期</th>
						<th field="drugPicktime" hidden=true align="center">采集日期</th>
						<th field="soCreatedatetime" hidden=true align="center">创建日期</th>
					</tr>
				</thead>				
				</table>
			</div>
		</div>
	</body>
</html>
