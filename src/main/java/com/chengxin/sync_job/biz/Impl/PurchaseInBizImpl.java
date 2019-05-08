package com.chengxin.sync_job.biz.Impl;

import com.chengxin.sync_job.biz.IPurchaseInBiz;
import com.chengxin.sync_job.dao.IPurchaseInDao;
import com.chengxin.sync_job.domain.PurchaseInParentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Mr.Song
 * @create 2019-05-06 16:07
 * @desc 采购入库biz
 **/
@Service
public class PurchaseInBizImpl implements IPurchaseInBiz {
    @Autowired
    private IPurchaseInDao purchaseInDao;

    @Override
    public List<PurchaseInParentEntity> findPurchaseInNoTransfer() {
        return purchaseInDao.findPurchaseInParentNotTransfer();
    }
}
