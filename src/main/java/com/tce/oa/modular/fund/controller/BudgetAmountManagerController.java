package com.tce.oa.modular.fund.controller;

import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.baomidou.mybatisplus.plugins.Page;
import com.tce.oa.core.common.constant.factory.ConstantFactory;
import com.tce.oa.core.common.constant.factory.PageFactory;
import com.tce.oa.core.common.constant.state.ApplyType;
import com.tce.oa.core.common.constant.state.LocationType;
import com.tce.oa.core.common.constant.state.ProcessNameType;
import com.tce.oa.core.common.exception.BizExceptionEnum;
import com.tce.oa.core.common.page.PageInfoBT;
import com.tce.oa.core.jxlss.JxlsUtil;
import com.tce.oa.core.log.LogObjectHolder;
import com.tce.oa.modular.company.service.ISubjectService;
import com.tce.oa.modular.fund.model.BudgetApply;
import com.tce.oa.modular.fund.model.BudgetApplyDetail;
import com.tce.oa.modular.fund.service.IBudgetApplyDetailService;
import com.tce.oa.modular.fund.service.IBudgetApplyService;
import com.tce.oa.modular.fund.transfer.BudgetApplyDetailDto;
import com.tce.oa.modular.fund.transfer.BudgetApplyDto;
import com.tce.oa.modular.fund.vo.BudgetApplyDetailVo;
import com.tce.oa.modular.fund.warpper.FundWarpper;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 资金额度控制器
 *
 * @author wangxy
 * @Date 2018-11-13 19:46:39
 */
