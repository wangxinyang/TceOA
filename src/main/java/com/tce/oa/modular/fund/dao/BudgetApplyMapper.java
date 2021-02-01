package com.tce.oa.modular.fund.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.tce.oa.modular.fund.model.BudgetApply;
import com.tce.oa.modular.fund.model.CostApply;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 预算申请表 Mapper 接口
 * </p>
 *
 * @author wangxy123
 * @since 2018-11-14
 */
public interface BudgetApplyMapper extends BaseMapper<BudgetApply> {

    /**
     *@Description 查询时间内数据
     *@Date 18:54 2018/11/15
     *@Param [now]
     *@return com.tce.oa.modular.fund.model.FundApply
     **/
    BudgetApply queryCanAdd(@Param("userid")Integer userid, @Param("deptid")Integer deptid);
    
    /**
     *@Description 根据processId查询数据
     *@Date 15:25 2018/11/20
     *@Param [processId]
     *@return com.tce.oa.modular.fund.model.FundApply
     **/
    BudgetApply queryByProcessId(String processId);


    /**
     *@Description 根据条件查询数据
     *@Date 15:25 2018/11/20
     *@Param [processId]
     *@return com.tce.oa.modular.fund.model.FundApply
     **/
    List<Map<String, Object>> queryApplys(@Param("page") Page<BudgetApply> page, @Param("applyType") Integer applyType,
                                          @Param("beginTime") String beginTime, @Param("endTime") String endTime);

    /**
     *@Description 根据条件查询数据
     *@Date 15:25 2018/11/20
     *@Param [processId]
     *@return com.tce.oa.modular.fund.model.FundApply
     **/
    List<Integer> queryApplysCqForExport(@Param("beginTime") String beginTime, @Param("endTime") String endTime);

    /**
     *@Description 根据条件查询数据
     *@Date 15:25 2018/11/20
     *@Param [processId]
     *@return com.tce.oa.modular.fund.model.FundApply
     **/
    List<Integer> queryApplysBjForExport(@Param("beginTime") String beginTime, @Param("endTime") String endTime);
}
