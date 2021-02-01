/**
 * 报销管理管理初始化
 */
var CostApplyProcess = {
    id: "ProcessTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
CostApplyProcess.initColumn = function () {
    return [
        // {field: 'selectItem', radio: true},
        {title: 'id', field: 'id', visible: false, align: 'center', valign: 'middle'},
        {title: 'processId', field: 'processId', visible: false, align: 'center', valign: 'middle'},
        {title: '申请地点', field: 'location', visible: true, align: 'center', valign: 'middle'},
        {title: '申请部门', field: 'deptname', visible: true, align: 'center', valign: 'middle'},
        {title: '申请单号', field: 'reqno', visible: true, align: 'center', valign: 'middle'},
        {title: '申请类型', field: 'typename', visible: true, align: 'center', valign: 'middle'},
        {title: '申请总价(元)', field: 'totalFee', visible: true, align: 'center', valign: 'middle'},
        {title: '申请人', field: 'taskUserName', visible: true, align: 'center', valign: 'middle'},
        {title: '申请时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'},
        {
            title: '操作', visible: true, align: 'center', valign: 'middle', formatter: function (value, row, index) {
                return '<button type="button" class="btn btn-primary" onclick="CostApplyProcess.detail(\'' + row.id + '\', \'' + row.type + '\')" id=""><i class="fa fa-table"></i>&nbsp;详情</button>';
            }
        }
    ];
};

/**
 * 查看申请详情
 */
CostApplyProcess.detail = function (taskId, type) {
    window.location.href = Feng.ctxPath + "/cost/apply/approve/purchase/detail/" +  taskId + '/' + type;
};


$(function () {
    var defaultColunms = CostApplyProcess.initColumn();
    var table = new BSTable(CostApplyProcess.id, "/cost/apply/approve/list", defaultColunms);
    table.setPaginationType("client");
    CostApplyProcess.table = table.init();
});
