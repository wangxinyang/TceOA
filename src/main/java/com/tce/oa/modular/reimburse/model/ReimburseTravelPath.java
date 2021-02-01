package com.tce.oa.modular.reimburse.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 差旅费用报销起止路径表
 * </p>
 *
 * @author wangxy123
 * @since 2018-11-29
 */
@TableName("biz_reimburse_travel_path")
public class ReimburseTravelPath extends Model<ReimburseTravelPath> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 报销表id
     */
    private Integer rtid;
    /**
     * 行程起时间
     */
    private String fromtime;
    /**
     * 行程起地点
     */
    private String fromlocation;
    /**
     * 行程止时间
     */
    private String totime;
    /**
     * 行程止地点
     */
    private String tolocation;
    /**
     * 主要目的及具体工作安排
     */
    private String target;
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

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "ReimburseTravelPath{" +
        "id=" + id +
        ", rtid=" + rtid +
        ", fromtime=" + fromtime +
        ", fromlocation=" + fromlocation +
        ", totime=" + totime +
        ", tolocation=" + tolocation +
        ", target=" + target +
        ", createtime=" + createtime +
        "}";
    }
}
