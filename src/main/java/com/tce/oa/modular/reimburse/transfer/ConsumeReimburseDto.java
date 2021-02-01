package com.tce.oa.modular.reimburse.transfer;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author wangxinyang
 * @version 1.0
 * @date 2018/11/30 9:51
 **/
public class ConsumeReimburseDto {

    private Integer id;
    private Integer location;
    private Integer deptid;
    private Integer type;
    private String position;
    private Integer state;
    private Integer userid;
    private String processId;
    private Date createtime;
    private String assignee;
    private String taskId;
    private String taskUser;
    private BigDecimal totalfee;
    private BigDecimal applycash;
    private BigDecimal adjustcash;
    private String leadernote;
    private Date leadertime;
    private String cashernote;
    private Date cashertime;
    private String deputynote;
    private Date deputytime;
    private String managernote;
    private Date managertime;
    private String taskDefinitionKey;
    private List<ConsumeDetailDto> consumeDetailDtos;

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

    public BigDecimal getTotalfee() {
        return totalfee;
    }

    public void setTotalfee(BigDecimal totalfee) {
        this.totalfee = totalfee;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public List<ConsumeDetailDto> getConsumeDetailDtos() {
        return consumeDetailDtos;
    }

    public void setConsumeDetailDtos(List<ConsumeDetailDto> consumeDetailDtos) {
        this.consumeDetailDtos = consumeDetailDtos;
    }

    public String getTaskDefinitionKey() {
        return taskDefinitionKey;
    }

    public void setTaskDefinitionKey(String taskDefinitionKey) {
        this.taskDefinitionKey = taskDefinitionKey;
    }
}
