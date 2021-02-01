package com.tce.oa.modular.fund.controller;

import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.roses.core.reqres.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tce.oa.core.common.constant.factory.ConstantFactory;
import com.tce.oa.core.common.constant.state.LocationType;
import com.tce.oa.core.common.constant.state.ProcessNameType;
import com.tce.oa.core.common.constant.state.ProcessState;
import com.tce.oa.core.common.exception.BizExceptionEnum;
import com.tce.oa.core.log.LogObjectHolder;
import com.tce.oa.core.shiro.ShiroKit;
import com.tce.oa.core.util.ReqNoTools;
import com.tce.oa.modular.fund.model.BudgetApply;
import com.tce.oa.modular.fund.model.BudgetApplyDetail;
import com.tce.oa.modular.fund.service.IBudgetApplyDetailService;
import com.tce.oa.modular.fund.service.IBudgetApplyService;
import com.tce.oa.modular.fund.transfer.BudgetApplyDetailDto;
import com.tce.oa.modular.fund.transfer.BudgetApplyDto;
import com.tce.oa.modular.fund.vo.BudgetApplyDetailVo;
import com.tce.oa.modular.fund.warpper.FundWarpper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * 资金预算控制器
 *
 * @author wangxy
 * @Date 2018-11-14 10:17:14
 */
@Controller
@RequestMapping("/fund/budget/apply")
public class BudgetApplyController extends BaseController {

    private final static String HANDLER_METHOD_DETAIL = "detail";
    private static final String APPLY_SALES_PREFIX = "AS";
    private static final String APPLY_TRAVEL_PREFIX = "AT";
    private static final String APPLY_PURCHASE_PREFIX = "AP";
    private static final String APPLY_OTHER_PREFIX = "AO";
    private static final String NO_PROJECT = "-1";

    private String PREFIX = "/biz/apply/budget/apply/";

    @Autowired
    private IBudgetApplyService fundApplyService;

