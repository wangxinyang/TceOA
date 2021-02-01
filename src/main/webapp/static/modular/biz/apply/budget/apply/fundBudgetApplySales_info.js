/**
 * 初始化资金预算详情对话框
 */
var FundBudgetApplySalesInfoDlg = {
    fundApplySalesInfoData : {},
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
        'projectname0': {
            validators: {
                notEmpty: {
                    message: '项目名称不能为空'
                }
            }
        },
        'customername0': {
            validators: {
                notEmpty: {
                    message: '客户名称不能为空'
                }
            }
        },
        'totalfee0': {
            validators: {
                notEmpty: {
                    message: '预算金额不能为空'
                },
                regexp: {
                    regexp : /^\d+(\.\d{0,2})?$/i,
                    message: '只能为数字含两位小数'
                }
            }
        }
    }
};


/**
 * 清除数据
 */
FundBudgetApplySalesInfoDlg.clearData = function() {
    this.fundApplySalesInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
FundBudgetApplySalesInfoDlg.set = function(key, val) {
    this.fundApplySalesInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
FundBudgetApplySalesInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
FundBudgetApplySalesInfoDlg.goBack = function() {
    window.history.back();
}



// 获取详情数据
FundBudgetApplySalesInfoDlg.getFundApplyDetail = function() {
    var fundApplySalesDto = new Array();
    var tableData = {};
    $(".table-bordered tr").each(function (trindex, tritem) {
        tableData = {};
        $(tritem).find("input").each(function (tdindex, tditem) {
            tableData[$(tditem).attr("id")] = $(tditem).val();
        });
        if (!$.isEmptyObject(tableData)) {
            fundApplySalesDto.push(tableData);
        }
    });
    return fundApplySalesDto;
}

/**
 * 保存操作
 */
FundBudgetApplySalesInfoDlg.saveSubmit = function() {
    if (!this.validate()) {
        return;
    }
    var fundApplySalesDto = FundBudgetApplySalesInfoDlg.getFundApplyDetail();
    var param = {"fundApplyDetailDtos" : fundApplySalesDto,
                 "deptid": $("#deptid").val(),
                 "projectid": $("#projectid").val(),
                 "location": $("#location").val(),
                 "type": 1
                };
    var jsonTemp = JSON.stringify(param);
    //提交信息
    $.ajax({
         type: "post",
         url: Feng.ctxPath + "/fund/budget/apply/save",
         data: jsonTemp,
         contentType:"application/json;charset=utf-8",
         dataType: "json",
         success: function (data) {
             if (data.code == 200) {
                 Feng.success("保存成功!");
             } else {
                 eng.error("保存失败!" + data.message + "!");
             }
             window.location.href = Feng.ctxPath + "/fund/budget/apply";
         },
         error: function (data) {
             Feng.error("保存失败!" + data.message + "!");
         }
    });
}


/**
 * 提交修改
 */
FundBudgetApplySalesInfoDlg.editSubmit = function() {
    if (!this.validate()) {
        return;
    }

    var fundApplySalesDto = FundBudgetApplySalesInfoDlg.getFundApplyDetail();
    var param = {"fundApplyDetailDtos" : fundApplySalesDto,
        "id": $("#aid").val(),
        "type": $("#type").val()};
    var jsonTemp = JSON.stringify(param);
    console.log(jsonTemp);
    //提交信息
    $.ajax({
         type: "post",
         url: Feng.ctxPath + "/fund/budget/apply/update",
         data: jsonTemp,
         contentType:"application/json;charset=utf-8",
         dataType: "json",
         success: function (data) {
             if (data.code == 200) {
                 Feng.success("修改成功!");
             } else {
                 eng.error("修改失败!" + data.message + "!");
             }
             window.location.href = Feng.ctxPath + "/fund/budget/apply";
         },
         error: function (data) {
             Feng.error("修改失败!" + data.message + "!");
         }
    });
}

/**
 * 提交操作
 */
FundBudgetApplySalesInfoDlg.addSubmit = function() {
    if (!this.validate()) {
        return;
    }
    var fundApplySalesDto = FundBudgetApplySalesInfoDlg.getFundApplyDetail();
    var param = {"fundApplyDetailDtos" : fundApplySalesDto,
                 "deptid": $("#deptid").val(),
                 "location": $("#location").val(),
                 "id": $("#aid").val(),
                 "type": 1};
    var jsonTemp = JSON.stringify(param);
    //提交信息
    $.ajax({
        type: "post",
        url: Feng.ctxPath + "/fund/budget/apply/add",
        data: jsonTemp,
        contentType:"application/json;charset=utf-8",
        dataType: "json",
        success: function (data) {
            if (data.code == 200) {
                Feng.success("开始流程成功!");
            } else {
                Feng.error("开始流程失败!" + data.message + "!");
            }
            window.location.href = Feng.ctxPath + "/fund/budget/apply";
        },
        error: function (data) {
            Feng.error("开始流程失败!" + data.message + "!");
        }
    });
}

/**
 * 添加行
 */
FundBudgetApplySalesInfoDlg.addRow = function() {
    var index = $("#para_table").find("tr").length;
    var table = $("#para_table");
    var tr= $('<tr class="salesContent" id="' + (index-1) + '"><td class="col-md-6"><div class="form-group"><div class="col-sm-12">' +
        '<input class="form-control" id="subject" name="subject' + (index-1) + '" type="text" readonly="readonly" onclick="FundBudgetApplySalesInfoDlg.showSubjectSelectTree(this); return false;" style="background-color: #ffffff !important;">' +
        '<input class="form-control" type="hidden" id="subjectid" name="subjectid' + (index-1) + '"></div></div></td>' +
        '<td class="col-md-6"><div class="form-group"><div class="col-sm-12">' +
        '<select class="form-control" id="project" name="project' + (index-1) + '"></select><input class="form-control" type="hidden" id="projectid" name="projectid' + (index-1) + '"/></div></div></td>' +
        '<td class="col-md-6"><div class="form-group"><div class="col-sm-12">' +
        '<input class="form-control" id="customername" name="customername' + (index-1) + '" type="text"></div></div></td>' +
        '<td class="col-md-6"><div class="form-group"><div class="col-sm-12">' +
        '<input class="form-control" id="totalfee" name="totalfee' + (index-1) + '" type="text"></div></div></td>' +
        '<td class="col-md-6"><div class="form-group"><div class="col-sm-12">' +
        '<input class="form-control" id="description" name="description' + (index-1) + '" type="text"></div></div></td>' +
        '<td class="col-md-6"><button type="button" class="btn btn-primary " onclick="FundBudgetApplySalesInfoDlg.deleteRow(this, \'add\', 0)" id=""><i class="fa fa-minus"></i>&nbsp;删除</button></td></tr>');
    table.append(tr);
    initProjectOptionData(index-1);
    sortTrNumber();
    $('#salesInfoForm').bootstrapValidator('addField', 'subject' + (index-1), {
        validators: {
            notEmpty: {
                message: '科目不能为空'
            }
        }
    });
    $('#salesInfoForm').bootstrapValidator('addField', 'projectname' + (index-1), {
        validators: {
            notEmpty: {
                message: '项目名称不能为空'
            }
        }
    });
    $('#salesInfoForm').bootstrapValidator('addField', 'customername' + (index-1), {
        validators: {
            notEmpty: {
                message: '客户名称不能为空'
            }
        }
    });
    $('#salesInfoForm').bootstrapValidator('addField', 'totalfee' + (index-1), {
        validators: {
            notEmpty: {
                message: '预算金额不能为空'
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
FundBudgetApplySalesInfoDlg.deleteRow = function(tdobject, type, id) {
    var td = $(tdobject);
    if (type == 'add') {
        td.parents("tr").remove();
    } else {
        var ajax = new $ax(Feng.ctxPath + "/fund/budget/apply/deleteDetailById", function (data) {
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
    $("#para_table").find(".salesContent").each(function (index, obj) {
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
FundBudgetApplySalesInfoDlg.showDeptSelectTree = function () {
    Feng.showInputTree("departs", "menuContent");
};

/**
 * 小计金额的计算
 */
FundBudgetApplySalesInfoDlg.countTotal = function(obj) {
    var td = $(obj);
    var index = td.parents("tr").attr("id");
    var hospitality = $("input[name='hospitality" + index +"']").val();
    if(hospitality == '' || hospitality == undefined){
        hospitality = 0;
    }
    var giftfee = $("input[name='giftfee" + index +"']").val();
    if(giftfee == '' || giftfee == undefined){
        giftfee = 0;
    }
    var nocontract = $("input[name='nocontract" + index +"']").val();
    if(nocontract == '' || nocontract == undefined){
        nocontract = 0;
    }
    var contract = $("input[name='contract" + index +"']").val();
    if(contract == '' || contract == undefined){
        contract = 0;
    }
    var totalfee = Math.floor(parseFloat(hospitality*100 + giftfee*100 + nocontract*100 + contract*100))/100;
    $("input[name='totalfee" + index +"']").val(totalfee);
}

var index = 0;
FundBudgetApplySalesInfoDlg.showSubjectSelectTree = function (obj) {
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
        FundBudgetApplySalesInfoDlg.hideSubjectSelectTree();
    }
}

/**
 * 隐藏科目选择的树
 */
FundBudgetApplySalesInfoDlg.hideSubjectSelectTree = function() {
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
FundBudgetApplySalesInfoDlg.onClickDept = function (e, treeId, treeNode) {
    var selVal = FundBudgetApplySalesInfoDlg.deptZtree.getSelectedVal();
    $("#departs").attr("value", selVal);
    $("#deptid").attr("value", treeNode.id);
};


/**
 * 点击科目input框时
 *
 * @param e
 * @param treeId
 * @param treeNode
 * @returns
 */
FundBudgetApplySalesInfoDlg.onClickSubject = function (e, treeId, treeNode) {
    var selVal = FundBudgetApplySalesInfoDlg.subjectZtreeInstance.getSelectedVal();
    $("input[name='subject" + index +"']").attr("value", selVal);
    $("input[name='subjectid" + index +"']").attr("value", treeNode.id);
};

/**
 * 验证数据
 */
FundBudgetApplySalesInfoDlg.validate = function () {
    $('#salesInfoForm').data("bootstrapValidator").resetForm();
    $('#salesInfoForm').bootstrapValidator('validate');
    return $("#salesInfoForm").data('bootstrapValidator').isValid();
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
        // var id = idAndCode.split(":")[0];
        // var code = idAndCode.split(":")[1];
        // 填充值
        $("input[name='projectid" + index + "']").val(idAndCode);
        // $("input[name='code" + index + "']").val(code);
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
    Feng.initValidator("salesInfoForm", FundBudgetApplySalesInfoDlg.validateFields);
    // 初始化的时候对validater动态添加
    var index = $("#para_table").find("tr").length;
    for (var i=0; i<index-1; i++) {
        // 初始化项目的下拉框选项
        initProjectOptionData(i);
        // 拼接value值
        var id = $("input[name='projectid" + i + "']").val();
        // 初始化下拉框
        $("select[name='project" + i + "']").val(id);
        $('#salesInfoForm').bootstrapValidator('addField', 'subject' + (i+1), {
            validators: {
                notEmpty: {
                    message: '科目不能为空'
                }
            }
        });
        $('#salesInfoForm').bootstrapValidator('addField', 'project' + (i+1), {
            validators: {
                notEmpty: {
                    message: '项目名称不能为空'
                }
            }
        });
        $('#salesInfoForm').bootstrapValidator('addField', 'customername' + (i+1), {
            validators: {
                notEmpty: {
                    message: '客户名称不能为空'
                }
            }
        });
        $('#salesInfoForm').bootstrapValidator('addField', 'totalfee' + (i+1), {
            validators: {
                notEmpty: {
                    message: '预算金额不能为空'
                },
                regexp: {
                    regexp : /^\d+(\.\d{0,2})?$/i,
                    message: '只能为数字含两位小数'
                }
            }
        });
    }

    var ztree = new $ZTree("treeDemo", "/dept/tree");
    ztree.bindOnClick(FundBudgetApplySalesInfoDlg.onClickDept);
    ztree.init();
    FundBudgetApplySalesInfoDlg.deptZtree = ztree;

    var subjectree = new $ZTree("treeSubjectDemo", "/subject/tree");
    subjectree.bindOnClick(FundBudgetApplySalesInfoDlg.onClickSubject);
    subjectree.init();
    FundBudgetApplySalesInfoDlg.subjectZtreeInstance = subjectree;
});
