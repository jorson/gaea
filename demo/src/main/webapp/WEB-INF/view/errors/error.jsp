<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page import="com.huayu.foundation.web.controller.ErrorControllerResult" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=GBK">
    <title>错误页面</title>
</head>
<body>
<h1>出错了</h1>
<%
    ErrorControllerResult result = (ErrorControllerResult) request.getAttribute("actionResult");
    if (result != null) {
        out.print(result.populate());
    } else {
        out.print("未知错误！");
    }
%>
</body>
</html>