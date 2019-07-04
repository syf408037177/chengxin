package com.chengxin.sync_job.biz;

import com.chengxin.sync_job.domain.PurchaseInParentEntity;

import java.util.List;

public interface IPurchaseInBiz {

    /**
     * 查询未传输至用友接口的采购入库单数据
     * @return
     */
    public List<PurchaseInParentEntity> findPurchaseInNoTransfer();

    /**
     * 推送采购入库单数据
     */
    public void pushPurchaseInToYy();

    /**
     * 重推传输失败的采购入库单数据
     */
    public void reSyncFailOrder();

    /**
     * 修改采购入库单状态
     */
    public void modifyOrderStatus();
}
