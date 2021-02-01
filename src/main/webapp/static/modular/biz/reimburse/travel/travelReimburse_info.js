/**
 * 初始化差旅费报销详情对话框
 */
var TravelReimburseInfoDlg = {
    travelReimburseInfoData : {},
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
        'travelreason': {
            validators: {
                notEmpty: {
                    message: '出差事由不能为空'
                }
            }
        },
        'applytime': {
            validators: {
                notEmpty: {
                    message: '申请时间不能为空'
                }
            }
        },
        'accommodationfee': {
            validators: {
                notEmpty: {
                    message: '住宿不能为空'
                },
                regexp: {
                    regexp : /^\d+(\.\d{0,2})?$/i,
                    message: '只能为数字含两位小数'
                }
            }
        },
        'trafficfee': {
            validators: {
                notEmpty: {
                    message: '市内交通不能为空'
                },
                regexp: {
                    regexp : /^\d+(\.\d{0,2})?$/i,
                    message: '只能为数字含两位小数'
                }
            }
        },
        'traveldays': {
            validators: {
                notEmpty: {
                    message: '天数不能为空'
                },
                digits: {
                    message: '只能为数字'
                }
            }
        },
        'travelmethod': {
            validators: {
                notEmpty: {
                    message: '交通方式不能为空'
                }
            }
        },
        'travelfee': {
            validators: {
                notEmpty: {
                    message: '交通费不能为空'
                },
                regexp: {
                    regexp : /^\d+(\.\d{0,2})?$/i,
                    message: '只能为数字含两位小数'
                }
            }
        },
        'othercash': {
            validators: {
                regexp: {
                    regexp : /^\d+(\.\d{0,2})?$/i,
                    message: '只能为数字含两位小数'
                }
            }
        },
        'ticketnums': {
            validators: {
                notEmpty: {
                    message: '单据张数不能为空'
                }
            }
        },
        'upperfee': {
            validators: {
                notEmpty: {
                    message: '金额大写不能为空'
                }
            }
        },
        'applycash': {
            validators: {
                regexp: {
                    regexp : /^\d+(\.\d{0,2})?$/i,
                    message: '只能为数字含两位小数'
                }
            }
        },
        'fromtime0': {
            validators: {
                notEmpty: {
                    message: '行程起时间不能为空'
                }
            }
        },
        'fromlocation0': {
            validators: {
                notEmpty: {
                    message: '行程起地点不能为空'
                }
            }
        },
        'totime0': {
            validators: {
                notEmpty: {
                    message: '行程止时间不能为空'
                }
            }
        },
        'tolocation0': {
            validators: {
                notEmpty: {
                    message: '行程止地点不能为空'
                }
            }
        },
        'target0': {
            validators: {
                notEmpty: {
                    message: '主要目的及具体工作安排不能为空'
                }
            }
        } ,
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
TravelReimburseInfoDlg.clearData = function() {
    this.travelReimburseInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
TravelReimburseInfoDlg.set = function(key, val) {
    this.travelReimburseInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
TravelReimburseInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
TravelReimburseInfoDlg.close = function() {
    parent.layer.close(window.parent.TravelReimburse.layerIndex);
}

/**
 * 关闭此对话框
 */
TravelReimburseInfoDlg.goBack = function() {
    window.history.back();
}

/**
 * 收集数据
 */
TravelReimburseInfoDlg.collectData = function() {
    this
    .set('id')
    .set('reimburseno')
    .set('deptid')
    .set('position')
    .set('applytime')
    .set('projetid')
    .set('travelreason')
    .set('accommodationfee')
    .set('trafficfee')
    .set('traveldays')
    .set('subsidy')
    .set('travelmethod')
    .set('travelfee')
    .set('description')
    .set('totalfee')
    .set('ticketnums')
    .set('upperfee')
    .set('applycash')
    .set('backcash')
    .set('addcash')
    .set('usernote')
    .set('leadernote')
    .set('leadertime')
    .set('cashernote')
    .set('cashertime')
    .set('deputynote')
    .set('deputytime')
    .set('managernote')
    .set('managertime')
    .set('state')
    .set('userid')
    .set('processId')
    .set('createtime');
}

// 获取详情数据
TravelReimburseInfoDlg.getTravelReimburseDetail = function() {
    var travelPathDtos = new Array();
    var tableData = {};
    $(".table-bordered tr").each(function (trindex, tritem) {
        tableData = {};
        $(tritem).find("input").each(function (tdindex, tditem) {
            tableData[$(tditem).attr("id")] = $(tditem).val();
        });
        if (!$.isEmptyObject(tableData)) {
            travelPathDtos.push(tableData);
        }
    });
    var param = {"travelPathDtos" : travelPathDtos,
                "id": $("#id").val(),
                "deptid": $("#deptid").val(),
                "location": $("#location").val(),
                "applytime": $("#applytime").val(),
                "projectid": $("#projectid").val().split(":")[0],
                "travelreason": $("#travelreason").val(),
                "accommodationfee": $("#accommodationfee").val(),
                "trafficfee": $("#trafficfee").val(),
                "traveldays": $("#traveldays").val(),
                "travelmethod": $("#travelmethod").val(),
                "subsidy": $("#subsidy").val(),
                "travelfee": $("#travelfee").val(),
                "othercash": $("#othercash").val(),
                "totalfee": $("#totalfee").val(),
                "upperfee": $("#upperfee").val(),
                "ticketnums": $("#ticketnums").val(),
                "applycash": $("#applycash").val(),
                "adjustcash": $("#adjustcash").val(),
                "assignee": $("#assignee").val()};
    return param;
}

/**
 * 保存操作
 */
TravelReimburseInfoDlg.saveSubmit = function() {
    if (!this.validate()) {
        return;
    }
    var param = TravelReimburseInfoDlg.getTravelReimburseDetail();
    var jsonTemp = JSON.stringify(param);
    //提交信息
    $.ajax({
        type: "post",
        url: Feng.ctxPath + "/travel/reimburse/save",
        data: jsonTemp,
        contentType:"application/json;charset=utf-8",
        dataType: "json",
        success: function (data) {
            if (data.code == 200) {
                Feng.success("保存成功!");
            } else {
                eng.error("保存失败!" + data.message + "!");
            }
            window.location.href = Feng.ctxPath + "/travel/reimburse";
        },
        error: function (data) {
            Feng.error("保存失败!" + data.message + "!");
        }
    });
}

/**
 * 提交添加
 */
TravelReimburseInfoDlg.addSubmit = function() {
    if (!this.validate()) {
        return;
    }
    var param = TravelReimburseInfoDlg.getTravelReimburseDetail();
    var jsonTemp = JSON.stringify(param);
    //提交信息
    $.ajax({
        type: "post",
        url: Feng.ctxPath + "/travel/reimburse/add",
        data: jsonTemp,
        contentType:"application/json;charset=utf-8",
        dataType: "json",
        success: function (data) {
            if (data.code == 200) {
                Feng.success("提交流程成功!");
            } else {
                eng.error("提交流程失败!" + data.message + "!");
            }
            window.location.href = Feng.ctxPath + "/travel/reimburse";
        },
        error: function (data) {
            Feng.error("提交流程失败!" + data.message + "!");
        }
    });
}

/**
 * 提交修改
 */
TravelReimburseInfoDlg.editSubmit = function() {

    if (!this.validate()) {
        return;
    }
    var param = TravelReimburseInfoDlg.getTravelReimburseDetail();
    var jsonTemp = JSON.stringify(param);
    //提交信息
    $.ajax({
        type: "post",
        url: Feng.ctxPath + "/travel/reimburse/update",
        data: jsonTemp,
        contentType:"application/json;charset=utf-8",
        dataType: "json",
        success: function (data) {
            if (data.code == 200) {
                Feng.success("修改成功!");
            } else {
                eng.error("修改失败!" + data.message + "!");
            }
            window.location.href = Feng.ctxPath + "/travel/reimburse";
        },
        error: function (data) {
            Feng.error("修改失败!" + data.message + "!");
        }
    });
}

/**
 * 添加行
 */
TravelReimburseInfoDlg.addRow = function() {
    var index = $("#para_table").find("tr").length;
    var table = $("#para_table");
    var tr= $('<tr class="travelContent" id="' + (index-2) + '"><td class="col-md-6"><div class="form-group"><div class="col-sm-12">' +
        '<input class="form-control" id="fromtime" name="fromtime' + (index-2) + '" type="text"></div></div></td>' +
        '<td class="col-md-6"><div class="form-group"><div class="col-sm-12">' +
        '<input class="form-control" id="fromlocation" name="fromlocation' + (index-2) + '" type="text"></div></div></td>' +
        '<td class="col-md-6"><div class="form-group"><div class="col-sm-12">' +
        '<input class="form-control" id="totime" name="totime' + (index-2) + '" type="text"></div></div></td>' +
        '<td class="col-md-6"><div class="form-group"><div class="col-sm-12">' +
        '<input class="form-control" id="tolocation" name="tolocation' + (index-2) + '" type="text"></div></div></td>' +
        '<td class="col-md-6"><div class="form-group"><div class="col-sm-12">' +
        '<input class="form-control" id="target" name="target' + (index-2) + '" type="text"></div></div></td>' +
        '<td class="col-md-6"><button type="button" class="btn btn-primary " onclick="TravelReimburseInfoDlg.deleteRow(this, \'add\', 0)" id=""><i class="fa fa-minus"></i>&nbsp;删除</button></td></tr>');
    table.append(tr);
    sortTrNumber();
    $('#travelReimburseForm').bootstrapValidator('addField', 'fromtime' + (index-2), {
        validators: {
            notEmpty: {
                message: '行程起时间不能为空'
            }
        }
    });
    $('#travelReimburseForm').bootstrapValidator('addField', 'fromlocation' + (index-2), {
        validators: {
            notEmpty: {
                message: '行程起地点不能为空'
            }
        }
    });
    $('#travelReimburseForm').bootstrapValidator('addField', 'totime' + (index-2), {
        validators: {
            notEmpty: {
                message: '行程止时间不能为空'
            }
        }
    });
    $('#travelReimburseForm').bootstrapValidator('addField', 'tolocation' + (index-2), {
        validators: {
            notEmpty: {
                message: '行程止地点不能为空'
            }
        }
    });
    $('#travelReimburseForm').bootstrapValidator('addField', 'target' + (index-2), {
        validators: {
            notEmpty: {
                message: '主要目的及具体工作安排不能为空'
            }
        }
    });
    laydate.render({
        elem: 'input[name="fromtime' + (index-2) + '"]'
    });
    laydate.render({
        elem: 'input[name="totime' + (index-2) + '"]'
    });
}

/**
 * 删除行
 */
TravelReimburseInfoDlg.deleteRow = function(tdobject, type, id) {
    var td = $(tdobject);
    if (type == 'add') {
        td.parents("tr").remove();
    } else {
        var ajax = new $ax(Feng.ctxPath + "/travel/reimburse/deletePathDetailById", function (data) {
            // Feng.success("开始流程成功!");
            td.parents("tr").remove();
        }, function (data) {
            Feng.error("删除信息失败!" + data.message + "!");
        });
        ajax.set("id", id);
        ajax.start();
    }
    sortTrNumber();
}

/**
 * 增加删除行之后对tr的id的序号重新排序
 */
function sortTrNumber() {
    $("#para_table").find(".travelContent").each(function (index, obj) {
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
TravelReimburseInfoDlg.showDeptSelectTree = function () {
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
TravelReimburseInfoDlg.onClickDept = function (e, treeId, treeNode) {
    var selVal = TravelReimburseInfoDlg.deptZtree.getSelectedVal();
    $("#departs").attr("value", selVal);
    $("#deptid").attr("value", treeNode.id);
};

/**
 * 根据天数计算补贴
 */
TravelReimburseInfoDlg.countSubsidy = function () {
    var days = $("#traveldays").val();
    if(days == '' || days == undefined){
        days = 0;
    }
    var subsidy = Math.floor(parseFloat(days * 100 * 85))/100;
    $("#subsidy").val(subsidy);
    TravelReimburseInfoDlg.countTotal();
}

/**
 * 小计金额的计算
 */
TravelReimburseInfoDlg.countTotal = function() {
    // 住宿
    var accommodationfee = $("#accommodationfee").val();
    if(accommodationfee == '' || accommodationfee == undefined){
        accommodationfee = 0;
    }
    // 市内交通
    var trafficfee = $("#trafficfee").val();
    if(trafficfee == '' || trafficfee == undefined){
        trafficfee = 0;
    }
    // 补贴
    var subsidy = $("#subsidy").val();
    if(subsidy == '' || subsidy == undefined){
        subsidy = 0;
    }
    // 交通费
    var travelfee = $("#travelfee").val();
    if(travelfee == '' || travelfee == undefined){
        travelfee = 0;
    }
    // 其他费用
    var othercash = $("#othercash").val();
    if(othercash == '' || othercash == undefined){
        othercash = 0;
    }

    var totalfee = Math.floor(parseFloat(accommodationfee * 100 + trafficfee * 100 + subsidy * 100 + travelfee * 100 + othercash * 100))/100;
    $("#totalfee").val(totalfee);
    TravelReimburseInfoDlg.countAdjustCash();
}

/**
 * 退补金额的计算
 */
TravelReimburseInfoDlg.countAdjustCash = function() {
    // 预借现金
    var applycash = $("#applycash").val();
    if(applycash == '' || applycash == 'undefined'){
        applycash = 0;
    }
    var totalfee = $("#totalfee").val();
    var adjustcash = Math.floor(parseFloat(totalfee * 100 - applycash * 100))/100;
    $("#adjustcash").val(adjustcash);
}


/**
 * 验证数据
 */
TravelReimburseInfoDlg.validate = function () {
    $('#travelReimburseForm').data("bootstrapValidator").resetForm();
    $('#travelReimburseForm').bootstrapValidator('validate');
    return $("#travelReimburseForm").data('bootstrapValidator').isValid();
};

$(function() {
    Feng.initValidator("travelReimburseForm", TravelReimburseInfoDlg.validateFields);

    var ztree = new $ZTree("treeDemo", "/dept/tree");
    ztree.bindOnClick(TravelReimburseInfoDlg.onClickDept);
    ztree.init();
    TravelReimburseInfoDlg.deptZtree = ztree;


    // 初始化的时候对validater动态添加
    var index = $("#para_table").find("tr").length;
    for (var i=0; i<index-2; i++) {
        $('#travelReimburseForm').bootstrapValidator('addField', 'fromtime' + (i+1), {
            validators: {
                notEmpty: {
                    message: '行程起时间不能为空'
                }
            }
        });
        $('#travelReimburseForm').bootstrapValidator('addField', 'fromlocation' + (i+1), {
            validators: {
                notEmpty: {
                    message: '行程起地点不能为空'
                }
            }
        });
        $('#travelReimburseForm').bootstrapValidator('addField', 'totime' + (i+1), {
            validators: {
                notEmpty: {
                    message: '行程止时间不能为空'
                }
            }
        });
        $('#travelReimburseForm').bootstrapValidator('addField', 'tolocation' + (i+1), {
            validators: {
                notEmpty: {
                    message: '行程止地点不能为空'
                }
            }
        });
        $('#travelReimburseForm').bootstrapValidator('addField', 'target' + (i+1), {
            validators: {
                notEmpty: {
                    message: '主要目的及具体工作安排不能为空'
                }
            }
        });
        laydate.render({
            elem: 'input[name="fromtime' + (i+1) + '"]'
        });
        laydate.render({
            elem: 'input[name="totime' + (i+1) + '"]'
        });
    }

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
    ajax.set("processId", "reimburseProcess");
    ajax.start();

    // 初始化下拉框
    $("#location").val($("#locationValue").val());
    $("#travelmethod").val($("#travelmethodValue").val());

});
