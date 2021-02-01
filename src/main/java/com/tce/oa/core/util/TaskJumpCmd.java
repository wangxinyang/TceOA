package com.tce.oa.core.util;

import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.Process;
import org.flowable.common.engine.impl.interceptor.Command;
import org.flowable.common.engine.impl.interceptor.CommandContext;
import org.flowable.engine.FlowableEngineAgenda;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityManager;
import org.flowable.engine.impl.util.CommandContextUtil;
import org.flowable.engine.impl.util.ProcessDefinitionUtil;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;
import org.flowable.task.service.impl.persistence.entity.TaskEntityManager;

/**
 * @author wangxinyang
 * @version 1.0
 * @date 2018/11/21 15:16
 **/
public class TaskJumpCmd implements Command {

    protected String taskId;
    protected String target;

    public TaskJumpCmd(String taskId, String target) {
        this.taskId = taskId;
        this.target = target;
    }

    @Override
    public Object execute(CommandContext commandContext) {
        ExecutionEntityManager executionEntityManager = CommandContextUtil.getExecutionEntityManager(commandContext);
        TaskEntityManager taskEntityManager = org.flowable.task.service.impl.util.CommandContextUtil.getTaskEntityManager();
        TaskEntity taskEntity = taskEntityManager.findById(taskId);
        ExecutionEntity executionEntity = executionEntityManager.findById(taskEntity.getExecutionId());
        Process process = ProcessDefinitionUtil.getProcess(executionEntity.getProcessDefinitionId());
        FlowElement targetFlowElement = process.getFlowElement(target);
        executionEntity.setCurrentFlowElement(targetFlowElement);
        FlowableEngineAgenda flowableEngineAgenda = CommandContextUtil.getAgenda();
        flowableEngineAgenda.planContinueProcessInCompensation(executionEntity);
        taskEntityManager.delete(taskId);
        return null;
    }
}
