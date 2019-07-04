package com.chengxin.sync_job.config;

import com.chengxin.sync_job.job.ModifyPurchaseOrderStatusJob;
import com.chengxin.sync_job.job.SyncFailPurchaseOrderJob;
import com.chengxin.sync_job.job.SyncPurchaseOrderJob;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * @author Mr.Song
 * @create 2019-05-05 9:50
 * @desc quartz配置
 **/
@Configuration
public class QuartzConfiguration {
    //采购订单任务周期
    @Value("${sync_purchase_order_cron}")
    private String syncPurchaseOrderCron;

    //重推采购订单周期
    @Value("${sync_fail_purchase_order_cron}")
    private String syncFailPurchaseOrderCron;

    @Value("${modify_order_status_cron}")
    private String modifyOrderStatusCron;

    /**
     * 配置修改采购订单状态任务
     *
     * @param modifyPurchaseOrderStatusJob syncJob为需要执行的任务
     * @return
     */
    @Bean(name = "modifyPurchaseOrderStatusJobDetail")
    public MethodInvokingJobDetailFactoryBean modifyPurchaseOrderJobDetail(ModifyPurchaseOrderStatusJob modifyPurchaseOrderStatusJob) {
        MethodInvokingJobDetailFactoryBean jobDetail = new MethodInvokingJobDetailFactoryBean();
        // 是否并发执行
        jobDetail.setConcurrent(false);
        // 需要执行的对象
        jobDetail.setTargetObject(modifyPurchaseOrderStatusJob);
        /*
         * 执行QuartzTask类中的需要执行方法
         */
        jobDetail.setTargetMethod("modify");
        return jobDetail;
    }

    /**
     * 配置采购订单推送任务
     *
     * @param syncPurchaseOrderJob syncJob为需要执行的任务
     * @return
     */
    @Bean(name = "syncPurchaseOrderJobDetail")
    public MethodInvokingJobDetailFactoryBean syncPurchaseOrderJobDetail(SyncPurchaseOrderJob syncPurchaseOrderJob) {
        MethodInvokingJobDetailFactoryBean jobDetail = new MethodInvokingJobDetailFactoryBean();
        // 是否并发执行
        jobDetail.setConcurrent(false);
        // 需要执行的对象
        jobDetail.setTargetObject(syncPurchaseOrderJob);
        /*
         * 执行QuartzTask类中的需要执行方法
         */
        jobDetail.setTargetMethod("sync");
        return jobDetail;
    }

    /**
     * 配置重推采购订单任务
     *
     * @param syncFailPurchaseOrderJob syncJob为需要执行的任务
     * @return
     */
    @Bean(name = "syncFailPurchaseOrderJobDetail")
    public MethodInvokingJobDetailFactoryBean syncFailPurchaseOrderJobDetail(SyncFailPurchaseOrderJob syncFailPurchaseOrderJob) {
        MethodInvokingJobDetailFactoryBean jobDetail = new MethodInvokingJobDetailFactoryBean();
        // 是否并发执行
        jobDetail.setConcurrent(false);
        // 需要执行的对象
        jobDetail.setTargetObject(syncFailPurchaseOrderJob);
        /*
         * 执行QuartzTask类中的需要执行方法
         */
        jobDetail.setTargetMethod("sync");
        return jobDetail;
    }

    /**
     * 采购入库定时触发器
     *
     * @param modifyPurchaseOrderStatusJobDetail 任务
     * @return
     */
    @Bean(name = "modifyPurchaseOrderStatusJobTrigger")
    public CronTriggerFactoryBean modifyPurchaseOrderJobTrigger(JobDetail modifyPurchaseOrderStatusJobDetail) {
        CronTriggerFactoryBean tigger = new CronTriggerFactoryBean();
        tigger.setJobDetail(modifyPurchaseOrderStatusJobDetail);
        //cron表达式，每1分钟执行一次
        tigger.setCronExpression(modifyOrderStatusCron);
        return tigger;
    }

    /**
     * 采购订单定时触发器
     *
     * @param syncPurchaseOrderJobDetail 任务
     * @return
     */
    @Bean(name = "syncPurchaseOrderJobTrigger")
    public CronTriggerFactoryBean syncPurchaseOrderJobTrigger(JobDetail syncPurchaseOrderJobDetail) {
        CronTriggerFactoryBean tigger = new CronTriggerFactoryBean();
        tigger.setJobDetail(syncPurchaseOrderJobDetail);
        //cron表达式，每1分钟执行一次
        tigger.setCronExpression(syncPurchaseOrderCron);
        return tigger;
    }

    /**
     * 重推采购订单定时触发器
     *
     * @param syncFailPurchaseOrderJobDetail 任务
     * @return
     */
    @Bean(name = "syncFailPurchaseOrderJobTrigger")
    public CronTriggerFactoryBean syncFailPurchaseOrderJobTrigger(JobDetail syncFailPurchaseOrderJobDetail) {
        CronTriggerFactoryBean tigger = new CronTriggerFactoryBean();
        tigger.setJobDetail(syncFailPurchaseOrderJobDetail);
        //cron表达式，每1分钟执行一次
        tigger.setCronExpression(syncFailPurchaseOrderCron);
        return tigger;
    }

    /**
     * 调度工厂
     *
     * @param syncPurchaseOrderJobTrigger
     * @return
     */
    @Bean(name = "scheduler1")
    public SchedulerFactoryBean schedulerFactory(Trigger syncPurchaseOrderJobTrigger) {

        SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();

        // 用于quartz集群,QuartzScheduler 启动时更新己存在的Job
        factoryBean.setOverwriteExistingJobs(true);

        // 延时启动，应用启动1秒后
        factoryBean.setStartupDelay(10);

        // 注册触发器
        factoryBean.setTriggers(syncPurchaseOrderJobTrigger);
        return factoryBean;
    }

    @Bean(name = "scheduler2")
    public SchedulerFactoryBean purchaseOrderFailSchedulerFactory(Trigger syncFailPurchaseOrderJobTrigger) {

        SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();

        // 用于quartz集群,QuartzScheduler 启动时更新己存在的Job
        factoryBean.setOverwriteExistingJobs(true);

        // 延时启动，应用启动1秒后
        factoryBean.setStartupDelay(10);

        // 注册触发器
        factoryBean.setTriggers(syncFailPurchaseOrderJobTrigger);
        return factoryBean;
    }

    @Bean(name = "scheduler3")
    public SchedulerFactoryBean modifyOrderStatusSchedulerFactory(Trigger modifyPurchaseOrderStatusJobTrigger) {

        SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();

        // 用于quartz集群,QuartzScheduler 启动时更新己存在的Job
        factoryBean.setOverwriteExistingJobs(true);

        // 延时启动，应用启动1秒后
        factoryBean.setStartupDelay(10);

        // 注册触发器
        factoryBean.setTriggers(modifyPurchaseOrderStatusJobTrigger);
        return factoryBean;
    }

}
