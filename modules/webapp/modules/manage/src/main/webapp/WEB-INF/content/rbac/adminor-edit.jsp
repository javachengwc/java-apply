<%@ page contentType="text/html;charset=UTF-8" %>
<div id="hiddencontent">
    <div class="title">
	    <h2>账号新增</h2>
	</div>
	<form id="inputForm" name="inputForm" method="post">
	<input type="hidden" id="id" name="id" />
	   <div class="tablediv">
	      <table cellpadding="0" cellspacing="2" border="0" class="hiddentable" >
	          <tr>
	            <td class="td_title">登录名</td>
	            <td class="td_input"><input id="name" name="name" type="text" maxlength="100" class="fil"/> 
	            </td>
	          </tr>
	          <tr>
	            <td class="td_title">昵&nbsp;&nbsp;称</td>
	            <td class="td_input"><input id="nickname" name="nickname" type="text" maxlength="100" class="fil"/> 
	            </td>
	          </tr>
	          <tr>  
	            <td class="td_title">是否super</td>
	            <td class="td_input"><span class="preselect">
	                <select id="typevalue" name="typevalue" class="fil">
	                    <option value="0" >一般</option>
                        <option value="1" >超级</option>
			        </select></span> 
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