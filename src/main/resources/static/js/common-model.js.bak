//------------------------------------------
//
// 自动化编辑查看代码
//
// -----------------------------------------

function _generateAttribute(obj) {
    if (!obj) return "";
    var s = [];
    for (var o in obj) {
        var v = obj[o];
        if (v !== undefined && v !== null) {
            s.push(o + '="' + v + '"');
        }
    }
    return s.length > 0 ? s.join(" ") : "";
}

function _generateTagAttribute(tag, obj) {
    var s = [];
    if (obj) {
        for (var o in obj) {
            var v = obj[o];
            if (v !== undefined && v !== null) {
                s.push(o + '="' + v + '"');
            }
        }
    }
    s = s.length > 0 ? s.join(" ") : "";
    return "<" + tag + " " + s + "></" + tag + ">";
}

function generateToolBar(toolBtn) {
    var html = "";
    if (toolBtn) {
        if (!$.isArray(toolBtn)) {
            toolBtn = [toolBtn];
        }

        toolBtn.sort(function(a, b) {
            return (a.order > b.order) ? 1 : -1;
        });

        toolBtn.forEach(function(b) {
            var a = b.showIn == 'view' ? ' tonto-model-tool-view-btn' : (b.showIn == 'edit' ? ' tonto-model-tool-edit-btn' : '');
            if (b.name) {
                html += '<a class="btn' + a + '" id="' + b.id + '" href="javascript:void(0)">' + (b.icon ? '<i class="' + b.icon + '"></i>' : '') + b.name + '</a>\n';
            } else {
                html += '<button id="' + b.id + '" type="button" class="btn btn-box-tool' + a + '"><i class="' + b.icon + '"></i></button>'
            }
        });
    }
    return html
}

function generateBox(options, content) {
    var id = options.id,
        name = options.name,
        columns = options.columns,
        icon = options.icon,
        editable = options.editable !== false,
        borderClass = options.boxHeaderClass || 'box-header no-border',
        boxStyle = options.boxStyle,
        boxHeaderStyle = options.boxHeaderStyle,
        boxClass = options.boxClass || 'box box-widget',
        toolBtn = options.toolBtn || [];

    var html = '<div id="' + id + '_container" class="' + boxClass + '" ' + (boxStyle ? 'style="' + boxStyle + '"' : '') + '>\n';

    if (options.hearderBox !== false) {

        if (editable) {
            if (toolBtn.length > 0 && !toolBtn[0].name) {
                toolBtn.push({
                    id: id + '_edit_btn',
                    icon: 'fa fa-edit',
                    showIn: 'view',
                    order: -1
                });
            } else {
                toolBtn.push({
                    id: id + '_edit_btn',
                    icon: 'fa fa-edit',
                    showIn: 'view',
                    name: '编辑',
                    order: -1
                });
            }
        }

        html += '<div class="' + borderClass + '" ' + (boxHeaderStyle ? 'style="' + boxHeaderStyle + '"' : '') + '>\n' +
            (icon ? '<i class="' + icon + '"></i>\n' : '') +
            (name ? '<h3 class="box-title">' + name + '</h3>\n' : '<h3 class="box-title"> </h3>') +
            '    <div class="box-tools pull-right">\n';
        html += generateToolBar(toolBtn);
        html += '    </div>\n' +
            '</div>\n';
    }

    html += content;
    html += '</div>\n';

    return html;
}

function generateHtml(options) {
    return generateBox(options, generateViewFormHtml(options) + generateEditFormHtml(options, true));
}

function generateEditHtml(options) {
    options.editable = options.editable === false ? false : true;
    return generateBox(options, generateEditFormHtml(options));
}

function generateEditFormHtml(options, hide) {
    var id = options.id,
        columns = options.columns,
        bodyClass = options.editBodyClass || 'box-body',
        formClass = options.editFormClass;

    var html =
        '<div id="' + id + '_edit" class="' + bodyClass + '" ' + (hide == true ? 'style="display: none"' : '') + '>\n' +
        '   <form id="' + id + '_form" action="' + options.url + '" method="post" class="form-horizontal' + (formClass ? ' ' + formClass : '') + '">\n';

    var defaultConfig = {
            maxColspan: 2,
            firstLabelSize: 3,
            inputSize: 3,
            labelSize: 2,
            server: true
        },
        currentColspan = 0;

    options = $.extend(defaultConfig, options);

    for (var i = 0; i < columns.length;) {
        var column = columns[i++],
            fieldBuilder = _FieldBuilderContainer[column.inputType],
            colspan = fieldBuilder.generateEditFormColspan(column, options);

        // 附件独占一行
        if (currentColspan + colspan <= options.maxColspan) {
            if (currentColspan == 0) {
                html += '<div class="form-group">\n';
            }

            if (column.editable === false) {
                result = fieldBuilder.generateViewFormHtml(column, currentColspan == 0 ? true : false, options);
            } else {
                result = fieldBuilder.generateEditFormHtml(column, currentColspan == 0 ? true : false, options);
            }

            html += result.html;

            if (result.colspan == 0) {
                continue;
            } else if (result.back === true) {
                i--;
                currentColspan = currentColspan > 0 ? maxColspan : 0;
            } else {
                currentColspan += result.colspan;
            }
        } else {
            i--;
            if (currentColspan == 0) {
                console && console.log("域[name:" + column.name + "]生成colspan大于最大colspan");
                continue;
            } else {
                currentColspan = options.maxColspan;
            }
        }

        if (currentColspan >= options.maxColspan) {
            html += '</div>\n';
            currentColspan = 0;
        }
    }

    if (currentColspan > 0) {
        html += '</div>\n';
        currentColspan = 0;
    }

    options.formButtonBar = options.formButtonBar || [];
    if (options.submitBtn !== false) {
        options.formButtonBar.push({
            id: id + '_form_submit_btn',
            type: options.server === false ? 'button' : 'submit',
            name: options.submitBtnName || '保存',
            class: options.submitBtnClass || 'btn btn-primary btn-block',
            order: -1
        });
    }

    if (options.cancelBtn !== false) {
        options.formButtonBar.push({
            id: id + '_form_cancel_btn',
            type: 'button',
            name: options.cancelBtnName || '取消',
            class: options.cancelBtnClass || 'btn btn-default btn-block',
            order: 9999
        });
    }

    options.formButtonBar.sort(function(a, b) {
        return (a.order > b.order) ? 1 : -1;
    });

    if (options.formButtonBar.length > 0) {
        var formButtonBarClass = options.formButtonBarClass === false ? null : (options.formButtonBarClass || 'form-button-bar');
        html += '<div class="form-group' + (formButtonBarClass ? ' ' + formButtonBarClass : '') + '">\n';
        var firstBtn = true,
            btnWidth = options.formButtonBar.length > 2 ? 'col-sm-1' : 'col-sm-2';
        options.formButtonBar.forEach(function(a) {
            html += firstBtn ? '<div class="' + btnWidth + ' col-sm-offset-3">\n' : '<div class="' + btnWidth + ' col-sm-offset-1">\n';
            html += '<button type="' + a.type + '" id="' + a.id + '" class="' + a.class + '">' + a.name + '</button>\n';
            html += '</div>\n';

            firstBtn = false;
        });

        html += '</div>\n';
    }

    html +=
        '</form>\n' +
        '</div>\n';
    return html;
}

function generateViewHtml(options) {
    options.editable = options.editable === true ? true : false;
    return generateBox(options, generateViewFormHtml(options));
}

function generateViewFormHtml(options) {
    var id = options.id,
        columns = options.columns,
        bodyClass = options.viewBodyClass || 'box-body';


    var html =
        '<div id="' + id + '_view" class="' + bodyClass + '">\n' +
        '    <form class="form-horizontal">\n';

    var defaultConfig = {
            maxColspan: 2,
            firstLabelSize: 3,
            inputSize: 3,
            labelSize: 2
        },
        currentColspan = 0;

    options = $.extend(defaultConfig, options);

    for (var i = 0; i < columns.length;) {
        var column = columns[i++],
            fieldBuilder = _FieldBuilderContainer[column.inputType],
            colspan = fieldBuilder.generateViewFormColspan(column, options);

        // 附件独占一行
        if (currentColspan + colspan <= options.maxColspan) {
            if (currentColspan == 0) {
                html += '<div class="form-group">\n';
            }

            result = fieldBuilder.generateViewFormHtml(column, currentColspan == 0 ? true : false, options);
            html += result.html;

            if (result.colspan == 0) {
                continue;
            } else if (result.back === true) {
                i--;
                currentColspan = currentColspan > 0 ? maxColspan : 0;
            } else {
                currentColspan += result.colspan;
            }
        } else {
            i--;
            if (currentColspan == 0) {
                console && console.log("域[name:" + column.name + "]生成colspan大于最大colspan");
                continue;
            } else {
                currentColspan = options.maxColspan;
            }
        }

        if (currentColspan >= options.maxColspan) {
            html += '</div>\n';
            currentColspan = 0;
        }
    }

    if (currentColspan > 0) {
        html += '</div>\n';
        currentColspan = 0;
    }

    html +=
        '   </form>\n' +
        '</div>\n';
    return html;
}

