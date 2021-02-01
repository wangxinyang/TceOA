/**
 * 资金预算管理初始化
 */
var FundAmount = {
    id: "fundAmountTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
FundAmount.initColumn = function () {
    return [
            {field: 'selectItem', radio: true},
            {title: 'id', field: 'id', visible: false, align: 'center', valign: 'middle'},
            {title: '申请地点', field: 'location', visible: true, align: 'center', valign: 'middle'},
            {title: '申请部门', field: 'deptname', visible: true, align: 'center', valign: 'middle'},
            {title: '申请人', field: 'user', visible: true, align: 'center', valign: 'middle'},
            {title: '申请类型', field: 'typename', visible: true, align: 'center', valign: 'middle'},
            {title: '预算金额(元)', field: 'money', visible: true, align: 'center', valign: 'middle'},
            {title: '申请状态', field: 'statemean', visible: true, align: 'center', valign: 'middle'},
            {title: '申请时间', field: 'createtime', visible: true, align: 'center', valign: 'middle'},
            {
                title: '操作', visible: true, align: 'center', valign: 'middle', formatter: function (value, row, index) {
                    return '<button type="button" class="btn btn-primary" onclick="FundAmount.detail(\'' + row.id + '\', \'' + row.type + '\')" id=""><i class="fa fa-table"></i>&nbsp;详情</button>';
                }
            }
    ];
};

/**
 * 检查是否选中
 */
FundAmount.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        FundAmount.seItem = selected[0];
        return true;
    }
};

FundAmount.resetSearch = function () {
    $("#applyType").val("0");
    $("#beginTime").val("");
    $("#endTime").val("");

    FundAmount.search();
}

FundAmount.search = function () {
    FundAmount.table.refresh({query: FundAmount.formParams()});
}


/**
 * 打开查看资金预算详情更新
 */
FundAmount.openFundAmountDetailUpdate = function () {
    var flag = true;
    if (this.check()) {
        window.location.href = Feng.ctxPath + "/fund/amount/toUpdate/" + FundAmount.seItem.id + "/" + FundAmount.seItem.type;
    }
};

/**
 * 导出
 */
FundAmount.export = function () {
    // 先查询能否导出数据
    // var ajax = new $ax(Feng.ctxPath + "/consume/reimburse/can/export", function (data) {
    //     if (data.code == 500) {
    //         Feng.info(data.message);
    //         flag = false;
    //     }
    // }, function (data) {
    //     Feng.error("查询失败!" + data.message + "!");
    // });
    // ajax.set("id", ConsumeReimburse.seItem.id);
    // ajax.start();
    // var applyType = $("#applyType").val();
    var beginTime = $("#beginTime").val();
    var endTime = $("#endTime").val();
    if (beginTime == '') {
        beginTime = "notime";
        endTime = "notime"
    }
    window.location.href = Feng.ctxPath + "/fund/amount/export/" + beginTime + "/" + endTime;
}

/**
 * 查询表单提交参数对象
 * @returns {{}}
 */
FundAmount.formParams = function() {
    var queryData = {};

    queryData['applyType'] = $("#applyType").val();
    queryData['beginTime'] = $("#beginTime").val();
    queryData['endTime'] = $("#endTime").val();

    return queryData;
}

/**
 * 查看申请详情
 */
FundAmount.detail = function (id, type) {
    window.location.href = Feng.ctxPath + "/fund/budget/apply/toDetail/" + id + "/" + type;
};

$(function () {
    var defaultColunms = FundAmount.initColumn();
    var table = new BSTable(FundAmount.id, "/fund/amount/list", defaultColunms);
    table.setPaginationType("server");
    table.setQueryParams(FundAmount.formParams());
    table.init();
    FundAmount.table = table;
});
