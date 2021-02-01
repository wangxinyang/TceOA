/**
 * 报销管理管理初始化
 */
var FundAmountInfoDlg = {
    fundProcessInfoData : {},
    validateFields: {
        'actualfee0': {
            validators: {
                regexp: {
                    regexp : /^\d+(\.\d{0,2})?$/i,
                    message: '只能为数字含两位小数'
                }
            }
        }
    }
};

FundAmountInfoDlg.approveByAjax = function() {
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
    //             "fundApplySalesDtos": data,
    //             "id": $("#aid").val(),
    //             "type": $("#type").val()
    //         };
    //         break;
    //     case "2":
    //         param = {
    //             "fundApplyTravelDtos": data,
    //             "id": $("#aid").val(),
    //             "type": $("#type").val()
    //         };
    //         break;
    //     case "3":
    //         param = {
    //             "fundApplyPurchaseDtos": data,
    //             "id": $("#aid").val(),
    //             "type": $("#type").val()
    //         };
    //         break;
    //     case "4":
    //         param = {
    //             "fundApplyOtherDtos": data,
    //             "id": $("#aid").val(),
    //             "type": $("#type").val()
    //         };
    //         break;
    // }
    param = {
        "fundApplyDetailDtos": data,
        "id": $("#aid").val(),
        "type": $("#type").val()
    };
    var jsonTemp = JSON.stringify(param);
    //提交信息
    $.ajax({
        type: "post",
        url: Feng.ctxPath + "/fund/amount/update",
        data: jsonTemp,
        contentType: "application/json;charset=utf-8",
        dataType: "json",
        success: function (data) {
            if (data.code == 200) {
                Feng.success("修改通过!");
            } else {
                Feng.error("修改失败!" + data.message + "!");
            }
            window.location.href = Feng.ctxPath + "/fund/amount";
        },
        error: function (data) {
            Feng.error("修改失败!" + data.message + "!");
        }
    });
}

/**
 * 通过审核
 */
FundAmountInfoDlg.update = function () {
    this.approveByAjax();
};


/**
 * 关闭此对话框
 */
FundAmountInfoDlg.goBack = function() {
    window.history.back();
}

/**
 * 执行率的计算
 */
FundAmountInfoDlg.countRatio = function(obj) {
    var td = $(obj);
    var index = td.parents("tr").attr("id");
    var totalfee = $("input[name='totalfee" + index +"']").val();
    var actualfee = $("input[name='actualfee" + index +"']").val();
    if(actualfee == '' || actualfee == undefined){
        actualfee = 0;
    }
    var ratio = Math.round(parseFloat(actualfee / totalfee) * 100) / 100;
    $("input[name='ratio" + index +"']").val(ratio);
}

/**
 * 验证数据
 */
FundAmountInfoDlg.validate = function () {
    $('#amountInfoForm').data("bootstrapValidator").resetForm();
    $('#amountInfoForm').bootstrapValidator('validate');
    return $("#amountInfoForm").data('bootstrapValidator').isValid();
};


$(function () {
    Feng.initValidator("amountInfoForm", FundAmountInfoDlg.validateFields);
    var index = $("#para_table").find("tr").length;
    for (var i=0; i<index-1; i++) {
        $('#amountInfoForm').bootstrapValidator('addField', 'actualfee' + (i+1), {
            validators: {
                regexp: {
                    regexp : /^\d+(\.\d{0,2})?$/i,
                    message: '只能为数字含两位小数'
                }
            }
        });
    }
});
