package com.chengxin.sync_job.dao.erp;

import com.chengxin.sync_job.domain.PurchaseInParentEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("purchaseInDao")
@Mapper
public interface IPurchaseInDao {

    /**
     * 查询未传输至用友的采购入库单数据
     * @return
     */
    public List<PurchaseInParentEntity> findPurchaseInParentNotTransfer(@Param("purchaseInCount") Long purchaseInCount, @Param("dgType") String dgType);

    /**
     * 查询传输失败的采购入库单数据
     * @return
     */
    public PurchaseInParentEntity  findPurchaseInParentFail(@Param("dgType") String dgType);

    /**
     * 根据主表id查询子表数据
     * @param parentId
     * @return
     */
    public List<PurchaseInParentEntity> findPurchaseInChildByParentId(String parentId);

    /**
     * 修改采购入库单推送状态
     * @param parentIds
     * @param yyFinishFlag
     * @return
     */
    public Integer updatePurchaseInStatus(@Param("parentIds") List<String> parentIds, @Param("yyFinishFlag") String yyFinishFlag);

    /**
     * 将校验失败的单号状态置为0(可继续传输)
     * @return
     */
    public Integer modifyFailOrderStatus();
}
