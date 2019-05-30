package com.chengxin.sync_job.dao.erp;

import com.chengxin.sync_job.domain.PurchaseInChildEntity;
import com.chengxin.sync_job.domain.PurchaseInParentEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("purchaseInDao")
@Mapper
public interface IPurchaseInDao {

    /**
     * 查询未传输至用友的采购入库数据
     * @return
     */
    public List<PurchaseInParentEntity> findPurchaseInParentNotTransfer();

    /**
     * 根据主表id查询字表数据
     * @param parentId
     * @return
     */
    public List<PurchaseInChildEntity> findPurchaseInChildByParentId(String parentId);
}
