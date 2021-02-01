package com.tce.oa.modular.fund.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tce.oa.modular.fund.model.BudgetApplyDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 业务费用预算详情表 Mapper 接口
 * </p>
 *
 * @author wangxy123
 * @since 2018-11-14
 */
public interface BudgetApplyDetailMapper extends BaseMapper<BudgetApplyDetail> {

    /**
     *@Description 根据aid获取业务费用预算详情
     *@Date 17:40 2018/11/15
     *@Param [aid]
     *@return java.util.List<com.tce.oa.modular.fund.model.FundApplyDetailSales>
     **/
    List<BudgetApplyDetail> listByAid(Integer aid);

    List<BudgetApplyDetail> queryDetailByIds(@Param("idList") List<Integer> idList);
}
