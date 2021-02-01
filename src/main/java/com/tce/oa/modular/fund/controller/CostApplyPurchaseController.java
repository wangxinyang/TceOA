package com.tce.oa.modular.fund.controller;

import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.roses.core.reqres.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.tce.oa.core.common.constant.factory.ConstantFactory;
import com.tce.oa.core.common.constant.state.LocationType;
import com.tce.oa.core.common.constant.state.ProcessNameType;
import com.tce.oa.core.common.constant.state.ProcessState;
import com.tce.oa.core.common.exception.BizExceptionEnum;
import com.tce.oa.core.jxlss.JxlsUtil;
import com.tce.oa.core.shiro.ShiroKit;
import com.tce.oa.core.util.ReqNoTools;
import com.tce.oa.modular.fund.model.CostApply;
import com.tce.oa.modular.fund.model.CostApplyPurchaseGoods;
import com.tce.oa.modular.fund.service.ICostApplyPurchaseGoodsService;
import com.tce.oa.modular.fund.service.ICostApplyService;
import com.tce.oa.modular.fund.transfer.CostApplyDto;
import com.tce.oa.modular.fund.transfer.CostApplyPurchaseGoodsDto;
import com.tce.oa.modular.fund.vo.CostPurchaseDetailVo;
import com.tce.oa.modular.fund.warpper.FundWarpper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * 费用申请控制器
 *
 * @author wxy
 * @Date 2018-12-17 11:13:24
 */
@Controller
@RequestMapping("/cost/apply/purchase")
public class CostApplyPurchaseController extends BaseController {

    private String PREFIX = "/biz/apply/cost/purchase/";

    @Autowired
    private ICostApplyService costApplyService;

    @Autowired
    private ICostApplyPurchaseGoodsService costApplyPurchaseGoodsService;

    @Autowired
    private ModelMapper modelMapper;

    private static final String COST_APPLY_PURCHASE_PREFIX = "CAP";

    /**
     * 跳转到费用申请首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "costPurchaseApply.html";
    }

    /**
     * 跳转到添加费用申请
     */
    @RequestMapping("/to_add")
    public String toAdd() {
        return PREFIX + "costPurchaseApply_add.html";
    }

    /**
     * 跳转到资金预算详情页
     */
    @RequestMapping("/to_detail/{id}")
    public String toDetail(@PathVariable Integer id, Model model) {
        getDetailInfoById(id, model);
        return PREFIX + "costPurchaseApply_detail.html";
    }


