<%@ page import="com.huayu.foundation.core.config.SystemConfig" %>
<%@ page contentType="text/html; charset=utf-8" language="java" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <jsp:include page="/pages/css_resource.jsp"></jsp:include>
    <title><decorator:title default='默认标题'/></title>
    <decorator:head/>
    <jsp:include page="/pages/js_resource.jsp"></jsp:include>
</head>
<body>
<decorator:body/>
</body>
</html>