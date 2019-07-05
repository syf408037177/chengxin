package com.chengxin.sync_job.domain;

/**
 * @author Mr.Song
 * @create 2019-05-08 16:49
 * @desc 收货单子表
 **/
public class ArriveOrderChildEntity {
    private String parentCode;//到货单ID
    private String code;//到货单行ID
    private String pkCorp;//收票公司/需求公司
    private String taudittime;
    private String itemCode;
    private String purchaseNo;
    private String priceUnitAmt;//到货数量

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPkCorp() {
        return pkCorp;
    }

    public void setPkCorp(String pkCorp) {
        this.pkCorp = pkCorp;
    }

    public String getTaudittime() {
        return taudittime;
    }

    public void setTaudittime(String taudittime) {
        this.taudittime = taudittime;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getPurchaseNo() {
        return purchaseNo;
    }

    public void setPurchaseNo(String purchaseNo) {
        this.purchaseNo = purchaseNo;
    }

    public String getPriceUnitAmt() {
        return priceUnitAmt;
    }

    public void setPriceUnitAmt(String priceUnitAmt) {
        this.priceUnitAmt = priceUnitAmt;
    }
}
