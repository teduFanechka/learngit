<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>住院统计</title>
		<c:set value="${pageContext.request.contextPath}" var="path" scope="page" />
		<link rel="stylesheet" href="${path}/easyui/themes/default/easyui.css" />
		<link rel="stylesheet" href="${path}/easyui/themes/icon.css" />
		<link rel="stylesheet" href="${path}/css/icon.css" />
		<link rel="stylesheet" href="${path }/css/ew-main.css" />
		<link rel="stylesheet" href="${path }/css/ew-home.css" />
		<script src="${path}/easyui/jquery-1.7.2.min.js"></script>
		<script src="${path}/easyui/jquery.easyui.min.js"></script>
		<script src="${path}/js/listload.js"></script>
	</head>
	<script type="text/javascript">
		$(function() {
			$('#tt').tree({
				url:'getHospital',
				onBeforeExpand: function(node){//节点展开前触发，返回 false 则取消展开动作
					if(node){
						var cusName = encodeURI(encodeURI(node.attributes.cusName));
						$('#tt').tree('options').url = "getHospital?type="+node.attributes.type+"&cusId="+node.attributes.cusId+"&hospDept="+node.attributes.hospDept+"&cusName="+cusName;
					}
				},
				onSelect: function(node){//当节点被选中时触发
					if(node.attributes.type=="department"){//医院详情
						var url = "toCustomerInfo?id="+node.id;
						var content = "<iframe name='mainFrame' scrolling='auto' frameborder='0'  src='" + url + "' style='width:100%;height:100%;'></iframe>";
						$('#tabs').tabs('add', {
							title : node.text,
							content : content,
							closable : true
						});
					}
					/*if(node.attributes.type=="next"){//病人详情
						var url = "toHospitalInfo?id="+node.id;
						var content = "<iframe name='mainFrame' scrolling='auto' frameborder='0'  src='" + url + "' style='width:100%;height:100%;'></iframe>";
						$('#tabs').tabs('add', {
							title : node.attributes.cusName+"-"+node.text,
							content : content,
							closable : true
						});
					}*/
				}
				
			});
		});

		//清空搜索框
		function clearForm() {
			$('#searchText').searchbox('setValue', null);
			window.location.reload();
		}

		//搜索
		function searchCustomer(){
		     var searchText = encodeURI(encodeURI($("#searchText").val()));
		     if(searchText != ""){
		    	 $('#tt').tree({
		    		 url:'searchHospital?text='+searchText,
		    		 onSelect: function(node){//当节点被选中时触发
		    		 	var url = "toCustomerInfo?id="+node.id;
						var content = "<iframe name='mainFrame' scrolling='auto' frameborder='0'  src='" + url + "' style='width:100%;height:100%;'></iframe>";
						$('#tabs').tabs('add', {
							title : node.text,
							content : content,
							closable : true
						});
					}
		    	 });
		     }
		}
		
	</script>	
	<body>
		<div id="content_wrap" class="easyui-layout content_wrap" data-options="fit:true" style="">
			<!-- 左侧药店列表  -->
			<div data-options="region:'west',title:'住院统计',split:true,width:200,minWidth:200,border:false" class="menu_warp" style="">
				<div class="easyui-list">
					<input class="easyui-searchbox" data-options="searcher:searchCustomer,prompt:'搜索定点医院'" id="searchText">
					<a href="javascript:void(0);" onClick="clearForm();">
						<img style="margin:3px 5px;position: absolute"  src="images/xx.png"/>
					</a>
					
					<ul id="tt" class="easyui-tree"></ul>
				</div>
			</div>
			
			<!-- 中间药店内容 -->
			<div id="mainPanle" region="center" style="overflow: auto">
				<div id="tabs" class="easyui-tabs" fit="true" border="false">

					<div id="mm" class="easyui-menu" style="width: 150px; overflow: scroll">
						<div id="mm-tabreload">
							刷新
						</div>
						<div id="mm-tabcloseall">
							关闭全部
						</div>
						<div id="mm-tabcloseother">
							关闭其他
						</div>
						<div class="menu-sep"></div>
						<div id="mm-tabcloseright">
							关闭右侧标签
						</div>
						<div id="mm-tabcloseleft">
							关闭左侧标签
						</div>
					</div>
				</div>
			</div>
		</div>
	</body>
</html>
