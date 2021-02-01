package com.tce.oa.modular.reimburse.transfer;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author wangxinyang
 * @version 1.0
 * @date 2018/11/30 9:51
 **/
public class TravelReimburseDto {

    private Integer id;
    private Integer location;
    private Integer deptid;
    private Integer type;
    private String position;
    private String applytime;
    private Integer projectid;
    private String travelreason;
    private List<TravelPathDto> travelPathDtos;
    private BigDecimal accommodationfee;
    private BigDecimal trafficfee;
    private String traveldays;
    private BigDecimal subsidy;
    private Integer travelmethod;
    private BigDecimal travelfee;
    private BigDecimal othercash;
    private BigDecimal totalfee;
    private String ticketnums;
    private String upperfee;
    private BigDecimal applycash;
    private BigDecimal adjustcash;
    private String usernote;
    private String leadernote;
    private Date leadertime;
    private String cashernote;
    private Date cashertime;
    private String deputynote;
    private Date deputytime;
    private String managernote;
    private Date managertime;
    private Integer state;
    private Integer userid;
    private String processId;
    private Date createtime;
    private String assignee;
    private String taskId;
    private String taskUser;
    private String taskDefinitionKey;

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

    public List<TravelPathDto> getTravelPathDtos() {
        return travelPathDtos;
    }

    public void setTravelPathDtos(List<TravelPathDto> travelPathDtos) {
        this.travelPathDtos = travelPathDtos;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTaskDefinitionKey() {
        return taskDefinitionKey;
    }

    public void setTaskDefinitionKey(String taskDefinitionKey) {
        this.taskDefinitionKey = taskDefinitionKey;
    }
}
