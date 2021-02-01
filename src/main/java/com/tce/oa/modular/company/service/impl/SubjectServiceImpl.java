package com.tce.oa.modular.company.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tce.oa.core.common.node.ZTreeNode;
import com.tce.oa.modular.company.dao.SubjectMapper;
import com.tce.oa.modular.company.model.Subject;
import com.tce.oa.modular.company.service.ISubjectService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 资金科目 服务实现类
 * </p>
 *
 * @author wangxy123
 * @since 2018-11-13
 */
@Service
public class SubjectServiceImpl extends ServiceImpl<SubjectMapper, Subject> implements ISubjectService {

    @Resource
    private SubjectMapper fundSubjectMapper;

    @Override
    public List<ZTreeNode> tree() {
        return fundSubjectMapper.tree();
    }

    @Override
    public List<Map<String, Object>> list(String condition) {
        return fundSubjectMapper.list(condition);
    }

    @Override
    public List<Integer> queryIdByExport() {
        return fundSubjectMapper.queryIdByExport();
    }
}
