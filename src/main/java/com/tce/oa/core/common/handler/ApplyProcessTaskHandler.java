package com.tce.oa.core.common.handler;

import com.tce.oa.core.common.constant.state.ProcessAssigneeName;
import com.tce.oa.core.common.constant.state.ProcessTaskId;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;
import org.springframework.stereotype.Component;

/**
 * 预算申请流程中任务分配
 */
@Component
public class ApplyProcessTaskHandler implements TaskListener {


    @Override
    public void notify(DelegateTask delegateTask) {
        String id = delegateTask.getTaskDefinitionKey();
        String assignee = (String)delegateTask.getVariable("assignee");
        // 部门负责人
        if (id.equals(ProcessTaskId.LEADER.getMessage())) {
            delegateTask.setAssignee(assignee);
        } else if (id.equals(ProcessTaskId.ASSISTANT.getMessage())) {
            delegateTask.setAssignee(ProcessAssigneeName.ASSIGNEE_ASSISANT);
        } else if (id.equals(ProcessTaskId.CQFINANCE.getMessage())
                || id.equals(ProcessTaskId.FINANCE.getMessage())) {
            // 财务
            delegateTask.setAssignee(ProcessAssigneeName.ASSIGNEE_FINANCE);
        } else if (id.equals(ProcessTaskId.BJFINANCE.getMessage())) {
            // 财务
            delegateTask.setAssignee(ProcessAssigneeName.ASSIGNEE_FINANCE_BJ);
        } else if (id.equals(ProcessTaskId.DEPUTYMANAGER.getMessage())) {
            // 副总
            delegateTask.setAssignee(ProcessAssigneeName.ASSIGNEE_DEPUTY);
        } else if (id.equals(ProcessTaskId.MANAGER.getMessage())) {
            // 总经理
            delegateTask.setAssignee(ProcessAssigneeName.ASSIGNEE_MANAGER);
        }
    }

}
