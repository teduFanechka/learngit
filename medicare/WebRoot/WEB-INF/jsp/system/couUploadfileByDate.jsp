<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>上传统计</title>
		<c:set value="${pageContext.request.contextPath}" var="path" scope="page" />
		<link rel="stylesheet" href="${path}/easyui/themes/default/easyui.css" />
		<link rel="stylesheet" href="${path}/easyui/themes/icon.css" />
		<script src="${path}/easyui/jquery-1.7.2.min.js"></script>
		<script src="${path}/easyui/jquery.easyui.min.js"></script>
		<script src="${path}/easyui/locale/easyui-lang-zh_CN.js"></script>
		<script src="${path}/js/publicload.js"></script>
		<script type="text/javascript">
			$(function() {
				var cusFlag = $("#cusFlag").attr("cusFlag");
				var datagrid;
				datagrid = $('#couUploadfileByDate').datagrid( {
					title : '上传统计',
					iconCls : 'icon-ok',
					pageSize : 30,// 默认选择的分页是每页5行数据
					pageList : [30, 50, 100, 200 ],// 可以选择的分页集合
					nowrap : false,// true,列内容多时自动折至第二行
					striped : true,// 设置为true将交替显示行背景。
					collapsible : false,// 显示可折叠按钮
					url : 'getCouUploadfileByDate?cusFlag=' + cusFlag,
					loadMsg : '数据装载中......',
					singleSelect : true,// 为true时只能选择单行,无法批量删除
					fit : true, // datagrid自适应宽度
					fitColumns : true,// 允许表格自动缩放，以适应父容器，防止水平滚动
					remoteSort : true,// 定义从服务器对数据进行排序。
					checkbox : false,
					pagination : true,// 分页
					rownumbers : false, // 行数
					queryParams : {}, // 查询条件
					onLoadSuccess : function() {
						$('.openinNewTab').click(function() {
							var tabTitle = $(this).text();
							var cusName = $(this).attr('cusName');
							if (typeof (cusName) != "undefined") {
								tabTitle = cusName;
							}
							// 对应index.jsp 新加的ghref属性
							var url = $(this).attr("ghref");
							addTab(tabTitle, url);
						});
					}
				});
				bindTabEvent();
				bindTabMenuEvent();
			});
			
			//查看详情
			function uploadDetail(val,row,index){
				var cusId=row.cusId;
				var cusName=row.cusName;
				return '<a class="openinNewTab" cusName='+cusName+' style="text-decoration:none;cursor:pointer;color:blue" ghref="toCouUploadfileDetail?cusId='+cusId+'" >'+cusName+'</a>';  
			}  
	
			//表格查询  
			function queryForm(){
			  var params = $('#couUploadfileByDate').datagrid('options').queryParams;
			  var fields =$('#searchForm').serializeArray();
			  $.each( fields, function(i, field){  
			      params[field.name] = field.value; //设置查询参数  
			  });   
			  $('#couUploadfileByDate').datagrid('reload'); //设置好查询参数 reload 
			} 
	
			//点击清空按钮出发事件
			function clearForm() {
				 $('#searchForm').form('clear');  
			}
				
		</script>
	</head>
	<body>
		<div id="content_wrap" class="easyui-layout content_wrap" data-options="fit:true">
			<!--查询 -->
			<div data-options="region:'north',title:'查询'" style="height: 62px; background: #F4F4F4;">
				<form id="searchForm"  onkeydown="if(event.keyCode==13){ queryForm();}">
					<table>
						<tr>
							<th>定点名称</th>
							<td><input id="searchname" name="cusName" /></td>
							<td><a class="easyui-linkbutton" href="javascript:void(0);" onclick="queryForm()" iconCls="icon-search">查询</a></td>
							<td><a class="easyui-linkbutton" href="javascript:void(0);" onclick="clearForm()" iconCls="icon-remove">清空</a></td>
							<td><input id="cusFlag" cusFlag="${cusFlag }" style="visibility: hidden"/></td>
						</tr>
					</table>
				</form>
			</div>
			<!-- 左侧药店列表  -->
			<div data-options="region:'west',title:'定点列表',split:true,width:800,minWidth:500,border:false" class="menu_warp">
				<table id="couUploadfileByDate" data-options="rownumbers:true">
					<thead>
						<tr>
							<th field="cusId" hidden=true align="center" editor="{type:'text'}">客户ID</th>
							<th field="cusName" align="left" editor="{type:'text'}" formatter="uploadDetail">定点名称</th>
							<th field="cufFileflag" align="left"  editor="{type:'text'}">定点编码</th>
							<th data-options="field:'cufUpdate',align:'center'">最后上传日期</th>
							<th field="cufRecordcount" align="center" editor="{type:'text'}">昨日上传类型数量</th>
							<th field="cufFilecount" align="center" editor="{type:'text'}">历史上传类型数量</th>							
						</tr>
					</thead>
				</table>
			</div>
			<!-- 中间药店内容 -->
			<div id="mainPanle" region="center" style="overflow: auto">
				<div id="tabs" class="easyui-tabs" fit="true" border="false">
					<div id="mm" class="easyui-menu" style="width: 150px; overflow: scroll">
						<div id="mm-tabreload">刷新</div>
						<div id="mm-tabcloseall">关闭全部</div>
						<div id="mm-tabcloseother">关闭其他</div>
						<div class="menu-sep"></div>
						<div id="mm-tabcloseright">关闭右侧标签</div>
						<div id="mm-tabcloseleft">关闭左侧标签</div>
					</div>
				</div>
			</div>
		</div>
	</body>
</html>
