package com.chengxin.sync_job.job;

import com.chengxin.sync_job.biz.IArriveOrderBiz;
import com.chengxin.sync_job.common.Constants;
import org.quartz.DisallowConcurrentExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Mr.Song
 * @create 2019-05-27 13:24
 * @desc 重推传输失败的收货单job
 **/
@Service
@DisallowConcurrentExecution
public class SyncFailArriveOrderJob {
    private static Logger log = LoggerFactory.getLogger(SyncFailArriveOrderJob.class);
    @Autowired
    private IArriveOrderBiz arriveOrderBiz;

    public void sync() {
        log.info(Constants.RE_PUSH_TAG + "重推传输失败的收货单任务执行开始");
        arriveOrderBiz.reSyncFailOrder();
        log.info(Constants.RE_PUSH_TAG + "重推传输失败的收货单任务执行结束");
    }
}
