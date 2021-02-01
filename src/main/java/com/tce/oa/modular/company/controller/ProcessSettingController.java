package com.tce.oa.modular.company.controller;

import cn.stylefeng.roses.core.base.controller.BaseController;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.tce.oa.core.common.constant.factory.ConstantFactory;
import com.tce.oa.modular.company.model.ProcessSetting;
import com.tce.oa.modular.company.service.IProcessSettingService;
import com.tce.oa.modular.company.vo.ProcessUserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * 流程相关设置控制器
 * @author wangxinyang
 * @version 1.0
 * @date 2018/11/28 18:22
 **/
@Controller
@RequestMapping("/process/setting")
public class ProcessSettingController extends BaseController {

    private String PREFIX = "/biz/company/project/";

    @Autowired
    private IProcessSettingService processSettingService;


    /**
     * 获取流程关联user的列表
     */
    @RequestMapping(value = "/users")
    @ResponseBody
    public List<ProcessUserVo> users(@RequestParam String processId) {
        List<ProcessUserVo> processUserVoList = new ArrayList<>();
        Wrapper<ProcessSetting> processSettingWrapper = new EntityWrapper<>();
        processSettingWrapper.eq("pid", processId);
        List<ProcessSetting> processSettingList = processSettingService.selectList(processSettingWrapper);
        for (ProcessSetting processSetting : processSettingList) {
            // 处理查询出的userid
            String userid = processSetting.getUserid();
            String[] userids = userid.split(",", -1);
            for (String str : userids) {
                ProcessUserVo processUserVo = new ProcessUserVo();
                processUserVo.setPosition(ConstantFactory.me().getUserPositionById(Integer.parseInt(str)));
                processUserVo.setUsername(ConstantFactory.me().getUserNameById(Integer.parseInt(str)));
                processUserVoList.add(processUserVo);
            }
        }
        return processUserVoList;
    }
}
