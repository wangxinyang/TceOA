package com.tce.oa.modular.reimburse.controller;

import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.roses.core.reqres.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.tce.oa.core.common.constant.factory.ConstantFactory;
import com.tce.oa.core.common.constant.state.LocationType;
import com.tce.oa.core.common.constant.state.ProcessState;
import com.tce.oa.core.common.exception.BizExceptionEnum;
import com.tce.oa.core.log.LogObjectHolder;
import com.tce.oa.core.shiro.ShiroKit;
import com.tce.oa.core.util.NumberToCNUtils;
import com.tce.oa.modular.reimburse.model.ReimburseConsume;
import com.tce.oa.modular.reimburse.model.ReimburseConsumeDetail;
import com.tce.oa.modular.reimburse.service.IReimburseConsumeDetailService;
import com.tce.oa.modular.reimburse.service.IReimburseConsumeService;
import com.tce.oa.core.util.ReqNoTools;
import com.tce.oa.modular.reimburse.transfer.ConsumeDetailDto;
import com.tce.oa.modular.reimburse.transfer.ConsumeReimburseDto;
import com.tce.oa.modular.reimburse.vo.ConsumeDetailVo;
import com.tce.oa.modular.reimburse.warpper.ReimburseWarpper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * 费用报销控制器
 *
 * @author fengshuonan
 * @Date 2018-11-29 11:24:11
 */
@Controller
@RequestMapping("/consume/reimburse")
public class ConsumeReimburseController extends BaseController {

    private String PREFIX = "/biz/reimburse/consume/";

    private static final String REIMBURSE_COMMON_PREFIX = "CR";

    @Autowired
    private IReimburseConsumeService consumeReimburseService;

