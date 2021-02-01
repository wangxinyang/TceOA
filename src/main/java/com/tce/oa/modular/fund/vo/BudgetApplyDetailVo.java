package com.tce.oa.modular.fund.vo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 预算申请传递到页面的数据bean
 * @author wangxinyang
 * @version 1.0
 * @date 2018/11/19 14:56
 **/
public class BudgetApplyDetailVo {

    private Integer id;
    private Integer aid;
    private String projectid;
    private String projectname;
    private String customername;
    private String empname;
    private String subjectname;
    private String expenseitem;
    private String purchaseitem;
    private String traveldest;
    private String traveldays;
    private BigDecimal hospitality;
    private BigDecimal giftfee;
    private BigDecimal nocontract;
    private BigDecimal contract;
    private BigDecimal travelfee;
    private BigDecimal trafficfee;
    private BigDecimal accommodationfee;
    private BigDecimal subsidy;
    private BigDecimal unitprice;
    private BigDecimal totalfee;
    private Integer num;
    private String description;
    private String note;
    private String assistantnote;
    private String deputynote;
    private String managernote;
    private Date createtime;
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

    public String getProjectname() {
        return projectname;
    }

    public void setProjectname(String projectname) {
        this.projectname = projectname;
    }

    public String getCustomername() {
        return customername;
    }

    public void setCustomername(String customername) {
        this.customername = customername;
    }

    public String getEmpname() {
        return empname;
    }

    public void setEmpname(String empname) {
        this.empname = empname;
    }

    public String getSubjectname() {
        return subjectname;
    }

    public void setSubjectname(String subjectname) {
        this.subjectname = subjectname;
    }

    public String getExpenseitem() {
        return expenseitem;
    }

    public void setExpenseitem(String expenseitem) {
        this.expenseitem = expenseitem;
    }

    public String getPurchaseitem() {
        return purchaseitem;
    }

    public void setPurchaseitem(String purchaseitem) {
        this.purchaseitem = purchaseitem;
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

    public BigDecimal getHospitality() {
        return hospitality;
    }

    public void setHospitality(BigDecimal hospitality) {
        this.hospitality = hospitality;
    }

    public BigDecimal getGiftfee() {
        return giftfee;
    }

    public void setGiftfee(BigDecimal giftfee) {
        this.giftfee = giftfee;
    }

    public BigDecimal getNocontract() {
        return nocontract;
    }

    public void setNocontract(BigDecimal nocontract) {
        this.nocontract = nocontract;
    }

    public BigDecimal getContract() {
        return contract;
    }

    public void setContract(BigDecimal contract) {
        this.contract = contract;
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

    public BigDecimal getUnitprice() {
        return unitprice;
    }

    public void setUnitprice(BigDecimal unitprice) {
        this.unitprice = unitprice;
    }

    public BigDecimal getTotalfee() {
        return totalfee;
    }

    public void setTotalfee(BigDecimal totalfee) {
        this.totalfee = totalfee;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
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

    public String getProjectid() {
        return projectid;
    }

    public void setProjectid(String projectid) {
        this.projectid = projectid;
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
}
