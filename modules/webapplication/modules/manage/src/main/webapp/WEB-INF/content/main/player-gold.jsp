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
		    //location=no;
		    window.open(url,'','width=800,height=600,resizable=1,scrollbars=1');
		}
		function trim(str)
		{
		    return str.replace(/&nbsp;/g,"").replace(/(^\s*)|(\s*$)/g,"");
		}
		
		var pop;
		//赠款
		function showPresentDialog(playerId)
		{
		    pop=new Popup({
		        contentType:1,
		        scrollType:'yes',
		        isReloadOnClose:false,
		        width:500,
		        height:200   
		    });
		    var url="${ctx}/main/player!presentGoldInput.action?id="+playerId;
		    url=encodeURI(url);
		    pop.setContent("contentUrl",url);
		    pop.setContent("title","<font style='color:#003e51;'>赠款操作</font>");
		    pop.build();
		    pop.show();
		}
		//扣款
		function showDeductDialog(playerId)
		{
		    pop=new Popup({
		        contentType:1,
		        scrollType:'yes',
		        isReloadOnClose:false,
		        width:500,
		        height:200   
		    });
		    var url="${ctx}/main/player!deductGoldInput.action?id="+playerId;
		    url=encodeURI(url);
		    pop.setContent("contentUrl",url);
		    pop.setContent("title","<font style='color:#003e51;'>扣款操作</font>");
		    pop.build();
		    pop.show();
		}
		
		function onClose(playerId,result)
		{
		    result =eval(result);
		    if(result.result)
		    {
		      $("#gold"+playerId).html(result.gold);
		      $("#rmb"+playerId).html(result.rmb);
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
		    if($("#checkInput").val()== deftip)
		        $("#checkInput").val("");
		    $('#queryForm').submit();
		}
	 </script>
</rapid:override> 

<rapid:override name="content">
  <div class="demo_rtdbq02 out_form_div" id="mytest">
	<form onSubmit="resetValue();" id="queryForm" name="queryForm" action="${ctx}/main/player.action" method="post" style="display: inline;">
	    <input type="hidden" id="viewtype" name="viewtype" value="1" />
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
				<a href="#" onclick="$('#div1').show();return false;" style="margin-top:2px;"><span>更多查询</span></a>
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
			                    <a href="javascript:;" id="querymore" style="margin-right: 3px;"onclick="moreQuery()" ><span>查&nbsp;询</span></a>
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
				<td width="" >账号</td>
				<td width="" >名称</td>
				<td width="" >等级</td>
				<td width="" >金币</td>
				<td width="" >元宝</td>
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
				
				<td class="playername">
				<div style="width:70px;overflow:hidden;text-overflow:ellipsis;white-space:nowrap;" title="${item.playerName}">
				<c:out value='${item.playerName}'/>&nbsp;
				</div>
				</td>
				
				<td>${item.lv}</td>
				<td id="gold${item.id}">${item.gold}</td>
				<td id="rmb${item.id}" >${item.rmb}</td>
				<td>
					<a href="javascript:;" onclick="showPresentDialog('${item.id}');return false;"><span>赠&nbsp;款</span></a>
					<a href="javascript:;" onclick="showDeductDialog('${item.id}');return false;"><span>扣&nbsp;款</span></a>
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
