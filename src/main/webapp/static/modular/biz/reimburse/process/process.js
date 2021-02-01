/**
 * 报销管理管理初始化
 */
var ReimburseProcess = {
    id: "ProcessTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
ReimburseProcess.initColumn = function () {
    return [
        // {field: 'selectItem', radio: true},
        {title: 'id', field: 'id', visible: false, align: 'center', valign: 'middle'},
        {title: 'processId', field: 'processId', visible: false, align: 'center', valign: 'middle'},
        {title: 'type', field: 'type', visible: false, align: 'center', valign: 'middle'},
        {title: '报销地点', field: 'locationame', visible: true, align: 'center', valign: 'middle'},
        {title: '报销部门', field: 'deptname', visible: true, align: 'center', valign: 'middle'},
        {title: '报销类型', field: 'typename', visible: true, align: 'center', valign: 'middle'},
        {title: '项目名称', field: 'projectname', visible: true, align: 'center', valign: 'middle'},
        {title: '报销人', field: 'taskUserName', visible: true, align: 'center', valign: 'middle'},
        {title: '报销金额(元)', field: 'totalFee', visible: true, align: 'center', valign: 'middle'},
        {title: '报销时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'},
        {
            title: '操作', visible: true, align: 'center', valign: 'middle', formatter: function (value, row, index) {
                return '<button type="button" class="btn btn-primary" onclick="ReimburseProcess.detail(\'' + row.id + '\', \'' + row.type + '\')" id=""><i class="fa fa-table"></i>&nbsp;详情</button>';
            }
        }
    ];
};

/**
 * 查看申请详情
 */
ReimburseProcess.detail = function (taskId, type) {
    window.location.href = Feng.ctxPath + "/reimburse/process/approve/detail/" +  taskId + '/' + type;
};

/**
 * 查询报销管理列表
 */
ReimburseProcess.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    Process.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = ReimburseProcess.initColumn();
    var table = new BSTable(ReimburseProcess.id, "/reimburse/process/approve/list", defaultColunms);
    table.setPaginationType("client");
    ReimburseProcess.table = table.init();

    // 根据条件隐藏列
    var projectname = $("#projectname").val();
    if (projectname == '' || projectname == undefined) {
        $("#ProcessTable").bootstrapTable('hideColumn', 'projectname');
    }

});
