package com.tce.oa.modular.company.service;


import com.baomidou.mybatisplus.service.IService;
import com.tce.oa.core.common.node.ZTreeNode;
import com.tce.oa.modular.company.model.Subject;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 资金科目 服务类
 * </p>
 *
 * @author wangxy123
 * @since 2018-11-13
 */
public interface ISubjectService extends IService<Subject> {

    /**
     * 获取ztree的节点列表
     */
    List<ZTreeNode> tree();

    /**
     * 获取所有科目列表
     */
    List<Map<String, Object>> list(String condition);

    List<Integer> queryIdByExport();
}