    /**
     * 跳转到修改费用申请
     */
    @RequestMapping("/to_update/{id}")
    public String toUpdate(@PathVariable Integer id, Model model) {
        getDetailInfoById(id, model);
        return PREFIX + "costPurchaseApply_edit.html";
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
        CostApply costApplyPurchase = costApplyService.selectById(id);
        // 必选是审核完成之后才能导出数据
        if (costApplyPurchase != null) {
            if (costApplyPurchase.getState() != ProcessState.PASS.getCode()) {
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
        wrapper.eq("type", ProcessNameType.PURCHASE_APPLY.getCode());
        List<Map<String, Object>> selectMaps = costApplyService.selectMaps(wrapper);
        return new FundWarpper(selectMaps).wrap();
    }


    /**
     * 保存业务费用资金预算 不直接发起flowable的流程
     */
    @RequestMapping(value = "/save")
    @ResponseBody
    public Object save(@RequestBody CostApplyDto applyPurchaseDto) {
        Map<String, Object> modelMap = buildModel(applyPurchaseDto);
        costApplyService.save(modelMap, ProcessNameType.PURCHASE_APPLY.getCode());
        return SUCCESS_TIP;
    }

    /**
     * 新增费用申请
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(@RequestBody CostApplyDto applyPurchaseDto) {
        Map<String, Object> modelMap = buildModel(applyPurchaseDto);
        costApplyService.add(modelMap, ProcessNameType.PURCHASE_APPLY.getCode(), applyPurchaseDto.getAssignee());
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
     * 根据id删除详情
     */
    @RequestMapping(value = "/deleteGoodsById")
    @ResponseBody
    public Object deleteDetailById(@RequestParam Integer id) {
        costApplyService.deleteDetailById(id);
        return SUCCESS_TIP;
    }

    /**
     * 删除商品询价详情
     */
    @RequestMapping(value = "/deleteGoods")
    @ResponseBody
    public Object deleteGoods(@RequestBody CostApplyDto applyPurchaseDto) {
        List<CostApplyPurchaseGoodsDto> purchaseGoodsInquiryList = applyPurchaseDto.getPurchaseGoodsInquiryList();
        costApplyService.deleteInquiryList(purchaseGoodsInquiryList);
        return SUCCESS_TIP;
    }

    /**
     * 修改费用申请
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(@RequestBody CostApplyDto applyPurchaseDto) {
        CostApply costApplyPurchase = new CostApply();
        modelMapper.map(applyPurchaseDto, costApplyPurchase);
        List<CostApplyPurchaseGoodsDto> confirmDtos = applyPurchaseDto.getPurchaseGoodsConfirmList();
        List<CostApplyPurchaseGoodsDto> inquiryDtos = applyPurchaseDto.getPurchaseGoodsInquiryList();
        List<CostApplyPurchaseGoods> confirmList = new ArrayList<>();

        BigDecimal totalFee = new BigDecimal(0);
        if (null != confirmDtos && confirmDtos.size() > 0) {
            for (CostApplyPurchaseGoodsDto confirmDto : confirmDtos) {
                // 更新总价
                totalFee = totalFee.add(confirmDto.getTotalfee());

                CostApplyPurchaseGoods purchaseGoods = new CostApplyPurchaseGoods();
                modelMapper.map(confirmDto, purchaseGoods);
                confirmList.add(purchaseGoods);
            }
        } else {
            throw new ServiceException(BizExceptionEnum.REQUEST_PARAM_ERROR);
        }
        if (null != inquiryDtos && inquiryDtos.size() > 0) {
            for (CostApplyPurchaseGoodsDto inquiryDto : inquiryDtos) {
                CostApplyPurchaseGoods purchaseGoods = new CostApplyPurchaseGoods();
                modelMapper.map(inquiryDto, purchaseGoods);
                confirmList.add(purchaseGoods);
            }
        }
        costApplyPurchase.setTotalfee(totalFee);
        // 执行保存操作
        costApplyService.update(costApplyPurchase, confirmList);
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
    public void purchaseDataExport(@PathVariable Integer id, HttpServletResponse response) {
        CostApply costApplyPurchase = costApplyService.selectById(id);
        Wrapper<CostApplyPurchaseGoods> wrapper = new EntityWrapper<>();
        wrapper.eq("aid", costApplyPurchase.getId());
        List<CostApplyPurchaseGoods> purchaseGoodsList = costApplyPurchaseGoodsService.selectList(wrapper);
        List<CostPurchaseDetailVo> purchaseGoodsConifrmVoList = new ArrayList<>();
        List<CostPurchaseDetailVo> purchaseGoodsInquiryVoList = new ArrayList<>();
        int index = 0;
        for (int i=0; i<purchaseGoodsList.size(); i++) {
            index = i + 1;
            CostPurchaseDetailVo costPurchaseDetailVo = new CostPurchaseDetailVo();
            modelMapper.map(purchaseGoodsList.get(i), costPurchaseDetailVo);
            costPurchaseDetailVo.setIndex(index);
            if (costPurchaseDetailVo.getSupplier() != null) {
                purchaseGoodsConifrmVoList.add(costPurchaseDetailVo);
            } else {
                purchaseGoodsInquiryVoList.add(costPurchaseDetailVo);
            }
        }
        // 定义一个Map，往里面放入要在模板中显示数据
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("deptname", ConstantFactory.me().getDeptName(costApplyPurchase.getDeptid()));
        model.put("username", ConstantFactory.me().getUserNameById(costApplyPurchase.getUserid()));
        model.put("reqno", costApplyPurchase.getReqno());
        model.put("applytime", costApplyPurchase.getApplytime());
        model.put("projectname", ConstantFactory.me().getProjectName(costApplyPurchase.getProjectid()));
        model.put("code", ConstantFactory.me().getProjectCode(costApplyPurchase.getProjectid()));
        model.put("use", costApplyPurchase.getUse());
        model.put("purchaseGoodsConifrmDetail", purchaseGoodsConifrmVoList);
        model.put("purchaseGoodsInquiryDetail", purchaseGoodsInquiryVoList);
        model.put("totalfee", costApplyPurchase.getTotalfee());
        model.put("leadernote", costApplyPurchase.getLeadernote() == null ? "同意" : costApplyPurchase.getLeadernote());
        model.put("assistantnote", costApplyPurchase.getAssistantnote() == null ? "同意" : costApplyPurchase.getAssistantnote());
        model.put("cashernote", costApplyPurchase.getCashernote() == null ? "同意" : costApplyPurchase.getCashernote());
        Integer location = costApplyPurchase.getLocation();
        if (location == LocationType.CQ.getCode()) {
            model.put("deputynote", costApplyPurchase.getDeputynote() == null ? "" : costApplyPurchase.getDeputynote());
        } else {
            model.put("deputynote", costApplyPurchase.getDeputynote() == null ? "同意" : costApplyPurchase.getDeputynote());
        }
        model.put("managernote", costApplyPurchase.getManagernote() == null ? "同意" : costApplyPurchase.getManagernote());

        // 调用导出方法
        String fileName = costApplyPurchase.getReqno() + ".xlsx";
        File template = JxlsUtil.getTemplate("purchase_template.xlsx");
        JxlsUtil.export(response, model, fileName, template);
    }

    /**
     * 获取详情数据
     */
    private void getDetailInfoById(Integer id, Model model) {
        CostApply costApplyPurchase = costApplyService.selectById(id);

        model.addAttribute("deptname", ConstantFactory.me().getDeptName(costApplyPurchase.getDeptid()));
        model.addAttribute("locationName", LocationType.valueOf(costApplyPurchase.getLocation()));
        model.addAttribute("projectName", ConstantFactory.me().getProjectName(costApplyPurchase.getProjectid()));
        model.addAttribute("code", ConstantFactory.me().getProjectCode(costApplyPurchase.getProjectid()));
        Wrapper<CostApplyPurchaseGoods> wrapper = new EntityWrapper<>();
        wrapper.eq("aid", id);
        List<CostPurchaseDetailVo> purchaseGoodsConfirmList = new ArrayList<>();
        List<CostPurchaseDetailVo> purchaseGoodsInquiryList = new ArrayList<>();
        List<CostApplyPurchaseGoods> goods = costApplyPurchaseGoodsService.selectList(wrapper);
        for (CostApplyPurchaseGoods purchaseGoods : goods) {
            CostPurchaseDetailVo purchaseDetailVo = new CostPurchaseDetailVo();
            modelMapper.map(purchaseGoods, purchaseDetailVo);
            // 区分确认报价数据还是询价数据
            if (purchaseGoods.getSupplier() != null) {
                purchaseGoodsConfirmList.add(purchaseDetailVo);
            } else {
                purchaseGoodsInquiryList.add(purchaseDetailVo);
            }
        }
        model.addAttribute("item", costApplyPurchase);
        model.addAttribute("purchaseGoodsConfirmList", purchaseGoodsConfirmList);
        model.addAttribute("purchaseGoodsInquiryList", purchaseGoodsInquiryList);
    }

    /**
     * 构造保存和新增操作的数据的model
     *
     */
    private Map<String, Object> buildModel(CostApplyDto applyPurchaseDto) {
        Map<String, Object> modelMap = new HashMap<>();
        // 构造model的数据
        CostApply costApplyPurchase = new CostApply();
        modelMapper.map(applyPurchaseDto, costApplyPurchase);
        costApplyPurchase.setCreatetime(new Date());
        // 待提交的状态
        costApplyPurchase.setState(ProcessState.SUBMITING.getCode());
        costApplyPurchase.setUserid(ShiroKit.getUser().getId());
        // 获取一个申请单号
        String reqNo = ReqNoTools.getReqNo(COST_APPLY_PURCHASE_PREFIX);
        costApplyPurchase.setReqno(reqNo);
        costApplyPurchase.setType(ProcessNameType.PURCHASE_APPLY.getCode());

        // 转换详情数据
        BigDecimal totalFee = new BigDecimal(0);
        List<CostApplyPurchaseGoodsDto> purchaseGoodsDtoConfirmList = applyPurchaseDto.getPurchaseGoodsConfirmList();
        List<CostApplyPurchaseGoods> purchaseGoodsConfirmList = new ArrayList<>();
        // 获取总共的报销金额
        if (null != purchaseGoodsDtoConfirmList && purchaseGoodsDtoConfirmList.size() > 0) {
            for (CostApplyPurchaseGoodsDto purchaseGoodsDto : purchaseGoodsDtoConfirmList) {
                CostApplyPurchaseGoods costApplyPurchaseGoods = new CostApplyPurchaseGoods();
                modelMapper.map(purchaseGoodsDto, costApplyPurchaseGoods);
                totalFee = totalFee.add(purchaseGoodsDto.getTotalfee());
                purchaseGoodsConfirmList.add(costApplyPurchaseGoods);
            }
            modelMap.put("purchaseGoodsConfirmList", purchaseGoodsConfirmList);
        } else {
            throw new ServiceException(BizExceptionEnum.REQUEST_PARAM_ERROR);
        }
        costApplyPurchase.setTotalfee(totalFee);
        modelMap.put("costApplyPurchase", costApplyPurchase);

        List<CostApplyPurchaseGoodsDto> purchaseGoodsDtoInquiryList = applyPurchaseDto.getPurchaseGoodsInquiryList();
        List<CostApplyPurchaseGoods> purchaseGoodsInquiryList = new ArrayList<>();
        // 获取总共的报销金额
        if (null != purchaseGoodsDtoInquiryList && purchaseGoodsDtoInquiryList.size() > 0) {
            for (CostApplyPurchaseGoodsDto purchaseGoodsDto : purchaseGoodsDtoInquiryList) {
                CostApplyPurchaseGoods costApplyPurchaseGoods = new CostApplyPurchaseGoods();
                modelMapper.map(purchaseGoodsDto, costApplyPurchaseGoods);
                purchaseGoodsInquiryList.add(costApplyPurchaseGoods);
            }
            modelMap.put("purchaseGoodsInquiryList", purchaseGoodsInquiryList);
        }
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
        CostApply costApplyPurchase = costApplyService.selectById(id);
        // 有申请并且不是待提交状态之后就不允许删除了(2,3,4,5,6)
        if (costApplyPurchase != null) {
            if (costApplyPurchase.getState() == ProcessState.CHECKING_LEADER.getCode()
                    || costApplyPurchase.getState() == ProcessState.CHECKING_ASSISTANT.getCode()
                    || costApplyPurchase.getState() == ProcessState.CHECKING_FINANCE.getCode()
                    || costApplyPurchase.getState() == ProcessState.CHECKING_DEPUTY.getCode()
                    || costApplyPurchase.getState() == ProcessState.CHECKING_MANAGER.getCode()) {
                successResponseData = new SuccessResponseData(ResponseData.DEFAULT_ERROR_CODE, "申请审核流程中," + msg,
                        null);
            } else if (costApplyPurchase.getState() == ProcessState.PASS.getCode()) {
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
