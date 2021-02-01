package com.tce.oa.modular.fund.service.impl;

import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tce.oa.core.common.constant.state.ProcessState;
import com.tce.oa.core.common.constant.state.ProcessTaskId;
import com.tce.oa.core.common.exception.BizExceptionEnum;
import com.tce.oa.core.shiro.ShiroKit;
import com.tce.oa.core.util.ProcessUtil;
import com.tce.oa.modular.fund.dao.BudgetApplyDetailMapper;
import com.tce.oa.modular.fund.dao.BudgetApplyMapper;
import com.tce.oa.modular.fund.model.BudgetApply;
import com.tce.oa.modular.fund.model.BudgetApplyDetail;
import com.tce.oa.modular.fund.service.IBudgetApplyService;
import com.tce.oa.modular.fund.transfer.BudgetApplyDetailDto;
import com.tce.oa.modular.fund.transfer.BudgetApplyDto;
import org.flowable.engine.*;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 预算申请表 服务实现类
 * </p>
 *
 * @author wangxy
 * @since 2018-11-14
 */
@Service
public class BudgetApplyServiceImpl extends ServiceImpl<BudgetApplyMapper, BudgetApply> implements IBudgetApplyService {

    @Value("${guns.flowable-fund-apply-process-definition}")
    private String fundApplyProcessDefinitionId;

    private static final String APPLY_DEFINITION_KEY = "budgetApplyProcess";

    @Autowired
    private ModelMapper modelMapper;

    @Resource
    private BudgetApplyMapper fundApplyMapper;

    @Resource
    private BudgetApplyDetailMapper fundApplyDetailMapper;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private ProcessEngine processEngine;

    /**
     * @return com.tce.oa.modular.fund.model.FundApply
     * @Description 查询是否本月内有数据
     * @Date 18:54 2018/11/15
     * @Param [now]
     **/
    @Override
    public BudgetApply queryCanAdd() {
        Integer id = ShiroKit.getUser().getId();
        Integer deptId = ShiroKit.getUser().getDeptId();
        return fundApplyMapper.queryCanAdd(id, deptId);
    }

