package com.tce.oa.modular.fund.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tce.oa.modular.fund.dao.CostApplyFeeDetailMapper;
import com.tce.oa.modular.fund.model.CostApplyFeeDetail;
import com.tce.oa.modular.fund.service.ICostApplyFeeDetailService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 资金申请费用明细表 服务实现类
 * </p>
 *
 * @author wangxy123
 * @since 2018-12-17
 */
@Service
public class CostApplyFeeDetailServiceImpl extends ServiceImpl<CostApplyFeeDetailMapper, CostApplyFeeDetail> implements ICostApplyFeeDetailService {

    @Override
    public List<CostApplyFeeDetail> listByFid(Integer id) {
        return this.baseMapper.listByFid(id);
    }
}
