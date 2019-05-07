package com.chengxin.sync_job.biz;

import com.chengxin.sync_job.domain.PurchaseInEntity;

import java.util.List;

public interface IPurchaseInBiz {

    /**
     * 查询未传输至用友接口的采购入库数据
     * @return
     */
    public List<PurchaseInEntity> findPurchaseInNoTransfer();
}
