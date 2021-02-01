package com.tce.oa.core.listener;

import com.tce.oa.core.common.constant.state.ProcessAssigneeName;
import com.tce.oa.core.common.constant.state.ProcessTaskId;
import com.tce.oa.core.common.handler.MailSendHandler;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.SequenceFlow;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 报销发送邮件
 */
@Component(value = "mailListener")
public class MailTaskListener implements ExecutionListener {

    @Resource(name = "defaultThreadPool")
    private ThreadPoolTaskExecutor executor;

    @Autowired
    private MailSendHandler mailSendHandler;

    @Override
    public void notify(DelegateExecution execution) {
        FlowElement currentFlowElement = execution.getCurrentFlowElement();
        String targetRef = ((SequenceFlow) currentFlowElement).getTargetRef();
        String assignee = (String)execution.getVariable("assignee");
        if (targetRef.equals(ProcessTaskId.LEADER.getMessage())) {

        } else if (targetRef.equals(ProcessTaskId.ASSISTANT.getMessage())) {
            assignee = ProcessAssigneeName.ASSIGNEE_ASSISANT;
        } else if (targetRef.equals(ProcessTaskId.CQFINANCE.getMessage())
                || targetRef.equals(ProcessTaskId.FINANCE.getMessage())) {
            // 财务
            assignee = ProcessAssigneeName.ASSIGNEE_FINANCE;
        } else if (targetRef.equals(ProcessTaskId.BJFINANCE.getMessage())) {
            // 财务
            assignee = ProcessAssigneeName.ASSIGNEE_FINANCE_BJ;
        } else if (targetRef.equals(ProcessTaskId.DEPUTYMANAGER.getMessage())) {
            // 副总
            assignee = ProcessAssigneeName.ASSIGNEE_DEPUTY;
        } else if (targetRef.equals(ProcessTaskId.MANAGER.getMessage())) {
            // 总经理
            assignee = ProcessAssigneeName.ASSIGNEE_MANAGER;
        }
        String reqno = (String)execution.getVariable("reqno");
        Integer type = (Integer)execution.getVariable("type");
        String usrname = (String)execution.getVariable("taskUserName");
        Date date = (Date)execution.getVariable("createtime");
        mailSendHandler.sendTaskMail(assignee, reqno, usrname, type, date);
    }
}
