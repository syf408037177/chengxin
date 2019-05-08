package com.chengxin.sync_job.domain;

/**
 * @author Mr.Song
 * @create 2019-05-08 16:49
 * @desc 采购入库子表
 **/
public class PurchaseInChildEntity {
    private String materCode;//存货ID
    private String floorCode;//货位ID
    private String priceUnitAmt;//入库数量/实收数量
    private String avePrice;//单价

    public String getMaterCode() {
        return materCode;
    }

    public void setMaterCode(String materCode) {
        this.materCode = materCode;
    }

    public String getFloorCode() {
        return floorCode;
    }

    public void setFloorCode(String floorCode) {
        this.floorCode = floorCode;
    }

    public String getPriceUnitAmt() {
        return priceUnitAmt;
    }

    public void setPriceUnitAmt(String priceUnitAmt) {
        this.priceUnitAmt = priceUnitAmt;
    }

    public String getAvePrice() {
        return avePrice;
    }

    public void setAvePrice(String aveprice) {
        this.avePrice = aveprice;
    }

    @Override
    public String toString() {
        return "PurchaseInChildEntity{" +
                "materCode='" + materCode + '\'' +
                ", floorCode='" + floorCode + '\'' +
                ", priceUnitAmt='" + priceUnitAmt + '\'' +
                ", avePrice='" + avePrice + '\'' +
                '}';
    }
}
