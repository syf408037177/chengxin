package com.chengxin.sync_job.job;

import com.chengxin.sync_job.biz.IPurchaseOrderBiz;
import com.chengxin.sync_job.util.APICaller;
import org.quartz.DisallowConcurrentExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.stereotype.Service;

import java.io.IOException;
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
    private static Logger log = LoggerFactory.getLogger(SyncPurchaseInJob.class);
    @Autowired
    private IPurchaseOrderBiz purchaseOrderBiz;

    public void sync() {
        log.info("传输采购订单任务执行开始,当前时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        purchaseOrderBiz.pushPurchaseOrderToYy();
        log.info("传输采购订单任务执行结束,当前时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }
}
