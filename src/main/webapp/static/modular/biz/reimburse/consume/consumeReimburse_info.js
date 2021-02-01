/**
 * 初始化费用报销详情对话框
 */
var ConsumeReimburseInfoDlg = {
    consumeReimburseInfoData : {},
    deptZtree: null,
    subjectZtreeInstance: null,
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
        'subject0': {
            validators: {
                notEmpty: {
                    message: '科目不能为空'
                }
            }
        },
        'project0': {
            validators: {
                notEmpty: {
                    message: '项目名称不能为空'
                }
            }
        },
        // 'cashuse0': {
        //     validators: {
        //         notEmpty: {
        //             message: '资金用途不能为空'
        //         }
        //     }
        // },
        'cash0': {
            validators: {
                notEmpty: {
                    message: '报销金额不能为空'
                },
                regexp: {
                    regexp : /^\d+(\.\d{0,2})?$/i,
                    message: '只能为数字含两位小数'
                }
            }
        },
        'reqno0': {
            validators: {
                notEmpty: {
                    message: '关联申请单号不能为空'
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
ConsumeReimburseInfoDlg.clearData = function() {
    this.consumeReimburseInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
ConsumeReimburseInfoDlg.set = function(key, val) {
    this.consumeReimburseInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
ConsumeReimburseInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
ConsumeReimburseInfoDlg.close = function() {
    parent.layer.close(window.parent.ConsumeReimburse.layerIndex);
}

/**
 * 关闭此对话框
 */
ConsumeReimburseInfoDlg.goBack = function() {
    window.history.back();
}

/**
 * 收集数据
 */
ConsumeReimburseInfoDlg.collectData = function() {
    this
    .set('id')
    .set('reimburseno')
    .set('deptid')
    .set('totalfee')
    .set('state')
    .set('userid')
    .set('processId')
    .set('createtime');
}

// 获取详情数据
ConsumeReimburseInfoDlg.getConsumeReimburseDetail = function() {
    var consumeDetailDtos = new Array();
    var tableData = {};
    $(".table-bordered tr").each(function (trindex, tritem) {
        tableData = {};
        $(tritem).find("input").each(function (tdindex, tditem) {
            tableData[$(tditem).attr("id")] = $(tditem).val();
        });
        if (!$.isEmptyObject(tableData)) {
            consumeDetailDtos.push(tableData);
        }
    });
    var param = {"consumeDetailDtos" : consumeDetailDtos,
        "id": $("#id").val(),
        "deptid": $("#deptid").val(),
        "location": $("#location").val(),
        "applycash": $("#applycash").val(),
        "adjustcash": $("#adjustcash").val(),
        "totalfee": $("#totalfee").val(),
        "assignee": $("#assignee").val()};
    return param;
}

/**
 * 提交保存
 */
ConsumeReimburseInfoDlg.saveSubmit = function() {

    if (!this.validate()) {
        return;
    }

    var param = ConsumeReimburseInfoDlg.getConsumeReimburseDetail();
    var jsonTemp = JSON.stringify(param);

    //提交信息
    $.ajax({
        type: "post",
        url: Feng.ctxPath + "/consume/reimburse/save",
        data: jsonTemp,
        contentType:"application/json;charset=utf-8",
        dataType: "json",
        success: function (data) {
            if (data.code == 200) {
                Feng.success("保存成功!");
            } else {
                eng.error("保存失败!" + data.message + "!");
            }
            window.location.href = Feng.ctxPath + "/consume/reimburse";
        },
        error: function (data) {
            Feng.error("保存失败!" + data.message + "!");
        }
    });
}

/**
 * 提交流程
 */
ConsumeReimburseInfoDlg.addSubmit = function() {

    if (!this.validate()) {
        return;
    }

    var param = ConsumeReimburseInfoDlg.getConsumeReimburseDetail();
    var jsonTemp = JSON.stringify(param);

    //提交信息
    $.ajax({
        type: "post",
        url: Feng.ctxPath + "/consume/reimburse/add",
        data: jsonTemp,
        contentType:"application/json;charset=utf-8",
        dataType: "json",
        success: function (data) {
            if (data.code == 200) {
                Feng.success("提交流程成功!");
            } else {
                eng.error("提交流程失败!" + data.message + "!");
            }
            window.location.href = Feng.ctxPath + "/consume/reimburse";
        },
        error: function (data) {
            Feng.error("保存失败!" + data.message + "!");
        }
    });
}

/**
 * 提交修改
 */
ConsumeReimburseInfoDlg.editSubmit = function() {
    if (!this.validate()) {
        return;
    }
    var param = ConsumeReimburseInfoDlg.getConsumeReimburseDetail();
    var jsonTemp = JSON.stringify(param);
    //提交信息
    $.ajax({
        type: "post",
        url: Feng.ctxPath + "/consume/reimburse/update",
        data: jsonTemp,
        contentType:"application/json;charset=utf-8",
        dataType: "json",
        success: function (data) {
            if (data.code == 200) {
                Feng.success("修改成功!");
            } else {
                eng.error("修改失败!" + data.message + "!");
            }
            window.location.href = Feng.ctxPath + "/consume/reimburse";
        },
        error: function (data) {
            Feng.error("修改失败!" + data.message + "!");
        }
    });
}

/**
 * 添加行
 */
ConsumeReimburseInfoDlg.addRow = function() {
    var index = $("#para_table").find("tr").length;
    var table = $("#para_table");
    var tr= $('<tr class="consumeContent" id="' + (index-1) + '"><td class="col-md-6"><div class="form-group"><div class="col-sm-12">' +
        '<input class="form-control" id="subject" name="subject' + (index-1) + '" type="text" readonly="readonly" onclick="ConsumeReimburseInfoDlg.showSubjectSelectTree(this); return false;" style="background-color: #ffffff !important;">' +
        '<input class="form-control" type="hidden" id="subjectid" name="subjectid' + (index-1) + '"></div></div></td>' +
        '<td class="col-md-6"><div class="form-group"><div class="col-sm-12">' +
        '<select class="form-control" id="project" name="project' + (index-1) + '"></select>' +
        '<input class="form-control" type="hidden" id="projectid" name="projectid' + (index-1) + '"/></div></div></td>' +
        '<td class="col-md-6"><div class="form-group"><div class="col-sm-12">' +
        '<input class="form-control" type="text" id="code" name="code' + (index-1) + '" readonly="readonly"/></div></div></td>' +
        '<td class="col-md-6"><div class="form-group"><div class="col-sm-12">' +
        '<input class="form-control" type="text" id="cashuse" name="cashuse' + (index-1) + '"/></div></div></td>' +
        '<td class="col-md-6"><div class="form-group"><div class="col-sm-12">' +
        '<input class="form-control" type="text" id="cash" name="cash' + (index-1) + '" onchange="ConsumeReimburseInfoDlg.countTotal(this);return false;"/></div></div></td>' +
        '<td class="col-md-6"><div class="form-group"><div class="col-sm-12">' +
        '<input class="form-control" type="text" id="reqno" name="reqno' + (index-1) + '"/></div></div></td>' +
        '<td class="col-md-6"><button type="button" class="btn btn-primary " onclick="ConsumeReimburseInfoDlg.deleteRow(this, \'add\', 0)" id=""><i class="fa fa-minus"></i>&nbsp;删除</button></td></tr>');
    table.append(tr);
    initProjectOptionData(index-1);
    sortTrNumber();
    $('#consumeReimburseForm').bootstrapValidator('addField', 'subject' + (index-1), {
        validators: {
            notEmpty: {
                message: '科目不能为空'
            }
        }
    });
    $('#consumeReimburseForm').bootstrapValidator('addField', 'project' + (index-1), {
        validators: {
            notEmpty: {
                message: '项目名称不能为空'
            }
        }
    });
    $('#consumeReimburseForm').bootstrapValidator('addField', 'cash' + (index-1), {
        validators: {
            notEmpty: {
                message: '报销金额不能为空'
            },
            regexp: {
                regexp : /^\d+(\.\d{0,2})?$/i,
                message: '只能为数字含两位小数'
            }
        }
    });
    $('#consumeReimburseForm').bootstrapValidator('addField', 'reqno' + (index-1), {
        validators: {
            notEmpty: {
                message: '关联申请单号不能为空不能为空'
            }
        }
    });
}

/**
 * 删除行
 */
ConsumeReimburseInfoDlg.deleteRow = function(tdobject, type, id) {
    var td = $(tdobject);
    if (type == 'add') {
        td.parents("tr").remove();
    } else {
        var ajax = new $ax(Feng.ctxPath + "/consume/reimburse/deletePathDetailById", function (data) {
            // Feng.success("开始流程成功!");
            td.parents("tr").remove();
        }, function (data) {
            Feng.error("删除信息失败!" + data.message + "!");
        });
        ajax.set("id", id);
        ajax.start();
    }
    sortTrNumber();
    countTotalFee();
}

/**
 * 增加删除行之后对tr的id的序号重新排序
 */
function sortTrNumber() {
    $("#para_table").find(".consumeContent").each(function (index, obj) {
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


function countTotalFee() {
    var countFee = 0;
    $("#para_table").find(".consumeContent").each(function (index, obj) {
        var cash = $("input[name='cash" + index + "']").val();
        countFee = Math.floor(parseFloat(countFee * 100 + cash * 100)) / 100;
    });
    $("#totalfee").val(countFee);
    ConsumeReimburseInfoDlg.countAdjustCash();
}


/**
 * 小计金额的计算
 */
ConsumeReimburseInfoDlg.countTotal = function(obj) {
    // 报销金额
    countTotalFee();
}

/**
 * 退补金额的计算
 */
ConsumeReimburseInfoDlg.countAdjustCash = function() {
    // 预借现金
    var applycash = $("#applycash").val();
    if(applycash == '' || applycash == undefined){
        applycash = 0;
    }
    var totalfee = $("#totalfee").val();
    if(totalfee == '' || totalfee == undefined){
        totalfee = 0;
    }
    var adjustcash = Math.floor(parseFloat(totalfee * 100 - applycash * 100))/100;
    $("#adjustcash").val(adjustcash);
}

/**
 * 显示部门选择的树
 *
 * @returns
 */
ConsumeReimburseInfoDlg.showDeptSelectTree = function () {
    Feng.showInputTree("departs", "menuContent");
};

var index = 0;
ConsumeReimburseInfoDlg.showSubjectSelectTree = function (obj) {
    // 先初始化
    index = 0;
    // 再赋值
    index = $(obj).parent().parent().parent().parent("tr").attr("id");
    var subject = $(obj);
    var subjectOffset = subject.offset();
    $("#subjectContent").css({
        left: subjectOffset.left + "px",
        top: subjectOffset.top + subject.outerHeight() + "px"
    }).slideDown("fast");

    $("body").bind("mousedown", onBodyDown);
};

function onBodyDown(event) {
    if (!(event.target.id == "menuBtn" || event.target.id == "subjectContent" || $(
            event.target).parents("#subjectContent").length > 0)) {
        ConsumeReimburseInfoDlg.hideSubjectSelectTree();
    }
}

/**
 * 隐藏科目选择的树
 */
ConsumeReimburseInfoDlg.hideSubjectSelectTree = function() {
    $("#subjectContent").fadeOut("fast");
    $("body").unbind("mousedown", onBodyDown);// mousedown当鼠标按下就可以触发，不用弹起
}

/**
 * 点击部门input框时
 *
 * @param e
 * @param treeId
 * @param treeNode
 * @returns
 */
ConsumeReimburseInfoDlg.onClickSubject = function (e, treeId, treeNode) {
    var selVal = ConsumeReimburseInfoDlg.subjectZtreeInstance.getSelectedVal();
    $("input[name='subject" + index +"']").attr("value", selVal);
    $("input[name='subjectid" + index +"']").attr("value", treeNode.id);
};

/**
 * 点击部门input框时
 *
 * @param e
 * @param treeId
 * @param treeNode
 * @returns
 */
ConsumeReimburseInfoDlg.onClickDept = function (e, treeId, treeNode) {
    var selVal = ConsumeReimburseInfoDlg.deptZtree.getSelectedVal();
    $("#departs").attr("value", selVal);
    $("#deptid").attr("value", treeNode.id);
};

/**
 * 验证数据
 */
ConsumeReimburseInfoDlg.validate = function () {
    $('#consumeReimburseForm').data("bootstrapValidator").resetForm();
    $('#consumeReimburseForm').bootstrapValidator('validate');
    return $("#consumeReimburseForm").data('bootstrapValidator').isValid();
};

// 项目下拉框选择监听
function projectChangeListener() {
    $("select[name^='project']").change(function () {
        // 获取集合中下拉框的index
        var pattern = $(this).attr('id');
        var nameObj = $(this).attr('name');
        var index = nameObj.substr(pattern.length, nameObj.length);
        // 获取下拉框选中的值
        var idAndCode = $(this).val();
        // 分解字符串获取项目编号
        var id = idAndCode.split(":")[0];
        var code = idAndCode.split(":")[1];
        // 填充值
        $("input[name='projectid" + index + "']").val(id);
        $("input[name='code" + index + "']").val(code);
    });
}

/**
 * 初始化项目下拉的选项数据
 */
function initProjectOptionData(index) {
    var ajax = new $ax(Feng.ctxPath + "/project/manager/project/info", function(data) {
        var select = $("select[name='project" + index +"']");
        var html = '';
        html += '<option value="">请选择</option>';
        Object.keys(data).forEach(function(index){
            html += '<option value="' + data[index].id + ':' + data[index].code + '">' + data[index].name+ '</option>';
        });
        select.html(html);
    }, function(data){
        Feng.error("查询项目信息出错!");
    });
    ajax.start();
    // 项目下拉框选择监听
    projectChangeListener();
}

$(function() {
    Feng.initValidator("consumeReimburseForm", ConsumeReimburseInfoDlg.validateFields);

    var ztree = new $ZTree("treeDemo", "/dept/tree");
    ztree.bindOnClick(ConsumeReimburseInfoDlg.onClickDept);
    ztree.init();
    ConsumeReimburseInfoDlg.deptZtree = ztree;

    var subjectree = new $ZTree("treeSubjectDemo", "/subject/tree");
    subjectree.bindOnClick(ConsumeReimburseInfoDlg.onClickSubject);
    subjectree.init();
    ConsumeReimburseInfoDlg.subjectZtreeInstance = subjectree;

    // 初始化下拉框
    $("#location").val($("#locationValue").val());

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
        Feng.error("查询项目信息出错!");
    });
    ajax.set("processId", "reimburseProcess");
    ajax.start();

    // 初始化的时候对validater动态添加
    var index = $("#para_table").find("tr").length;
    for (var i=0; i<index-1; i++) {
        // 初始化项目的下拉框选项
        initProjectOptionData(i);
        // 拼接value值
        var id = $("input[name='projectid" + i + "']").val();
        var code = $("input[name='code" + i + "']").val();
        // 初始化下拉框
        var value = id + ':' + code;
        $("select[name='project" + i + "']").val(value);

        $('#consumeReimburseForm').bootstrapValidator('addField', 'subject' + (i+1), {
            validators: {
                notEmpty: {
                    message: '科目不能为空'
                }
            }
        });
        $('#consumeReimburseForm').bootstrapValidator('addField', 'project' + (i+1), {
            validators: {
                notEmpty: {
                    message: '项目名称不能为空'
                }
            }
        });
        $('#consumeReimburseForm').bootstrapValidator('addField', 'cash' + (i+1), {
            validators: {
                notEmpty: {
                    message: '报销金额不能为空'
                },
                regexp: {
                    regexp : /^\d+(\.\d{0,2})?$/i,
                    message: '只能为数字含两位小数'
                }
            }
        });
        $('#consumeReimburseForm').bootstrapValidator('addField', 'reqno' + (i+1), {
            validators: {
                notEmpty: {
                    message: '关联申请单号不能为空不能为空'
                }
            }
        });
    }
});
