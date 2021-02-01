/**
 * 项目初始化
 */
var Project = {
    id: "ProjectTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Project.initColumn = function () {
    return [
            {field: 'selectItem', radio: true},
            {title: 'id', field: 'id', visible: false, align: 'center', valign: 'middle'},
            {title: '项目编号', field: 'code', visible: true, align: 'center', valign: 'middle'},
            {title: '项目名称', field: 'name', visible: true, align: 'center', valign: 'middle', width:'600px'},
            {title: '创建时间', field: 'createtime', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
Project.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        Project.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加项目
 */
Project.openAddProject = function () {
    var index = layer.open({
        type: 2,
        title: '添加项目',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/project/manager/toAdd'
    });
    this.layerIndex = index;
};

/**
 * 打开修改项目详情
 */
Project.openEditProject = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '资金科目详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/project/manager/toEdit/' + Project.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除项目
 */
Project.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/project/manager/delete", function (data) {
            Feng.success("删除成功!");
            Project.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("id",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询项目
 */
Project.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    Project.table.refresh({query: queryData});
};

Project.getUploadFile = function () {
    window.location.href = Feng.ctxPath + "/project/manager/file/";
};

$(function () {
    var defaultColunms = Project.initColumn();
    var table = new BSTable(Project.id, "/project/manager/list", defaultColunms);
    table.setPaginationType("server");
    Project.table = table.init();

    var $list = $("#thelist");
    var $btn = $("#ctlBtn");
    var state = 'pending'; // 上传文件初始化
    var uploader = WebUploader.create({
        swf : 'webuploader/Uploader.swf',
        server : Feng.ctxPath + "/project/manager/upload",
        pick : '#picker',
        resize : false
    });
    uploader.on('fileQueued', function(file) {
        $list.append('<div id="' + file.id + '" class="item">'
            + '<h4 class="info">' + file.name + '</h4>'
            + '<p class="state">等待上传...</p>' + '</div>');
    });

    uploader.on('uploadProgress', function(file, percentage) {
            var $li = $('#' + file.id), $percent = $li
                .find('.progress .progress-bar');

            // 避免重复创建
            if (!$percent.length) {
                $percent = $(
                    '<div class="progress progress-striped active">'
                    + '<div class="progress-bar" role="progressbar" style="width: 0%">'
                    + '</div>' + '</div>')
                    .appendTo($li).find('.progress-bar');
            }

            $li.find('p.state').text('上传中');
            $percent.css('width', percentage * 100 + '%');
    });

    uploader.on('uploadSuccess', function(file) {
        $('#' + file.id).find('p.state').text('已上传');
        Project.table.refresh();
    });

    uploader.on('uploadError', function(file) {
        $('#' + file.id).find('p.state').text('上传出错');
    });

    uploader.on('uploadComplete', function(file) {
        $('#' + file.id).find('.progress').fadeOut();
    });

    $btn.on('click', function() {
        if (state === 'uploading') {
            uploader.stop();
        } else {
            uploader.upload();
        }
    });
});
