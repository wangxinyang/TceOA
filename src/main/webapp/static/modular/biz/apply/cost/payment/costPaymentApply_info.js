/**
 * 初始化费用申请详情对话框
 */
var CostPaymentApplyInfoDlg = {
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
        'applytime': {
            validators: {
                notEmpty: {
                    message: '申请日期不能为空'
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
        'reason': {
            validators: {
                notEmpty: {
                    message: '申请事由不能为空'
                }
            }
        },
        'price': {
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
CostPaymentApplyInfoDlg.clearData = function() {
    this.costApplyInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
CostPaymentApplyInfoDlg.set = function(key, val) {
    this.costApplyInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
CostPaymentApplyInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
CostPaymentApplyInfoDlg.close = function() {
    parent.layer.close(window.parent.CostApply.layerIndex);
}

/**
 * 收集数据
 */
CostPaymentApplyInfoDlg.collectData = function() {
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
CostPaymentApplyInfoDlg.goBack = function() {
    window.history.back();
}

// 获取详情数据
CostPaymentApplyInfoDlg.getPaymentApplyDetail = function() {
    var param = {"id": $("#id").val(),
        "deptid": $("#deptid").val(),
        "location": $("#location").val(),
        "applytime": $("#applytime").val(),
        "receivingunit": $("#receivingunit").val(),
        "bank": $("#bank").val(),
        "account": $("#account").val(),
        "deadline": $("#deadline").val(),
        "reason": $("#reason").val(),
        "price": $("#price").val(),
        "assignee": $("#assignee").val()};
    return param;
}

/**
 * 保存操作
 */
CostPaymentApplyInfoDlg.saveSubmit = function() {
    if (!this.validate()) {
        return;
    }
    var param = CostPaymentApplyInfoDlg.getPaymentApplyDetail();
    var jsonTemp = JSON.stringify(param);
    //提交信息
    $.ajax({
        type: "post",
        url: Feng.ctxPath + "/cost/apply/payment/save",
        data: jsonTemp,
        contentType:"application/json;charset=utf-8",
        dataType: "json",
        success: function (data) {
            if (data.code == 200) {
                Feng.success("保存成功!");
            } else {
                eng.error("保存失败!" + data.message + "!");
            }
            window.location.href = Feng.ctxPath + "/cost/apply/payment";
        },
        error: function (data) {
            Feng.error("保存失败!" + data.message + "!");
        }
    });
}

/**
 * 提交添加
 */
CostPaymentApplyInfoDlg.addSubmit = function() {
    if (!this.validate()) {
        return;
    }
    var param = CostPaymentApplyInfoDlg.getPaymentApplyDetail();
    var jsonTemp = JSON.stringify(param);
    //提交信息
    $.ajax({
        type: "post",
        url: Feng.ctxPath + "/cost/apply/payment/add",
        data: jsonTemp,
        contentType:"application/json;charset=utf-8",
        dataType: "json",
        success: function (data) {
            if (data.code == 200) {
                Feng.success("提交流程成功!");
            } else {
                eng.error("提交流程失败!" + data.message + "!");
            }
            window.location.href = Feng.ctxPath + "/cost/apply/payment";
        },
        error: function (data) {
            Feng.error("提交流程失败!" + data.message + "!");
        }
    });
}

/**
 * 提交修改
 */
CostPaymentApplyInfoDlg.editSubmit = function() {
    if (!this.validate()) {
        return;
    }
    var param = CostPaymentApplyInfoDlg.getPaymentApplyDetail();
    var jsonTemp = JSON.stringify(param);
    //提交信息
    $.ajax({
        type: "post",
        url: Feng.ctxPath + "/cost/apply/payment/update",
        data: jsonTemp,
        contentType:"application/json;charset=utf-8",
        dataType: "json",
        success: function (data) {
            if (data.code == 200) {
                Feng.success("修改成功!");
            } else {
                Feng.error("修改失败!" + data.message + "!");
            }
            window.location.href = Feng.ctxPath + "/cost/apply/payment";
        },
        error: function (data) {
            Feng.error("修改失败!" + data.message + "!");
        }
    });
}

/**
 * 显示部门选择的树
 *
 * @returns
 */
CostPaymentApplyInfoDlg.showDeptSelectTree = function () {
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
CostPaymentApplyInfoDlg.onClickDept = function (e, treeId, treeNode) {
    var selVal = CostPaymentApplyInfoDlg.deptZtree.getSelectedVal();
    $("#departs").attr("value", selVal);
    $("#deptid").attr("value", treeNode.id);
};

/**
 * 验证数据
 */
CostPaymentApplyInfoDlg.validate = function () {
    $('#costPaymentForm').data("bootstrapValidator").resetForm();
    $('#costPaymentForm').bootstrapValidator('validate');
    return $("#costPaymentForm").data('bootstrapValidator').isValid();
};

$(function() {
    Feng.initValidator("costPaymentForm", CostPaymentApplyInfoDlg.validateFields);

    var ztree = new $ZTree("treeDemo", "/dept/tree");
    ztree.bindOnClick(CostPaymentApplyInfoDlg.onClickDept);
    ztree.init();
    CostPaymentApplyInfoDlg.deptZtree = ztree;

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
});
