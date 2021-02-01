package com.tce.oa.modular.reimburse.service.impl;

import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tce.oa.core.common.constant.state.*;
import com.tce.oa.core.common.exception.BizExceptionEnum;
import com.tce.oa.core.shiro.ShiroKit;
import com.tce.oa.core.util.ProcessUtil;
import com.tce.oa.core.util.TaskJumpCmd;
import com.tce.oa.modular.reimburse.dao.ReimburseConsumeDetailMapper;
import com.tce.oa.modular.reimburse.dao.ReimburseConsumeMapper;
import com.tce.oa.modular.reimburse.model.ReimburseConsume;
import com.tce.oa.modular.reimburse.model.ReimburseConsumeDetail;
import com.tce.oa.modular.reimburse.service.IReimburseConsumeService;
import com.tce.oa.modular.reimburse.transfer.ConsumeReimburseDto;
import org.flowable.engine.ManagementService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

/**
 * <p>
 * 费用报销 服务实现类
 * </p>
 *
 * @author wangxy123
 * @since 2018-11-29
 */
@Service
public class ReimburseConsumeServiceImpl extends ServiceImpl<ReimburseConsumeMapper, ReimburseConsume> implements IReimburseConsumeService {

    @Resource
    private ReimburseConsumeDetailMapper reimburseConsumeDetailMapper;

    @Resource
    private ReimburseConsumeMapper reimburseConsumeMapper;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ProcessEngine processEngine;

    @Value("${guns.flowable-reimburse-process-definition}")
    private String reimburseProcessDefinitionId;

    private static final String REIMBURSE_DEFINITION_KEY = "reimburseProcess";

    @Value("${guns.flowable-finance-position}")
    private String financePosition;

    @Value("${guns.flowable-skip-target}")
    private String jumpTarget;


