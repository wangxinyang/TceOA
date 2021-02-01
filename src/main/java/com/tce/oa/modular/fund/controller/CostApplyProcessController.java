package com.tce.oa.modular.fund.controller;


import cn.stylefeng.roses.core.base.controller.BaseController;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.tce.oa.core.common.constant.factory.ConstantFactory;
import com.tce.oa.core.common.constant.state.LocationType;
import com.tce.oa.core.common.constant.state.ProcessNameType;
import com.tce.oa.core.common.constant.state.ProcessTaskId;
import com.tce.oa.modular.fund.model.CostApply;
import com.tce.oa.modular.fund.model.CostApplyFeeDetail;
import com.tce.oa.modular.fund.model.CostApplyPurchaseGoods;
import com.tce.oa.modular.fund.service.ICostApplyFeeDetailService;
import com.tce.oa.modular.fund.service.ICostApplyPurchaseGoodsService;
import com.tce.oa.modular.fund.service.ICostApplyService;
import com.tce.oa.modular.fund.transfer.CostApplyDto;
import com.tce.oa.modular.fund.vo.CostFeeDetailVo;
import com.tce.oa.modular.fund.vo.CostPurchaseDetailVo;
import com.tce.oa.modular.fund.warpper.FundWarpper;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 费用申请审批控制器
 *
 * @author wxy
 * @Date 2017-12-04 21:11:36
 */
@Controller
@RequestMapping("/cost/apply/approve")
public class CostApplyProcessController extends BaseController {

    private String PREFIX = "/biz/apply/cost/process/";


    @Autowired
    private ICostApplyService purchaseService;

    @Autowired
    private ICostApplyPurchaseGoodsService purchaseGoodsService;

    @Autowired
    private ICostApplyFeeDetailService feeDetailService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private HistoryService historyService;

    /**
     * 跳转到审批管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "process.html";
    }

    /**
     * 查看资金预算申请详情
     */
    @RequestMapping("/purchase/detail/{taskId}/{type}")
    public String fundApplyDetail(@PathVariable String taskId, @PathVariable Integer type, Model model) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        // 根据taskid查询出流程定义的id
        String processId = task.getProcessInstanceId();
        // 获取当前的流程定义key
        String taskDefinitionKey = task.getTaskDefinitionKey();

        Wrapper<CostApply> wrapper = new EntityWrapper<>();
        wrapper.eq("processId", processId);
        // 根据processid查询出预算申请信息
        CostApply costApply = purchaseService.selectOne(wrapper);
        model.addAttribute("location", costApply.getLocation());
        model.addAttribute("locationName", LocationType.valueOf(costApply.getLocation()));
        model.addAttribute("projectName", ConstantFactory.me().getProjectName(costApply.getProjectid()));
        model.addAttribute("code", ConstantFactory.me().getProjectCode(costApply.getProjectid()));
        model.addAttribute("deptname", ConstantFactory.me().getDeptName(costApply.getDeptid()));
        model.addAttribute("taskId", taskId);
        model.addAttribute("taskDefinitionKey", taskDefinitionKey);
        model.addAttribute("taskUser", ConstantFactory.me().getUserPositionById(costApply.getUserid()));
        model.addAttribute("item", costApply);

        model.addAttribute("leaderapprove", "");
        model.addAttribute("assistantapprove", "");
        model.addAttribute("casherapprove", "");
        model.addAttribute("deputyapprove", "");
        model.addAttribute("managerapprove", "");
        // 查询该申请信息的历史任务
        List<HistoricVariableInstance> variableInstances = historyService.createHistoricVariableInstanceQuery()
                .processInstanceId(processId)
                .variableName("assigneeUser")
                .list();
        for (int i=0; i<variableInstances.size(); i++) {
            String assigneeUser = (String)variableInstances.get(i).getValue();
            String[] assigneeUsers = assigneeUser.split(":");
            if (assigneeUsers[1].equals(ProcessTaskId.LEADER.getMessage())) {
                model.addAttribute("leaderapprove", assigneeUsers[0]);
            } else if (assigneeUsers[1].equals(ProcessTaskId.ASSISTANT.getMessage())) {
                model.addAttribute("assistantapprove", assigneeUsers[0]);
            } else if (assigneeUsers[1].equals(ProcessTaskId.FINANCE.getMessage())) {
                model.addAttribute("casherapprove", assigneeUsers[0]);
            } else if (assigneeUsers[1].equals(ProcessTaskId.DEPUTYMANAGER.getMessage())) {
                model.addAttribute("deputyapprove", assigneeUsers[0]);
            } else if (assigneeUsers[1].equals(ProcessTaskId.MANAGER.getMessage())) {
                model.addAttribute("managerapprove", assigneeUsers[0]);
            }
        }


        // 根据申请的类型不同,获取不同的详情数据
        if (ProcessNameType.PURCHASE_APPLY.getCode() == type.intValue()) {
            List<CostPurchaseDetailVo> purchaseGoodsConifrmVoList = new ArrayList<>();
            List<CostPurchaseDetailVo> purchaseGoodsInquiryVoList = new ArrayList<>();
            List<CostApplyPurchaseGoods> purchaseGoodsList = purchaseGoodsService.listByAid(costApply.getId());
            for (CostApplyPurchaseGoods costApplyPurchaseGoods : purchaseGoodsList) {
                CostPurchaseDetailVo costPurchaseDetailVo = new CostPurchaseDetailVo();
                modelMapper.map(costApplyPurchaseGoods, costPurchaseDetailVo);
                if (costPurchaseDetailVo.getSupplier() != null) {
                    purchaseGoodsConifrmVoList.add(costPurchaseDetailVo);
                } else {
                    purchaseGoodsInquiryVoList.add(costPurchaseDetailVo);
                }
            }
            model.addAttribute("purchaseGoodsConfirmList", purchaseGoodsConifrmVoList);
            model.addAttribute("purchaseGoodsInquiryList", purchaseGoodsInquiryVoList);
            return PREFIX + "process_purchase_detail.html";
        } else if (ProcessNameType.FEE_APPLY.getCode() == type.intValue()) {
            List<CostFeeDetailVo> costFeeDetailVoList = new ArrayList<>();
            List<CostApplyFeeDetail> feeDetails = feeDetailService.listByFid(costApply.getId());
            for (CostApplyFeeDetail feeDetail : feeDetails) {
                CostFeeDetailVo costFeeDetailVo = new CostFeeDetailVo();
                modelMapper.map(feeDetail, costFeeDetailVo);
                costFeeDetailVoList.add(costFeeDetailVo);
            }
            model.addAttribute("costFeeDetailList", costFeeDetailVoList);
            return PREFIX + "process_fee_detail.html";
        } else {
            return PREFIX + "process_payment_detail.html";
        }
    }


    /**
     * 获取审批管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String type) {
        List<Map<String, Object>> processList = purchaseService.getProcessList();
        return new FundWarpper(processList).wrap();
    }

    /**
     * 审核通过
     */
    @RequestMapping(value = "/pass")
    @ResponseBody
    public Object pass(@RequestBody CostApplyDto costApplyDto) {
        purchaseService.pass(costApplyDto);
        return SUCCESS_TIP;
    }

    /**
     * 审核不通过
     */
    @RequestMapping(value = "/unPass")
    @ResponseBody
    public Object unPass(@RequestBody CostApplyDto costApplyDto) {
        purchaseService.unPass(costApplyDto);
        return SUCCESS_TIP;
    }
}
