package com.chengxin.sync_job.domain;

import java.util.List;

/**
 * @author Mr.Song
 * @create 2019-05-06 13:49
 * @desc 采购入库主表数据实体
 **/
public class PurchaseInParentEntity {
    private String creatorCode;//审批人id
    private String corpCode;//库存组织/公司
    private String corpName;//库存组织/公司名称
    private String operateDate;//最后修改日期
    private String createDate;//制单日期/订单日期
    private String specialCode;//供应商
    private String status;//传输状态
    private List<PurchaseInChildEntity> purchaseInChilds;

    public String getCreatorCode() {
        return creatorCode;
    }

    public void setCreatorCode(String creatorCode) {
        this.creatorCode = creatorCode;
    }

    public String getCorpCode() {
        return corpCode;
    }

    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode;
    }

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<PurchaseInChildEntity> getPurchaseInChilds() {
        return purchaseInChilds;
    }

    public void setPurchaseInChilds(List<PurchaseInChildEntity> purchaseInChilds) {
        this.purchaseInChilds = purchaseInChilds;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "PurchaseInParentEntity{" +
                "creatorCode='" + creatorCode + '\'' +
                ", corpCode='" + corpCode + '\'' +
                ", operateDate='" + operateDate + '\'' +
                ", createDate='" + createDate + '\'' +
                ", specialCode='" + specialCode + '\'' +
                ", status='" + status + '\'' +
                ", purchaseInChilds=" + purchaseInChilds +
                '}';
    }
}
