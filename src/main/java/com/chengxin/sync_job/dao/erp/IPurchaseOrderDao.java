package com.chengxin.sync_job.dao.erp;

import com.chengxin.sync_job.domain.PurchaseOrderChildEntity;
import com.chengxin.sync_job.domain.PurchaseOrderParentEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 采购订单Dao
 */
@Repository("purchaseOrderDao")
@Mapper
public interface IPurchaseOrderDao {

    /**
     * 查询未传输至用友的采购订单数据
     * @return
     */
    public List<PurchaseOrderParentEntity> findPurchaseOrderParentNotTransfer(Long purchaseOrderCount);

    /**
     * 查询传输失败的采购订单数据
     * @return
     */
    public PurchaseOrderParentEntity  findPurchaseOrderParentFail();

    /**
     * 根据主表id查询子表数据
     * @param parentId
     * @return
     */
    public List<PurchaseOrderChildEntity> findPurchaseOrderChildByParentId(String parentId);

    /**
     * 修改采购订单推送状态
     * @param parentIds
     * @param yyFinishFlag
     * @return
     */
    public Integer updatePurchaseOrderStatus(@Param("parentIds") List<String> parentIds, @Param("yyFinishFlag") String yyFinishFlag);
}
