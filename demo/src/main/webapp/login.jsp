<%--
  Created by IntelliJ IDEA.
  User: ND
  Date: 14-5-30
  Time: 下午2:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>用户登录</title>
</head>
<body>
<div class="container">
    <form action="useraccess/login.do" class="form-signin" role="form" method="post">
        <input type="text" name="id" class="form-control" placeholder="请输入账号" required="" >
        </br>
        <button class="btn btn-primary " type="submit">登 陆</button>
    </form>
</div>
</body>
</html>
