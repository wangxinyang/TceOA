package com.tce.oa.modular.reimburse.controller;


import cn.stylefeng.roses.core.base.controller.BaseController;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.tce.oa.core.common.constant.factory.ConstantFactory;
import com.tce.oa.core.common.constant.state.LocationType;
import com.tce.oa.core.common.constant.state.ProcessNameType;
import com.tce.oa.core.common.constant.state.ProcessTaskId;
import com.tce.oa.core.common.constant.state.TransportationType;
import com.tce.oa.core.log.LogObjectHolder;
import com.tce.oa.modular.fund.model.CostApply;
import com.tce.oa.modular.fund.service.ICostApplyService;
import com.tce.oa.modular.reimburse.model.ReimburseConsume;
import com.tce.oa.modular.reimburse.model.ReimburseConsumeDetail;
import com.tce.oa.modular.reimburse.model.ReimburseTravel;
import com.tce.oa.modular.reimburse.model.ReimburseTravelPath;
import com.tce.oa.modular.reimburse.service.IReimburseConsumeDetailService;
import com.tce.oa.modular.reimburse.service.IReimburseConsumeService;
import com.tce.oa.modular.reimburse.service.IReimburseTravelPathService;
import com.tce.oa.modular.reimburse.service.IReimburseTravelService;
import com.tce.oa.modular.reimburse.transfer.ConsumeReimburseDto;
import com.tce.oa.modular.reimburse.transfer.TravelReimburseDto;
import com.tce.oa.modular.reimburse.vo.ConsumeDetailVo;
import com.tce.oa.modular.reimburse.warpper.ReimburseWarpper;
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
 * 报销流程控制器
 *
 * @author wxy
 * @Date 2017-12-04 21:11:36
 */
@Controller
@RequestMapping("/reimburse/process/approve")
public class ReimburseProcessController extends BaseController {

    private String PREFIX = "/biz/reimburse/process/";

    @Autowired
    private IReimburseTravelService reimburseTravelService;

    @Autowired
    private IReimburseTravelPathService reimburseTravelPathService;

    @Autowired
    private IReimburseConsumeService reimburseConsumeService;

