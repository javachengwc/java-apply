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
	<script type="text/javascript" src="${ctx}/scripts/My97DatePicker/WdatePicker.js"></script>

	<script type="text/javascript" >
	    
	    var deftip="请输入账号名";
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
		    window.open(url,'','width=800,height=500,location=no,resizable=1,scrollbars=1');
		}
		function trim(str)
		{
		    return str.replace(/&nbsp;/g,"").replace(/(^\s*)|(\s*$)/g,"");
		}
		function unban(id)
		{
		    var htmlObj=$.ajax({url:"${ctx}/main/player!unban.action",async:false,type:"POST",data:{"id":id}});
		    var result=htmlObj.responseText;
		    result =eval(result);
		    if(result)
		    {
		       var eleId="ban"+id;
		       var ele=document.getElementById(eleId);
		       if(ele!=null)
		       {
		 
		            ele.innerHTML="正常";
		            $(ele).siblings().last().find(".banbtn1").show();
		            $(ele).siblings().last().find(".banbtn2").hide();
		       }
		       
		    }
		    else
		    {
		        alert("解封失败!");
		    }
		}
		var pop;
		function showBanDialog(playerId,playerName)
		{
		    pop=new Popup({
		        contentType:1,
		        scrollType:'yes',
		        isReloadOnClose:false,
		        width:500,
		        height:200   
		    });
		    var url="${ctx}/main/player!banInput.action?id="+playerId+"&playerName="+playerName;
		    url=encodeURI(url);
		    pop.setContent("contentUrl",url);
		    pop.setContent("title","<font style='color:#003e51;'>封号处理</font>");
		    pop.build();
		    pop.show();
		}
		function onClose(playerId,result)
		{
		    result =eval(result);
		    if(result)
		    {
		       var eleId="ban"+playerId;
		       var ele=document.getElementById(eleId);
		       if(ele!=null)
		       {
		           ele.innerHTML="封锁";
		           $(ele).siblings().last().find(".banbtn1").hide();
		           $(ele).siblings().last().find(".banbtn2").show();
		       }
		    }
		    closePop();
		}
		function closePop()
		{
		   pop.close();
		   pop="";
		}
		function moreQuery()
		{
		    $('#checkInput').val('');
		    $('#morequery').val('1');
		    $('#queryForm').submit();
		}
		function fastQuery()
		{
		    $('#morequery').val('0');
		    $('#queryForm').submit();
		}
		
		function refreshUser(id,user)
		{
		    $("#playerName"+id).find("div").html(user.playerName);
		    $("#lv"+id).html(user.lv);
		    if(user.sex==1)
		    {
		       $("#sex"+id).html("男");
		    }else
		    {
		       $("#sex"+id).html("女");
		    }
		    if(user.force==1)
		         $("#force"+id).html("鸟");
		    else if(user.force==2)
		         $("#force"+id).html("猪");
		    else if(user.force==3)
		         $("#force"+id).html("猴");
		    else
		         $("#force"+id).html("猫");      
		         
		    if(user.job==1)
		         $("#job"+id).html("神射");
		    else if(user.job==2)
		         $("#job"+id).html("神兵");
		    else if(user.job==3)
		         $("#job"+id).html("天将");
		    else if(user.job==4)
		         $("#job"+id).html("贤者");
		    else
		         $("#job"+id).html("偷蛋者");      
		        
		}
	 </script>
</rapid:override> 

