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
 * @desc 重推传输失败的采购订单job
 **/
@Service
@DisallowConcurrentExecution
public class SyncFailPurchaseOrderJob {
    private static Logger log = LoggerFactory.getLogger(SyncPurchaseInJob.class);
    @Autowired
    private IPurchaseOrderBiz purchaseOrderBiz;

    public void sync() {
        log.info(Constants.RE_PUSH_TAG + "重推传输失败的采购订单任务执行开始,当前时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        purchaseOrderBiz.reSyncFailOrder();
        log.info(Constants.RE_PUSH_TAG + "重推传输失败的采购订单任务执行结束,当前时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }
}
