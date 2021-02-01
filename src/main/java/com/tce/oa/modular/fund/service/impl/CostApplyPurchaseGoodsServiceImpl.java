package com.tce.oa.modular.fund.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tce.oa.modular.fund.dao.CostApplyPurchaseGoodsMapper;
import com.tce.oa.modular.fund.model.CostApplyPurchaseGoods;
import com.tce.oa.modular.fund.service.ICostApplyPurchaseGoodsService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 采购申请商品详情表 服务实现类
 * </p>
 *
 * @author wangxy123
 * @since 2018-12-17
 */
@Service
public class CostApplyPurchaseGoodsServiceImpl extends ServiceImpl<CostApplyPurchaseGoodsMapper, CostApplyPurchaseGoods>
        implements ICostApplyPurchaseGoodsService {

    @Override
    public List<CostApplyPurchaseGoods> listByAid(Integer id) {
        return this.baseMapper.listByPurId(id);
    }
}
