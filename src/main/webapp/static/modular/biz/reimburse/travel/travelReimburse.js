/**
 * 差旅费报销管理初始化
 */
var TravelReimburse = {
    id: "TravelReimburseTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
TravelReimburse.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        {title: '', field: 'id', visible: false, align: 'center', valign: 'middle'},
        {title: '地区', field: 'locationame', visible: true, align: 'center', valign: 'middle'},
        {title: '部门', field: 'deptname', visible: true, align: 'center', valign: 'middle'},
        {title: '报销单号', field: 'reimburseno', visible: true, align: 'center', valign: 'middle'},
        {title: '报销人', field: 'user', visible: true, align: 'center', valign: 'middle'},
        {title: '项目名称', field: 'projectname', visible: false, align: 'center', valign: 'middle'},
        {title: '出差事由', field: 'travelreason', visible: false, align: 'center', valign: 'middle'},
        {title: '报销金额(元)', field: 'totalfee', visible: false, align: 'center', valign: 'middle'},
        {title: '申请时间', field: 'applytime', visible: true, align: 'center', valign: 'middle'},
        {title: '状态', field: 'statename', visible: true, align: 'center', valign: 'middle'},
        {
            title: '操作', visible: true, align: 'center', valign: 'middle', formatter: function (value, row, index) {
                if (row.state == 2 || row.state == 3 || row.state == 4 || row.state == 5 || row.state == 6 || row.state == 7) {
                    return '<button type="button" class="btn btn-primary button-margin" onclick="TravelReimburse.findRecord(' + row.id + ')" id=""><i class="fa fa-edit"></i>&nbsp;流程</button>';
                } else if (row.state == 1 || row.state == 8 || row.state == 10) {
                    return '<button type="button" class="btn btn-primary button-margin" onclick="TravelReimburse.openFundBudgetApplyDetail()" id=""><i class="fa fa-arrows-alt"></i>&nbsp;详情</button>' +
                        '<button type="button" class="btn btn-danger button-margin" onclick="TravelReimburse.delete(' + row.id + ')" id=""><i class="fa fa-arrows-alt"></i>&nbsp;删除</button>';
                } else {
                    return '<button type="button" class="btn btn-primary button-margin" onclick="TravelReimburse.openFundBudgetApplyDetail()" id=""><i class="fa fa-arrows-alt"></i>&nbsp;详情</button>';
                }
            }
        }
    ];
};

/**
 * 检查是否选中
 */
TravelReimburse.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if (selected.length == 0) {
        Feng.info("请先选中表格中的某一记录！");
        return false;
    } else {
        TravelReimburse.seItem = selected[0];
        return true;
    }
};

/**
 * 流程详情
 */
TravelReimburse.findRecord = function (id) {
    var index = layer.open({
        type: 2,
        title: '流程详情',
        area: ['1200px', '600px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/travel/reimburse/showProcess/' + id
    });
    this.layerIndex = index;
};

/**
 * 点击添加差旅费报销
 */
TravelReimburse.openAddTravelReimburse = function () {
    window.location.href = Feng.ctxPath + "/travel/reimburse/toAdd";
};

/**
 * 打开查看差旅费报销编辑页面
 */
TravelReimburse.openUpdateTravelReimburse = function () {
    var flag = true;
    if (this.check()) {
        // 先查询能否删除预算的申请
        var ajax = new $ax(Feng.ctxPath + "/travel/reimburse/can/update", function (data) {
            if (data.code == 500) {
                Feng.info(data.message);
                flag = false;
            }
        }, function (data) {
            Feng.error("查询失败!" + data.message + "!");
        });
        ajax.set("id", TravelReimburse.seItem.id);
        ajax.start();
        if (flag) {
            window.location.href = Feng.ctxPath + "/travel/reimburse/toUpdate/" + TravelReimburse.seItem.id;
        }

    }
};

/**
 * 打开查看差旅费报销详情页面
 */
TravelReimburse.openFundBudgetApplyDetail = function () {
    if (this.check()) {
        window.location.href = Feng.ctxPath + "/travel/reimburse/toDetail/" + TravelReimburse.seItem.id;
    }
};

/**
 * 删除差旅费报销
 */
TravelReimburse.delete = function () {
    var flag = true;
    if (this.check()) {
        // 先查询能否删除预算的申请
        var ajax = new $ax(Feng.ctxPath + "/travel/reimburse/can/delete", function (data) {
            if (data.code == 500) {
                Feng.info(data.message);
                flag = false;
            }
        }, function (data) {
            Feng.error("查询失败!" + data.message + "!");
        });
        ajax.set("id", TravelReimburse.seItem.id);
        ajax.start();
        if (flag) {
            var ajax = new $ax(Feng.ctxPath + "/travel/reimburse/delete", function (data) {
                Feng.success("删除成功!");
                TravelReimburse.table.refresh();
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("id", this.seItem.id);
            ajax.start();
        }
    }
};

/**
 * 导出差旅费报销
 */
TravelReimburse.export = function () {
    var flag = true;
    if (this.check()) {
        // 先查询能否导出数据
        var ajax = new $ax(Feng.ctxPath + "/travel/reimburse/can/export", function (data) {
            if (data.code == 500) {
                Feng.info(data.message);
                flag = false;
            }
        }, function (data) {
            Feng.error("查询失败!" + data.message + "!");
        });
        ajax.set("id", TravelReimburse.seItem.id);
        ajax.start();
        if (flag) {
            // var ajax = new $ax(Feng.ctxPath + "/reimburse/export/travel", function (data) {
            //     Feng.success(data.message);
            //     TravelReimburse.table.refresh();
            // }, function (data) {
            //     Feng.error("导出失败!" + data.message + "!");
            // });
            // ajax.set("id", this.seItem.id);
            // ajax.start();
            window.location.href = Feng.ctxPath + "/reimburse/export/travel/" + this.seItem.id;
        }
    }
}

$(function () {
    var defaultColunms = TravelReimburse.initColumn();
    var table = new BSTable(TravelReimburse.id, "/travel/reimburse/list", defaultColunms);
    table.setPaginationType("client");
    TravelReimburse.table = table.init();
});
