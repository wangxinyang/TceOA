package com.tce.oa.modular.fund.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 预算申请表
 * </p>
 *
 * @author wangxy123
 * @since 2018-11-14
 */
@TableName("biz_fund_apply")
public class BudgetApply extends Model<BudgetApply> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 部门id
     */
    private Integer deptid;
    private String reqno;
    /**
     * 地区: 1.重庆 2.北京
     */
    private Integer location;

    /**
     * 总计
     */
    private BigDecimal money;
    /**
     * 创建时间
     */
    private Date createtime;

    /**
     * 类型: 1.业务费用  2:差旅费用  3.采购费用 4:其它费用
     */
    private Integer type;
    /**
     * 状态: 1.待提交  2:待审核  3.审核通过 4:驳回
     */
    private Integer state;
    /**
     * 用户id
     */
    private Integer userid;
    /**
     * 流程定义id
     */
    private String processId;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDeptid() {
        return deptid;
    }

    public void setDeptid(Integer deptid) {
        this.deptid = deptid;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public Integer getLocation() {
        return location;
    }

    public void setLocation(Integer location) {
        this.location = location;
    }

    public String getReqno() {
        return reqno;
    }

    public void setReqno(String reqno) {
        this.reqno = reqno;
    }

    @Override
    public String toString() {
        return "FundApply{" +
                "id=" + id +
                ", deptid=" + deptid +
                ", reqno='" + reqno + '\'' +
                ", location=" + location +
                ", money=" + money +
                ", createtime=" + createtime +
                ", type=" + type +
                ", state=" + state +
                ", userid=" + userid +
                ", processId='" + processId + '\'' +
                '}';
    }
}
