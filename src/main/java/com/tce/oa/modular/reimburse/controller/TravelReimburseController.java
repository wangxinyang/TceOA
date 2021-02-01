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
import com.tce.oa.core.common.constant.state.TransportationType;
import com.tce.oa.core.common.exception.BizExceptionEnum;
import com.tce.oa.core.log.LogObjectHolder;
import com.tce.oa.core.shiro.ShiroKit;
import com.tce.oa.core.util.NumberToCNUtils;
import com.tce.oa.modular.reimburse.model.ReimburseTravel;
import com.tce.oa.modular.reimburse.model.ReimburseTravelPath;
import com.tce.oa.modular.reimburse.service.IReimburseTravelPathService;
import com.tce.oa.modular.reimburse.service.IReimburseTravelService;
import com.tce.oa.core.util.ReqNoTools;
import com.tce.oa.modular.reimburse.transfer.TravelPathDto;
import com.tce.oa.modular.reimburse.transfer.TravelReimburseDto;
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
 * 差旅费报销控制器
 *
 * @author fengshuonan
 * @Date 2018-11-29 11:14:56
 */
@Controller
@RequestMapping("/travel/reimburse")
public class TravelReimburseController extends BaseController {

    private String PREFIX = "/biz/reimburse/travel/";
    private static final String REIMBURSE_TRAVEL_PREFIX = "TR";

    @Autowired
    private IReimburseTravelService reimburseTravelService;

