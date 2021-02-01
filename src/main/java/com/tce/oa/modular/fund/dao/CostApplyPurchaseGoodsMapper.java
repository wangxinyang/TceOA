package com.tce.oa.modular.fund.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tce.oa.modular.fund.model.CostApplyPurchaseGoods;

import java.util.List;

/**
 * <p>
 * 采购申请商品详情表 Mapper 接口
 * </p>
 *
 * @author wangxy123
 * @since 2018-12-17
 */
public interface CostApplyPurchaseGoodsMapper extends BaseMapper<CostApplyPurchaseGoods> {

    List<CostApplyPurchaseGoods> listByPurId(Integer id);
}
