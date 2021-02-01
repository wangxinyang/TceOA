package com.tce.oa.modular.fund.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 采购申请商品详情表
 * </p>
 *
 * @author wangxy123
 * @since 2018-12-17
 */
@TableName("biz_fund_cost_purchase_goods")
public class CostApplyPurchaseGoods extends Model<CostApplyPurchaseGoods> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 申请表id
     */
    private Integer aid;
    /**
     * 商品名称
     */
    private String goods;
    /**
     * 规格型号
     */
    private String type;
    /**
     * 数量
     */
    private String num;
    /**
     * 单价
     */
    private BigDecimal price;
    /**
     * 总价
     */
    private BigDecimal totalfee;
    /**
     * 供应商
     */
    private String supplier;
    /**
     * 报价单位
     */
    private String quotationunit;
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

    public String getGoods() {
        return goods;
    }

    public void setGoods(String goods) {
        this.goods = goods;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getTotalfee() {
        return totalfee;
    }

    public void setTotalfee(BigDecimal totalfee) {
        this.totalfee = totalfee;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getQuotationunit() {
        return quotationunit;
    }

    public void setQuotationunit(String quotationunit) {
        this.quotationunit = quotationunit;
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
        return "FundCostPurchaseGoods{" +
        "id=" + id +
        ", aid=" + aid +
        ", goods=" + goods +
        ", type=" + type +
        ", num=" + num +
        ", price=" + price +
        ", totalfee=" + totalfee +
        ", supplier=" + supplier +
        ", quotationunit=" + quotationunit +
        ", note=" + note +
        "}";
    }
}
