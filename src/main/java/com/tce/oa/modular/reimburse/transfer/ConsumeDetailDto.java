package com.tce.oa.modular.reimburse.transfer;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author wangxinyang
 * @version 1.0
 * @date 2018/11/30 9:51
 **/
public class ConsumeDetailDto {

    private Integer id;
    private Integer rcid;
    private Integer subjectid;
    private Integer projectid;
    private String cashuse;
    private BigDecimal cash;
    private String reqno;
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
}