var _Model = function(name, column, options) {
    var that = this;
    that.name = name;
    options = options || {};
    that.container = options.container || $("#" + name + "_container");

    if (that.container.length == 0) {
        that.container = $("body");
    }

    that.editBtn = $("#" + name + "_edit_btn");
    that.status = "view";
    that.viewBody = $("#" + name + "_view");
    that.editBody = $("#" + name + "_edit");
    that.formSubmitBtn = $("#" + name + "_form_submit_btn");
    that.formCancelBtn = $("#" + name + "_form_cancel_btn");
    that.formBody = $("#" + name + "_form");

    that.editBtn.click(function() {
        that.toEdit();
    });

    that.formCancelBtn.click(function() {
        that.toView();
    });

    that.columns = column;

    // 注入域构建器
    if (that.columns) {
        that.columns.forEach(function(column) {
            column.fieldBuilder = _FieldBuilderContainer[column.inputType];
            if (!column.fieldBuilder && console) {
                console.log("找不到对应的域构造器[inputType:" + column.inputType + "]");
            }

            column.viewDisplay = column.viewDisplay || "show";
            column.editDisplay = column.editDisplay || "show";
        });
    }

    that.config = $.extend({
        pattern: "normal", // edit:只能编辑,view:只能查看
        successCallback: function(data) {
            $.successMessage("保存成功");
            that.setData(data)
            that.toView();
        }
    }, options);


    // 如果非服务端
    if (typeof that.config.submitClick === 'function') {
        that.formSubmitBtn.click(function() {
            that.config.submitClick(that);
        });
    } else if (options.server === false) {
        that.formSubmitBtn.click(function() {
            if (that.formBody.valid()) {
                var d = that.getFormData();
                that.setData(d);
                that.toView();
            }
        });
    }

    // 创建表单提交
    if (that.formBody) {
        that.formBody.createForm({
            beforeCallback: function(formData) {
                var extraParam = that.config.extraParam;
                if (extraParam) {
                    if (typeof extraParam === 'function') {
                        extraParam = extraParam();
                    }

                    for (var o in extraParam) {
                        formData.push({
                            name: o,
                            value: extraParam[o],
                            type: "text",
                            required: false
                        });
                    }
                }

                for (var k = 0; k < that.columns.length; k++) {
                    if (that.columns[k].fieldBuilder.formDataHandler(that.columns[k], formData, that) === false) {
                        return false;
                    }
                }

                var beforeSubmit = that.config.beforeSubmit;
                if (beforeSubmit && typeof beforeSubmit === 'function') {
                    return beforeSubmit(formData);
                }
            },
            successCallback: that.config.successCallback
        });
    }

    // 初始化依赖关系
    that.dependency = {};
    if (that.columns) {
        that.columns.forEach(function(column) {
            if (column.dependency) {
                that.dependency[column.name] = [{
                    target: column.name,
                    dependColumn: column.dependency[0],
                    dependValue: column.dependency.slice(1, column.dependency.length + 1)
                }];
            }
        });

        for (var o in that.dependency) {
            var d = that.dependency[o];
            var fd = d[0].dependColumn;

            var p = that.dependency[fd];
            while (p) {
                d.push(p[0]);
                p = that.dependency[p[0].dependColumn];
            }
        }

        var cache = {};
        for (var o in that.dependency) {
            var depend = that.dependency[o][0];
            if (cache[depend.dependColumn]) {
                continue;
            }
            var dc = that.getColumn(depend.dependColumn);
            dc.fieldBuilder.dependTrigger(dc, that);
            cache[depend.dependColumn] = 1;
        }

        // 初始化接口
        that.columns.forEach(function(column) {
            var fun = column.fieldBuilder.initHandler;
            if (typeof fun === 'function') {
                column.fieldBuilder.initHandler(column, that);
            }
        });

        if (that.config.pattern == 'view') {
            that.editBtn.hide();
        }
    }
}

_Model.prototype.getColumn = function(columnName) {
    for (var i = 0; i < this.columns.length; i++) {
        if (this.columns[i].name == columnName) {
            return this.columns[i];
        }
    }
    return null;
}

_Model.prototype.setData = function(data) {
    var that = this;
    that.data = data;

    if (that.data) {
        // 如果列依赖不成立时，列数据应该为空
        for (var o in that.dependency) {
            var depends = that.dependency[o];
            var tar = depends[0].target;

            if (!that.isDependencySatisfy(depends, that.data)) {
                that.data[tar] = null;
            }
        }
    }

    if (that.columns) {
        that.columns.forEach(function(column) {
            column.fieldBuilder.setDataHandler(column, data, that);
        });
    }

    if (that.config.pattern == 'edit') {
        that.toEdit();
    } else {
        that.fillViewBody();
    }
}

_Model.prototype.fillViewBody = function() {
    var that = this,
        data = that.data;
    if (that.columns) {
        that.filling = true;
        that.columns.forEach(function(column) {
            column.fieldBuilder.fillView(column, data, that);
        });

        that.filling = false;
        that.checkViewDependency();
    }

    if (typeof that.config.fillViewHandler === 'function') {
        that.config.fillViewHandler(that, that.data);
    }
}

_Model.prototype.fillEditBody = function() {
    var that = this,
        data = that.data;
    if (that.columns) {
        that.filling = true;
        that.columns.forEach(function(column) {
            column.fieldBuilder.fillEdit(column, data, that);
        });
        that.filling = false;
        that.checkEditDependency();
    }

    if (typeof that.config.fillEditHandler === 'function') {
        that.config.fillEditHandler(that, that.data);
    }
}

_Model.prototype.toEdit = function() {
    var that = this;
    that.container.find(".tonto-model-tool-view-btn").hide();
    that.container.find(".tonto-model-tool-edit-btn").show();
    that.viewBody.hide();
    that.editBody.show();
    that.fillEditBody();
}

_Model.prototype.toView = function() {
    var that = this;
    that.container.find(".tonto-model-tool-view-btn").show();
    that.container.find(".tonto-model-tool-edit-btn").hide();
    that.viewBody.show();
    that.editBody.hide();
}

_Model.prototype.isView = function() {
    var that = this;
    return that.viewBody.length > 0 ? that.viewBody.is(':visible') : false;
}

_Model.prototype.isEdit = function() {
    var that = this;
    return that.editBody.length > 0 ? that.editBody.is(':visible') : false;
}

_Model.prototype.isInDependencyValues = function(val, vals) {
    // 是否在依赖值内
    if (val != null && val != undefined && val !== "") {
        if ($.isArray(val)) {
            for (var i = 0; i < val.length; i++) {
                var v = val[i];
                for (var j = 0; j < vals.length; j++) {
                    if (v == vals[s]) {
                        return true;
                    }
                }
            }
        } else {
            for (var i = 0; i < vals.length; i++) {
                if (val == vals[i]) {
                    return true;
                }
            }
        }
    }
    return false;
}

_Model.prototype.isDependencySatisfy = function(dependencies, data) {
    // 是否满足依赖
    for (var i = 0; i < dependencies.length; i++) {
        var dep = dependencies[i];
        if (!this.isInDependencyValues(data[dep.dependColumn], dep.dependValue)) {
            return false;
        }
    }
    return true;
}

_Model.prototype.checkViewDependency = function() {
    // 检查VIEW页面依赖
    var that = this,
        data = that.data;
    for (var o in that.dependency) {
        var depends = that.dependency[o],
            targetColumn = that.getColumn(depends[0].target);
        if (!that.isDependencySatisfy(depends, data)) {
            targetColumn.fieldBuilder.hideView(targetColumn, that);
        } else {
            targetColumn.fieldBuilder.showView(targetColumn, that);
        }
    }
}

_Model.prototype.checkEditDependency = function() {
    // 检查EDIT页面依赖
    var that = this;
    for (var o in that.dependency) {
        var dependencies = that.dependency[o];
        var isOk = true;

        for (var i = 0; i < dependencies.length; i++) {
            var depend = dependencies[i],
                dependCol = that.getColumn(depend.dependColumn),
                val = dependCol.fieldBuilder.getEditValue(dependCol, that);

            if (!that.isInDependencyValues(val, depend.dependValue)) {
                isOk = false;
                break;
            }
        }

        var targetCol = that.getColumn(dependencies[0].target);
        if (isOk) {
            targetCol.fieldBuilder.showEdit(targetCol, that);
        } else {
            targetCol.fieldBuilder.hideEdit(targetCol, that);
        }
    }
}

_Model.prototype.getFormData = function() {
    // TODO 附件等处理
    var jsonData = this.formBody.serializeArray();
    var d = {},
        that = this;
    jsonData.forEach(function(item) {
        if (d[item.name]) {
            d[item.name] = d[item.name] + "," + item.value;
        } else {
            d[item.name] = item.value;
        }
    });

    that.columns.forEach(function(column) {
        column.fieldBuilder.getFormData(d, column, that);
    });

    return d;
}

