<%@ page contentType="text/html;charset=UTF-8" %>
<div id="hiddencontent2">
    <div class="title">
	    <h2>账号信息</h2>
	</div>
	<form id="inputForm2" name="inputForm2">
	   <div class="tablediv">
	      <table cellpadding="0" cellspacing="2" border="0" class="hiddentable" >
	          <tr>
	            <td class="td_title">登录名</td>
	            <td class="td_input"><input id="name2" name="name2" type="text" maxlength="100" class="fil" readonly="readonly" disabled="disabled" /> 
	            </td>
	          </tr>
	          <tr>
	            <td class="td_title">昵&nbsp;&nbsp;称</td>
	            <td class="td_input"><input id="nickname2" name="nickname2" type="text" maxlength="100" class="fil" readonly="readonly" disabled="disabled" /> 
	            </td>
	          </tr>
	          <tr>  
	            <td class="td_title">是否super</td>
	            <td class="td_input"><span class="preselect">
	                <select id="typevalue2" name="typevalue2" class="fil" disabled="disabled" >
	                    <option value="0" >一般</option>
                        <option value="1" >超级</option>
			        </select></span> 
	            </td>
	          </tr>
	          <tr>  
	            <td class="td_title">创建时间</td>
	            <td class="td_input">
	                <input id="createTimeStr2" name="createTimeStr2" type="text" maxlength="200" class="fil" readonly="readonly" disabled="disabled" /> 
	            </td>
	          </tr>
	          <tr>  
	            <td class="td_title">登录时间</td>
	            <td class="td_input">
	                <input id="lastLoginTimeStr2" name="lastLoginTimeStr2" type="text" maxlength="200" class="fil" readonly="readonly" disabled="disabled" /> 
	            </td>
	          </tr>
	          <tr>
	             <td colspan="2" style="text-align:center">
	                 <input type="button" class="btn" onclick="closeInfoDialog();" value="关闭" />
	             </td>
	          </tr>
	      </table>
	   </div>
	</form>
</div>
