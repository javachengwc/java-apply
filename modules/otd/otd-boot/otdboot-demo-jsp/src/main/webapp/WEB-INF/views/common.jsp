<%@ page language="java" pageEncoding="UTF-8" errorPage="/WEB-INF/view/errorpage.html"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ page import="com.lenovo.tms.common.EncodesUtils" %>
<%
String basePath = EncodesUtils.encodeHtml(request.getContextPath());
request.setAttribute("basePath", basePath);
%>
<head>
		<meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
		<script src="<c:url value='/resources/js/jquery.min.js'/>"></script>
		<script src="<c:url value='/resources/js/jquery.form.min.js'/>"></script>
		<link rel="stylesheet" href="<c:url value='/resources/js/themes/icon.css'/>" />
 </head>
<c:set var="webRoot" scope="request" value="<%=basePath%>" />
<script type="text/javascript">
     var webRoot = "<v:out type="basePath"/>" ;
     alert(webRoot);
</script>