    @Autowired
    private IBudgetApplyDetailService fundApplyDetailService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * 跳转到资金预算首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "fundBudgetApply.html";
    }

    /**
     * 判断是否能跳转到添加资金预算
     */
    @RequestMapping("/can/add")
    @ResponseBody
    public Object fundApplyCanAdd(@RequestParam Integer type) {
//        SuccessResponseData successResponseData = null;
//        // 查询在本月内是否已经有提交或者保存的申请了
//        FundApply fundApply = fundApplyService.queryCanAdd();
//        if (fundApply != null) {
//            successResponseData = new SuccessResponseData(ResponseData.DEFAULT_ERROR_CODE, "本月已有预算申请,不能再新增",
//                    null);
//        } else {
//            successResponseData = new SuccessResponseData();
//        }
//        return successResponseData;
        return SUCCESS_TIP;
    }

    /**
     * 判断是否能修改选中的申请
     */
    @RequestMapping("/can/update")
    @ResponseBody
    public Object fundApplyCanUpdate(@RequestParam Integer fundApplyId) {
        SuccessResponseData successResponseData = getUpdOrDelInfo(fundApplyId, "不能修改申请");
        return successResponseData;
    }

    /**
     * 判断是否能删除选中的申请
     */
    @RequestMapping("/can/delete")
    @ResponseBody
    public Object fundApplyCanDelete(@RequestParam Integer fundApplyId) {
        SuccessResponseData successResponseData = getUpdOrDelInfo(fundApplyId, "不能删除申请");
        return successResponseData;
    }


    /**
     * 跳转到添加资金预算
     */
    @RequestMapping("/add/{type}")
    public String add(@PathVariable Integer type) {
        switch (ProcessNameType.getByValue(type)) {
            case SALES:
                return PREFIX + "fundBudgetApplySales_add.html";
            case TRAVEL:
                return PREFIX + "fundBudgetApplyTravel_add.html";
            case PURCHASE:
                return PREFIX + "fundBudgetApplyPurchase_add.html";
            case OTHER:
                return PREFIX + "fundBudgetApplyOther_add.html";
            default:
                return PREFIX + "fundBudgetApplySales_add.html";
        }
    }


    /**
     * 跳转到资金预算详情页
     */
    @RequestMapping("/toDetail/{fundApplyId}/{type}")
    public String toDetail(@PathVariable Integer fundApplyId, @PathVariable Integer type, Model model) {
        return queryFundApplyData(fundApplyId, type, model, "detail");
    }

    /**
     * 跳转到修改资金预算
     */
    @RequestMapping("/toUpdate/{fundApplyId}/{type}")
    public String toUpdate(@PathVariable Integer fundApplyId, @PathVariable Integer type, Model model) {
        return queryFundApplyData(fundApplyId, type, model, "update");
    }

    /**
     * 获取资金预算列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        EntityWrapper<BudgetApply> fundApplyEntityWrapper = new EntityWrapper<>();
        fundApplyEntityWrapper.eq("userid", ShiroKit.getUser().getId());
        List<Map<String, Object>> selectMaps = fundApplyService.selectMaps(fundApplyEntityWrapper);
        return new FundWarpper(selectMaps).wrap();
    }


    /**
     * 保存业务费用资金预算 不直接发起flowable的流程
     */
    @RequestMapping(value = "/save")
    @ResponseBody
    public Object save(@RequestBody BudgetApplyDto fundApplyDto) {
        if (fundApplyDto != null) {
            Integer type = fundApplyDto.getType();
            // 转换业务预算数据
            Map<String, Object> buildDataMap = buildDto2Bean(fundApplyDto, false);
            // 保存数据
            fundApplyService.saveFundApply(type, buildDataMap);
            return SUCCESS_TIP;
        } else {
            throw new ServiceException(BizExceptionEnum.REQUEST_PARAM_ERROR);
        }
    }

    /**
     * 保存业务费用资金预算 直接发起flowable的流程
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(@RequestBody BudgetApplyDto fundApplyDto) {
        if (fundApplyDto != null) {
            Integer type = fundApplyDto.getType();
            // 转换业务预算数据
            Map<String, Object> buildDataMap = buildDto2Bean(fundApplyDto, false);
            // 提交数据 开始流程
            fundApplyService.addFundApply(type, buildDataMap);
            return SUCCESS_TIP;
        } else {
            throw new ServiceException(BizExceptionEnum.REQUEST_PARAM_ERROR);
        }
    }

    /**
     * 查看当前流程图
     */
    @RequestMapping(value = "/showFundSalesProcess/{fundApplyId}")
    @ResponseBody
    public Object showFundSalesProcess(@PathVariable("fundApplyId") Integer fundApplyId) {
        try {
            fundApplyService.printProcessImage(fundApplyId);
        } catch (IOException e) {
            return ResponseData.error(BizExceptionEnum.FILE_READING_ERROR.getCode(), e.getMessage());
        }
        return SUCCESS_TIP;
    }

    /**
     * 删除资金预算所有信息
     */
    @RequestMapping(value = "/delete")
    @Transactional
    @ResponseBody
    public Object delete(@RequestParam Integer fundApplyId, Integer type) {
        fundApplyService.deleteData(fundApplyId, type);
        return SUCCESS_TIP;
    }

    /**
     * 删除资金预算详情数据根据id
     */
    @RequestMapping(value = "/deleteDetailById")
    @ResponseBody
    public Object deleteDetailById(@RequestParam Integer id) {
        fundApplyDetailService.deleteById(id);
        return SUCCESS_TIP;
    }


    /**
     * 修改资金预算
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(@RequestBody BudgetApplyDto fundApplyDto) {
        if (fundApplyDto != null) {
            Integer type = fundApplyDto.getType();
            // 转换业务预算数据
            Map<String, Object> buildDataMap = buildDto2Bean(fundApplyDto, true);
            // 提交数据 开始流程
            fundApplyService.updateFundApply(type, buildDataMap);
            return SUCCESS_TIP;
        } else {
            throw new ServiceException(BizExceptionEnum.REQUEST_PARAM_ERROR);
        }
    }

    /**
     * 资金预算详情
     */
    @RequestMapping(value = "/detail/{fundApplyId}")
    @ResponseBody
    public Object detail(@PathVariable("id") Integer fundApplyId) {
//        Wrapper<FundApplyDetailSales> wrapper = new EntityWrapper<>();
//        wrapper.eq("aid", fundApplyId);
//        wrapper.orderBy("id", true);
//        .selectList(wrapper);
        return fundApplyService.selectById(fundApplyId);
    }


    /**
     * @return cn.stylefeng.roses.core.reqres.response.SuccessResponseData
     * @Description 判断是否能够修改或者删除申请
     * @Date 17:18 2018/11/20
     * @Param [fundApplyId, s]
     **/
    private SuccessResponseData getUpdOrDelInfo(Integer fundApplyId, String msg) {
        SuccessResponseData successResponseData = null;
        // 查询在本月内是否已经有提交或者保存的申请了
        BudgetApply fundApply = fundApplyService.selectById(fundApplyId);
        // 有申请并且不是待提交状态之后就不允许删除了
        if (fundApply != null) {
            if (fundApply.getState() == ProcessState.CHECKING_ASSISTANT.getCode()
                    || fundApply.getState() == ProcessState.CHECKING_DEPUTY.getCode()
                    || fundApply.getState() == ProcessState.CHECKING_MANAGER.getCode()
                    || fundApply.getState() == ProcessState.CHECKING_SELF.getCode()) {
                successResponseData = new SuccessResponseData(ResponseData.DEFAULT_ERROR_CODE, "申请审核流程中," + msg,
                        null);
            } else if (fundApply.getState() == ProcessState.PASS.getCode()) {
                successResponseData = new SuccessResponseData(ResponseData.DEFAULT_ERROR_CODE, "申请审核已完成," + msg,
                        null);
            } else {
                successResponseData = new SuccessResponseData();
            }
        } else {
            successResponseData = new SuccessResponseData();
        }
        return successResponseData;
    }

    /**
     * dto数据转为bean
     *
     * @param fundApplyDto
     * @return
     */
    private Map<String, Object> buildDto2Bean(BudgetApplyDto fundApplyDto, Boolean isUpdate) {
        Map<String, Object> modelMap = new HashMap<>();
        // 构造FundApply数据
        BudgetApply fundApply = new BudgetApply();
        modelMapper.map(fundApplyDto, fundApply);
        fundApply.setCreatetime(new Date());
        // 待提交的状态
        fundApply.setState(ProcessState.SUBMITING.getCode());
        fundApply.setUserid(ShiroKit.getUser().getId());
        String reimburseNo = "";
        BigDecimal countCash = new BigDecimal(0);
        // 转换详情数据
        if (!isUpdate) {
            // 获取一个申请单号
            switch (ProcessNameType.getByValue(fundApplyDto.getType())) {
                case SALES:
                    reimburseNo = ReqNoTools.getReqNo(APPLY_SALES_PREFIX);
                    break;
                case TRAVEL:
                    reimburseNo = ReqNoTools.getReqNo(APPLY_TRAVEL_PREFIX);
                    break;
                case PURCHASE:
                    reimburseNo = ReqNoTools.getReqNo(APPLY_PURCHASE_PREFIX);
                    break;
                case OTHER:
                    reimburseNo = ReqNoTools.getReqNo(APPLY_OTHER_PREFIX);
                    break;
                default:
                    throw new ServiceException(BizExceptionEnum.REQUEST_PARAM_ERROR);
            }
            fundApply.setReqno(reimburseNo);
        }
        List<BudgetApplyDetailDto> fundApplyDetailDtos = fundApplyDto.getFundApplyDetailDtos();
        List<BudgetApplyDetail> fundApplyDetailList = new ArrayList<>();
        if (null != fundApplyDetailDtos && fundApplyDetailDtos.size() > 0) {
            for (BudgetApplyDetailDto fundApplyDetailDto : fundApplyDetailDtos) {
                BudgetApplyDetail fundApplyDetail = new BudgetApplyDetail();
                modelMapper.map(fundApplyDetailDto, fundApplyDetail);
                fundApplyDetail.setCreatetime(new Date());
                fundApplyDetailList.add(fundApplyDetail);
                // 统计金额
                countCash = countCash.add(fundApplyDetail.getTotalfee());
            }
            modelMap.put("fundApplyDetailList", fundApplyDetailList);
        } else {
            throw new ServiceException(BizExceptionEnum.REQUEST_PARAM_ERROR);
        }
        fundApply.setMoney(countCash);
        modelMap.put("fundApply", fundApply);
        return modelMap;
    }

    /**
     * 根据id获取预算详情
     *
     * @param fundApplyId
     * @param model
     */
    private String queryFundApplyData(Integer fundApplyId, Integer type, Model model, String handler) {
        BudgetApply fundApply = fundApplyService.selectById(fundApplyId);
        model.addAttribute("aid", fundApply.getId());
        model.addAttribute("deptid", fundApply.getDeptid());
        model.addAttribute("deptname", ConstantFactory.me().getDeptName(fundApply.getDeptid()));
        model.addAttribute("location", fundApply.getLocation());
        model.addAttribute("locationName", LocationType.valueOf(fundApply.getLocation()));
        model.addAttribute("state", fundApply.getState());
        model.addAttribute("type", type);
        // 根据申请的类型不同,获取不同的详情数据
        List<BudgetApplyDetailVo> fundApplyDetails = new ArrayList<>();
        List<BudgetApplyDetail> fundApplyDetailSalesList = fundApplyDetailService.listByAid(fundApplyId);
        for (BudgetApplyDetail fundApplyDetailSales : fundApplyDetailSalesList) {
            BudgetApplyDetailVo fundApplyDetailVo = new BudgetApplyDetailVo();
            modelMapper.map(fundApplyDetailSales, fundApplyDetailVo);
            fundApplyDetailVo.setSubjectname(ConstantFactory.me().getSubjectName(fundApplyDetailSales.getSubjectid()));
            String projectid = fundApplyDetailSales.getProjectid();
            if (NO_PROJECT.equals(projectid)) {
                fundApplyDetailVo.setProjectname("无");
            } else {
                String[] projectids = projectid.split(":");
                fundApplyDetailVo.setProjectname(ConstantFactory.me().getProjectName(Integer.parseInt(projectids[0])));
            }
            fundApplyDetails.add(fundApplyDetailVo);
        }
        model.addAttribute("fundApplys", fundApplyDetails);
        LogObjectHolder.me().set(fundApplyDetails);
        switch (ProcessNameType.getByValue(type)) {
            case SALES:
                if (HANDLER_METHOD_DETAIL.equals(handler)) {
                    return PREFIX + "fundBudgetApplySales_detail.html";
                } else {
                    return PREFIX + "fundBudgetApplySales_edit.html";
                }
            case TRAVEL:
                if (HANDLER_METHOD_DETAIL.equals(handler)) {
                    return PREFIX + "fundBudgetApplyTravel_detail.html";
                } else {
                    return PREFIX + "fundBudgetApplyTravel_edit.html";
                }
            case PURCHASE:
                if (HANDLER_METHOD_DETAIL.equals(handler)) {
                    return PREFIX + "fundBudgetApplyPurchase_detail.html";
                } else {
                    return PREFIX + "fundBudgetApplyPurchase_edit.html";
                }
            case OTHER:
                if (HANDLER_METHOD_DETAIL.equals(handler)) {
                    return PREFIX + "fundBudgetApplyOther_detail.html";
                } else {
                    return PREFIX + "fundBudgetApplyOther_edit.html";
                }
            default:
                throw new ServiceException(BizExceptionEnum.REQUEST_PARAM_ERROR);
        }
    }

}
