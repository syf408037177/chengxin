package com.chengxin.sync_job.domain;

/**
 * @author Mr.Song
 * @create 2019-05-08 16:49
 * @desc 采购入库子表
 **/
public class PurchaseInChildEntity {
    private String wareCode;//库存仓库(需转换)
    private String uniqueid;//出入库单表体主键
    private String masterid;//出入库单表头主键
    private String materCode;//存货基本id(需转换)
    private String priceUnitAmt;//实入数量
    private String itemMoney;//金额
    private String avePrice;//单价
    private String missionCode;

    public String getWareCode() {
        return wareCode;
    }

    public void setWareCode(String wareCode) {
        this.wareCode = wareCode;
    }

    public String getUniqueid() {
        return uniqueid;
    }

    public void setUniqueid(String uniqueid) {
        this.uniqueid = uniqueid;
    }

    public String getMasterid() {
        return masterid;
    }

    public void setMasterid(String masterid) {
        this.masterid = masterid;
    }

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

    public String getItemMoney() {
        return itemMoney;
    }

    public void setItemMoney(String itemMoney) {
        this.itemMoney = itemMoney;
    }

    public String getAvePrice() {
        return avePrice;
    }

    public void setAvePrice(String avePrice) {
        this.avePrice = avePrice;
    }

    public String getMissionCode() {
        return missionCode;
    }

    public void setMissionCode(String missionCode) {
        this.missionCode = missionCode;
    }
}
