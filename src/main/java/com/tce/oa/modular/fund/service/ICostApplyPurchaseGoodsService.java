package com.tce.oa.modular.fund.service;

import com.baomidou.mybatisplus.service.IService;
import com.tce.oa.modular.fund.model.CostApplyPurchaseGoods;

import java.util.List;

/**
 * <p>
 * 采购申请商品详情表 服务类
 * </p>
 *
 * @author wangxy123
 * @since 2018-12-17
 */
public interface ICostApplyPurchaseGoodsService extends IService<CostApplyPurchaseGoods> {

    List<CostApplyPurchaseGoods> listByAid(Integer id);
}
