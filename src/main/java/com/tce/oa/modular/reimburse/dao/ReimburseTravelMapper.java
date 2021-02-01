package com.tce.oa.modular.reimburse.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.tce.oa.modular.reimburse.model.ReimburseTravel;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 差旅费用报销 Mapper 接口
 * </p>
 *
 * @author wangxy123
 * @since 2018-11-29
 */
public interface ReimburseTravelMapper extends BaseMapper<ReimburseTravel> {

    /**
     * 获取报销数据
     */
    List<Map<String, Object>> getReimburseTravels(@Param("page") Page<ReimburseTravel> page,
                                                  @Param("beginTime") String beginTime,
                                                  @Param("endTime") String endTime,
                                                  @Param("deptid") Integer deptid,
                                                  @Param("state") Integer state,
                                                  @Param("ids") List<Integer> ids);

    /**
     * 获取报销数据
     */
    List<Map<String, Object>> getReimburseTravels(@Param("beginTime") String beginTime,
                                                  @Param("endTime") String endTime,
                                                  @Param("deptid") Integer deptid,
                                                  @Param("state") Integer state,
                                                  @Param("ids") List<Integer> ids);

}
