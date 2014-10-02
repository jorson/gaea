<!DOCTYPE html>
<html>
<head>
    <title>错误页面</title>
</head>
<body>
<div class="hero-unit" style="margin: 10px;">
    访问页面发生了错误，错误信息：
<#if actionResult??>
${actionResult.populate()}
<#else>
    未知错误
</#if>
</div>
</body>
</html>