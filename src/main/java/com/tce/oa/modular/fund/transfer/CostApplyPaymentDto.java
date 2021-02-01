package com.tce.oa.modular.fund.transfer;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author wangxinyang
 * @version 1.0
 * @date 2018/11/14 18:00
 **/
public class CostApplyPaymentDto {

    private Integer id;

    private Integer location;
    /**
     * 部门id
     */
    private Integer deptid;
    private String applytime;
    private String receivingunit;
    private String bank;
    private String account;
    private String deadline;
    private String reason;
    private String position;
    private Integer state;
    private Integer userid;
    private String processId;
    private Date createtime;
    private String assignee;
    private String taskId;
    private String taskUser;
    private BigDecimal totalfee;
    private String assistantnote;
    private String leadernote;
    private Date leadertime;
    private String cashernote;
    private Date cashertime;
    private String deputynote;
    private Date deputytime;
    private String managernote;
    private Date managertime;

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

    public Integer getDeptid() {
        return deptid;
    }

    public void setDeptid(Integer deptid) {
        this.deptid = deptid;
    }

    public String getApplytime() {
        return applytime;
    }

    public void setApplytime(String applytime) {
        this.applytime = applytime;
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

    public BigDecimal getTotalfee() {
        return totalfee;
    }

    public void setTotalfee(BigDecimal totalfee) {
        this.totalfee = totalfee;
    }

    public String getAssistantnote() {
        return assistantnote;
    }

    public void setAssistantnote(String assistantnote) {
        this.assistantnote = assistantnote;
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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
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

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskUser() {
        return taskUser;
    }

    public void setTaskUser(String taskUser) {
        this.taskUser = taskUser;
    }
}
