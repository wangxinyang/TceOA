/**
 * 报销管理管理初始化
 */
var FundProcess = {
    id: "ProcessTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
FundProcess.initColumn = function () {
    return [
        // {field: 'selectItem', radio: true},
        {title: 'id', field: 'id', visible: false, align: 'center', valign: 'middle'},
        {title: 'processId', field: 'processId', visible: false, align: 'center', valign: 'middle'},
        {title: '申请地点', field: 'location', visible: true, align: 'center', valign: 'middle'},
        {title: '申请部门', field: 'deptname', visible: true, align: 'center', valign: 'middle'},
        {title: '申请类型', field: 'typename', visible: true, align: 'center', valign: 'middle'},
        {title: '申请金额(元)', field: 'money', visible: true, align: 'center', valign: 'middle'},
        {title: '申请时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'},
        {title: '申请人', field: 'taskUserName', visible: true, align: 'center', valign: 'middle'},
        {
            title: '操作', visible: true, align: 'center', valign: 'middle', formatter: function (value, row, index) {
                return '<button type="button" class="btn btn-primary" onclick="FundProcess.detail(\'' + row.id + '\', \'' + row.selfFlag + '\')" id=""><i class="fa fa-table"></i>&nbsp;详情</button>';
            }
        }
    ];
};

/**
 * 查看申请详情
 */
FundProcess.detail = function (taskId, flag) {
    window.location.href = Feng.ctxPath + "/fund/budget/process/detail/" + taskId + '/' + flag
};


/**
 * 查询报销管理列表
 */
FundProcess.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    Process.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = FundProcess.initColumn();
    var table = new BSTable(FundProcess.id, "/fund/budget/process/list", defaultColunms);
    table.setPaginationType("client");
    FundProcess.table = table.init();
});
