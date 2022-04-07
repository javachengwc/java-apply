<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib tagdir="/WEB-INF/tags/simpletable" prefix="simpletable"%>
<%@ include file="/commons/taglibs.jsp" %>

<rapid:override name="head">
    <title>角色授权</title>
	<link href="${ctx}/styles/index_css.css"  type="text/css" rel="stylesheet"/>
	<script src="${ctx}/scripts/jquery.js" type="text/javascript"></script>
	<script src="${ctx}/scripts/jquery.form.js" type="text/javascript"></script>
	<script type="text/javascript" src="${ctx}/scripts/common/list.js"></script>
	<style>
	   
	</style>
	<script type="text/javascript" >
	    
	    window.onload=initWidth;				
		
		function auth()
	    {  
	        var options={
	            url:"${ctx}/rbac/role!auth.action",
	            type:"POST",
	            dataType:"script",
	            success:function(msg)
	            {    
	                var result=eval(msg);
	                if(result)
	                {
	                    alert("授权成功!");
	                    window.close();
	                 
	                }else
	                {
	                    alert("授权失败!");
	                }
	                
	            }
	        };
	        $("#queryForm").ajaxSubmit(options);
	        
	    }
	 </script>
</rapid:override> 

<rapid:override name="content">
  <div class="demo_rtdbq02 out_form_div" id="mytest">
	<form id="queryForm" name="queryForm" method="post">
	    <input type="hidden" id="id" name="id" value="${id}">
	  	<!--最上行区域  -->
	  	<div class="tf_main_bt">
	  	    <!--上行左边菜单区域  -->
			<div class="tf_main_bt_lf">
			    <a  href="javascript:;" onclick="auth();return false;"><span>确&nbsp;定</span></a>
			</div>
			<!--上行右边查询区域  -->
			<div class="tf_main_bt_rt" >
				<span>当前角色:${role.name}</span>
			</div>
		</div>
		<!-- 内容table列表 -->
		<div id="tablecontent" >
	  	<table width="100%" id="all" border="0" align="center" cellPadding="0"  cellSpacing="0">
			<tr class="tf_table_th">
				<td style="width:25%;">资源名</td>
				<td style="width:25%;">描述</td>
				<td width="">权限项</td>
			 </tr>
			 <s:iterator value="rpList" var="item" status="status">
		  	  
			  <tr class="demo_table02_lb ${status.count % 2 == 0 ? 'demo_table02_lb02' : ''}">
				
				<td >
				<div style="width:50%;overflow:hidden;text-overflow:ellipsis;white-space:nowrap;" title="${item.resourceValue}">
				<c:out value='${item.resourceValue}'/>&nbsp;
				</div>
				</td>
				<td>
				<div style="width:50%;overflow:hidden;text-overflow:ellipsis;white-space:nowrap;" title="${item.resourceIntroduct}">
				<c:out value='${item.resourceIntroduct}'/>&nbsp;
				</div>
				</td>
				<td >
				    <s:iterator value="@com.manage.model.rbac.ResourcePermission$OperationFlag@values()" >
                        <input type="checkbox" id="auths" name="auths" value="${item.resourceid}_${value}" <s:if test="#item.flag!=null && ((#item.flag & value) >0)">checked="checked"</s:if>  />${name}&nbsp;
                    </s:iterator>
				</td>
			  </tr>
		  	  </s:iterator>		  
		</table> 
		</div>
	</form>
</div>
</rapid:override>

<%@ include file="base.jsp" %>
