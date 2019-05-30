package com.chengxin.sync_job.biz;

import com.chengxin.sync_job.domain.PurchaseInParentEntity;

import java.util.List;

public interface IPurchaseInBiz {

    /**
     * 查询未传输至用友接口的采购入库数据
     * @return
     */
    public List<PurchaseInParentEntity> findPurchaseInNoTransfer();

    /**
     * 获取采购入库数据
     * @return
     */
    public String getPurchase();
}
