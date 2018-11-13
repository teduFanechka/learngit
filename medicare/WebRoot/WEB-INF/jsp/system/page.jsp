<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div style="font-size: 14px;display:inline">
	当前第[${page.currentPage }]页&nbsp;
	<a href="${page.url }&pagenum=1">[首页]</a> &nbsp;
	<c:if test="${page.currentPage>1 }">
		<a href="${page.url }&pagenum=${(page.currentPage-1) }">上一页</a>&nbsp;
	</c:if>
	
	<c:forEach var="pagenum" begin="${page.startPage}" end="${page.endPage}">
			[<a href="${page.url }&pagenum=${pagenum }">${pagenum}</a>]
	</c:forEach>
	
	<c:if test="${page.currentPage<page.totalPage}">
		<a href="${page.url }&pagenum=${(page.currentPage+1)}">下一页</a>&nbsp;
	</c:if>
	<a href="${page.url }&pagenum=${page.totalPage }">[最后一页]</a> &nbsp;
	共[${page.totalPage }]页 共[${page.allRow }]条记录&nbsp; 第
	<input type="text" style="width: 35px; text-align: center;" id="pagenum">页
	<input type="button" value=" GO " onclick=goWhich(document.getElementById('pagenum'));>
	<script type="text/javascript">
		function goWhich(input){
			var pagenum=input.value;
			if(pagenum==null || pagenum==""){
				 $.messager.alert('系统提示','请输入页码!','warning');
				return;
			}
			if(!pagenum.match("\\d+")){
				$.messager.alert('系统提示','请输入数字!','warning');
				input.value="";
				return;
			}
			if(pagenum<1 || pagenum>${page.totalPage}){
				$.messager.alert('系统提示','无效的页码!','warning');
				input.value="";
				return;
			}					
			window.location.href="${page.url }&pagenum="+pagenum;
		}
	</script>
</div>