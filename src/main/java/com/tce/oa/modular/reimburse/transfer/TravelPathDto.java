package com.tce.oa.modular.reimburse.transfer;

import java.util.Date;

/**
 * @author wangxinyang
 * @version 1.0
 * @date 2018/11/30 9:51
 **/
public class TravelPathDto {

    private Integer id;
    private Integer rtid;
    private String fromtime;
    private String fromlocation;
    private String totime;
    private String tolocation;
    private String target;
    private Date createtime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRtid() {
        return rtid;
    }

    public void setRtid(Integer rtid) {
        this.rtid = rtid;
    }

    public String getFromtime() {
        return fromtime;
    }

    public void setFromtime(String fromtime) {
        this.fromtime = fromtime;
    }

    public String getFromlocation() {
        return fromlocation;
    }

    public void setFromlocation(String fromlocation) {
        this.fromlocation = fromlocation;
    }

    public String getTotime() {
        return totime;
    }

    public void setTotime(String totime) {
        this.totime = totime;
    }

    public String getTolocation() {
        return tolocation;
    }

    public void setTolocation(String tolocation) {
        this.tolocation = tolocation;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
}
