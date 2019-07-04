package com.chengxin.sync_job.job;


import com.chengxin.sync_job.biz.IPurchaseInBiz;
import com.chengxin.sync_job.common.Constants;
import com.chengxin.sync_job.domain.PurchaseInChildEntity;
import com.chengxin.sync_job.domain.PurchaseInParentEntity;
import com.chengxin.sync_job.util.APICaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
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

    private static Logger log = LoggerFactory.getLogger(SyncPurchaseInJob.class);

    public void sync() {
        log.info(Constants.PUSH_TAG + "传输采购订单任务执行开始,当前时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        purchaseInBiz.pushPurchaseInToYy();
        log.info(Constants.PUSH_TAG + "传输采购订单任务执行结束,当前时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }
}

