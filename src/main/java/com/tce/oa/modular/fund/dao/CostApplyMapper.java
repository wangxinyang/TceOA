package com.tce.oa.modular.fund.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.tce.oa.modular.fund.model.CostApply;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 采购申请表 Mapper 接口
 * </p>
 *
 * @author wangxy123
 * @since 2018-12-17
 */
public interface CostApplyMapper extends BaseMapper<CostApply> {

    /**
     *@Description 根据条件查询数据
     *@Date 15:25 2018/11/20
     *@Param [processId]
     *@return com.tce.oa.modular.fund.model.FundApply
     **/
    List<Map<String, Object>> queryCostApply(@Param("page") Page<CostApply> page, @Param("costType") Integer costType,
                                             @Param("costState") Integer costState, @Param("costPay") Integer costPay,
                                             @Param("ids") List<Integer> ids, @Param("beginTime") String beginTime,
                                             @Param("endTime") String endTime);
}
