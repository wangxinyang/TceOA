package com.tce.oa.modular.fund.vo;

import java.math.BigDecimal;

/**
 * 传递到页面的数据bean
 * @author wangxinyang
 * @version 1.0
 * @date 2018/11/19 14:56
 **/
public class CostPurchaseDetailVo {

    private Integer id;
    private Integer index;
    private Integer purid;
    private String goods;
    private String type;
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

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
}
