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
 * 采购申请表
 * </p>
 *
 * @author wangxy123
 * @since 2018-12-17
 */
@TableName("biz_fund_cost_apply")
public class CostApply extends Model<CostApply> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 申请单号
     */
    private String reqno;
    /**
     * 部门id
     */
    private Integer deptid;
    /**
     * 项目id
     */
    private Integer projectid;
    /**
     * 类型: 1.重庆  2:北京
     */
    private Integer location;
    private Integer pay;
    /**
     * 申请时间
     */
    private String applytime;
    /**
     * 用途
     */
    private String use;
    /**
     * 总计
     */
    private BigDecimal totalfee;
    private BigDecimal countfee;
    /**
     * 收款单位
     */
    private String receivingunit;
    /**
     * 开户行
     */
    private String bank;
    /**
     * 账号
     */
    private String account;
    /**
     * 截止日期
     */
    private String deadline;
    /**
     * 申请事由
     */
    private String reason;
    /**
     * 金额小写
     */
    private BigDecimal price;
    /**
     * 金额大写
     */
    private String upperfee;
    private Integer type;
    /**
     * 创建时间
     */
    private Date createtime;
    /**
     * 状态: 1.待提交 2:部门负责人审核 3:待总经理助理审核  4.待财务审核 5.待副总审核 6:待总经理审核  7:审核未通过 8:审核通过
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
    /**
     * 选择的下级审批人
     */
    private String assignee;
    /**
     * 部门负责人备注说明
     */
    private String leadernote;
    /**
     * 部门负责人审批时间
     */
    private Date leadertime;
    /**
     * 总助备注说明
     */
    private String assistantnote;
    /**
     * 行政审批时间
     */
    private Date assistantime;
    /**
     * 财务备注说明
     */
    private String cashernote;
    /**
     * 财务审批时间
     */
    private Date cashertime;
    /**
     * 副总备注说明
     */
    private String deputynote;
    /**
     * 副总审批时间
     */
    private Date deputytime;
    /**
     * 总经理备注说明
     */
    private String managernote;
    /**
     * 总经理审批时间
     */
    private Date managertime;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReqno() {
        return reqno;
    }

    public void setReqno(String reqno) {
        this.reqno = reqno;
    }

    public Integer getDeptid() {
        return deptid;
    }

    public void setDeptid(Integer deptid) {
        this.deptid = deptid;
    }

    public Integer getProjectid() {
        return projectid;
    }

    public void setProjectid(Integer projectid) {
        this.projectid = projectid;
    }

    public Integer getLocation() {
        return location;
    }

    public void setLocation(Integer location) {
        this.location = location;
    }

    public String getApplytime() {
        return applytime;
    }

    public void setApplytime(String applytime) {
        this.applytime = applytime;
    }

    public String getUse() {
        return use;
    }

    public void setUse(String use) {
        this.use = use;
    }

    public BigDecimal getTotalfee() {
        return totalfee;
    }

    public void setTotalfee(BigDecimal totalfee) {
        this.totalfee = totalfee;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
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

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getLeadernote() {
        return leadernote;
    }

    public void setLeadernote(String leadernote) {
        this.leadernote = leadernote;
    }

    public Date getLeadertime() {
        return leadertime;
    }

    public void setLeadertime(Date leadertime) {
        this.leadertime = leadertime;
    }

    public String getAssistantnote() {
        return assistantnote;
    }

    public void setAssistantnote(String assistantnote) {
        this.assistantnote = assistantnote;
    }

    public Date getAssistantime() {
        return assistantime;
    }

    public void setAssistantime(Date assistantime) {
        this.assistantime = assistantime;
    }

    public String getCashernote() {
        return cashernote;
    }

    public void setCashernote(String cashernote) {
        this.cashernote = cashernote;
    }

    public Date getCashertime() {
        return cashertime;
    }

    public void setCashertime(Date cashertime) {
        this.cashertime = cashertime;
    }

    public String getDeputynote() {
        return deputynote;
    }

    public void setDeputynote(String deputynote) {
        this.deputynote = deputynote;
    }

    public Date getDeputytime() {
        return deputytime;
    }

    public void setDeputytime(Date deputytime) {
        this.deputytime = deputytime;
    }

    public String getManagernote() {
        return managernote;
    }

    public void setManagernote(String managernote) {
        this.managernote = managernote;
    }

    public Date getManagertime() {
        return managertime;
    }

    public void setManagertime(Date managertime) {
        this.managertime = managertime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getReceivingunit() {
        return receivingunit;
    }

    public void setReceivingunit(String receivingunit) {
        this.receivingunit = receivingunit;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getUpperfee() {
        return upperfee;
    }

    public void setUpperfee(String upperfee) {
        this.upperfee = upperfee;
    }

    public BigDecimal getCountfee() {
        return countfee;
    }

    public void setCountfee(BigDecimal countfee) {
        this.countfee = countfee;
    }

    public Integer getPay() {
        return pay;
    }

    public void setPay(Integer pay) {
        this.pay = pay;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "CostApply{" +
                "id=" + id +
                ", reqno='" + reqno + '\'' +
                ", deptid=" + deptid +
                ", projectid=" + projectid +
                ", location=" + location +
                ", pay=" + pay +
                ", applytime='" + applytime + '\'' +
                ", use='" + use + '\'' +
                ", totalfee=" + totalfee +
                ", countfee=" + countfee +
                ", receivingunit='" + receivingunit + '\'' +
                ", bank='" + bank + '\'' +
                ", account='" + account + '\'' +
                ", deadline='" + deadline + '\'' +
                ", reason='" + reason + '\'' +
                ", price=" + price +
                ", upperfee='" + upperfee + '\'' +
                ", type=" + type +
                ", createtime=" + createtime +
                ", state=" + state +
                ", userid=" + userid +
                ", processId='" + processId + '\'' +
                ", assignee='" + assignee + '\'' +
                ", leadernote='" + leadernote + '\'' +
                ", leadertime=" + leadertime +
                ", assistantnote='" + assistantnote + '\'' +
                ", assistantime=" + assistantime +
                ", cashernote='" + cashernote + '\'' +
                ", cashertime=" + cashertime +
                ", deputynote='" + deputynote + '\'' +
                ", deputytime=" + deputytime +
                ", managernote='" + managernote + '\'' +
                ", managertime=" + managertime +
                '}';
    }
}
