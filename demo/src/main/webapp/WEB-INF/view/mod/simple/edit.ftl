<!DOCTYPE html>
<html>
<head>
    <title>新增简单实体</title>
    <script type="text/javascript">
        var simpleModel = {
        <#if status??>
            params: {
                status: ${status}
            }
        </#if>
        <#if model??>
            update: ${model}
        </#if>
        }
    </script>
    <#assign static=JspTaglibs["http://com.huayu.foundation/static-include-tablib"]/>
    <@static.javascript value="/hydemo/js/simple/add.js"/>
</head>
<body>
<div class="container-fluid" id="addContainer" style="margin: 10px;">
    <div class="row-fluid">
        <div class="span9">
            <form id="addForm" method="post" action="/simple/<#if model??>update<#else>add</#if>.do">
                名称：<input type="text" name="name" data-bind="value: model.update.name"/>
                <br/>
                生日：<input type="text" name="birthday" data-bind="value: model.update.birthday"/>
                <br/>
                状态：<select name="status" data-bind="options:model.params.status,
                                   optionsText:'name',optionsValue:'code',value:model.update.status">
            </select>
                <br/>
                年龄：<input type="text" name="age" data-bind="value: model.update.age"/>
                <br/>
                备注：<input type="text" name="remark" data-bind="value: model.update.remark"/>
                <br/>
                <a class="btn btn-primary" id="search" data-bind="click:<#if model??>update<#else>add</#if>">提交</a>
                <#--<input id="submit_button" type="button" value="提交" data-bind="click:add"/>-->
            </form>
        </div>
    </div>
</div>
</body>
</html>