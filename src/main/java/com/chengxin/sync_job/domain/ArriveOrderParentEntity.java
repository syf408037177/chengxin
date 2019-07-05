package com.chengxin.sync_job.domain;

import java.util.List;

/**
 * @author Mr.Song
 * @create 2019-05-06 13:49
 * @desc 收货单主表数据实体
 **/
public class ArriveOrderParentEntity {
    private String code;//到货单ID
    private String billNo;//单号
    private String pkCode;//所属公司
    private String provName;//供应商名称
    private String cdeptid;//部门id
    private String receiveDate;//到货日期
    private String status;//传输状态
    private List<ArriveOrderChildEntity> arriveOrderChildEntities;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
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

    public String getCdeptid() {
        return cdeptid;
    }

    public void setCdeptid(String cdeptid) {
        this.cdeptid = cdeptid;
    }

    public String getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(String receiveDate) {
        this.receiveDate = receiveDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ArriveOrderChildEntity> getArriveOrderChildEntities() {
        return arriveOrderChildEntities;
    }

    public void setArriveOrderChildEntities(List<ArriveOrderChildEntity> arriveOrderChildEntities) {
        this.arriveOrderChildEntities = arriveOrderChildEntities;
    }
}
