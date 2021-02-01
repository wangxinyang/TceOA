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
 * 业务费用预算详情表
 * </p>
 *
 * @author wangxy123
 * @since 2018-11-14
 */
@TableName("biz_fund_apply_detail")
public class BudgetApplyDetail extends Model<BudgetApplyDetail> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 申请表id
     */
    private Integer aid;
    /**
     * 申请表id
     */
    private Integer subjectid;
    /**
     * 项目id
     */
    private String projectid;
    /**
     * 客户名称
     */
    private String customername;
    /**
     * 员工姓名
     */
    private String empname;
    /**
     * 出差目的地
     */
    private String traveldest;
    /**
     * 预计出差天数
     */
    private String traveldays;
    /**
     * 长途交通费
     */
    private BigDecimal travelfee;
    /**
     * 站点往返交通费
     */
    private BigDecimal trafficfee;
    /**
     * 住宿费
     */
    private BigDecimal accommodationfee;
    /**
     * 补贴
     */
    private BigDecimal subsidy;
    /**
     * 采购项目
     */
    private String purchaseitem;
    /**
     * 单价
     */
    private BigDecimal unitprice;
    /**
     * 数量
     */
    private Integer num;
    /**
     * 支出项目
     */
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
     * 创建时间
     */
    private Date createtime;
    /**
     * 副总审批金额
     */
    private BigDecimal subapprovefee;
    /**
     * 总经理审批金额
     */
    private BigDecimal approvefee;

    /**
     * 备注
     */
    private String note;
    /**
     * 总助备注
     */
    private String assistantnote;
    /**
     * 副总备注
     */
    private String deputynote;
    /**
     * 总经理备注
     */
    private String managernote;
    /**
     * 实际发生金额
     */
    private BigDecimal actualfee;
    /**
     * 执行率
     */
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

    @Override
    protected Serializable pkVal() {
        return this.id;
    }


    public String getCustomername() {
        return customername;
    }

    public void setCustomername(String customername) {
        this.customername = customername;
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

    public Integer getSubjectid() {
        return subjectid;
    }

    public void setSubjectid(Integer subjectid) {
        this.subjectid = subjectid;
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

    @Override
    public String toString() {
        return "FundApplyDetail{" +
                "id=" + id +
                ", aid=" + aid +
                ", subjectid=" + subjectid +
                ", projectid='" + projectid + '\'' +
                ", customername='" + customername + '\'' +
                ", empname='" + empname + '\'' +
                ", traveldest='" + traveldest + '\'' +
                ", traveldays='" + traveldays + '\'' +
                ", travelfee=" + travelfee +
                ", trafficfee=" + trafficfee +
                ", accommodationfee=" + accommodationfee +
                ", subsidy=" + subsidy +
                ", purchaseitem='" + purchaseitem + '\'' +
                ", unitprice=" + unitprice +
                ", num=" + num +
                ", expenseitem='" + expenseitem + '\'' +
                ", totalfee=" + totalfee +
                ", description='" + description + '\'' +
                ", createtime=" + createtime +
                ", subapprovefee=" + subapprovefee +
                ", approvefee=" + approvefee +
                ", note='" + note + '\'' +
                ", assistantnote='" + assistantnote + '\'' +
                ", deputynote='" + deputynote + '\'' +
                ", managernote='" + managernote + '\'' +
                ", actualfee=" + actualfee +
                ", ratio='" + ratio + '\'' +
                '}';
    }
}
