(function ($) {

    var store = {

        pageQuery: function (data, success) {
            store.sendRequest('/role/pageQuery.do', data, success);
        },

        del: function (data, success) {
            store.sendRequest('/role/delete.do', data, success);
        },

        save: function (data, success) {
            store.sendRequest('/role/save.do', data, success);
        },

        getPurviews: function (data, success) {
            store.sendRequest('/role/getPurviews.do', data, success);

        },

        sendRequest: function (url, data, success, error, dataType) {
            $.ajax({
                url: url,
                cache: false,
                data: data,
                dataType: (dataType == null || typeof dataType == "undefined") ? "json" : dataType,
                type: 'POST',
                success: success,
                error: error
            });
        }
    };


    var empty = {
        id: '',
            name: ''
    }

    var viewModel = {

        model: {
            // 分页
            pager: {
                pageNo: 0,
                pageSize: 10,
                result: [],
                totalRecordCount: -1
            },

            // 输入界面
            role: empty
        },

        _loadList: function () {
            store.pageQuery({
                pageNo: viewModel.model.pager.pageNo(),
                pageSize: viewModel.model.pager.pageSize(),
                totalRecordCount: viewModel.model.pager.totalRecordCount()
            }, function (data) {
                ko.mapping.fromJS(data, {}, viewModel.model.pager);
                viewModel._pagination();
            })
        },

        // 处理分页
        _pagination: function () {
            $("#pagination").pagination(viewModel.model.pager.totalRecordCount(), {
                is_show_first_last: false,
                is_show_input: true,
                show_if_single_page: true,
                items_per_page: viewModel.model.pager.pageSize(),
                num_display_entries: 5,
                current_page: viewModel.model.pager.pageNo(),
                prev_text: "上一页",
                next_text: "下一页",
                callback: function (index) {
                    if (index != viewModel.model.pager.pageNo()) {
                        viewModel.model.pager.pageNo(index);
                        viewModel._loadList();
                    }
                }
            });
        },

        _initTree: function (roleId) {
            var setting = {
                check: {
                    enable: true
                },
                data: {
                    simpleData: {
                        enable: true,
                        idKey: 'id',
                        pIdKey: 'parent'
                    }
                }
            };
            store.getPurviews({roleId: roleId}, function (data) {
                $.fn.zTree.init($("#tree"), setting, data);
            });
        },

        _clear: function () {
            ko.mapping.fromJS(empty, {}, viewModel.model.role);
        },

        _showModal: function () {
            $("#addModal").modal('show');
            $("#addModal").css("top", "10%");
        },

        _hideModal: function () {
            $("#addModal").modal('hide');
            viewModel._clear();
        },

        _afterDelete: function () {
            // 检验删除后Pager的当前页是否已经被删光，如果被删光就需要查上一页的数据
            if (viewModel.model.pager.totalRecordCount() - 1
                <= viewModel.model.pager.pageNo() * viewModel.model.pager.pageSize()) {
                viewModel.model.pager.pageNo(viewModel.model.pager.pageNo() - 1);
                viewModel._loadList();
            } else {
                viewModel._loadList();
            }
        },

        init: function () {
            viewModel.model = ko.mapping.fromJS(viewModel.model);
            ko.applyBindings(viewModel);
            viewModel._loadList();
        },

        // 监听事件
        addClick: function () {
            viewModel.model.role.id(null);
            viewModel._initTree('');
            viewModel._showModal();
            $('#modal-title').html('新增角色');
        },

        editClick: function (self) {
            ko.mapping.fromJS(ko.mapping.toJS(self), {}, viewModel.model.role);
            viewModel._initTree(self.id());
            viewModel._showModal();
            $('#modal-title').html('编辑角色');
        },

        delClick: function (self) {
            if (confirm("是否删除该角色？")) {
                store.del({roleId: self.id()}, function (data) {
                    viewModel._afterDelete();
                })
                return true;
            }
        },

        cancel: function () {
            viewModel._hideModal();
        },

        save: function () {
            var success = function (data) {
                // 隐藏model
                viewModel._hideModal();
                // 刷新列表
                viewModel._loadList();
            }
            var tree = $.fn.zTree.getZTreeObj("tree");
            var role = ko.mapping.toJS(viewModel.model.role);
            if ($.trim(role.name).length == 0) {
                alert("请输入角色名称");
                return;
            }
            // 获取选中的节点
            var checkedNodes = tree.getCheckedNodes();
            if (checkedNodes.length == 0) {
                alert("请选择要赋予该角色的权限");
                return;
            }
            role.instanceKey = checkedNodes[0].domainKey;
            role.purviewKeys = [];
            $.each(checkedNodes, function (index, el) {
                role.purviewKeys.push(el.id);
            });
            store.save({json: JSON.stringify(role)}, success);
        }
    }


    $(function () {
        viewModel.init();
    });

})(jQuery)