    @Autowired
    private IReimburseConsumeDetailService reimburseConsumeDetailService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private ICostApplyService costApplyService;

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
    @RequestMapping("/detail/{taskId}/{type}")
    public String detail(@PathVariable String taskId,
                                  @PathVariable Integer type, Model model) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        // 根据taskid查询出流程定义的id
        String processId = task.getProcessInstanceId();
        // 获取当前的流程定义key
        String taskDefinitionKey = task.getTaskDefinitionKey();

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
            } else if (assigneeUsers[1].equals(ProcessTaskId.FINANCE.getMessage())
                    || assigneeUsers[1].equals(ProcessTaskId.CQFINANCE.getMessage())
                    || assigneeUsers[1].equals(ProcessTaskId.BJFINANCE.getMessage())) {
                model.addAttribute("casherapprove", assigneeUsers[0]);
            } else if (assigneeUsers[1].equals(ProcessTaskId.DEPUTYMANAGER.getMessage())) {
                model.addAttribute("deputyapprove", assigneeUsers[0]);
            } else if (assigneeUsers[1].equals(ProcessTaskId.MANAGER.getMessage())) {
                model.addAttribute("managerapprove", assigneeUsers[0]);
            }
        }

        // 差旅费报销
        if (ProcessNameType.REIMBURSE_TRAVEL.getCode() == type.intValue()) {
            Wrapper<ReimburseTravel> reimburseTravelWrapper = new EntityWrapper<>();
            reimburseTravelWrapper.eq("processId", processId);
            // 根据processid查询报销信息
            ReimburseTravel reimburseTravel = reimburseTravelService.selectOne(reimburseTravelWrapper);
            // 查询出差旅的详情路径信息
            List<ReimburseTravelPath> reimburseTravelPathList = reimburseTravelPathService.listByAid(reimburseTravel.getId());
            model.addAttribute("item", reimburseTravel);
            model.addAttribute("deptName", ConstantFactory.me().getDeptName(reimburseTravel.getDeptid()));
            model.addAttribute("locationName", LocationType.valueOf(reimburseTravel.getLocation()));
            model.addAttribute("projectName", ConstantFactory.me().getProjectName(reimburseTravel.getProjectid()));
            model.addAttribute("travelMethod", TransportationType.valueOf(reimburseTravel.getTravelmethod()));
            model.addAttribute("code", ConstantFactory.me().getProjectCode(reimburseTravel.getProjectid()));
            model.addAttribute("taskId", taskId);
            model.addAttribute("taskUserPosition", ConstantFactory.me().getUserPositionById(reimburseTravel.getUserid()));
            model.addAttribute("taskDefinitionKey", taskDefinitionKey);
            model.addAttribute("reimburseTravelPathList", reimburseTravelPathList);
            LogObjectHolder.me().set(reimburseTravelPathList);
            return PREFIX + "process_travel_detail.html";
        } else {
            // 费用报销
            Wrapper<ReimburseConsume> reimburseConsumeWrapper = new EntityWrapper<>();
            reimburseConsumeWrapper.eq("processId", processId);
            // 根据processid查询报销信息
            ReimburseConsume reimburseConsume = reimburseConsumeService.selectOne(reimburseConsumeWrapper);
            // 查询出报销详情信息
            List<ReimburseConsumeDetail> reimburseConsumeDetailList = reimburseConsumeDetailService.listByRcId(reimburseConsume.getId());
            List<ConsumeDetailVo> consumeDetailVoList = new ArrayList<>();
            for (ReimburseConsumeDetail reimburseConsumeDetail : reimburseConsumeDetailList) {
                ConsumeDetailVo consumeDetailVo = new ConsumeDetailVo();
                modelMapper.map(reimburseConsumeDetail, consumeDetailVo);
                // 手动set的值
                consumeDetailVo.setSubjectname(ConstantFactory.me().getSubjectName(reimburseConsumeDetail.getSubjectid()));
                consumeDetailVo.setProjectname(ConstantFactory.me().getProjectName(reimburseConsumeDetail.getProjectid()));
                consumeDetailVo.setCode(ConstantFactory.me().getProjectCode(reimburseConsumeDetail.getProjectid()));
                // 处理费用申请单号 查询申请状态
                String reqno = reimburseConsumeDetail.getReqno();
                Wrapper<CostApply> wrapper = new EntityWrapper<>();
                wrapper.eq("reqno", reqno);
                CostApply costApply = costApplyService.selectOne(wrapper);
                consumeDetailVo.setReqno(reqno);
                consumeDetailVoList.add(consumeDetailVo);
            }

            model.addAttribute("item", reimburseConsume);
            model.addAttribute("deptname", ConstantFactory.me().getDeptName(reimburseConsume.getDeptid()));
            model.addAttribute("locationName", LocationType.valueOf(reimburseConsume.getLocation()));
            model.addAttribute("taskId", taskId);
            model.addAttribute("taskUserPosition", ConstantFactory.me().getUserPositionById(reimburseConsume.getUserid()));
            model.addAttribute("taskDefinitionKey", taskDefinitionKey);
            model.addAttribute("consumeDetailVoList", consumeDetailVoList);
            LogObjectHolder.me().set(reimburseConsumeDetailList);
            return PREFIX + "process_common_detail.html";
        }
    }


    /**
     * 获取审批管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        List<Map<String, Object>> processList = reimburseTravelService.getProcessList();
        return new ReimburseWarpper(processList).wrap();
    }

//    /**
//     * 审核通过
//     */
//    @RequestMapping(value = "/selfPass")
//    @ResponseBody
//    public Object selfPass(String taskId, Integer location) {
//        fundApplyService.selfPass(taskId, location);
//        return SUCCESS_TIP;
//    }
//
//    /**
//     * 审核不通过
//     */
//    @RequestMapping(value = "/selfUnPass")
//    @ResponseBody
//    public Object selfUnPass(String taskId) {
//        fundApplyService.selfUnPass(taskId);
//        return SUCCESS_TIP;
//    }

    /**
     * 审核通过
     */
    @RequestMapping(value = "/pass")
    @ResponseBody
    public Object pass(@RequestBody TravelReimburseDto travelReimburseDto) {
        reimburseTravelService.pass(travelReimburseDto);
        return SUCCESS_TIP;
    }

    /**
     * 审核通过
     */
    @RequestMapping(value = "/consume/pass")
    @ResponseBody
    public Object consumePass(@RequestBody ConsumeReimburseDto consumeReimburseDto) {
        reimburseConsumeService.pass(consumeReimburseDto);
        return SUCCESS_TIP;
    }

    /**
     * 审核不通过
     */
    @RequestMapping(value = "/unPass")
    @ResponseBody
    public Object unPass(@RequestBody TravelReimburseDto travelReimburseDto) {
        reimburseTravelService.unPass(travelReimburseDto);
        return SUCCESS_TIP;
    }

    /**
     * 审核不通过
     */
    @RequestMapping(value = "/consume/unPass")
    @ResponseBody
    public Object consumeUnPass(@RequestBody ConsumeReimburseDto consumeReimburseDto) {
        reimburseConsumeService.unPass(consumeReimburseDto);
        return SUCCESS_TIP;
    }


}
