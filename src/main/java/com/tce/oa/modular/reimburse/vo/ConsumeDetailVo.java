package com.tce.oa.modular.reimburse.vo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 传递到页面的数据bean
 * @author wangxinyang
 * @version 1.0
 * @date 2018/11/19 14:56
 **/
public class ConsumeDetailVo {

    private Integer id;
    private Integer rcid;
    private Integer subjectid;
    private String subjectname;
    private Integer projectid;
    private String projectname;
    private String code;
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

    public String getSubjectname() {
        return subjectname;
    }

    public void setSubjectname(String subjectname) {
        this.subjectname = subjectname;
    }

    public String getProjectname() {
        return projectname;
    }

    public void setProjectname(String projectname) {
        this.projectname = projectname;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
}
