package com.tce.oa.modular.fund.service.impl;

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
import com.tce.oa.modular.fund.dao.CostApplyFeeDetailMapper;
import com.tce.oa.modular.fund.dao.CostApplyMapper;
import com.tce.oa.modular.fund.dao.CostApplyPurchaseGoodsMapper;
import com.tce.oa.modular.fund.model.CostApply;
import com.tce.oa.modular.fund.model.CostApplyFeeDetail;
import com.tce.oa.modular.fund.model.CostApplyPurchaseGoods;
import com.tce.oa.modular.fund.service.ICostApplyService;
import com.tce.oa.modular.fund.transfer.CostApplyDto;
import com.tce.oa.modular.fund.transfer.CostApplyPurchaseGoodsDto;
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
 * 采购申请表 服务实现类
 * </p>
 *
 * @author wangxy123
 * @since 2018-12-17
 */
@Service
public class CostApplyServiceImpl extends ServiceImpl<CostApplyMapper, CostApply> implements ICostApplyService {

    @Resource
    private CostApplyPurchaseGoodsMapper purchaseGoodsMapper;

    @Resource
    private CostApplyFeeDetailMapper feeDetailMapper;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ProcessEngine processEngine;

    @Value("${guns.flowable-finance-position}")
    private String financePosition;

    @Value("${guns.flowable-skip-target}")
    private String jumpTarget;

    @Value("${guns.flowable-purchase-process-definition}")
    private String applyProcessDefinitionId;

    private static final String APPLY_DEFINITION_KEY = "purchaseApplyProcess";

    @Value("${guns.flowable-fee-process-definition}")
    private String feeProcessDefinitionId;

    private static final String FEE_DEFINITION_KEY = "feeApplyProcess";

    @Value("${guns.flowable-payment-process-definition}")
    private String paymentProcessDefinitionId;

    private static final String PAYMENT_DEFINITION_KEY = "paymentApplyProcess";

