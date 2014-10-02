<!DOCTYPE html>
<html>
<head>
<#assign r=JspTaglibs["http://com.huayu.foundation/tags/resource"]/>
    <title>简单实体列表</title>
<@r.js value="/hydemo/js/simple/index.js"/>
</head>
<body>
<div id="listContainer" style="margin: 5px;">
    <div class="navbar main-header">
        <div class="navbar-title">
            <ul class="breadcrumb no-radius">
                <li class="active">简单实体列表</li>
            </ul>
        </div>
    </div>
    <img src=<@r.url value="/mooc/images/backend.png"/>/>

    <div class="row-fluid">
        <div class="span12">
            <form class="well form-inline" onsubmit="javascript:return false;">
                <label class="control-label">
                    用户名称：<input id="dev-name" type="text" class="input-medium" placeholder=""
                                data-bind="value:model.queryData.name">
                </label>
                &nbsp;
                <a class="btn btn-primary" id="search" data-bind="click:search">查询</a>
                <a class="btn btn-primary" id="add" href="/simple/toAdd.do">添加</a>
            </form>
        </div>
    </div>
<#--<div style="align-content:"><a href="/simple/toAdd.do">增加</a></div>-->
    <div>
        <table class="table table-bordered table-striped">
            <thead>
            <tr>
                <td>用户名称</td>
                <td>生日</td>
                <td>年龄</td>
                <td>备注</td>
                <td>操作</td>
            </tr>
            </thead>
            <tbody data-bind="foreach:model.dataList,visible:model.dataList().length&gt;0">
            <tr>
                <td>
                    <span data-bind="text:name"></span>
                </td>
                <td>
                </td>
                <td data-bind="text:age">
                </td>
                <td data-bind="text:remark">
                </td>
                <td><a data-bind="attr:{href:'/simple/update-' + id() + '.do'}">修改</a>&nbsp;<a
                        data-bind="attr:{href:'/simple/delete-' + id() + '.do'}">删除</a></td>
            </tr>
            </tbody>
            <tbody>
            <tr>
                <td colspan="5"
                    style="overflow: hidden; padding:8px; font-weight: bolder; font-size: 14px; text-align: center;"
                    data-bind="visible:model.dataList().length&lt;=0">没有用户笔记数据
                </td>
            </tr>
            </tbody>
        </table>
        <!--反馈列表 end-->

        <div id="pagination"></div>
    </div>
<#--<div id="page"></div>-->
</div>

<script type="text/javascript">

</script>
</body>
</html>