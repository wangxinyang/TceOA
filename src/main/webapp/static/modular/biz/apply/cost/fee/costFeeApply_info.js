/**
 * 初始化费用申请详情对话框
 */
var CostApplyInfoDlg = {
    costApplyInfoData : {},
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
        'name0': {
            validators: {
                notEmpty: {
                    message: '费用名称不能为空'
                }
            }
        },
        'price0': {
            validators: {
                notEmpty: {
                    message: '金额不能为空'
                },
                regexp: {
                    regexp : /^\d+(\.\d{0,2})?$/i,
                    message: '只能为数字含两位小数'
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
CostApplyInfoDlg.clearData = function() {
    this.costApplyInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
CostApplyInfoDlg.set = function(key, val) {
    this.costApplyInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
CostApplyInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
CostApplyInfoDlg.close = function() {
    parent.layer.close(window.parent.CostApply.layerIndex);
}

/**
 * 收集数据
 */
CostApplyInfoDlg.collectData = function() {
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


/**
 * 关闭此对话框
 */
CostApplyInfoDlg.goBack = function() {
    window.history.back();
}

// 获取详情数据
CostApplyInfoDlg.getFeeApplyDetail = function() {
    var feeDetailList = new Array();
    var tableData = {};
    $("#para_table tr").each(function (trindex, tritem) {
        tableData = {};
        $(tritem).find("input").each(function (tdindex, tditem) {
            tableData[$(tditem).attr("id")] = $(tditem).val();
        });
        if (!$.isEmptyObject(tableData)) {
            feeDetailList.push(tableData);
        }
    });

    var param = {"feeDetailDtoList" : feeDetailList,
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
CostApplyInfoDlg.saveSubmit = function() {
    if (!this.validate()) {
        return;
    }
    var param = CostApplyInfoDlg.getFeeApplyDetail();
    var jsonTemp = JSON.stringify(param);
    //提交信息
    $.ajax({
        type: "post",
        url: Feng.ctxPath + "/cost/apply/fee/save",
        data: jsonTemp,
        contentType:"application/json;charset=utf-8",
        dataType: "json",
        success: function (data) {
            if (data.code == 200) {
                Feng.success("保存成功!");
            } else {
                eng.error("保存失败!" + data.message + "!");
            }
            window.location.href = Feng.ctxPath + "/cost/apply/fee";
        },
        error: function (data) {
            Feng.error("保存失败!" + data.message + "!");
        }
    });
}

/**
 * 提交添加
 */
CostApplyInfoDlg.addSubmit = function() {
    if (!this.validate()) {
        return;
    }
    var param = CostApplyInfoDlg.getFeeApplyDetail();
    var jsonTemp = JSON.stringify(param);
    //提交信息
    $.ajax({
        type: "post",
        url: Feng.ctxPath + "/cost/apply/fee/add",
        data: jsonTemp,
        contentType:"application/json;charset=utf-8",
        dataType: "json",
        success: function (data) {
            if (data.code == 200) {
                Feng.success("提交流程成功!");
            } else {
                eng.error("提交流程失败!" + data.message + "!");
            }
            window.location.href = Feng.ctxPath + "/cost/apply/fee";
        },
        error: function (data) {
            Feng.error("提交流程失败!" + data.message + "!");
        }
    });
}

/**
 * 提交修改
 */
CostApplyInfoDlg.editSubmit = function() {
    if (!this.validate()) {
        return;
    }
    var param = CostApplyInfoDlg.getFeeApplyDetail();
    var jsonTemp = JSON.stringify(param);
    //提交信息
    $.ajax({
        type: "post",
        url: Feng.ctxPath + "/cost/apply/fee/update",
        data: jsonTemp,
        contentType:"application/json;charset=utf-8",
        dataType: "json",
        success: function (data) {
            if (data.code == 200) {
                Feng.success("修改成功!");
            } else {
                eng.error("修改失败!" + data.message + "!");
            }
            window.location.href = Feng.ctxPath + "/cost/apply/fee";
        },
        error: function (data) {
            Feng.error("修改失败!" + data.message + "!");
        }
    });
}

/**
 * 添加行
 */
CostApplyInfoDlg.addRow = function() {
    var index = $("#para_table").find("tr").length;
    var table = $("#para_table");
    var tr= $('<tr class="feeContent" id="' + (index-1) + '"><td class="col-md-6"><div class="form-group"><div class="col-sm-12">' +
        '<input class="form-control" type="text" id="name" name="name' + (index-1) + '"/></div></div></td>' +
        '<td class="col-md-6"><div class="form-group"><div class="col-sm-12">' +
        '<input class="form-control" type="text" id="outlaypro" name="outlaypro' + (index-1) + '"/></div></div></td>' +
        '<td class="col-md-6"><div class="form-group"><div class="col-sm-12">' +
        '<input class="form-control" type="text" id="price" name="price' + (index-1) + '" onblur="CostApplyInfoDlg.countTotal(this);return false;"/></div></div></td>' +
        '<td class="col-md-6"><div class="form-group"><div class="col-sm-12">' +
        '<input class="form-control" type="text" id="note" name="note' + (index-1) + '"/>' +
        '<td class="col-md-6"><button type="button" class="btn btn-primary " onclick="CostApplyInfoDlg.deleteRow(this, \'add\', 0)" id=""><i class="fa fa-minus"></i>&nbsp;删除</button></td></tr>');
    table.append(tr);
    sortTrNumber();
    $('#costFeeForm').bootstrapValidator('addField', 'name' + (index-1), {
        validators: {
            notEmpty: {
                message: '费用名称不能为空'
            }
        }
    });
    $('#costFeeForm').bootstrapValidator('addField', 'price' + (index-1), {
        validators: {
            notEmpty: {
                message: '预计金额不能为空'
            },
            regexp: {
                regexp : /^\d+(\.\d{0,2})?$/i,
                message: '只能为数字含两位小数'
            }
        }
    });
}

/**
 * 删除行
 */
CostApplyInfoDlg.deleteRow = function(tdobject, type, id) {
    var td = $(tdobject);
    if (type == 'add') {
        td.parents("tr").remove();
    } else {
        var ajax = new $ax(Feng.ctxPath + "/cost/apply/fee/deleteGoodsById", function (data) {
            // Feng.success("开始流程成功!");
            td.parents("tr").remove();
        }, function (data) {
            Feng.error("删除信息失败!" + data.message + "!");
        });
        ajax.set("id", id);
        ajax.start();
    }
    sortTrNumber();
    // 填充计算所有的总价
    countTotalFee();
}

/**
 * 小计金额的计算
 */
CostApplyInfoDlg.countTotal = function(obj) {
    // 填充计算所有的总价
    countTotalFee();
}

function countTotalFee() {
    var countFee = 0;
    $("#para_table").find(".feeContent").each(function (index, obj) {
        var rowFeeTotal = $("input[name='price" + index + "']").val();
        countFee = Math.floor(parseFloat(countFee * 100 + rowFeeTotal * 100)) / 100;
    });
    $("#countfee").val(countFee);
}

/**
 * 增加删除行之后对tr的id的序号重新排序
 */
function sortTrNumber() {
    $("#para_table").find(".feeContent").each(function (index, obj) {
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
 * 显示部门选择的树
 *
 * @returns
 */
CostApplyInfoDlg.showDeptSelectTree = function () {
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
CostApplyInfoDlg.onClickDept = function (e, treeId, treeNode) {
    var selVal = CostApplyInfoDlg.deptZtree.getSelectedVal();
    $("#departs").attr("value", selVal);
    $("#deptid").attr("value", treeNode.id);
};

/**
 * 验证数据
 */
CostApplyInfoDlg.validate = function () {
    $('#costFeeForm').data("bootstrapValidator").resetForm();
    $('#costFeeForm').bootstrapValidator('validate');
    return $("#costFeeForm").data('bootstrapValidator').isValid();
};

$(function() {
    Feng.initValidator("costFeeForm", CostApplyInfoDlg.validateFields);

    var ztree = new $ZTree("treeDemo", "/dept/tree");
    ztree.bindOnClick(CostApplyInfoDlg.onClickDept);
    ztree.init();
    CostApplyInfoDlg.deptZtree = ztree;

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

    var index = $("#para_table").find("tr").length;
    for (var i=0; i<index-2; i++) {
        $('#costFeeForm').bootstrapValidator('addField', 'name' + (i+1), {
            validators: {
                notEmpty: {
                    message: '费用名称不能为空'
                }
            }
        });
        $('#costFeeForm').bootstrapValidator('addField', 'price' + (i+1), {
            validators: {
                notEmpty: {
                    message: '预计金额不能为空'
                },
                regexp: {
                    regexp : /^\d+(\.\d{0,2})?$/i,
                    message: '只能为数字含两位小数'
                }
            }
        });
    }
});
