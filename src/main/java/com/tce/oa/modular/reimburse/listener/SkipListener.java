package com.tce.oa.modular.reimburse.listener;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;

import java.util.Map;

/**
 * 设置跳过任务的listener
 * @author wangxinyang
 * @version 1.0
 * @date 2018/12/4 16:19
 **/
public class SkipListener implements ExecutionListener {
    @Override
    public void notify(DelegateExecution execution) {
        // 获取流程变量
        Map<String, Object> variables = execution.getVariables();
        // 开启支持跳过表达式"_ACTIVITI_SKIP_EXPRESSION_ENABLED"
        variables.put("_ACTIVITI_SKIP_EXPRESSION_ENABLED", true);
        // 将修改同步到流程中
        execution.setTransientVariables(variables);
    }
}
