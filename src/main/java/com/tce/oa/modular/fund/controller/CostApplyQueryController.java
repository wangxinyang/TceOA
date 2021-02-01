package com.tce.oa.modular.fund.controller;

import cn.stylefeng.roses.core.base.controller.BaseController;
import com.baomidou.mybatisplus.plugins.Page;
import com.tce.oa.core.common.constant.factory.PageFactory;
import com.tce.oa.core.common.constant.state.CostType;
import com.tce.oa.core.common.constant.state.PaymentType;
import com.tce.oa.core.common.constant.state.ProcessNameType;
import com.tce.oa.core.common.page.PageInfoBT;
import com.tce.oa.modular.fund.model.CostApply;
import com.tce.oa.modular.fund.service.ICostApplyService;
import com.tce.oa.modular.fund.warpper.FundWarpper;
import com.tce.oa.modular.system.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 费用申请控制器
 *
 * @author wxy
 * @Date 2018-12-17 11:13:24
 */
@Controller
@RequestMapping("/cost/apply/query")
public class CostApplyQueryController extends BaseController {

    private String PREFIX = "/biz/apply/cost/query/";

    @Autowired
    private ICostApplyService costApplyService;

    @Autowired
    private IUserService userService;

    /**
     * 跳转到费用申请首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "costApplyQuery.html";
    }

    /**
     * 获取费用申请列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) Integer costType,
                       @RequestParam(required = false) Integer costState,
                       @RequestParam(required = false) Integer costPay,
                       @RequestParam(required = false) String costPerson,
                       @RequestParam(required = false) String beginTime,
                       @RequestParam(required = false) String endTime) {
        // 申请类型
        if (null != costType) {
            if (CostType.ALL.getCode() == costType) {
                costType = null;
            } else if (CostType.PURCHASE.getCode() == costType) {
                costType = ProcessNameType.PURCHASE_APPLY.getCode();
            } else if (CostType.FEE.getCode() == costType) {
                costType = ProcessNameType.FEE_APPLY.getCode();
            } else if (CostType.PAYMENT.getCode() == costType) {
                costType = ProcessNameType.PAYMENT_APPLY.getCode();
            }
        }

        // 查询用户
        List<Integer> ids = null;
        if (!StringUtils.isEmpty(costPerson)) {
            ids = userService.selectUserIds(costPerson);
        }

        Page<CostApply> page = new PageFactory<CostApply>().defaultPage();
        List<Map<String, Object>> applyList = costApplyService.selectCostApplys(page, costType,
                costState, costPay, ids, beginTime, endTime);
        page.setRecords(new FundWarpper(applyList).wrap());
        return new PageInfoBT<>(page);
    }

    /**
     * 是否付款的状态更新
     * @param ids
     * @return
     */
    @RequestMapping("/pay")
    @ResponseBody
    public Object pay(@RequestParam(value = "ids[]") Integer[] ids) {
        List<CostApply> entityList = new ArrayList<>();
        for (Integer id : ids) {
            CostApply costApply = new CostApply();
            costApply.setId(id);
            costApply.setPay(PaymentType.PAYMENT.getCode());
            entityList.add(costApply);
        }
        costApplyService.updateBatchById(entityList);
        return SUCCESS_TIP;
    }
}