    @Autowired
    private IReimburseTravelPathService reimburseTravelPathService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * 跳转到差旅费报销首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "travelReimburse.html";
    }

    /**
     * 跳转到添加差旅费报销
     */
    @RequestMapping("/toAdd")
    public String toAdd() {
        return PREFIX + "travelReimburse_add.html";
    }

    /**
     * 跳转到修改差旅费报销
     */
    @RequestMapping("/toUpdate/{id}")
    public String toUpdate(@PathVariable Integer id, Model model) {
        getDetailInfoById(id, model);
        return PREFIX + "travelReimburse_edit.html";
    }

    /**
     * 跳转到差旅费报销详情页面
     */
    @RequestMapping("/toDetail/{id}")
    public String toDetail(@PathVariable Integer id, Model model) {
        getDetailInfoById(id, model);
        return PREFIX + "travelReimburse_detail.html";
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
        ReimburseTravel reimburseTravel = reimburseTravelService.selectById(id);
        // 必选是审核完成之后才能导出数据
        if (reimburseTravel != null) {
            if (reimburseTravel.getState() != ProcessState.PASS.getCode()) {
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
     * 获取差旅费报销列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        EntityWrapper<ReimburseTravel> travelEntityWrapper = new EntityWrapper<>();
        travelEntityWrapper.eq("userid", ShiroKit.getUser().getId());
        List<Map<String, Object>> selectMaps = reimburseTravelService.selectMaps(travelEntityWrapper);
        return new ReimburseWarpper(selectMaps).wrap();
    }

    /**
     * 保存差旅费报销数据，不启动工作流
     *
     */
    @RequestMapping(value = "/save")
    @ResponseBody
    public Object save(@RequestBody TravelReimburseDto travelReimburseDto) {
        Map<String, Object> modelMap = buildTravelReimBurseModel(travelReimburseDto);
        // 执行保存操作
        reimburseTravelService.save(modelMap);
        return SUCCESS_TIP;
    }


    /**
     * 新增差旅费报销
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(@RequestBody TravelReimburseDto travelReimburseDto) {
        Map<String, Object> modelMap = buildTravelReimBurseModel(travelReimburseDto);
        String assignee = travelReimburseDto.getAssignee();
        // 执行新增数据并开始工作流
        reimburseTravelService.add(modelMap, assignee);
        return SUCCESS_TIP;
    }

    /**
     * 删除差旅费报销
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer id) {
        reimburseTravelService.delete(id);
        return SUCCESS_TIP;
    }

    /**
     * 修改差旅费报销
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(@RequestBody TravelReimburseDto travelReimburseDto) {
        // 构造model的数据
        ReimburseTravel reimburseTravel = new ReimburseTravel();
        modelMapper.map(travelReimburseDto, reimburseTravel);
        // 构造path数据
        List<TravelPathDto> travelPathDtos = travelReimburseDto.getTravelPathDtos();
        List<ReimburseTravelPath> reimburseTravelPathList = new ArrayList<>();
        if (null != travelPathDtos && travelPathDtos.size() > 0) {
            for (TravelPathDto travelPathDto : travelPathDtos) {
                ReimburseTravelPath reimburseTravelPath = new ReimburseTravelPath();
                modelMapper.map(travelPathDto, reimburseTravelPath);
                reimburseTravelPathList.add(reimburseTravelPath);
            }
            // 执行保存操作
            reimburseTravelService.update(reimburseTravel, reimburseTravelPathList);
        } else {
            throw new ServiceException(BizExceptionEnum.REQUEST_PARAM_ERROR);
        }
        return SUCCESS_TIP;
    }

    /**
     * 差旅费报销详情
     */
    @RequestMapping(value = "/detail/{travelReimburseId}")
    @ResponseBody
    public Object detail(@PathVariable("travelReimburseId") Integer travelReimburseId) {
        return reimburseTravelService.selectById(travelReimburseId);
    }

    /**
     * 根据id删除差旅费报销详情
     */
    @RequestMapping(value = "/deletePathDetailById")
    @ResponseBody
    public Object deletePathDetailById(@RequestParam Integer id) {
        reimburseTravelService.deletePathDetailById(id);
        return SUCCESS_TIP;
    }

    /**
     * 查看当前流程图
     */
    @RequestMapping(value = "/showProcess/{id}")
    @ResponseBody
    public Object showProcess(@PathVariable("id") Integer id) {
        try {
            reimburseTravelService.printProcessImage(id);
        } catch (IOException e) {
            return ResponseData.error(BizExceptionEnum.FILE_READING_ERROR.getCode(), e.getMessage());
        }
        return SUCCESS_TIP;
    }


    /**
     * 获取差旅费报销详情数据
     */
    private void getDetailInfoById(@PathVariable Integer id, Model model) {
        ReimburseTravel travelReimburse = reimburseTravelService.selectById(id);
        model.addAttribute("item",travelReimburse);
        model.addAttribute("locationName", LocationType.valueOf(travelReimburse.getLocation()));
        Wrapper<ReimburseTravelPath> travelPathWrapper = new EntityWrapper<>();
        travelPathWrapper.eq("rtid", id);
        List<ReimburseTravelPath> reimburseTravelPathList = reimburseTravelPathService.selectList(travelPathWrapper);
        model.addAttribute("reimburseTravelPathList", reimburseTravelPathList);
        model.addAttribute("deptName", ConstantFactory.me().getDeptName(travelReimburse.getDeptid()));
        model.addAttribute("code", ConstantFactory.me().getProjectCode(travelReimburse.getProjectid()));
        model.addAttribute("projectName", ConstantFactory.me().getProjectName(travelReimburse.getProjectid()));
        model.addAttribute("travelMethod", TransportationType.valueOf(travelReimburse.getTravelmethod()));
        LogObjectHolder.me().set(travelReimburse);
    }

    /**
     * 构造保存和新增操作的数据的model
     *
     */
    private Map<String, Object> buildTravelReimBurseModel(@RequestBody TravelReimburseDto travelReimburseDto) {
        Map<String, Object> modelMap = new HashMap<>();
        // 构造model的数据
        ReimburseTravel reimburseTravel = new ReimburseTravel();
        modelMapper.map(travelReimburseDto, reimburseTravel);
        reimburseTravel.setCreatetime(new Date());
        // 待提交的状态
        reimburseTravel.setState(ProcessState.SUBMITING.getCode());
        reimburseTravel.setUserid(ShiroKit.getUser().getId());
        // 获取一个报销单号
        String reimburseNo = ReqNoTools.getReqNo(REIMBURSE_TRAVEL_PREFIX);
        reimburseTravel.setReimburseno(reimburseNo);
        // 金额转换大写
        BigDecimal totalfee = reimburseTravel.getTotalfee();
        String upperfee = NumberToCNUtils.number2CNMontrayUnit(totalfee);
        reimburseTravel.setUpperfee(upperfee);
        modelMap.put("reimburseTravel", reimburseTravel);

        List<TravelPathDto> travelPathDtos = travelReimburseDto.getTravelPathDtos();
        List<ReimburseTravelPath> reimburseTravelPathList = new ArrayList<>();
        if (null != travelPathDtos && travelPathDtos.size() > 0) {
            for (TravelPathDto travelPathDto : travelPathDtos) {
                ReimburseTravelPath reimburseTravelPath = new ReimburseTravelPath();
                modelMapper.map(travelPathDto, reimburseTravelPath);
                reimburseTravelPath.setCreatetime(new Date());
                reimburseTravelPathList.add(reimburseTravelPath);
            }
            modelMap.put("reimburseTravelPathList", reimburseTravelPathList);
        } else {
            throw new ServiceException(BizExceptionEnum.REQUEST_PARAM_ERROR);
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
        ReimburseTravel reimburseTravel = reimburseTravelService.selectById(id);
        // 有申请并且不是待提交状态之后就不允许删除了(2,3,4,5,6)
        if (reimburseTravel != null) {
            if (reimburseTravel.getState() == ProcessState.CHECKING_SELF.getCode()
                    || reimburseTravel.getState() == ProcessState.CHECKING_LEADER.getCode()
                    || reimburseTravel.getState() == ProcessState.CHECKING_FINANCE.getCode()
                    || reimburseTravel.getState() == ProcessState.CHECKING_DEPUTY.getCode()
                    || reimburseTravel.getState() == ProcessState.CHECKING_MANAGER.getCode()) {
                successResponseData = new SuccessResponseData(ResponseData.DEFAULT_ERROR_CODE, "申请审核流程中," + msg,
                        null);
            } else if (reimburseTravel.getState() == ProcessState.PASS.getCode()) {
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
