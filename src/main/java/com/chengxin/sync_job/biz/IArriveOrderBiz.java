package com.chengxin.sync_job.biz;

import com.chengxin.sync_job.domain.ArriveOrderParentEntity;
import com.chengxin.sync_job.domain.PurchaseInParentEntity;

import java.util.List;

public interface IArriveOrderBiz {

    /**
     * 查询未传输至用友接口的采购入库单数据
     * @return
     */
    public List<ArriveOrderParentEntity> findArriveOrderNoTransfer();

    /**
     * 推送采购入库单数据
     */
    public void pushArriveOrderToYy();

    /**
     * 重推传输失败的采购入库单数据
     */
    public void reSyncFailOrder();

    /**
     * 修改采购入库单状态
     */
    public void modifyOrderStatus();
}
