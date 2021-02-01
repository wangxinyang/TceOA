package com.tce.oa.modular.reimburse.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.tce.oa.modular.reimburse.model.ReimburseConsume;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 费用报销 Mapper 接口
 * </p>
 *
 * @author wangxy123
 * @since 2018-11-29
 */
public interface ReimburseConsumeMapper extends BaseMapper<ReimburseConsume> {

    /**
     * 获取报销数据
     */
    List<Map<String, Object>> getReimburseConsumes(@Param("page") Page<ReimburseConsume> page,
                                                  @Param("beginTime") String beginTime,
                                                  @Param("endTime") String endTime,
                                                  @Param("deptid") Integer deptid,
                                                  @Param("state") Integer state, @Param("ids") List<Integer> ids);

    /**
     * 获取报销数据
     */
    List<Map<String, Object>> getReimburseConsumes(@Param("beginTime") String beginTime,
                                                   @Param("endTime") String endTime,
                                                   @Param("deptid") Integer deptid,
                                                   @Param("state") Integer state,
                                                   @Param("ids") List<Integer> ids);
}
