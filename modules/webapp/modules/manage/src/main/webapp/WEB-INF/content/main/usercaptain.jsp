<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib tagdir="/WEB-INF/tags/simpletable" prefix="simpletable"%>
<%@ include file="/commons/taglibs.jsp" %>

<rapid:override name="head">
    <title>玩家武将列表</title>
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
	<script type="text/javascript" src="${ctx}/scripts/My97DatePicker/WdatePicker.js"></script>

	<script type="text/javascript" >
	    
	    var deftip="请输入玩家";
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
		    window.open(url,'','width=600,height=400,resizable=1,scrollbars=1');
		}
		function trim(str)
		{
		    return str.replace(/&nbsp;/g,"").replace(/(^\s*)|(\s*$)/g,"");
		}
		function moreQuery()
		{
		    $('#checkInput').val('');
		    $('#owner').val('');
		    $('#morequery').val('1');
		    $('#queryForm').submit();
		}
		function fastQuery()
		{
		    $('#morequery').val('0');
		    $('#queryForm').submit();
		}
		function selectPlayer()
	    {
	        url="${ctx}/main/player!select.action";
		    window.open(url,'','width=900,height=450,resizable=1,scrollbars=1');
	    }
	    function assignPlayer(playerId,playerName)
	    {
	        $("#owner").val(playerId);
	        $("#checkInput").val(playerName);
	    }
	    function fastInput()
	    {
	        if($("#checkInput").val()==deftip)
	        {
	            var aa=" ";
	            $("#checkInput").val(aa);
	        }
	        selectPlayer();
	    }
	    function refreshLv(id,lv)
	    {
	        $("#lv"+id).html(lv);
	    }
	    function refreshState(id,state)
	    {
	       $("#state"+id).html(state);
	    }
	 </script>
</rapid:override> 

