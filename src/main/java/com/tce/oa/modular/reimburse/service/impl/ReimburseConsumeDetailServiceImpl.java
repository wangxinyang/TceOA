package com.tce.oa.modular.reimburse.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tce.oa.modular.reimburse.dao.ReimburseConsumeDetailMapper;
import com.tce.oa.modular.reimburse.model.ReimburseConsumeDetail;
import com.tce.oa.modular.reimburse.service.IReimburseConsumeDetailService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 费用报销详情表 服务实现类
 * </p>
 *
 * @author wangxy123
 * @since 2018-11-29
 */
@Service
public class ReimburseConsumeDetailServiceImpl extends ServiceImpl<ReimburseConsumeDetailMapper, ReimburseConsumeDetail> implements IReimburseConsumeDetailService {

    @Override
    public List<ReimburseConsumeDetail> listByRcId(Integer id) {
        return this.baseMapper.listByRcId(id);
    }
}
