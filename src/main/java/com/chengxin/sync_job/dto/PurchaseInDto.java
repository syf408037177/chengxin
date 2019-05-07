package com.chengxin.sync_job.dto;

/**
 * @author Mr.Song
 * @create 2019-05-06 13:54
 * @desc 采购入库传输实体
 **/
public class PurchaseInDto {
    private String cbiztype;//业务类型
    private String coperatorid;//操作员
    private String cwarehouseid;//仓库ID
    private String pk_calbody;//库存组织
    private String pk_corp;//公司
    private String dbilldate;//单据日期
    private String cproviderid;//供应商

    public String getCbiztype() {
        return cbiztype;
    }

    public void setCbiztype(String cbiztype) {
        this.cbiztype = cbiztype;
    }

    public String getCoperatorid() {
        return coperatorid;
    }

    public void setCoperatorid(String coperatorid) {
        this.coperatorid = coperatorid;
    }

    public String getCwarehouseid() {
        return cwarehouseid;
    }

    public void setCwarehouseid(String cwarehouseid) {
        this.cwarehouseid = cwarehouseid;
    }

    public String getPk_calbody() {
        return pk_calbody;
    }

    public void setPk_calbody(String pk_calbody) {
        this.pk_calbody = pk_calbody;
    }

    public String getPk_corp() {
        return pk_corp;
    }

    public void setPk_corp(String pk_corp) {
        this.pk_corp = pk_corp;
    }

    public String getDbilldate() {
        return dbilldate;
    }

    public void setDbilldate(String dbilldate) {
        this.dbilldate = dbilldate;
    }

    public String getCproviderid() {
        return cproviderid;
    }

    public void setCproviderid(String cproviderid) {
        this.cproviderid = cproviderid;
    }
}