    /**
     * @return void
     * @Description 保存操作
     * @Date 15:53 2018/11/30
     * @Param [modelMap]
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(Map<String, Object> modelMap, Integer type) {
        switch(ProcessNameType.getByValue(type)) {
            case PURCHASE_APPLY:
                CostApply costApplyPurchase = (CostApply) modelMap.get("costApplyPurchase");
                // 插入biz_reimburse_travel表
                this.baseMapper.insert(costApplyPurchase);
                // 获取主键id
                Integer id = costApplyPurchase.getId();

                List<CostApplyPurchaseGoods> purchaseGoodsConfirmList =
                        (List<CostApplyPurchaseGoods>) modelMap.get("purchaseGoodsConfirmList");
                // 填充到详情中
                for (CostApplyPurchaseGoods purchaseGoods : purchaseGoodsConfirmList) {
                    purchaseGoods.setAid(id);
                    purchaseGoodsMapper.insert(purchaseGoods);
                }
                List<CostApplyPurchaseGoods> purchaseGoodsInquiryList =
                        (List<CostApplyPurchaseGoods>) modelMap.get("purchaseGoodsInquiryList");
                if (purchaseGoodsInquiryList != null && purchaseGoodsInquiryList.size() > 0) {
                    // 填充到详情中
                    for (CostApplyPurchaseGoods purchaseGoods : purchaseGoodsInquiryList) {
                        purchaseGoods.setAid(id);
                        purchaseGoodsMapper.insert(purchaseGoods);
                    }
                }
                break;
            case FEE_APPLY:
                CostApply costApplyFee = (CostApply) modelMap.get("costApplyFee");
                // 插入biz_reimburse_travel表
                this.baseMapper.insert(costApplyFee);

                List<CostApplyFeeDetail> feeDetailList =
                        (List<CostApplyFeeDetail>) modelMap.get("feeDetailList");
                // 填充到详情中
                for (CostApplyFeeDetail feeDetail : feeDetailList) {
                    feeDetail.setAid(costApplyFee.getId());
                    feeDetailMapper.insert(feeDetail);
                }
                break;
            case PAYMENT_APPLY:
                CostApply costApplyPayment = (CostApply) modelMap.get("costApplyPayment");
                // 插入biz_fund_cost_apply表
                this.baseMapper.insert(costApplyPayment);
                break;
            default:
                break;
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
    public void add(Map<String, Object> modelMap, Integer type, String assignee) {
        CostApply costApply = null;
        List<CostApplyPurchaseGoods> purchaseGoodsConfirmList = null;
        List<CostApplyPurchaseGoods> purchaseGoodsInquiryList = null;
        List<CostApplyFeeDetail> feeDetailList = null;
        switch(ProcessNameType.getByValue(type)) {
            case PURCHASE_APPLY:
                costApply = (CostApply) modelMap.get("costApplyPurchase");
                // 如果是被打回的申请再次提交流程就不需要再次插入数据了
                if (StringUtils.isEmpty(costApply.getId())) {
                    // 先插入biz_fund_cost_purchase表
                    this.baseMapper.insert(costApply);
                }
                purchaseGoodsConfirmList =
                        (List<CostApplyPurchaseGoods>) modelMap.get("purchaseGoodsConfirmList");
                // 先插入biz_fund_cost_purchase_goods表数据
                for (CostApplyPurchaseGoods purchaseGoods : purchaseGoodsConfirmList) {
                    if (StringUtils.isEmpty(purchaseGoods.getId())) {
                        purchaseGoods.setAid(costApply.getId());
                        purchaseGoodsMapper.insert(purchaseGoods);
                    }
                }
                purchaseGoodsInquiryList =
                        (List<CostApplyPurchaseGoods>) modelMap.get("purchaseGoodsInquiryList");
                if (purchaseGoodsInquiryList != null && purchaseGoodsInquiryList.size() > 0) {
                    // 先插入biz_fund_cost_purchase_goods表数据
                    for (CostApplyPurchaseGoods purchaseGoods : purchaseGoodsInquiryList) {
                        if (StringUtils.isEmpty(purchaseGoods.getId())) {
                            purchaseGoods.setAid(costApply.getId());
                            purchaseGoodsMapper.insert(purchaseGoods);
                        }
                    }
                }
                break;
            case FEE_APPLY:
                costApply = (CostApply) modelMap.get("costApplyFee");
                // 如果是被打回的申请再次提交流程就不需要再次插入数据了
                if (StringUtils.isEmpty(costApply.getId())) {
                    // 先插入biz_fund_cost_fee表
                    this.baseMapper.insert(costApply);
                }
                feeDetailList =
                        (List<CostApplyFeeDetail>) modelMap.get("feeDetailList");
                // 先插入biz_fund_cost_fee_detail表数据
                for (CostApplyFeeDetail feeDetail : feeDetailList) {
                    if (StringUtils.isEmpty(feeDetail.getId())) {
                        feeDetail.setAid(costApply.getId());
                        feeDetailMapper.insert(feeDetail);
                    }
                }
                break;
            case PAYMENT_APPLY:
                costApply = (CostApply) modelMap.get("costApplyPayment");
                // 如果是被打回的申请再次提交流程就不需要再次插入数据了
                if (StringUtils.isEmpty(costApply.getId())) {
                    // 先插入biz_fund_cost_fee表
                    this.baseMapper.insert(costApply);
                }
                break;
            default:
                break;
        }

        // 设置流程开始的参数
        HashMap<String, Object> map = new HashMap<>();
        // 涉及流程审批人设置
        String position = ShiroKit.getUser().getPosition();
        map.put("reqno", costApply.getReqno());
        map.put("createtime", costApply.getCreatetime());
        map.put("type", costApply.getType());
        map.put("taskUserId", position);
        map.put("taskUserName", ShiroKit.getUser().getName());
        map.put("id", costApply.getId());
        map.put("assignee", assignee);
        ProcessInstance process = null;
        switch(ProcessNameType.getByValue(type)) {
            case PURCHASE_APPLY:
                process = runtimeService.startProcessInstanceByKey(applyProcessDefinitionId, map);
                break;
            case FEE_APPLY:
                process = runtimeService.startProcessInstanceByKey(feeProcessDefinitionId, map);
                break;
            case PAYMENT_APPLY:
                process = runtimeService.startProcessInstanceByKey(paymentProcessDefinitionId, map);
                break;
            default:
                break;
        }
        String processId = process.getId();
        // 回填数据
        costApply.setProcessId(processId);
        // 直接审批
        List<Task> list = null;
        switch(ProcessNameType.getByValue(type)) {
            case PURCHASE_APPLY:
                list = taskService.createTaskQuery()
                        .taskAssignee(position)
                        .processDefinitionKey(APPLY_DEFINITION_KEY)
                        .processInstanceId(processId)
                        .orderByTaskCreateTime().desc()
                        .list();
                break;
            case FEE_APPLY:
                list = taskService.createTaskQuery()
                        .taskAssignee(position)
                        .processDefinitionKey(FEE_DEFINITION_KEY)
                        .processInstanceId(processId)
                        .orderByTaskCreateTime().desc()
                        .list();
                break;
            case PAYMENT_APPLY:
                list = taskService.createTaskQuery()
                        .taskAssignee(position)
                        .processDefinitionKey(PAYMENT_DEFINITION_KEY)
                        .processInstanceId(processId)
                        .orderByTaskCreateTime().desc()
                        .list();
                break;
            default:
                break;
        }
        if (list != null && list.size() > 0) {
            for (Task task : list) {
                map = new HashMap<>();
                map.put("submitType", "同意");
                if (assignee.equals(financePosition)) {
                    // 如果是副总和总经理的账号 并且assignee设置了为财务，为了避免财务两次审批，直接跳转到财务的节点
                    if (position.equals(ProcessAssigneeName.ASSIGNEE_DEPUTY)
                            || position.equals(ProcessAssigneeName.ASSIGNEE_MANAGER)) {
                        costApply.setState(ProcessState.CHECKING_FINANCE.getCode());
                        // 直接跳转
                        ManagementService managementService = processEngine.getManagementService();
                        managementService.executeCommand(new TaskJumpCmd(task.getId(), jumpTarget));
                    } else {
                        costApply.setState(ProcessState.CHECKING_LEADER.getCode());
                        // 通过审核
                        taskService.complete(task.getId(), map);
                    }
                } else {
                    costApply.setState(ProcessState.CHECKING_LEADER.getCode());
                    // 通过审核
                    taskService.complete(task.getId(), map);
                }
                // 更新状态 如果assignee是财务的position 那么提交的状态的就是待财务审批 否则就是待部门负责人审批
                costApply.updateById();
                switch(ProcessNameType.getByValue(type)) {
                    case PURCHASE_APPLY:
                        if (purchaseGoodsConfirmList != null && purchaseGoodsConfirmList.size() > 0) {
                            for (CostApplyPurchaseGoods purchaseGoods : purchaseGoodsConfirmList) {
                                purchaseGoods.updateById();
                            }
                        }

                        if (purchaseGoodsInquiryList != null && purchaseGoodsInquiryList.size() > 0) {
                            for (CostApplyPurchaseGoods purchaseGoods : purchaseGoodsInquiryList) {
                                purchaseGoods.updateById();
                            }
                        }
                        break;
                    case FEE_APPLY:
                        if (feeDetailList != null && feeDetailList.size() > 0) {
                            for (CostApplyFeeDetail feeDetail : feeDetailList) {
                                feeDetail.updateById();
                            }
                        }
                        break;
                    case PAYMENT_APPLY:
                        break;
                    default:
                        break;
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
    public void update(CostApply costApplyPurchase,
                       List<CostApplyPurchaseGoods> purchaseGoodsList) {
        // 修改biz_fund_cost_purchase表
        this.baseMapper.updateById(costApplyPurchase);
        // 修改biz_fund_cost_purchase_goods数据
        for (CostApplyPurchaseGoods costApplyPurchaseGoods : purchaseGoodsList) {
            if (null == costApplyPurchaseGoods.getId()) {
                costApplyPurchaseGoods.setAid(costApplyPurchase.getId());
                purchaseGoodsMapper.insert(costApplyPurchaseGoods);
            } else {
                purchaseGoodsMapper.updateById(costApplyPurchaseGoods);
            }
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
    public void updatePayment(CostApply costApply) {
        // 修改biz_fund_cost_purchase表
        this.baseMapper.updateById(costApply);
    }

    /**
     * @return void
     * @Description 保存操作
     * @Date 15:53 2018/11/30
     * @Param [reimburseConsume, reimburseConsumeDetailList]
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateFee(CostApply costApplyFee,
                       List<CostApplyFeeDetail> feeDetailList) {
        // 修改biz_fund_cost_fee表
        this.baseMapper.updateById(costApplyFee);
        // 修改biz_fund_cost_fee_detail数据
        for (CostApplyFeeDetail feeDetail : feeDetailList) {
            if (null == feeDetail.getId()) {
                feeDetail.setAid(costApplyFee.getId());
                feeDetailMapper.insert(feeDetail);
            } else {
                feeDetailMapper.updateById(feeDetail);
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
        // 删除biz_fund_cost_purchase表的数据
        this.baseMapper.deleteById(id);
        // 删除biz_fund_cost_purchase_goods表的数据
        Wrapper<CostApplyPurchaseGoods> wrapper = new EntityWrapper<>();
        wrapper.eq("aid", id);
        purchaseGoodsMapper.delete(wrapper);
    }

    /**
     * @return void
     * @Description 删除数据
     * @Date 17:27 2018/11/30
     * @Param [id]
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFee(Integer id) {
        // 删除biz_fund_cost_purchase表的数据
        this.baseMapper.deleteById(id);
        // 删除biz_fund_cost_purchase_goods表的数据
        Wrapper<CostApplyFeeDetail> wrapper = new EntityWrapper<>();
        wrapper.eq("aid", id);
        feeDetailMapper.delete(wrapper);
    }

    /**
     * @return void
     * @Description 根据id删除差旅行程数据
     * @Date 8:57 2018/12/3
     * @Param [id]
     **/
    @Override
    public void deleteDetailById(Integer id) {
        purchaseGoodsMapper.deleteById(id);
    }