var _FieldBuilderContainer = {};
var _FieldBuilder = function(name, interfaces) {
    var that = this;
    that.name = name;
    var defaultInterfaces = {
        initHandler: function(column, model) {
            if (typeof column.initHandler === 'function') {
                return column.initHandler(column, model);
            }
        },
        setDataHandler: function(column, data, model) {
            // 插入数据时候调用
            if (typeof column.setDataHandler === 'function') {
                return column.setDataHandler(column, data, model);
            }

            if (data && column.separator) {
                var v = data[column.name];
                if (v) {
                    data[column.name] = v.split(column.separator);
                }
            }
        },
        formDataHandler: function(column, formData, model) {
            // 提交表单数据调用
            if (typeof column.formDataHandler === 'function') {
                return column.formDataHandler(column, formData, model);
            }
        },
        getFormData: function(data, column, model) {
            if (typeof column.getFormData === 'function') {
                return column.getFormData(data, column, model);
            }

            delete data[column.name];
            if (column.editDisplay !== "hide") {
                return data[column.name] = this.getEditValue(column, model);
            }
        },
        dependTrigger: function(column, model) {
            // 依赖域变化注册，监听依赖域变更
            if (typeof column.dependTrigger === 'function') {
                return column.dependTrigger(column, model);
            }
            model.editBody.find("[name='" + column.name + "']").change(function() {
                if (model.filling === false) {
                    model.checkEditDependency();
                }
            });
        },
        getEditValue: function(column, model) {
            // 获取域EDIT页面值
            if (typeof column.getEditValue === 'function') {
                return column.getEditValue(column, model);
            }
            return model.editBody.find("[name='" + column.name + "']").val();
        },
        hideView: function(column, model) {
            if (column.viewDisplay === "hide") {
                return;
            }

            // VIEW页面列隐藏时候调用
            if (typeof column.hideView === 'function') {
                column.hideView(column, model);
                column.viewDisplay = "hide";
                return;
            }

            var p = model.viewBody.find("[name='" + column.name + "']");
            if (!p || p.length == 0) return;
            var d = p.is("div") ? p : p.parent();
            var f = d.parent();
            d.hide();
            d.prev().hide();
            if (f.children(":visible").length == 0) {
                f.hide();
            }

            column.viewDisplay = "hide";
        },
        showView: function(column, model) {
            if (column.viewDisplay === "show") {
                return;
            }

            // VIEW页面列显示时候调用
            if (typeof column.showView === 'function') {
                column.showView(column, model);
                column.viewDisplay = "show";
                return;
            }

            var p = model.viewBody.find("[name='" + column.name + "']");
            var d = p.is("div") ? p : p.parent();
            d.show();
            d.prev().show();
            d.parent().show();

            column.viewDisplay = "show";
        },
        fillView: function(column, data, model) {
            // VIEW页面填充值时候调用
            if (typeof column.fillView === 'function') {
                return column.fillView(column, data, model);
            }

            var p = model.viewBody.find("[name='" + column.name + "']");
            if (!p || p.length == 0) return;
            var v = data ? data[column.name] : null;

            if (v || v === 0) {
                p.removeClass("text-muted");
                p.text(v);
            } else {
                p.addClass("text-muted");
                p.text("无");
            }
        },
        hideEdit: function(column, model) {
            if (column.editDisplay === "hide") {
                return;
            }

            // EDIT页面列隐藏时候调用
            if (typeof column.hideEdit === 'function') {
                column.hideEdit(column, model);
                column.editDisplay = "hide";
                return;
            }

            var p = model.editBody.find("[name='" + column.name + "']");
            if (!p || p.length == 0) return;
            var d = p.is("div") ? p : p.parent();
            var f = d.parent();
            d.hide();
            d.prev().hide();
            if (f.children(":visible").length == 0) {
                f.hide();
            }
            column.editDisplay = "hide";
        },
        showEdit: function(column, model) {
            if (column.editDisplay === "show") {
                return;
            }

            // EDIT页面列隐藏时候调用
            if (typeof column.showEdit === 'function') {
                column.showEdit(column, model);
                column.editDisplay = "show";
                return;
            }

            var p = model.editBody.find("[name='" + column.name + "']");
            if (!p || p.length == 0) return;
            var d = p.is("div") ? p : p.parent();
            d.show();
            d.prev().show();
            d.parent().show();
            column.editDisplay = "show";
        },
        fillEdit: function(column, data, model) {
            // EDIT页面填充值时候调用
            if (typeof column.fillEdit === 'function') {
                return column.fillEdit(column, data, model);
            }

            var input = model.editBody.find("[name='" + column.name + "']");
            if (!input && input.length == 0) return;

            var v = data ? data[column.name] : null,
                isP = input.is("p");

            if (v || v === 0) {
                if (isP || column.editable === false) {
                    input.removeClass("text-muted");
                    input.text(v);
                } else {
                    input.val(v);
                }
            } else {
                if (isP || column.editable === false) {
                    input.addClass("text-muted");
                    input.text("无");
                } else {
                    input.val("");
                }
            }
        },
        generateViewFormColspan: function(column, options) {
            if (typeof column.generateViewFormColspan === 'function') {
                return column.generateViewFormColspan(column, options);
            }
            return column.colspan || 1;
        },
        generateViewFormHtml: function(column, isFirst, options) {
            if (typeof column.generateViewFormHtml === 'function') {
                return column.generateViewFormHtml(column, isFirst, options);
            }
            var colspan = column.colspan || 1,
                html = '<label for="' + column.name + '" class="col-sm-' + (isFirst ? options.firstLabelSize : options.labelSize) + ' control-label">' + column.title + '：</label>\n';
            html += '<div class="col-sm-' + ((colspan - 1) * (options.inputSize + options.labelSize) + options.inputSize) + '">\n';
            html += '<p name="' + column.name + '" class="form-control-static description"></p>\n';
            html += '</div>\n';
            return {
                colspan: colspan,
                html: html
            };
        },
        generateEditFormColspan: function(column, options) {
            if (typeof column.generateEditFormColspan === 'function') {
                return column.generateEditFormColspan(column, options);
            }
            return column.colspan || 1;
        },
        generateEditFormHtml: function(column, isFirst, options) {
            if (typeof column.generateEditFormHtml === 'function') {
                return column.generateEditFormHtml(column, isFirst, options);
            }
            var colspan = column.colspan || 1,
                required = column.required === 'required',
                html = '<label for="' + column.name + '" class="col-sm-' + (isFirst ? options.firstLabelSize : options.labelSize) + ' control-label">' + (required ? '<i class="required-label fa fa-asterisk"></i>' : '') + column.title + '：</label>\n';
            html += '<div class="col-sm-' + ((colspan - 1) * (options.inputSize + options.labelSize) + options.inputSize) + '">\n';
            html += '<input name="' + column.name + '" placeholder="请输入' + column.title + '" type="text" class="form-control" ' + (required ? 'required="required"' : '') + ' ' + _generateAttribute(column.attr) + '/>\n';
            html += '</div>\n';
            return {
                colspan: colspan,
                html: html
            };
        }
    }

    interfaces = $.extend(defaultInterfaces, interfaces);

    if (_FieldBuilderContainer[name]) {
        console && console.log("存在相同名称的域构建器[name:" + name + "]");
    }

    for (var o in interfaces) {
        that[o] = interfaces[o];
    }

    _FieldBuilderContainer[name] = that;
}

// 文本域构建器
var _textFieldBuilder = new _FieldBuilder("TEXT", {});

// 数字域构建器
var _numberFieldBuilder = new _FieldBuilder("NUMBER", {
    fillView: function(column, data, model) {
        // VIEW页面填充值时候调用
        if (typeof column.fillView === 'function') {
            return column.fillView(column, data, model);
        }

        var p = model.viewBody.find("[name='" + column.name + "']");
        if (!p || p.length == 0) return;
        var v = data ? data[column.name] : null;

        if (v || v === 0) {
            p.removeClass("text-muted");
            if (column.unit) {
                p.text(v + column.unit);
            } else {
                p.text(v);
            }
        } else {
            p.addClass("text-muted");
            p.text("无");
        }
    },
    hideEdit: function(column, model) {
        if (column.editDisplay === "hide") {
            return;
        }

        // EDIT页面列隐藏时候调用
        if (typeof column.hideEdit === 'function') {
            column.hideEdit(column, model);
            column.editDisplay = "hide";
            return;
        }

        var p = model.editBody.find("[name='" + column.name + "']");
        if (!p || p.length == 0) return;

        if (column.unit || column.unitIcon) {
            var d = p.parent().parent,
                f = d.parent();
            d.hide();
            d.prev().hide();
            if (f.children(":visible").length == 0) {
                f.hide();
            }
        } else {
            var d = p.parent(),
                f = d.parent();
            d.hide();
            d.prev().hide();
            if (f.children(":visible").length == 0) {
                f.hide();
            }
        }

        column.editDisplay = "hide";
    },
    generateEditFormHtml: function(column, isFirst, options) {
        if (typeof column.generateEditFormHtml === 'function') {
            return column.generateEditFormHtml(column, isFirst, options);
        }
        var colspan = column.colspan || 1,
            required = column.required === 'required',
            html = '<label for="' + column.name + '" class="col-sm-' + (isFirst ? options.firstLabelSize : options.labelSize) + ' control-label">' + (required ? '<i class="required-label fa fa-asterisk"></i>' : '') + column.title + '：</label>\n';
        html += '<div class="col-sm-' + ((colspan - 1) * (options.inputSize + options.labelSize) + options.inputSize) + '">\n';
        if (column.unit || column.unitIcon) {
            html += '<div class="input-group">';
            html += '<input name="' + column.name + '" class="form-control" ' + (required ? 'required="required"' : '') + ' type="number" ' + _generateAttribute(column.attr) + '></input>\n';
            if (column.unitIcon) {
                html += '<div class="input-group-addon">';
                html += '       <i class="' + column.unitIcon + '"></i>';
                html += '</div>';
            } else {
                html += '   <span class="input-group-addon">' + column.unit + '</span>';
            }
            html += '</div>';
        } else {
            html += '<input name="' + column.name + '" type="number"></input>\n';
        }

        html += '</div>\n';
        return {
            colspan: colspan,
            html: html
        };
    }
});

