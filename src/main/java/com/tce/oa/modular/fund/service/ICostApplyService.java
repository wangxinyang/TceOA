package com.tce.oa.modular.fund.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.tce.oa.modular.fund.model.CostApply;
import com.tce.oa.modular.fund.model.CostApplyFeeDetail;
import com.tce.oa.modular.fund.model.CostApplyPurchaseGoods;
import com.tce.oa.modular.fund.transfer.CostApplyDto;
import com.tce.oa.modular.fund.transfer.CostApplyPurchaseGoodsDto;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 采购申请表 服务类
 * </p>
 *
 * @author wangxy123
 * @since 2018-12-17
 */
public interface ICostApplyService extends IService<CostApply> {

    void save(Map<String, Object> modelMap, Integer type);

    void add(Map<String, Object> modelMap, Integer type, String assignee);

    void update(CostApply costApplyPurchase,
                List<CostApplyPurchaseGoods> purchaseGoodsList);

    void updateFee(CostApply costApplyPurchase,
                List<CostApplyFeeDetail> feeDetailList);

    void updatePayment(CostApply costApplyPurchase);

    void delete(Integer id);

    void deleteFee(Integer id);

    void deleteDetailById(Integer id);

    void deleteFeeDetailById(Integer id);

    void deleteInquiryList(List<CostApplyPurchaseGoodsDto> purchaseGoodsInquiryList);

    void printProcessImage(Integer id) throws IOException;

    List<Map<String, Object>> getProcessList();

    void pass(CostApplyDto costApplyPurchaseDto);

    void unPass(CostApplyDto costApplyPurchaseDto);

    /**
     * 根据条件查询申请列表
     */
    List<Map<String, Object>> selectCostApplys(Page<CostApply> page, Integer costType, Integer costState,
                                               Integer costPay, List<Integer> ids, String beginTime, String endTime);
}