    /**
     * @return void
     * @Description 根据id删除差旅行程数据
     * @Date 8:57 2018/12/3
     * @Param [id]
     **/
    @Override
    public void deleteFeeDetailById(Integer id) {
        feeDetailMapper.deleteById(id);
    }

    /**
     *@Description 删除商品采购询价内容
     *@Date 11:41 2018/12/18
     *@Param [purchaseGoodsInquiryList]
     *@return void
     **/
    @Override
    public void deleteInquiryList(List<CostApplyPurchaseGoodsDto> purchaseGoodsInquiryList) {
        for (CostApplyPurchaseGoodsDto purchaseGoodsDto : purchaseGoodsInquiryList) {
            Integer id = purchaseGoodsDto.getId();
            purchaseGoodsMapper.deleteById(id);
        }
    }

    /**
     * @return void
     * @Description 画流程信息图
     * @Date 10:01 2018/11/19
     * @Param [id]
     **/
    @Override
    public void printProcessImage(Integer id) throws IOException {
        CostApply costApplyPurchase = this.selectById(id);
        String processId = costApplyPurchase.getProcessId();
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
        List<String> definitionKeys = new ArrayList<>();
        definitionKeys.add(APPLY_DEFINITION_KEY);
        definitionKeys.add(FEE_DEFINITION_KEY);
        definitionKeys.add(PAYMENT_DEFINITION_KEY);
        List<Task> list = ProcessUtil.me().getProcessList(position, definitionKeys);
        ArrayList<Map<String, Object>> taskVos = new ArrayList<>();
        for (Task task : list) {
            String taskUserId = (String) taskService.getVariable(task.getId(), "taskUserId");
            String taskUserName = (String) taskService.getVariable(task.getId(), "taskUserName");
            Integer id = (Integer) taskService.getVariable(task.getId(), "id");
            CostApply costApply = this.baseMapper.selectById(id);
            boolean flag = false;
            if (position.equals(taskUserId)) {
                flag = false;
            } else {
                flag = true;
            }
            Map<String, Object> map = new HashMap<>();
            map.put("id", task.getId());
            map.put("processId", task.getProcessInstanceId());
            map.put("reqno", costApply.getReqno());
            map.put("deptid", costApply.getDeptid());
            map.put("location", costApply.getLocation());
            map.put("totalFee", costApply.getTotalfee());
            map.put("createTime", task.getCreateTime());
            map.put("assignee", taskUserId);
            map.put("taskUserName", taskUserName);
            map.put("type", costApply.getType());
            map.put("typename", ProcessNameType.getByValue(costApply.getType()).getMessage());
            map.put("selfFlag", flag);
            map.put("taskDefinitionKey", task.getTaskDefinitionKey());
            taskVos.add(map);
        }
        return taskVos;
    }

