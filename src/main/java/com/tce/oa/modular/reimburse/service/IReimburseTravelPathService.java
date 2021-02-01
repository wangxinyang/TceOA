package com.tce.oa.modular.reimburse.service;

import com.baomidou.mybatisplus.service.IService;
import com.tce.oa.modular.reimburse.model.ReimburseTravelPath;

import java.util.List;

/**
 * <p>
 * 差旅费用报销起止路径表 服务类
 * </p>
 *
 * @author wangxy123
 * @since 2018-11-29
 */
public interface IReimburseTravelPathService extends IService<ReimburseTravelPath> {

    List<ReimburseTravelPath> listByAid(Integer aid);
}
