package com.tce.oa.modular.company.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 资金科目
 * </p>
 *
 * @author wangxy123
 * @since 2018-11-13
 */
@TableName("biz_fund_subject")
public class Subject extends Model<Subject> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 排序
     */
    private Integer num;
    /**
     * 父部门id
     */
    private Integer pid;
    /**
     * 父级ids
     */
    private String pids;
    /**
     * 科目
     */
    private String subject;
    /**
     * 说明
     */
    private String note;
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

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getPids() {
        return pids;
    }

    public void setPids(String pids) {
        this.pids = pids;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    @Override
    public String toString() {
        return "Subject{" +
                "id=" + id +
                ", num=" + num +
                ", pid=" + pid +
                ", pids='" + pids + '\'' +
                ", subject='" + subject + '\'' +
                ", note='" + note + '\'' +
                ", createtime=" + createtime +
                '}';
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
