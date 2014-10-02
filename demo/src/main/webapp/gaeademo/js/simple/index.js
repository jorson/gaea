(function ($) {

    var store = {
        getDataList: function (search, success) {
            //$('body').loading('show');
            $.ajax({
                url: "/simple/list.do",
                // contentType: "application/json; charset=utf-8",
                dataType: "json",
                type: "POST",
                //data: JSON.stringify(search),
                data: search,
                cache: false,
                async: false,
                success: success
            });
        }
    };

    var viewModel = {

        model: {
            pageIndex: 0,
            pageSize: 2,
            dataList: [],
            queryData: {}
        },

        init: function () {
            this.model = ko.mapping.fromJS(this.model);
            //GAEA.showProps(this);
            ko.applyBindings(this);
            this.getDataList();
        },
        deleteconf: function (id) {
            $.fn.udialog.confirm("确认删除吗(本操作不可恢复)?",
                [
                    { text: "取消", click: function () {
                        $(this).udialog("destroy");
                        $(this).remove();
                        window.location.hash = "";
                    } },
                    {
                        text: "确定", click: function () {
                        $(this).udialog("destroy");
                        $(this).remove();
                        window.location.href = "http://" + window.location.hostname + ":" + location.port + webName + "/apiConfSet/delete/" + id + ".do";
                    },
                        'class': 'default-btn'
                    }
                ]);
        },
        getDataList: function () {
            var self = this;
            var qdata = ko.mapping.fromJS(this.model.queryData);
            $.extend(qdata, {pageIndex: self.model.pageIndex(), pageSize: self.model.pageSize()});
            //alert(qdata);
            store.getDataList(qdata, function (data) {
                //GAEA.showProps(data.data, 2);
                ko.mapping.fromJS(data.data, {}, self.model.dataList);
                self.page(data.totalRecordCount, self.model.pageIndex(), self.model.pageSize());
//                /$("body").loading("hide");
            });
        },

        page: function (totalCount, pageIndex, pageSize) {
            var self = this;
            //alert(totalCount + "," + pageIndex + "," + pageSize);
            $("#pagination").pagination(totalCount, {
                is_show_first_last: false,
                is_show_input: true,
                show_if_single_page: true,
                items_per_page: pageSize,
                num_display_entries: 5,
                current_page: pageIndex,
                prev_text: "上一页",
                next_text: "下一页",
                callback: function (index) {
                    if (index != pageIndex) {
                        self.model.pageIndex(index);
                        self.getDataList();
                    }
                }
            });
        },
        search: function () {
            this.getDataList();
        }
    }

    $(function () {
        $("#tab-apiConfSet").addClass("active");
        viewModel.init();
        var app = $.sammy(function () {
            this.get('#/', function () {
            });
            this.get('#/delete/:id', function () {
                viewModel.deleteconf(this.params['id']);
            });
        });
        app.run("#/");


    });
})(jQuery)
