package com.tce.oa.modular.fund.warpper;

import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.plugins.Page;
import com.tce.oa.core.common.constant.factory.ConstantFactory;
import com.tce.oa.core.common.constant.state.LocationType;
import com.tce.oa.core.common.constant.state.PaymentType;
import com.tce.oa.core.common.constant.state.ProcessNameType;
import com.tce.oa.core.common.constant.state.ProcessState;

import java.util.List;
import java.util.Map;

/**
 * @author wangxinyang
 * @version 1.0
 * @date 2018/11/15 15:46
 **/
public class FundWarpper extends BaseControllerWrapper {

    public FundWarpper(Map<String, Object> single) {
        super(single);
    }

    public FundWarpper(List<Map<String, Object>> multi) {
        super(multi);
    }

    public FundWarpper(Page<Map<String, Object>> page) {
        super(page);
    }

    public FundWarpper(PageResult<Map<String, Object>> pageResult) {
        super(pageResult);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
        map.put("deptname", ConstantFactory.me().getDeptName((Integer) map.get("deptid")));
        Integer state = (Integer) map.get("state");
        map.put("statemean", ProcessState.valueOf(state));
        Integer type = (Integer) map.get("type");
        map.put("typename", ProcessNameType.valueOf(type));
        Integer location = (Integer) map.get("location");
        map.put("location", LocationType.valueOf(location));
        map.put("user", ConstantFactory.me().getUserNameById((Integer) map.get("userid")));
        Integer pay = (Integer) map.get("pay");
        map.put("payname", PaymentType.valueOf(pay));
    }
}