<rapid:override name="content">
  <div class="demo_rtdbq02 out_form_div" id="mytest">
	<form onSubmit="resetValue();" id="queryForm" name="queryForm" action="${ctx}/main/user-captain.action" method="post" style="display: inline;">
	  	<!--最上行区域  -->
	  	<div class="tf_main_bt">
	  	    <!--上行左边菜单区域  -->
			<div class="tf_main_bt_lf">
			
			</div>
			<!--上行右边查询区域  -->
			<div class="tf_main_bt_rt" >
			    <input type="hidden" id="owner" value="<s:property value='userCaptainQuery.owner'/>" name="userCaptainQuery.owner" />
				<input class="bt_rt_input" id="checkInput" value="<s:property value='fastQueryActStr'/>" readonly="readonly"
				       onclick="fastInput();"  onblur="javascript:if(value==''){value=deftip;};"  
				       name="fastQueryActStr" type="text" />
				<input class="cx" type="image" alt="快速查询" src="${ctx}/images/demo_main_bj03.gif" onclick="fastQuery();return false;"/>
				<a href="#"  onclick="$('#div1').show();return false;" style="margin-top:2px;"><span>更多查询</span></a>
			</div>
			<div id="div1" class="tf_inquire" style="display:none;">
			    <div class="tf_inquire_nr">
			        <input type="hidden" id="morequery" name="morequery" value="<s:property value='morequery'/>" />
			        <table width="100%" border="0" cellspacing="0" cellpadding="0" style="line-height:35px;">
			            <tr>
			                <td><span class="wz">武将</span></td>
			                <td style="text-align:left;"><span class="select_pre_span"><select id="code" name="userCaptainQuery.code">
			                                                 <option value="0">请选择</option>
			                                                 <s:iterator value="captainList">
			                                                     <option value="${code}" <s:if test="code == userCaptainQuery.code">selected="selected"</s:if> >${capname}</option>
			                                                 </s:iterator>
			                   							 </select>
			                   							 </span> 
			                </td>
			                <td><span class="wz">类型</span></td>
			                <td style="text-align:left;"><span class="select_pre_span"><select id="capType" name="userCaptainQuery.capType">
			                                                 <option value="0">请选择</option>
			                                                 <option value="1" <s:if test="userCaptainQuery.capType==1">selected="selected"</s:if> >主公</option>
			                                                 <option value="2" <s:if test="userCaptainQuery.capType==2">selected="selected"</s:if> >普通</option>
			                                                 <option value="3" <s:if test="userCaptainQuery.capType==3">selected="selected"</s:if> >兵种</option>
			                                                 <option value="4" <s:if test="userCaptainQuery.capType==4">selected="selected"</s:if> >FD</option>
			                   							 </select>
			                   							 </span>
                            </td>
			                <td><span class="wz">等级</span></td>
			                <td style="text-align:left;"><input type="text" id="lvfrom" name="userCaptainQuery.lvfrom" style=" width:81px;" value="<s:property value='userCaptainQuery.lvfrom'/>" >
			                    <span class="wz" style="text-align:center;width:25px;padding-left:3px;">至</span>
			                    <input type="text" id="lvto" name="userCaptainQuery.lvto" style=" width:81px;" value="<s:property value='userCaptainQuery.lvto'/>" ></td>
			            </tr>
			            <tr>
			                <td colspan="6" style="text-align:right;">
			                    <a href="javascript:;" id="querymore"  style="margin-right: 3px;"onclick="moreQuery()" ><span>查&nbsp;询</span></a>
			                </td>
			            </tr>
			        </table>
			    </div>
			    <div class="tf_inquire_gb"><p onclick="$('#div1').hide();"><img src='${ctx}/images/demo_inquire.gif' /></p></div>
			</div>
		</div>
		<!-- 内容table列表 -->
		<div id="tablecontent">
	  	<table width="100%" id="all" border="0" align="center" cellPadding="0"  cellSpacing="0">
			<tr class="tf_table_th">
				<td class="tdleft" width=""><input type="checkbox" name="allbox2" value="0" onclick="setAllCheckboxState('items',this.checked)" class="demo_qx">&nbsp;全选</td>
				<td width="" >名称</td>
				<td width="">所属玩家</td>
				<td width="" >类型</td>
				<td width="" >职业</td>
				<td width="" >类别</td>
				<td width="" >等级</td>
				<td width="" >状态</td>
				<td style="width:15%;">创建时间</td>
				<td width="">操作</td>
			 </tr>
			 <c:forEach items="${page.result}" var="item" varStatus="status" >
			 
			  <tr class="demo_table02_lb ${status.count % 2 == 0 ? 'demo_table02_lb02' : ''}">
				<td class="tdleft" align="left" ><input type="checkbox" name="items" value="${item.id}"></td>
				
				<td>
				<div style="width:70px;overflow:hidden;text-overflow:ellipsis;white-space:nowrap;" title="${item.capname}">
				<c:out value='${item.capname}'/>&nbsp;
				</div>
				</td>
				
				<td class="playername">
				<div style="width:70px;overflow:hidden;text-overflow:ellipsis;white-space:nowrap;" title="${item.playerName}">
				<c:out value='${item.playerName}'/>&nbsp;
				</div>
				</td>
				
				<td><c:choose><c:when test="${item.capType==1}">主公</c:when><c:when test="${item.capType==2}">普通</c:when>
				              <c:when test="${item.capType==3}">兵种</c:when><c:otherwise>FD</c:otherwise></c:choose>
				</td>
				
				<td><c:choose><c:when test="${item.capjob==1}">神射</c:when><c:when test="${item.capjob==2}">神兵</c:when>
				              <c:when test="${item.capjob==3}">天将</c:when><c:when test="${item.capjob==4}">贤者</c:when><c:otherwise>偷蛋者</c:otherwise></c:choose>
				</td>
				<td><c:choose><c:when test="${item.capforce==1}">鸟</c:when><c:when test="${item.capforce==2}">猪</c:when>
				              <c:when test="${item.capforce==3}">猴</c:when><c:otherwise>猫</c:otherwise></c:choose>
				</td>
				<td id="lv${item.id}">${item.lv}</td>
				<td id="state${item.id}"><c:choose><c:when test="${item.state==0}">离队</c:when><c:otherwise>入队</c:otherwise></c:choose></td>
				<td>${item.createTimeStr}</td>
				<td>
					<a  href="javascript:;" onclick="openTo('${ctx}/main/user-captain!input.action?id=${item.id}');return false;"><span>修&nbsp;改</span></a>
					<a  href="javascript:;" onclick="openTo('${ctx}/main/user-captain!info.action?id=${item.id}');return false;"><span>详&nbsp;情</span></a>
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
