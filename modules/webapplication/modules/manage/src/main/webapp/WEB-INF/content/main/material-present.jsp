<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/commons/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>物资赠送</title>
		<script src="${ctx}/scripts/jquery.js" type="text/javascript"></script>
		<script src="${ctx}/scripts/jquery.form.js" type="text/javascript"></script>
		
		<style>
		    *{ margin:0; padding:0;}
		    #title,#contain{width:500px;height:100%;margin:0px auto;}
		    #title h2{text-align:center;font-size:14px;font-weight:bold;height:27px;line-height:27px;background:url(../images/demo_lf_menu_bj.gif) repeat-x;color:#fff;}
		    .td_title{text-align:right;height:30px;width:30%;line-height:30px;font-size:14px;color:#7a7a7a;padding-right:35px; }
		    .td_input{text-align:ft;width:70%;height:30px;line-height:30px;}
		    .td_input input{line-height:23px;height:23px;width:70%;}
		    .btn{height:23px;width:65px;}
		    table tr{background:#fff;}
		    input,textarea{border:1px solid #aecaf0;}
		</style>
		<script type="text/javascript">
		    function isNumber(str)
		    {
		        var i;
		        if(str.length==0)
		           return false;
		        for(i=0;i<str.length;i++)
		        {
		           if(str.charAt(i)<"0" || str.charAt(i)>"9")
		                 return false;
		        }
		        return true;
		    }
		    function onclose()
		    {
                window.close();
		    }
		    function onSave()
		    {  
		       if($("#playerId").val()==null ||$("#playerId").val()=="" )
		       {
		          alert("玩家不能为空!");
		          return;
		       }
		       if($("#count").val()==null ||$("#count").val()=="" )
		       {
		          alert("数量不能为空!");
		          return;
		       }
	            if(!isNumber($("#count").val()))
	            {
	                alert("数量必须是数字!");
	                return;
	            }
	            if($("#count").val()<=0)
	            {
	                alert("数量必须大于0!");
	                return;
	            }
	            var options={
		            url:"${ctx}/main/material-ext!present.action",
		            type:"POST",
		            dataType:"script",
		            success:function(msg)
		            {    
		                var result=eval(msg);
		                if(result)
		                {
		                   alert("赠送成功!");
		                   onclose();
		                  
		                }else
		                {
		                   alert("赠送失败!");
		                }
		                
		            }
		        };
		        $("#inputForm").ajaxSubmit(options);
		        
		    }
		    function selectPlayer()
		    {
		        url="${ctx}/main/player!select.action";
			    window.open(url,'','width=900,height=450,resizable=1,scrollbars=1');
		    }
		    function assignPlayer(playerId,playerName)
		    {
		        $("#playerId").val(playerId);
		        $("#playerName").val(playerName);
		    }
		</script>
	</head>
	<body>
	    <div id="title">
	        <h2>物资赠送</h2>
	    </div>
	    <div id="contain">
	    <s:form id="inputForm" name="inputForm">
	    <input type="hidden" id="combinid" name="combinid" value="<s:property value="combinid" />" />
	    <input type="hidden" id="playerId" name="playerId" />
	    <div style="text-align:center;">
	       <table cellpadding="0" cellspacing="1" border="0" style="width:500px;font-size:12px;" bgcolor="#d4d4d4">
	           <tr>
	             <td class="td_title">玩&nbsp;&nbsp;家</td>
	             <td class="td_input"><input id="playerName" name="playerName" type="text" readonly="readonly" disabled="disabled" />
	             <input type="button" class="btn" style="width:50px;" onclick="selectPlayer()" value="选择" /> 
	             </td>
	           </tr>
	           <tr>  
	             <td class="td_title">物&nbsp;&nbsp;资</td>
	             <td class="td_input"><input id="name" name="name" type="text" value="<s:property value="name" />" readonly="readonly" disabled="disabled" /> 
	             </td>
	           </tr>
	           <tr>  
	             <td class="td_title">数&nbsp;&nbsp;量</td>
	             <td class="td_input"><input id="count" name="count" type="text" value="1" /> 
	             </td>
	           </tr>
	           <tr>
	              <td colspan="2" style="text-align:center">
	                  <input type="button" class="btn" onclick="onSave()" value="提交" />
	                  <input type="button" class="btn" onclick="onclose();" value="关闭" />
	              </td>
	           </tr>
	       </table>
	    </div>
	    </s:form>
	    </div>
	</body>
</html>