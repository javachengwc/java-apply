<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib tagdir="/WEB-INF/tags/simpletable" prefix="simpletable"%>
<%@ include file="/commons/taglibs.jsp" %>

<rapid:override name="head">
    <title>玩家列表</title>
	<link href="${ctx}/styles/index_css.css"  type="text/css" rel="stylesheet"/>
	<link href="${ctx}/widgets/simpletable/simpletable.css" type="text/css" rel="stylesheet"/>
	<link href="${ctx}/styles/list.css" type="text/css" rel="stylesheet"/>
	<script src="${ctx}/widgets/simpletable/simpletable.js" type="text/javascript"></script>
	<script src="${ctx}/scripts/jquery.js" type="text/javascript"></script>
    <script src="${ctx}/scripts/jquery.cookie.js" type="text/javascript"></script>
    <script type="text/javascript" src="${ctx}/scripts/jquery.pngFix.js"></script>
	<script type="text/javascript" src="${ctx}/scripts/jquery.pngFix.pack.js"></script>
	<script type="text/javascript" src="${ctx}/scripts/common/list.js"></script>
	<script type="text/javascript" src="${ctx}/scripts/popup.js"></script>
	<style>
	   
	</style>
	<script type="text/javascript" >
	    
	    var deftip="请输入账号名";
	    window.onload=initValue;				
							
		$(document).ready(function() {
		    $(document).pngFix();
			// 分页需要依赖的初始化动作
			window.simpleTable = new SimpleTable('queryForm','${page.thisPageNumber}','${page.pageSize}','${pageRequest.sortColumns}');
			jQuery(".demo_table02_lb").mouseover(function() {jQuery(this).addClass("demo_table02_lb03");}).mouseout(function() {jQuery(this).removeClass("demo_table02_lb03");});
		});
		
		function openTo(url)
		{
		    url=encodeURI(url);
		    window.open(url,'','width=800,height=600,resizable=1,scrollbars=1');
		}
		function trim(str)
		{
		    return str.replace(/&nbsp;/g,"").replace(/(^\s*)|(\s*$)/g,"");
		}
		
		
		var checkElement;
		function checkAll() { 
			var check = document.getElementsByTagName("input");
			var index1 = 0;
			var checkArr = new Array();
			for(var m = 0;m<check.length;m++){
				if(check[m].type == "checkbox"){
					checkArr[index1] = check[m];
					index1++;
  				}
 			}
			if(checkArr[0].checked== true){
				for(var i = 1;i<checkArr.length;i++){
					checkArr[i].checked = true;
				}
		 	}else{
		  		for(var i = 1;i<checkArr.length;i++){
					checkArr[i].checked = false;
				}
		    }	
	    }
	    function getCheckCount()
	    {
	    	var checkEle;
	    	var checkCount=0;
	    	var check = document.getElementsByTagName("input");
			var index1 = 0;
			var checkArr = new Array();
			for(var m = 0;m<check.length;m++){
				if(check[m].type == "checkbox"){
					checkArr[index1] = check[m];
					index1++;
  				}
 			}
			for(var i = 1;i<checkArr.length;i++){
				if(checkArr[i].checked == true)
				{
				   checkEle=checkArr[i];
				   checkCount+=1;
				}
			}
			if(checkCount==1)
				checkElement=checkEle;
			return checkCount;
		}
	    
		function selectConfirm()
		{
		    var count=getCheckCount();
			if(count<=0)
				alert("必须选中一个玩家!");
			if(count>1)
				alert("只能选择一个玩家!");
			if(count==1)
		    {
		        var ele=checkElement;
		        var playerId=ele.value;
		        var playerName=$(ele).parent().parent().find(".playername").find("div").html();
		            playerName=trim(playerName);
		        window.opener.assignPlayer(playerId,playerName);
		        window.close();
		    }
		}
	 </script>
</rapid:override> 

<rapid:override name="content">
  <div class="demo_rtdbq02 out_form_div" id="mytest">
	<form onSubmit="resetValue();" id="queryForm" name="queryForm" action="${ctx}/main/player!select.action" method="post" style="display: inline;">
	  	<!--最上行区域  -->
	  	<div class="tf_main_bt">
	  	    <!--上行左边菜单区域  -->
			<div class="tf_main_bt_lf">
			    <a href="javascript:;" onclick="selectConfirm();return false;""><span><img src="${ctx}/images/demo_main_tb.png" />确定</span></a>
			</div>
			<!--上行右边查询区域  -->
			<div class="tf_main_bt_rt" >
				<input class="bt_rt_input" id="checkInput" value="${query.account}" onfocus="if(value==deftip)this.value='';"  onblur="javascript:if(value==''){value=deftip;};"  
				       name="user.account" type="text" />
				<input class="cx" type="image" alt="快速查询" src="${ctx}/images/demo_main_bj03.gif" />
			</div>
		</div>
		<!-- 内容table列表 -->
		<div id="tablecontent" >
	  	<table width="100%" id="all" border="0" align="center" cellPadding="0"  cellSpacing="0">
			<tr class="tf_table_th">
				<td class="tdleft" width=""><input type="checkbox" name="allbox2" value="0" onclick="setAllCheckboxState('items',this.checked)" class="demo_qx">&nbsp;全选</td>
				<td width="" >账号</td>
				<td width="" >名称</td>
				<td width="" >性别</td>
				<td width="" >类别</td>
				<td width="" >职业</td>
				<td width="" >等级</td>
			 </tr>
			 <c:forEach items="${page.result}" var="item" varStatus="status" >
			 
			  <tr class="demo_table02_lb ${status.count % 2 == 0 ? 'demo_table02_lb02' : ''}">
				<td class="tdleft" align="left" ><input type="checkbox" name="items" value="${item.id}"></td>
				
				<td>
				<div style="width:70px;overflow:hidden;text-overflow:ellipsis;white-space:nowrap;" title="${item.account}">
				<c:out value='${item.account}'/>&nbsp;
				</div>
				</td>
				
				<td class="playername">
				<div style="width:70px;overflow:hidden;text-overflow:ellipsis;white-space:nowrap;" title="${item.playerName}">
				<c:out value='${item.playerName}'/>&nbsp;
				</div>
				</td>
				
				<td><c:choose><c:when test="${item.sex==1}">男</c:when><c:otherwise>女</c:otherwise></c:choose></td>
				
				<td><c:choose><c:when test="${item.force==1}">鸟</c:when><c:when test="${item.force==2}">猪</c:when>
				              <c:when test="${item.force==3}">猴</c:when><c:otherwise>猫</c:otherwise></c:choose>
				</td>
				<td><c:choose><c:when test="${item.job==1}">神射</c:when><c:when test="${item.job==2}">神兵</c:when>
				              <c:when test="${item.job==3}">天将</c:when><c:when test="${item.job==4}">贤者</c:when><c:otherwise>偷蛋者</c:otherwise></c:choose>
				</td>
				<td>${item.lv}</td>
		  	  </c:forEach>		  
		</table>
        <simpletable:pageToolbar page="${page}"></simpletable:pageToolbar>  
		</div>
	</form>
</div>
</rapid:override>

<%@ include file="base.jsp" %>
