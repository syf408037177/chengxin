package com.chengxin.sync_job.job;

import com.chengxin.sync_job.biz.IPurchaseOrderBiz;
import com.chengxin.sync_job.common.Constants;
import org.quartz.DisallowConcurrentExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Mr.Song
 * @create 2019-05-27 13:24
 * @desc 传输采购订单至用友job
 **/
@Service
@DisallowConcurrentExecution
public class SyncPurchaseOrderJob {
    private static Logger log = LoggerFactory.getLogger(SyncPurchaseOrderJob.class);
    @Autowired
    private IPurchaseOrderBiz purchaseOrderBiz;

    public void sync() {
        log.info(Constants.PUSH_TAG + "传输采购订单任务执行开始,当前时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        purchaseOrderBiz.pushPurchaseOrderToYy();
        log.info(Constants.PUSH_TAG + "传输采购订单任务执行结束,当前时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }
}
