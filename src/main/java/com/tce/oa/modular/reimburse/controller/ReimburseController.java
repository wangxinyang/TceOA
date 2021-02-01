package com.tce.oa.modular.reimburse.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.tce.oa.core.common.constant.factory.PageFactory;
import com.tce.oa.core.common.constant.state.ProcessState;
import com.tce.oa.core.common.constant.state.ReimburseType;
import com.tce.oa.core.jxlss.JxlsUtil;
import com.tce.oa.modular.reimburse.model.ReimburseConsume;
import com.tce.oa.modular.reimburse.model.ReimburseTravel;
import com.tce.oa.modular.reimburse.service.IReimburseConsumeService;
import com.tce.oa.modular.reimburse.service.IReimburseTravelService;
import com.tce.oa.modular.reimburse.warpper.ReimburseWarpper;
import com.tce.oa.modular.system.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.text.DateFormat;
import java.util.*;

/**
 * 报销数据controller
 * @author wangxinyang
 * @version 1.0
 * @date 2018/12/6 16:59
 **/
@Controller
@RequestMapping("/reimburse/query")
public class ReimburseController {

    private static Logger logger = LoggerFactory.getLogger(ReimburseController.class);

    private String PREFIX = "/biz/reimburse/";

    @Autowired
    private IReimburseConsumeService consumeReimburseService;

    @Autowired
    private IReimburseTravelService reimburseTravelService;

    @Autowired
    private IUserService userService;

    private static final String NO_TIME = "notime";
    private static final int ZERO = 0;


    /**
     * 跳转到审批管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "reimburse.html";
    }

    /**
     * 获取审批管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String beginTime, @RequestParam(required = false) String endTime,
                       @RequestParam(required = false) Integer deptid, @RequestParam(required = false) Integer reimburseType,
                       @RequestParam(required = false) Integer reimburseState,
                       @RequestParam(required = false) String reimbursePerson) {
        // 查询用户
        List<Integer> ids = null;
        if (!StringUtils.isEmpty(reimbursePerson)) {
            ids = userService.selectUserIds(reimbursePerson);
        }

        List<Map<String, Object>> resultList = new ArrayList<>();
        // 差旅报销
        if (ReimburseType.TRAVEL.getCode() == reimburseType) {
            Page<ReimburseTravel> page = new PageFactory<ReimburseTravel>().defaultPage();
            resultList = reimburseTravelService
                    .getReimburseTravels(page, beginTime, endTime, deptid, reimburseState, ids);
        } else if (ReimburseType.CONSUME.getCode() == reimburseType) {
            // 费用报销
            Page<ReimburseConsume> page = new PageFactory<ReimburseConsume>().defaultPage();
            resultList = consumeReimburseService
                    .getReimburseConsumes(page, beginTime, endTime, deptid, reimburseState, ids);
        } else {
            Page<ReimburseTravel> page = new PageFactory<ReimburseTravel>().defaultPage();
            List<Map<String, Object>> reimburseTravels = reimburseTravelService
                    .getReimburseTravels(page, beginTime, endTime, deptid, reimburseState, ids);
            resultList.addAll(reimburseTravels);
            // 费用报销
            Page<ReimburseConsume> consumePage = new PageFactory<ReimburseConsume>().defaultPage();
            List<Map<String, Object>> reimburseConsumes = consumeReimburseService
                    .getReimburseConsumes(consumePage, beginTime, endTime, deptid, reimburseState, ids);
            resultList.addAll(reimburseConsumes);
        }
        return new ReimburseWarpper(resultList).wrap();
    }

    /**
     * 导出费用报销数据
     */
    @RequestMapping(value = "/export/{beginTime}/{endTime}/{deptid}/{reimburseType}")
    public void applyDataExport(@PathVariable(required = false) String beginTime,
                                @PathVariable(required = false) String endTime,
                                @PathVariable(required = false) Integer deptid,
                                @PathVariable(required = false) Integer reimburseType,
                                HttpServletResponse response) {
        if (NO_TIME.equals(beginTime)) {
            beginTime = null;
            endTime = null;
        }
        if (ZERO == deptid) {
            deptid = null;
        }

        List<Map<String, Object>> resultList = new ArrayList<>();
        // 差旅报销
        if (ReimburseType.TRAVEL.getCode() == reimburseType) {
            resultList = reimburseTravelService
                    .getReimburseTravels(beginTime, endTime, deptid, ProcessState.PASS.getCode(), null);
        } else if (ReimburseType.CONSUME.getCode() == reimburseType) {
            // 费用报销
            resultList = consumeReimburseService
                    .getReimburseConsumes(beginTime, endTime, deptid, ProcessState.PASS.getCode(), null);
        } else {
            List<Map<String, Object>> reimburseTravels = reimburseTravelService
                    .getReimburseTravels(beginTime, endTime, deptid, ProcessState.PASS.getCode(), null);
            resultList.addAll(reimburseTravels);
            // 费用报销
            List<Map<String, Object>> reimburseConsumes = consumeReimburseService
                    .getReimburseConsumes(beginTime, endTime, deptid, ProcessState.PASS.getCode(), null);
            resultList.addAll(reimburseConsumes);
        }
        new ReimburseWarpper(resultList).wrap();
        // 定义一个Map，往里面放入要在模板中显示数据
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("reimburseData", resultList);

        // 调用导出方法
        DateFormat df = DateFormat.getDateTimeInstance();
        String date = df.format(new Date());
        String fileName = date + ".xlsx";
        File template = JxlsUtil.getTemplate("reimburse_template.xlsx");
        JxlsUtil.export(response, model, fileName, template);
    }

}