// 大文本域构建器
var _textAreaFieldBuilder = new _FieldBuilder("TEXTAREA", {
    generateViewFormColspan: function(column, options) {
        if (typeof column.generateViewFormColspan === 'function') {
            return column.generateViewFormColspan(column, options);
        }
        return column.colspan || options.maxColspan;
    },
    generateViewFormHtml: function(column, isFirst, options) {
        if (typeof column.generateViewFormHtml === 'function') {
            return column.generateViewFormHtml(column, isFirst, options);
        }
        var colspan = column.colspan || options.maxColspan;
        html = '<label for="' + column.name + '" class="col-sm-' + (isFirst ? options.firstLabelSize : options.labelSize) + ' control-label">' + column.title + '：</label>\n';
        html += '<div class="col-sm-' + ((colspan - 1) * (options.inputSize + options.labelSize) + options.inputSize) + '">\n';
        html += '<pre name="' + column.name + '" style="min-height:150px" class="form-control-static description"></pre>\n';
        html += '</div>\n';
        return {
            colspan: colspan,
            html: html
        };
    },
    generateEditFormColspan: function(column, options) {
        if (typeof column.generateEditFormColspan === 'function') {
            return column.generateEditFormColspan(column, options);
        }
        return column.colspan || options.maxColspan;
    },
    generateEditFormHtml: function(column, isFirst, options) {
        if (typeof column.generateEditFormHtml === 'function') {
            return column.generateEditFormHtml(column, isFirst, options);
        }
        var colspan = column.colspan || options.maxColspan,
            required = column.required === 'required';
        html = '<label for="' + column.name + '" class="col-sm-' + (isFirst ? options.firstLabelSize : options.labelSize) + ' control-label">' + (required ? '<i class="required-label fa fa-asterisk"></i>' : '') + column.title + '：</label>\n';
        html += '<div class="col-sm-' + ((colspan - 1) * (options.inputSize + options.labelSize) + options.inputSize) + '">\n';
        html += '<textarea name="' + column.name + '" rows="' + (column.rows || 5) + '" placeholder="请输入' + column.title + '" class="form-control" ' + (required ? 'required="required"' : '') + ' ' + _generateAttribute(column.attr) + '></textarea>\n';
        html += '</div>\n';
        return {
            colspan: colspan,
            html: html
        };
    }
});

// 日期域构建器
var _dateFieldBuilder = new _FieldBuilder("DATE", {
    setDataHandler: function(column, data, model) {
        // 插入数据时候调用
        if (typeof column.setDataHandler === 'function') {
            return column.setDataHandler(column, data, model);
        }

        var v = data && data[column.name];
        if (typeof v === 'number') {
            data[column.name] = dateFormat(v);
        }
    },
    hideEdit: function(column, model) {
        if (column.editDisplay === "hide") {
            return;
        }

        // EDIT页面列隐藏时候调用
        if (typeof column.hideEdit === 'function') {
            column.hideEdit(column, model);
            column.editDisplay = "hide";
            return;
        }

        var p = model.editBody.find("[name='" + column.name + "']");
        if (!p || p.length == 0) return;
        var d = p.parent().parent(),
            f = d.parent();
        d.hide();
        d.prev().hide();
        if (f.children(":visible").length == 0) {
            f.hide();
        }
        column.editDisplay = "hide";
    },
    showEdit: function(column, model) {
        if (column.editDisplay === "show") {
            return;
        }

        // EDIT页面列隐藏时候调用
        if (typeof column.showEdit === 'function') {
            column.showEdit(column, model);
            column.editDisplay = "show";
            return;
        }

        var p = model.editBody.find("[name='" + column.name + "']");
        if (!p || p.length == 0) return;
        var d = p.parent().parent();
        d.show();
        d.prev().show();
        d.parent().show();
        column.editDisplay = "show";
    },
    generateEditFormColspan: function(column, options) {
        if (typeof column.generateEditFormColspan === 'function') {
            return column.generateEditFormColspan(column, options);
        }
        return column.colspan || 1;
    },
    generateEditFormHtml: function(column, isFirst, options) {
        if (typeof column.generateEditFormHtml === 'function') {
            return column.generateEditFormHtml(column, isFirst, options);
        }
        var colspan = column.colspan || 1,
            required = column.required === 'required',
            html = '<label for="' + column.name + '" class="col-sm-' + (isFirst ? options.firstLabelSize : options.labelSize) + ' control-label">' + (required ? '<i class="required-label fa fa-asterisk"></i>' : '') + column.title + '：</label>\n';
        html += '<div class="col-sm-' + ((colspan - 1) * (options.inputSize + options.labelSize) + options.inputSize) + '">\n';
        html += '<input name="' + column.name + '" autocomplete="off" placeholder="请输入' + column.title + '" type="text" class="form-control tonto-datepicker-date" ' + (required ? 'required="required"' : '') + ' ' + _generateAttribute(column.attr) + '/>\n';
        html += '</div>\n';
        return {
            colspan: colspan,
            html: html
        };
    }
});

// 时间域构建器
var _timeFieldBuilder = new _FieldBuilder("TIME", {
    setDataHandler: function(column, data, model) {
        // 插入数据时候调用
        if (typeof column.setDataHandler === 'function') {
            return column.setDataHandler(column, data, model);
        }

        var v = data && data[column.name];
        if (typeof v === 'number') {
            data[column.name] = datetimeFormat(v);
        }
    },
    hideEdit: function(column, model) {
        if (column.editDisplay === "hide") {
            return;
        }

        // EDIT页面列隐藏时候调用
        if (typeof column.hideEdit === 'function') {
            column.hideEdit(column, model);
            column.editDisplay = "hide";
            return;
        }

        var p = model.editBody.find("[name='" + column.name + "']");
        if (!p || p.length == 0) return;
        var d = p.parent().parent(),
            f = d.parent();
        d.hide();
        d.prev().hide();
        if (f.children(":visible").length == 0) {
            f.hide();
        }
        column.editDisplay = "hide";
    },
    showEdit: function(column, model) {
        if (column.editDisplay === "show") {
            return;
        }

        // EDIT页面列隐藏时候调用
        if (typeof column.showEdit === 'function') {
            column.showEdit(column, model);
            column.editDisplay = "show";
            return;
        }

        var p = model.editBody.find("[name='" + column.name + "']");
        if (!p || p.length == 0) return;
        var d = p.parent().parent();
        d.show();
        d.prev().show();
        d.parent().show();
        column.editDisplay = "show";
    },
    generateEditFormColspan: function(column, options) {
        if (typeof column.generateEditFormColspan === 'function') {
            return column.generateEditFormColspan(column, options);
        }
        return column.colspan || 1;
    },
    generateEditFormHtml: function(column, isFirst, options) {
        if (typeof column.generateEditFormHtml === 'function') {
            return column.generateEditFormHtml(column, isFirst, options);
        }
        var colspan = column.colspan || 1,
            required = column.required === 'required',
            html = '<label for="' + column.name + '" class="col-sm-' + (isFirst ? options.firstLabelSize : options.labelSize) + ' control-label">' + (required ? '<i class="required-label fa fa-asterisk"></i>' : '') +
            column.title + '：</label>\n';
        html += '<div class="col-sm-' + ((colspan - 1) * (options.inputSize + options.labelSize) + options.inputSize) + '">\n';
        html += '<input name="' + column.name + '" autocomplete="off" placeholder="请输入' + column.title +
            '" type="text" class="form-control tonto-datepicker-datetime" ' + (required ? 'required="required"' : '') + ' ' + _generateAttribute(column.attr) + '/>\n';
        html += '</div>\n';
        return {
            colspan: colspan,
            html: html
        };
    }
});

// 下拉框域构建器
var _selectFieldBuilder = new _FieldBuilder("SELECT", {
    initHandler: function(column, model) {
        if (typeof column.initHandler === 'function') {
            return column.initHandler(column, model);
        }
        if (column.multiple === true) {
            column.separator = column.separator || ',';
        }
    },
    getDataName: function(column, v) {
        if (column.multiple === true) {
            if (v) {
                var vs = "";
                v.forEach(function(vi) {
                    var a = $.getConstantEnumValue(column.enum, vi);
                    if (a) {
                        vs += a + "，";
                    }
                });

                if (vs.length > 0) {
                    vs = vs.substring(0, vs.length - 1);
                }

                v = vs;
            }
        } else {
            if (v || v === 0) {
                v = $.getConstantEnumValue(column.enum, v);
            }
        }
        return v;
    },
    fillView: function(column, data, model) {
        // VIEW页面填充值时候调用
        if (typeof column.fillView === 'function') {
            return column.fillView(column, data, model);
        }

        var p = model.viewBody.find("[name='" + column.name + "']");
        if (!p || p.length == 0) return;
        var v = data ? data[column.name] : null;

        v = this.getDataName(column, v);

        if (v || v === 0) {
            p.removeClass("text-muted");
            p.text(v);
        } else {
            p.addClass("text-muted");
            p.text("无");
        }
    },
    fillEdit: function(column, data, model) {
        // EDIT页面填充值时候调用
        if (typeof column.fillEdit === 'function') {
            return column.fillEdit(column, data, model);
        }

        var input = model.editBody.find("[name='" + column.name + "']");
        if (!input && input.length == 0) return;

        var ov = data ? data[column.name] : null,
            isP = input.is("p");


        if (isP) {
            var v = this.getDataName(column, ov);

            if (v || v === 0) {
                input.removeClass("text-muted");
                input.text(v);
            } else {
                input.addClass("text-muted");
                input.text("无");
            }
        } else {
            if (ov || ov === 0) {
                if (column.multiple === true) {
                    input.val(ov).trigger('change');
                } else {
                    input.val(ov);
                }
            } else {
                if (column.multiple === true) {

                } else {
                    input.find("option:first").prop("selected", 'selected');
                }
            }
        }
    },
    generateEditFormColspan: function(column, options) {
        if (typeof column.generateEditFormColspan === 'function') {
            return column.generateEditFormColspan(column, options);
        }
        return column.colspan || 1;
    },
    generateEditFormHtml: function(column, isFirst, options) {
        if (typeof column.generateEditFormHtml === 'function') {
            return column.generateEditFormHtml(column, isFirst, options);
        }
        var colspan = column.colspan || 1,
            required = column.required === 'required',
            multiple = column.multiple === true,
            html = '<label for="' + column.name + '" class="col-sm-' + (isFirst ? options.firstLabelSize : options.labelSize) + ' control-label">' + (required ? '<i class="required-label fa fa-asterisk"></i>' : '') +
            column.title + '：</label>\n';
        html += '<div class="col-sm-' + ((colspan - 1) * (options.inputSize + options.labelSize) + options.inputSize) + '">\n';
        html += '<select name="' + column.name + '" ' + (column.placeholder ? 'placeholder="' + column.placeholder + '"' : '') +
            ' class="form-control tonto-select-constant' + (multiple ? ' tonto-multiple-select" multiple="multiple"' : '"') + (required ? ' required="required"' : '') + ' enumcode="' + column.enum + '" ' + _generateAttribute(column.attr) + '>\n';
        if (column.nullable !== false && !required && !multiple) {
            html += '<option value="">请选择</option>\n';
        }
        html += '</select>\n';
        html += '</div>\n';
        return {
            colspan: colspan,
            html: html
        };
    }
});