@Controller
@RequestMapping("/fund/amount")
public class BudgetAmountManagerController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(BudgetAmountManagerController.class);

    private static final String NO_TIME = "notime";

    private String PREFIX = "/biz/apply/budget/amount/";

    @Autowired
    private IBudgetApplyService fundApplyService;

    @Autowired
    private ISubjectService subjectService;

    @Autowired
    private IBudgetApplyDetailService fundApplyDetailService;

    @Autowired
    private ModelMapper modelMapper;

    private static final String NO_PROJECT = "-1";

    /**
     * 跳转到资金科目首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "amount.html";
    }

    /**
     * 跳转到修改资金预算
     */
    @RequestMapping("/toUpdate/{id}/{type}")
    public String toUpdate(@PathVariable Integer id, @PathVariable Integer type, Model model) {
        return queryFundApplyData(id, type, model, "update");
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
            Map<String, Object> buildDataMap = buildDetailData(fundApplyDto);
            // 提交数据 开始流程
            fundApplyService.updateFundApplyDetail(type, buildDataMap);
            return SUCCESS_TIP;
        } else {
            throw new ServiceException(BizExceptionEnum.REQUEST_PARAM_ERROR);
        }
    }

    /**
     * 获取资金额度管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) Integer applyType,
                       @RequestParam(required = false) String beginTime,
                       @RequestParam(required = false) String endTime) {
        if (null != applyType) {
            if (ApplyType.ALL.getCode() == applyType) {
                applyType = null;
            } else if (ApplyType.SALES.getCode() == applyType) {
                applyType = ProcessNameType.SALES.getCode();
            } else if (ApplyType.TRAVEL.getCode() == applyType) {
                applyType = ProcessNameType.TRAVEL.getCode();
            } else if (ApplyType.PURCHASE.getCode() == applyType) {
                applyType = ProcessNameType.PURCHASE.getCode();
            } else if (ApplyType.OTHER.getCode() == applyType) {
                applyType = ProcessNameType.OTHER.getCode();
            }
        }
        Page<BudgetApply> page = new PageFactory<BudgetApply>().defaultPage();
        List<Map<String, Object>> applyList = fundApplyService.selectApplys(page, applyType, beginTime, endTime);
        page.setRecords(new FundWarpper(applyList).wrap());
        return new PageInfoBT<>(page);
    }

    /**
     * 导出费用报销数据
     */
    @RequestMapping(value = "/export/{beginTime}/{endTime}")
    public void applyDataExport(@PathVariable(required = false) String beginTime,
                                  @PathVariable(required = false) String endTime,
                                  HttpServletResponse response) {
        // 没有输入时间的时候 默认查询当月的数据
        if (NO_TIME.equals(beginTime)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            beginTime = sdf.format(new Date());
            endTime = sdf.format(new Date());
        }
        // 查询目前的科目的ids
        List<Integer> subjectIds = subjectService.queryIdByExport();
        // 查询重庆本月申请的ids
        List<Integer> cqIds = fundApplyService.queryApplysCqForExport(beginTime, endTime);
        List<BudgetApplyDetailVo> fundApplyDetailVoList = new ArrayList<>();
        if (cqIds != null && cqIds.size() > 0) {
            // 根据id查询出详情
            List<BudgetApplyDetail> cqDetailList = fundApplyDetailService.queryDetailByIds(cqIds);
            HashMap<Integer, BudgetApplyDetailVo> tempMap = new HashMap<>();
            collectExportData(cqDetailList, tempMap);
            for (Integer subjectId : tempMap.keySet()) {
                bindExportData(fundApplyDetailVoList, tempMap, subjectId);
            }
        }

        // 查询北京本月申请的ids
        List<Integer> bjIds = fundApplyService.queryApplysBjForExport(beginTime, endTime);
        List<BudgetApplyDetailVo> fundApplyDetailVoListByBj = new ArrayList<>();
        if (bjIds != null && bjIds.size() > 0) {
            // 根据id查询出详情
            List<BudgetApplyDetail> bjDetailList = fundApplyDetailService.queryDetailByIds(bjIds);
            HashMap<Integer, BudgetApplyDetailVo> tempMapBj = new HashMap<>();
            collectExportData(bjDetailList, tempMapBj);
            for (Integer subjectId : tempMapBj.keySet()) {
                bindExportData(fundApplyDetailVoListByBj, tempMapBj, subjectId);
            }
        }
        // 定义一个Map，往里面放入要在模板中显示数据
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("cqApply", fundApplyDetailVoList);
        model.put("bjApply", fundApplyDetailVoListByBj);

        // 调用导出方法
        DateFormat df = DateFormat.getDateTimeInstance();
        String date = df.format(new Date());
        String fileName = date + ".xlsx";
        File template = JxlsUtil.getTemplate("apply_template.xlsx");
        JxlsUtil.export(response, model, fileName, template);
    }

    /**
     *@Description 计算执行率以及设置值
     *@Date 9:58 2018/12/14
     *@Param [fundApplyDetailVoListByBj, tempMapBj, subjectId]
     *@return void
     **/
    private void bindExportData(List<BudgetApplyDetailVo> fundApplyDetailVoListByBj, HashMap<Integer, BudgetApplyDetailVo> tempMapBj, Integer subjectId) {
        BudgetApplyDetailVo fundApplyDetailVoBj = tempMapBj.get(subjectId);
        BigDecimal actualfee = fundApplyDetailVoBj.getActualfee();
        BigDecimal totalfee = fundApplyDetailVoBj.getTotalfee();
        if (actualfee != null) {
            BigDecimal ratio = actualfee.divide(totalfee, 2, BigDecimal.ROUND_HALF_DOWN);
            fundApplyDetailVoBj.setRatio(String.valueOf(ratio));
        }
        fundApplyDetailVoListByBj.add(fundApplyDetailVoBj);
    }

    /**
     * @return void
     * @Description 组装需要导出的数据
     * @Date 16:35 2018/12/12
     * @Param [detailList, tempMap]
     **/
    private void collectExportData(List<BudgetApplyDetail> detailList, HashMap<Integer, BudgetApplyDetailVo> tempMap) {
        for (BudgetApplyDetail fundApplyDetail : detailList) {
            BudgetApplyDetailVo fundApplyDetailVo = new BudgetApplyDetailVo();
            Integer subjectid = fundApplyDetail.getSubjectid();
            // 如果科目是一样的 那金额要相加
            if (tempMap.containsKey(subjectid)) {
                BigDecimal totalFee = tempMap.get(subjectid).getTotalfee();
                totalFee = totalFee.add(fundApplyDetail.getTotalfee());
                fundApplyDetailVo.setTotalfee(totalFee);
                BigDecimal approvefee = tempMap.get(subjectid).getApprovefee();
                if (approvefee != null) {
                    approvefee = approvefee.add(fundApplyDetail.getApprovefee());
                }
                fundApplyDetailVo.setApprovefee(approvefee);
                BigDecimal actualfee = tempMap.get(subjectid).getActualfee();
                if (actualfee != null) {
                    actualfee = actualfee.add(fundApplyDetail.getActualfee());
                }
                fundApplyDetailVo.setActualfee(actualfee);
                fundApplyDetailVo.setSubjectname(ConstantFactory.me().getSubjectName(fundApplyDetail.getSubjectid()));
                tempMap.put(subjectid, fundApplyDetailVo);
            } else {
                modelMapper.map(fundApplyDetail, fundApplyDetailVo);
                fundApplyDetailVo.setSubjectname(ConstantFactory.me().getSubjectName(fundApplyDetail.getSubjectid()));
                tempMap.put(subjectid, fundApplyDetailVo);
            }
        }
    }


    /**
     * dto数据转为bean
     *
     * @param fundApplyDto
     * @return
     */
    private Map<String, Object> buildDetailData(BudgetApplyDto fundApplyDto) {
        Map<String, Object> modelMap = new HashMap<>();
        List<BudgetApplyDetailDto> fundApplyDtos = fundApplyDto.getFundApplyDetailDtos();
        List<BudgetApplyDetail> fundApplyDetailList = new ArrayList<>();
        if (null != fundApplyDtos && fundApplyDtos.size() > 0) {
            for (BudgetApplyDetailDto fundApplyDetailDto : fundApplyDtos) {
                BudgetApplyDetail fundApplyDetail = new BudgetApplyDetail();
                modelMapper.map(fundApplyDetailDto, fundApplyDetail);
                fundApplyDetail.setCreatetime(new Date());
                fundApplyDetailList.add(fundApplyDetail);
            }
            modelMap.put("fundApplyDetailList", fundApplyDetailList);
        } else {
            throw new ServiceException(BizExceptionEnum.REQUEST_PARAM_ERROR);
        }
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
        model.addAttribute("deptname", ConstantFactory.me().getDeptName(fundApply.getDeptid()));
        model.addAttribute("locationName", LocationType.valueOf(fundApply.getLocation()));
        model.addAttribute("type", type);
        // 根据申请的类型不同,获取不同的详情数据
        List<BudgetApplyDetailVo> fundApplyDetails = new ArrayList<>();
        List<BudgetApplyDetail> fundApplyDetailList = fundApplyDetailService.listByAid(fundApplyId);
        for (BudgetApplyDetail fundApplyDetail : fundApplyDetailList) {
            BudgetApplyDetailVo fundApplyDetailVo = new BudgetApplyDetailVo();
            modelMapper.map(fundApplyDetail, fundApplyDetailVo);
            fundApplyDetailVo.setSubjectname(ConstantFactory.me().getSubjectName(fundApplyDetail.getSubjectid()));
            String projectid = fundApplyDetail.getProjectid();
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
                return PREFIX + "amount_sales_detail.html";
            case TRAVEL:
                return PREFIX + "amount_travel_detail.html";
            case PURCHASE:
                return PREFIX + "amount_purchase_detail.html";
            case OTHER:
                return PREFIX + "amount_other_detail.html";
            default:
                throw new ServiceException(BizExceptionEnum.REQUEST_PARAM_ERROR);
        }
    }
}
