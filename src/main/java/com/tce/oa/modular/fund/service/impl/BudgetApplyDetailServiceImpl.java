package com.tce.oa.modular.fund.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tce.oa.modular.fund.dao.BudgetApplyDetailMapper;
import com.tce.oa.modular.fund.model.BudgetApplyDetail;
import com.tce.oa.modular.fund.service.IBudgetApplyDetailService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 业务费用预算详情表 服务实现类
 * </p>
 *
 * @author wangxy123
 * @since 2018-11-14
 */
@Service
public class BudgetApplyDetailServiceImpl extends ServiceImpl<BudgetApplyDetailMapper, BudgetApplyDetail> implements IBudgetApplyDetailService {

    @Resource
    private BudgetApplyDetailMapper fundApplyDetailMapper;

    @Override
    public List<BudgetApplyDetail> listByAid(Integer aid) {
        return fundApplyDetailMapper.listByAid(aid);
    }

    @Override
    public List<BudgetApplyDetail> queryDetailByIds(List<Integer> idList) {
        return fundApplyDetailMapper.queryDetailByIds(idList);
    }
}
