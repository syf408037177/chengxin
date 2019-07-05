package com.chengxin.sync_job.job;

import com.chengxin.sync_job.biz.IArriveOrderBiz;
import com.chengxin.sync_job.biz.IPurchaseInBiz;
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
 * @desc 修改收货单状态job
 **/
@Service
@DisallowConcurrentExecution
public class ModifyArriveOrderStatusJob {
    private static Logger log = LoggerFactory.getLogger(ModifyArriveOrderStatusJob.class);
    @Autowired
    private IArriveOrderBiz arriveOrderBiz;

    public void modify() {
        log.info(Constants.MODIFY_TAG + "修改收货单状态任务执行开始");
        arriveOrderBiz.modifyOrderStatus();
        log.info(Constants.MODIFY_TAG + "修改收货单状态任务执行结束");
    }
}
