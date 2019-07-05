package com.chengxin.sync_job.job;


import com.chengxin.sync_job.biz.IArriveOrderBiz;
import com.chengxin.sync_job.common.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Mr.Song
 * @create 2019-05-05 9:43
 * @desc 同步收货单数据job
 **/
@Service
public class SyncArriveOrderJob {
    @Autowired
    private IArriveOrderBiz arriveOrderBiz;

    private static Logger log = LoggerFactory.getLogger(SyncArriveOrderJob.class);

    public void sync() {
        log.info(Constants.PUSH_TAG + "传输收货单任务执行开始");
        arriveOrderBiz.pushArriveOrderToYy();
        log.info(Constants.PUSH_TAG + "传输收货单任务执行结束");
    }
}

