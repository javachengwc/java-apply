<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/commons/taglibs.jsp" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>赠款操作</title>
		<script src="${ctx}/scripts/jquery.js" type="text/javascript"></script>
		<script src="${ctx}/scripts/jquery.form.js" type="text/javascript"></script>
		<script src="${ctx}/scripts/common/edit.js" type="text/javascript"></script>
		
		<style>
		    .td_title{text-align:right;height:30px;width:80px;}
		    table{width:460px;font-size:12px;color:#003e51;}
		    .td_input{text-align:left;width:300px;}
		    .fil{width:98%;}
		    .in{width:120px;}
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
		       if(checknum($("#chgGold"),"金币") && checknum($("#chgRmb"),"元宝") )
		       {
		            var playerId =$("#id").val();
			        var options={
			            url:"${ctx}/main/player!presentGold.action",
			            type:"POST",
			            dataType:"script",
			            success:function(msg)
			            {    
			                var result=jQuery.parseJSON(msg);
			                if(result.result)
			                {
			                    parent.onClose(playerId,result);
			                  
			                }else
			                {
			                   alert("赠款失败!");
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
	    <input type="hidden" id="id" name="id" value="<s:property value="user.id" />" />
	    <div style="text-align:center;">
	       <table cellpadding="0" cellspacing="2" border="0" >
	           <tr>
	             <td class="td_title">玩&nbsp;&nbsp;家</td>
	             <td class="td_input"><input id="playerName" name="playerName" type="text" class="fil" value="<s:property value="user.playerName" />" readonly="readonly" disabled="disabled" /> 
	             </td>
	           </tr>
	           <tr>  
	             <td class="td_title">当前金钱</td>
	             <td class="td_input">
	                 <input id="gold" name="gold" type="text" class="in" value="<s:property value="user.gold" />" readonly="readonly" disabled="disabled" />金币
	                 <input id="rmb" name="rmb" type="text" class="in" value="<s:property value="user.rmb" />" readonly="readonly" disabled="disabled" />元宝
	             </td>
	           </tr>
	           <tr>  
	             <td class="td_title">赠款金额</td>
	             <td class="td_input">
	                 <input id="chgGold" name="chgGold" type="text" class="in" value="0" />金币
	                 <input id="chgRmb" name="chgRmb" type="text" class="in" value="0" />元宝
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