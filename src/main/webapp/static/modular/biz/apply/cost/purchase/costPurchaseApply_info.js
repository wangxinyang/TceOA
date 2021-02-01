/**
 * 初始化费用申请详情对话框
 */
var CostPurchaseApplyInfoDlg = {
    costPurchaseApplyInfoData : {},
    deptZtree: null,
    validateFields: {
        'location': {
            validators: {
                notEmpty: {
                    message: '地区不能为空'
                }
            }
        },
        'departs': {
            validators: {
                notEmpty: {
                    message: '部门不能为空'
                }
            }
        },
        'projectid': {
            validators: {
                notEmpty: {
                    message: '项目名称不能为空'
                }
            }
        },
        'receivingunit': {
            validators: {
                notEmpty: {
                    message: '收款单位(员工姓名)不能为空'
                }
            }
        },
        'bank': {
            validators: {
                notEmpty: {
                    message: '开户行不能为空'
                }
            }
        },
        'account': {
            validators: {
                notEmpty: {
                    message: '账号不能为空'
                }
            }
        },
        'applytime': {
            validators: {
                notEmpty: {
                    message: '申请日期不能为空'
                }
            }
        },
        'use': {
            validators: {
                notEmpty: {
                    message: '用途不能为空'
                }
            }
        },
        'goods0': {
            validators: {
                notEmpty: {
                    message: '商品名称不能为空'
                }
            }
        },
        'num0': {
            validators: {
                notEmpty: {
                    message: '数量不能为空'
                },
                digits: {
                    message: '只能为数字'
                }
            }
        },
        'price0': {
            validators: {
                notEmpty: {
                    message: '预计单价不能为空'
                },
                regexp: {
                    regexp : /^\d+(\.\d{0,2})?$/i,
                    message: '只能为数字含两位小数'
                }
            }
        },
        'supplier0': {
            validators: {
                notEmpty: {
                    message: '供应商不能为空'
                }
            }
        },
        'inquiry_goods0': {
            validators: {
                notEmpty: {
                    message: '商品名称不能为空'
                }
            }
        },
        'inquiry_num0': {
            validators: {
                notEmpty: {
                    message: '数量不能为空'
                },
                digits: {
                    message: '只能为数字'
                }
            }
        },
        'inquiry_price0': {
            validators: {
                notEmpty: {
                    message: '预计单价不能为空'
                },
                regexp: {
                    regexp : /^\d+(\.\d{0,2})?$/i,
                    message: '只能为数字含两位小数'
                }
            }
        },
        'inquiry_quotationunit0': {
            validators: {
                notEmpty: {
                    message: '报价单位不能为空'
                }
            }
        },
        'assignee': {
            validators: {
                notEmpty: {
                    message: '审批人不能为空'
                }
            }
        }
    }
};

/**
 * 清除数据
 */
