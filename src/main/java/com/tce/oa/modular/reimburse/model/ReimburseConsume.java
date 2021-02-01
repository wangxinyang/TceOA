package com.tce.oa.modular.reimburse.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 费用报销
 * </p>
 *
 * @author wangxy123
 * @since 2018-11-29
 */
@TableName("biz_reimburse_consume")
public class ReimburseConsume extends Model<ReimburseConsume> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer location;
    /**
     * 报销单号
     */
    private String reimburseno;
    /**
     * 部门id
     */
    private Integer deptid;
    /**
     * 预借现金
     */
    private BigDecimal applycash;
    /**
     * 退补现金
     */
    private BigDecimal adjustcash;
    /**
     * 金额合计
     */
    private BigDecimal totalfee;
    /**
     * 金额合计大写
     */
    private String upperfee;
    /**
     * 状态: 1.待提交 2:待自身审核 3:待部门领导审核  4.待财务审核 5:待副总经理审核 6:待总经理审核  7:审核未通过 8:审核通过 9:撤回
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
    /**
     * 创建时间
     */
    private Date createtime;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReimburseno() {
        return reimburseno;
    }

    public void setReimburseno(String reimburseno) {
        this.reimburseno = reimburseno;
    }

    public Integer getDeptid() {
        return deptid;
    }

    public void setDeptid(Integer deptid) {
        this.deptid = deptid;
    }

    public BigDecimal getTotalfee() {
        return totalfee;
    }

    public void setTotalfee(BigDecimal totalfee) {
        this.totalfee = totalfee;
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

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Integer getLocation() {
        return location;
    }

    public void setLocation(Integer location) {
        this.location = location;
    }

    public BigDecimal getApplycash() {
        return applycash;
    }

    public void setApplycash(BigDecimal applycash) {
        this.applycash = applycash;
    }

    public BigDecimal getAdjustcash() {
        return adjustcash;
    }

    public void setAdjustcash(BigDecimal adjustcash) {
        this.adjustcash = adjustcash;
    }

    public String getUpperfee() {
        return upperfee;
    }

    public void setUpperfee(String upperfee) {
        this.upperfee = upperfee;
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

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "ReimburseConsume{" +
                "id=" + id +
                ", location=" + location +
                ", reimburseno='" + reimburseno + '\'' +
                ", deptid=" + deptid +
                ", applycash=" + applycash +
                ", adjustcash=" + adjustcash +
                ", totalfee=" + totalfee +
                ", upperfee='" + upperfee + '\'' +
                ", state=" + state +
                ", userid=" + userid +
                ", processId='" + processId + '\'' +
                ", assignee='" + assignee + '\'' +
                ", leadernote='" + leadernote + '\'' +
                ", leadertime=" + leadertime +
                ", cashernote='" + cashernote + '\'' +
                ", cashertime=" + cashertime +
                ", deputynote='" + deputynote + '\'' +
                ", deputytime=" + deputytime +
                ", managernote='" + managernote + '\'' +
                ", managertime=" + managertime +
                ", createtime=" + createtime +
                '}';
    }
}
