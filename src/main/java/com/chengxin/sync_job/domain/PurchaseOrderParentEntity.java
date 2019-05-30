package com.chengxin.sync_job.domain;

import java.util.List;

/**
 * @author Mr.Song
 * @create 2019-05-06 13:49
 * @desc 采购订单主表数据实体
 **/
public class PurchaseOrderParentEntity {
    private String billId;//订单编号
    private String orderId;//采购订单主表主键
    private String pkCorp;//所属公司
    private String cpurOrganization;//采购组织
    private String setDate;//订单日期
    private String provId;//供应商
    private String provName;//供应商名称
    private String status;//传输状态
    private String cdeptId;//采购部门
    private List<PurchaseOrderChildEntity> purchaseOrderChilds;

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPkCorp() {
        return pkCorp;
    }

    public void setPkCorp(String pkCorp) {
        this.pkCorp = pkCorp;
    }

    public String getCpurOrganization() {
        return cpurOrganization;
    }

    public void setCpurOrganization(String cpurOrganization) {
        this.cpurOrganization = cpurOrganization;
    }

    public String getSetDate() {
        return setDate;
    }

    public void setSetDate(String setDate) {
        this.setDate = setDate;
    }

    public String getProvId() {
        return provId;
    }

    public void setProvId(String provId) {
        this.provId = provId;
    }

    public String getProvName() {
        return provName;
    }

    public void setProvName(String provName) {
        this.provName = provName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<PurchaseOrderChildEntity> getPurchaseOrderChilds() {
        return purchaseOrderChilds;
    }

    public void setPurchaseOrderChilds(List<PurchaseOrderChildEntity> purchaseOrderChilds) {
        this.purchaseOrderChilds = purchaseOrderChilds;
    }

    public String getCdeptId() {
        return cdeptId;
    }

    public void setCdeptId(String cdeptId) {
        this.cdeptId = cdeptId;
    }
}