// 下拉框域构建器
var _selectServerFieldBuilder = new _FieldBuilder("SELECT-SERVER", {
    initHandler: function(column, model) {
        if (typeof column.initHandler === 'function') {
            return column.initHandler(column, model);
        }
        if (column.multiple === true) {
            column.separator = column.separator || ',';
        }
        this.getDataFromServer(column, model);
    },
    getDataFromServer: function(column, model) {
        var that = this;
        $.getAjax(column.url, function(data) {
            column.serverData = data;
            column.serverDataGot = true;
            that.fillDataFromServer(column, model);
        });
    },
    fillDataFromServer: function(column, model) {
        var input = model.editBody.find("[name='" + column.name + "']");
        if (column.serverData && input.length > 0) {
            var k = column.idField || 'id',
                n = column.nameField || 'name';
            column.serverData.forEach(function(d) {
                input.append('<option value="' + d[k] + '">' + d[n] + '</option>');
            });

            input.select2({
                placeholder: input.attr("placeholder") || "请选择", //未选择时显示文本
                maximumSelectionSize: column.maxSelectionSize || null, //显示最大选项数目
                multiple: column.multiple !== false,
                width: '100%',
                allowClear: true
            });
        };

        this.fillView(column, model.data, model);
        this.fillEdit(column, model.data, model);
    },
    getDataName: function(column, v) {
        if (column.serverData) {
            var k = column.idField || 'id',
                n = column.nameField || 'name';
            if (column.multiple === true) {
                if (v) {
                    var vs = "";
                    v.forEach(function(vi) {
                        for (var i = 0; i < column.serverData.length; i++) {
                            var a = column.serverData[i];
                            if (a[k] == vi) {
                                vs += a[n] + "，";
                            }
                        }
                    });

                    if (vs.length > 0) {
                        vs = vs.substring(0, vs.length - 1);
                    }

                    v = vs;
                }
            } else {
                if (v || v === 0) {
                    for (var i = 0; i < column.serverData.length; i++) {
                        var a = column.serverData[i];
                        if (a[k] == v) {
                            v = a[n];
                            break;
                        }
                    }
                }
            }
        }
        return v;
    },
    fillView: function(column, data, model) {
        // VIEW页面填充值时候调用
        if (typeof column.fillView === 'function') {
            return column.fillView(column, data, model);
        }

        if (column.serverDataGot === true) {
            var p = model.viewBody.find("[name='" + column.name + "']");
            if (!p || p.length == 0) return;
            var v = data ? data[column.name] : null;
            v = this.getDataName(column, v);

            if (v || v === 0) {
                p.removeClass("text-muted");
                p.text(v);
            } else {
                p.addClass("text-muted");
                p.text("无");
            }
        }
    },
    fillEdit: function(column, data, model) {
        // EDIT页面填充值时候调用
        if (typeof column.fillEdit === 'function') {
            return column.fillEdit(column, data, model);
        }

        if (column.serverDataGot === true) {
            var input = model.editBody.find("[name='" + column.name + "']");
            if (!input && input.length == 0) return;

            var ov = data ? data[column.name] : null,
                isP = input.is("p");

            if (isP) {
                var v = this.getDataName(column, ov);

                if (v || v === 0) {
                    input.removeClass("text-muted");
                    input.text(v);
                } else {
                    input.addClass("text-muted");
                    input.text("无");
                }

            } else {
                if (ov || ov === 0) {
                    if (column.multiple === true) {
                        input.val(ov).trigger('change');
                    } else {
                        input.val(ov);
                    }
                } else {
                    if (column.multiple === true) {

                    } else {
                        input.find("option:first").prop("selected", 'selected');
                    }
                }
            }
        }
    },
    generateEditFormColspan: function(column, options) {
        if (typeof column.generateEditFormColspan === 'function') {
            return column.generateEditFormColspan(column, options);
        }
        return column.colspan || 1;
    },
    generateEditFormHtml: function(column, isFirst, options) {
        if (typeof column.generateEditFormHtml === 'function') {
            return column.generateEditFormHtml(column, isFirst, options);
        }
        var colspan = column.colspan || 1,
            required = column.required === 'required',
            multiple = column.multiple === true,
            html = '<label for="' + column.name + '" class="col-sm-' + (isFirst ? options.firstLabelSize : options.labelSize) + ' control-label">' + (required ? '<i class="required-label fa fa-asterisk"></i>' : '') +
            column.title + '：</label>\n';
        html += '<div class="col-sm-' + ((colspan - 1) * (options.inputSize + options.labelSize) + options.inputSize) + '">\n';
        html += '<select name="' + column.name + '"' + (column.placeholder ? ' placeholder="' + column.placeholder + '"' : '') +
            ' class="form-control"' + (multiple ? ' multiple="multiple"' : '') + (required ? ' required="required"' : '') + ' ' + _generateAttribute(column.attr) + '>\n';
        if (column.nullable !== false && !required && !multiple) {
            html += '<option value="">请选择</option>\n';
        }
        html += '</select>\n';
        html += '</div>\n';
        return {
            colspan: colspan,
            html: html
        };
    }
});

