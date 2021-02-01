package com.tce.oa.modular.fund.service;

import com.baomidou.mybatisplus.service.IService;
import com.tce.oa.modular.fund.model.CostApplyFeeDetail;

import java.util.List;

/**
 * <p>
 * 资金申请费用明细表 服务类
 * </p>
 *
 * @author wangxy123
 * @since 2018-12-17
 */
public interface ICostApplyFeeDetailService extends IService<CostApplyFeeDetail> {

    List<CostApplyFeeDetail> listByFid(Integer id);
}
