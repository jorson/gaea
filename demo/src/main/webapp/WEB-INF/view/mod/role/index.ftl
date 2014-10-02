<!DOCTYPE html>
<html>
<head>
<#assign r=JspTaglibs["http://com.huayu.foundation/tags/resource"]/>
<@r.css value="/addins/jquery/ztree/v3.3/css/ztreestyle/ztreestyle.css"/>
    <title>权限管理DEMO</title>
</head>
<body>

<!-- 角色列表 -->
<div class="row-fluid">
    <div class="span12">
        <button type="button" class="btn btn-primary" data-bind="event:{'click':addClick}">添加角色</button>
    </div>
    <div>
        <table class="table table-bordered">
            <thead>
            <tr>
                <th style="width:50%;">角色名称</th>
                <th style="width:50%;">操作</th>
            </tr>
            </thead>
            <tbody data-bind="visible: model.pager.result().length > 0, foreach:model.pager.result" style="display: none">
            <tr>
                <td data-bind="text:name"></td>
                <td>
                    <button type="button" class="btn btn-primary" data-bind="event:{'click':$parent.editClick}">编辑角色
                    </button>
                    &nbsp;
                    <button type="button" class="btn btn-primary" data-bind="event:{'click':$parent.delClick}">删除角色
                    </button>
                </td>
            </tr>
            </tbody>
            <tbody data-bind="visible:model.pager.result().length==0">
            <tr>
                <td colspan="6" style="text-align: center;">暂无数据</td>
            </tr>
            </tbody>
        </table>
    </div>
    <!-- 分页 -->
    <div id="pagination"></div>
</div>


<!-- 角色添加/编辑弹出框 -->
<div class="modal" id="addModal" style="display: none;" data-backdrop="static">
    <div class="modal-header">
        <a class="close" data-dismiss="modal" data-bind="click:cancel">&times;</a>

        <h3 id="modal-title"></h3>
    </div>

    <div class="modal-body">
        <div class="row-fluid">
            <form class="form-horizontal">
                <div class="control-group">
                    <label class="control-label">名称</label>

                    <div class="controls">
                        <input type="text" class="input-large" id="name" name="name" data-bind="value: model.role.name"
                               required/>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">权限</label>

                    <div class="controls">
                        <div id="tree" class="ztree"></div>
                    </div>
                </div>
            </form>
        </div>
    </div>
    <div class="modal-footer">
        <button type="button" class="btn btn-primary" data-bind="event:{'click':save}">确定</button>
        <a href="#" data-bind="event:{'click':cancel}" class="btn btn-default" data-dismiss="modal">取消</a>
    </div>
</div>
<@r.js value="/addins/jquery/ztree/v3.3/js/jquery.ztree.all-3.3.min.js"/>
<script type="text/javascript" src="/hydemo/js/role/index.js"></script>
</body>
</html>