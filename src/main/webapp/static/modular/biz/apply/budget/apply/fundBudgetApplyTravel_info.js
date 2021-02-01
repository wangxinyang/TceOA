/**
 * 初始化资金预算详情对话框
 */
var FundBudgetApplyTravelInfoDlg = {
    fundApplyTravelInfoData : {},
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
        'empname0': {
            validators: {
                notEmpty: {
                    message: '员工姓名不能为空'
                }
            }
        },
        'traveldest0': {
            validators: {
                notEmpty: {
                    message: '出差目的地不能为空'
                }
            }
        },
        'traveldays0': {
            validators: {
                notEmpty: {
                    message: '出差天数不能为空'
                },
                digits: {
                    message: '出差天数只能为数字'
                }
            }
        },
        'travelfee0': {
            validators: {
                notEmpty: {
                    message: '长途交通费不能为空'
                },
                regexp: {
                    regexp : /^\d+(\.\d{0,2})?$/i,
                    message: '只能为数字含两位小数'
                }
            }
        },
        'trafficfee0': {
            validators: {
                notEmpty: {
                    message: '往返交通费不能为空'
                },
                regexp: {
                    regexp : /^\d+(\.\d{0,2})?$/i,
                    message: '只能为数字含两位小数'
                }
            }
        },
        'accommodationfee0': {
            validators: {
                notEmpty: {
                    message: '住宿费不能为空'
                },
                regexp: {
                    regexp : /^\d+(\.\d{0,2})?$/i,
                    message: '只能为数字含两位小数'
                }
            }
        },
        'subsidy0': {
            validators: {
                notEmpty: {
                    message: '补贴支出不能为空'
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
FundBudgetApplyTravelInfoDlg.clearData = function() {
    this.fundApplyTravelInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
FundBudgetApplyTravelInfoDlg.set = function(key, val) {
    this.fundApplyTravelInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
FundBudgetApplyTravelInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
FundBudgetApplyTravelInfoDlg.goBack = function() {
    window.history.back();
}



// 获取详情数据
FundBudgetApplyTravelInfoDlg.getFundApplyDetail = function() {
    var fundApplyTravelDto = new Array();
    var tableData = {};
    $(".table-bordered tr").each(function (trindex, tritem) {
        tableData = {};
        $(tritem).find("input").each(function (tdindex, tditem) {
            tableData[$(tditem).attr("id")] = $(tditem).val();
        });
        if (!$.isEmptyObject(tableData)) {
            fundApplyTravelDto.push(tableData);
        }
    });
    return fundApplyTravelDto;
}

/**
 * 保存操作
 */
FundBudgetApplyTravelInfoDlg.saveSubmit = function() {
    if (!this.validate()) {
        return;
    }
    var fundApplyTravelDto = FundBudgetApplyTravelInfoDlg.getFundApplyDetail();
    var param = {"fundApplyDetailDtos" : fundApplyTravelDto,
        "deptid": $("#deptid").val(),
        "deptname": $("#departs").val(),
        "location": $("#location").val(),
        "type": 2};
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
 * 增加操作
 */
FundBudgetApplyTravelInfoDlg.addSubmit = function() {
    if (!this.validate()) {
        return;
    }
    var fundApplyTravelDto = FundBudgetApplyTravelInfoDlg.getFundApplyDetail();
    var param = {"fundApplyDetailDtos" : fundApplyTravelDto,
                 "deptid": $("#deptid").val(),
                 "id": $("#aid").val(),
                 "location": $("#location").val(),
                 "type": 2};
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
                 eng.error("开始流程失败!" + data.message + "!");
             }
             window.location.href = Feng.ctxPath + "/fund/budget/apply";
         },
         error: function (data) {
             Feng.error("开始流程失败!" + data.message + "!");
         }
    });
}


/**
 * 提交修改
 */
FundBudgetApplyTravelInfoDlg.editSubmit = function() {
    if (!this.validate()) {
        return;
    }

    var fundApplyTravelDto = FundBudgetApplyTravelInfoDlg.getFundApplyDetail();
    var param = {"fundApplyDetailDtos" : fundApplyTravelDto,
        "id": $("#aid").val(),
        "type": $("#type").val()};
    var jsonTemp = JSON.stringify(param);
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
 * 添加行
 */
FundBudgetApplyTravelInfoDlg.addRow = function() {
    var index = $("#para_table").find("tr").length;
    var table = $("#para_table");
    var tr= $('<tr class="travelContent" id="' + (index-1) + '"><td class="col-md-6"><div class="form-group"><div class="col-sm-12">' +
        '<input class="form-control" id="subject" name="subject' + (index-1) + '" type="text" readonly="readonly" onclick="FundBudgetApplyTravelInfoDlg.showSubjectSelectTree(this); return false;" style="background-color: #ffffff !important;">' +
        '<input class="form-control" type="hidden" id="subjectid" name="subjectid' + (index-1) + '"></div></div></td>' +
        '<td class="col-md-6"><div class="form-group"><div class="col-sm-12">' +
        '<select class="form-control" id="project" name="project' + (index-1) + '"></select><input class="form-control" type="hidden" id="projectid" name="projectid' + (index-1) + '"/></div></div></td>' +
        '<td class="col-md-6"><div class="form-group"><div class="col-sm-12">' +
        '<input class="form-control" id="empname" name="empname' + (index-1) + '" type="text"></div></div></td>' +
        '<td class="col-md-6"><div class="form-group"><div class="col-sm-12">' +
        '<input class="form-control" id="traveldest" name="traveldest' + (index-1) + '" type="text"></div></div></td>' +
        '<td class="col-md-6"><div class="form-group"><div class="col-sm-12">' +
        '<input class="form-control" id="traveldays" name="traveldays' + (index-1) + '" type="text" value="0" onblur="FundBudgetApplyTravelInfoDlg.countTotal(this);return false;"></div></div></td>' +
        '<td class="col-md-6"><div class="form-group"><div class="col-sm-12">' +
        '<input class="form-control" id="travelfee" name="travelfee' + (index-1) + '" type="text" value="0" onblur="FundBudgetApplyTravelInfoDlg.countTotal(this);return false;"></div></div></td>' +
        '<td class="col-md-6"><div class="form-group"><div class="col-sm-12">' +
        '<input class="form-control" id="trafficfee" name="trafficfee' + (index-1) + '" type="text" value="0" onblur="FundBudgetApplyTravelInfoDlg.countTotal(this);return false;"></div></div></td>' +
        '<td class="col-md-6"><div class="form-group"><div class="col-sm-12">' +
        '<input class="form-control" id="accommodationfee" name="accommodationfee' + (index-1) + '" type="text" value="0" onblur="FundBudgetApplyTravelInfoDlg.countTotal(this);return false;"></div></div></td>' +
        '<td class="col-md-6"><div class="form-group"><div class="col-sm-12">' +
        '<input class="form-control" id="subsidy" name="subsidy' + (index-1) + '" type="text" value="0" readonly="readonly"></div></div></td>' +
        '<td class="col-md-6"><div class="form-group"><div class="col-sm-12">' +
        '<input class="form-control" id="totalfee" name="totalfee' + (index-1) + '" type="text" value="0" readonly="readonly"></div></div></td>' +
        '<td class="col-md-6"><div class="form-group"><div class="col-sm-12">' +
        '<input class="form-control" id="description" name="description' + (index-1) + '" type="text"></div></div></td>' +
        '<td class="col-md-6"><button type="button" class="btn btn-primary " onclick="FundBudgetApplyTravelInfoDlg.deleteRow(this, \'add\', 0)" id=""><i class="fa fa-minus"></i>&nbsp;删除</button></td></tr>');
    table.append(tr);
    initProjectOptionData(index-1);
    sortTrNumber();
    $('#travelInfoForm').bootstrapValidator('addField', 'subject' + (index-1), {
        validators: {
            notEmpty: {
                message: '科目不能为空'
            }
        }
    });
    $('#travelInfoForm').bootstrapValidator('addField', 'projectname' + (index-1), {
        validators: {
            notEmpty: {
                message: '项目名称不能为空'
            }
        }
    });
    $('#travelInfoForm').bootstrapValidator('addField', 'empname' + (index-1), {
        validators: {
            notEmpty: {
                message: '员工姓名不能为空'
            }
        }
    });
    $('#travelInfoForm').bootstrapValidator('addField', 'traveldest' + (index-1), {
        validators: {
            notEmpty: {
                message: '出差目的地不能为空'
            }
        }
    });
    $('#travelInfoForm').bootstrapValidator('addField', 'traveldays' + (index-1), {
        validators: {
            notEmpty: {
                message: '出差天数不能为空'
            },
            digits: {
                message: '出差天数只能为数字'
            }
        }
    });
    $('#travelInfoForm').bootstrapValidator('addField', 'travelfee' + (index-1), {
        validators: {
            notEmpty: {
                message: '长途交通费不能为空'
            },
            regexp: {
                regexp : /^\d+(\.\d{0,2})?$/i,
                message: '只能为数字含两位小数'
            }
        }
    });
    $('#travelInfoForm').bootstrapValidator('addField', 'trafficfee' + (index-1), {
        validators: {
            notEmpty: {
                message: '往返交通费不能为空'
            },
            regexp: {
                regexp : /^\d+(\.\d{0,2})?$/i,
                message: '只能为数字含两位小数'
            }
        }
    });
    $('#travelInfoForm').bootstrapValidator('addField', 'accommodationfee' + (index-1), {
        validators: {
            notEmpty: {
                message: '住宿费不能为空'
            },
            regexp: {
                regexp : /^\d+(\.\d{0,2})?$/i,
                message: '只能为数字含两位小数'
            }
        }
    });
    $('#travelInfoForm').bootstrapValidator('addField', 'subsidy' + (index-1), {
        validators: {
            notEmpty: {
                message: '补贴支出不能为空'
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
FundBudgetApplyTravelInfoDlg.deleteRow = function(tdobject, type, id) {
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
FundBudgetApplyTravelInfoDlg.showDeptSelectTree = function () {
    Feng.showInputTree("departs", "menuContent");
};

/**
 * 小计金额的计算
 */
FundBudgetApplyTravelInfoDlg.countTotal = function(obj) {
    var td = $(obj);
    var index = td.parents("tr").attr("id");
    var traveldays = $("input[name='traveldays" + index +"']").val();
    if(traveldays == '' || traveldays == undefined){
        traveldays = 0;
    }
    var travelfee = $("input[name='travelfee" + index +"']").val();
    if(travelfee == '' || travelfee == undefined){
        travelfee = 0;
    }
    var trafficfee = $("input[name='trafficfee" + index +"']").val();
    if(trafficfee == '' || trafficfee == undefined){
        trafficfee = 0;
    }
    var accommodationfee = $("input[name='accommodationfee" + index +"']").val();
    if(accommodationfee == '' || accommodationfee == undefined){
        accommodationfee = 0;
    }
    var subsidy = Math.floor(parseFloat(traveldays * 100 * 85))/100;
    $("input[name='subsidy" + index +"']").val(subsidy);
    var totalfee = Math.floor(parseFloat(travelfee * 100 + trafficfee * 100 + accommodationfee * 100 + subsidy * 100))/100;
    $("input[name='totalfee" + index +"']").val(totalfee);
}

var index = 0;
FundBudgetApplyTravelInfoDlg.showSubjectSelectTree = function (obj) {
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
        FundBudgetApplyTravelInfoDlg.hideSubjectSelectTree();
    }
}

/**
 * 隐藏科目选择的树
 */
FundBudgetApplyTravelInfoDlg.hideSubjectSelectTree = function() {
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
FundBudgetApplyTravelInfoDlg.onClickDept = function (e, treeId, treeNode) {
    var selVal = FundBudgetApplyTravelInfoDlg.deptZtree.getSelectedVal();
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
FundBudgetApplyTravelInfoDlg.onClickSubject = function (e, treeId, treeNode) {
    var selVal = FundBudgetApplyTravelInfoDlg.subjectZtreeInstance.getSelectedVal();
    $("input[name='subject" + index +"']").attr("value", selVal);
    $("input[name='subjectid" + index +"']").attr("value", treeNode.id);
};

/**
 * 验证数据
 */
FundBudgetApplyTravelInfoDlg.validate = function () {
    $('#travelInfoForm').data("bootstrapValidator").resetForm();
    $('#travelInfoForm').bootstrapValidator('validate');
    return $("#travelInfoForm").data('bootstrapValidator').isValid();
};


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


$(function() {
    Feng.initValidator("travelInfoForm", FundBudgetApplyTravelInfoDlg.validateFields);
    // 初始化的时候对validater动态添加
    var index = $("#para_table").find("tr").length;
    for (var i=0; i<index-1; i++) {
        // 初始化项目的下拉框选项
        initProjectOptionData(i);
        // 拼接value值
        var id = $("input[name='projectid" + i + "']").val();
        // 初始化下拉框
        $("select[name='project" + i + "']").val(id);

        $('#travelInfoForm').bootstrapValidator('addField', 'subject' + (i+1), {
            validators: {
                notEmpty: {
                    message: '科目不能为空'
                }
            }
        });
        $('#travelInfoForm').bootstrapValidator('addField', 'projectname' + (i+1), {
            validators: {
                notEmpty: {
                    message: '项目名称不能为空'
                }
            }
        });
        $('#travelInfoForm').bootstrapValidator('addField', 'empname' + (i+1), {
            validators: {
                notEmpty: {
                    message: '员工姓名不能为空'
                }
            }
        });
        $('#travelInfoForm').bootstrapValidator('addField', 'traveldest' + (i+1), {
            validators: {
                notEmpty: {
                    message: '出差目的地不能为空'
                }
            }
        });
        $('#travelInfoForm').bootstrapValidator('addField', 'traveldays' + (i+1), {
            validators: {
                notEmpty: {
                    message: '出差天数不能为空'
                },
                digits: {
                    message: '出差天数只能为数字'
                }
            }
        });
        $('#travelInfoForm').bootstrapValidator('addField', 'travelfee' + (i+1), {
            validators: {
                notEmpty: {
                    message: '长途交通费不能为空'
                },
                regexp: {
                    regexp : /^\d+(\.\d{0,2})?$/i,
                    message: '只能为数字含两位小数'
                }
            }
        });
        $('#travelInfoForm').bootstrapValidator('addField', 'trafficfee' + (i+1), {
            validators: {
                notEmpty: {
                    message: '往返交通费不能为空'
                },
                regexp: {
                    regexp : /^\d+(\.\d{0,2})?$/i,
                    message: '只能为数字含两位小数'
                }
            }
        });
        $('#travelInfoForm').bootstrapValidator('addField', 'accommodationfee' + (i+1), {
            validators: {
                notEmpty: {
                    message: '住宿费不能为空'
                },
                regexp: {
                    regexp : /^\d+(\.\d{0,2})?$/i,
                    message: '只能为数字含两位小数'
                }
            }
        });
        $('#travelInfoForm').bootstrapValidator('addField', 'subsidy' + (i+1), {
            validators: {
                notEmpty: {
                    message: '补贴支出不能为空'
                },
                regexp: {
                    regexp : /^\d+(\.\d{0,2})?$/i,
                    message: '只能为数字含两位小数'
                }
            }
        });
    }

    var ztree = new $ZTree("treeDemo", "/dept/tree");
    ztree.bindOnClick(FundBudgetApplyTravelInfoDlg.onClickDept);
    ztree.init();
    FundBudgetApplyTravelInfoDlg.deptZtree = ztree;

    var subjectree = new $ZTree("treeSubjectDemo", "/subject/tree");
    subjectree.bindOnClick(FundBudgetApplyTravelInfoDlg.onClickSubject);
    subjectree.init();
    FundBudgetApplyTravelInfoDlg.subjectZtreeInstance = subjectree;
});
