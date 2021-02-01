package com.tce.oa.modular.fund.transfer;

import java.math.BigDecimal;

/**
 * @author wangxinyang
 * @version 1.0
 * @date 2018/11/14 18:00
 **/
public class CostApplyPurchaseGoodsDto {

    private Integer id;

    private Integer purid;

    private String goods;

    private String type;
    /**
     * 客户名称
     */
    private String num;
    private BigDecimal price;
    private BigDecimal totalfee;
    private String supplier;
    private String quotationunit;
    private String note;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPurid() {
        return purid;
    }

    public void setPurid(Integer purid) {
        this.purid = purid;
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
}
