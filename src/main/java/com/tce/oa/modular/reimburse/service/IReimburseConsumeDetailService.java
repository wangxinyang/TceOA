package com.tce.oa.modular.reimburse.service;

import com.baomidou.mybatisplus.service.IService;
import com.tce.oa.modular.reimburse.model.ReimburseConsumeDetail;

import java.util.List;

/**
 * <p>
 * 费用报销详情表 服务类
 * </p>
 *
 * @author wangxy123
 * @since 2018-11-29
 */
public interface IReimburseConsumeDetailService extends IService<ReimburseConsumeDetail> {

    List<ReimburseConsumeDetail> listByRcId(Integer id);
}
