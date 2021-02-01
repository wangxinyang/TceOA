package com.tce.oa.modular.company.dao;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tce.oa.core.common.node.ZTreeNode;
import com.tce.oa.modular.company.model.Subject;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 资金科目 Mapper 接口
 * </p>
 *
 * @author wangxy123
 * @since 2018-11-13
 */
public interface SubjectMapper extends BaseMapper<Subject> {

    /**
     * 获取ztree的节点列表
     */
    List<ZTreeNode> tree();

    /**
     * 获取所有科目列表
     */
    List<Map<String, Object>> list(@Param("condition") String condition);

    List<Integer> queryIdByExport();
}
