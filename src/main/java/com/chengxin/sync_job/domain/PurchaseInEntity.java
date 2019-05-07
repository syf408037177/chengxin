package com.chengxin.sync_job.domain;

/**
 * @author Mr.Song
 * @create 2019-05-06 13:49
 * @desc 采购入库数据实体
 **/
public class PurchaseInEntity {
    private String billType;//业务类型
    private String creatorCode;//操作员
    private String wareCode;//仓库ID
    private String corpCode;//库存组织/公司
    private String operateDate;//单据日期
    private String specialCode;//供应商

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public String getCreatorCode() {
        return creatorCode;
    }

    public void setCreatorCode(String creatorCode) {
        this.creatorCode = creatorCode;
    }

    public String getWareCode() {
        return wareCode;
    }

    public void setWareCode(String wareCode) {
        this.wareCode = wareCode;
    }

    public String getCorpCode() {
        return corpCode;
    }

    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode;
    }

    public String getOperateDate() {
        return operateDate;
    }

    public void setOperateDate(String operateDate) {
        this.operateDate = operateDate;
    }

    public String getSpecialCode() {
        return specialCode;
    }

    public void setSpecialCode(String specialCode) {
        this.specialCode = specialCode;
    }
}
