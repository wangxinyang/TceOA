package com.tce.oa.modular.reimburse.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tce.oa.modular.reimburse.model.ReimburseTravelPath;

import java.util.List;

/**
 * <p>
 * 差旅费用报销起止路径表 Mapper 接口
 * </p>
 *
 * @author wangxy123
 * @since 2018-11-29
 */
public interface ReimburseTravelPathMapper extends BaseMapper<ReimburseTravelPath> {

    List<ReimburseTravelPath> listByAid(Integer aid);
}
