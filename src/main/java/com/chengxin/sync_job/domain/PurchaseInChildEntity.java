package com.chengxin.sync_job.domain;

/**
 * @author Mr.Song
 * @create 2019-05-08 16:49
 * @desc 采购入库子表
 **/
public class PurchaseInChildEntity {
    private String materCode;//存货基本Id
    private String priceUnitAmt;//订单数量
    private String avePriceUnit;//辅计量单位

    public String getMaterCode() {
        return materCode;
    }

    public void setMaterCode(String materCode) {
        this.materCode = materCode;
    }

    public String getPriceUnitAmt() {
        return priceUnitAmt;
    }

    public void setPriceUnitAmt(String priceUnitAmt) {
        this.priceUnitAmt = priceUnitAmt;
    }

    public String getAvePriceUnit() {
        return avePriceUnit;
    }

    public void setAvePriceUnit(String avePriceUnit) {
        this.avePriceUnit = avePriceUnit;
    }

    @Override
    public String toString() {
        return "PurchaseInChildEntity{" +
                "materCode='" + materCode + '\'' +
                ", priceUnitAmt='" + priceUnitAmt + '\'' +
                ", avepriceUnit='" + avePriceUnit + '\'' +
                '}';
    }
}
