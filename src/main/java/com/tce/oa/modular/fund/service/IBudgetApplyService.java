package com.tce.oa.modular.fund.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.tce.oa.modular.fund.model.BudgetApply;
import com.tce.oa.modular.fund.transfer.BudgetApplyDto;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 预算申请表 服务类
 * </p>
 *
 * @author wangxy
 * @since 2018-11-14
 */
public interface IBudgetApplyService extends IService<BudgetApply> {

    /**
     *@Description 查询是否能发起申请
     *@Date 18:54 2018/11/15
     *@Param [now]
     *@return com.tce.oa.modular.fund.model.FundApply
     **/
    BudgetApply queryCanAdd();

    /**
     * 保存业务预算数据
     * @param buildDataMap
     */
    void saveFundApply(Integer type, Map<String, Object> buildDataMap);
    void addFundApply(Integer type, Map<String, Object> buildDataMap);
    void updateFundApply(Integer type, Map<String, Object> buildDataMap);

    /**
     * 绘画当前流程图
     */
    void printProcessImage(Integer expenseId) throws IOException;

    /**
     * 获取审批列表
     */
    List<Map<String, Object>> getProcessList();

    /**
     * 通过审批
     */
    void pass(BudgetApplyDto fundApplyDto);

    /**
     * 驳回审批
     */
    void unPass(BudgetApplyDto fundApplyDto);

    void deleteData(Integer fundApplyId, Integer type);

    /**
     * 根据条件查询申请列表
     */
    List<Map<String, Object>> selectApplys(Page<BudgetApply> page, Integer applyType, String beginTime, String endTime);

    void updateFundApplyDetail(Integer type, Map<String, Object> buildDataMap);

    /**
     *@Description 根据条件查询数据
     *@Date 15:25 2018/11/20
     *@Param [processId]
     *@return com.tce.oa.modular.fund.model.FundApply
     **/
    List<Integer> queryApplysCqForExport(String beginTime, String endTime);

    /**
     *@Description 根据条件查询数据
     *@Date 15:25 2018/11/20
     *@Param [processId]
     *@return com.tce.oa.modular.fund.model.FundApply
     **/
    List<Integer> queryApplysBjForExport(String beginTime, String endTime);
}
