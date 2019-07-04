package com.chengxin.sync_job.domain;

import java.util.List;

/**
 * @author Mr.Song
 * @create 2019-05-06 13:49
 * @desc 采购入库主表数据实体
 **/
public class  PurchaseInParentEntity {
    private String billCode;//单号
    private String pkCode;//所属公司
    private String provName;//供应商名称
    private String cdptid;//部门id
    private String uniqueid;//出入库单表头ID
    private String operateDate;//库房签字日期
    private String status;//传输状态
    private List<PurchaseInChildEntity> purchaseInChilds;

    public String getBillCode() {
        return billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    public String getPkCode() {
        return pkCode;
    }

    public void setPkCode(String pkCode) {
        this.pkCode = pkCode;
    }

    public String getProvName() {
        return provName;
    }

    public void setProvName(String provName) {
        this.provName = provName;
    }

    public String getCdptid() {
        return cdptid;
    }

    public void setCdptid(String cdptid) {
        this.cdptid = cdptid;
    }

    public String getUniqueid() {
        return uniqueid;
    }

    public void setUniqueid(String uniqueid) {
        this.uniqueid = uniqueid;
    }

    public String getOperateDate() {
        return operateDate;
    }

    public void setOperateDate(String operateDate) {
        this.operateDate = operateDate;
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
}
