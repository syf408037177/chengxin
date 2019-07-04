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
 * @desc 修改采购订单状态job
 **/
@Service
@DisallowConcurrentExecution
public class ModifyPurchaseOrderStatusJob {
    private static Logger log = LoggerFactory.getLogger(ModifyPurchaseInStatusJob.class);
    @Autowired
    private IPurchaseOrderBiz purchaseOrderBiz;

    public void modify() {
        log.info(Constants.MODIFY_TAG + "修改采购订单状态任务执行开始,当前时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        purchaseOrderBiz.modifyOrderStatus();
        log.info(Constants.MODIFY_TAG + "修改采购订单状态任务执行结束,当前时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }
}
