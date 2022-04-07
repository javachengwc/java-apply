<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/commons/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
 <title></title>
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
 <link href="${ctx}/styles/index_css.css"  type="text/css" rel="stylesheet"/>
 <script src="${ctx}/scripts/jquery.js" type="text/javascript" ></script>
 <script src="${ctx}/scripts/jquery.form.js" type="text/javascript"></script>
 <script src="${ctx}/scripts/common/edit.js" type="text/javascript"></script>
 <style>
    #div1{ position:absolute; padding:2px; background:#e4e4e4;width:300px; height:auto; right:0px; top:30px;}
    .td_title{text-align:right;height:20px;width:15%;line-height:20px;font-size:12px;color:#003e51;padding-right:15px; }
    .td_input{text-align:left;width:35%;height:20px;line-height:20px;color:#003e51;}
    .td_input input{line-height:18px;height:18px;width:70%;}
    table{width:100%;font-size:12px;background:#fff;}
    .btn{height:20px;width:50px;}
	input{border:1px solid #aecaf0;color:#003e51;}  
 </style>

 <script type="text/javascript">
	
	function logout()
	{
	    $.ajax({
	         type:"post",
	         dataType:"text",
	         url:"${ctx}/login!logout.action",
	         success:function(msg)
	         {
	             window.top.location.href="${ctx}/login.jsp";
	         }
	    });
	    
	}
	function changePwd()
	{
	    var tipStr=null;
        
        if(isBlank($("#newPassword").val()))
        {
            tipStr="新密码不能为空!";
        }
        if(isBlank($("#rePassword").val()) || ( !isBlank($("#newPassword").val()) && $("#rePassword").val()!=$("#newPassword").val() ))
        {
            tipStr="重复密码不一致!";
        }
        if(isBlank($("#oldPassword").val()))
        {
            tipStr="旧密码不能为空!";
        }
        if(tipStr!=null)
        {
	         $("#tip").find("span").html(tipStr);
	         $("#tip").show(); 
	         return;
        }
	   
        var options={
            url:"${ctx}/rbac/adminor!changePwd.action",
            type:"POST",
            dataType:"script",
            success:function(msg)
            {    
                var result=eval(msg);
                if(result==1)
                {
                    var tipStr="修改成功!";
                    $("#tip").find("span").html(tipStr);
	                $("#tip").show(); 
                    window.setTimeout(hidePwd,1000);
                 
                }else if(result==2)
                {
                    var tipStr="密码错误!";
                    $("#tip").find("span").html(tipStr);
	                $("#tip").show(); 
                }else
                {
                   var tipStr="修改失败!";
                    $("#tip").find("span").html(tipStr);
	                $("#tip").show(); 
                }
                
            }
        };
        $("#pwdform").ajaxSubmit(options);
       
	}
	
	function hidetip()
    {
      $("#tip").hide();   
    }
    
	function showPwd()
	{
	   $("#div1").show();
	   window.top.$("#totalFrame").attr("rows","150,*,40");
	}
	
	function hidePwd()
	{
	    window.top.$("#totalFrame").attr("rows","108,*,40");
	    $("#div1").hide();
	    $("#div1").find("input").each(
	        function ()
	        {
	            if(this.type!="button" && this.type!="hidden")
	                $(this).val("");
	        }
	    );
	    hidetip();
	    $("#tip").find("span").html("");
	}
 </script>
</head>

<body>
	<div class="tf_head">
		<div class="tf_head_lf">
			<ul>
				<li class="tf_logo"></li>
				<li class="tf_mc"></li>
			</ul>
		</div>
		<div class="tf_head_rt">
			<ul>
				<li class="tf_cygn"><a href="javascript:;" onclick="showPwd();return false;">修改密码</a> | <a href="javascript:;" onclick="logout();return false;">退出</a></li>
				<li class="tf_hyy">欢迎您：${sessionScope.userNickname}</li>
			</ul>
		</div>
	</div>
	<div id="div1" style="display:none;">
        <form id="pwdform" name="pwdform" method="post">
        <input type="hidden" id="id" name="id" value="${sessionScope.userId}" />
        <table width="100%" border="0" cellspacing="2" cellpadding="" >
            <tr>
                <td class="td_title">旧密码</td>
                <td class="td_input">
                    <input  type="password" id="oldPassword" name="oldPassword" onfocus="hidetip();" />
                </td>
            </tr>
            <tr>
                <td class="td_title">新密码</td>
                <td class="td_input">
                    <input  type="password" id="newPassword" name="newPassword" onfocus="hidetip();" />
                </td>
            </tr>
            <tr>
                <td class="td_title">重复密码</td>
                <td class="td_input">
                    <input  type="password" id="rePassword" name="rePassword" onfocus="hidetip();" />
                </td>
            </tr>
            <tr id="tip" style="display:none;">
                <td class="td_title"></td>
                <td class="td_input">
                                                       提示:<span></span>
                </td>
            </tr>
            <tr>
                <td colspan="2" align="center">
						<input type="button" value="提交" onclick="changePwd();" class="btn"/>
						<input type="button" value="关闭" onclick="hidePwd();"  class="btn"/>
			    </td>
            </tr>
        </table>
        </form>
	</div>
</body>
</html>