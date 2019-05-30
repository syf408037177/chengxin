package com.chengxin.sync_job.biz;

import com.chengxin.sync_job.domain.PurchaseOrderParentEntity;

import java.util.List;

public interface IPurchaseOrderBiz {

    /**
     * 查询未传输至用友接口的采购订单数据
     * @return
     */
    public List<PurchaseOrderParentEntity> findPurchaseOrderNoTransfer();

    /**
     * 推送采购订单数据
     */
    public void pushPurchaseOrderToYy();

    /**
     * 重推传输失败的采购订单数据
     */
    public void reSyncFailOrder();
}
