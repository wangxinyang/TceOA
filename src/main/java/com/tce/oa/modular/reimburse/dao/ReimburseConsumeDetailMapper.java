package com.tce.oa.modular.reimburse.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tce.oa.modular.reimburse.model.ReimburseConsumeDetail;

import java.util.List;

/**
 * <p>
 * 费用报销详情表 Mapper 接口
 * </p>
 *
 * @author wangxy123
 * @since 2018-11-29
 */
public interface ReimburseConsumeDetailMapper extends BaseMapper<ReimburseConsumeDetail> {

    List<ReimburseConsumeDetail> listByRcId(Integer id);
}
