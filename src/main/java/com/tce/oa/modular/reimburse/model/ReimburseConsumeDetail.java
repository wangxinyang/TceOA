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
 * 费用报销详情表
 * </p>
 *
 * @author wangxy123
 * @since 2018-11-29
 */
@TableName("biz_reimburse_consume_detail")
public class ReimburseConsumeDetail extends Model<ReimburseConsumeDetail> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 报销表id
     */
    private Integer rcid;
    /**
     * 科目id
     */
    private Integer subjectid;
    /**
     * 项目id
     */
    private Integer projectid;
    /**
     * 资金用途
     */
    private String cashuse;
    /**
     * 报销金额
     */
    private BigDecimal cash;
    /**
     * 关联申请单号
     */
    private String reqno;

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

    public Integer getRcid() {
        return rcid;
    }

    public void setRcid(Integer rcid) {
        this.rcid = rcid;
    }

    public Integer getSubjectid() {
        return subjectid;
    }

    public void setSubjectid(Integer subjectid) {
        this.subjectid = subjectid;
    }

    public Integer getProjectid() {
        return projectid;
    }

    public void setProjectid(Integer projectid) {
        this.projectid = projectid;
    }

    public String getCashuse() {
        return cashuse;
    }

    public void setCashuse(String cashuse) {
        this.cashuse = cashuse;
    }

    public BigDecimal getCash() {
        return cash;
    }

    public void setCash(BigDecimal cash) {
        this.cash = cash;
    }


    public String getReqno() {
        return reqno;
    }

    public void setReqno(String reqno) {
        this.reqno = reqno;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "ReimburseConsumeDetail{" +
        "id=" + id +
        ", rcid=" + rcid +
        ", subjectid=" + subjectid +
        ", projectid=" + projectid +
        ", cashuse=" + cashuse +
        ", cash=" + cash +
        ", reqno=" + reqno +
        ", createtime=" + createtime +
        "}";
    }
}
