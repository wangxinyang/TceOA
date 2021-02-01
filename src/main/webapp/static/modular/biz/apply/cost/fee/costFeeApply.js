/**
 * 费用申请管理初始化
 */
var CostFeeApply = {
    id: "CostFeeApplyTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
CostFeeApply.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        {title: '', field: 'id', visible: false, align: 'center', valign: 'middle'},
        {title: '申请地点', field: 'location', visible: true, align: 'center', valign: 'middle', width:'100px'},
        {title: '申请部门', field: 'deptname', visible: true, align: 'center', valign: 'middle', width:'100px'},
        {title: '申请单号', field: 'reqno', visible: true, align: 'center', valign: 'middle', width:'150px'},
        {title: '预计总价(元)', field: 'totalfee', visible: true, align: 'center', valign: 'middle'},
        {title: '申请状态', field: 'statemean', visible: true, align: 'center', valign: 'middle'},
        {title: '申请时间', field: 'createtime', visible: true, align: 'center', valign: 'middle'},
        {
            title: '操作', visible: true, align: 'center', valign: 'middle', formatter: function (value, row, index) {
                if (row.state == 2 || row.state == 3 || row.state == 4 || row.state == 5 || row.state == 6 || row.state == 7) {
                    return '<button type="button" class="btn btn-primary button-margin" onclick="CostFeeApply.findRecord(' + row.id + ')" id=""><i class="fa fa-edit"></i>流程</button>';
                } else if (row.state == 1 || row.state == 8 ||row.state == 10) {
                    return '<button type="button" class="btn btn-primary button-margin" onclick="CostFeeApply.openCostFeeApplyDetail()" id=""><i class="fa fa-arrows-alt"></i>详情</button>'+
                        '<button type="button" class="btn btn-danger button-margin" onclick="CostFeeApply.delete(' + row.id + ')" id=""><i class="fa fa-arrows-alt"></i>删除</button>';
                } else {
                    return '<button type="button" class="btn btn-primary button-margin" onclick="CostFeeApply.openCostFeeApplyDetail()" id=""><i class="fa fa-arrows-alt"></i>详情</button>';
                }
            }
        }
    ];
};

/**
 * 检查是否选中
 */
CostFeeApply.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        CostFeeApply.seItem = selected[0];
        return true;
    }
};

/**
 * 流程详情
 */
CostFeeApply.findRecord = function (id) {
    var index = layer.open({
        type: 2,
        title: '流程详情',
        area: ['1200px', '600px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/cost/apply/fee/showProcess/' + id
    });
    this.layerIndex = index;
};

/**
 * 点击添加费用申请
 */
CostFeeApply.openAddCostFeeApply = function () {
    window.location.href = Feng.ctxPath + "/cost/apply/fee/to_add";
};

/**
 * 打开查看费用申请详情
 */
CostFeeApply.openUpdateCostFeeApplyDetail = function () {
    var flag = true;
    if (this.check()) {
        // 先查询能否删除预算的申请
        var ajax = new $ax(Feng.ctxPath + "/cost/apply/fee/can/update", function (data) {
            if (data.code == 500) {
                Feng.info(data.message);
                flag = false;
            }
        }, function (data) {
            Feng.error("查询失败!" + data.message + "!");
        });
        ajax.set("id", CostFeeApply.seItem.id);
        ajax.start();
        if (flag) {
            window.location.href = Feng.ctxPath + "/cost/apply/fee/to_update/" + CostFeeApply.seItem.id;
        }
    }
};

/**
 * 打开查看费用报销详情
 */
CostFeeApply.openCostFeeApplyDetail = function () {
    if (this.check()) {
        window.location.href = Feng.ctxPath + "/cost/apply/fee/to_detail/" + CostFeeApply.seItem.id;
    }
};

/**
 * 删除费用申请
 */
CostFeeApply.delete = function () {
    var flag = true;
    if (this.check()) {
        // 先查询能否删除预算的申请
        var ajax = new $ax(Feng.ctxPath + "/cost/apply/fee/can/delete", function (data) {
            if (data.code == 500) {
                Feng.info(data.message);
                flag = false;
            }
        }, function (data) {
            Feng.error("查询失败!" + data.message + "!");
        });
        ajax.set("id", CostFeeApply.seItem.id);
        ajax.start();
        if (flag) {
            var ajax = new $ax(Feng.ctxPath + "/cost/apply/fee/delete", function (data) {
                Feng.success("删除成功!");
                CostFeeApply.table.refresh();
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
CostFeeApply.export = function () {
    var flag = true;
    if (this.check()) {
        // 先查询能否导出数据
        var ajax = new $ax(Feng.ctxPath + "/cost/apply/fee/can/export", function (data) {
            if (data.code == 500) {
                Feng.info(data.message);
                flag = false;
            }
        }, function (data) {
            Feng.error("查询失败!" + data.message + "!");
        });
        ajax.set("id", CostFeeApply.seItem.id);
        ajax.start();
        if (flag) {
            window.location.href = Feng.ctxPath + "/cost/apply/fee/export/" + this.seItem.id;
        }
    }
}


$(function () {
    var defaultColunms = CostFeeApply.initColumn();
    var table = new BSTable(CostFeeApply.id, "/cost/apply/fee/list", defaultColunms);
    table.setPaginationType("client");
    CostFeeApply.table = table.init();
});
