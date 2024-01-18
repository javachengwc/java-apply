<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" errorPage="/WEB-INF/view/errorpage.html"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ page import="com.otd.boot.demo.util.EncodesUtils" %>

<%
 String basePath = EncodesUtils.encodeHtml(request.getContextPath());
%>
<html>
<head>
<c:set var="webRoot" value="<%=basePath%>" />
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<script src="<c:url value='/resources/js/jquery.min.js'/>"></script>
<script src="<c:url value='/resources/js/jquery.form.min.js'/>"></script>、
</head>
<body class=welcomebody>
	<div class=welcome>
		<span style="color: #120b55; font-size: 18px; font-weight: 600">浏览器支持：</span>
		<ul>
			<li>浏览器建议使用IE8及以上版本、chrome33以上版本</li>
		</ul>
	</div>
</body>
</html>