// 附件域构建器
var _attachmentFieldBuilder = new _FieldBuilder("ATTACHMENT", {
    setDataHandler: function(column, data, model) {
        // 插入数据时候调用
        if (typeof column.setDataHandler === 'function') {
            return column.setDataHandler(column, data, model);
        }

        // 解析的附件
        if (!data) return;

        var filename = column.fileName,
            v = data[column.name];
        data[filename] = $.parseAttachmentData(data[filename]);
        if (v) {
            data[column.name] = v.split(column.separator || ",");
        }
    },
    getFormData: function(data, column, model) {
        if (typeof column.getFormData === 'function') {
            return column.getFormData(data, column, model);
        }

        if (column.editDisplay !== "hide") {
            // 有附件时，需要替换某些参数
            var previews = column.inputAttachment.fileinput('getPreview');
            var attachments = "";
            if (previews && previews.config && previews.config.length > 0) {
                previews.config.forEach(function(p) {
                    attachments += p.key + ",";
                });
            }

            data[column.name] = attachments;

            // 动态加入未上传的文件数据
            var files = column.inputAttachment.fileinput('getFileStack');
            if (files) {
                var fileArr = [];

                files.forEach(function(file) {
                    fileArr.push(file);
                });
                data[column.fileName] = fileArr;
            }
        }
    },
    formDataHandler: function(column, formData, model) {
        // 提交表单数据调用
        if (typeof column.formDataHandler === 'function') {
            return column.formDataHandler(column, formData, model);
        }

        var maxFileCount = column.maxFileCount || 5,
            fileName = column.fileName,
            fileCount = 0,
            i = 0;

        // 原表单文件数据只有最后一个，这里需要手动从插件中获取File Object添加到表单数据中
        for (; i < formData.length; i++) {
            if (formData[i].name == fileName) {
                formData.splice(i, 1);
                i--;
            }
        }

        if (column.editDisplay !== "hide") {
            // 有附件时，需要替换某些参数
            var previews = column.inputAttachment.fileinput('getPreview');
            var attachments = "";
            if (previews && previews.config && previews.config.length > 0) {
                previews.config.forEach(function(p) {
                    attachments += p.key + ",";
                    fileCount++;
                });
            }

            // 动态加入已经上传的附件ID
            formData.push({
                name: column.name,
                value: attachments,
                type: "text",
                required: false
            });

            // 动态加入未上传的文件数据
            var files = column.inputAttachment.fileinput('getFileStack');
            if (files) {
                files.forEach(function(file) {
                    formData.push({
                        name: fileName,
                        value: file,
                        type: "file",
                        required: false
                    });
                    fileCount++;
                });
            }

            if (fileCount > maxFileCount) {
                $.errorAlert("附件数量不能超过" + maxFileCount + "个");
                return false;
            }
        }
    },
    dependTrigger: function(column, model) {
        // 依赖域变化注册，监听依赖域变更
        if (typeof column.dependTrigger === 'function') {
            return column.dependTrigger(column, model);
        }
        // 不能被依赖
        console && console.log("附件不应该被依赖");
    },
    getEditValue: function(column, model) {
        // 获取域EDIT页面值
        if (typeof column.getEditValue === 'function') {
            return column.getEditValue(column, model);
        }

        // 获取文件数据暂不支持
        console && console.log("暂不实现文件数据获取");
    },
    hideView: function(column, model) {
        if (column.viewDisplay === "hide") {
            return;
        }

        // VIEW页面列隐藏时候调用
        if (typeof column.hideView === 'function') {
            column.hideView(column, model);
            column.viewDisplay = "hide";
            return;
        }

        var d = model.viewBody.find("[name='" + column.name + "']");
        if (!d || d.length == 0) return;
        var f = d.parent();
        d.hide();
        d.prev().hide();
        if (f.children(":visible").length == 0) {
            f.hide();
        }

        column.viewDisplay = "hide";
    },
    showView: function(column, model) {
        if (column.viewDisplay === "show") {
            return;
        }

        // VIEW页面列显示时候调用
        if (typeof column.showView === 'function') {
            column.showView(column, model);
            column.viewDisplay = "show";
            return;
        }

        var d = model.viewBody.find("[name='" + column.name + "']");
        if (!d || d.length == 0) return;
        d.show();
        d.prev().show();
        d.parent().show();
        column.viewDisplay = "show";
    },
    fillView: function(column, data, model) {
        // VIEW页面填充值时候调用
        if (typeof column.fillView === 'function') {
            return column.fillView(column, data, model);
        }

        var name = column.name,
            atts = data && data[column.fileName];

        if (atts) {
            var attDiv = model.viewBody.find('[name="' + name + '"]');
            var html = '<ul class="mailbox-attachments clearfix">';
            for (var i = 0; i < atts.length; i++) {
                var b = atts[i];
                var k = b.filename.lastIndexOf(".");
                var suffix = "";
                if (k >= 0) {
                    suffix = b.filename.substring(k + 1).toLowerCase();
                }

                var header = "";
                if (suffix == "jpeg" || suffix == "jpg" || suffix == "png" || suffix == "gif") {
                    header = '<span class="mailbox-attachment-icon has-img"><img src="' + b.url + '" alt="Attachment"></span>';
                } else {
                    var iconMap = {
                        txt: "fa-file-text-o",
                        xls: "fa-file-excel-o",
                        xlsx: "fa-file-excel-o",
                        pdf: "fa-file-pdf-o",
                        doc: "fa-file-word-o",
                        docx: "fa-file-word-o",
                        rar: "fa-file-zip-o",
                        zip: "fa-file-zip-o"
                    }
                    var icon = iconMap[suffix] || "fa-file-o";
                    header = '<span class="mailbox-attachment-icon"><i class="fa ' + icon + '"></i></span>';
                }

                html +=
                    '<li>' + header +
                    '    <div class="mailbox-attachment-info">' +
                    '        <a target="_blank" href="' + b.url + '" class="mailbox-attachment-name"><i class="fa fa-camera"></i>' + b.filename + '</a>' +
                    '        <span class="mailbox-attachment-size">' + (Math.floor(b.size / 1024) + "KB") + '<a target="_blank" download="' + b.filename + '" href="' + b.url + '" class="btn btn-default btn-xs pull-right"><i class="fa fa-cloud-download"></i></a></span>' +
                    '    </div>' +
                    '</li>';
            }
            html += "</ul>";
            attDiv.html(html);
        }
    },
    hideEdit: function(column, model) {
        if (column.editDisplay === "hide") {
            return;
        }

        // EDIT页面列隐藏时候调用
        if (typeof column.hideEdit === 'function') {
            column.hideEdit(column, model);
            column.editDisplay = "hide";
            return;
        }

        var i = model.editBody.find("[name='" + column.fileName + "']");
        if (!i || i.length == 0) return;
        var d = i.parent().parent().parent().parent().parent();
        var f = d.parent();
        d.hide();
        d.prev().hide();
        if (f.children(":visible").length == 0) {
            f.hide();
        }
        column.editDisplay = "hide";
    },
    showEdit: function(column, model) {
        if (column.editDisplay === "show") {
            return;
        }

        // EDIT页面列隐藏时候调用
        if (typeof column.showEdit === 'function') {
            column.showEdit(column, model);
            column.editDisplay = "show";
            return;
        }

        var i = model.editBody.find("[name='" + column.fileName + "']");
        if (!i || i.length == 0) return;
        var d = i.parent().parent().parent().parent().parent();
        d.show();
        d.prev().show();
        d.parent().show();
        column.editDisplay = "show";
    },
    fillEdit: function(column, data, model) {
        // EDIT页面填充值时候调用
        if (typeof column.fillEdit === 'function') {
            return column.fillEdit(column, data, model);
        }

        var name = column.fileName,
            atts = data ? data[name] : null,
            fileInput = model.formBody.find('[name="' + name + '"]');

        var initialPreview = [];
        var initialPreviewConfig = [];
        if (atts) {
            atts.forEach(function(att) {
                initialPreview.push(att.url);
                initialPreviewConfig.push({
                    caption: att.filename,
                    size: att.size,
                    key: att.id
                });
            });
        }

        if (column.inputAttachment) {
            column.inputAttachment.fileinput('destroy');
        }

        column.inputAttachment = $(fileInput).fileinput({
            language: 'zh',
            uploadUrl: '/common/upload/files',
            showUpload: false,
            layoutTemplates: {
                actionUpload: '' //去除上传预览缩略图中的上传图片；
            },
            uploadAsync: false,
            maxFileCount: column.maxFileCount || 4,
            allowedFileExtensions: column.allowedFileExtensions || ["jpeg", "jpg", "png", "gif"],
            overwriteInitial: false,
            dropZoneEnabled: false, // 禁止拖拽
            ajaxDelete: false, // 扩展定义配置，不进行后台删除操作
            initialPreview: initialPreview,
            initialPreviewAsData: true, // allows you to set a raw markup
            initialPreviewFileType: 'image', // image is the default and can be overridden in config below
            initialPreviewConfig: initialPreviewConfig
        });
    },
    generateViewFormColspan: function(column, options) {
        if (typeof column.generateViewFormColspan === 'function') {
            return column.generateViewFormColspan(column, options);
        }
        return column.colspan || options.maxColspan;
    },
    generateViewFormHtml: function(column, isFirst, options) {
        if (typeof column.generateViewFormHtml === 'function') {
            return column.generateViewFormHtml(column, isFirst, options);
        }
        var colspan = column.colspan || options.maxColspan;
        var html = '<label for="' + column.name + '" class="col-sm-' + (isFirst ? options.firstLabelSize : options.labelSize) + ' control-label">' + column.title + '：</label>\n';
        var colCount = column.colCount ? column.colCount : ((colspan - 1) * (options.inputSize + options.labelSize) + options.inputSize);
        html += '<div name="' + column.name + '" class="col-sm-' + colCount + '"></div>\n';
        return {
            colspan: colspan,
            html: html
        };
    },
    generateEditFormColspan: function(column, options) {
        if (typeof column.generateEditFormColspan === 'function') {
            return column.generateEditFormColspan(column, options);
        }
        return column.colspan || options.maxColspan;
    },
    generateEditFormHtml: function(column, isFirst, options) {
        if (typeof column.generateEditFormHtml === 'function') {
            return column.generateEditFormHtml(column, isFirst, options);
        }
        var colspan = column.colspan || options.maxColspan,
            required = column.required === 'required';
        var html = '<label for="' + column.name + '" class="col-sm-' + (isFirst ? options.firstLabelSize : options.labelSize) + ' control-label">' + (required ? '<i class="required-label fa fa-asterisk"></i>' : '') + column.title + '：</label>\n';
        var colCount = column.colCount ? column.colCount : ((colspan - 1) * (options.inputSize + options.labelSize) + options.inputSize);
        html += '<div name="' + column.name + '" class="col-sm-' + colCount + '">\n';
        html += '<input type="file" name="' + column.fileName + '" ' + (column.maxFileCount === 1 ? '' : 'multiple') + '>\n';
        html += '</div>\n';
        return {
            colspan: colspan,
            html: html
        };
    }
});

// 单选构建器
var _radioFieldBuilder = new _FieldBuilder("RADIO", {
    getEditValue: function(column, model) {
        // 获取域EDIT页面值
        if (typeof column.getEditValue === 'function') {
            return column.getEditValue(column, model);
        }

        return model.editBody.find("input[name='" + column.name + "']:checked").val();
    },
    dependTrigger: function(column, model) {
        // 依赖域变化注册，监听依赖域变更
        if (typeof column.dependTrigger === 'function') {
            return column.dependTrigger(column, model);
        }
        // 这里使用icheck 所以调用ifChecked事件
        model.editBody.find("input[name='" + column.name + "']").on('ifChecked', function() {
            if (model.filling === false) {
                model.checkEditDependency();
            }
        });
    },
    fillView: function(column, data, model) {
        // VIEW页面填充值时候调用
        if (typeof column.fillView === 'function') {
            return column.fillView(column, data, model);
        }

        var p = model.viewBody.find("[name='" + column.name + "']");
        if (!p || p.length == 0) return;
        var v = data ? data[column.name] : null;
        if (column.enum && (v || v === 0)) {
            v = $.getConstantEnumValue(column.enum, v);
        }

        if (v || v === 0) {
            p.removeClass("text-muted");
            p.text(v);
        } else {
            p.addClass("text-muted");
            p.text("无");
        }
    },
    fillEdit: function(column, data, model) {
        // EDIT页面填充值时候调用
        if (typeof column.fillEdit === 'function') {
            return column.fillEdit(column, data, model);
        }

        var input = model.editBody.find("[name='" + column.name + "']");
        if (!input && input.length == 0) return;

        var ov = data ? data[column.name] : null,
            isP = input.is("p"),
            v = column.enum && (ov || ov === 0) ? $.getConstantEnumValue(column.enum, ov) : null;

        if (isP) {
            if (v || v === 0) {
                input.removeClass("text-muted");
                input.text(v);
            } else {
                input.addClass("text-muted");
                input.text("无");
            }
        } else {
            input.each(function() {
                var a = $(this);
                if (a.val() == ov) {
                    a.iCheck('check');
                }
            });
        }
    },
    generateEditFormColspan: function(column, options) {
        if (typeof column.generateEditFormColspan === 'function') {
            return column.generateEditFormColspan(column, options);
        }
        return column.colspan || 1;
    },
    generateEditFormHtml: function(column, isFirst, options) {
        if (typeof column.generateEditFormHtml === 'function') {
            return column.generateEditFormHtml(column, isFirst, options);
        }
        var colspan = column.colspan || 1,
            required = column.required === 'required',
            html = '<label for="' + column.name + '" class="col-sm-' + (isFirst ? options.firstLabelSize : options.labelSize) + ' control-label">' + (required ? '<i class="required-label fa fa-asterisk"></i>' : '') + column.title + '：</label>\n';
        var colCount = column.colCount ? column.colCount : ((colspan - 1) * (options.inputSize + options.labelSize) + options.inputSize);
        html += '<div class="col-sm-' + colCount + '">\n';
        html += '<div name="' + column.name + '" class="tonto-radio-constant" ' + (required ? 'required="required"' : '') + ' enumcode="' + column.enum + '"></div>\n';
        html += '</div>\n';
        return {
            colspan: colspan,
            html: html
        };
    }
});