<rapid:override name="content">
  <div class="demo_rtdbq02 out_form_div" id="mytest">
	<form onSubmit="resetValue();" id="queryForm" name="queryForm" action="${ctx}/main/player.action" method="post" style="display: inline;">
	  	<!--最上行区域  -->
	  	<div class="tf_main_bt">
	  	    <!--上行左边菜单区域  -->
			<div class="tf_main_bt_lf">
			
			</div>
			<!--上行右边查询区域  -->
			<div class="tf_main_bt_rt" >
				<input class="bt_rt_input" id="checkInput" value="<s:property value='fastQueryActStr'/>" onfocus="if(value==deftip)this.value='';"  onblur="javascript:if(value==''){value=deftip;};"  
				       name="fastQueryActStr" type="text" />
				<input class="cx" type="image" alt="快速查询" src="${ctx}/images/demo_main_bj03.gif"  onclick="fastQuery();return false;"/>
				<a href="#"  onclick="$('#div1').show();return false;" style="margin-top:2px;"><span>更多查询</span></a>
			</div>
			<div id="div1" class="tf_inquire" style="display:none;">
			    <div class="tf_inquire_nr">
			        <input type="hidden" id="morequery" name="morequery" value="<s:property value='morequery'/>" />
			        <table width="100%" border="0" cellspacing="0" cellpadding="0" style="line-height:35px;">
			            <tr>
			                <td><span class="wz">ID</span></td>
			                <td style="text-align:left;"><input type="text" id="id" name="playerQuery.id" value="<s:property value='playerQuery.id'/>"></td>
			                <td><span class="wz">账号</span></td>
			                <td style="text-align:left;"><input  type="text" id="account" name="playerQuery.account" value="<s:property value='playerQuery.account'/>" ></td>
			                <td><span class="wz">名称</span></td>
			                <td style="text-align:left;"><input  type="text" id="playerName" name="playerQuery.playerName" value="<s:property value='playerQuery.playerName'/>" ></td>
			            </tr>
			            <tr>
			                <td><span class="wz">类别</span></td>
			                <td style="text-align:left;"><span class="select_pre_span"><select id="force" name="playerQuery.force">
			                                                 <option value="0">请选择</option> 
			                                                 <option value="1" <s:if test="playerQuery.force==1">selected="selected"</s:if> >鸟</option>
			                                                 <option value="2" <s:if test="playerQuery.force==2">selected="selected"</s:if> >猪</option>
			                                                 <option value="3" <s:if test="playerQuery.force==3">selected="selected"</s:if> >猴</option>
			                                                 <option value="4" <s:if test="playerQuery.force==4">selected="selected"</s:if> >猫</option>
			                   							 </select>
			                   							 </span> 
			                </td>
			                <td><span class="wz">职业</span></td>
			                <td style="text-align:left;"><span class="select_pre_span"><select id="job" name="playerQuery.job">
			                                                 <option value="0">请选择</option>
			                                                 <option value="1" <s:if test="playerQuery.job==1">selected="selected"</s:if> >神射</option>
			                                                 <option value="2" <s:if test="playerQuery.job==2">selected="selected"</s:if> >神兵</option>
			                                                 <option value="3" <s:if test="playerQuery.job==3">selected="selected"</s:if> >天将</option>
			                                                 <option value="4" <s:if test="playerQuery.job==4">selected="selected"</s:if> >贤者</option>
			                                                 <option value="5" <s:if test="playerQuery.job==5">selected="selected"</s:if> >偷蛋者</option>
			                   							 </select>
			                   							 </span>
                            </td>
			                <td><span class="wz">状态</span></td>
			                <td style="text-align:left;"><span class="select_pre_span"><select id="banned" name="playerQuery.banned">
			                                                 <option value="-1">请选择</option>
			                                                 <option value="0" <s:if test="playerQuery.banned==0">selected="selected"</s:if> >正常</option>
			                                                 <option value="1" <s:if test="playerQuery.banned==1">selected="selected"</s:if> >封锁</option>
			                                             </select>
			                                             </span>    
			                </td>
			            </tr>
			            <tr>
			                <td><span class="wz">等级</span></td>
			                <td style="text-align:left;"><input type="text" id="playerQuery.lvfrom" name="playerQuery.lvfrom" style=" width:81px;" value="<s:property value='playerQuery.lvfrom'/>" >
			                    <span class="wz" style="text-align:center;width:25px;padding-left:3px;">至</span>
			                    <input type="text" id="lvto" name="playerQuery.lvto" style=" width:81px;" value="<s:property value='playerQuery.lvto'/>" ></td>
			                <td><span class="wz">登录时间</span></td>
			                <td style="text-align:left;"><input type="text" id="logintimefrom" name="playerQuery.logintimefrom" value="<s:property value='playerQuery.logintimefromStr'/>"  style=" width:81px;" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})">
			                    <span class="wz" style="text-align:center;width:25px;padding-left:3px;">至</span>
			                    <input type="text" id="logintimeto" name="playerQuery.logintimeto" style=" width:81px;" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" value="<s:property value='playerQuery.logintimetoStr'/>" >
			                </td>
			                <td></td>
			                <td style="text-align:left;"></td>
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
				<td width="" >状态</td>
				<td style="width:15%;">登录时间</td>
				<td width="">操作</td>
			 </tr>
			 <c:forEach items="${page.result}" var="item" varStatus="status" >
			 
			  <tr class="demo_table02_lb ${status.count % 2 == 0 ? 'demo_table02_lb02' : ''}">
				<td class="tdleft" align="left" ><input type="checkbox" name="items" value="${item.id}"></td>
				
				<td>
				<div style="width:70px;overflow:hidden;text-overflow:ellipsis;white-space:nowrap;" title="${item.account}">
				<c:out value='${item.account}'/>&nbsp;
				</div>
				</td>
				
				<td class="playername" id="playerName${item.id}">
				<div style="width:70px;overflow:hidden;text-overflow:ellipsis;white-space:nowrap;" title="${item.playerName}">
				<c:out value='${item.playerName}'/>&nbsp;
				</div>
				</td>
				
				<td id="sex${item.id}"><c:choose><c:when test="${item.sex==1}">男</c:when><c:otherwise>女</c:otherwise></c:choose></td>
				
				<td id="force${item.id}"><c:choose><c:when test="${item.force==1}">鸟</c:when><c:when test="${item.force==2}">猪</c:when>
				              <c:when test="${item.force==3}">猴</c:when><c:otherwise>猫</c:otherwise></c:choose>
				</td>
				<td id="job${item.id}"><c:choose><c:when test="${item.job==1}">神射</c:when><c:when test="${item.job==2}">神兵</c:when>
				              <c:when test="${item.job==3}">天将</c:when><c:when test="${item.job==4}">贤者</c:when><c:otherwise>偷蛋者</c:otherwise></c:choose>
				</td>
				<td id="lv${item.id}">${item.lv}</td>
				<td id="ban${item.id}"><c:choose><c:when test="${item.banned==0}">正常</c:when><c:otherwise>锁定</c:otherwise></c:choose></td>
				<td>${item.lastLoginTimeStr}</td>
				<td>
					<a  href="javascript:;" onclick="openTo('${ctx}/main/player!input.action?id=${item.id}');return false;"><span>修&nbsp;改</span></a>
					<a  href="javascript:;" onclick="openTo('${ctx}/main/player!info.action?id=${item.id}');return false;"><span>详&nbsp;情</span></a>
					<a style="display:<c:if test="${item.banned==0}">inline;</c:if><c:if test="${item.banned==1}">none;</c:if>" href="javascript:;" onclick="showBanDialog('${item.id}','${item.playerName}');return false;"><span>封&nbsp;号 </span></a>
				    <a style="display:<c:if test="${item.banned==0}">none;</c:if><c:if test="${item.banned==1}">inline;</c:if>" href="javascript:;" onclick="unban('${item.id}');return false;"><span>解&nbsp;封 </span></a>
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