    /**
     * @return void
     * @Description 保存操作
     * @Date 15:53 2018/11/30
     * @Param [modelMap]
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(Map<String, Object> modelMap) {
        ReimburseConsume reimburseConsume = (ReimburseConsume) modelMap.get("reimburseConsume");
        // 插入biz_reimburse_travel表
        this.baseMapper.insert(reimburseConsume);
        // 获取主键id
        Integer id = reimburseConsume.getId();

        List<ReimburseConsumeDetail> reimburseConsumeDetailList =
                (List<ReimburseConsumeDetail>) modelMap.get("reimburseConsumeDetailList");
        // 填充到travelpath的rtid中
        for (ReimburseConsumeDetail reimburseConsumeDetail : reimburseConsumeDetailList) {
            reimburseConsumeDetail.setRcid(id);
            reimburseConsumeDetailMapper.insert(reimburseConsumeDetail);
        }
    }

    /**
     * @return void
     * @Description 新增操作并开启工作流和同意的操作
     * @Date 15:53 2018/11/30
     * @Param [modelMap, assignee]
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(Map<String, Object> modelMap, String assignee) {
        ReimburseConsume reimburseConsume = (ReimburseConsume) modelMap.get("reimburseConsume");
        List<ReimburseConsumeDetail> reimburseConsumeDetailList =
                (List<ReimburseConsumeDetail>) modelMap.get("reimburseConsumeDetailList");
        // 如果是被打回的申请再次提交流程就不需要再次插入数据了
        if (StringUtils.isEmpty(reimburseConsume.getId())) {
            // 先插入biz_reimburse_travel表
            this.baseMapper.insert(reimburseConsume);
        }
        // 先插入biz_reimburse_travel_path表数据
        for (ReimburseConsumeDetail reimburseConsumeDetail : reimburseConsumeDetailList) {
            if (StringUtils.isEmpty(reimburseConsumeDetail.getId())) {
                reimburseConsumeDetail.setRcid(reimburseConsume.getId());
                reimburseConsumeDetailMapper.insert(reimburseConsumeDetail);
            }
        }

        // 设置流程开始的参数
        HashMap<String, Object> map = new HashMap<>();
        // 涉及流程审批人设置
        String position = ShiroKit.getUser().getPosition();
        map.put("reqno", reimburseConsume.getReimburseno());
        map.put("createtime", reimburseConsume.getCreatetime());
        map.put("taskUserId", position);
        map.put("taskUserName", ShiroKit.getUser().getName());
        map.put("type", ProcessNameType.REIMBURSE_COMMON.getCode());
        map.put("id", reimburseConsume.getId());
        map.put("assignee", assignee);
        ProcessInstance reimburseProcess = runtimeService.startProcessInstanceByKey(reimburseProcessDefinitionId, map);
        String processId = reimburseProcess.getId();
        // 回填数据
        reimburseConsume.setProcessId(processId);
        // 直接审批
        List<Task> list = taskService.createTaskQuery()
                .taskAssignee(position)
                .processDefinitionKey(REIMBURSE_DEFINITION_KEY)
                .processInstanceId(processId)
                .orderByTaskCreateTime().desc()
                .list();
        if (list != null && list.size() > 0) {
            for (Task task : list) {
                map = new HashMap<>();
                map.put("submitType", "同意");
                if (assignee.equals(financePosition)) {
                    // 如果是副总和总经理的账号 并且assignee设置了为财务，为了避免财务两次审批，直接跳转到财务的节点
                    if (position.equals(ProcessAssigneeName.ASSIGNEE_DEPUTY)
                            || position.equals(ProcessAssigneeName.ASSIGNEE_MANAGER)) {
                        reimburseConsume.setState(ProcessState.CHECKING_FINANCE.getCode());
                        // 直接跳转
                        ManagementService managementService = processEngine.getManagementService();
                        managementService.executeCommand(new TaskJumpCmd(task.getId(), jumpTarget));
                    } else {
                        reimburseConsume.setState(ProcessState.CHECKING_LEADER.getCode());
                        // 通过审核
                        taskService.complete(task.getId(), map);
                    }
                } else {
                    reimburseConsume.setState(ProcessState.CHECKING_LEADER.getCode());
                    // 通过审核
                    taskService.complete(task.getId(), map);
                }
                // 更新状态 如果assignee是财务的position 那么提交的状态的就是待财务审批 否则就是待部门负责人审批
                reimburseConsume.updateById();
                for (ReimburseConsumeDetail reimburseConsumeDetail : reimburseConsumeDetailList) {
                    reimburseConsumeDetail.updateById();
                }
            }
        } else {
            throw new ServiceException(BizExceptionEnum.FLOWABLE_QUERY_TASK_ERROR);
        }
    }

    /**
     * @return void
     * @Description 保存操作
     * @Date 15:53 2018/11/30
     * @Param [reimburseConsume, reimburseConsumeDetailList]
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ReimburseConsume reimburseConsume, List<ReimburseConsumeDetail> reimburseConsumeDetailList) {
        // 修改biz_reimburse_consume表
        this.baseMapper.updateById(reimburseConsume);
        // 修改biz_reimburse_consume_detail数据
        for (ReimburseConsumeDetail reimburseConsumeDetail : reimburseConsumeDetailList) {
            if (null == reimburseConsumeDetail.getId()) {
                reimburseConsumeDetail.setRcid(reimburseConsume.getId());
                reimburseConsumeDetail.setCreatetime(new Date());
                reimburseConsumeDetailMapper.insert(reimburseConsumeDetail);
            } else {
                reimburseConsumeDetailMapper.updateById(reimburseConsumeDetail);
            }
        }
    }

    /**
     * @return void
     * @Description 删除数据
     * @Date 17:27 2018/11/30
     * @Param [id]
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        // 删除biz_reimburse_consume表的数据
        this.baseMapper.deleteById(id);
        // 删除biz_reimburse_consume_detail表的数据
        Wrapper<ReimburseConsumeDetail> wrapper = new EntityWrapper<>();
        wrapper.eq("rcid", id);
        reimburseConsumeDetailMapper.delete(wrapper);
    }

    /**
     * @return void
     * @Description 根据id删除差旅行程数据
     * @Date 8:57 2018/12/3
     * @Param [id]
     **/
    @Override
    public void deletePathDetailById(Integer id) {
        reimburseConsumeDetailMapper.deleteById(id);
    }

