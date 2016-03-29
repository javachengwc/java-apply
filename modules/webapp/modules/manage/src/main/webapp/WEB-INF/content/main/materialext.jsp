<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib tagdir="/WEB-INF/tags/simpletable" prefix="simpletable"%>
<%@ include file="/commons/taglibs.jsp" %>

<rapid:override name="head">
    <title>物质列表</title>
	<link href="${ctx}/styles/index_css.css"  type="text/css" rel="stylesheet"/>
	<link href="${ctx}/widgets/simpletable/simpletable.css" type="text/css" rel="stylesheet"/>
	<link href="${ctx}/styles/list.css" type="text/css" rel="stylesheet"/>
	<script src="${ctx}/widgets/simpletable/simpletable.js" type="text/javascript"></script>
	<script src="${ctx}/scripts/jquery.js" type="text/javascript"></script>
    <script src="${ctx}/scripts/jquery.cookie.js" type="text/javascript"></script> 
    <script type="text/javascript" src="${ctx}/scripts/jquery.pngFix.js"></script>
	<script type="text/javascript" src="${ctx}/scripts/jquery.pngFix.pack.js"></script>
	<script type="text/javascript" src="${ctx}/scripts/common/list.js"></script>

	<script type="text/javascript" >
	    
	    var deftip="请输入物资名";
	    window.onload=initOnload;				
							
		$(document).ready(function() {
		    $(document).pngFix();
			// 分页需要依赖的初始化动作
			window.simpleTable = new SimpleTable('queryForm','${page.thisPageNumber}','${page.pageSize}','${pageRequest.sortColumns}');
			jQuery(".demo_table02_lb").mouseover(function() {jQuery(this).addClass("demo_table02_lb03");}).mouseout(function() {jQuery(this).removeClass("demo_table02_lb03");});
		});
		
		function openTo(url)
		{
		    url=encodeURI(url);
		    window.open(url,'','width=500,height=300,resizable=1,scrollbars=1');
		}
	 </script>
</rapid:override> 

<rapid:override name="content">
  <div class="demo_rtdbq02 out_form_div" id="mytest">
	<form onSubmit="resetValue();" id="queryForm" name="queryForm" action="${ctx}/main/material-ext.action" method="post" style="display: inline;">
	  	<!--最上行区域  -->
	  	<div class="tf_main_bt">
	  	    <!--上行左边菜单区域  -->
			<div class="tf_main_bt_lf">
			</div>
			<!--上行右边查询区域  -->
			<div class="tf_main_bt_rt" >
				<input class="bt_rt_input" id="checkInput" value="${query.name}" onfocus="if(value==deftip)this.value='';"  onblur="javascript:if(value==''){value=deftip;};"  
				       name="material.name" type="text" />
				<input class="cx" type="image" alt="快速查询" src="${ctx}/images/demo_main_bj03.gif" />
			</div>
		</div>
		<!-- 内容table列表 -->
		<div id="tablecontent">
	  	<table width="100%" id="all" border="0" align="center" cellPadding="0"  cellSpacing="0">
			<tr class="tf_table_th">
				<td class="tdleft" width=""><input type="checkbox" name="allbox2" value="0" onclick="setAllCheckboxState('items',this.checked)" class="demo_qx">&nbsp;全选</td>
				<td width="" >ID</td>
				<td width="" >类型</td>
				<td width="" >名称</td>
				<td width="" >图标</td>
				<td width="" >备注</td>
				<td width="" >操作</td>
			 </tr>
			 <c:forEach items="${page.result}" var="item" varStatus="status" >
			 
			  <tr class="demo_table02_lb ${status.count % 2 == 0 ? 'demo_table02_lb02' : ''}">
			  
				<td class="tdleft" align="left" ><input type="checkbox" name="items" value="id=${item.id}&"></td>
				
				<td><c:out value='${item.id}'/>&nbsp;</td>
				
				<td><c:out value='${item.maTypeStr}'/>&nbsp;</td>
				
				<td>
				<div  title="${item.name}">
				<c:out value='${item.name}'/>&nbsp;
				</div>
				</td>
				
				<td><c:out value='${item.icon}'/>&nbsp;</td>
				
				<td>${item.desc}</td>
				<td style="width:25%;text-align:center;">
					<a href="javascript:;" onclick="window.open('${ctx}/user/User/edit.do?id=${item.id}','','width=800,height=600,resizable=1,scrollbars=1');return false;"><span>修&nbsp;改</span></a>
					<a href="javascript:;" onclick="window.open('${ctx}/user/User/show.do?id=${item.id}','','width=800,height=600,resizable=1,scrollbars=1');return false;"><span>详&nbsp;情</span></a>
					<a href="javascript:;" onclick="openTo('${ctx}/main/material-ext!presentInput.action?id=${item.id}&combinid=${item.combinid}&name=${item.name}');return false;"><span>赠&nbsp;送</span></a>
				</td>
			  </tr>
		  	  </c:forEach>		  
		</table>
        <simpletable:pageToolbar page="${page}"></simpletable:pageToolbar>  
		</div>
	</form>
</div>
</rapid:override>

<%@ include file="base.jsp" %>
