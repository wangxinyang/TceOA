/**
 * 报销管理管理初始化
 */
var FundProcessInfoDlg = {
    fundProcessInfoData : {},
    validateFields: {
        'assistantnote0': {
            validators: {
                notEmpty: {
                    message: '审批信息不能为空'
                }
            }
        },
        'subapprovefee0': {
            validators: {
                notEmpty: {
                    message: '审批金额不能为空'
                },
                regexp: {
                    regexp : /^\d+(\.\d{0,2})?$/i,
                    message: '只能为数字含两位小数'
                }
            }
        },
        'approvefee0': {
            validators: {
                notEmpty: {
                    message: '审批金额不能为空'
                },
                regexp: {
                    regexp : /^\d+(\.\d{0,2})?$/i,
                    message: '只能为数字含两位小数'
                }
            }
        }
    }
};

FundProcessInfoDlg.approveByAjax = function(type) {
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
    var detailType = $("#type").val();
    var param = {};
    // switch (detailType) {
    //     case "1":
    //         param = {
    //             "fundApplyDetailDtos": data,
    //             "taskId": $("#taskId").val(),
    //             "location": $("#location").val(),
    //             "id": $("#fundApplyId").val(),
    //             "type": $("#type").val()
    //         };
    //         break;
    //     case "2":
    //         param = {
    //             "fundApplyTravelDtos": data,
    //             "taskId": $("#taskId").val(),
    //             "location": $("#location").val(),
    //             "id": $("#fundApplyId").val(),
    //             "type": $("#type").val()
    //         };
    //         break;
    //     case "3":
    //         param = {
    //             "fundApplyPurchaseDtos": data,
    //             "taskId": $("#taskId").val(),
    //             "location": $("#location").val(),
    //             "id": $("#fundApplyId").val(),
    //             "type": $("#type").val()
    //         };
    //         break;
    //     case "4":
    //         param = {
    //             "fundApplyOtherDtos": data,
    //             "taskId": $("#taskId").val(),
    //             "location": $("#location").val(),
    //             "id": $("#fundApplyId").val(),
    //             "type": $("#type").val()
    //         };
    //         break;
    // }
    param = {
        "fundApplyDetailDtos": data,
        "taskId": $("#taskId").val(),
        "location": $("#location").val(),
        "id": $("#fundApplyId").val(),
        "taskDefinitionKey": $("#taskDefinitionKey").val(),
        "type": $("#type").val()
    };
    var jsonTemp = JSON.stringify(param);
    var url = '';
    var keyWord = '';
    if(type == 'pass') {
        url = Feng.ctxPath + "/fund/budget/process/pass"
        keyWord = "审核";
    } else {
        url = Feng.ctxPath + "/fund/budget/process/unPass"
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
                eng.error(keyWord + "失败!" + data.message + "!");
            }
            window.location.href = Feng.ctxPath + "/fund/budget/process";
        },
        error: function (data) {
            Feng.error(keyWord + "失败!" + data.message + "!");
        }
    });
}

/**
 * 通过审核
 */
FundProcessInfoDlg.pass = function () {
    if (!this.validate()) {
        return;
    }
    this.approveByAjax("pass");
};

/**
 * 未通过审核
 */
FundProcessInfoDlg.unPass = function () {
    this.approveByAjax("unPass");
};

/**
 * 关闭此对话框
 */
FundProcessInfoDlg.goBack = function() {
    window.history.back();
}

/**
 * 验证数据
 */
FundProcessInfoDlg.validate = function () {
    $('#processInfoForm').data("bootstrapValidator").resetForm();
    $('#processInfoForm').bootstrapValidator('validate');
    return $("#processInfoForm").data('bootstrapValidator').isValid();
};


$(function () {
    Feng.initValidator("processInfoForm", FundProcessInfoDlg.validateFields);
    var index = $("#para_table").find("tr").length;
    for (var i=0; i<index-1; i++) {
        $('#processInfoForm').bootstrapValidator('addField', 'assistantnote' + (i+1), {
            validators: {
                notEmpty: {
                    message: '审批信息不能为空'
                }
            }
        });
        $('#processInfoForm').bootstrapValidator('addField', 'subapprovefee' + (i+1), {
            validators: {
                notEmpty: {
                    message: '审批金额不能为空'
                },
                regexp: {
                    regexp : /^\d+(\.\d{0,2})?$/i,
                    message: '只能为数字含两位小数'
                }
            }
        });
        $('#processInfoForm').bootstrapValidator('addField', 'approvefee' + (i+1), {
            validators: {
                notEmpty: {
                    message: '审批金额不能为空'
                },
                regexp: {
                    regexp : /^\d+(\.\d{0,2})?$/i,
                    message: '只能为数字含两位小数'
                }
            }
        });
    }
});
