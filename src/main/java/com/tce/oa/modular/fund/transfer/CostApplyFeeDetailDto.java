package com.tce.oa.modular.fund.transfer;

import java.math.BigDecimal;

/**
 * @author wangxinyang
 * @version 1.0
 * @date 2018/11/14 18:00
 **/
public class CostApplyFeeDetailDto {

    private Integer id;

    private Integer fid;

    private String name;

    private String outlaypro;

    private BigDecimal price;
    private String note;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFid() {
        return fid;
    }

    public void setFid(Integer fid) {
        this.fid = fid;
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
}
