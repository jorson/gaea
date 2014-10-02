<%@ page import="org.springframework.security.core.context.SecurityContextHolder" %>
<%@ page import="com.huayu.foundation.security.access.User" %>
<%--
  Created by IntelliJ IDEA.
  User: jsc
  Date: 14-6-6
  Time: 上午11:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>主页</title>
</head>
<body>
用户中心
<%
    Object o = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    // 已登录
    if(o instanceof User){
        User user = (User) o;
        out.print(user.getId());
        out.print(user.getName());
    }else{ // 匿名登录
        out.print(" 匿名登录");
    }
%>
<a href="${pageContext.request.contextPath}/logout">注销</a> </br>
<button onclick="location.href='${pageContext.request.contextPath}/purview.do'">查看权限列表</button>
</body>
</html>