    /**
     * @return void
     * @Description 画流程信息图
     * @Date 10:01 2018/11/19
     * @Param [id]
     **/
    @Override
    public void printProcessImage(Integer id) throws IOException {
        ReimburseConsume reimburseConsume = this.selectById(id);
        String processId = reimburseConsume.getProcessId();
        ProcessUtil.me().printProcessImage(processId);
    }

    /**
     * @return java.util.List<java.util.Map   <   java.lang.String   ,   java.lang.Object>>
     * @Description 获取流程信息
     * @Date 10:01 2018/11/19
     * @Param []
     **/
    @Override
    public List<Map<String, Object>> getProcessList() {
        String position = ShiroKit.getUser().getPosition();
        List<Task> list = ProcessUtil.me().getProcessList(position, REIMBURSE_DEFINITION_KEY);
        ArrayList<Map<String, Object>> taskVos = new ArrayList<>();
        for (Task task : list) {
            String taskUserId = (String) taskService.getVariable(task.getId(), "taskUserId");
            String taskUserName = (String) taskService.getVariable(task.getId(), "taskUserName");
            Integer type = (Integer) taskService.getVariable(task.getId(), "type");
            Integer id = (Integer) taskService.getVariable(task.getId(), "id");
            ReimburseConsume reimburseConsume = this.baseMapper.selectById(id);
            boolean flag = false;
            if (position.equals(taskUserId)) {
                flag = false;
            } else {
                flag = true;
            }
            Map<String, Object> map = new HashMap<>();
            map.put("id", task.getId());
            map.put("processId", task.getProcessInstanceId());
//            map.put("projetid", reimburseTravel.getProjetid());
            map.put("deptid", reimburseConsume.getDeptid());
            map.put("location", reimburseConsume.getLocation());
//            map.put("totalFee", reimburseTravel.getTotalfee());
            map.put("createTime", task.getCreateTime());
            map.put("assignee", taskUserId);
            map.put("taskUserName", taskUserName);
            map.put("type", type);
            map.put("typename", ProcessNameType.getByValue(type).getMessage());
            map.put("selfFlag", flag);
            taskVos.add(map);
        }
        return taskVos;
    }

    /**
     * @return void
     * @Description 同意操作
     * @Date 10:51 2018/12/4
     * @Param [travelReimburseDto]
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pass(ConsumeReimburseDto consumeReimburseDto) {
        // 执行同意的流程
        HashMap<String, Object> map = new HashMap<>();
        map.put("submitType", "同意");
        map.put("location", LocationType.prefixValueOf(consumeReimburseDto.getLocation()));
        String taskUser = consumeReimburseDto.getTaskUser();
        map.put("taskUser", taskUser);
        map.put("assigneeUser", ShiroKit.getUser().getName() + ":" + consumeReimburseDto.getTaskDefinitionKey());
        processHandler(consumeReimburseDto, map, true);
    }

    /**
     * @return void
     * @Description 驳回操作
     * @Date 10:51 2018/12/4
     * @Param [travelReimburseDto]
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unPass(ConsumeReimburseDto consumeReimburseDto) {
        // 执行驳回的流程
        HashMap<String, Object> map = new HashMap<>();
        map.put("submitType", "驳回");
        processHandler(consumeReimburseDto, map, false);
    }

    /**
     *@Description 获取费用报销的数据
     *@Date 11:22 2018/12/20
     *@Param [page, beginTime, endTime, deptid, state]
     *@return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     **/
    @Override
    public List<Map<String, Object>> getReimburseConsumes(Page<ReimburseConsume> page, String beginTime,
                                                          String endTime, Integer deptid, Integer state,
                                                          List<Integer> ids) {
        return this.baseMapper.getReimburseConsumes(page, beginTime, endTime, deptid, state, ids);
    }

