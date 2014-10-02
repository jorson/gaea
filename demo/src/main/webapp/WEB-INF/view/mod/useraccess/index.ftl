<!DOCTYPE html>
<html>
<head>
<#assign r=JspTaglibs["http://com.huayu.foundation/tags/resource"]/>
<@r.css value="/addins/jquery/ztree/v3.3/css/ztreestyle/ztreestyle.css"/>
    <title>权限管理DEMO</title>
</head>
<body>

<!-- 账号列表 -->
<div class="row-fluid">
    <div class="span12">
        <button type="button" class="btn btn-primary" data-bind="event:{'click':add}">添加账号</button>
    </div>
    <div>
        <table class="table table-bordered">
            <thead>
            <tr>
                <th style="width:50%;">账号</th>
                <th style="width:50%;">操作</th>
            </tr>
            </thead>
            <tbody data-bind="visible: model.pager.result().length > 0, foreach:model.pager.result" style="display: none">
            <tr>
                <td data-bind="text:id"></td>
                <td>
                    <button type="button" class="btn btn-primary" data-bind="event:{'click':$parent.edit}">编辑账号
                    </button>
                    &nbsp;
                    <button type="button" class="btn btn-primary" data-bind="event:{'click':$parent.del}">删除账号
                    </button>
                </td>
            </tr>
            </tbody>
            <tbody data-bind="visible:model.pager.result().length==0" style="display: none">
            <tr>
                <td colspan="6" style="text-align: center;">暂无数据</td>
            </tr>
            </tbody>
        </table>
    </div>
    <!-- 分页 -->
    <div id="pagination"></div>
</div>


<!-- 账号添加/编辑弹出框 -->
<div class="modal" id="addModal" style="display: none;" data-backdrop="static">
    <div class="modal-header">
        <a class="close" data-dismiss="modal" data-bind="click:cancel">&times;</a>

        <h3 id="modal-title"></h3>
    </div>

    <div class="modal-body">
        <div class="row-fluid">
            <form class="form-horizontal">
                <div class="control-group">
                    <label class="control-label">账号</label>

                    <div class="controls">
                        <input type="text" class="input-large" id="id" name="id" data-bind="value: model.editEntity.id"
                               required/>
                    </div>
                </div>
            </form>
            <div class="row-fluid">

            <#--当前可选角色-->
                <div class="span6">
                    <table class="table table-bordered" style="background: #3bbcc4; color: #fff; font-size: 15px;">
                        <tr>
                            <td><strong style="margin-left: 80px;">当前可选角色</strong></td>
                        </tr>
                    </table>
                    <table class="table table-hover table-bordered">
                        <tbody data-bind="foreach: model.toSelect">
                        <tr>
                            <td data-bind="click: $parent.select">
                                <em data-bind="text: name"></em>
                                <i data-bind="visible: selected" style="float: right" class="icon-ok"></i>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>

            <#--账号关联角色-->
                <div class="span6">
                    <table class="table table-bordered" style="background: #3bbcc4; color: #fff; font-size: 15px;">
                        <tr>
                            <td><strong style="margin-left: 80px; ">账号关联的角色</strong></td>
                        </tr>
                    </table>
                    <table class="table table-bordered" data-bind="foreach: model.selected">
                        <tr>
                            <td>
                                <em data-bind="text: name"></em>
                                <i style="float: right" class="icon-remove" data-bind="click: $parent.unSelect"></i>
                            </td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>
    </div>
    <div class="modal-footer">
        <button type="button" class="btn btn-primary" data-bind="event:{'click':save}">确定</button>
        <a href="#" data-bind="event:{'click':cancel}" class="btn btn-default" data-dismiss="modal">取消</a>
    </div>
</div>

<@r.js value="/addins/jquery/ztree/v3.3/js/jquery.ztree.all-3.3.min.js"/>
<script type="text/javascript" src="/hydemo/js/useraccess/index.js"></script>
</body>
</html>