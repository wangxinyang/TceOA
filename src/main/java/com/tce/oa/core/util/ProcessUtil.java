package com.tce.oa.core.util;

import cn.stylefeng.roses.core.util.SpringContextHolder;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.*;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.task.api.Task;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wangxinyang
 * @version 1.0
 * @date 2018/12/3 16:58
 **/
@Component
@DependsOn("springContextHolder")
public class ProcessUtil {

    private RuntimeService runtimeService = SpringContextHolder.getBean(RuntimeService.class);
    private RepositoryService repositoryService = SpringContextHolder.getBean(RepositoryService.class);
    private TaskService taskService = SpringContextHolder.getBean(TaskService.class);
    private ProcessEngine processEngine = SpringContextHolder.getBean(ProcessEngine.class);

    public static ProcessUtil me() {
        return SpringContextHolder.getBean("processUtil");
    }

    public void printProcessImage(String processId) throws IOException {
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processId).singleResult();

        //流程走完的不显示图
        if (pi == null) {
            return;
        }

//        Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();

        //使用流程实例ID，查询正在执行的执行对象表，返回流程实例对象
//        String InstanceId = task.getProcessInstanceId();
        List<Execution> executions = runtimeService
                .createExecutionQuery()
                .processInstanceId(processId)
                .list();

        //得到正在执行的Activity的Id
        List<String> activityIds = new ArrayList<>();
        List<String> flows = new ArrayList<>();
        for (Execution exe : executions) {
            List<String> ids = runtimeService.getActiveActivityIds(exe.getId());
            activityIds.addAll(ids);
        }

        //获取流程图
        BpmnModel bpmnModel = repositoryService.getBpmnModel(pi.getProcessDefinitionId());
        ProcessEngineConfiguration engconf = processEngine.getProcessEngineConfiguration();
        ProcessDiagramGenerator diagramGenerator = engconf.getProcessDiagramGenerator();
        InputStream in = diagramGenerator.generateDiagram(bpmnModel, "png",
                activityIds, flows, "宋体", "宋体", "宋体",
                engconf.getClassLoader(), 1.0, true);
        OutputStream out = null;
        byte[] buf = new byte[1024];
        int legth = 0;
        try {
            out = HttpServletUtil.getResponse().getOutputStream();
            while ((legth = in.read(buf)) != -1) {
                out.write(buf, 0, legth);
            }
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }

    /**
     *@Description 获取任务的集合
     *@Date 18:33 2018/12/3
     *@Param []
     *@return java.util.List<org.flowable.task.api.Task>
     **/
    public List<Task> getProcessList(String name, String definitionKey) {
        List<Task> list = taskService.createTaskQuery()
                .taskAssignee(name)
                .processDefinitionKey(definitionKey)
                .orderByTaskCreateTime().desc()
                .list();
        return list;
    }

    /**
     *@Description 获取任务的集合
     *@Date 18:33 2018/12/3
     *@Param []
     *@return java.util.List<org.flowable.task.api.Task>
     **/
    public List<Task> getProcessList(String name, List<String> definitionKeys) {
        List<Task> list = taskService.createTaskQuery()
                .taskAssignee(name)
                .processDefinitionKeyIn(definitionKeys)
                .orderByTaskCreateTime().desc()
                .list();
        return list;
    }

    /**
     *@Description 获取正在执行的执行对象表
     *@Date 11:52 2018/11/28
     *@Param [InstanceId]
     *@return java.util.List<java.lang.String>
     **/
    public List<String> getActivityIds(String InstanceId) {
        List<Execution> executions = runtimeService
                .createExecutionQuery()
                .processInstanceId(InstanceId)
                .list();

        //得到正在执行的Activity的Id
        List<String> activityIds = new ArrayList<>();
        for (Execution exe : executions) {
            List<String> ids = runtimeService.getActiveActivityIds(exe.getId());
            activityIds.addAll(ids);
        }
        return activityIds;
    }
}
