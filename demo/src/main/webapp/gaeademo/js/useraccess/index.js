(function ($) {

    var store = {

        pageQuery: function (data, success) {
            store.sendRequest('/useraccess/pageQuery.do', data, success);
        },

        del: function (data, success) {
            store.sendRequest('/useraccess/delete.do', data, success);
        },

        save: function (data, success) {
            store.sendRequest('/useraccess/save.do', data, success);
        },

        getPurviews: function (data, success) {
            store.sendRequest('/useraccess/getPurviews.do', data, success);
        },

        getSelected: function(data, success) {
            store.sendRequest('/useraccess/getRoles.do', data, success);
        },

        getToSelect: function(data, success) {
            store.sendRequest('/useraccess/getAllRoles.do', data, success);
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
        id: ''
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

            // 编辑界面
            editEntity: empty,

            //==========================================================================================================
            // 待选择项
            toSelect: [],

            // 已选择项
            selected: []
        },

        // 记载列表
        _loadList: function () {
            var query = {
                pageNo: viewModel.model.pager.pageNo(),
                pageSize: viewModel.model.pager.pageSize(),
                totalRecordCount: viewModel.model.pager.totalRecordCount()
            }
            store.pageQuery(query, function (data) {
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

        _showModal: function () {
            $("#addModal").modal('show');
            $("#addModal").css("top", "10%");
        },

        _hideModal: function () {
            $("#addModal").modal('hide');
            ko.mapping.fromJS(empty, {}, viewModel.model.editEntity);
            ko.mapping.fromJS([], {}, viewModel.model.selected);
            var toSelect = ko.mapping.toJS(viewModel.model.toSelect);
            $.each(toSelect, function (i, el) {
                el.selected = false;
            });
            ko.mapping.fromJS(toSelect, {}, viewModel.model.toSelect);
        },

        init: function () {
            viewModel.model = ko.mapping.fromJS(viewModel.model);
            viewModel._loadList();
            ko.applyBindings(viewModel);
        },

        add: function () {
            viewModel.model.editEntity.id(null);
            viewModel.getToSelect();
            viewModel._showModal();
            $('#modal-title').html('新增账号');
        },

        edit: function (self) {
            ko.mapping.fromJS(ko.mapping.toJS(self), {}, viewModel.model.editEntity);
            viewModel.getToSelect();
            viewModel.getSelected(self.id());
            viewModel._showModal();
            $('#modal-title').html('编辑账号');
        },

        del: function (self) {
            if (confirm("是否删除该账号？")) {
                var delData = {
                    userId: self.id()
                };
                store.del(delData, function (data) {
                    // 检验删除后Pager的当前页是否已经被删光，如果被删光就需要查上一页的数据
                    if (viewModel.model.pager.totalRecordCount() - 1
                        <= viewModel.model.pager.pageNo() * viewModel.model.pager.pageSize()) {
                        viewModel.model.pager.pageNo(viewModel.model.pager.pageNo() - 1);
                        viewModel._loadList();
                    } else {
                        viewModel._loadList();
                    }
                })
            }
        },

        cancel: function () {
            viewModel._hideModal();
        },

        save: function () {
            var entity = ko.mapping.toJS(viewModel.model.editEntity);
            if ($.trim(entity.id).length == 0) {
                alert("请输入账号名称");
                return;
            }
            var selected = ko.mapping.toJS(viewModel.model.selected);
            if(selected.length == 0) {
                alert("请选择与账号管理的角色");
                return;
            }
            var roleIds = [];
            $.each(selected, function(i,el){
                roleIds.push(el.id);
            })
            var saveData = {
                userId: viewModel.model.editEntity.id(),
                roleIds: roleIds,
                instanceKey: viewModel.model.toSelect()[0].instanceKey()
            };
            store.save(saveData, function () {
                viewModel._hideModal();
                viewModel._loadList();
            });
        },
        //==============================================================================================================
        select: function(self) {
            var selected = self.selected();
            if (!selected) {
                var arr = ko.mapping.toJS(viewModel.model.selected);
                arr.push(ko.mapping.toJS(self));
                ko.mapping.fromJS(arr, {}, viewModel.model.selected);
            } else {
                var arr = ko.mapping.toJS(viewModel.model.selected);
                arr = $.grep(arr, function (n, i) {
                    return n.id != self.id();
                });
                ko.mapping.fromJS(arr, {}, viewModel.model.selected);
            }
            self.selected(!selected);
        },

        unSelect: function(self){
            var arr = ko.mapping.toJS(viewModel.model.selected);
            arr = $.grep(arr, function (n, i) {
                return n.id != self.id();
            });
            ko.mapping.fromJS(arr, {}, viewModel.model.selected);

            //去除勾选
            arr = ko.mapping.toJS(viewModel.model.toSelect);
            $.each(arr, function (i, n) {
                if (n.id == self.id()) {
                    n.selected = false;
                }
            });
            ko.mapping.fromJS(arr, {}, viewModel.model.toSelect);
        },

        getSelected: function(userId) {
           store.getSelected({userId: userId},function(selected) {
               $.each(selected, function (i, selectedEl) {
                   $.extend(selectedEl, {selected: true});
                   var toSelect = ko.mapping.toJS(viewModel.model.toSelect);
                   $.each(toSelect, function (j, toSelectEl) {
                       if (selectedEl.id == toSelectEl.id) {
                           toSelectEl.selected = true;
                       }
                   });
                   ko.mapping.fromJS(toSelect, {}, viewModel.model.toSelect);
               });
               ko.mapping.fromJS(selected, {}, viewModel.model.selected);
           })
        },

        getToSelect: function() {
            store.getToSelect({}, function(data) {
                $.each(data, function (i, n) {
                    $.extend(n, {selected: false});
                });
                ko.mapping.fromJS(data, {}, viewModel.model.toSelect);
            })
        }
    }


    $(function () {
        viewModel.init();
    });

})(jQuery)