    @Autowired
    private IReimburseConsumeDetailService consumeReimburseDetailService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * 跳转到费用报销首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "consumeReimburse.html";
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
        ReimburseConsume reimburseConsume = consumeReimburseService.selectById(id);
        // 必选是审核完成之后才能导出数据
        if (reimburseConsume != null) {
            if (reimburseConsume.getState() != ProcessState.PASS.getCode()) {
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
     * 跳转到添加费用报销
     */
    @RequestMapping("/toAdd")
    public String toAdd() {
        return PREFIX + "consumeReimburse_add.html";
    }

    /**
     * 跳转到修改差旅费报销
     */
    @RequestMapping("/toUpdate/{id}")
    public String toUpdate(@PathVariable Integer id, Model model) {
        getDetailInfoById(id, model);
        return PREFIX + "consumeReimburse_edit.html";
    }

    /**
     * 跳转到差旅费报销详情页面
     */
    @RequestMapping("/toDetail/{id}")
    public String toDetail(@PathVariable Integer id, Model model) {
        getDetailInfoById(id, model);
        return PREFIX + "consumeReimburse_detail.html";
    }

    /**
     * 获取费用报销列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        EntityWrapper<ReimburseConsume> wrapper = new EntityWrapper<>();
        wrapper.eq("userid", ShiroKit.getUser().getId());
        List<Map<String, Object>> selectMaps = consumeReimburseService.selectMaps(wrapper);
        return new ReimburseWarpper(selectMaps).wrap();
    }

    /**
     * 保存费用报销数据，不启动工作流
     *
     */
    @RequestMapping(value = "/save")
    @ResponseBody
    public Object save(@RequestBody ConsumeReimburseDto consumeReimburseDto) {
        Map<String, Object> modelMap = buildReimBurseModel(consumeReimburseDto);
        // 执行保存操作
        consumeReimburseService.save(modelMap);
        return SUCCESS_TIP;
    }

    /**
     * 新增费用报销
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(@RequestBody ConsumeReimburseDto consumeReimburseDto) {
        Map<String, Object> modelMap = buildReimBurseModel(consumeReimburseDto);
        String assignee = consumeReimburseDto.getAssignee();
        consumeReimburseService.add(modelMap, assignee);
        return SUCCESS_TIP;
    }

    /**
     * 删除费用报销
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer id) {
        consumeReimburseService.delete(id);
        return SUCCESS_TIP;
    }

    /**
     * 根据id删除差旅费报销详情
     */
    @RequestMapping(value = "/deletePathDetailById")
    @ResponseBody
    public Object deletePathDetailById(@RequestParam Integer id) {
        consumeReimburseService.deletePathDetailById(id);
        return SUCCESS_TIP;
    }

    /**
     * 修改费用报销
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(@RequestBody ConsumeReimburseDto consumeReimburseDto) {
        // 构造model的数据
        ReimburseConsume reimburseConsume = new ReimburseConsume();
        modelMapper.map(consumeReimburseDto, reimburseConsume);
        // 构造详情数据
        List<ConsumeDetailDto> consumeDetailDtos = consumeReimburseDto.getConsumeDetailDtos();
        List<ReimburseConsumeDetail> reimburseConsumeDetailList = new ArrayList<>();
        if (null != consumeDetailDtos && consumeDetailDtos.size() > 0) {
            for (ConsumeDetailDto consumeDetailDto : consumeDetailDtos) {
                ReimburseConsumeDetail reimburseConsumeDetail = new ReimburseConsumeDetail();
                modelMapper.map(consumeDetailDto, reimburseConsumeDetail);
                reimburseConsumeDetailList.add(reimburseConsumeDetail);
            }
            // 执行保存操作
            consumeReimburseService.update(reimburseConsume, reimburseConsumeDetailList);
        } else {
            throw new ServiceException(BizExceptionEnum.REQUEST_PARAM_ERROR);
        }
        return SUCCESS_TIP;
    }

    /**
     * 费用报销详情
     */
    @RequestMapping(value = "/detail/{consumeReimburseId}")
    @ResponseBody
    public Object detail(@PathVariable("consumeReimburseId") Integer consumeReimburseId) {
        return consumeReimburseService.selectById(consumeReimburseId);
    }

    /**
     * 查看当前流程图
     */
    @RequestMapping(value = "/showProcess/{id}")
    @ResponseBody
    public Object showProcess(@PathVariable("id") Integer id) {
        try {
            consumeReimburseService.printProcessImage(id);
        } catch (IOException e) {
            return ResponseData.error(BizExceptionEnum.FILE_READING_ERROR.getCode(), e.getMessage());
        }
        return SUCCESS_TIP;
    }

    /**
     * 构造保存和新增操作的数据的model
     *
     */
    private Map<String, Object> buildReimBurseModel(ConsumeReimburseDto consumeReimburseDto) {
        Map<String, Object> modelMap = new HashMap<>();
        // 构造model的数据
        ReimburseConsume reimburseConsume = new ReimburseConsume();
        modelMapper.map(consumeReimburseDto, reimburseConsume);
        reimburseConsume.setCreatetime(new Date());
        // 待提交的状态
        reimburseConsume.setState(ProcessState.SUBMITING.getCode());
        reimburseConsume.setUserid(ShiroKit.getUser().getId());
        // 获取一个报销单号
        String reimburseNo = ReqNoTools.getReqNo(REIMBURSE_COMMON_PREFIX);
        reimburseConsume.setReimburseno(reimburseNo);
        // 金额转换大写
        BigDecimal totalfee = reimburseConsume.getTotalfee();
        String upperfee = NumberToCNUtils.number2CNMontrayUnit(totalfee);
        reimburseConsume.setUpperfee(upperfee);
        modelMap.put("reimburseConsume", reimburseConsume);

        // 转换详情数据
        List<ConsumeDetailDto> consumeDetailDtos = consumeReimburseDto.getConsumeDetailDtos();
        List<ReimburseConsumeDetail> reimburseConsumeDetailList = new ArrayList<>();
        // 获取总共的报销金额
        if (null != consumeDetailDtos && consumeDetailDtos.size() > 0) {
            for (ConsumeDetailDto consumeDetailDto : consumeDetailDtos) {
                ReimburseConsumeDetail reimburseConsumeDetail = new ReimburseConsumeDetail();
                modelMapper.map(consumeDetailDto, reimburseConsumeDetail);
                reimburseConsumeDetail.setCreatetime(new Date());
                reimburseConsumeDetailList.add(reimburseConsumeDetail);
            }
            modelMap.put("reimburseConsumeDetailList", reimburseConsumeDetailList);
        } else {
            throw new ServiceException(BizExceptionEnum.REQUEST_PARAM_ERROR);
        }
        return modelMap;
    }

    /**
     * 获取差旅费报销详情数据
     */
    private void getDetailInfoById(Integer id, Model model) {
        ReimburseConsume reimburseConsume = consumeReimburseService.selectById(id);
        model.addAttribute("item", reimburseConsume);
        model.addAttribute("deptname", ConstantFactory.me().getDeptName(reimburseConsume.getDeptid()));
        model.addAttribute("locationName", LocationType.valueOf(reimburseConsume.getLocation()));
        Wrapper<ReimburseConsumeDetail> wrapper = new EntityWrapper<>();
        wrapper.eq("rcid", id);
        List<ConsumeDetailVo> consumeDetailVoList = new ArrayList<>();
        List<ReimburseConsumeDetail> reimburseConsumeDetailList = consumeReimburseDetailService.selectList(wrapper);
        for (ReimburseConsumeDetail reimburseConsumeDetail : reimburseConsumeDetailList) {
            ConsumeDetailVo consumeDetailVo = new ConsumeDetailVo();
            modelMapper.map(reimburseConsumeDetail, consumeDetailVo);
            // 手动set的值
            consumeDetailVo.setSubjectname(ConstantFactory.me().getSubjectName(reimburseConsumeDetail.getSubjectid()));
            consumeDetailVo.setProjectname(ConstantFactory.me().getProjectName(reimburseConsumeDetail.getProjectid()));
            consumeDetailVo.setCode(ConstantFactory.me().getProjectCode(reimburseConsumeDetail.getProjectid()));
            consumeDetailVoList.add(consumeDetailVo);
        }
        model.addAttribute("consumeDetailVoList", consumeDetailVoList);
        LogObjectHolder.me().set(reimburseConsume);
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
        ReimburseConsume reimburseConsume = consumeReimburseService.selectById(id);
        // 有申请并且不是待提交状态之后就不允许删除了(2,3,4,5,6)
        if (reimburseConsume != null) {
            if (reimburseConsume.getState() == ProcessState.CHECKING_SELF.getCode()
                    || reimburseConsume.getState() == ProcessState.CHECKING_LEADER.getCode()
                    || reimburseConsume.getState() == ProcessState.CHECKING_FINANCE.getCode()
                    || reimburseConsume.getState() == ProcessState.CHECKING_DEPUTY.getCode()
                    || reimburseConsume.getState() == ProcessState.CHECKING_MANAGER.getCode()) {
                successResponseData = new SuccessResponseData(ResponseData.DEFAULT_ERROR_CODE, "申请审核流程中," + msg,
                        null);
            } else if (reimburseConsume.getState() == ProcessState.PASS.getCode()) {
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
