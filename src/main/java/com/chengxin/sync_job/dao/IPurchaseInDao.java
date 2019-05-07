package com.chengxin.sync_job.dao;

import com.chengxin.sync_job.domain.PurchaseInEntity;
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
    public List<PurchaseInEntity> findPurchaseInNoTransfer();
}