// 多选选构建器
var _checkBoxFieldBuilder = new _FieldBuilder("CHECKBOX", {
    setDataHandler: function(column, data, model) {
        // 插入数据时候调用
        if (typeof column.setDataHandler === 'function') {
            return column.setDataHandler(column, data, model);
        }

        // 解析的附件
        var v = data && data[column.name];
        if (v) {
            data[column.name] = v.split(column.separator || ",");
        }
    },
    getEditValue: function(column, model) {
        // 获取域EDIT页面值
        if (typeof column.getEditValue === 'function') {
            return column.getEditValue(column, model);
        }

        var vals = [];
        model.editBody.find("input[name='" + column.name + "']:checked").each(function() {
            vals.push($(this).val());
        });
        return vals.join();
    },
    dependTrigger: function(column, model) {
        // 依赖域变化注册，监听依赖域变更
        if (typeof column.dependTrigger === 'function') {
            return column.dependTrigger(column, model);
        }
        // 这里使用icheck 所以调用ifChecked事件
        model.editBody.find("input[name='" + column.name + "']").on('ifChecked', function() {
            if (model.filling === false) {
                model.checkEditDependency();
            }
        });
    },
    fillView: function(column, data, model) {
        // VIEW页面填充值时候调用
        if (typeof column.fillView === 'function') {
            return column.fillView(column, data, model);
        }

        var p = model.viewBody.find("[name='" + column.name + "']");
        if (!p || p.length == 0) return;
        var v = data ? data[column.name] : null;


        if (v) {
            var t = "";
            v.forEach(function(a) {
                t += column.enum ? $.getConstantEnumValue(column.enum, a) : a;
                t += ",";
            });

            if (t.length > 0) {
                t = t.substring(0, t.length - 1);
            }

            p.removeClass("text-muted");
            p.text(t);
        } else {
            p.addClass("text-muted");
            p.text("无");
        }
    },
    fillEdit: function(column, data, model) {
        // EDIT页面填充值时候调用
        if (typeof column.fillEdit === 'function') {
            return column.fillEdit(column, data, model);
        }

        var input = model.editBody.find("[name='" + column.name + "']");
        if (!input && input.length == 0) return;

        if (input.is("p")) {
            var ov = data ? data[column.name] : null,
                v = column.enum && ov ? $.getConstantEnumValue(column.enum, ov) : null,
                t = "";
            if (v) {
                v.forEach(function(a) {
                    t += column.enum ? $.getConstantEnumValue(column.enum, a) : a;
                    t += ",";
                });
            }

            if (t.length > 0) {
                t = t.substring(0, t.length - 1);
            }

            input.removeClass("text-muted");
            input.text(t);
        } else {
            var v = data ? data[column.name] : null;
            if (v) {
                v.forEach(function(a) {
                    model.editBody.find("input[name='" + column.name + "'][value='" + a + "']").iCheck('check');
                });
            }
        }
    },
    generateEditFormColspan: function(column, options) {
        if (typeof column.generateEditFormColspan === 'function') {
            return column.generateEditFormColspan(column, options);
        }
        return column.colspan || 1;
    },
    generateEditFormHtml: function(column, isFirst, options) {
        if (typeof column.generateEditFormHtml === 'function') {
            return column.generateEditFormHtml(column, isFirst, options);
        }
        var colspan = column.colspan || 1,
            required = column.required === 'required',
            html = '<label for="' + column.name + '" class="col-sm-' + (isFirst ? options.firstLabelSize : options.labelSize) + ' control-label">' + (required ? '<i class="required-label fa fa-asterisk"></i>' : '') +
            column.title + '：</label>\n';
        var colCount = column.colCount ? column.colCount : ((colspan - 1) * (options.inputSize + options.labelSize) + options.inputSize);
        html += '<div class="col-sm-' + colCount + '">\n';
        html += '<div name="' + column.name + '" class="tonto-checkbox-constant" ' + (required ? 'required="required"' : '') + ' enumcode="' + column.enum + '"></div>\n';
        html += '</div>\n';
        return {
            colspan: colspan,
            html: html
        };
    }
});

// 标签域构建器
var _tagsinputFieldBuilder = new _FieldBuilder("TAGSINPUT", {
    setDataHandler: function(column, data, model) {
        // 插入数据时候调用
        if (typeof column.setDataHandler === 'function') {
            return column.setDataHandler(column, data, model);
        }

        // 解析的附件
        var v = data && data[column.name];
        if (v) {
            data[column.name] = v.split(column.separator || ",");
        }
    },
    getEditValue: function(column, model) {
        // 获取域EDIT页面值
        if (typeof column.getEditValue === 'function') {
            return column.getEditValue(column, model);
        }

        return model.editBody.find("input[name='" + column.name + "']").tagsinput("items");
    },
    fillView: function(column, data, model) {
        // VIEW页面填充值时候调用
        if (typeof column.fillView === 'function') {
            return column.fillView(column, data, model);
        }

        var p = model.viewBody.find("[name='" + column.name + "']");
        if (!p || p.length == 0) return;
        var v = data ? data[column.name] : null;


        if (v) {
            var t = "";
            v.forEach(function(a) {
                t += a + ",";
            });

            if (t.length > 0) {
                t = t.substring(0, t.length - 1);
            }

            p.removeClass("text-muted");
            p.text(t);
        } else {
            p.addClass("text-muted");
            p.text("无");
        }
    },
    fillEdit: function(column, data, model) {
        // EDIT页面填充值时候调用
        if (typeof column.fillEdit === 'function') {
            return column.fillEdit(column, data, model);
        }

        var input = model.editBody.find("[name='" + column.name + "']");
        if (!input && input.length == 0) return;
        input.tagsinput("removeAll");
        var v = data ? data[column.name] : null;

        if (v) {
            v.forEach(function(a) {
                input.tagsinput('add', a);
            });
        }
    },
    generateEditFormHtml: function(column, isFirst, options) {
        if (typeof column.generateEditFormHtml === 'function') {
            return column.generateEditFormHtml(column, isFirst, options);
        }
        var colspan = column.colspan || 1,
            required = column.required === 'required',
            html = '<label for="' + column.name + '" class="col-sm-' + (isFirst ? options.firstLabelSize : options.labelSize) + ' control-label">' + (required ? '<i class="required-label fa fa-asterisk"></i>' : '') +
            column.title + '：</label>\n';
        var colCount = column.colCount ? column.colCount : ((colspan - 1) * (options.inputSize + options.labelSize) + options.inputSize);
        html += '<div class="col-sm-' + colCount + '">\n';
        html += '<input name="' + column.name + '" type="text" class="form-control" data-role="tagsinput" placeholder="输入内容后回车" ' +
            (required ? 'required="required"' : '') + ' ' + _generateAttribute(column.attr) + '/>\n';
        html += '</div>\n';
        return {
            colspan: colspan,
            html: html
        };
    }
});

