<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>药品销售价统计排序</title>
		<script src="${path}/easyui/jquery-1.7.2.min.js"></script>
		<script src="${path}/js/highcharts.js"></script>
		<script src="${path}/js/public.js"></script>
		<script src="${path}/js/Calendar3.js"></script>
		<script type="text/javascript">
			var cusFlag;
			var drugName;// 查询的药品名称
			var cusNames = [];// 定义药品名称集合
			var totals = [];
			var chart;
	
			// 清空折线图
			function chartClear() {
				var series = chart.series;
				while (series.length > 0) {
					series[0].remove(false);
				}
				chart.redraw();
			}
	
			// 创建拆线图
			function createMyChart() {
				chart = new Highcharts.Chart(
						{
							chart : {
								type : 'bar',
								// 将报表对象渲染到层上
								renderTo : 'container'
							},
	
							title : {
								text : drugName + ':销售价排序',
								x : -20
							// center
							},
							subtitle : {
								text : '',
								x : -20
							},
							xAxis : {
								categories : [], // 'Id1', 'Id12', 'Id13'
								title : {
									text : '定点名称'
								}
							},
							yAxis : {
								min : 0,
								title : {
									text : '',
									align : 'high'
								},
								labels : {
									overflow : 'null'
								}
							},
							tooltip : {
								// 数据提示框(一般为单位)
							valueSuffix : '元'
						},
							plotOptions : {// 柱状图/条形图
								bar : {
									color : '#3399FF',// 柱子颜色
									dataLabels : {
										enabled : true
									}
								}
							},
							legend : { // 图例说明是包含图表中数列标志和名称的容器
								layout : 'vertical', // 垂直 (horizontal水平)
								align : 'right',
								verticalAlign : 'top',
								x : 0,
								y : 0,
								floating : true,
								borderWidth : 1,
								backgroundColor : ((Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'),
								shadow : true
							},
							series : [ {
								name : '销售价',
								data : []
							// 107, 31, 635
							} ],
							credits : {
								enabled : false
							// 禁用版权信息
							}
						});
				chart.showLoading();//显示加载中...
			}
	
			// 为图表设置值
			function drawMyChart(chartdata, init) {
				if (typeof chartdata != undefined) {
					cusNames = chartdata.cusNames;
					totals = chartdata.totals;
					avg = chartdata.avg;
					if (cusNames.length > 0) {
						chartClear();// 重绘柱形图
						chart.xAxis[0].setCategories(cusNames, true);
						// chart.yAxis[0].setTitle( { text : '平均' + avg }, true);
						var seriedata = {
							'name' : '销售价',
							'data' : totals
						};
						chart.addSeries(seriedata);
						document.getElementById("count").innerHTML = "所有药店销售价平均值:</b>"
								+ avg + "元";
					}
				}
				chart.hideLoading();//隐藏加载中...
			}
			// 初始化jsp
			$(function() {
				cusFlag = $("#container").attr("cusFlag");
				drugName = $("#container").attr("drugName");
				createMyChart();
				// 使用JQuery从后台获取JSON格式的数据
				jQuery.getJSON('getDrugSalepriceCountChart?drugName=' + drugName,
						{
							"cusFlag" : cusFlag
						}, function(data) {
							if (data.cusNames.length == 0) {
								alert("最近一周未查询到数据!");
							}
							drawMyChart(data, false);
						});
			});
			// 查询事件
			function dosubmit() {
				// 使用JQuery从后台获取JSON格式的数据
				var firstDate = $("input[name='firstDate']").val();
				var secondDate = $("input[name='secondDate']").val();
				if (firstDate == "" || secondDate == "") {
					return;
				}
	
				// 使用JQuery从后台获取JSON格式的数据
				jQuery.getJSON('getDrugSalepriceCountChart?drugName=' + drugName,
						{
							"cusFlag" : cusFlag,
							"firstDate" : firstDate,
							"secondDate" : secondDate
						}, function(data) {
							if (data.cusNames.length == 0) {
								alert("该时间段未查询到数据!");
							}
							drawMyChart(data, true);
						});
			}
					
		</script>
	</head>
	<body>
		<div id="container" style="min-width: 800px; height: 500px;margin-bottom: 5px;" cusFlag="${cusFlag }" drugName="${drugName }"></div>
		<dir style="display: inline;">
			<div style="float:left; margin: 0 0 0 50px;">
				<form>

					日期:&nbsp;&nbsp;从
					<input name="firstDate" type="text" size="10" maxlength="10" onclick="new Calendar().show(this);" readonly="readonly" />
					到
					<input name="secondDate" type="text" size="10" maxlength="10" onclick="new Calendar().show(this);" readonly="readonly" />
					<input type="button" onclick="dosubmit();" value="搜索" />
					<input type="reset" value="清空" />
				</form>
			</div>
			<div style="float: right;margin-right: 30px" id="count"></div>
		</dir>
	</body>
</html>
