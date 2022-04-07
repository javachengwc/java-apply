<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/commons/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%
   Integer tip=(Integer)request.getAttribute("tip");
%>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>登录</title>
		<script src="${ctx}/scripts/jquery.js" type="text/javascript"></script>
		<script src="${ctx}/scripts/common/edit.js" type="text/javascript"></script>
	    <style type="text/css">
	        *{ margin:0; padding:0;}
	        body{background:#efffff;}
		    #main{width:50%;height:50%;margin:200px auto;} 
		    #title,#contain{width:500px;height:100%;margin:0px auto;}
		    #title{border:1px solid #0099ff;border-buttom:0px;}
		    #contain{border:1px solid #0099ff;border-top:0px;}
		    #title h2{text-align:center;font-size:14px;font-weight:bold;height:27px;line-height:27px;background:url(images/demo_lf_menu_bj.gif) repeat-x;color:#fff;}
		    .td_title{text-align:right;height:30px;width:15%;line-height:27px;font-size:14px;color:#003e51;padding-right:15px; }
		    .td_input{text-align:ft;width:35%;height:30px;line-height:27px;color:#003e51;}
		    .td_input input{line-height:23px;height:23px;width:70%;}
		    .btn{height:23px;width:65px;}
		    table{width:100%;font-size:12px;background:#fff;}
		    input{border:1px solid #aecaf0;color:#003e51;}   
	    </style>
	    <script>
	        function hidetip()
	        {
	          $("#tip").hide();   
	        }
	        function onLogin()
	        {
	            var tipStr=null;
	            if(isBlank($("#name").val()))
	            {
	                tipStr="账号不能为空!";
	            }
	            if(isBlank($("#password").val()))
	            {
	                tipStr="密码不能为空!";
	            }
	            if(tipStr!=null)
	            {
		            $("#tip").find("span").html(tipStr);
		            $("#tip").show(); 
		            return;
		        }else
		        {
		            $("#loginform").submit();
		        }
	        }
	        $().ready(function (){
	            var tip=<%=tip%>;
	            if(tip!=null && tip!=undefined)
	            {
	                var tipStr="登录失败!";
	                if(tip==2)
	                    tipStr="账号不存在!";
	                if(tip==3)
	                    tipStr="密码错误!";
	                $("#tip").find("span").html(tipStr);
	                $("#tip").show();        
	            }
	        });
	    </script> 	
	</head>
	
	<body>
	<div id="main">
        <div id="title">
            <h2>管理后台登录</h2>
        </div>
        <div id="contain">
		<form  id="loginform" name="loginform" action="${ctx}/login!login.action" method="post" >
			<table align="center">
				<tr>	
					<td class="td_title">管理员</td>	
					<td class="td_input">
					    <input type="text" id="name" name="name" maxlength="50" onfocus="hidetip();" value="${name}" />
					</td>
				</tr>
				
				<tr>	
					<td class="td_title">密&nbsp;&nbsp;码</td>	
					<td class="td_input">
					    <input type="password" id="password" name="password" maxlength="50" onfocus="hidetip();" value="${password}" />
					</td>
				</tr>
				
				<tr style="display:none;" id="tip">	
					<td></td>	
					<td class="td_input">
					   提示:<span></span>
					</td>
				</tr>
				
				<tr>
					<td colspan="2" align="center">
						<input type="button" value="登陆" onclick="onLogin();" class="btn"/>
						<input type="reset" value="重置"  class="btn"/>
					</td>
				</tr>		
			</table>	
		</form>
	    </div>
	</div>    
    </body>
</html>
