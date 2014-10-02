(function ($) {
    var store = {
        confParam: function (success) {
            $.ajax({
                url: '/simple/add.do',//用户Id待定
                dataType: 'json',
                cache: false,
                success: success
            });
        },
        save: function (data, success) {
            $.ajax({
                url: "/simple/add.do",
                type: "post",
                dataType: 'json',
                data: data,
                cache: false,
                success: success
            })
        },
        update: function (data, success) {
            $.ajax({
                url: "/simple/update.do",
                type: "post",
                dataType: 'json',
                data: data,
                cache: false,
                success: success
            })
        }
    };

    var viewModel = {
        model: {
            params: {
                status: [
                    {"code": "1", "name": "是"},
                    {"code": "0", "name": "否"}
                ]
            },
            update: {
                status: 1,
                age: 18,
                name: "",
                birthday: "",
                remark: ""
            }
        },

        init: function () {
            var self = this;
            $.extend(this.model,simpleModel);
            //this.model.params = simpleModel.params;
            this.model = ko.mapping.fromJS(this.model);
            // ko.applyBindings(this, $('#addContainer')[0]);
            ko.applyBindings(this);
            // self.confParam();
            return self;
        },
        confParam: function () {
            var self = this;
            store.confParam(function (data) {
                ko.mapping.fromJS(data, {}, self.model.confParam);
            });
        },
        add: function () {
            var data = ko.mapping.toJS(this.model.update);
            store.save(data, function (ret) {
                GAEA.showProps(ret);
                if (ret && ret["code"] == 1) {
                    alert("添加成功");
                } else {
                    alert("添加失败," + (ret ? ret["message"] : "未知错误"));
                }

            });
            //GAEA.showProps(data);
        },
        update: function () {
            var data = ko.mapping.toJS(this.model.update);
            store.update(data, function (ret) {
                if (ret && ret["code"] == 1) {
                    alert("修改成功");
                } else {
                    alert("修改失败," + (ret ? ret["message"] : "未知错误"));
                }

            });
            //GAEA.showProps(data);
        }

    }

    $(function () {
        viewModel.init();
        var app = $.sammy(function () {
            this.get('#/', function () {
            });
        });
        app.run("#/");
    });
})(jQuery)