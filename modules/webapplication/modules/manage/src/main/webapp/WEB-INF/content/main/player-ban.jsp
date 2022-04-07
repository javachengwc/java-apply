<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/commons/taglibs.jsp" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>封号处理</title>
		<script src="${ctx}/scripts/jquery.js" type="text/javascript"></script>
		<script src="${ctx}/scripts/jquery.form.js" type="text/javascript"></script>
		
		<style>
		    .td_title{text-align:right;height:30px;width:80px;}
		    table{width:460px;font-size:12px;color:#003e51;}
		    .td_input{text-align:right;width:80px;}
		    .btn{height:23px;width:65px;}
		    input,textarea{border:1px solid #aecaf0;}
		</style>
		<script type="text/javascript">
		    function oncls()
		    {
                 parent.closePop();
		    }
		    function onSave()
		    {  
		       if($("#banReason").val()==null ||$("#banReason").val()=="" )
		       {
		          alert("封号原因不能为空!");
		       }else
		       {    var playerId =$("#id").val();
			        var options={
			            url:"${ctx}/main/player!ban.action",
			            type:"POST",
			            dataType:"script",
			            success:function(msg)
			            {    
			                var result=eval(msg);
			                if(result)
			                {
			                    parent.onClose(playerId,result);
			                  
			                }else
			                {
			                   alert("封号失败!");
			                }
			                
			            }
			        };
			        $("#inputForm").ajaxSubmit(options);
		        }
		    }
		</script>
	</head>
	<body>
	    <s:form id="inputForm" name="inputForm">
	    <input type="hidden" id="id" name="id" value="<s:property value="id" />" />
	    <div style="text-align:center;">
	       <table cellpadding="0" cellspacing="2" border="0" >
	           <tr>
	             <td class="td_title">玩&nbsp;&nbsp;家</td>
	             <td class="td_input"><input id="playerName" name="playerName" type="text" value="<s:property value="playerName" />" readonly="readonly" disabled="disabled" /> 
	             </td>
	           </tr>
	           <tr>  
	             <td class="td_title">原&nbsp;&nbsp;因</td>
	             <td class="td_input">
	                 <textarea id="banReason" name="banReason" rows="4" cols="40"></textarea>
	             </td>
	           </tr>
	           <tr>
	              <td colspan="2" style="text-align:center">
	                  <input type="button" class="btn" onclick="onSave()" value="提交" />
	                  <input type="button" class="btn" onclick="oncls();" value="关闭" />
	              </td>
	           </tr>
	       </table>
	    </div>
	    </s:form>
	</body>
</html>