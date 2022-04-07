<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/commons/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>用户武将编辑</title>
		<script src="${ctx}/scripts/jquery.js" type="text/javascript"></script>
		<script src="${ctx}/scripts/jquery.form.js" type="text/javascript"></script>
		<script src="${ctx}/scripts/common/edit.js" type="text/javascript"></script>
		<style>
		    *{ margin:0; padding:0;}
		    #title,#contain{width:500px;height:100%;margin:0px auto;}
		    #title h2{text-align:center;font-size:14px;font-weight:bold;height:27px;line-height:27px;background:url(../images/demo_lf_menu_bj.gif) repeat-x;color:#fff;}
		    .td_title{text-align:right;height:30px;width:30%;line-height:30px;font-size:14px;color:#7a7a7a;padding-right:35px; }
		    .td_input{text-align:ft;width:70%;height:30px;line-height:30px;}
		    .td_input input{line-height:23px;height:23px;width:70%;}
		    .btn{height:23px;width:65px;}
		    table tr{background:#fff;}
		    input,textarea,select{border:1px solid #aecaf0;color:#003e51;}
		    select{line-height:23px;height:23px;width:70%;}
		</style>
		<script type="text/javascript">
		    function onSave()
		    {  
		        if(checknum($("#lv"),"等级") && checknum($("#life"),"生命值") && checknum($("#skillLv"),"技能等级"))
		        {
		            var id=$("#id").val();
		            var lv=$("#lv").val();
		            
		            var state=$("#state").val();
		            $("#state").children("option").each(function ()
		            {
		               if(this.value==state)
		               {state=this.innerHTML;}
		            });
		            
		            var options={
			            url:"${ctx}/main/user-captain!update.action",
			            type:"POST",
			            dataType:"script",
			            success:function(msg)
			            {    
			                var result=eval(msg);
			                if(result)
			                {
			                   alert("修改成功!");
			                   opener.refreshLv(id,lv);
			                   opener.refreshState(id,state);
			                   onclose();
			                  
			                }else
			                {
			                   alert("修改失败!");
			                }
			                
			            }
			        };
			        $("#inputForm").ajaxSubmit(options);
			     }
		        
		    }
		</script>
	</head>
	<body>
	    <div id="title">
	        <h2>用户武将</h2>
	    </div>
	    <div id="contain">
	    <s:form id="inputForm" name="inputForm">
	    <input type="hidden" id="id" name="id" value="<s:property value="userCaptain.id" />" />
	    <input type="hidden" id="owner" name="owner" value="<s:property value="userCaptain.owner" />" />
	    <div style="text-align:center;">
	       <table cellpadding="0" cellspacing="1" border="0" style="width:500px;font-size:12px;" bgcolor="#d4d4d4">
	           <tr>  
	             <td class="td_title">武&nbsp;&nbsp;将</td>
	             <td class="td_input"><input id="capname" name="capname" type="text" value="<s:property value="userCaptain.capname" />" readonly="readonly" disabled="disabled" /> 
	             </td>
	           </tr>
	           <tr>
	             <td class="td_title">所属玩家</td>
	             <td class="td_input"><input id="playerName" name="playerName" type="text" value="${userCaptain.playerName}" readonly="readonly" disabled="disabled" />
	             </td>
	           </tr>
	           <tr>  
	             <td class="td_title">等&nbsp;&nbsp;级</td>
	             <td class="td_input"><input id="lv" name="lv" type="text" value="${userCaptain.lv}" /> 
	             </td>
	           </tr>
	           <tr>  
	             <td class="td_title">生命值</td>
	             <td class="td_input"><input id="life" name="life" type="text" value="${userCaptain.life}" /> 
	             </td>
	           </tr>
	           <tr>  
	             <td class="td_title">技能等级</td>
	             <td class="td_input"><input id="skillLv" name="skillLv" type="text" value="${userCaptain.skillLv}" /> 
	             </td>
	           </tr>
	           <tr>  
	             <td class="td_title">状态</td>
                 <td class="td_input">
                     <select id="state" name="state">
                        <option value="0" <s:if test="userCaptain.state==0">selected="selected"</s:if> >离队</option>
                        <option value="1" <s:if test="userCaptain.state==1">selected="selected"</s:if> >入队</option>
	                 </select>
                 </td>
	           </tr>
	           <tr>
	              <td colspan="2" style="text-align:center">
	                  <input type="button" class="btn" onclick="onSave()" value="保存" />
	                  <input type="button" class="btn" onclick="onclose();" value="关闭" />
	              </td>
	           </tr>
	       </table>
	    </div>
	    </s:form>
	    </div>
	</body>
</html>