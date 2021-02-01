package com.tce.oa.modular.fund.vo;

import java.util.Date;

/**
 * 任务列表vo
 *
 * @author fengshuonan
 * @date 2017-12-04 23:18
 */
public class TaskVo {

    private String id;

    private String name;

    private Integer deptid;

    private Integer type;

    private Object money;

    private Integer state;

    private Date createTime;

    private String assignee;

    private Boolean selfFlag;

    public TaskVo() {
    }

    public TaskVo(String id, String name, Integer deptid, Integer type, Object money, Integer state,
                  Date createTime, String assignee, Boolean flag) {
        this.id = id;
        this.deptid = deptid;
        this.type = type;
        this.money = money;
        this.state = state;
        this.createTime = createTime;
        this.assignee = assignee;
        this.selfFlag = flag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDeptid() {
        return deptid;
    }

    public void setDeptid(Integer deptid) {
        this.deptid = deptid;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Object getMoney() {
        return money;
    }

    public void setMoney(Object money) {
        this.money = money;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public Boolean getSelfFlag() {
        return selfFlag;
    }

    public void setSelfFlag(Boolean selfFlag) {
        this.selfFlag = selfFlag;
    }
}
