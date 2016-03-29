<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/commons/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>用户详细信息</title>
		<script src="${ctx}/scripts/jquery.js" type="text/javascript"></script>
		<script src="${ctx}/scripts/jquery.form.js" type="text/javascript"></script>
		<script src="${ctx}/scripts/common/edit.js" type="text/javascript"></script>
		<style>
		    *{ margin:0; padding:0;}
		    #title,#contain{width:80%;height:100%;margin:0px auto;}
		    #title h2{text-align:center;font-size:14px;font-weight:bold;height:27px;line-height:27px;background:url(../images/demo_lf_menu_bj.gif) repeat-x;color:#fff;}
		    .td_title{text-align:right;height:30px;width:15%;line-height:30px;font-size:14px;color:#7a7a7a;padding-right:15px; }
		    .td_input{text-align:ft;width:35%;height:30px;line-height:30px;}
		    .td_input input{line-height:23px;height:23px;width:70%;}
		    .btn{height:23px;width:65px;}
		    table{width:100%;font-size:12px;background:#d4d4d4;}
		    table tr{background:#fff;}
		    input,textarea,select{border:1px solid #aecaf0;color:#003e51;}
		    select{line-height:23px;height:23px;width:70%;}
		</style>
		<script>
		   $(document).ready(function () {
		       $("input").attr("readonly",true);
		       $("input").attr("disabled",true);
		       $("select").attr("disabled",true);
		       $(".btn").attr("disabled",false);
		       }
		   );
		</script>
	</head>
	<body>
	    <div id="title">
	        <h2>用户信息</h2>
	    </div>
	    <div id="contain">
	    <s:form id="inputForm" name="inputForm">
	    <input type="hidden" id="id" name="id" value="<s:property value="user.id" />" />
	    <div style="text-align:center;">
	       <table cellpadding="0" cellspacing="1" border="0" >
	           <tr>  
	             <td class="td_title">账&nbsp;&nbsp;号</td>
	             <td class="td_input"><input id="account" name="account" type="text" value="<s:property value="user.account" />" /> 
	             </td>
	             <td class="td_title">名&nbsp;&nbsp;称</td>
	             <td class="td_input"><input id="playerName" name="playerName" type="text" value="${user.playerName}" readonly="readonly" disabled="disabled" />
	             </td>
	           </tr>
	           
	           <tr>  
	             <td class="td_title">性&nbsp;&nbsp;别</td>
                 <td class="td_input">
                     <select id="sex" name="sex" disabled="disabled" >
                        <option value="1" <s:if test="user.sex==1">selected="selected"</s:if> >男</option>
                        <option value="0" <s:if test="user.sex==0">selected="selected"</s:if> >女</option>
	                 </select>
                 </td>
                 <td class="td_title">类&nbsp;&nbsp;别</td>
                 <td class="td_input">
                     <select id="force" name="force" disabled="disabled" >
                        <option value="1" <s:if test="user.force==1">selected="selected"</s:if> >鸟</option>
                        <option value="2" <s:if test="user.force==2">selected="selected"</s:if> >猪</option>
                        <option value="3" <s:if test="user.force==3">selected="selected"</s:if> >猴</option>
                        <option value="4" <s:if test="user.force==4">selected="selected"</s:if> >猫</option>
	                 </select>
                 </td>
	           </tr>
	         
	           <tr>  
	             <td class="td_title">职&nbsp;&nbsp;业</td>
                 <td class="td_input">
                     <select id="job" name="job" disabled="disabled" >
                        <option value="1" <s:if test="user.job==1">selected="selected"</s:if> >神射</option>
                        <option value="2" <s:if test="user.job==2">selected="selected"</s:if> >神兵</option>
                        <option value="3" <s:if test="user.job==3">selected="selected"</s:if> >天将</option>
                        <option value="4" <s:if test="user.job==4">selected="selected"</s:if> >贤者</option>
                        <option value="5" <s:if test="user.job==5">selected="selected"</s:if> >偷蛋者</option>
                        
	                 </select>
                 </td>
                  <td class="td_title">等&nbsp;&nbsp;级</td>
	             <td class="td_input"><input id="lv" name="lv" type="text" value="${user.lv}" /> 
	             </td>
	           </tr>
			  
	           <tr>  
	             <td class="td_title">状&nbsp;&nbsp;态</td>
	             <td>
	                 <select id="banned" name="banned" disabled="disabled" >
                        <option value="0" <s:if test="user.banned==0">selected="selected"</s:if> >正常</option>
                        <option value="1" <s:if test="user.banned==1">selected="selected"</s:if> >锁定</option>
	                 </select>  
	             </td>
	             <td class="td_title">排&nbsp;&nbsp;名</td>
	             <td class="td_input"><input id="ranking" name="ranking" type="text" value="${user.ranking}" /> 
	             </td>
	           </tr>
	           
	           <tr>  
	             <td class="td_title">声&nbsp;&nbsp;望</td>
	             <td class="td_input"><input id="credit" name="credit" type="text" value="${user.credit}" /> 
	             </td>
	             <td class="td_title">军&nbsp;&nbsp;功</td>
	             <td class="td_input"><input id="exploit" name="exploit" type="text" value="${user.exploit}" /> 
	             </td>
	           </tr>
	          
	          <tr>  
	             <td class="td_title">体&nbsp;&nbsp;力</td>
	             <td class="td_input"><input id="power" name="power" type="text" value="${user.power}" /> 
	             </td>
	              <td class="td_title">额外体力</td>
	             <td class="td_input"><input id="powerExt" name="powerExt" type="text" value="${user.powerExt}" /> 
	             </td>
	           </tr>
	           <tr>  
	             <td class="td_title">仓库格数</td>
	             <td class="td_input"><input id="depotNum" name="depotNum" type="text" value="${user.depotNum}" /> 
	             </td>
	              <td class="td_title">防守阵型</td>
	             <td class="td_input"><input id="mapusing" name="mapusing" type="text" value="${user.mapusing}" /> 
	             </td>
	           </tr>
	           
	           <tr>  
	             <td class="td_title">普通关卡</td>
	             <td class="td_input"><input id="maxCommStageId" name="maxCommStageId" type="text" value="${user.maxCommStageId}" /> 
	             </td>
	              <td class="td_title">精英关卡</td>
	             <td class="td_input"><input id="maxEliteStageId" name="maxEliteStageId" type="text" value="${user.maxEliteStageId}" /> 
	             </td>
	           </tr>
	           <tr>  
	             <td class="td_title">注册时间</td>
	             <td class="td_input"><input id="regTimeStr" name="regTimeStr" type="text" value="${usersExt.regTimeStr}" /> 
	             </td>
	             <td class="td_title">登录时间</td>
	             <td class="td_input"><input id="lastLoginTimeStr" name="lastLoginTimeStr" type="text" value="${usersExt.lastLoginTimeStr}" /> 
	             </td>
	           </tr>
	           <tr>  
	             <td class="td_title">下线时间</td>
	             <td class="td_input"><input id="offLineTimeStr" name="maxEliteStageId" type="text" value="${usersExt.offLineTimeStr}" /> 
	             </td>
	              <td class="td_title">防沉迷</td>
                 <td class="td_input">
                     <select id="adult" name="adult" disabled="disabled" >
                        <option value="0" <s:if test="user.adult==0">selected="selected"</s:if> >未通过</option>
                        <option value="1" <s:if test="user.adult==1">selected="selected"</s:if> >通过</option>
	                 </select>
                 </td>
	           </tr>
              
	           <tr>  
	             <td class="td_title">服务器</td>
	             <td class="td_input"><input id="serverId" name="serverId" type="text" value="${user.serverId}"/> 
	             </td>
	             <td class="td_title">推广平台</td>
	             <td class="td_input"><input id="site" name="site" type="text" value="${user.site}" /> 
	             </td>
	           </tr>
	           
	           <tr>
	              <td colspan="4" style="text-align:center">
	                  <input type="button" class="btn" onclick="onclose();" value="关闭" />
	              </td>
	           </tr>
	       </table>
	    </div>
	    </s:form>
	    </div>
	</body>
</html>