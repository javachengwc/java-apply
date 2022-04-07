<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.manage.model.rbac.ResourcePermission.OperationFlag" %>
<%@ page import="com.manage.service.rbac.AdminorHolder" %>
<%@ taglib tagdir="/WEB-INF/tags/simpletable" prefix="simpletable"%>
<%@ include file="/commons/taglibs.jsp" %>
<rapid:override name="head">
    <title>资源列表</title>
	<link href="${ctx}/styles/index_css.css"  type="text/css" rel="stylesheet"/>
	<link href="${ctx}/widgets/simpletable/simpletable.css" type="text/css" rel="stylesheet"/>
	<link href="${ctx}/styles/list.css" type="text/css" rel="stylesheet"/>
	<script src="${ctx}/widgets/simpletable/simpletable.js" type="text/javascript"></script>
	<script src="${ctx}/scripts/jquery.js" type="text/javascript"></script>
	<script src="${ctx}/scripts/jquery.form.js" type="text/javascript"></script>
    <script src="${ctx}/scripts/jquery.cookie.js" type="text/javascript"></script>
    <script src="${ctx}/scripts/jquery.treeview.js" type="text/javascript"></script>
    <script src="${ctx}/scripts/jquery.ui.core.js"></script> 
    <script src="${ctx}/scripts/jquery.ui.widget.js"></script> 
    <script src="${ctx}/scripts/jquery.ui.accordion.js"></script>
    <script type="text/javascript" src="${ctx}/scripts/jquery.pngFix.js"></script>
	<script type="text/javascript" src="${ctx}/scripts/jquery.pngFix.pack.js"></script>
	<script type="text/javascript" src="${ctx}/scripts/common/list.js"></script>
	<script type="text/javascript" src="${ctx}/scripts/common/edit.js" ></script>
	<script type="text/javascript" src="${ctx}/scripts/common/common.js" ></script>
	<style>
	    #hiddenout{position:absolute;top:0px;left:0px;display:none;width:100%;height:100%;background:#757575;filter:alpha(opacity=70);}
	    #hiddencontent,#hiddencontent2{width:100%;height:250px;text-align:center;position:absolute;top:100px;left:0px;display:none;}
	    .title{width:500px;height:27px;margin:0px auto;}
	    .title h2{text-align:left;padding-left:8px;font-size:14px;font-weight:bold;height:27px;line-height:27px;
	              background:url(images/demo_lf_menu_bj.gif) repeat-x;color:#fff;}
	    .tablediv{width:500px;border:#d1d1d1 1px solid;text-align:center;}
	    .hiddentable{width:500px;font-size:12px;color:#003e51;background:#fff;}
	    .td_title{text-align:right;height:30px;width:80px;}
	    .td_input{text-align:left;width:300px;}
	    .fil{width:300px;height:20px;line-height:20px;}
	    .btn{height:23px;width:65px;}
	    input,textarea{border:1px solid #aecaf0;}
	</style>
	
	<script type="text/javascript" >
	    
	    var deftip="请输入资源名";
	    window.onload=initOnload;				
							
		$(document).ready(function() {
		    $(document).pngFix();
			// 分页需要依赖的初始化动作
			window.simpleTable = new SimpleTable('queryForm','${page.thisPageNumber}','${page.pageSize}','${pageRequest.sortColumns}');
			jQuery(".demo_table02_lb").mouseover(function() {jQuery(this).addClass("demo_table02_lb03");}).mouseout(function() {jQuery(this).removeClass("demo_table02_lb03");});
		});
		
		//增加面板
		function showAddDialog()
		{
		    $("#mid").val($("#moduleid").val());
		    $("#hiddencontent").find("h2").html("资源新增");
		    var pname=window.parent.frames["left"].reAsNode.name;
		    $("#moduleName").val(pname);
		    
		    $("#hiddenout").show();
		    $("#hiddencontent").show();
		    $("#inputForm").attr("action","${ctx}/rbac/resource!save.action");
		}
		//关闭新增/修改面板
		function closeDialog()
		{
		  $("#inputForm").find("input").each(function ()
           {
             if(this.type=="text" || this.type=="hidden")
                 $(this).val("");
           });
           
            $("#hiddenout").hide();
		    $("#hiddencontent").hide();
		}
		//修改面板
		function showEditDialog(id)
		{
		    var htmlObj;
		    htmlObj=$.ajax({url:"${ctx}/rbac/resource!info.action",async:false,type:"POST",data:{"id":id}
		    });
		    var resourceResult=htmlObj.responseText;
		    resourceResult=jQuery.parseJSON(resourceResult);
		    $("#id").val(id);
		    $("#value").val(resourceResult.value);

		    if(!isBlank(resourceResult.introduct) )
		    {
		        $("#introduct").val(resourceResult.introduct);
		    }
		    
		    var pname=window.parent.frames["left"].reAsNode.name;
		    $("#moduleName").val(pname);
		    
		    $("#hiddencontent").find("h2").html("资源修改");
		    $("#hiddenout").show();
		    $("#hiddencontent").show();
		    $("#inputForm").attr("action","${ctx}/rbac/resource!save.action");
		}
		//提交新增/修改面板
		function submitDialog()
		{
		   if(isBlank($("#value").val()))
           {
                alert("名称不能为空!");
                return;
           }	   
		   var path=$("#inputForm").attr("action");
            var options={
	            url:path,
	            type:"POST",
	            dataType:"script",
	            success:function(msg)
	            {    
	                var result=eval(msg);
	                if(result)
	                {
	                    alert("操作成功!");
	                    closeDialog();
	                    $("#refresh").val(1);
	                    $("#queryForm").submit();
	                }else
	                {
	                   alert("操作失败!");
	                }
	                
	            }
	        };
	        $("#inputForm").ajaxSubmit(options);
	        
		}
		//详情面板
		function showInfoDialog(id)
		{
		    var htmlObj;
		    htmlObj=$.ajax({url:"${ctx}/rbac/resource!info.action",async:false,type:"POST",data:{"id":id}
		    });
		    var resourceResult=htmlObj.responseText;
		    resourceResult=jQuery.parseJSON(resourceResult);
		    
		    $("#value2").val(resourceResult.value);
		    
		    if(!isBlank(resourceResult.introduct) )
		    {
		        $("#introduct2").val(resourceResult.introduct);
		    }
		    
		    var pname=window.parent.frames["left"].reAsNode.name;
		    $("#moduleName2").val(pname);
		    
		    $("#hiddencontent2").find("h2").html("资源信息");
		    $("#hiddenout").show();
		    $("#hiddencontent2").show();
		}
		//关闭详情面板
		function closeInfoDialog()
		{
		  $("#inputForm2").find("input").each(function ()
           {
             if(this.type=="text")
                 $(this).val("");
           });
           
            $("#hiddenout").hide();
		    $("#hiddencontent2").hide();
		}
		
		//删除
		function del()
		{
		    var count=getCheckCount();
			if(count<=0)
			{
				alert("必须选中一条记录!");
				return;
		    }
		    if(!confirm("确定删除选中记录吗?"))
		    {
		        return;
		    }
		    
		    var options={
	            url:"${ctx}/rbac/resource!delete.action",
	            type:"POST",
	            dataType:"script",
	            success:function(msg)
	            {    
	                result =eval(msg);
				    if(result==0)
				    {
				       alert("操作失败!");
				       
				    }else if(result==1)
				    {
				       alert("操作成功!");
		               $("#refresh").val(1);
		               $("#queryForm").submit();
				    }
	                
	            }
	        };
	        $("#queryForm").ajaxSubmit(options);
		   
		}
		
		
	 </script>
</rapid:override> 

<rapid:override name="content">
  <div class="demo_rtdbq02 out_form_div" id="mytest">
	<form onSubmit="resetValue();" id="queryForm" name="queryForm" action="${ctx}/rbac/resource!hierarchyPage.action" method="post" >
	    <input type="hidden" id="moduleid" name="moduleid" value="${moduleid}" />
	    <input type="hidden" id="refresh" name="refresh" value="0" /> 
	    <input type="hidden" id="pageNumber" name="pageRequestBak.pageNumber" value="${pageRequest.pageNumber}">
	    <input type="hidden" id="pageSize" name="pageRequestBak.pageSize" value="${pageRequest.pageSize}">
	    <!--最上行区域  -->
	  	<div class="tf_main_bt">
	  	    <!--上行左边菜单区域  -->
			<div class="tf_main_bt_lf">
			    <%
			        boolean canCreate=AdminorHolder.getInstance().hasPermission("com.manage.rbac.resource",OperationFlag.CREATE);
			        if(canCreate) {
			    %>
				<a  href="javascript:;" onclick="showAddDialog('${item.id}');return false;"><span>新&nbsp;增</span></a>
				<% } %>
				<%
			        boolean canDelete=AdminorHolder.getInstance().hasPermission("com.manage.rbac.resource",OperationFlag.DELETE);
			        if(canDelete) {
			    %>
				<a  href="javascript:;" onclick="del();return false;"><span>删&nbsp;除</span></a>
				<% } %>
			</div>
			<!--上行右边查询区域  -->
			<div class="tf_main_bt_rt" >
				<input class="bt_rt_input" id="checkInput" value="${query.value}" onfocus="if(value==deftip)this.value='';"  onblur="javascript:if(value==''){value=deftip;};"  
				name="resource.value" type="text" />
				<input class="cx" type="image" alt="快速查询" src="${ctx}/images/demo_main_bj03.gif" />
			</div>
		</div>
		<!-- 内容table列表 -->
		<div id="tablecontent">
	  	<table width="100%" id="all" border="0" align="center" cellPadding="0"  cellSpacing="0">
			<tr class="tf_table_th">
			    <% if(canDelete) { %>
				<td class="tdleft" width=""><input type="checkbox" name="allbox2" value="0" onclick="setAllCheckboxState('ids',this.checked)" class="demo_qx">&nbsp;全选</td>
				<% } %>
				<td width="" >ID</td>
				<td width="" >资源名</td>
				<td width="" >描述</td>
				<td width="">操作</td>
			 </tr>
			 <c:forEach items="${page.result}" var="item" varStatus="status">
		  	  
			  <tr class="demo_table02_lb ${status.count % 2 == 0 ? 'demo_table02_lb02' : ''}">
			     <% if(canDelete) { %>
				<td class="tdleft" align="left"><input type="checkbox" id="ids" name="ids" value="${item.id}"></td>
				<% } %>
				<td style="width:100px;">
				<c:out value='${item.id}'/>&nbsp;
				</td>
				
				<td>
				<c:out value='${item.value}'/>&nbsp;
				</td>
				
				<td>
				<c:out value='${item.introduct}'/>&nbsp;
				<td>
				    <%
				        boolean canUpdate=AdminorHolder.getInstance().hasPermission("com.manage.rbac.resource",OperationFlag.UPDATE);
			            if(canUpdate) {
			        %>
					<a  href="javascript:;" onclick="showEditDialog('${item.id}');return false;"><span>修&nbsp;改</span></a>
					<% } %>
					<a  href="javascript:;" onclick="showInfoDialog('${item.id}');return false;"><span>详&nbsp;情</span></a>
				</td>
			  </tr>
		  	  </c:forEach>		  
		</table>
        <simpletable:pageToolbar page="${page}"></simpletable:pageToolbar>  
		</div>
	</form>
</div>
<div id="hiddenout"></div>
<div id="hiddencontent">
    <div class="title">
	    <h2>资源新增</h2>
	</div>
	<form id="inputForm" name="inputForm" method="post">
	<input type="hidden" id="mid" name="mid" value="${moduleid}"/>
	<input type="hidden" id="id" name="id" />
	   <div class="tablediv">
	      <table cellpadding="0" cellspacing="2" border="0" class="hiddentable" >
	          <tr>
	            <td class="td_title">名&nbsp;&nbsp;称</td>
	            <td class="td_input"><input id="value" name="value" type="text" maxlength="100" class="must:true,type:string fil" /> 
	            </td>
	          </tr>
	          <tr>  
	            <td class="td_title">所属模块</td>
	            <td class="td_input">
	                <input id="moduleName" name="moduleName" type="text" readonly="readonly" disabled="disabled" class="fil"/> 
	            </td>
	          </tr>
	          <tr>  
	            <td class="td_title">描&nbsp;&nbsp;述</td>
	            <td class="td_input">
	                <input id="introduct" name="introduct" type="text" maxlength="200" class="type:string fil"/> 
	            </td>
	          </tr>
	          <tr>
	             <td colspan="2" style="text-align:center">
	                 <input type="button" class="btn" onclick="submitDialog()" value="提交" />
	                 <input type="button" class="btn" onclick="closeDialog();" value="关闭" />
	             </td>
	          </tr>
	      </table>
	   </div>
	</form>
</div>
<div id="hiddencontent2">
    <div class="title">
	    <h2>资源信息</h2>
	</div>
	<form id="inputForm2" name="inputForm2">
	   <div class="tablediv">
	      <table cellpadding="0" cellspacing="2" border="0" class="hiddentable" >
	          <tr>
	            <td class="td_title">名&nbsp;&nbsp;称</td>
	            <td class="td_input"><input id="value2" name="value2" type="text" maxlength="100" class="fil" readonly="readonly" disabled="disabled" /> 
	            </td>
	          </tr>
	          <tr>  
	            <td class="td_title">所属模块</td>
	            <td class="td_input">
	                <input id="moduleName2" name="moduleName2" type="text" readonly="readonly" disabled="disabled" class="fil"/> 
	            </td>
	          </tr>
	          <tr>  
	            <td class="td_title">描&nbsp;&nbsp;述</td>
	            <td class="td_input">
	                <input id="introduct2" name="introduct2" type="text" maxlength="200" class="fil" readonly="readonly" disabled="disabled" /> 
	            </td>
	          </tr>
	          <tr>
	             <td colspan="2" style="text-align:center">
	                 <input type="button" class="btn" onclick="closeInfoDialog();" value="关闭" />
	             </td>
	          </tr>
	      </table>
	   </div>
	</form>
</div>

</rapid:override>

<%@ include file="base.jsp" %>

