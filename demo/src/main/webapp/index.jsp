<%@ page import="com.huayu.foundation.core.config.SystemConfig" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="http://com.huayu.foundation/tags/security" %>
<%@ taglib prefix="r" uri="http://com.huayu.foundation/tags/resource" %>
<html>
<head>
    <title>主页</title>
</head>
<body>
主页</br>
<s:sec value="test&&test2">
    有权限才能看
</s:sec>

<img src="<r:url value="/mooc/images/backend.png"/>"/>
</body>
</html>