    /**
     * @return void
     * @Description 同意操作
     * @Date 10:51 2018/12/4
     * @Param [costApplyPurchaseDto]
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pass(CostApplyDto costApplyPurchaseDto) {
        // 执行同意的流程
        HashMap<String, Object> map = new HashMap<>();
        map.put("submitType", "同意");
        map.put("location", LocationType.prefixValueOf(costApplyPurchaseDto.getLocation()));
        String taskUser = costApplyPurchaseDto.getTaskUser();
        map.put("taskUser", taskUser);
        map.put("assigneeUser", ShiroKit.getUser().getName() + ":" + costApplyPurchaseDto.getTaskDefinitionKey());
        processHandler(costApplyPurchaseDto, map, true);
    }

    /**
     * @return void
     * @Description 驳回操作
     * @Date 10:51 2018/12/4
     * @Param [costApplyPurchaseDto]
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unPass(CostApplyDto costApplyPurchaseDto) {
        // 执行驳回的流程
        HashMap<String, Object> map = new HashMap<>();
        map.put("submitType", "驳回");
        processHandler(costApplyPurchaseDto, map, false);
    }

    /**
     *@Description 根据查询条件查询数据
     *@Date 17:12 2018/12/11
     *@Param [beginTime, endTime]
     *@return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     **/
    @Override
    public List<Map<String, Object>> selectCostApplys(Page<CostApply> page, Integer costType, Integer costState,
                                                      Integer costPay, List<Integer> ids,
                                                      String beginTime, String endTime) {
        return this.baseMapper.queryCostApply(page, costType, costState, costPay, ids, beginTime, endTime);
    }

