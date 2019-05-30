package com.chengxin.sync_job.config;

import com.chengxin.sync_job.job.SyncFailPurchaseOrderJob;
import com.chengxin.sync_job.job.SyncPurchaseInJob;
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
    @Value("${sync_cron}")
    private String syncCron;

    /**
     * 配置采购入库任务
     *
     * @param syncPurchaseInJob syncJob为需要执行的任务
     * @return
     */
    @Bean(name = "syncPurchaseInJobDetail")
    public MethodInvokingJobDetailFactoryBean syncPurchaseInJobDetail(SyncPurchaseInJob syncPurchaseInJob) {
        MethodInvokingJobDetailFactoryBean jobDetail = new MethodInvokingJobDetailFactoryBean();
        // 是否并发执行
        jobDetail.setConcurrent(false);
        // 需要执行的对象
        jobDetail.setTargetObject(syncPurchaseInJob);
        /*
         * 执行QuartzTask类中的需要执行方法
         */
        jobDetail.setTargetMethod("sync");
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
     * @param syncPurchaseInJobDetail 任务
     * @return
     */
    @Bean(name = "syncPurchaseInJobTrigger")
    public CronTriggerFactoryBean syncPurchaseInJobTrigger(JobDetail syncPurchaseInJobDetail) {
        CronTriggerFactoryBean tigger = new CronTriggerFactoryBean();
        tigger.setJobDetail(syncPurchaseInJobDetail);
        //cron表达式，每1分钟执行一次
        tigger.setCronExpression(syncCron);
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
        tigger.setCronExpression(syncCron);
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
        tigger.setCronExpression(syncCron);
        return tigger;
    }

    /**
     * 调度工厂
     *
     * @param syncPurchaseInJobTrigger
     * @param syncPurchaseOrderJobTrigger
     * @return
     */
    @Bean(name = "scheduler1")
    public SchedulerFactoryBean schedulerFactory(Trigger syncPurchaseInJobTrigger, Trigger syncPurchaseOrderJobTrigger) {

        SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();

        // 用于quartz集群,QuartzScheduler 启动时更新己存在的Job
        factoryBean.setOverwriteExistingJobs(true);

        // 延时启动，应用启动1秒后
        factoryBean.setStartupDelay(5);

        // 注册触发器
        factoryBean.setTriggers(syncPurchaseInJobTrigger, syncPurchaseOrderJobTrigger);
        return factoryBean;
    }

    @Bean(name = "scheduler2")
    public SchedulerFactoryBean purchaseOrderFailSchedulerFactory(Trigger syncFailPurchaseOrderJobTrigger) {

        SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();

        // 用于quartz集群,QuartzScheduler 启动时更新己存在的Job
        factoryBean.setOverwriteExistingJobs(true);

        // 延时启动，应用启动1秒后
        factoryBean.setStartupDelay(5);

        // 注册触发器
        factoryBean.setTriggers(syncFailPurchaseOrderJobTrigger);
        return factoryBean;
    }

    public void setSyncCron(String syncCron) {
        this.syncCron = syncCron;
    }
}