// 子模块域构建器
var _subModelFieldBuilder = new _FieldBuilder("SUB-MODEL", {
    getEditValue: function(column, model) {
        // 获取域EDIT页面值
        if (typeof column.getEditValue === 'function') {
            return column.getEditValue(column, model);
        }

        if (column.contentMap) {
            var datas = [];
            for (var o in column.contentMap) {
                datas.push(column.contentMap[o].data);
            }
            return datas;
        }
        return null;
    },
    getFormData: function(data, column) {
        var datas = [];
        if (column.contentMap) {
            for (var o in column.contentMap) {
                datas.push(column.contentMap[o].data);
            }
        }
        data[column.name] = datas;
    },
    fillView: function(column, data, model) {
        // VIEW页面填充值时候调用
        if (typeof column.fillView === 'function') {
            return column.fillView(column, data, model);
        }

        var that = this,
            div = model.viewBody.find("[name='" + column.name + "']"),
            ul = $('<ul class="products-list product-list-in-box"></ul>');

        div.empty();
        div.append(ul);
        var subData = data ? data[column.name] : null;
        if (subData) {
            subData.forEach(function(d) {
                that.fillSubView(column, d, model, ul);
            });
        }
    },
    fillSubView: function(column, data, model, contentContainer) {
        var itemHtml, that = this;
        if (typeof column.createSubDataHtml === 'function') {
            itemHtml = column.createSubDataHtml();
        } else {
            itemHtml = '';
            var subTitleViewHtmml;
            if (typeof column.subTitleViewHtmml === 'function') {
                subTitleViewHtmml = column.subTitleViewHtmml(data);
            } else {
                subTitleViewHtmml += "<h3 style='display: inline-block;font-size: 18px;margin: 0;line-height: 1;'>" + data[column.subViewField] + "</h3>";
            }

            itemHtml += subTitleViewHtmml;
        }

        contentContainer.append(itemHtml);
    },
    fillSubEdit: function(column, data, model, id) {
        var contentContainer = column.contentContainer,
            itemHtml, that = this;
        if (typeof column.createSubDataHtml === 'function') {
            itemHtml = column.createSubDataHtml();

        } else {
            itemHtml = '<div class="pull-right">' +
                '<a class="btn" id="' + column.name + '_sub_edit_btn" href="javascript:void(0)"><i class="fa fa-edit"></i>编辑</a>\n' +
                '<a class="btn" id="' + column.name + '_sub_remove_btn" href="javascript:void(0)"><i class="fa fa-remove"></i>删除</a>\n' +
                '</div>';
            var subTitleViewHtmml;
            if (typeof column.subTitleViewHtmml === 'function') {
                subTitleViewHtmml = column.subTitleViewHtmml(data);
            } else {
                subTitleViewHtmml += "<h3 style='display: inline-block;font-size: 18px;margin: 0;line-height: 1;'>" + data[column.subViewField] + "</h3>";
            }

            itemHtml += subTitleViewHtmml;
        }

        var div, com;
        if (!id) {
            id = column.name + "_content_" + new Date().getTime();
            div = $('<li class="item" style="background: none;"></li>');
            com = {
                id: id,
                div: div,
                data: data
            };
            column.contentMap[id] = com;
            contentContainer.append(div);
            div.html(itemHtml);
        } else {
            com = column.contentMap[id];
            div = com.div;
            com.data = data;
            div.html(itemHtml);
        }

        div.find('#' + column.name + '_sub_edit_btn').click(function() {
            that.openSubEditor(column, com, model);
        });

        div.find('#' + column.name + '_sub_remove_btn').click(function() {
            layer.confirm('确定删除吗?', function(layerIndex) {
                delete column.contentMap[id];
                div.remove();
                layer.close(layerIndex);
            });
        });
    },
    fillEdit: function(column, data, model) {
        // EDIT页面填充值时候调用
        if (typeof column.fillEdit === 'function') {
            return column.fillEdit(column, data, model);
        }
        var that = this,
            div = model.editBody.find("[name='" + column.name + "']");
        if (!column.hasEdited) {
            var contentContainer = $('<ul class="products-list product-list-in-box"></ul>'),
                addSubModelBtn = column.addSubModelBtn ? column.addSubModelBtn : $('<div class="dotted-line-btn"><a href="javascript:void(0)" ><i class="glyphicon glyphicon-plus"></i>' + (column.addSubModelBtnTitle ? column.addSubModelBtnTitle : '添加选项') + '</a></div>');
            div.append(contentContainer);
            div.append(addSubModelBtn);
            column.contentContainer = contentContainer;
            column.contentMap = {};

            var subData = data ? data[column.name] : null;
            if (subData) {
                subData.forEach(function(d) {
                    that.fillSubEdit(column, d, model, null);
                });
            }

            addSubModelBtn.click(function() {
                that.openSubEditor(column, null, model);
            });

            column.hasEdited = true;
        }
    },
    openSubEditor: function(column, com, model) {
        var that = this;
        var subOp = column.subModelOptions;
        subOp.id = subOp.id || column.name + "_" + new Date().getTime();

        var defaultSubOp = {
            cancelBtn: false,
            server: false,
            editFormClass: false,
            maxColspan: 1,
            firstLabelSize: 3,
            inputSize: 8,
            labelSize: 3,
            formButtonBar: [{
                id: subOp.id + '_edit_cancel_btn',
                type: 'button',
                name: '取消',
                class: 'btn btn-default btn-block',
                order: 999
            }]
        }

        var subOp = $.extend(defaultSubOp, subOp);
        var html = generateEditFormHtml(subOp, false);
        html = "<div style='padding:50px'>" + html + "</div>";
        var layerOption = subOp.layerOption || {};
        layerOption = $.extend({
                success: function(layero, index) {
                    $.initComponment($(layero));
                    $("#" + subOp.id + '_edit_cancel_btn').click(function() {
                        layer.close(index);
                    });

                    var subModel = new tonto.Model(subOp.id, subOp.columns, {
                        server: false,
                        pattern: "edit",
                        submitClick: function() {
                            if (subModel.formBody.valid()) {
                                var d = subModel.getFormData();
                                if (typeof column.beforeAddHandler === 'function') {
                                    if (column.beforeAddHandler(d, subModel, index) === false) {
                                        return;
                                    }
                                }

                                that.fillSubEdit(column, d, model, com ? com.id : null);
                                layer.close(index);
                            }
                        }
                    });

                    if (com) {
                        subModel.setData(com.data);
                    }
                }
            },
            layerOption);
        var index = $.openPageLayer(html, layerOption);
    },
    generateViewFormColspan: function(column, options) {
        if (typeof column.generateViewFormColspan === 'function') {
            return column.generateViewFormColspan(column, options);
        }
        return column.colspan || options.maxColspan;
    },
    generateViewFormHtml: function(column, isFirst, options) {
        if (typeof column.generateViewFormHtml === 'function') {
            return column.generateViewFormHtml(column, isFirst, options);
        }
        var colspan = column.colspan || options.maxColspan;
        var html = '<label for="' + column.name + '" class="col-sm-' + (isFirst ? options.firstLabelSize : options.labelSize) + ' control-label">' + column.title + '：</label>\n';

        var colCount = column.colCount ? column.colCount : ((colspan - 1) * (options.inputSize + options.labelSize) + options.inputSize);
        html += '<div name="' + column.name + '" class="col-sm-' + colCount + '"></div>\n';
        return {
            colspan: colspan,
            html: html
        };
    },
    generateEditFormColspan: function(column, options) {
        if (typeof column.generateEditFormColspan === 'function') {
            return column.generateEditFormColspan(column, options);
        }
        return column.colspan || options.maxColspan;
    },
    generateEditFormHtml: function(column, isFirst, options) {
        if (typeof column.generateEditFormHtml === 'function') {
            return column.generateEditFormHtml(column, isFirst, options);
        }
        var colspan = column.colspan || options.maxColspan,
            required = column.required === 'required',
            html = '<label for="' + column.name + '" class="col-sm-' + (isFirst ? options.firstLabelSize : options.labelSize) + ' control-label">' + (required ? '<i class="required-label fa fa-asterisk"></i>' : '') +
            column.title + '：</label>\n';

        var colCount = column.colCount ? column.colCount : ((colspan - 1) * (options.inputSize + options.labelSize) + options.inputSize);
        html += '<div name="' + column.name + '" class="col-sm-' + colCount + '"></div>\n';
        return {
            colspan: colspan,
            html: html
        };
    }
});

// 子模块域构建器
var _editorFieldBuilder = new _FieldBuilder("EDITOR", {
    initHandler: function(column, model) {
        if (model.config.pattern != 'view') {
            var that = this;
            column.editor = UE.getEditor(model.name + '_' + column.name + '_editor');
            column.editor.ready(function() {
                that.fillEdit(column, model.data, model);
                column.editorReady = true;
            })
        }

        if (model.config.pattern != 'edit') {
            $("#" + model.name + '_' + column.name + '_editor_show_btn').click(function() {
                var content = model.data ? model.data[column.name] : '';
                $.openPageLayer('<div style="padding:40px;padding-right:55px">' + content + '</div>');
            });
        }
    },
    getEditValue: function(column, model) {
        // 获取域EDIT页面值
        if (typeof column.getEditValue === 'function') {
            return column.getEditValue(column, model);
        }
        return column.editor.getContent();
    },
    formDataHandler: function(column, formData, model) {
        // 提交表单数据调用
        if (typeof column.formDataHandler === 'function') {
            return column.formDataHandler(column, formData, model);
        }

        var content = column.editor.getContent();

        if (!content && column.required === 'required') {
            $.errorMessage(column.title + "不能为空");
            return false;
        }

        formData.push({
            name: column.name,
            value: content,
            type: "text",
            required: false
        });
    },
    fillView: function(column, data, model) {
        // VIEW页面填充值时候调用
        if (typeof column.fillView === 'function') {
            return column.fillView(column, data, model);
        }
    },
    fillEdit: function(column, data, model) {
        // EDIT页面填充值时候调用
        if (typeof column.fillEdit === 'function') {
            return column.fillEdit(column, data, model);
        }

        if (column.editorReady === true) {
            var content = data ? data[column.name] : '';
            column.editor.setContent(content);
        }
    },
    generateViewFormHtml: function(column, isFirst, options) {
        if (typeof column.generateViewFormHtml === 'function') {
            return column.generateViewFormHtml(column, isFirst, options);
        }
        var colspan = column.colspan || options.maxColspan;
        var html = '<label for="' + column.name + '" class="col-sm-' + (isFirst ? options.firstLabelSize : options.labelSize) + ' control-label">' + column.title + '：</label>\n';

        var colCount = column.colCount ? column.colCount : ((colspan - 1) * (options.inputSize + options.labelSize) + options.inputSize);
        html += '<div name="' + column.name + '" class="col-sm-' + colCount + '"><label class="control-label"><a href="javascript:void(0)" id="' + options.id + '_' + column.name + '_editor_show_btn">查看富文本</a></label></div>\n';
        return {
            colspan: colspan,
            html: html
        };
    },
    generateEditFormHtml: function(column, isFirst, options) {
        if (typeof column.generateEditFormHtml === 'function') {
            return column.generateEditFormHtml(column, isFirst, options);
        }
        var colspan = column.colspan || options.maxColspan,
            required = column.required === 'required',
            html = '<label for="' + column.name + '" class="col-sm-' + (isFirst ? options.firstLabelSize : options.labelSize) + ' control-label">' + (required ? '<i class="required-label fa fa-asterisk"></i>' : '') +
            column.title + '：</label>\n';

        var colCount = column.colCount ? column.colCount : ((colspan - 1) * (options.inputSize + options.labelSize) + options.inputSize);
        var height = column.height || "500px";
        html += '<div name="' + column.name + '" class="col-sm-' + colCount + '"><script type="text/plain" id="' + options.id + '_' + column.name + '_editor" style="width:100%;height:' + height + ';"></script></div>\n';
        return {
            colspan: colspan,
            html: html
        };
    }
});

if (!window.toton) window.toton = {};
window.tonto.Model = _Model;
window.tonto.FieldBuilder = _FieldBuilder;