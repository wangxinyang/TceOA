/**
 * 费用申请管理初始化
 */
var CostPaymentApply = {
    id: "CostPaymentApplyTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
CostPaymentApply.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        {title: '', field: 'id', visible: false, align: 'center', valign: 'middle'},
        {title: '申请地点', field: 'location', visible: true, align: 'center', valign: 'middle', width:'100px'},
        {title: '申请部门', field: 'deptname', visible: true, align: 'center', valign: 'middle', width:'100px'},
        {title: '申请单号', field: 'reqno', visible: true, align: 'center', valign: 'middle', width:'150px'},
        {title: '金额(元)', field: 'totalfee', visible: true, align: 'center', valign: 'middle'},
        {title: '申请状态', field: 'statemean', visible: true, align: 'center', valign: 'middle'},
        {title: '申请时间', field: 'createtime', visible: true, align: 'center', valign: 'middle'},
        {
            title: '操作', visible: true, align: 'center', valign: 'middle', formatter: function (value, row, index) {
                if (row.state == 2 || row.state == 3 || row.state == 4 || row.state == 5 || row.state == 6 || row.state == 7) {
                    return '<button type="button" class="btn btn-primary button-margin" onclick="CostPaymentApply.findRecord(' + row.id + ')" id=""><i class="fa fa-edit"></i>流程</button>';
                } else if (row.state == 1 || row.state == 8 ||row.state == 10) {
                    return '<button type="button" class="btn btn-primary button-margin" onclick="CostPaymentApply.openCostPaymentApplyDetail()" id=""><i class="fa fa-arrows-alt"></i>详情</button>'+
                        '<button type="button" class="btn btn-danger button-margin" onclick="CostPaymentApply.delete(' + row.id + ')" id=""><i class="fa fa-arrows-alt"></i>删除</button>';
                } else {
                    return '<button type="button" class="btn btn-primary button-margin" onclick="CostPaymentApply.openCostPaymentApplyDetail()" id=""><i class="fa fa-arrows-alt"></i>详情</button>';
                }
            }
        }
    ];
};

/**
 * 检查是否选中
 */
CostPaymentApply.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        CostPaymentApply.seItem = selected[0];
        return true;
    }
};

/**
 * 流程详情
 */
CostPaymentApply.findRecord = function (id) {
    var index = layer.open({
        type: 2,
        title: '流程详情',
        area: ['1200px', '600px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/cost/apply/payment/showProcess/' + id
    });
    this.layerIndex = index;
};

/**
 * 点击添加费用申请
 */
CostPaymentApply.openAddCostPaymentApply = function () {
    window.location.href = Feng.ctxPath + "/cost/apply/payment/to_add";
};

/**
 * 打开查看费用申请修改
 */
CostPaymentApply.openUpdatePaymentApplyDetail = function () {
    var flag = true;
    if (this.check()) {
        // 先查询能否删除预算的申请
        var ajax = new $ax(Feng.ctxPath + "/cost/apply/payment/can/update", function (data) {
            if (data.code == 500) {
                Feng.info(data.message);
                flag = false;
            }
        }, function (data) {
            Feng.error("查询失败!" + data.message + "!");
        });
        ajax.set("id", CostPaymentApply.seItem.id);
        ajax.start();
        if (flag) {
            window.location.href = Feng.ctxPath + "/cost/apply/payment/to_update/" + CostPaymentApply.seItem.id;
        }
    }
};

/**
 * 打开查看费用报销详情
 */
CostPaymentApply.openCostPaymentApplyDetail = function () {
    if (this.check()) {
        window.location.href = Feng.ctxPath + "/cost/apply/payment/to_detail/" + CostPaymentApply.seItem.id;
    }
};

/**
 * 删除费用申请
 */
CostPaymentApply.delete = function () {
    var flag = true;
    if (this.check()) {
        // 先查询能否删除预算的申请
        var ajax = new $ax(Feng.ctxPath + "/cost/apply/payment/can/delete", function (data) {
            if (data.code == 500) {
                Feng.info(data.message);
                flag = false;
            }
        }, function (data) {
            Feng.error("查询失败!" + data.message + "!");
        });
        ajax.set("id", CostPaymentApply.seItem.id);
        ajax.start();
        if (flag) {
            var ajax = new $ax(Feng.ctxPath + "/cost/apply/payment/delete", function (data) {
                Feng.success("删除成功!");
                CostPaymentApply.table.refresh();
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("id", this.seItem.id);
            ajax.start();
        }
    }
};

/**
 * 导出费用报销
 */
CostPaymentApply.export = function () {
    var flag = true;
    if (this.check()) {
        // 先查询能否导出数据
        var ajax = new $ax(Feng.ctxPath + "/cost/apply/payment/can/export", function (data) {
            if (data.code == 500) {
                Feng.info(data.message);
                flag = false;
            }
        }, function (data) {
            Feng.error("查询失败!" + data.message + "!");
        });
        ajax.set("id", CostPaymentApply.seItem.id);
        ajax.start();
        if (flag) {
            window.location.href = Feng.ctxPath + "/cost/apply/payment/export/" + this.seItem.id;
        }
    }
}


$(function () {
    var defaultColunms = CostPaymentApply.initColumn();
    var table = new BSTable(CostPaymentApply.id, "/cost/apply/payment/list", defaultColunms);
    table.setPaginationType("client");
    CostPaymentApply.table = table.init();
});