    /**
     * @return void
     * @Description 流程同意或者驳回实际操作
     * @Date 10:57 2018/12/4
     * @Param [isPass, costApplyPurchaseDto, map]
     **/
    private void processHandler(CostApplyDto costApplyPurchaseDto, HashMap<String, Object> map, Boolean isPass) {
        // 更新审批的内容
        CostApply costApplyPurchase = this.baseMapper.selectById(costApplyPurchaseDto.getId());
        String leadernote = costApplyPurchaseDto.getLeadernote();
        if (!StringUtils.isEmpty(leadernote)) {
            costApplyPurchase.setLeadernote(leadernote);
            costApplyPurchase.setLeadertime(new Date());
        }
        String assistantnote = costApplyPurchaseDto.getAssistantnote();
        if (!StringUtils.isEmpty(assistantnote)) {
            costApplyPurchase.setAssistantnote(assistantnote);
            costApplyPurchase.setAssistantime(new Date());
        }
        String cashernote = costApplyPurchaseDto.getCashernote();
        if (!StringUtils.isEmpty(cashernote)) {
            costApplyPurchase.setCashernote(cashernote);
            costApplyPurchase.setCashertime(new Date());
        }
        String deputynote = costApplyPurchaseDto.getDeputynote();
        if (!StringUtils.isEmpty(deputynote)) {
            costApplyPurchase.setDeputynote(deputynote);
            costApplyPurchase.setDeputytime(new Date());
        }
        String managernote = costApplyPurchaseDto.getManagernote();
        if (!StringUtils.isEmpty(managernote)) {
            costApplyPurchase.setManagernote(managernote);
            costApplyPurchase.setManagertime(new Date());
        }
        //使用任务ID，查询任务对象，获取流程流程实例ID
        String taskId = costApplyPurchaseDto.getTaskId();
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
                costApplyPurchase.setState(ProcessState.PASS.getCode());
            } else {
                //审核通过如果还有记录则为经理或boss审核中
                if (activityIds.contains(ProcessTaskId.LEADER.getMessage())) {
                    costApplyPurchase.setState(ProcessState.CHECKING_LEADER.getCode());
                } else if (activityIds.contains(ProcessTaskId.ASSISTANT.getMessage())) {
                    costApplyPurchase.setState(ProcessState.CHECKING_ASSISTANT.getCode());
                } else if (activityIds.contains(ProcessTaskId.BJFINANCE.getMessage())
                        || activityIds.contains(ProcessTaskId.CQFINANCE.getMessage())
                        || activityIds.contains(ProcessTaskId.FINANCE.getMessage())) {
                    costApplyPurchase.setState(ProcessState.CHECKING_FINANCE.getCode());
                } else if (activityIds.contains(ProcessTaskId.DEPUTYMANAGER.getMessage())) {
                    costApplyPurchase.setState(ProcessState.CHECKING_DEPUTY.getCode());
                } else if (activityIds.contains(ProcessTaskId.MANAGER.getMessage())) {
                    costApplyPurchase.setState(ProcessState.CHECKING_MANAGER.getCode());
                }
            }
        } else {
            // 驳回的情况下自动设置为不通过（相当于自动撤回）的状态，相当于直接把之前的流程完毕
            if (null != InstanceId) {
                // 删除流程任务
                runtimeService.deleteProcessInstance(InstanceId, null);
            }
            costApplyPurchase.setState(ProcessState.UN_PASS.getCode());
            costApplyPurchase.setProcessId("");
        }
        costApplyPurchase.updateById();
    }
}