CostPurchaseApplyInfoDlg.clearData = function() {
    this.costPurchaseApplyInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
CostPurchaseApplyInfoDlg.set = function(key, val) {
    this.costPurchaseApplyInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
CostPurchaseApplyInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
CostPurchaseApplyInfoDlg.goBack = function() {
    window.history.back();
}

/**
 * 收集数据
 */
CostPurchaseApplyInfoDlg.collectData = function() {
    this
    .set('id')
    .set('reqno')
    .set('deptid')
    .set('projectid')
    .set('location')
    .set('applytime')
    .set('use')
    .set('totalfee')
    .set('createtime')
    .set('state')
    .set('userid')
    .set('processId')
    .set('assignee')
    .set('leadernote')
    .set('leadertime')
    .set('assistantnote')
    .set('assistantime')
    .set('cashernote')
    .set('cashertime')
    .set('deputynote')
    .set('deputytime')
    .set('managernote')
    .set('managertime');
}

// 获取详情数据
CostPurchaseApplyInfoDlg.getPurchaseApplyDetail = function() {
    var purchaseGoodsConfirmList = new Array();
    var tableConfirmData = {};
    $("#confirm_table tr").each(function (trindex, tritem) {
        tableConfirmData = {};
        $(tritem).find("input").each(function (tdindex, tditem) {
            tableConfirmData[$(tditem).attr("id")] = $(tditem).val();
        });
        if (!$.isEmptyObject(tableConfirmData)) {
            purchaseGoodsConfirmList.push(tableConfirmData);
        }
    });
    var purchaseGoodsInquiryList = new Array();
    if (isInquiryTableShow) {
        var tableInquuiryData = {};
        $("#inquiry_table tr").each(function (trindex, tritem) {
            tableInquuiryData = {};
            $(tritem).find("input").each(function (tdindex, tditem) {
                tableInquuiryData[$(tditem).attr("id")] = $(tditem).val();
            });
            if (!$.isEmptyObject(tableInquuiryData)) {
                purchaseGoodsInquiryList.push(tableInquuiryData);
            }
        });
    }

    var param = {"purchaseGoodsConfirmList" : purchaseGoodsConfirmList,
        "purchaseGoodsInquiryList" : purchaseGoodsInquiryList,
        "id": $("#id").val(),
        "deptid": $("#deptid").val(),
        "location": $("#location").val(),
        "applytime": $("#applytime").val(),
        "receivingunit": $("#receivingunit").val(),
        "bank": $("#bank").val(),
        "account": $("#account").val(),
        "projectid": $("#projectid").val().split(":")[0],
        "use": $("#use").val(),
        "assignee": $("#assignee").val()};
    return param;
}

/**
 * 保存操作
 */
CostPurchaseApplyInfoDlg.saveSubmit = function() {
    if (!this.validate()) {
        return;
    }
    var param = CostPurchaseApplyInfoDlg.getPurchaseApplyDetail();
    var jsonTemp = JSON.stringify(param);
    //提交信息
    $.ajax({
        type: "post",
        url: Feng.ctxPath + "/cost/apply/purchase/save",
        data: jsonTemp,
        contentType:"application/json;charset=utf-8",
        dataType: "json",
        success: function (data) {
            if (data.code == 200) {
                Feng.success("保存成功!");
            } else {
                eng.error("保存失败!" + data.message + "!");
            }
            window.location.href = Feng.ctxPath + "/cost/apply/purchase";
        },
        error: function (data) {
            Feng.error("保存失败!" + data.message + "!");
        }
    });
}

/**
 * 提交添加
 */
CostPurchaseApplyInfoDlg.addSubmit = function() {
    if (!this.validate()) {
        return;
    }
    var param = CostPurchaseApplyInfoDlg.getPurchaseApplyDetail();
    var jsonTemp = JSON.stringify(param);
    //提交信息
    $.ajax({
        type: "post",
        url: Feng.ctxPath + "/cost/apply/purchase/add",
        data: jsonTemp,
        contentType:"application/json;charset=utf-8",
        dataType: "json",
        success: function (data) {
            if (data.code == 200) {
                Feng.success("提交流程成功!");
            } else {
                eng.error("提交流程失败!" + data.message + "!");
            }
            window.location.href = Feng.ctxPath + "/cost/apply/purchase";
        },
        error: function (data) {
            Feng.error("提交流程失败!" + data.message + "!");
        }
    });
}

/**
 * 提交修改
 */
CostPurchaseApplyInfoDlg.editSubmit = function() {
    if (!this.validate()) {
        return;
    }
    var param = CostPurchaseApplyInfoDlg.getPurchaseApplyDetail();
    var jsonTemp = JSON.stringify(param);
    //提交信息
    $.ajax({
        type: "post",
        url: Feng.ctxPath + "/cost/apply/purchase/update",
        data: jsonTemp,
        contentType:"application/json;charset=utf-8",
        dataType: "json",
        success: function (data) {
            if (data.code == 200) {
                Feng.success("修改成功!");
            } else {
                eng.error("修改失败!" + data.message + "!");
            }
            window.location.href = Feng.ctxPath + "/cost/apply/purchase";
        },
        error: function (data) {
            Feng.error("修改失败!" + data.message + "!");
        }
    });
}

/**
 * 显示采购商品询价table
 */
CostPurchaseApplyInfoDlg.showInquiryTable = function () {
    $("#inquiry_table").show();
    $("#addtrinquirydiv").show();
    $("#showInquiry").hide();
    $("#hiddenInquiry").show();
    isInquiryTableShow = true;
}

/**
 * 隐藏采购商品询价table
 */
CostPurchaseApplyInfoDlg.hiddenInquiryTable = function () {
    var purchaseGoodsInquiryList = new Array();
    var tableInquuiryData = {};
    $("#inquiry_table tr").each(function (trindex, tritem) {
        tableInquuiryData = {};
        $(tritem).find("input").each(function (tdindex, tditem) {
            tableInquuiryData[$(tditem).attr("id")] = $(tditem).val();
        });
        if (!$.isEmptyObject(tableInquuiryData)) {
            purchaseGoodsInquiryList.push(tableInquuiryData);
        }
    });
    var param = {"purchaseGoodsInquiryList" : purchaseGoodsInquiryList};
    var jsonTemp = JSON.stringify(param);

    $.ajax({
        type: "post",
        url: Feng.ctxPath + "/cost/apply/purchase/deleteGoods",
        data: jsonTemp,
        contentType:"application/json;charset=utf-8",
        dataType: "json",
        success: function (data) {
            $("#inquiry_table tr:not(:first)").empty("");
            $("#inquiry_table").hide();
            $("#addtrinquirydiv").hide();
            $("#showInquiry").show();
            $("#hiddenInquiry").hide();
            isInquiryTableShow = false;
        },
        error: function (data) {
            Feng.error("删除商品询价信息失败!" + data.message + "!");
        }
    });

}

/**
 * 添加行
 */
CostPurchaseApplyInfoDlg.addConfirmRow = function() {
    var index = $("#confirm_table").find("tr").length;
    var table = $("#confirm_table");
    var tr= $('<tr class="confirmContent" id="' + (index-1) + '"><td class="col-md-6"><div class="form-group"><div class="col-sm-12">' +
        '<input class="form-control" type="text" id="goods" name="goods' + (index-1) + '"/></div></div></td>' +
        '<td class="col-md-6"><div class="form-group"><div class="col-sm-12">' +
        '<input class="form-control" type="text" id="type" name="type' + (index-1) + '"/></div></div></td>' +
        '<td class="col-md-6"><div class="form-group"><div class="col-sm-12">' +
        '<input class="form-control" type="text" id="num" name="num' + (index-1) + '" onblur="CostPurchaseApplyInfoDlg.countTotal(this);return false;"/></div></div></td>' +
        '<td class="col-md-6"><div class="form-group"><div class="col-sm-12">' +
        '<input class="form-control" type="text" id="price" name="price' + (index-1) + '" onblur="CostPurchaseApplyInfoDlg.countTotal(this);return false;"/></div></div></td>' +
        '<td class="col-md-6"><div class="form-group"><div class="col-sm-12">' +
        '<input class="form-control" type="text" id="totalfee" name="totalfee' + (index-1) + '" readonly="readonly"/>' +
        '<td class="col-md-6"><div class="form-group"><div class="col-sm-12">' +
        '<input class="form-control" type="text" id="supplier" name="supplier' + (index-1) + '"/>' +
        '<td class="col-md-6"><div class="form-group"><div class="col-sm-12">' +
        '<input class="form-control" type="text" id="note" name="note' + (index-1) + '"/>' +
        '<td class="col-md-6"><button type="button" class="btn btn-primary " onclick="CostPurchaseApplyInfoDlg.deleteConfirmRow(this, \'add\', 0)" id=""><i class="fa fa-minus"></i>&nbsp;删除</button></td></tr>');
    table.append(tr);
    sortConfirmTrNumber();
    $('#costPurchaseForm').bootstrapValidator('addField', 'goods' + (index-1), {
        validators: {
            notEmpty: {
                message: '商品名称不能为空'
            }
        }
    });
    $('#costPurchaseForm').bootstrapValidator('addField', 'num' + (index-1), {
        validators: {
            notEmpty: {
                message: '数量不能为空'
            },
            digits: {
                message: '只能为数字'
            }
        }
    });
    $('#costPurchaseForm').bootstrapValidator('addField', 'price' + (index-1), {
        validators: {
            notEmpty: {
                message: '预计单价不能为空'
            },
            regexp: {
                regexp : /^\d+(\.\d{0,2})?$/i,
                message: '只能为数字含两位小数'
            }
        }
    });
    $('#costPurchaseForm').bootstrapValidator('addField', 'supplier' + (index-1), {
        validators: {
            notEmpty: {
                message: '供应商不能为空'
            }
        }
    });
}

function countTotalFee() {
    var countFee = 0;
    $("#confirm_table").find(".confirmContent").each(function (index, obj) {
        var rowFeeTotal = $("input[name='totalfee" + index + "']").val();
        countFee = Math.floor(parseFloat(countFee * 100 + rowFeeTotal * 100)) / 100;
    });
    $("#countfee").val(countFee);
}

/**
 * 删除行
 */
CostPurchaseApplyInfoDlg.deleteConfirmRow = function(tdobject, type, id) {
    var td = $(tdobject);
    if (type == 'add') {
        td.parents("tr").remove();
    } else {
        var ajax = new $ax(Feng.ctxPath + "/cost/apply/purchase/deleteGoodsById", function (data) {
            // Feng.success("开始流程成功!");
            td.parents("tr").remove();
        }, function (data) {
            Feng.error("删除信息失败!" + data.message + "!");
        });
        ajax.set("id", id);
        ajax.start();
    }
    sortConfirmTrNumber();
    // 填充计算所有的总价
    countTotalFee();
}

/**
 * 增加删除行之后对tr的id的序号重新排序
 */
function sortConfirmTrNumber() {
    $("#confirm_table").find(".confirmContent").each(function (index, obj) {
        // 修改tr的id的index值
        $(obj).attr('id', index);
        // 改变td内容中name的index值
        // console.log($(obj).find(".form-control"));
        $(obj).find(".form-control").each(function (i, elements) {
            var name = $(elements).attr("id");
            $(elements).attr("name", name + index);
        })
    });
}

/**
 * 添加行
 */
CostPurchaseApplyInfoDlg.addInquiryRow = function() {
    isInquiryTableShow = true;
    var index = $("#inquiry_table").find("tr").length;
    var table = $("#inquiry_table");
    var tr= $('<tr class="inquiryContent" id="' + (index-1) + '"><td class="col-md-6"><div class="form-group"><div class="col-sm-12">' +
        '<input class="form-control" type="text" id="goods" name="inquiry_goods' + (index-1) + '"/></div></div></td>' +
        '<td class="col-md-6"><div class="form-group"><div class="col-sm-12">' +
        '<input class="form-control" type="text" id="type" name="inquiry_type' + (index-1) + '"/></div></div></td>' +
        '<td class="col-md-6"><div class="form-group"><div class="col-sm-12">' +
        '<input class="form-control" type="text" id="num" name="inquiry_num' + (index-1) + '" onblur="CostPurchaseApplyInfoDlg.countInquiryTotal(this);return false;"/></div></div></td>' +
        '<td class="col-md-6"><div class="form-group"><div class="col-sm-12">' +
        '<input class="form-control" type="text" id="price" name="inquiry_price' + (index-1) + '" onblur="CostPurchaseApplyInfoDlg.countInquiryTotal(this);return false;"/></div></div></td>' +
        '<td class="col-md-6"><div class="form-group"><div class="col-sm-12">' +
        '<input class="form-control" type="text" id="totalfee" name="inquiry_totalfee' + (index-1) + '" readonly="readonly"/>' +
        '<td class="col-md-6"><div class="form-group"><div class="col-sm-12">' +
        '<input class="form-control" type="text" id="quotationunit" name="inquiry_quotationunit' + (index-1) + '"/>' +
        '<td class="col-md-6"><div class="form-group"><div class="col-sm-12">' +
        '<input class="form-control" type="text" id="note" name="inquiry_note' + (index-1) + '"/>' +
        '<td class="col-md-6"><button type="button" class="btn btn-primary " onclick="CostPurchaseApplyInfoDlg.deleteInquiryRow(this, \'add\', 0)" id=""><i class="fa fa-minus"></i>&nbsp;删除</button></td></tr>');
    table.append(tr);
    sortInquiryTrNumber();
    $('#costPurchaseForm').bootstrapValidator('addField', 'inquiry_goods' + (index-1), {
        validators: {
            notEmpty: {
                message: '商品名称不能为空'
            }
        }
    });
    $('#costPurchaseForm').bootstrapValidator('addField', 'inquiry_num' + (index-1), {
        validators: {
            notEmpty: {
                message: '数量不能为空'
            },
            digits: {
                message: '只能为数字'
            }
        }
    });
    $('#costPurchaseForm').bootstrapValidator('addField', 'inquiry_price' + (index-1), {
        validators: {
            notEmpty: {
                message: '预计单价不能为空'
            },
            regexp: {
                regexp : /^\d+(\.\d{0,2})?$/i,
                message: '只能为数字含两位小数'
            }
        }
    });
    $('#costPurchaseForm').bootstrapValidator('addField', 'inquiry_supplier' + (index-1), {
        validators: {
            notEmpty: {
                message: '报价单位不能为空'
            }
        }
    });
}

/**
 * 删除行
 */
CostPurchaseApplyInfoDlg.deleteInquiryRow = function(tdobject, type, id) {
    var td = $(tdobject);
    if (type == 'add') {
        td.parents("tr").remove();
    } else {
        var ajax = new $ax(Feng.ctxPath + "/cost/apply/purchase/deleteGoodsById", function (data) {
            // Feng.success("开始流程成功!");
            td.parents("tr").remove();
        }, function (data) {
            Feng.error("删除信息失败!" + data.message + "!");
        });
        ajax.set("id", id);
        ajax.start();
    }
    sortInquiryTrNumber();
}

/**
 * 增加删除行之后对tr的id的序号重新排序
 */
function sortInquiryTrNumber() {
    $("#inquiry_table").find(".inquiryContent").each(function (index, obj) {
        // 修改tr的id的index值
        $(obj).attr('id', index);
        // 改变td内容中name的index值
        // console.log($(obj).find(".form-control"));
        $(obj).find(".form-control").each(function (i, elements) {
            var name = $(elements).attr("id");
            $(elements).attr("name", 'inquiry_' + name + index);
        })
    });
}

/**
 * 小计金额的计算
 */
CostPurchaseApplyInfoDlg.countTotal = function(obj) {
    var name = $(obj).attr('name');
    var id = $(obj).attr('id');
    var index = name.substr(id.length, name.length);
    // 数量
    var num = $("input[name='num" + index + "']").val();
    if(num == '' || num == undefined){
        num = 0;
    }
    // 预计单价
    var price = $("input[name='price" + index + "']").val();
    if(price == '' || price == undefined){
        price = 0;
    }
    var totalfee = Math.floor(parseFloat(num * price * 100))/100;

    $("input[name='totalfee" + index + "']").val(totalfee);

    // 填充计算所有的总价
    countTotalFee();
}

/**
 * 小计金额的计算
 */
CostPurchaseApplyInfoDlg.countInquiryTotal = function(obj) {
    var name = $(obj).attr('name');
    var id = "inquiry_" + $(obj).attr('id');
    var index = name.substr(id.length, name.length);
    // 数量
    var num = $("input[name='inquiry_num" + index + "']").val();
    if(num == '' || num == undefined){
        num = 0;
    }
    // 预计单价
    var price = $("input[name='inquiry_price" + index + "']").val();
    if(price == '' || price == undefined){
        price = 0;
    }
    var totalfee = Math.floor(parseFloat(num * price * 100))/100;
    $("input[name='inquiry_totalfee" + index + "']").val(totalfee);
}

/**
 * 显示部门选择的树
 *
 * @returns
 */
CostPurchaseApplyInfoDlg.showDeptSelectTree = function () {
    Feng.showInputTree("departs", "menuContent");
};

/**
 * 点击部门input框时
 *
 * @param e
 * @param treeId
 * @param treeNode
 * @returns
 */
CostPurchaseApplyInfoDlg.onClickDept = function (e, treeId, treeNode) {
    var selVal = CostPurchaseApplyInfoDlg.deptZtree.getSelectedVal();
    $("#departs").attr("value", selVal);
    $("#deptid").attr("value", treeNode.id);
};

/**
 * 验证数据
 */
CostPurchaseApplyInfoDlg.validate = function () {
    $('#costPurchaseForm').data("bootstrapValidator").resetForm();
    $('#costPurchaseForm').bootstrapValidator('validate');
    return $("#costPurchaseForm").data('bootstrapValidator').isValid();
};

var isInquiryTableShow = false;
$(function() {
    isInquiryTableShow = false;

    Feng.initValidator("costPurchaseForm", CostPurchaseApplyInfoDlg.validateFields);

    var ztree = new $ZTree("treeDemo", "/dept/tree");
    ztree.bindOnClick(CostPurchaseApplyInfoDlg.onClickDept);
    ztree.init();
    CostPurchaseApplyInfoDlg.deptZtree = ztree;

    // 填充计算所有的总价
    countTotalFee();

    // 初始化项目的下拉框选项
    $(".selectpicker").selectpicker({
        noneSelectedText : '请选择'//默认显示内容
    });
    $(window).on('load', function() {
        if ($("#projectValue").val() != '' && $("#projectValue").val() != undefined) {
            $('.selectpicker').selectpicker('val', $("#projectValue").val());
        } else {
            $('.selectpicker').selectpicker('val', '');
        }
        $('.selectpicker').selectpicker('refresh');
    });
    var ajax = new $ax(Feng.ctxPath + "/project/manager/project/info", function(data) {
        var select = $("#projectid");
        var html = '';
        html = '<option value="">请选择</option>';
        Object.keys(data).forEach(function(index){
            html += '<option value="' + data[index].id + ':' + data[index].code + '">' + data[index].name+ '</option>';
        });
        select.html(html);
        //必须加，刷新select
        select.selectpicker('refresh');
    }, function(data){
        Feng.error("查询项目信息出错!");
    });
    ajax.start();

    // 项目下拉框选择监听
    $("#projectid").change(function() {
        // 获取下拉框选中的值
        var idAndCode = $("#projectid").val();
        // 分解字符串获取项目编号
        var code = idAndCode.split(":")[1];
        $("#code").val(code);
    });

    // 初始化审批人的下拉框选项
    $("#assignee").selectpicker({
        noneSelectedText : '请选择'//默认显示内容
    });
    $(window).on('load', function() {
        var assigneeValue = $("#assigneeValue").val();
        if(assigneeValue == '' || assigneeValue == undefined){
            $("#assignee").selectpicker('val', '');
        } else {
            $("#assignee").selectpicker('val', assigneeValue);
        }
        $("#assignee").selectpicker('refresh');
    });
    var ajax = new $ax(Feng.ctxPath + "/process/setting/users", function(data) {
        var select = $("#assignee");
        var html = '';
        Object.keys(data).forEach(function(index){
            html += '<option value="' + data[index].position + '">' + data[index].username+ '</option>';
        });
        select.html(html);
        //必须加，刷新select
        select.selectpicker('refresh');
    }, function(data){
        Feng.error("查询审批人员信息出错!");
    });
    ajax.set("processId", "costApplyProcess");
    ajax.start();

    // 初始化下拉框
    $("#location").val($("#locationValue").val());

    // 初始化的时候对validater动态添加
    var index = $("#confirm_table").find("tr").length;
    for (var i=0; i<index-2; i++) {
        $('#costPurchaseForm').bootstrapValidator('addField', 'goods' + (i+1), {
            validators: {
                notEmpty: {
                    message: '商品名称不能为空'
                }
            }
        });
        $('#costPurchaseForm').bootstrapValidator('addField', 'num' + (i+1), {
            validators: {
                notEmpty: {
                    message: '数量不能为空'
                },
                digits: {
                    message: '只能为数字'
                }
            }
        });
        $('#costPurchaseForm').bootstrapValidator('addField', 'price' + (i+1), {
            validators: {
                notEmpty: {
                    message: '预计单价不能为空'
                },
                regexp: {
                    regexp : /^\d+(\.\d{0,2})?$/i,
                    message: '只能为数字含两位小数'
                }
            }
        });
        $('#costPurchaseForm').bootstrapValidator('addField', 'supplier' + (i+1), {
            validators: {
                notEmpty: {
                    message: '供应商不能为空'
                }
            }
        });
    }

    var index = $("#inquiry_table").find("tr").length;
    // 有数据的时候 商品询价的是否采集数据的flag为true
    if ($("#inquiry_table").is(':visible')) {
        if (index >= 2) {
            isInquiryTableShow = true;
        }
    }
    for (var i=0; i<index-2; i++) {
        $('#costPurchaseForm').bootstrapValidator('addField', 'inquiry_goods' + (i+1), {
            validators: {
                notEmpty: {
                    message: '商品名称不能为空'
                }
            }
        });
        $('#costPurchaseForm').bootstrapValidator('addField', 'inquiry_num' + (i+1), {
            validators: {
                notEmpty: {
                    message: '数量不能为空'
                },
                digits: {
                    message: '只能为数字'
                }
            }
        });
        $('#costPurchaseForm').bootstrapValidator('addField', 'inquiry_price' + (i+1), {
            validators: {
                notEmpty: {
                    message: '预计单价不能为空'
                },
                regexp: {
                    regexp : /^\d+(\.\d{0,2})?$/i,
                    message: '只能为数字含两位小数'
                }
            }
        });
        $('#costPurchaseForm').bootstrapValidator('addField', 'inquiry_supplier' + (i+1), {
            validators: {
                notEmpty: {
                    message: '报价单位不能为空'
                }
            }
        });
    }
});
