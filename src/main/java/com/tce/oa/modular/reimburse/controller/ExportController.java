package com.tce.oa.modular.reimburse.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.tce.oa.core.common.constant.factory.ConstantFactory;
import com.tce.oa.core.common.constant.state.TransportationType;
import com.tce.oa.core.jxlss.JxlsUtil;
import com.tce.oa.modular.reimburse.model.ReimburseConsume;
import com.tce.oa.modular.reimburse.model.ReimburseConsumeDetail;
import com.tce.oa.modular.reimburse.model.ReimburseTravel;
import com.tce.oa.modular.reimburse.model.ReimburseTravelPath;
import com.tce.oa.modular.reimburse.service.IReimburseConsumeDetailService;
import com.tce.oa.modular.reimburse.service.IReimburseConsumeService;
import com.tce.oa.modular.reimburse.service.IReimburseTravelPathService;
import com.tce.oa.modular.reimburse.service.IReimburseTravelService;
import com.tce.oa.modular.reimburse.vo.ConsumeDetailVo;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 报销数据导出controller
 * @author wangxinyang
 * @version 1.0
 * @date 2018/12/6 16:59
 **/
@Controller
@RequestMapping("/reimburse/export")
public class ExportController {

    private static Logger logger = LoggerFactory.getLogger(ExportController.class);

    @Autowired
    private IReimburseConsumeService consumeReimburseService;

    @Autowired
    private IReimburseConsumeDetailService consumeReimburseDetailService;

    @Autowired
    private IReimburseTravelService reimburseTravelService;

    @Autowired
    private IReimburseTravelPathService reimburseTravelPathService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * 导出费用报销数据
     */
    @RequestMapping(value = "/consume/{id}")
    public void reimburseConsumeDataExport(@PathVariable Integer id, HttpServletResponse response) {
        ReimburseConsume reimburseConsume = consumeReimburseService.selectById(id);
        Wrapper<ReimburseConsumeDetail> wrapper = new EntityWrapper<>();
        wrapper.eq("rcid", reimburseConsume.getId());
        List<ReimburseConsumeDetail> reimburseConsumeDetailList = consumeReimburseDetailService.selectList(wrapper);
        List<ConsumeDetailVo> consumeDetailVoList = new ArrayList<>();
        for (ReimburseConsumeDetail reimburseConsumeDetail : reimburseConsumeDetailList) {
            ConsumeDetailVo consumeDetailVo = new ConsumeDetailVo();
            modelMapper.map(reimburseConsumeDetail, consumeDetailVo);
            consumeDetailVo.setSubjectname(ConstantFactory.me().getSubjectName(reimburseConsumeDetail.getSubjectid()));
            consumeDetailVo.setProjectname(ConstantFactory.me().getProjectName(reimburseConsumeDetail.getProjectid()));
            consumeDetailVo.setCode(ConstantFactory.me().getProjectCode(reimburseConsumeDetail.getProjectid()));
            consumeDetailVoList.add(consumeDetailVo);
        }
        // 定义一个Map，往里面放入要在模板中显示数据
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("deptname", ConstantFactory.me().getDeptName(reimburseConsume.getDeptid()));
        model.put("username", ConstantFactory.me().getUserNameById(reimburseConsume.getUserid()));
        model.put("reimburseno", reimburseConsume.getReimburseno());
        model.put("consumeDetail", consumeDetailVoList);
        model.put("totalfee", reimburseConsume.getTotalfee());
        model.put("upperfee", reimburseConsume.getUpperfee());
        model.put("applycash", reimburseConsume.getApplycash());
        model.put("adjustcash", reimburseConsume.getAdjustcash());
        model.put("managernote", reimburseConsume.getManagernote() == null ? reimburseConsume.getCashernote() : reimburseConsume.getManagernote());

        // 调用导出方法
        String fileName = reimburseConsume.getReimburseno() + ".xlsx";
        File template = JxlsUtil.getTemplate("consume_template.xlsx");
        JxlsUtil.export(response, model, fileName, template);
    }



    /**
     * 导出差旅报销数据
     */
    @RequestMapping(value = "/travel/{id}")
    public void reimburseTravelDataExport(@PathVariable Integer id, HttpServletResponse response) {
        ReimburseTravel reimburseTravel = reimburseTravelService.selectById(id);
        Wrapper<ReimburseTravelPath> wrapper = new EntityWrapper<>();
        wrapper.eq("rtid", reimburseTravel.getId());
        List<ReimburseTravelPath> reimburseTravelPathList = reimburseTravelPathService.selectList(wrapper);
        // 定义一个Map，往里面放入要在模板中显示数据
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("deptname", ConstantFactory.me().getDeptName(reimburseTravel.getDeptid()));
        model.put("reimburseno", reimburseTravel.getReimburseno());
        String applytime = reimburseTravel.getApplytime();
        model.put("year", applytime.split("-")[0]);
        model.put("month", applytime.split("-")[1]);
        model.put("day", applytime.split("-")[2]);
        model.put("projectname", ConstantFactory.me().getProjectName(reimburseTravel.getProjectid()));
        model.put("code", ConstantFactory.me().getProjectCode(reimburseTravel.getProjectid()));
        model.put("travelreason", reimburseTravel.getTravelreason());
        model.put("accommodationfee", reimburseTravel.getAccommodationfee());
        model.put("trafficfee", reimburseTravel.getTrafficfee());
        model.put("traveldays", reimburseTravel.getTraveldays());
        model.put("subsidy", reimburseTravel.getSubsidy());
        model.put("travelmethod", TransportationType.valueOf(reimburseTravel.getTravelmethod()));
        model.put("travelfee", reimburseTravel.getTravelfee());
        model.put("othercash", reimburseTravel.getOthercash());
        model.put("totalfee", reimburseTravel.getTotalfee());
        model.put("ticketnums", reimburseTravel.getTicketnums());
        model.put("upperfee", reimburseTravel.getUpperfee());
        model.put("applycash", reimburseTravel.getApplycash());
        model.put("adjustcash", reimburseTravel.getAdjustcash());
        model.put("managernote", reimburseTravel.getManagernote() == null ? "同意" : reimburseTravel.getManagernote());
        model.put("leadernote", reimburseTravel.getLeadernote() == null ? "同意" : reimburseTravel.getLeadernote());
        model.put("cashernote", reimburseTravel.getCashernote() == null ? "同意" : reimburseTravel.getCashernote());
        model.put("username", ConstantFactory.me().getUserNameById(reimburseTravel.getUserid()));
        model.put("travelDetail", reimburseTravelPathList);
        // 调用导出方法
        String fileName = reimburseTravel.getReimburseno() + ".xlsx";
        File template = JxlsUtil.getTemplate("travel_template.xlsx");
        JxlsUtil.export(response, model, fileName, template);
    }
}
