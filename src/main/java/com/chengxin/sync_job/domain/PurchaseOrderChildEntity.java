package com.chengxin.sync_job.domain;

/**
 * @author Mr.Song
 * @create 2019-05-08 16:49
 * @desc 采购订单子表
 **/
public class PurchaseOrderChildEntity {
    private String detailId;//采购订单明细id
    private String prodCode;//存货基础ID
    private String priceUnitQty;//订单数量
    private String packPrice;//含税单价/净含税单价
    private String packPriceUnit;//辅计量单位

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }

    public String getProdCode() {
        return prodCode;
    }

    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }

    public String getPriceUnitQty() {
        return priceUnitQty;
    }

    public void setPriceUnitQty(String priceUnitQty) {
        this.priceUnitQty = priceUnitQty;
    }

    public String getPackPrice() {
        return packPrice;
    }

    public void setPackPrice(String packPrice) {
        this.packPrice = packPrice;
    }

    public String getPackPriceUnit() {
        return packPriceUnit;
    }

    public void setPackPriceUnit(String packPriceUnit) {
        this.packPriceUnit = packPriceUnit;
    }

    @Override
    public String toString() {
        return "PurchaseOrderChildEntity{" +
                "detailId='" + detailId + '\'' +
                ", prodCode='" + prodCode + '\'' +
                ", priceUnitQty='" + priceUnitQty + '\'' +
                ", packPrice='" + packPrice + '\'' +
                ", packPriceUnit='" + packPriceUnit + '\'' +
                '}';
    }
}
