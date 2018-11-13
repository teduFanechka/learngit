<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>销售统计</title>
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
				$("#begindt").datebox("setValue", begin_time);  
				$("#enddt").datebox("setValue", end_time);
			});

			//查询
			function getSaleslist(){  
				var begin_time = $("#begindt").datebox("getValue");  
				var end_time = $("#enddt").datebox("getValue");
				var ahref = "getAllSales2?flag=1&bgdt=" + begin_time + "&eddt=" + end_time;
			    $('#salesquery').datagrid({url:ahref,
			    	 onLoadSuccess:function(data){
				        if(data.total == 0){
				            var body = $(this).data().datagrid.dc.body2;
				            body.find('table tbody').append('<tr><td width="'+body.width()+'" style="height: 25px; text-align: center;" colspan="16">没有查询到满足条件的数据</td></tr>');
				        }else{
				        	var rows = $('#salesquery').getData();
							for(var i=0; i<rows.length; i++){
							    var row = rows[i]
							}
				        }
				    }
			    });     
			}
	
			function fmtDrugName(val, row, index) {
				var drugCode=row.drugcode;
				var drugName=row.drugname;
			    return '<a href="javascript:void(0);" aurl="toSaleslist2?dc='+drugCode+'" onclick="showlist(this);" name="'+drugName+'">'+row.drugname+'</a>';
			}
	
			//添加新选项卡显示销售记录
			function showlist(obj){
				var ahref = $(obj).attr('aurl');
				var begin_time = $("#begindt").datebox("getValue");  
				var end_time = $("#enddt").datebox("getValue");
				ahref = ahref + '&bgdt=' + begin_time + '&eddt=' + end_time;
				var title = obj.name + ' - 销售记录';
				window.parent.addTab(title, ahref);//调用父类方法添加选项卡
			}
	
		</script>
	</head>

	<body>
	    <div class="easyui-layout" title="销售查询列表" data-options="collapsible:false,noheader:true,border:false,fit:true" >
	    	<div name="querybar" title="查询条件" data-options="region:'north',border:false" style="padding:5px; height:65px;">
				<div class="top-time" style="background: none; float: left; margin-left: 10px;">
					<table>
						<tr>
							<td>开始日期:</td>
							<td><input id="begindt" class="easyui-datebox" data-options="sharedCalendar:'#cc',editable:false" /></td>
							<td>结束日期:</td>
							<td><input id="enddt" class="easyui-datebox"data-options="sharedCalendar:'#cc',editable:false" /></td>
						</tr>
					</table>

					<div id="cc" class="easyui-calendar"></div>
					<script type="text/javascript">
						var begin_time = '${begindt}';
						var end_time = '${enddt}';
					</script>
				</div>
				<div style="float: left; margin-left: 5px;">
					<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'" style="width: 80px" onclick="javascript:getSaleslist();">查询</a>
				</div>
	    	</div>
	    	<div id="queryresult" title="查询结果" data-options="region:'center',noheader:false,border:false" style="height:300px;">
				<div class="easyui-layout" title="结果" data-options="noheader:true,border:false,fit:true" >
					<div id="querydata" data-options="region:'center',noheader:true,border:false" >
						<!-- datagrid表格 -->
						<table id="salesquery" name="${cusId}" class="easyui-datagrid" data-options="loadMsg:'数据装载中......',pagination:true,singleSelect:true,fit:true,border:false,rownumbers:true">
						<thead>
							<tr>
								<th field="siId" align="center" hidden=true></th>
								<th field="drugcode" halign="center" align="left" width="100" sortable="true">项目编码</th>
								<th field="drugname" halign="center" align="left" width="180" sortable="true" formatter="fmtDrugName">项目名称</th>
								<th field="totalquantity" halign="center" align="right" width="80" sortable="true">销售总量</th>
								<th field="totalamount" halign="center" align="right" width="80" sortable="true">销售总额</th>
								<th field="drugspecification" halign="center" align="left" width="100">项目规格</th>
								<th field="drugmfrs" halign="center" align="left" width="180" formatter="fmtNull">生产商</th>
								<th field="drugmadein" halign="center" align="left" width="180" formatter="fmtNull">产地</th>
							</tr>
						</thead>				
						</table>
					</div>
				</div>
			</div>
		</div>
	</body>
</html>