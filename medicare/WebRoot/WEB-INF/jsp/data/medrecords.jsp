<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>病案首页</title>
		<c:set value="${pageContext.request.contextPath}" var="path" scope="page" />
		<link rel="stylesheet" href="${path}/css/bootstrap.min.css" />
		<link rel="stylesheet" href="${path}/css/bootstrap.css" />
		<link rel="stylesheet" href="${path}/css/bootstrap-theme.css" />
		<link rel="stylesheet" href="${path}/easyui/themes/default/easyui.css" />
		<link rel="stylesheet" href="${path}/easyui/themes/icon.css" />
		<link rel="stylesheet" href="${path}/css/hospindex.css" />
		<script src="${path}/easyui/jquery-1.7.2.min.js"></script>
		<script src="${path}/easyui/jquery.easyui.min.js"></script>
		<script src="${path}/easyui/locale/easyui-lang-zh_CN.js"></script>
		<script type="text/javascript">
			$(function() {
				// var te= $("#tp").text(); 获取元素 文本内容
				// var ht= $("#tp").html(); 获取元素html 内容
				/**
				 * ********医疗付费方式编码表 1-21***********
				 */
				var tp = $("#tp").attr("name");
				var val = "";
				if (tp == "1") {
					val = "城镇职工基本医疗保险";
				} else if (tp == "2") {
					val = "城镇居民基本医疗保险";
				} else if (tp == "3") {
					val = "新型农村合作医疗";
				} else if (tp == "4") {
					val = "贫困救助";
				} else if (tp == "5") {
					val = "商业医疗保险";
				} else if (tp == "6") {
					val = "全公费";
				} else if (tp == "7") {
					val = "全自费";
				} else if (tp == "8") {
					val = "其他社会保险";
				} else {
					val = "其他";
				}
				$("#tp").html('&nbsp;&nbsp;' + val + '&nbsp;&nbsp;');
				/**
				 * ********婚姻状态编码表 1-22***********
				 */
				var mar = $("#mar").attr("name");
				if (mar == "1") {
					val = "未婚";
				} else if (mar == "2") {
					val = "已婚";
				} else if (mar == "3") {
					val = "丧偶";
				} else if (mar == "4") {
					val = "离婚";
				} else {
					val = "其他";
				}
				$("#mar").html('&nbsp;&nbsp;' + val + '&nbsp;&nbsp;');
	
				/**
				 * ********入院情况编码表 1-35***********
				 */
				var instatus = $("#ins").attr("name");
				if (instatus == "1") {
					val = "危";
				} else if (instatus == "2") {
					val = "急";
				} else if (instatus == "3") {
					val = "一般";
				} else {
					val = "其他"
				}
				$("#ins").html('&nbsp;&nbsp;' + val + '&nbsp;&nbsp;');
	
				/**
				 * ********过敏编码表 1-0***********
				 */
				var allergic = $("#all").attr("name");
				if (allergic == "0") {
					val = "否";
				} else if (allergic == "1") {
					val = "是";
				} else {
					val = "其他"
				}
				$("#all").html('&nbsp;&nbsp;' + val + '&nbsp;&nbsp;');
				/**
				 * ********诊断符合情况编码表 1-34***********
				 */
				var clnicout = $("#clnicout").attr("name");
				if (clnicout == "1") {
					val = "符合";
				} else if (clnicout == "2") {
					val = "不符合";
				} else if (clnicout == "3") {
					val = "不肯定"
				} else {
					val = "其他";
				}
				$("#clnicout").html('&nbsp;&nbsp;' + val + '&nbsp;&nbsp;');
	
				var inout = $("#inout").attr("name");
				if (inout == "1") {
					val = "符合";
				} else if (inout == "2") {
					val = "不符合";
				} else if (inout == "3") {
					val = "不肯定"
				} else {
					val = "其他";
				}
				$("#inout").html('&nbsp;&nbsp;' + val + '&nbsp;&nbsp;');
	
				var beforafter = $("#beforafter").attr("name");
				if (beforafter == "1") {
					val = "符合";
				} else if (beforafter == "2") {
					val = "不符合";
				} else if (beforafter == "3") {
					val = "不肯定"
				} else {
					val = "其他";
				}
				$("#beforafter").html('&nbsp;&nbsp;' + val + '&nbsp;&nbsp;');
	
				var clinicpath = $("#clinicpath").attr("name");
				if (clinicpath == "1") {
					val = "符合";
				} else if (clinicpath == "2") {
					val = "不符合";
				} else if (clinicpath == "3") {
					val = "不肯定"
				} else {
					val = "其他";
				}
				$("#clinicpath").html('&nbsp;&nbsp;' + val + '&nbsp;&nbsp;');
	
				var rediopath = $("#rediopath").attr("name");
				if (rediopath == "1") {
					val = "符合";
				} else if (rediopath == "2") {
					val = "不符合";
				} else if (rediopath == "3") {
					val = "不肯定"
				} else {
					val = "其他";
				}
				$("#rediopath").html('&nbsp;&nbsp;' + val + '&nbsp;&nbsp;');
				/**
				 * 病案质量编码表 1-24
				 */
				var qualty = $("#qualty").attr("name");
				if (qualty == "1") {
					val = "甲";
				} else if (qualty == "2") {
					val = "乙";
				} else if (qualty == "3") {
					val = "丙"
				} else {
					val = "其他";
				}
				$("#qualty").html('&nbsp;&nbsp;' + val + '&nbsp;&nbsp;');
				/**
				 * ********尸检编码表 1-0***********
				 */
				var autopsy = $("#autopsy").attr("name");
				if (autopsy == "0") {
					val = "否";
				} else if (autopsy == "1") {
					val = "是";
				} else {
					val = "未做说明"
				}
				$("#autopsy").html('&nbsp;&nbsp;' + val + '&nbsp;&nbsp;');
	
				/**
				 * ********血型类别编码表 1-26***********
				 */
				var bloodtype = $("#tp").attr("bloodtype");
				if (bloodtype = "1") {
					val = "A";
				} else if (bloodtype = "2") {
					val = "B";
				} else if (bloodtype = "3") {
					val = "AB";
				} else if (bloodtype = "4") {
					val = "O";
				} else if (bloodtype = "5") {
					val = "不详";
				} else {
					val = "未查";
				}
				$("#bloodtype").html('&nbsp;&nbsp;' + val + '&nbsp;&nbsp;');
				/**
				 * ********rh编码表 1-0***********
				 */
				var rh = $("#rh").attr("name");
				if (rh == "0") {
					val = "否";
				} else if (rh == "1") {
					val = "是";
				} else {
					val = "未做说明"
				}
				$("#rh").html('&nbsp;&nbsp;' + val + '&nbsp;&nbsp;');
				/**
				 * ********输血品种编码表 1-27**********
				 */
				var imr = $("#imt").attr("name");
				if (imt == "1") {
					val = "红细胞";
				} else if (imt == "2") {
					val = "血小板";
				} else if (imt == "3") {
					val = "血浆";
				} else if (imt == "4") {
					val = "全血";
				} else {
					val = "其他"
				}
				$("#imt").html('&nbsp;&nbsp;' + val + '&nbsp;&nbsp;');
				/**
				 * ********输血反应编码表 1-28**********
				 */
				var imr = $("#imr").attr("name");
				if (imr == "0") {
					val = "无";
				} else if (imr == "1") {
					val = "有";
				} else {
					val = "未做说明"
				}
				$("#imr").html('&nbsp;&nbsp;' + val + '&nbsp;&nbsp;');
	
			});

			//查询
			function queryForm(){  
				$("form").submit();
			} 

			//清空
			function clearForm() {
				 $('#merCaseno').val("");
				 $('#merPtsname').val("");
			}

			//删除重复数据
			function deleteRepeat(){
				var cusId=$("#cusId").val();
				$.messager.show({
					title: "系统提示",
	                msg: "正在删除病案首页重复数据..."
				});	
				$.ajax({
					type: "POST",
			        url: "deleteRepeat",
			        data:{type:'medrecords',cusId:cusId,name:'病案首页(概要)'},
			        success: function(data) {
			        	$.messager.show({
							title: "系统提示",
			                msg: data.info
						});
					}
				});
				$.ajax({
					type: "POST",
			        url: "deleteRepeat",
			        data:{type:'dischargediag',cusId:cusId,name:'病案首页(出院诊断)'},
			        success: function(data) {
			        	$.messager.show({
							title: "系统提示",
			                msg: data.info
						});
					}
				});
				$.ajax({
					type: "POST",
			        url: "deleteRepeat",
			        data:{type:'operation',cusId:cusId,name:'病案首页(手术操作)'},
			        success: function(data) {
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
		<div align="left" style="width: 80%; margin: 0 auto">
			<form id="form" action="toMedrecords" method="post" onkeydown="if(event.keyCode==13){ queryForm();}">
				<input id="cusId" name="cusId" value="${cusId }" type="hidden" />
				<table style="margin: 5px;">
					<tr>
						<td>病案号/住院号&nbsp;</td>
						<td><input id="merCaseno" name="merCaseno" value="${merCaseno }" style="width:100px" />&nbsp;</td>
						<td>患者姓名&nbsp;</td>
						<td><input id="merPtsname" name="merPtsname" value="${merPtsname }" style="width:100px" />&nbsp;</td>
						<td>
							<a class="easyui-linkbutton" href="javascript:void(0);" iconCls="icon-search" onclick="queryForm();">查询&nbsp;</a>
							<a class="easyui-linkbutton" href="javascript:void(0);" iconCls="icon-remove" onclick="clearForm();">清空&nbsp;</a>
							<a class="easyui-linkbutton" href="javascript:void(0);" iconCls="icon-cancel" onclick="deleteRepeat();">删除重复数据</a>
						</td>
					</tr>
				</table>
			</form>
			<c:forEach items="${page.list}" var="c">
				<div align="center">&nbsp;&nbsp; ${c.merOrgan }</div>
				<div style="font-size: 22px;" align="center"><strong>住 院 病 案 首 页</strong></div>
				<table width="100%">
					<tr>
						<td>病案号:${c.merCaseno }</td>
						<td align="right">&nbsp;&nbsp;&nbsp;&nbsp;第&nbsp;&nbsp;${c.merHosptimes }&nbsp;&nbsp;次住院</td>
						<td align="right">医疗付款方式: <u id="tp" name="${c.merPaytype }"></u></td>
					</tr>
				</table>
				<div class="div">
					<table>
						<tr>
							<td>姓名
								<u>&nbsp;&nbsp;${c.merPtsname }&nbsp;&nbsp;</u>性别
								<u>&nbsp;&nbsp;${c.merPtssex }&nbsp;&nbsp;</u>出生
								<u>&nbsp;&nbsp;${c.merBirthday }&nbsp;&nbsp;</u>年龄
								<u>&nbsp;&nbsp;${c.merAge }&nbsp;&nbsp;</u>婚姻
								<u id="mar" name="${c.merMarstatus }"></u>
							</td>
						</tr>
						<tr>
							<td>
								职业出生地
								<u>&nbsp;&nbsp;${c.merProfession }&nbsp;&nbsp;</u>民族
								<u>&nbsp;&nbsp;${c.merNation }&nbsp;&nbsp;</u>国籍
								<u>&nbsp;&nbsp;${c.merCountry }&nbsp;&nbsp;</u>身份证号
								<u>&nbsp;&nbsp;${c.merIdnumber }&nbsp;&nbsp;</u>
							</td>
						</tr>
						<tr>
							<td>
								工作单位及地址
								<u>&nbsp;&nbsp;${c.merWorkaddr }&nbsp;&nbsp;</u>电话
								<u>&nbsp;&nbsp;${c.merWorkphone }&nbsp;&nbsp;</u>邮政编码
								<u>&nbsp;&nbsp;${c.merWorkpost }&nbsp;&nbsp;</u>
							</td>
						</tr>
						<tr>
							<td>
								户口地址
								<u>&nbsp;&nbsp;${c.merHukouaddr }&nbsp;&nbsp;</u>邮政编码
								<u>&nbsp;&nbsp;${c.merHukoupost }&nbsp;&nbsp;</u>
							</td>
						</tr>
						<tr>
							<td>
								联系人姓名
								<u>&nbsp;&nbsp;${c.merContactname }&nbsp;&nbsp;</u>关系
								<u>&nbsp;&nbsp;${c.merRelationship }&nbsp;&nbsp;</u>地址
								<u>&nbsp;&nbsp;${c.merContactaddr }&nbsp;&nbsp;</u>
							</td>
						</tr>
						<tr>
							<td>
								入院日期
								<u>&nbsp;&nbsp;${c.merIntime }&nbsp;&nbsp;</u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								&nbsp;&nbsp;入院科别
								<u>&nbsp;&nbsp;${c.merIndept }&nbsp;&nbsp;</u>病室
								<u>&nbsp;&nbsp;${c.merInward }&nbsp;&nbsp;</u>转科科别
								<u>&nbsp;&nbsp;${c.merTurndept }&nbsp;&nbsp;</u>
							</td>
						</tr>
						<tr>
							<td>
								出院日期
								<u>&nbsp;&nbsp;${c.merOuttime }&nbsp;&nbsp;</u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								&nbsp;&nbsp;出院科别
								<u>&nbsp;&nbsp;${c.merOutdept }&nbsp;&nbsp;</u>病室
								<u>&nbsp;&nbsp;${c.merOutward }&nbsp;&nbsp;</u>实际住院
								<u>&nbsp;&nbsp;${c.merIndays }&nbsp;&nbsp;</u>天
							</td>
						</tr>
						<tr>
							<td>
								门 (急) 诊诊断
								<u>&nbsp;&nbsp;${c.merClinicdiag }&nbsp;&nbsp;</u>入院时情况
								<u id="ins" name="${c.merInstatus }"></u>
							</td>
						</tr>
						<tr>
							<td>
								入院诊断
								<u>&nbsp;&nbsp;${c.merIndiag }&nbsp;&nbsp;</u>入院后确诊日期
								<u>&nbsp;&nbsp;${c.merIndiagdate }&nbsp;&nbsp;</u>
							</td>
						</tr>
					</table>
					<table class="border2">
						<tr align="center">
							<td>出院诊断</td>
							<td>出院情况</td>
							<td>诊断编码</td>
						</tr>
						<!-- 查询当前病人(出院诊断表)信息开始 -->
						<c:if test="${!empty disList}">
							<c:forEach items="${disList}" var="s">
								<tr align="center">
									<td align="left">
										<c:choose>
											<c:when test="${s.ddDiagtype=='1'}">&nbsp;&nbsp;&nbsp;&nbsp;主要诊断:</c:when>
											<c:when test="${s.ddDiagtype=='0'}">&nbsp;&nbsp;&nbsp;&nbsp;其他诊断:</c:when>
											<c:otherwise>未做说明</c:otherwise>
										</c:choose>
										${s.ddDisdiag }
									</td>
									<td>${s.ddOutstatus }</td>
									<td>${s.ddDiseasecode }</td>
								</tr>
							</c:forEach>
						</c:if>
						<tr>
							<td>&nbsp;</td>
							<td></td>
							<td></td>
						</tr>
						<!-- 查询当前病人(出院诊断表)信息开结束-->
						<tr>
							<td colspan="3">
								出院编码 :001 治愈 002 好转 003 未愈 004 恶化 005 死亡 006 自动出院 007 转院治疗 008
								转家庭病床 009 未治
							</td>
						</tr>
					</table>
					<table class="border2">
						<tr>
							<td>病理诊断
								<u>&nbsp;&nbsp;${c.merPathological }&nbsp;&nbsp;</u>
							</td>
						</tr>
						<tr>
							<td>损伤和中毒的外部原因
								<u>&nbsp;&nbsp;${c.merDemagecause }&nbsp;&nbsp;</u>
							</td>
						</tr>
						<tr>
							<td>是否过敏:
								<u id="all" name="${c.merAllergic }"></u>
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								药物过敏:
								<u>&nbsp;&nbsp;${c.merAllergicname }&nbsp;&nbsp;</u>
							</td>
						</tr>
						<tr>
							<td>
								诊断符合情况 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;门诊与出院
								<u id="clnicout" name="${c.merClnicout }"></u> 入院与出院
								<u id="inout" name="${c.merInout }"></u> 术前与术后
								<u id="beforafter" name="${c.merBeforafter }"></u> 临床与病理
								<u id="clinicpath" name="${c.merClinicpath }"></u>
							</td>
						</tr>
						<tr>
							<td>
								&nbsp;&nbsp;&nbsp;
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;放射与病理
								<u id="rediopath" name="${c.merRadiopath }"></u>
								&nbsp;&nbsp;&nbsp;抢救
								<u>&nbsp;&nbsp;${c.merSafenum }&nbsp;&nbsp;</u>次 成功
								<u>&nbsp;&nbsp;${c.merSuccnum }&nbsp;&nbsp;</u>次
							</td>
						</tr>
						<tr>
							<td>
								&nbsp;科主任
								<u>&nbsp;&nbsp;${c.merDirector }&nbsp;&nbsp;</u>主 (副)
								<u>&nbsp;&nbsp;${c.merChief }&nbsp;&nbsp;</u>主冶医师
								<u>&nbsp;&nbsp;${c.merAttend }&nbsp;&nbsp;</u>住院医师
								<u>&nbsp;&nbsp;${c.merResident }&nbsp;&nbsp;</u>
							</td>
						</tr>
						<tr>
							<td>
								&nbsp;进修医师
								<u>&nbsp;&nbsp;${c.merStudaydoc }&nbsp;&nbsp;</u> 研究生实习医师
								<u>&nbsp;&nbsp;${c.merIntern }&nbsp;&nbsp;</u>编码员
								<u>&nbsp;&nbsp;${c.merCoder }&nbsp;&nbsp;</u>
							</td>
						</tr>
						<tr>
							<td>
								&nbsp;病案质量
								<u id="qualty" name="${c.merQualty }"></u> &nbsp;&nbsp;质控医师
								<u>&nbsp;&nbsp;${c.merQcdoc }&nbsp;&nbsp;</u> 质控护士
								<u>&nbsp;&nbsp;${c.merQcnur }&nbsp;&nbsp;</u>日期
								<u>&nbsp;&nbsp;${c.merQcdate }&nbsp;&nbsp;</u>
							</td>
						</tr>

						<!-- 查询当前病人(手术记录表)信息开始 -->
					</table>
					<table class="border2">
						<tr align="center">
							<td rowspan="2">手术操作编码</td>
							<td rowspan="2">手术操作日期</td>
							<td rowspan="2">手术操作名称</td>
							<td colspan="3">手术操作医师</td>
							<td rowspan="2">麻醉方式</td>
							<td rowspan="2">切口愈合等级</td>
							<td rowspan="2">麻醉医师</td>
						</tr>
						<tr align="center">
							<td>术者</td>
							<td>Ⅰ助</td>
							<td>Ⅱ助</td>
						</tr>
						<c:if test="${!empty oplist}">
							<c:forEach items="${oplist}" var="o">
								<tr align="center">
									<td>${o.opCode }</td>
									<td>${o.opDate }</td>
									<td>${o.opName }</td>
									<td>${o.opPerson }</td>
									<td>${o.opAide1 }</td>
									<td>${o.opAide2 }</td>
									<td>${o.opAnesmode }</td>
									<td>${o.opHeallevel }</td>
									<td>${o.opAnes }</td>
								</tr>
							</c:forEach>
						</c:if>

						<tr>
							<td>&nbsp;</td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
						</tr>
						<tr>
							<td colspan="9">
								麻醉方式编码说明 :1 局麻 2 硬膜外 3 颈丛 4 臂丛 5 骶麻 6 基础 7 腰麻 8 针麻 9 吸入全麻 10
								静脉全麻
							</td>
						</tr>
					</table>
					<!-- 查询当前病人(手术记录表)信息结束 -->
					<table>
						<tr>
							<td>
								住院费用总计 (元)
								<u>&nbsp;&nbsp;${c.merIncount }&nbsp;&nbsp;</u> 床费
								<u>&nbsp;&nbsp;${c.merBedbill }&nbsp;&nbsp;</u> 护理费
								<u>&nbsp;&nbsp;${c.merNursecost }&nbsp;&nbsp;</u> 西药
								<u>&nbsp;&nbsp;${c.merWestern }&nbsp;&nbsp;</u> 中成药
								<u>&nbsp;&nbsp;${c.merChinese}&nbsp;&nbsp;</u> 中草药
								<u>&nbsp;&nbsp;${c.merHerb }&nbsp;&nbsp;</u>
							</td>
						</tr>
						<tr>
							<td>
								放射
								<u>&nbsp;&nbsp;${c.merRadiocost }&nbsp;&nbsp;</u> 化验
								<u>&nbsp;&nbsp;${c.merTestcost }&nbsp;&nbsp;</u> 输氧
								<u>&nbsp;&nbsp;${c.merOxycost}&nbsp;&nbsp;</u> 输血
								<u>&nbsp;&nbsp;${c.merBloodcost }&nbsp;&nbsp;</u> 诊疗
								<u>&nbsp;&nbsp;${c.merDiagcost }&nbsp;&nbsp;</u> 手术
								<u>&nbsp;&nbsp;${c.merOperatcost }&nbsp;&nbsp;</u> 接生
								<u>&nbsp;&nbsp;${c.merDeliverycost }&nbsp;&nbsp;</u>
							</td>
						</tr>
						<tr>
							<td>
								检查费
								<u>&nbsp;&nbsp;${c.merCheckcost }&nbsp;&nbsp;</u>麻醉费
								<u>&nbsp;&nbsp;${c.merDrugcost }&nbsp;&nbsp;</u>婴儿费
								<u>&nbsp;&nbsp;${c.merInfantcost }&nbsp;&nbsp;</u>陪床费
								<u>&nbsp;&nbsp;${c.merLacost }&nbsp;&nbsp;</u>其它
								<u>&nbsp;&nbsp;${c.merOther }&nbsp;&nbsp;</u> 尸检
								<u id="autopsy" name="${c.merAutopsy }"></u>
							</td>
						</tr>
						<tr>
							<td>
								血型&nbsp;&nbsp;
								<u id="bloodtype" name="${c.merBloodtype }"></u> Rh
								<u id="rh" name="${c.merRh }"></u> 输血反应&nbsp;&nbsp;
								<u id="imr" name="${c.merImportresp }"></u> 输血量
								<u>&nbsp;&nbsp;${c.merImportcount }&nbsp;&nbsp;</u>
								输血品种&nbsp;&nbsp;
								<u id="imt" name="${c.merImporttype }"></u>
							</td>
						</tr>
					</table>
				</div>
				<div style="margin: 10px 0" align="left">
					&nbsp;&nbsp;&nbsp;&nbsp;说明:医疗付款方式: 1.社会基本医疗保险(补充保险,特大病保险); 2.商业保险;
					3 . 自费医疗; 4. 公费医疗 ;5. 大病统筹; 6.其它. 住院费用总计:
					凡可由计算机提供住院费用清单的,住院首页中可不填.
				</div>
			</c:forEach>
		</div>
		
		<c:if test="${!empty page.list}">
			<div align="right" style="width: 80%; margin: 0 auto">
				<div class="row-fluid">
					<div class="span12">
						<%@include file="/WEB-INF/jsp/system/page.jsp"%>
					</div>
				</div>
			</div>
		</c:if>
		
		<c:if test="${empty page.list}">
			<div class="text-center">
				<H3>未查找到数据!</H3>
			</div>
		</c:if>
		
	</body>
</html>
