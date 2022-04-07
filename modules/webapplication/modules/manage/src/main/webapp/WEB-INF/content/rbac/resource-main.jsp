<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/commons/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
 <title>资源管理</title>
 </head>
 <frameset cols="200,*" frameborder="no" border="0" framespacing="0">
    <frame src="${ctx}/rbac/module!tree.action" id="left" name="left" scrolling="no"/>
    <frame src="${ctx}/rbac/module!hierarchyPage.action" id="right" name="right" scrolling="no"/>
 </frameset>
<noframes><body>
</body>
</noframes></html>