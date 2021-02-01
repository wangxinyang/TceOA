/**
 * 报销管理初始化
 */
var Reimburse = {
    id: "reimburseTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    deptZtree: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Reimburse.initColumn = function () {
    return [
        {title: '', field: 'id', visible: false, align: 'center', valign: 'middle'},
        {title: '报销类型', field: 'typename', visible: true, align: 'center', valign: 'middle'},
        {title: '报销单号', field: 'reimburseno', visible: true, align: 'center', valign: 'middle'},
        {title: '报销人', field: 'user', visible: true, align: 'center', valign: 'middle'},
        {title: '报销地点', field: 'locationame', visible: true, align: 'center', valign: 'middle'},
        {title: '报销部门', field: 'deptname', visible: true, align: 'center', valign: 'middle'},
        {title: '报销金额', field: 'totalfee', visible: true, align: 'center', valign: 'middle'},
        {title: '状态', field: 'statename', visible: true, align: 'center', valign: 'middle'},
        {title: '报销时间', field: 'createtime', visible: true, align: 'center', valign: 'middle'},
        {
            title: '操作', visible: true, align: 'center', valign: 'middle', formatter: function (value, row, index) {
                return '<button type="button" class="btn btn-primary" onclick="Reimburse.detail(\'' + row.id + '\', \'' + row.type + '\')" id=""><i class="fa fa-table"></i>&nbsp;详情</button>';
            }
        }
    ];
};

/**
 * 查询表单提交参数对象
 * @returns {{}}
 */
Reimburse.formParams = function() {
    var queryData = {};

    queryData['reimburseType'] = $("#reimburseType").val();
    queryData['beginTime'] = $("#beginTime").val();
    queryData['endTime'] = $("#endTime").val();
    queryData['deptid'] = $("#deptid").val();
    queryData['reimburseState'] = $("#reimburseState").val();
    queryData['reimbursePerson'] = $("#reimbursePerson").val();
    return queryData;
}


/**
 * 查询日志列表
 */
Reimburse.search = function () {

    Reimburse.table.refresh({query: Reimburse.formParams()});
};

Reimburse.resetSearch = function () {
    $("#beginTime").val("");
    $("#endTime").val("");
    $("#reimburseType").val("0");
    $("#deptid").val("");
    $("#reimburseState").val("");
    $("#reimbursePerson").val("");
    Reimburse.search();
}

/**
 * 导出费用报销
 */
Reimburse.export = function () {
    var reimburseType = $("#reimburseType").val();
    var deptid = $("#deptid").val();
    var beginTime = $("#beginTime").val();
    var endTime = $("#endTime").val();
    if (beginTime == '') {
        beginTime = "notime";
        endTime = "notime"
    }
    if (reimburseType == '') {
        reimburseType = 0
    }
    if (deptid == '') {
        deptid = 0;
    }
    window.location.href = Feng.ctxPath + "/reimburse/query/export/" + beginTime + "/" + endTime + "/" + deptid + "/" + reimburseType;
}

/**
 * 显示部门选择的树
 *
 * @returns
 */
Reimburse.showDeptSelectTree = function () {
    Feng.showInputTree("departs", "menuContent", 480, 32);
};

/**
 * 点击部门input框时
 *
 * @param e
 * @param treeId
 * @param treeNode
 * @returns
 */
Reimburse.onClickDept = function (e, treeId, treeNode) {
    var selVal = Reimburse.deptZtree.getSelectedVal();
    $("#departs").attr("value", selVal);
    $("#deptid").attr("value", treeNode.id);
};

/**
 * 查看申请详情
 */
Reimburse.detail = function (id, type) {
    if (type == '5') {
        // 差旅费报销
        window.location.href = Feng.ctxPath + "/travel/reimburse/toDetail/" +  id;
    } else if (type == '6') {
        // 费用报销
        window.location.href = Feng.ctxPath + "/consume/reimburse/toDetail/" +  id;
    }
};


$(function () {
    var defaultColunms = Reimburse.initColumn();
    var table = new BSTable(Reimburse.id, "/reimburse/query/list", defaultColunms);
    table.setPaginationType("client");
    table.setQueryParams(Reimburse.formParams());
    Reimburse.table = table.init();

    var ztree = new $ZTree("treeDemo", "/dept/tree");
    ztree.bindOnClick(Reimburse.onClickDept);
    ztree.init();
    Reimburse.deptZtree = ztree;
});
