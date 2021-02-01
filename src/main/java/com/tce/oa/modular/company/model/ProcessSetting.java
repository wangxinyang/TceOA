package com.tce.oa.modular.company.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 项目表
 * </p>
 *
 * @author wangxy123
 * @since 2018-11-14
 */
@TableName("biz_process_setting")
public class ProcessSetting extends Model<ProcessSetting> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String pid;

    private String processname;
    private String userid;
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

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getProcessname() {
        return processname;
    }

    public void setProcessname(String processname) {
        this.processname = processname;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    @Override
    public String toString() {
        return "ProcessSetting{" +
                "id=" + id +
                ", pid='" + pid + '\'' +
                ", processname='" + processname + '\'' +
                ", userid='" + userid + '\'' +
                ", createtime=" + createtime +
                '}';
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
