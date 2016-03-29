<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/commons/taglibs.jsp" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>角色分配</title>
		<link href="${ctx}/styles/jquery.multiselect2side.css"  type="text/css" rel="stylesheet"/>
		<script src="${ctx}/scripts/jquery.js" type="text/javascript"></script>
		<script src="${ctx}/scripts/jquery.form.js" type="text/javascript"></script>
		<script src="${ctx}/scripts/jquery.multiselect2side.js" type="text/javascript"></script>
		
		<style>
		    .td_title{text-align:right;height:30px;width:80px;}
		    table{width:460px;font-size:12px;color:#003e51;}
		    .td_input{text-align:left;width:350px;}
		    .fil{width:98%;}
		    .in{width:120px;}
		    .btn{height:23px;width:65px;}
		    input,textarea{border:1px solid #aecaf0;}
		</style>
		<script type="text/javascript">
		    $().ready(
		        function() {
		            $("#role").multiselect2side(
		            {
		               selectedPosition:'right',
		               moveOptions:false,
		               labelsx:'可分配角色',
		               labeldx:'已有角色',
		               autoSort:true,
		               autoSortAvailable:true
		            }
		            );
		        }
		    );
		    
		    function oncls()
		    {
                 parent.closePop();
		    }
		    function onSave()
		    {  
		        var options={
		            url:"${ctx}/rbac/adminor!assign.action",
		            type:"POST",
		            dataType:"script",
		            success:function(msg)
		            {    
		                var result=eval(msg);
		                if(result)
		                {
		                    alert("分配角色成功!");
		                    oncls();
		                 
		                }else
		                {
		                    alert("分配角色失败!");
		                }
		                
		            }
		        };
		        $("#inputForm").ajaxSubmit(options);
		        
		    }
		</script>
	</head>
	<body>
	    <s:form id="inputForm" name="inputForm">
	    <input type="hidden" id="id" name="id" value="<s:property value="adminor.id" />" />
	    <div style="text-align:center;">
	       <table cellpadding="0" cellspacing="2" border="0" >
	           <tr>
	             <td class="td_title">账&nbsp;&nbsp;号</td>
	             <td class="td_input"><input id="name" name="name" type="text" class="fil" value="<s:property value="adminor.name" />" readonly="readonly" disabled="disabled" /> 
	             </td>
	           </tr>
	           <tr>  
	             <td class="td_title">名&nbsp;&nbsp;称</td>
	             <td class="td_input"><input id="nickname" name="nickname" type="text" class="fil" value="<s:property value="adminor.nickname" />" readonly="readonly" disabled="disabled" /> 
	             </td>
	           </tr>
	           <tr>  
	             <td class="td_title" valign="top">角色分配</td>
	             <td class="td_input">
	                 <select id="role" name="roleSelected" multiple="multiple" size="8">
	                 <s:iterator value="seleRoles" id="perRole">
	                 <s:if test="#perRole.rela==true">
	                     <option value="${perRole.id}" selected>${perRole.name}</option>
	                 </s:if>
	                 <s:else>
	                     <option value="${perRole.id}">${perRole.name}</option>
	                 </s:else>
	                 </s:iterator>
	                 </select>
	             </td>
	           </tr>
	           <tr>
	              <td colspan="2" style="text-align:center">
	                  <input type="button" class="btn" onclick="onSave()" value="保存" />
	                  <input type="button" class="btn" onclick="oncls();" value="关闭" />
	              </td>
	           </tr>
	       </table>
	    </div>
	    </s:form>
	</body>
</html>