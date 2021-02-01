package com.tce.oa.modular.fund.transfer;

import java.math.BigDecimal;

/**
 * @author wangxinyang
 * @version 1.0
 * @date 2018/11/14 18:00
 **/
public class BudgetApplyDetailDto {

    private Integer id;

    private Integer aid;

    /**
     * 项目名称
     */
    private String projectid;

    /**
     * 项目名称
     */
    private Integer subjectid;
    /**
     * 客户名称
     */
    private String customername;
    private String empname;
    private String traveldest;
    private String traveldays;
    private BigDecimal travelfee;
    private BigDecimal trafficfee;
    private BigDecimal accommodationfee;
    private BigDecimal subsidy;
    private String purchaseitem;
    private BigDecimal unitprice;
    /**
     * 礼品费
     */
    private Integer num;
    private String expenseitem;
    /**
     * 小计
     */
    private BigDecimal totalfee;
    /**
     * 资金使用用途说明及核算标准
     */
    private String description;
    /**
     * 备注说明
     */
    private String note;
    private String assistantnote;
    private String deputynote;
    private String managernote;
    private BigDecimal subapprovefee;
    private BigDecimal approvefee;
    private BigDecimal actualfee;
    private String ratio;

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

    public String getProjectid() {
        return projectid;
    }

    public void setProjectid(String projectid) {
        this.projectid = projectid;
    }

    public Integer getSubjectid() {
        return subjectid;
    }

    public void setSubjectid(Integer subjectid) {
        this.subjectid = subjectid;
    }

    public String getCustomername() {
        return customername;
    }

    public void setCustomername(String customername) {
        this.customername = customername;
    }

    public BigDecimal getTotalfee() {
        return totalfee;
    }

    public void setTotalfee(BigDecimal totalfee) {
        this.totalfee = totalfee;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getAssistantnote() {
        return assistantnote;
    }

    public void setAssistantnote(String assistantnote) {
        this.assistantnote = assistantnote;
    }

    public String getDeputynote() {
        return deputynote;
    }

    public void setDeputynote(String deputynote) {
        this.deputynote = deputynote;
    }

    public String getManagernote() {
        return managernote;
    }

    public void setManagernote(String managernote) {
        this.managernote = managernote;
    }

    public BigDecimal getSubapprovefee() {
        return subapprovefee;
    }

    public void setSubapprovefee(BigDecimal subapprovefee) {
        this.subapprovefee = subapprovefee;
    }

    public BigDecimal getApprovefee() {
        return approvefee;
    }

    public void setApprovefee(BigDecimal approvefee) {
        this.approvefee = approvefee;
    }

    public BigDecimal getActualfee() {
        return actualfee;
    }

    public void setActualfee(BigDecimal actualfee) {
        this.actualfee = actualfee;
    }

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }

    public String getEmpname() {
        return empname;
    }

    public void setEmpname(String empname) {
        this.empname = empname;
    }

    public String getTraveldest() {
        return traveldest;
    }

    public void setTraveldest(String traveldest) {
        this.traveldest = traveldest;
    }

    public String getTraveldays() {
        return traveldays;
    }

    public void setTraveldays(String traveldays) {
        this.traveldays = traveldays;
    }

    public BigDecimal getTravelfee() {
        return travelfee;
    }

    public void setTravelfee(BigDecimal travelfee) {
        this.travelfee = travelfee;
    }

    public BigDecimal getTrafficfee() {
        return trafficfee;
    }

    public void setTrafficfee(BigDecimal trafficfee) {
        this.trafficfee = trafficfee;
    }

    public BigDecimal getAccommodationfee() {
        return accommodationfee;
    }

    public void setAccommodationfee(BigDecimal accommodationfee) {
        this.accommodationfee = accommodationfee;
    }

    public BigDecimal getSubsidy() {
        return subsidy;
    }

    public void setSubsidy(BigDecimal subsidy) {
        this.subsidy = subsidy;
    }

    public String getPurchaseitem() {
        return purchaseitem;
    }

    public void setPurchaseitem(String purchaseitem) {
        this.purchaseitem = purchaseitem;
    }

    public BigDecimal getUnitprice() {
        return unitprice;
    }

    public void setUnitprice(BigDecimal unitprice) {
        this.unitprice = unitprice;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getExpenseitem() {
        return expenseitem;
    }

    public void setExpenseitem(String expenseitem) {
        this.expenseitem = expenseitem;
    }
}
