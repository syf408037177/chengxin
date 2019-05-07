package com.chengxin.sync_job.biz.Impl;

import com.chengxin.sync_job.biz.IPurchaseInBiz;
import com.chengxin.sync_job.domain.PurchaseInEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Mr.Song
 * @create 2019-05-06 16:07
 * @desc 采购入库biz
 **/
@Service
public class PurchaseInBizImpl implements IPurchaseInBiz {

    @Override
    public List<PurchaseInEntity> findPurchaseInNoTransfer() {
        return null;
    }
}
