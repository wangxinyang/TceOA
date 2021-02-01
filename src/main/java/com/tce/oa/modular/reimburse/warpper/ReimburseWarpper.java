package com.tce.oa.modular.reimburse.warpper;

import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.plugins.Page;
import com.tce.oa.core.common.constant.factory.ConstantFactory;
import com.tce.oa.core.common.constant.state.LocationType;
import com.tce.oa.core.common.constant.state.ProcessNameType;
import com.tce.oa.core.common.constant.state.ProcessState;

import java.util.List;
import java.util.Map;

/**
 * @author wangxinyang
 * @version 1.0
 * @date 2018/11/15 15:46
 **/
public class ReimburseWarpper extends BaseControllerWrapper {

    private static final String REIMBURSE_TRAVEL_PREFIX = "TR";
    private static final String REIMBURSE_COMMON_PREFIX = "CR";

    public ReimburseWarpper(Map<String, Object> single) {
        super(single);
    }

    public ReimburseWarpper(List<Map<String, Object>> multi) {
        super(multi);
    }

    public ReimburseWarpper(Page<Map<String, Object>> page) {
        super(page);
    }

    public ReimburseWarpper(PageResult<Map<String, Object>> pageResult) {
        super(pageResult);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
        // 部门
        map.put("deptname", ConstantFactory.me().getDeptName((Integer) map.get("deptid")));
        // 状态
        Integer state = (Integer) map.get("state");
        map.put("statename", ProcessState.valueOf(state));
        // 地区
        Integer location = (Integer) map.get("location");
        map.put("locationame", LocationType.valueOf(location));
        // 项目
        map.put("projectname", ConstantFactory.me().getProjectName((Integer) map.get("projetid")));
        map.put("user", ConstantFactory.me().getUserNameById((Integer) map.get("userid")));
        // 单号
        String reqno = (String) map.get("reimburseno");
        if (reqno.startsWith(REIMBURSE_TRAVEL_PREFIX)) {
            map.put("typename", ProcessNameType.REIMBURSE_TRAVEL.getMessage());
            map.put("type", ProcessNameType.REIMBURSE_TRAVEL.getCode());
        } else {
            map.put("typename", ProcessNameType.REIMBURSE_COMMON.getMessage());
            map.put("type", ProcessNameType.REIMBURSE_COMMON.getCode());
        }
    }
}
