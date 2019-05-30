package com.chengxin.sync_job.job;


import com.chengxin.sync_job.biz.IPurchaseInBiz;
import com.chengxin.sync_job.domain.PurchaseInChildEntity;
import com.chengxin.sync_job.domain.PurchaseInParentEntity;
import com.chengxin.sync_job.util.APICaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
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

    @Value("${purchasePushIp}")
    private String ip;

    @Value("${purchaseServiceName}")
    private String serviceName;

    private static Logger log = LoggerFactory.getLogger(SyncPurchaseInJob.class);

    public void sync() {
//        String json = purchaseInBiz.getPurchase();
//        System.out.println(json);
//        try {
//            String result = APICaller.call(ip, serviceName, "code", json);
//            System.out.println(result);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}