    /**
     *@Description 获取费用报销的数据
     *@Date 11:22 2018/12/20
     *@Param [page, beginTime, endTime, deptid, state]
     *@return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     **/
    @Override
    public List<Map<String, Object>> getReimburseConsumes(String beginTime, String endTime,
                                                          Integer deptid, Integer state, List<Integer> ids) {
        return this.baseMapper.getReimburseConsumes(beginTime, endTime, deptid, state, ids);
    }

    /**
     * @return void
     * @Description 流程同意或者驳回实际操作
     * @Date 10:57 2018/12/4
     * @Param [isPass, travelReimburseDto, map]
     **/
    private void processHandler(ConsumeReimburseDto consumeReimburseDto, HashMap<String, Object> map, Boolean isPass) {
        // 更新审批的内容
        ReimburseConsume reimburseConsume = reimburseConsumeMapper.selectById(consumeReimburseDto.getId());
        String leadernote = consumeReimburseDto.getLeadernote();
        if (!StringUtils.isEmpty(leadernote)) {
            reimburseConsume.setLeadernote(leadernote);
            reimburseConsume.setLeadertime(new Date());
        }
        String cashernote = consumeReimburseDto.getCashernote();
        if (!StringUtils.isEmpty(cashernote)) {
            reimburseConsume.setCashernote(cashernote);
            reimburseConsume.setCashertime(new Date());
        }
        String deputynote = consumeReimburseDto.getDeputynote();
        if (!StringUtils.isEmpty(deputynote)) {
            reimburseConsume.setDeputynote(deputynote);
            reimburseConsume.setDeputytime(new Date());
        }
        String managernote = consumeReimburseDto.getManagernote();
        if (!StringUtils.isEmpty(managernote)) {
            reimburseConsume.setManagernote(managernote);
            reimburseConsume.setManagertime(new Date());
        }
        //使用任务ID，查询任务对象，获取流程流程实例ID
        String taskId = consumeReimburseDto.getTaskId();
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult();
        //使用流程实例ID，查询正在执行的执行对象表，返回流程实例对象
        String InstanceId = task.getProcessInstanceId();
        if (isPass) {
            //通过审核
            taskService.complete(taskId, map);
            // 等审核完毕之后再获取之后节点活动的信息
            List<String> activityIds = ProcessUtil.me().getActivityIds(InstanceId);
            //使用流程实例ID查询
            ProcessInstance pi = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(InstanceId)
                    .singleResult();
            //审核通过修改为通过状态
            if (pi == null) {
                reimburseConsume.setState(ProcessState.PASS.getCode());
            } else {
                //审核通过如果还有记录则为经理或boss审核中
                if (activityIds.contains(ProcessTaskId.LEADER.getMessage())) {
                    reimburseConsume.setState(ProcessState.CHECKING_LEADER.getCode());
                } else if (activityIds.contains(ProcessTaskId.BJFINANCE.getMessage())
                        || activityIds.contains(ProcessTaskId.CQFINANCE.getMessage())
                        || activityIds.contains(ProcessTaskId.FINANCE.getMessage())) {
                    reimburseConsume.setState(ProcessState.CHECKING_FINANCE.getCode());
                } else if (activityIds.contains(ProcessTaskId.DEPUTYMANAGER.getMessage())) {
                    reimburseConsume.setState(ProcessState.CHECKING_DEPUTY.getCode());
                } else if (activityIds.contains(ProcessTaskId.MANAGER.getMessage())) {
                    reimburseConsume.setState(ProcessState.CHECKING_MANAGER.getCode());
                }
            }
        } else {
            // 驳回的情况下自动设置为不通过（相当于自动撤回）的状态，相当于直接把之前的流程完毕
            if (null != InstanceId) {
                // 删除流程任务
                runtimeService.deleteProcessInstance(InstanceId, null);
            }
            reimburseConsume.setState(ProcessState.UN_PASS.getCode());
            reimburseConsume.setProcessId("");
        }
        reimburseConsume.updateById();
    }
}
