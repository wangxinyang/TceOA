package com.tce.oa.modular.fund.controller;


import cn.stylefeng.roses.core.base.controller.BaseController;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.tce.oa.core.common.constant.factory.ConstantFactory;
import com.tce.oa.core.common.constant.state.LocationType;
import com.tce.oa.core.common.constant.state.ProcessNameType;
import com.tce.oa.core.common.constant.state.ProcessTaskId;
import com.tce.oa.core.log.LogObjectHolder;
import com.tce.oa.modular.fund.model.BudgetApply;
import com.tce.oa.modular.fund.model.BudgetApplyDetail;
import com.tce.oa.modular.fund.service.IBudgetApplyDetailService;
import com.tce.oa.modular.fund.service.IBudgetApplyService;
import com.tce.oa.modular.fund.transfer.BudgetApplyDto;
import com.tce.oa.modular.fund.vo.BudgetApplyDetailVo;
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
 * 资金审批控制器
 *
 * @author wxy
 * @Date 2017-12-04 21:11:36
 */
@Controller
@RequestMapping("/fund/budget/process")
public class BudgetProcessController extends BaseController {

    private String PREFIX = "/biz/apply/budget/process/";

    @Autowired
    private IBudgetApplyService fundApplyService;

    @Autowired
    private IBudgetApplyDetailService fundApplyDetailService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private HistoryService historyService;

    private static final String NO_PROJECT = "-1";

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
    @RequestMapping("/detail/{taskId}/{flag}")
    public String fundApplyDetail(@PathVariable String taskId,
                                  @PathVariable Boolean flag, Model model) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        // 根据taskid查询出流程定义的id
        String processId = task.getProcessInstanceId();
        // 获取当前的审核者
        String assignee = task.getAssignee();
        // 获取当前的流程定义key
        String taskDefinitionKey = task.getTaskDefinitionKey();
        Wrapper<BudgetApply> fundApplyWrapper = new EntityWrapper<>();
        fundApplyWrapper.eq("processId", processId);
        // 根据processid查询出预算申请信息
        BudgetApply fundApply = this.fundApplyService.selectOne(fundApplyWrapper);
        // 根据申请的类型不同,获取不同的详情数据
        List<BudgetApplyDetailVo> fundApplyDetails = new ArrayList<>();
        List<BudgetApplyDetail> salesList = fundApplyDetailService.listByAid(fundApply.getId());
        for (BudgetApplyDetail fundApplyDetailSales : salesList) {
            BudgetApplyDetailVo fundApplyDetailVo = new BudgetApplyDetailVo();
            modelMapper.map(fundApplyDetailSales, fundApplyDetailVo);
            fundApplyDetailVo.setSubjectname(ConstantFactory.me().getSubjectName(fundApplyDetailSales.getSubjectid()));
            String projectid = fundApplyDetailSales.getProjectid();
            if (NO_PROJECT.equals(projectid)) {
                fundApplyDetailVo.setProjectname("无");
            } else {
                String[] projectids = projectid.split(":");
                fundApplyDetailVo.setProjectname(ConstantFactory.me().getProjectName(Integer.parseInt(projectids[0])));
            }
            fundApplyDetails.add(fundApplyDetailVo);
        }
        model.addAttribute("type", fundApply.getType());
        model.addAttribute("location", fundApply.getLocation());
        model.addAttribute("locationName", LocationType.valueOf(fundApply.getLocation()));
        model.addAttribute("deptname", ConstantFactory.me().getDeptName(fundApply.getDeptid()));
        model.addAttribute("fundApplyId", fundApply.getId());
        model.addAttribute("taskId", taskId);
        model.addAttribute("assignee", assignee);
        model.addAttribute("flag", flag);
        model.addAttribute("taskDefinitionKey", taskDefinitionKey);
        model.addAttribute("fundApplys", fundApplyDetails);

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
            if (assigneeUsers[1].equals(ProcessTaskId.ASSISTANT.getMessage())) {
                model.addAttribute("assistantapprove", assigneeUsers[0]);
            } else if (assigneeUsers[1].equals(ProcessTaskId.DEPUTYMANAGER.getMessage())) {
                model.addAttribute("deputyapprove", assigneeUsers[0]);
            } else if (assigneeUsers[1].equals(ProcessTaskId.MANAGER.getMessage())) {
                model.addAttribute("managerapprove", assigneeUsers[0]);
            }
        }
        LogObjectHolder.me().set(fundApplyDetails);
        switch (ProcessNameType.getByValue(fundApply.getType())) {
            case SALES:
                return PREFIX + "process_sales_detail.html";
            case TRAVEL:
                return PREFIX + "process_travel_detail.html";
            case PURCHASE:
                return PREFIX + "process_purchase_detail.html";
            case OTHER:
                return PREFIX + "process_other_detail.html";
            default:
                return PREFIX + "process_sales_detail.html";
        }
    }


    /**
     * 获取审批管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        List<Map<String, Object>> processList = fundApplyService.getProcessList();
        return new FundWarpper(processList).wrap();
    }

    /**
     * 审核通过
     */
    @RequestMapping(value = "/pass")
    @ResponseBody
    public Object pass(@RequestBody BudgetApplyDto fundApplyDto) {
        fundApplyService.pass(fundApplyDto);
        return SUCCESS_TIP;
    }

    /**
     * 审核不通过
     */
    @RequestMapping(value = "/unPass")
    @ResponseBody
    public Object unPass(@RequestBody BudgetApplyDto fundApplyDto) {
        fundApplyService.unPass(fundApplyDto);
        return SUCCESS_TIP;
    }
}
