package com.tce.oa.modular.fund.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 资金申请费用明细表
 * </p>
 *
 * @author wangxy123
 * @since 2018-12-17
 */
@TableName("biz_fund_cost_fee_detail")
public class CostApplyFeeDetail extends Model<CostApplyFeeDetail> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 申请表id
     */
    private Integer aid;
    /**
     * 费用名称
     */
    private String name;
    /**
     * 支出项目
     */
    private String outlaypro;
    /**
     * 金额
     */
    private BigDecimal price;
    /**
     * 备注
     */
    private String note;


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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOutlaypro() {
        return outlaypro;
    }

    public void setOutlaypro(String outlaypro) {
        this.outlaypro = outlaypro;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "FundCostFeeDetail{" +
        "id=" + id +
        ", aid=" + aid +
        ", name=" + name +
        ", outlaypro=" + outlaypro +
        ", price=" + price +
        ", note=" + note +
        "}";
    }
}
