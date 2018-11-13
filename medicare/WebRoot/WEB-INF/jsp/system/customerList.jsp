<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>定点列表</title>
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
	<body>
		<div id="content_wrap" class="easyui-layout content_wrap" data-options="fit:true" style="">
			<!-- 左侧药店列表  -->
			<div data-options="region:'west',title:'定点信息',split:true,width:200,minWidth:200,border:false" class="menu_warp">
				<div class="easyui-list">
					<ul class="easyui-tree" data-options="lines:false">
						<c:forEach items="${customerList}" var="customer" varStatus="status">
							<li data-options="state:'open', selected:true, checked:true" iconCls="icon-folder1">
								<span><a ghref="${path }/getCustomerInfo?cusId=${customer.cusId}">${status.count}.${customer.cusName }</a> </span>
								<ul>
									<li><span><a cusname="${customer.cusName}" ghref="${path }/toDrugcatalog?cusId=${customer.cusId}&cusFlag=${cusFlag}">项目编码</a></span></li>
									<li><span><a cusname="${customer.cusName}" ghref="${path }/toSupplier?cusId=${customer.cusId}&cusFlag=${cusFlag}">供应商/生产商信息</a></span></li>
									<li><span><a cusname="${customer.cusName}" ghref="${path }/toEmployee?cusId=${customer.cusId}">医护人员信息/职业药师</a></span></li>
									<li><span><a cusname="${customer.cusName}" ghref="${path }/toWarehouseitem?cusId=${customer.cusId}">入库信息</a></span></li>
									<li><span><a cusname="${customer.cusName}" ghref="${path }/toDeliveryitem?cusId=${customer.cusId}">出库信息</a></span></li>
									<li><span><a cusname="${customer.cusName}" ghref="${path }/toItemstock?cusId=${customer.cusId}">库存信息</a></span></li>
									<li><span><a cusname="${customer.cusName}" ghref="${path }/toSalesitem?cusId=${customer.cusId}">销售信息/门诊住院结算</a></span></li>
									<li><span><a cusname="${customer.cusName}" ghref="${path }/toWarehouse?cusId=${customer.cusId}">库房信息</a></span></li>
									<li><span><a cusname="${customer.cusName}" ghref="${path }/toDepartment?cusId=${customer.cusId}">科室信息</a></span></li>
									<c:if test="${cusFlag!='301' }">
										<li><span><a cusname="${customer.cusName}" ghref="${path }/toPrescribe?cusId=${customer.cusId}">处方信息</a></span></li>
									</c:if>
									<c:if test="${cusFlag=='301' }"><!-- 医院 -->
										<li><span><a cusname="${customer.cusName}" ghref="${path }/toHospitalized?cusId=${customer.cusId}">住院记录</a></span></li>
										<li><span><a cusname="${customer.cusName}" ghref="${path }/toDischarged?cusId=${customer.cusId}">出院记录</a></span></li>
										<li><span><a cusname="${customer.cusName}" ghref="${path }/toDoctororder?cusId=${customer.cusId}">医嘱信息</a></span></li>
										<li><span><a cusname="${customer.cusName}" ghref="${path }/toOrderperform?cusId=${customer.cusId}">医嘱执行记录</a></span></li>
										<li><span><a cusname="${customer.cusName}" ghref="${path }/toClinicrecords?cusId=${customer.cusId}">诊断记录</a></span></li>
										<li><span><a cusname="${customer.cusName}" ghref="${path }/toMedrecords?cusId=${customer.cusId}">病案首页</a></span></li>
										<li><span><a cusname="${customer.cusName}" ghref="${path }/toCheckresult?cusId=${customer.cusId}">检查、检验结果</a></span></li>
										<li><span><a cusname="${customer.cusName}" ghref="${path }/toCheckdetail?cusId=${customer.cusId}">检查、检验结果详细</a></span></li>
									</c:if>
								</ul>
							</li>
						</c:forEach>
					</ul>
				</div>
			</div>
			
			<!-- 中间药店内容 -->
			<div id="mainPanle" region="center" style="overflow: auto">
				<div id="tabs" class="easyui-tabs" fit="true" border="false">

					<div id="mm" class="easyui-menu" style="width: 150px; overflow: scroll">
						<div id="mm-tabupdate">刷新</div>
						<div class="menu-sep"></div>
						<div id="mm-tabclose">关闭</div>
						<div id="mm-tabcloseright">关闭右侧</div>
						<div id="mm-tabcloseleft">关闭左侧</div>
						<div id="mm-tabcloseother">关闭其他</div>
						<div id="mm-tabcloseall">关闭全部</div>
					</div>
				</div>
			</div>
		</div>
	</body>
</html>