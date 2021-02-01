/**
 * 初始化资金科目详情对话框
 */
var SubjectInfoDlg = {
    subjectInfoData : {},
    zTreeInstance : null,
    validateFields: {
        subject: {
            validators: {
                notEmpty: {
                    message: '科目不能为空'
                }
            }
        },
        pName: {
            validators: {
                notEmpty: {
                    message: '上级不能为空'
                }
            }
        }
    }
};

/**
 * 清除数据
 */
SubjectInfoDlg.clearData = function() {
    this.subjectInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
SubjectInfoDlg.set = function(key, val) {
    this.subjectInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
SubjectInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
SubjectInfoDlg.close = function() {
    parent.layer.close(window.parent.Subject.layerIndex);
}

/**
 * 收集数据
 */
SubjectInfoDlg.collectData = function() {
    this
    .set('id').set('num').set('subject').set('note').set('pid');
}

/**
 * 提交添加
 */
SubjectInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    if (!this.validate()) {
        return;
    }

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/subject/add", function(data){
        Feng.success("添加成功!");
        window.parent.Subject.table.refresh();
        SubjectInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.subjectInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
SubjectInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    if (!this.validate()) {
        return;
    }

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/subject/update", function(data){
        Feng.success("修改成功!");
        window.parent.Subject.table.refresh();
        SubjectInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.subjectInfoData);
    ajax.start();
}

/**
 * 验证数据是否为空
 */
SubjectInfoDlg.validate = function () {
    $('#subjectInfoForm').data("bootstrapValidator").resetForm();
    $('#subjectInfoForm').bootstrapValidator('validate');
    return $("#subjectInfoForm").data('bootstrapValidator').isValid();
}


/**
 * 点击科目ztree列表的选项时
 *
 * @param e
 * @param treeId
 * @param treeNode
 * @returns
 */
SubjectInfoDlg.onClickSubject = function(e, treeId, treeNode) {
    $("#pName").attr("value", SubjectInfoDlg.zTreeInstance.getSelectedVal());
    $("#pid").attr("value", treeNode.id);
}

/**
 * 显示科目选择的树
 *
 * @returns
 */
SubjectInfoDlg.showSubjectSelectTree = function() {
    var pName = $("#pName");
    var pNameOffset = $("#pName").offset();
    $("#menuContent").css({
        left : pNameOffset.left + "px",
        top : pNameOffset.top + pName.outerHeight() + "px"
    }).slideDown("fast");

    $("body").bind("mousedown", onBodyDown);
}

/**
 * 隐藏部门选择的树
 */
SubjectInfoDlg.hideSubjectSelectTree = function() {
    $("#menuContent").fadeOut("fast");
    $("body").unbind("mousedown", onBodyDown);// mousedown当鼠标按下就可以触发，不用弹起
}


function onBodyDown(event) {
    if (!(event.target.id == "menuBtn" || event.target.id == "menuContent" || $(
            event.target).parents("#menuContent").length > 0)) {
        SubjectInfoDlg.hideSubjectSelectTree();
    }
}


$(function() {
    Feng.initValidator("subjectInfoForm", SubjectInfoDlg.validateFields);

    var ztree = new $ZTree("parentSubjectTree", "/subject/tree");
    ztree.bindOnClick(SubjectInfoDlg.onClickSubject);
    ztree.init();
    SubjectInfoDlg.zTreeInstance = ztree;
});
