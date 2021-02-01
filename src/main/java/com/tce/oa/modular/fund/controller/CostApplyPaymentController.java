package com.tce.oa.modular.fund.controller;


import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.roses.core.reqres.response.SuccessResponseData;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tce.oa.core.common.constant.factory.ConstantFactory;
import com.tce.oa.core.common.constant.state.LocationType;
import com.tce.oa.core.common.constant.state.ProcessNameType;
import com.tce.oa.core.common.constant.state.ProcessState;
import com.tce.oa.core.common.exception.BizExceptionEnum;
import com.tce.oa.core.jxlss.JxlsUtil;
import com.tce.oa.core.shiro.ShiroKit;
import com.tce.oa.core.util.NumberToCNUtils;
import com.tce.oa.core.util.ReqNoTools;
import com.tce.oa.modular.fund.model.CostApply;
import com.tce.oa.modular.fund.service.ICostApplyService;
import com.tce.oa.modular.fund.transfer.CostApplyDto;
import com.tce.oa.modular.fund.warpper.FundWarpper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 费用申请控制器
 *
 * @author wxy
 * @Date 2018-12-17 11:13:24
 */
@Controller
@RequestMapping("/cost/apply/payment")
public class CostApplyPaymentController extends BaseController {

    private String PREFIX = "/biz/apply/cost/payment/";

    @Autowired
    private ICostApplyService costApplyService;

    @Autowired
    private ModelMapper modelMapper;

    private static final String COST_APPLY_COMMON_PREFIX = "CAPA";
    /**
     * 跳转到费用申请首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "costPaymentApply.html";
    }

    /**
     * 跳转到添加费用申请
     */
    @RequestMapping("/to_add")
    public String toAdd() {
        return PREFIX + "costPaymentApply_add.html";
    }

    /**
     * 跳转到资金预算详情页
     */
    @RequestMapping("/to_detail/{id}")
    public String toDetail(@PathVariable Integer id, Model model) {
        getDetailInfoById(id, model);
        return PREFIX + "costPaymentApply_detail.html";
    }

    /**
     * 跳转到修改费用申请
     */
    @RequestMapping("/to_update/{id}")
    public String toUpdate(@PathVariable Integer id, Model model) {
        getDetailInfoById(id, model);
        return PREFIX + "costPaymentApply_edit.html";
    }

    /**
     * 判断是否能修改选中的申请
     */
    @RequestMapping("/can/update")
    @ResponseBody
    public Object canUpdate(@RequestParam Integer id) {
        return canUpdateOrDelete(id, "不能修改申请");
    }

    /**
     * 判断是否能删除选中的申请
     */
    @RequestMapping("/can/delete")
    @ResponseBody
    public Object canDelete(@RequestParam Integer id) {
        return canUpdateOrDelete(id, "不能删除申请");
    }

    /**
     * 判断是否能导出选中的申请
     */
    @RequestMapping("/can/export")
    @ResponseBody
    public Object canExport(@RequestParam Integer id) {
        SuccessResponseData successResponseData = null;
        // 查询申请的信息
        CostApply costApply = costApplyService.selectById(id);
        // 必选是审核完成之后才能导出数据
        if (costApply != null) {
            if (costApply.getState() != ProcessState.PASS.getCode()) {
                successResponseData = new SuccessResponseData(ResponseData.DEFAULT_ERROR_CODE, "申请审核未完成, 不能导出数据",
                        null);
            } else {
                successResponseData = new SuccessResponseData();
            }
        } else {
            successResponseData = new SuccessResponseData(ResponseData.DEFAULT_ERROR_CODE, "没有可以导出的数据", null);
        }
        return successResponseData;
    }

    /**
     * 获取费用申请列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        EntityWrapper<CostApply> wrapper = new EntityWrapper<>();
        wrapper.eq("userid", ShiroKit.getUser().getId());
        wrapper.eq("type", ProcessNameType.PAYMENT_APPLY.getCode());
        List<Map<String, Object>> selectMaps = costApplyService.selectMaps(wrapper);
        return new FundWarpper(selectMaps).wrap();
    }

    /**
     * 新增费用申请
     */
    @RequestMapping(value = "/save")
    @ResponseBody
    public Object save(@RequestBody CostApplyDto paymentDto) {
        Map<String, Object> modelMap = buildModel(paymentDto);
        costApplyService.save(modelMap, ProcessNameType.PAYMENT_APPLY.getCode());
        return SUCCESS_TIP;
    }


    /**
     * 新增费用申请
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(@RequestBody CostApplyDto paymentDto) {
        Map<String, Object> modelMap = buildModel(paymentDto);
        costApplyService.add(modelMap, ProcessNameType.PAYMENT_APPLY.getCode(), paymentDto.getAssignee());
        return SUCCESS_TIP;
    }

    /**
     * 删除费用申请
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer id) {
        costApplyService.delete(id);
        return SUCCESS_TIP;
    }

    /**
     * 修改费用申请
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(@RequestBody CostApplyDto paymentDto) {
        CostApply costApply = new CostApply();
        modelMapper.map(paymentDto, costApply);
        costApply.setTotalfee(paymentDto.getPrice());
        costApplyService.updatePayment(costApply);
        return SUCCESS_TIP;
    }

    /**
     * 费用申请详情
     */
    @RequestMapping(value = "/detail/{id}")
    @ResponseBody
    public Object detail(@PathVariable("id") Integer id) {
        return costApplyService.selectById(id);
    }

