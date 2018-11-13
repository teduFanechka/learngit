//参考文档::::::::加载工具条

(function ($) {
    var btnData = {
        'btnID': '',
        'btnIcon': '',
        'btnValue': ''
    };
    var linkbuttonTemplate = '<a href=\"javascript:void(0)\" id=\"${btnID}\" class=\"easyui-linkbutton\" data-options=\"plain:true,iconCls:\'${btnIcon}\'\">${btnValue}</a>';
    var divTemplate = '<div style=\"background:#C9EDCC;width:100%;\">${controls}</div>';
    var mycontrols = '';

    var methods = {
        init: function (options) {
            var $this = $(this);

            var settings = $.extend({
                id: $this.attr('id'),
                controls: ''
            }, options);

            var controlArray = settings.controls.split(',');
            if (controlArray.length == 0) return;
            for (var i = 0; i < controlArray.length; i++) {
                switch (controlArray[i]) {
                    case 'add':
                        btnData.btnID = 'linkbutton_add';
                        btnData.btnIcon = 'icon-add';
                        btnData.btnValue = '新增';
                        break;
                    case 'edit':
                        btnData.btnID = 'linkbutton_edit';
                        btnData.btnIcon = 'icon-edit';
                        btnData.btnValue = '编辑';
                        break;
                    case 'delete':
                        btnData.btnID = 'linkbutton_delete';
                        btnData.btnIcon = 'icon-cancel';
                        btnData.btnValue = '删除';
                        break;
                    default:
                        btnData.btnID = '';
                        btnData.btnIcon = '';
                        btnData.btnValue = '';
                }
                itembtn = linkbuttonTemplate;
                for (var d in btnData) {
                    itembtn = itembtn.replace(new RegExp('\\$\\{' + d + '\\}', 'g'), btnData[d]);
                }
                mycontrols += itembtn;
            }
            divTemplate = divTemplate.replace(new RegExp('\\$\\{controls\\}', 'g'), mycontrols);
            $this.append(divTemplate);
            $.parser.parse('#' + $this.attr('id'));

            if (settings.onAdd) {
                $('#linkbutton_add').bind('click', function () {
                    settings.onAdd.apply($this, arguments);
                });
            }
        },

        setControl: function (control) {
            $('#linkbutton_add').bind('click', control["add"].onClick);
            $('#linkbutton_edit').bind('click', control["edit"].onClick);

        }
    }

    $.fn.toolsbar = function (method) {

        if (methods[method]) {
            return methods[method].apply(this, Array.prototype.slice.call(arguments, 1));
        } else if (typeof method === 'object' || !method) {
            return methods.init.apply(this, arguments);
        } else {
            $.error(method + '参数错误');
        }

    };
})($);