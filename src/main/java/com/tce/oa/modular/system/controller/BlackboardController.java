/**
 * Copyright 2018-2020 stylefeng & fengshuonan (https://gitee.com/stylefeng)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tce.oa.modular.system.controller;

import cn.stylefeng.roses.core.base.controller.BaseController;
import com.tce.oa.core.shiro.ShiroKit;
import com.tce.oa.modular.system.service.INoticeService;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

/**
 * 总览信息
 *
 * @author fengshuonan
 * @Date 2017年3月4日23:05:54
 */
@Controller
@RequestMapping("/blackboard")
public class BlackboardController extends BaseController {

    @Autowired
    private INoticeService noticeService;


    @Autowired
    private TaskService taskService;

    /**
     * 跳转到黑板
     */
    @RequestMapping("")
    public String blackboard(Model model) {
        List<Map<String, Object>> notices = noticeService.list(null);
        String position = ShiroKit.getUser().getPosition();
        List<Task> taskList = taskService.createTaskQuery()
                .taskAssignee(position)
                .orderByTaskCreateTime()
                .desc()
                .list();
        model.addAttribute("noticeList", notices);
        model.addAttribute("task", taskList.size());
        return "/blackboard.html";
    }
}
