<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/commons/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
 <title>管理平台</title>
 <script src="${ctx}/scripts/jquery.js" type="text/javascript" ></script>
 </head>
 <frameset rows="108,*,40" frameborder="no" border="0" framespacing="0" id="totalFrame">
    <frame src="commons/head.jsp" name="topFrame" scrolling="No" noresize="noresize" id="topFrame"  />
    <frame src="${ctx}/main.action" id="right" name="right" scrolling="no"  />
    <frame src="commons/bottom.jsp" name="bottomFrame" scrolling="No" noresize="noresize" id="bottomFrame" />
 </frameset>
<noframes><body>
</body>
</noframes></html>