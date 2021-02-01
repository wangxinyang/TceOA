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
 * 差旅费用报销
 * </p>
 *
 * @author wangxy123
 * @since 2018-11-29
 */
@TableName("biz_reimburse_travel")
public class ReimburseTravel extends Model<ReimburseTravel> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 报销单号
     */
    private String reimburseno;

    private Integer location;
    /**
     * 部门id
     */
    private Integer deptid;
    /**
     * 职务
     */
    private String position;
    /**
     * 申请时间
     */
    private String applytime;
    /**
     * 项目id
     */
    private Integer projectid;
    /**
     * 出差事由
     */
    private String travelreason;
    /**
     * 住宿费
     */
    private BigDecimal accommodationfee;
    /**
     * 市内交通费
     */
    private BigDecimal trafficfee;
    /**
     * 出差天数
     */
    private String traveldays;
    /**
     * 补贴
     */
    private BigDecimal subsidy;
    /**
     * 交通方式 1.飞机 2:火车 3:高铁  4.汽车 5:其它
     */
    private Integer travelmethod;
    /**
     * 交通费
     */
    private BigDecimal travelfee;
    /**
     * 其他费用
     */
    private BigDecimal othercash;
    /**
     * 小计
     */
    private BigDecimal totalfee;
    /**
     * 单据张数
     */
    private String ticketnums;
    /**
     * 金额大写
     */
    private String upperfee;
    /**
     * 预借现金
     */
    private BigDecimal applycash;
    /**
     * 退补现金
     */
    private BigDecimal adjustcash;

    private String assignee;
    /**
     * 报销人备注说明
     */
    private String usernote;
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

    public Integer getLocation() {
        return location;
    }

    public void setLocation(Integer location) {
        this.location = location;
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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getApplytime() {
        return applytime;
    }

    public void setApplytime(String applytime) {
        this.applytime = applytime;
    }

    public Integer getProjectid() {
        return projectid;
    }

    public void setProjectid(Integer projectid) {
        this.projectid = projectid;
    }

    public String getTravelreason() {
        return travelreason;
    }

    public void setTravelreason(String travelreason) {
        this.travelreason = travelreason;
    }

    public BigDecimal getAccommodationfee() {
        return accommodationfee;
    }

    public void setAccommodationfee(BigDecimal accommodationfee) {
        this.accommodationfee = accommodationfee;
    }

    public BigDecimal getTrafficfee() {
        return trafficfee;
    }

    public void setTrafficfee(BigDecimal trafficfee) {
        this.trafficfee = trafficfee;
    }

    public String getTraveldays() {
        return traveldays;
    }

    public void setTraveldays(String traveldays) {
        this.traveldays = traveldays;
    }

    public BigDecimal getSubsidy() {
        return subsidy;
    }

    public void setSubsidy(BigDecimal subsidy) {
        this.subsidy = subsidy;
    }

    public Integer getTravelmethod() {
        return travelmethod;
    }

    public void setTravelmethod(Integer travelmethod) {
        this.travelmethod = travelmethod;
    }

    public BigDecimal getTravelfee() {
        return travelfee;
    }

    public void setTravelfee(BigDecimal travelfee) {
        this.travelfee = travelfee;
    }

    public BigDecimal getOthercash() {
        return othercash;
    }

    public void setOthercash(BigDecimal othercash) {
        this.othercash = othercash;
    }

    public BigDecimal getTotalfee() {
        return totalfee;
    }

    public void setTotalfee(BigDecimal totalfee) {
        this.totalfee = totalfee;
    }

    public String getTicketnums() {
        return ticketnums;
    }

    public void setTicketnums(String ticketnums) {
        this.ticketnums = ticketnums;
    }

    public String getUpperfee() {
        return upperfee;
    }

    public void setUpperfee(String upperfee) {
        this.upperfee = upperfee;
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

    public String getUsernote() {
        return usernote;
    }

    public void setUsernote(String usernote) {
        this.usernote = usernote;
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
        return "ReimburseTravel{" +
                "id=" + id +
                ", reimburseno='" + reimburseno + '\'' +
                ", location=" + location +
                ", deptid=" + deptid +
                ", position='" + position + '\'' +
                ", applytime='" + applytime + '\'' +
                ", projectid=" + projectid +
                ", travelreason='" + travelreason + '\'' +
                ", accommodationfee=" + accommodationfee +
                ", trafficfee=" + trafficfee +
                ", traveldays='" + traveldays + '\'' +
                ", subsidy=" + subsidy +
                ", travelmethod=" + travelmethod +
                ", travelfee=" + travelfee +
                ", othercash=" + othercash +
                ", totalfee=" + totalfee +
                ", ticketnums='" + ticketnums + '\'' +
                ", upperfee='" + upperfee + '\'' +
                ", applycash=" + applycash +
                ", adjustcash=" + adjustcash +
                ", assignee='" + assignee + '\'' +
                ", usernote='" + usernote + '\'' +
                ", leadernote='" + leadernote + '\'' +
                ", leadertime=" + leadertime +
                ", cashernote='" + cashernote + '\'' +
                ", cashertime=" + cashertime +
                ", deputynote='" + deputynote + '\'' +
                ", deputytime=" + deputytime +
                ", managernote='" + managernote + '\'' +
                ", managertime=" + managertime +
                ", state=" + state +
                ", userid=" + userid +
                ", processId='" + processId + '\'' +
                ", createtime=" + createtime +
                '}';
    }
}