    /**
     * 查看当前流程图
     */
    @RequestMapping(value = "/showProcess/{id}")
    @ResponseBody
    public Object showProcess(@PathVariable("id") Integer id) {
        try {
            costApplyService.printProcessImage(id);
        } catch (IOException e) {
            return ResponseData.error(BizExceptionEnum.FILE_READING_ERROR.getCode(), e.getMessage());
        }
        return SUCCESS_TIP;
    }

    /**
     * 导出采购费用申请数据
     */
    @RequestMapping(value = "/export/{id}")
    public void feeDataExport(@PathVariable Integer id, HttpServletResponse response) {
        CostApply costApply = costApplyService.selectById(id);
        // 定义一个Map，往里面放入要在模板中显示数据
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("deptname", ConstantFactory.me().getDeptName(costApply.getDeptid()));
        model.put("username", ConstantFactory.me().getUserNameById(costApply.getUserid()));
        model.put("reqno", costApply.getReqno());
        model.put("applytime", costApply.getApplytime());
        model.put("receivingunit", costApply.getReceivingunit());
        model.put("bank", costApply.getBank());
        model.put("account", costApply.getAccount());
        model.put("deadline", costApply.getDeadline());
        model.put("reason", costApply.getReason());
        model.put("upperfee", NumberToCNUtils.number2CNMontrayUnit(costApply.getPrice()));
        model.put("price", costApply.getPrice());
        model.put("leadernote", costApply.getLeadernote() == null ? "同意" : costApply.getLeadernote());
        model.put("cashernote", costApply.getCashernote() == null ? "同意" : costApply.getCashernote());
        Integer location = costApply.getLocation();
        if (location == LocationType.CQ.getCode()) {
            model.put("deputynote", costApply.getDeputynote() == null ? "" : costApply.getDeputynote());
        } else {
            model.put("deputynote", costApply.getDeputynote() == null ? "同意" : costApply.getDeputynote());
        }
        model.put("managernote", costApply.getManagernote() == null ? "同意" : costApply.getManagernote());

        // 调用导出方法
        String fileName = costApply.getReqno() + ".xlsx";
        File template = JxlsUtil.getTemplate("payment_template.xlsx");
        JxlsUtil.export(response, model, fileName, template);
    }

    /**
     * 获取详情数据
     */
    private void getDetailInfoById(Integer id, Model model) {
        CostApply costApply = costApplyService.selectById(id);
        model.addAttribute("item", costApply);
        model.addAttribute("deptname", ConstantFactory.me().getDeptName(costApply.getDeptid()));
        model.addAttribute("locationName", LocationType.valueOf(costApply.getLocation()));
    }

    /**
     * 构造保存和新增操作的数据的model
     *
     */
    private Map<String, Object> buildModel(CostApplyDto paymentDto) {
        Map<String, Object> modelMap = new HashMap<>();
        // 构造model的数据
        CostApply costApplyPayment = new CostApply();
        modelMapper.map(paymentDto, costApplyPayment);
        costApplyPayment.setCreatetime(new Date());
        // 待提交的状态
        costApplyPayment.setState(ProcessState.SUBMITING.getCode());
        costApplyPayment.setUserid(ShiroKit.getUser().getId());
        // 获取一个申请单号
        String reqNo = ReqNoTools.getReqNo(COST_APPLY_COMMON_PREFIX);
        costApplyPayment.setReqno(reqNo);
        costApplyPayment.setType(ProcessNameType.PAYMENT_APPLY.getCode());
        // 金额转换大写
//        BigDecimal totalfee = reimburseConsume.getTotalfee();
//        String upperfee = NumberToCNUtils.number2CNMontrayUnit(totalfee);
//        reimburseConsume.setUpperfee(upperfee);
        costApplyPayment.setTotalfee(paymentDto.getPrice());
        modelMap.put("costApplyPayment", costApplyPayment);
        return modelMap;
    }

    /**
     * @return cn.stylefeng.roses.core.reqres.response.SuccessResponseData
     * @Description 判断是否能够修改或者删除申请
     * @Date 17:18 2018/11/20
     * @Param [fundApplyId, s]
     **/
    private SuccessResponseData canUpdateOrDelete(Integer id, String msg) {
        SuccessResponseData successResponseData = null;
        // 查询申请的信息
        CostApply costApply = costApplyService.selectById(id);
        // 有申请并且不是待提交状态之后就不允许删除了(2,3,4,5,6)
        if (costApply != null) {
            if (costApply.getState() == ProcessState.CHECKING_LEADER.getCode()
                    || costApply.getState() == ProcessState.CHECKING_FINANCE.getCode()
                    || costApply.getState() == ProcessState.CHECKING_DEPUTY.getCode()
                    || costApply.getState() == ProcessState.CHECKING_MANAGER.getCode()) {
                successResponseData = new SuccessResponseData(ResponseData.DEFAULT_ERROR_CODE, "申请审核流程中," + msg,
                        null);
            } else if (costApply.getState() == ProcessState.PASS.getCode()) {
                successResponseData = new SuccessResponseData(ResponseData.DEFAULT_ERROR_CODE, "申请审核已完成," + msg,
                        null);
            } else {
                // 1,7,9就可以修改或者删除
                successResponseData = new SuccessResponseData();
            }
        } else {
            successResponseData = new SuccessResponseData(ResponseData.DEFAULT_ERROR_CODE, "没有可以操作的数据", null);
        }
        return successResponseData;
    }
}
