package com.chengxin.sync_job.job;


import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Mr.Song
 * @create 2019-05-05 9:43
 * @desc 同步采购入库数据job
 **/
@Service
public class SyncPurchaseInJob {

    public void sync() {
        System.out.println("任务执行一次,时间:" + new Date());
    }
}
