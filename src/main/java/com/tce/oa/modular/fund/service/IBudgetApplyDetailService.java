package com.tce.oa.modular.fund.service;

import com.baomidou.mybatisplus.service.IService;
import com.tce.oa.modular.fund.model.BudgetApplyDetail;

import java.util.List;

/**
 * <p>
 * 业务费用预算详情表 服务类
 * </p>
 *
 * @author wangxy123
 * @since 2018-11-14
 */
public interface IBudgetApplyDetailService extends IService<BudgetApplyDetail> {

    /**
     *@Description 根据aid获取业务费用预算详情
     *@Date 17:40 2018/11/15
     *@Param [aid]
     *@return java.util.List<com.tce.oa.modular.fund.model.FundApplyDetailSales>
     **/
    List<BudgetApplyDetail> listByAid(Integer aid);

    List<BudgetApplyDetail> queryDetailByIds(List<Integer> idList);
}
