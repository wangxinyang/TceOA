package com.tce.oa.modular.fund.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tce.oa.modular.fund.model.CostApplyFeeDetail;

import java.util.List;

/**
 * <p>
 * 资金申请费用明细表 Mapper 接口
 * </p>
 *
 * @author wangxy123
 * @since 2018-12-17
 */
public interface CostApplyFeeDetailMapper extends BaseMapper<CostApplyFeeDetail> {

    List<CostApplyFeeDetail> listByFid(Integer id);
}
