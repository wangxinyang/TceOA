/**
 * 报销管理管理初始化
 */
var ProcessInfoDlg = {
    reimburseProcessInfoData : {},
    validateFields: {
        'assistantnote': {
            validators: {
                notEmpty: {
                    message: '审批信息不能为空'
                }
            }
        },
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

ProcessInfoDlg.goBack = function() {
    window.history.back();
}

ProcessInfoDlg.approveByAjax = function(type) {
    param = {
        "leadernote": $("#leadernote").val(),
        "assistantnote": $("#assistantnote").val(),
        "cashernote": $("#cashernote").val(),
        "deputynote": $("#deputynote").val(),
        "managernote": $("#managernote").val(),
        "location": $("#locationValue").val(),
        "taskId": $("#taskId").val(),
        "taskUser": $("#taskUser").val(),
        "taskDefinitionKey": $("#taskDefinitionKey").val(),
        "id": $("#id").val()
    };
    var jsonTemp = JSON.stringify(param);
    var url = '';
    var keyWord = '';
    if(type == 'pass') {
        url = Feng.ctxPath + "/cost/apply/approve/pass"
        keyWord = "审核";
    } else {
        url = Feng.ctxPath + "/cost/apply/approve/unPass"
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
            window.location.href = Feng.ctxPath + "/cost/apply/approve";
        },
        error: function (data) {
            Feng.error(keyWord + "失败!" + data.message + "!");
        }
    });
}

/**
 * 通过审核
 */
ProcessInfoDlg.pass = function () {
    if (!this.validate()) {
        return;
    }
    this.approveByAjax("pass");
};

/**
 * 未通过审核
 */
ProcessInfoDlg.unPass = function () {
    this.approveByAjax("unPass");
};

/**
 * 验证数据
 */
ProcessInfoDlg.validate = function () {
    $('#processForm').data("bootstrapValidator").resetForm();
    $('#processForm').bootstrapValidator('validate');
    return $("#processForm").data('bootstrapValidator').isValid();
};


$(function () {
    Feng.initValidator("processForm", ProcessInfoDlg.validateFields);
});
