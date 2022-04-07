<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.manage.model.rbac.ResourcePermission.OperationFlag" %>
<%@ page import="com.manage.service.rbac.AdminorHolder" %>
<%@ taglib tagdir="/WEB-INF/tags/simpletable" prefix="simpletable"%>
<%@ include file="/commons/taglibs.jsp" %>

<rapid:override name="head">
    <title>角色列表</title>
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
	    .td_title{text-align:right;height:40px;width:80px;}
	    .td_input{text-align:left;width:300px;}
	    .fil{width:300px;height:20px;line-height:20px;}
	    .btn{height:23px;width:65px;}
	    input,textarea{border:1px solid #aecaf0;}
	</style>
	
	<script type="text/javascript" >
	    
	    var deftip="请输入角色名";
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
		    var width=900;
		    var height=500;
		    var itop=(window.screen.availHeight-30-height)/2;
		    var ileft=(window.screen.availWidth-10-width)/2;
		    window.open(url,'','width=900,height=500,top='+itop+',left='+ileft+',resizable=1,scrollbars=1,menubar=no,location=no,status=no');
		}
		
		//增加面板
		function showAddDialog()
		{
		    $("#hiddencontent").find("h2").html("角色新增");
		    
		    $("#hiddenout").show();
		    $("#hiddencontent").show();
		    $("#inputForm").attr("action","${ctx}/rbac/role!save.action");
		}
		//关闭新增/修改面板
		function closeDialog()
		{
		  $("#inputForm").find("input").each(function ()
           {
             if(this.type=="text")
                 $(this).val("");
           });
           
            $("#hiddenout").hide();
		    $("#hiddencontent").hide();
		}
		//修改面板
		function showEditDialog(id)
		{
		    var htmlObj;
		    htmlObj=$.ajax({url:"${ctx}/rbac/role!info.action",async:false,type:"POST",data:{"id":id}
		    });
		    var roleResult=htmlObj.responseText;
		    roleResult=jQuery.parseJSON(roleResult);
		    $("#id").val(id);
		    $("#name").val(roleResult.name);

		    if(!isBlank(roleResult.introduct) )
		    {
		        $("#introduct").val(roleResult.introduct);
		    }
		    
		    $("#hiddencontent").find("h2").html("角色修改");
		    $("#hiddenout").show();
		    $("#hiddencontent").show();
		    $("#inputForm").attr("action","${ctx}/rbac/role!save.action");
		}
		//提交新增/修改面板
		function submitDialog()
		{
		   if(isBlank($("#name").val()))
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
		    htmlObj=$.ajax({url:"${ctx}/rbac/role!info.action",async:false,type:"POST",data:{"id":id}
		    });
		    var roleResult=htmlObj.responseText;
		    roleResult=jQuery.parseJSON(roleResult);
		    $("#name2").val(roleResult.name);

		    if(!isBlank(roleResult.introduct) )
		    {
		        $("#introduct2").val(roleResult.introduct);
		    }
		    
		    $("#hiddencontent2").find("h2").html("角色信息");
		    $("#hiddenout").show();
		    $("#hiddencontent2").show();
		}
		//关闭详情面板
		function closeInfoDialog()
		{
		  $("#inputForm2").find("input").each(function ()
           {
             if(this.type=="text" || this.type=="hidden")
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
	            url:"${ctx}/rbac/role!delete.action",
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
  <div class="demo_rtdbq02 out_form_div" id="mytest" >

	<form onSubmit="resetValue();" id="queryForm" name="queryForm" action="${ctx}/rbac/role.action" method="post" >
	    <input type="hidden" id="refresh" name="refresh" value="0" /> 
	    <input type="hidden" id="pageNumber" name="pageRequestBak.pageNumber" value="${pageRequest.pageNumber}">
	    <input type="hidden" id="pageSize" name="pageRequestBak.pageSize" value="${pageRequest.pageSize}">
	  	<!--最上行区域  -->
	  	<div class="tf_main_bt">
	  	    <!--上行左边菜单区域  -->
			<div class="tf_main_bt_lf">
			    <%
			        boolean canCreate=AdminorHolder.getInstance().hasPermission("com.manage.rbac.role",OperationFlag.CREATE);
			        if(canCreate) {
			    %>
				<a  href="javascript:;" onclick="showAddDialog('${item.id}');return false;"><span>新&nbsp;增</span></a>
				<% } %>
				<%
			        boolean canDelete=AdminorHolder.getInstance().hasPermission("com.manage.rbac.role",OperationFlag.DELETE);
			        if(canDelete) {
			    %>
				<a  href="javascript:;" onclick="del();return false;"><span>删&nbsp;除</span></a>
				<% } %>
			</div>
			<!--上行右边查询区域  -->
			<div class="tf_main_bt_rt" >
				<input class="bt_rt_input" id="checkInput" value="${query.name}" onfocus="if(value==deftip)this.value='';"  onblur="javascript:if(value==''){value=deftip;};"  
				       name="role.name" type="text" />
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
				<td width="" >名称</td>
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
				
				<td style="width:30%;">
				<c:out value='${item.name}'/>&nbsp;
				</td>
				
				<td>
				<div style="width:30%;overflow:hidden;text-overflow:ellipsis;white-space:nowrap;" title="${item.introduct}">
				<c:out value='${item.introduct}'/>&nbsp;
				</div></td>
				<td style="width:25%;">
				    <%
				        boolean canUpdate=AdminorHolder.getInstance().hasPermission("com.manage.rbac.role",OperationFlag.UPDATE);
			            if(canUpdate) {
			        %>
					<a  href="javascript:;" onclick="showEditDialog('${item.id}');return false;"><span>修&nbsp;改</span></a>
					<% } %>
					<a  href="javascript:;" onclick="showInfoDialog('${item.id}');return false;"><span>详&nbsp;情</span></a>
					<%
			            if(canCreate && canDelete && canUpdate) {
			        %>
					<a  href="javascript:;" onclick="openTo('${ctx}/rbac/role!authInput.action?id=${item.id}&name=${item.name}');return false;"><span>授&nbsp;权</span></a>
					<% } %>
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
	    <h2>角色新增</h2>
	</div>
	<form id="inputForm" name="inputForm" method="post">
	<input type="hidden" id="id" name="id" />
	   <div class="tablediv">
	      <table cellpadding="0" cellspacing="2" border="0" class="hiddentable" >
	          <tr>
	            <td class="td_title">名&nbsp;&nbsp;称</td>
	            <td class="td_input"><input id="name" name="name" type="text" maxlength="100" class="must:true,type:string fil" /> 
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
	    <h2>角色信息</h2>
	</div>
	<form id="inputForm2" name="inputForm2">
	   <div class="tablediv">
	      <table cellpadding="0" cellspacing="2" border="0" class="hiddentable" >
	          <tr>
	            <td class="td_title">名&nbsp;&nbsp;称</td>
	            <td class="td_input"><input id="name2" name="name2" type="text" maxlength="100" class="fil" readonly="readonly" disabled="disabled" /> 
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
