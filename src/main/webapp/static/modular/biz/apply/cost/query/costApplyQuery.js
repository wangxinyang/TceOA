/**
 * 资金预算管理初始化
 */
var CostApplyQuery = {
    id: "CostApplyQueryTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
CostApplyQuery.initColumn = function () {
    return [
            {field: 'selectItem', checkbox: true},
            {title: 'id', field: 'id', visible: false, align: 'center', valign: 'middle'},
            {title: '申请地点', field: 'location', visible: true, align: 'center', valign: 'middle', width:'100px'},
            {title: '申请部门', field: 'deptname', visible: true, align: 'center', valign: 'middle', width:'100px'},
            {title: '申请单号', field: 'reqno', visible: true, align: 'center', valign: 'middle', width:'150px'},
            {title: '申请人', field: 'user', visible: true, align: 'center', valign: 'middle'},
            {title: '申请类型', field: 'typename', visible: true, align: 'center', valign: 'middle', width:'150px'},
            {title: '金额(元)', field: 'totalfee', visible: true, align: 'center', valign: 'middle'},
            {title: '申请状态', field: 'statemean', visible: true, align: 'center', valign: 'middle'},
            {title: '申请时间', field: 'createtime', visible: true, align: 'center', valign: 'middle'},
            {title: '已付款', field: 'payname', visible: true, align: 'center', valign: 'middle'},
            {
                title: '操作', visible: true, align: 'center', valign: 'middle', formatter: function (value, row, index) {
                    return '<button type="button" class="btn btn-primary" onclick="CostApplyQuery.detail(\'' + row.id + '\', \'' + row.type + '\')" id=""><i class="fa fa-table"></i>&nbsp;详情</button>';
                }
            }
    ];
};


CostApplyQuery.resetSearch = function () {
    $("#costType").val("0");
    $("#costState").val("");
    $("#costPay").val("");
    $("#costPerson").val("");
    $("#beginTime").val("");
    $("#endTime").val("");

    CostApplyQuery.search();
}

/**
 * 查询表单提交参数对象
 * @returns {{}}
 */
CostApplyQuery.formParams = function() {
    var queryData = {};

    queryData['costType'] = $("#costType").val();
    queryData['costState'] = $("#costState").val();
    queryData['costPay'] = $("#costPay").val();
    queryData['costPerson'] = $("#costPerson").val();
    queryData['beginTime'] = $("#beginTime").val();
    queryData['endTime'] = $("#endTime").val();

    return queryData;
}

CostApplyQuery.search = function () {

    CostApplyQuery.table.refresh({query: CostApplyQuery.formParams()});
}

CostApplyQuery.pay = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请至少选中表格中的一条记录！");
        return false;
    }else{
        var ids = new Array();
        for(var i=0; i<selected.length; i++){
            ids[i] = selected[i].id;
        }
        var ajax = new $ax(Feng.ctxPath + "/cost/apply/query/pay", function (data) {
            Feng.success("付款标记成功!");
            CostApplyQuery.table.refresh();
        }, function (data) {
            Feng.error("已付款更新失败!" + data.message + "!");
        });
        ajax.set("ids", ids);
        ajax.start();
    }
}

/**
 * 查看申请详情
 */
CostApplyQuery.detail = function (id, type) {
    if (type == '7') {
        // 采购申请
        window.location.href = Feng.ctxPath + "/cost/apply/purchase/to_detail/" +  id;
    } else if (type == '8') {
        // 资金费用
        window.location.href = Feng.ctxPath + "/cost/apply/fee/to_detail/" +  id;
    } else {
        window.location.href = Feng.ctxPath + "/cost/apply/payment/to_detail/" +  id;
    }
};

$(function () {
    var defaultColunms = CostApplyQuery.initColumn();
    var table = new BSTable(CostApplyQuery.id, "/cost/apply/query/list", defaultColunms);
    table.setPaginationType("server");
    table.setQueryParams(CostApplyQuery.formParams());
    CostApplyQuery.table = table.init();
});
