/**
 * 资金预算管理初始化
 */
var FundBudgetApply = {
    id: "FundBudgetApplyTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
FundBudgetApply.initColumn = function () {
    return [
            {field: 'selectItem', radio: true, height:'56px'},
            {title: 'id', field: 'id', visible: false, align: 'center', valign: 'middle', width:'60px', height:'56px'},
            {title: '申请地点', field: 'location', visible: true, align: 'center', valign: 'middle', width:'100px'},
            {title: '申请部门', field: 'deptname', visible: true, align: 'center', valign: 'middle', width:'100px'},
            {title: '申请单号', field: 'reqno', visible: true, align: 'center', valign: 'middle', width:'150px'},
            {title: '申请类型', field: 'typename', visible: true, align: 'center', valign: 'middle'},
            {title: '预算金额(元)', field: 'money', visible: true, align: 'center', valign: 'middle'},
            {title: '申请状态', field: 'statemean', visible: true, align: 'center', valign: 'middle'},
            {title: '申请时间', field: 'createtime', visible: true, align: 'center', valign: 'middle'},
            {
                title: '操作', visible: true, align: 'center', valign: 'middle', formatter: function (value, row, index) {
                    // if (row.state == 1 || row.state == 5) {
                    //     return '<button type="button" class="btn btn-primary button-margin" onclick="FundBudgetApply.startProcess(' + row.id + ',' + row.type + ')" id=""><i class="fa fa-edit"></i>&nbsp;提交流程</button>' +
                    //         '<button type="button" class="btn btn-danger button-margin" onclick="FundBudgetApply.delete(' + row.id + ')" id=""><i class="fa fa-arrows-alt"></i>&nbsp;删除申请</button>';
                    // } else if (row.state == 2) {
                    //     return '<button type="button" class="btn btn-primary button-margin" onclick="FundBudgetApply.findRecord(' + row.id + ')" id=""><i class="fa fa-edit"></i>&nbsp;查看流程图</button>';
                    // } else if (row.state == 4) {
                    //     return '<button type="button" class="btn btn-primary button-margin" onclick="FundBudgetApply.startProcess(' + row.id + ',' + row.type + ')" id=""><i class="fa fa-edit"></i>&nbsp;重新提交</button>' +
                    //         '<button type="button" class="btn btn-danger button-margin" onclick="FundBudgetApply.delete(' + row.id + ')" id=""><i class="fa fa-arrows-alt"></i>&nbsp;删除申请</button>';
                    // }
                    if (row.state == 2 || row.state == 3 || row.state == 4 || row.state == 5) {
                        return '<button type="button" class="btn btn-primary button-margin" onclick="FundBudgetApply.findRecord(' + row.id + ')" id=""><i class="fa fa-edit"></i>流程</button>';
                    } else if (row.state == 1 || row.state == 6 ||row.state == 8) {
                        return '<button type="button" class="btn btn-primary button-margin" onclick="FundBudgetApply.openFundBudgetApplyDetail()" id=""><i class="fa fa-arrows-alt"></i>详情</button>'+
                            '<button type="button" class="btn btn-danger button-margin" onclick="FundBudgetApply.delete(' + row.id + ')" id=""><i class="fa fa-arrows-alt"></i>删除</button>';
                    } else {
                        return '<button type="button" class="btn btn-primary button-margin" onclick="FundBudgetApply.openFundBudgetApplyDetail()" id=""><i class="fa fa-arrows-alt"></i>详情</button>';
                    }
                }
            }
    ];
};

/**
 * 开始流程操作
 */
FundBudgetApply.startProcess = function(id, type) {
    var param = {"id": id, "type": type};
    var jsonTemp = JSON.stringify(param);
    //提交信息
    $.ajax({
        type: "post",
        url: Feng.ctxPath + "/fund/budget/apply/start",
        data: jsonTemp,
        contentType:"application/json;charset=utf-8",
        dataType: "json",
        success: function (data) {
            if (data.code == 200) {
                Feng.success("开始流程成功!");
                FundBudgetApply.table.refresh();
            } else {
                Feng.error("开始流程失败!" + data.message + "!");
            }
        },
        error: function (data) {
            Feng.error("开始流程失败!" + data.message + "!");
        }
    });
}

/**
 * 流程详情
 */
FundBudgetApply.findRecord = function (id) {
    var index = layer.open({
        type: 2,
        title: '流程详情',
        area: ['1200px', '600px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/fund/budget/apply/showFundSalesProcess/' + id
    });
    this.layerIndex = index;
};

/**
 * 检查是否选中
 */
FundBudgetApply.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        FundBudgetApply.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加资金预算
 */
FundBudgetApply.openAddFundBudgetApply = function (type) {
    var flag = true;
    // 先查询本月是否还能增加预算的申请
    var ajax = new $ax(Feng.ctxPath + "/fund/budget/apply/can/add", function(data){
        if (data.code == 500) {
            Feng.info(data.message);
            flag = false;
        }
    },function(data){
        Feng.error("查询失败!" + data.message + "!");
    });
    ajax.set("type", type);
    ajax.start();
    if (flag) {
        var title = "";
        switch (type) {
            case "1":
                title = "添加业务费用预算";
                break;
            case "2":
                title = "添加差旅费用预算";
                break;
            case "3":
                title = "添加采购费用预算";
                break;
            case "4":
                title = "添加其它费用预算";
                break;
            default :
                title = "添加业务费用预算";
                break;
        }
        window.location.href = Feng.ctxPath + "/fund/budget/apply/add/" + type;
    }
};

/**
 * 打开查看资金预算详情
 */
FundBudgetApply.openFundBudgetApplyDetail = function () {
    if (this.check()) {
        window.location.href = Feng.ctxPath + "/fund/budget/apply/toDetail/" + FundBudgetApply.seItem.id + "/" + FundBudgetApply.seItem.type;
    }
};

/**
 * 打开查看资金预算详情更新
 */
FundBudgetApply.openFundBudgetApplyDetailUpdate = function () {
    var flag = true;
    if (this.check()) {
        // 先查询能否删除预算的申请
        var ajax = new $ax(Feng.ctxPath + "/fund/budget/apply/can/update", function(data){
            if (data.code == 500) {
                Feng.info(data.message);
                flag = false;
            }
        },function(data){
            Feng.error("查询失败!" + data.message + "!");
        });
        ajax.set("fundApplyId",this.seItem.id);
        ajax.start();
        if (flag) {
            window.location.href = Feng.ctxPath + "/fund/budget/apply/toUpdate/" + FundBudgetApply.seItem.id + "/" + FundBudgetApply.seItem.type;
        }
    }
};

/**
 * 删除资金预算
 */
FundBudgetApply.delete = function () {
    var flag = true;

    if (this.check()) {
        // 先查询能否删除预算的申请
        var ajax = new $ax(Feng.ctxPath + "/fund/budget/apply/can/delete", function(data){
            if (data.code == 500) {
                Feng.info(data.message);
                flag = false;
            }
        },function(data){
            Feng.error("查询失败!" + data.message + "!");
        });
        ajax.set("fundApplyId",this.seItem.id);
        ajax.start();
        if (flag) {
            var ajax = new $ax(Feng.ctxPath + "/fund/budget/apply/delete", function (data) {
                Feng.success("删除成功!");
                FundBudgetApply.table.refresh();
            }, function (data) {
                Feng.error("删除失败!" + data.message + "!");
            });
            ajax.set("fundApplyId",this.seItem.id);
            ajax.set("type",this.seItem.type);
            ajax.start();
        }

    }
};

var data = '[{"id":"1","name":"业务预算"},{"id":"2","name":"差旅费预算"},{"id":"3","name":"采购预算"},{"id":"4","name":"其它预算"}]';
FundBudgetApply.get_direct_select = function() {
    var d = eval("(" + data + ")");
    var selectValue="";
    for(var i=0;i<d.length;i++){
        selectValue=$("<li id='"+d[i].id+"'><a href='#' onclick='directBtnSelect(\""+d[i].id+"\",\""+d[i].name+"\")'>"+d[i].name+"</a></li>");
        $(".direct").append(selectValue);
    }
}

function directBtnSelect(id,name) {
    FundBudgetApply.openAddFundBudgetApply(id);
    $(".directText").html(name);
}

$(function () {
    var defaultColunms = FundBudgetApply.initColumn();
    var table = new BSTable(FundBudgetApply.id, "/fund/budget/apply/list", defaultColunms);
    table.setPaginationType("client");
    FundBudgetApply.table = table.init();
    FundBudgetApply.get_direct_select();
});
