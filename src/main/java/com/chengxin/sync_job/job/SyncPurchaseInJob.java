package com.chengxin.sync_job.job;


import com.chengxin.sync_job.biz.IPurchaseInBiz;
import com.chengxin.sync_job.domain.PurchaseInChildEntity;
import com.chengxin.sync_job.domain.PurchaseInParentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author Mr.Song
 * @create 2019-05-05 9:43
 * @desc 同步采购入库数据job
 **/
@Service
public class SyncPurchaseInJob {
    @Autowired
    private IPurchaseInBiz purchaseInBiz;

    public void sync() {
        System.out.println("任务执行一次,时间:" + new Date());
        List<PurchaseInParentEntity> purchaseInParentList = purchaseInBiz.findPurchaseInNoTransfer();
        for (PurchaseInParentEntity entity : purchaseInParentList) {
            System.out.println(entity);
            List<PurchaseInChildEntity> childs = entity.getPurchaseInChilds();
            for (PurchaseInChildEntity child : childs) {
                System.out.println(child);
            }
        }
    }
}
