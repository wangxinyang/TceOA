package com.tce.oa.modular.reimburse.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tce.oa.modular.reimburse.dao.ReimburseTravelPathMapper;
import com.tce.oa.modular.reimburse.model.ReimburseTravelPath;
import com.tce.oa.modular.reimburse.service.IReimburseTravelPathService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 差旅费用报销起止路径表 服务实现类
 * </p>
 *
 * @author wangxy123
 * @since 2018-11-29
 */
@Service
public class ReimburseTravelPathServiceImpl extends ServiceImpl<ReimburseTravelPathMapper, ReimburseTravelPath> implements IReimburseTravelPathService {

    @Resource
    private ReimburseTravelPathMapper reimburseTravelPathMapper;

    @Override
    public List<ReimburseTravelPath> listByAid(Integer aid) {
        return reimburseTravelPathMapper.listByAid(aid);
    }
}
