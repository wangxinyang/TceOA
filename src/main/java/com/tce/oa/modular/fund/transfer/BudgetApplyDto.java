package com.tce.oa.modular.fund.transfer;

import java.util.List;

/**
 * @author wangxinyang
 * @version 1.0
 * @date 2018/11/14 18:00
 **/
public class BudgetApplyDto {

    private Integer id;

    private Integer aid;

    private Integer location;

    private String taskId;
    /**
     * 部门id
     */
    private Integer deptid;
    /** 类型: 1.业务费用  2:差旅费用  3.采购费用 4:其它费用 */
    private Integer type;

    private List<BudgetApplyDetailDto> fundApplyDetailDtos;

    private String taskDefinitionKey;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAid() {
        return aid;
    }

    public void setAid(Integer aid) {
        this.aid = aid;
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

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public List<BudgetApplyDetailDto> getFundApplyDetailDtos() {
        return fundApplyDetailDtos;
    }

    public void setFundApplyDetailDtos(List<BudgetApplyDetailDto> fundApplyDetailDtos) {
        this.fundApplyDetailDtos = fundApplyDetailDtos;
    }

    public String getTaskDefinitionKey() {
        return taskDefinitionKey;
    }

    public void setTaskDefinitionKey(String taskDefinitionKey) {
        this.taskDefinitionKey = taskDefinitionKey;
    }
}
