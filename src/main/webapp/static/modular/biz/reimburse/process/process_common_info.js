/**
 * 报销管理管理初始化
 */
var ProcessConsumeInfoDlg = {
    reimburseConsumeProcessInfoData : {},
    validateFields: {
        'leadernote': {
            validators: {
                notEmpty: {
                    message: '审批信息不能为空'
                }
            }
        },
        'cashernote': {
            validators: {
                notEmpty: {
                    message: '审批信息不能为空'
                }
            }
        },
        'deputynote': {
            validators: {
                notEmpty: {
                    message: '审批信息不能为空'
                }
            }
        },
        'managernote': {
            validators: {
                notEmpty: {
                    message: '审批信息不能为空'
                }
            }
        }
    }
};

ProcessConsumeInfoDlg.goBack = function() {
    window.history.back();
}

ProcessConsumeInfoDlg.approveByAjax = function(type) {
    var data = new Array();
    var tableData = {};
    $(".table-bordered tr").each(function (trindex, tritem) {
        tableData = {};
        $(tritem).find("input").each(function (tdindex, tditem) {
            tableData[$(tditem).attr("id")] = $(tditem).val();
        });
        if (!$.isEmptyObject(tableData)) {
            data.push(tableData);
        }
    });
    param = {
        "leadernote": $("#leadernote").val(),
        "cashernote": $("#cashernote").val(),
        "deputynote": $("#deputynote").val(),
        "managernote": $("#managernote").val(),
        "location": $("#locationValue").val(),
        "taskId": $("#taskId").val(),
        "taskUser": $("#taskUserPosition").val(),
        "taskDefinitionKey": $("#taskDefinitionKey").val(),
        "id": $("#id").val()
    };
    var jsonTemp = JSON.stringify(param);
    var url = '';
    var keyWord = '';
    if(type == 'pass') {
        url = Feng.ctxPath + "/reimburse/process/approve/consume/pass"
        keyWord = "审核";
    } else {
        url = Feng.ctxPath + "/reimburse/process/approve/consume/unPass"
        keyWord = "驳回";
    }
    //提交信息
    $.ajax({
        type: "post",
        url: url,
        data: jsonTemp,
        contentType: "application/json;charset=utf-8",
        dataType: "json",
        success: function (data) {
            if (data.code == 200) {
                Feng.success(keyWord + "通过!");
            } else {
                Feng.error(keyWord + "失败!" + data.message + "!");
            }
            window.location.href = Feng.ctxPath + "/reimburse/process/approve";
        },
        error: function (data) {
            Feng.error(keyWord + "失败!" + data.message + "!");
        }
    });
}

/**
 * 通过审核
 */
ProcessConsumeInfoDlg.pass = function () {
    if (!this.validate()) {
        return;
    }
    this.approveByAjax("pass");
};

/**
 * 未通过审核
 */
ProcessConsumeInfoDlg.unPass = function () {
    this.approveByAjax("unPass");
};


/**
 * 验证数据
 */
ProcessConsumeInfoDlg.validate = function () {
    $('#processInfoForm').data("bootstrapValidator").resetForm();
    $('#processInfoForm').bootstrapValidator('validate');
    return $("#processInfoForm").data('bootstrapValidator').isValid();
};


$(function () {
    Feng.initValidator("processInfoForm", ProcessConsumeInfoDlg.validateFields);
});