    /**
     * 保存业务预算数据
     *
     * @param buildDataMap
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveFundApply(Integer type, Map<String, Object> buildDataMap) {
        BudgetApply fundApply = (BudgetApply) buildDataMap.get("fundApply");
        // 插入数据
        this.baseMapper.insert(fundApply);
        // 获取主键id
        Integer id = fundApply.getId();
        List<BudgetApplyDetail> fundApplyDetailList = (List<BudgetApplyDetail>) buildDataMap.get("fundApplyDetailList");
        // 插入预算详情表
        for (BudgetApplyDetail fundApplyDetail : fundApplyDetailList) {
            fundApplyDetail.setAid(id);
            fundApplyDetailMapper.insert(fundApplyDetail);
        }
    }

    /**
     * 保存业务预算数据
     *
     * @param buildDataMap
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addFundApply(Integer type, Map<String, Object> buildDataMap) {
        BudgetApply fundApply = (BudgetApply) buildDataMap.get("fundApply");
        // 如果是被打回的申请再次提交流程就不需要再次插入数据了
        if (StringUtils.isEmpty(fundApply.getId())) {
            // 先插入biz_reimburse_travel表
            this.baseMapper.insert(fundApply);
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("reqno", fundApply.getReqno());
        map.put("createtime", fundApply.getCreatetime());
        map.put("type", fundApply.getType());
        // 涉及流程审批人设置
        map.put("taskUser", ShiroKit.getUser().getPosition());
        map.put("taskUserName", ShiroKit.getUser().getName());
        map.put("id", fundApply.getId());
        ProcessInstance fundSalesApplyProcess = runtimeService.startProcessInstanceByKey(fundApplyProcessDefinitionId, map);
        String processId = fundSalesApplyProcess.getId();
        // 回填数据
        fundApply.setProcessId(processId);
        fundApply.setState(ProcessState.CHECKING_ASSISTANT.getCode());
        // 直接审批通过
        List<Task> list = taskService.createTaskQuery()
                .taskAssignee(ShiroKit.getUser().getPosition())
                .processDefinitionKey(APPLY_DEFINITION_KEY)
                .processInstanceId(processId)
                .orderByTaskCreateTime().desc()
                .list();
        if (list != null && list.size() > 0) {
            for (Task task : list) {
                map = new HashMap<>();
                map.put("submitType", "同意");
                String processInstanceId = task.getProcessInstanceId();
                //通过审核
                taskService.complete(task.getId(), map);
                // 修改审批状态
                fundApply.setState(ProcessState.CHECKING_ASSISTANT.getCode());
                fundApply.updateById();
                // 插入或者修改详情表表数据
                List<BudgetApplyDetail> fundApplyDetailList =
                        (List<BudgetApplyDetail>) buildDataMap.get("fundApplyDetailList");
                // 插入预算详情表
                for (BudgetApplyDetail fundApplyDetail : fundApplyDetailList) {
                    if (StringUtils.isEmpty(fundApplyDetail.getId())) {
                        fundApplyDetail.setAid(fundApply.getId());
                        fundApplyDetail.insert();
                    } else {
                        fundApplyDetail.updateById();
                    }
                }
            }
        } else {
            throw new ServiceException(BizExceptionEnum.FLOWABLE_QUERY_TASK_ERROR);
        }
    }

    /**
     * 修改业务预算数据
     *
     * @param buildDataMap
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateFundApply(Integer type, Map<String, Object> buildDataMap) {
        BudgetApply fundApply = (BudgetApply) buildDataMap.get("fundApply");
        this.baseMapper.updateById(fundApply);

        // 插入或者修改详情表表数据
        List<BudgetApplyDetail> fundApplyDetailList =
                (List<BudgetApplyDetail>) buildDataMap.get("fundApplyDetailList");
        // 插入预算详情表
        updateFundApplyDetailData(fundApply, fundApplyDetailList);
    }

    /**
     * 在填写实际支出金额和执行率之后更新预算数据
     *
     * @param buildDataMap
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateFundApplyDetail(Integer type, Map<String, Object> buildDataMap) {
        // 插入或者修改详情表表数据
        List<BudgetApplyDetail> fundApplyDetailList =
                (List<BudgetApplyDetail>) buildDataMap.get("fundApplyDetailList");
        // 更新预算详情表
        for (BudgetApplyDetail fundApplyDetail : fundApplyDetailList) {
            Wrapper<BudgetApplyDetail> fundApplyDetailWrapper = new EntityWrapper<>();
            fundApplyDetailWrapper.eq("id", fundApplyDetail.getId());
            fundApplyDetailMapper.update(fundApplyDetail, fundApplyDetailWrapper);
        }
    }


    /**
     * @return java.util.List<java.util.Map       <       java.lang.String       ,       java.lang.Object>>
     * @Description 获取流程信息
     * @Date 10:01 2018/11/19
     * @Param []
     **/
    @Override
    public List<Map<String, Object>> getProcessList() {
        String position = ShiroKit.getUser().getPosition();
        List<Task> list = ProcessUtil.me().getProcessList(position, APPLY_DEFINITION_KEY);
        ArrayList<Map<String, Object>> taskVos = new ArrayList<>();
        for (Task task : list) {
            String taskUser = (String) taskService.getVariable(task.getId(), "taskUser");
            String taskUserName = (String) taskService.getVariable(task.getId(), "taskUserName");
            Integer id = (Integer) taskService.getVariable(task.getId(), "id");
            BudgetApply fundApply = fundApplyMapper.selectById(id);
            boolean flag = false;
            if (position.equals(taskUser)) {
                flag = false;
            } else {
                flag = true;
            }
            Map<String, Object> map = new HashMap<>();
            map.put("id", task.getId());
            map.put("processId", task.getProcessInstanceId());
            map.put("deptid", fundApply.getDeptid());
            map.put("location", fundApply.getLocation());
            map.put("locationType", fundApply.getLocation());
            map.put("type", fundApply.getType());
            map.put("money", fundApply.getMoney());
            map.put("createTime", task.getCreateTime());
            map.put("assignee", taskUser);
            map.put("taskUserName", taskUserName);
            map.put("selfFlag", flag);
            map.put("taskDefinitionKey", task.getTaskDefinitionKey());
            taskVos.add(map);
        }
        return taskVos;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pass(BudgetApplyDto fundApplyDto) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("submitType", "同意");
        map.put("location", fundApplyDto.getLocation());
        map.put("assigneeUser", ShiroKit.getUser().getName() + ":" + fundApplyDto.getTaskDefinitionKey());
        updateDetailByApprove(fundApplyDto);
        // 开始同意流程
        approveProcess(fundApplyDto.getTaskId(), map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unPass(BudgetApplyDto fundApplyDto) {
        //使用任务ID，查询任务对象，获取流程流程实例ID
        Task task = taskService.createTaskQuery()
                .taskId(fundApplyDto.getTaskId())
                .singleResult();
        String processInstanceId = task.getProcessInstanceId();
        // 不以跳转task的方式处理了 直接删除当前的流程 保留原始数据
        if (null != processInstanceId) {
            // 删除流程任务
            runtimeService.deleteProcessInstance(processInstanceId, null);
        }
        // 更新状态为撤回
        Wrapper<BudgetApply> wrapper = new EntityWrapper<BudgetApply>().eq("processId", processInstanceId);
        BudgetApply fundApply = this.selectOne(wrapper);
        fundApply.setState(ProcessState.UN_PASS.getCode());
        fundApply.setProcessId("");
        fundApply.updateById();
        updateDetailByApprove(fundApplyDto);
    }

    /**
     * @return void
     * @Description 画流程信息图
     * @Date 10:01 2018/11/19
     * @Param [id]
     **/
    @Override
    public void printProcessImage(Integer id) throws IOException {
        BudgetApply fundApply = this.selectById(id);
        String processId = fundApply.getProcessId();
        ProcessUtil.me().printProcessImage(processId);
    }

    /**
     * @return void
     * @Description 删除流程数据和任务
     * @Date 16:04 2018/11/23
     * @Param []
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteData(Integer fundApplyId, Integer type) {
        BudgetApply fundApply = fundApplyMapper.selectById(fundApplyId);
        if (null != fundApply) {
            fundApply.deleteById();
            Wrapper<BudgetApplyDetail> wrapper = new EntityWrapper<>();
            wrapper.eq("aid", fundApplyId);
            fundApplyDetailMapper.delete(wrapper);
        }
    }

    /**
     *@Description 根据查询条件查询数据
     *@Date 17:12 2018/12/11
     *@Param [beginTime, endTime]
     *@return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     **/
    @Override
    public List<Map<String, Object>> selectApplys(Page<BudgetApply> page, Integer applyType, String beginTime, String endTime) {
        return this.baseMapper.queryApplys(page, applyType, beginTime, endTime);
    }

    @Override
    public List<Integer> queryApplysCqForExport(String beginTime, String endTime) {
        return this.baseMapper.queryApplysCqForExport(beginTime, endTime);
    }

    @Override
    public List<Integer> queryApplysBjForExport(String beginTime, String endTime) {
        return this.baseMapper.queryApplysBjForExport(beginTime, endTime);
    }

    /**
     * @return void
     * @Description 准备流程启动
     * @Date 14:28 2018/11/27
     * @Param [fundApply]
     **/
    private void prepareProcess(BudgetApply fundApply) {
        HashMap<String, Object> map = new HashMap<>();
        // 涉及流程审批人设置
        map.put("taskUser", ShiroKit.getUser().getAccount());
        map.put("taskUserName", ShiroKit.getUser().getName());
        map.put("id", fundApply.getId());
        ProcessInstance fundSalesApplyProcess = runtimeService.startProcessInstanceByKey(fundApplyProcessDefinitionId, map);
        String processId = fundSalesApplyProcess.getId();
        // 根据fundApply的id判断数据库表中是否已经保存有数据 没有就新增数据 有就更新数据
        fundApply.setProcessId(processId);
        fundApply.setState(ProcessState.CHECKING_ASSISTANT.getCode());
    }

    /**
     * @return void
     * @Description 执行pass操作
     * @Date 14:24 2018/11/27
     * @Param []
     **/
    private void passFlowableProcess() {
        HashMap<String, Object> map;
        String name = ShiroKit.getUser().getAccount();
        List<Task> list = taskService.createTaskQuery()
                .taskAssignee(name)
                .orderByTaskCreateTime().desc()
                .list();
        if (list != null && list.size() > 0) {
            for (Task task : list) {
                map = new HashMap<>();
                map.put("submitType", "同意");
                String processInstanceId = task.getProcessInstanceId();
                //通过审核
                taskService.complete(task.getId(), map);
                List<String> activityIds = ProcessUtil.me().getActivityIds(processInstanceId);
                //判断流程是否结束,结束之后修改状态
                updateStateAfterApprove(activityIds, processInstanceId);
            }
        } else {
            throw new ServiceException(BizExceptionEnum.FLOWABLE_QUERY_TASK_ERROR);
        }
    }

    /**
     * @return void
     * @Description 执行详情表的更新和插入操作
     * @Date 15:02 2018/11/23
     * @Param [fundApply, detailData]
     **/
    private void updateFundApplyDetailData(BudgetApply fundApply, List<BudgetApplyDetail> detailData) {
        for (BudgetApplyDetail fundApplyDetail : detailData) {
            if (fundApplyDetail.getId() != null) {
                Wrapper<BudgetApplyDetail> fundApplyDetailWrapper = new EntityWrapper<>();
                fundApplyDetailWrapper.eq("id", fundApplyDetail.getId());
                fundApplyDetailMapper.update(fundApplyDetail, fundApplyDetailWrapper);
            } else {
                fundApplyDetail.setAid(fundApply.getId());
                fundApplyDetailMapper.insert(fundApplyDetail);
            }
        }
    }

    /**
     * @return void
     * @Description 审批执行
     * @Date 15:29 2018/11/20
     * @Param [taskId, map]
     **/
    private void approveProcess(String taskId, HashMap<String, Object> map) {
        //使用任务ID，查询任务对象，获取流程流程实例ID
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult();
        //使用流程实例ID，查询正在执行的执行对象表，返回流程实例对象
        String InstanceId = task.getProcessInstanceId();
        //通过审核
        taskService.complete(taskId, map);
        List<String> activityIds = ProcessUtil.me().getActivityIds(InstanceId);
        //判断流程是否结束,结束之后修改状态
        updateStateAfterApprove(activityIds, InstanceId);
    }


    /**
     * @return void
     * @Description 判断流程是否结束, 结束之后修改状态
     * @Date 18:05 2018/11/21
     * @Param [task]
     **/
    private void updateStateAfterApprove(List<String> activityIds, String processId) {
        //使用流程实例ID查询
        ProcessInstance pi = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processId)
                .singleResult();
        Wrapper<BudgetApply> wrapper = new EntityWrapper<BudgetApply>().eq("processId", processId);
        BudgetApply fundApply = this.selectOne(wrapper);
        //审核通过修改为通过状态
        if (pi == null) {
            fundApply.setState(ProcessState.PASS.getCode());
        } else {
            //审核通过如果还有记录则为经理或boss审核中
            if (activityIds.contains(ProcessTaskId.ASSISTANT.getMessage())) {
                fundApply.setState(ProcessState.CHECKING_ASSISTANT.getCode());
            } else if (activityIds.contains(ProcessTaskId.DEPUTYMANAGER.getMessage())) {
                fundApply.setState(ProcessState.CHECKING_DEPUTY.getCode());
            } else if (activityIds.contains(ProcessTaskId.MANAGER.getMessage())) {
                fundApply.setState(ProcessState.CHECKING_MANAGER.getCode());
            }
        }
        fundApply.updateById();
    }

    /**
     * @return java.lang.String
     * @Description 根据除申请者之外的其他审批者审批结果更新数据库
     * @Date 17:06 2018/11/21
     * @Param [fundApplyDto]
     **/
    private void updateDetailByApprove(BudgetApplyDto fundApplyDto) {
        Integer type = fundApplyDto.getType();
        // 更新数据库数据
        List<BudgetApplyDetailDto> fundApplyDtos = fundApplyDto.getFundApplyDetailDtos();
        for (BudgetApplyDetailDto fundApplyDetailDto : fundApplyDtos) {
            BudgetApplyDetail fundApplyDetailSales = fundApplyDetailMapper.selectById(fundApplyDetailDto.getId());
            fundApplyDetailSales.setAssistantnote(fundApplyDetailDto.getAssistantnote());
            fundApplyDetailSales.setSubapprovefee(fundApplyDetailDto.getSubapprovefee());
            fundApplyDetailSales.setDeputynote(fundApplyDetailDto.getDeputynote());
            fundApplyDetailSales.setApprovefee(fundApplyDetailDto.getApprovefee());
            fundApplyDetailSales.setManagernote(fundApplyDetailDto.getManagernote());
            fundApplyDetailSales.setNote(fundApplyDetailDto.getNote());
            fundApplyDetailSales.updateById();
        }
    }
}
