package com.chengxin.sync_job.config;

import com.chengxin.sync_job.job.*;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * @author Mr.Song
 * @create 2019-05-05 9:50
 * @desc 采购入库quartz配置
 **/
//@Configuration
public class PurchaseInQuartzConfiguration {
    //采购入库任务周期
    @Value("${sync_purchase_in_cron}")
    private String syncPurchaseInCron;

    //重推采购入库周期
    @Value("${sync_fail_purchase_in_cron}")
    private String syncFailPurchaseInCron;

    @Value("${modify_order_status_cron}")
    private String modifyOrderStatusCron;

    /**
     * 配置修改采购入库单状态任务
     *
     * @param modifyPurchaseInStatusJob syncJob为需要执行的任务
     * @return
     */
    @Bean(name = "modifyPurchaseInStatusJobDetail")
    public MethodInvokingJobDetailFactoryBean modifyPurchaseInJobDetail(ModifyPurchaseInStatusJob modifyPurchaseInStatusJob) {
        MethodInvokingJobDetailFactoryBean jobDetail = new MethodInvokingJobDetailFactoryBean();
        // 是否并发执行
        jobDetail.setConcurrent(false);
        // 需要执行的对象
        jobDetail.setTargetObject(modifyPurchaseInStatusJob);
        /*
         * 执行QuartzTask类中的需要执行方法
         */
        jobDetail.setTargetMethod("modify");
        return jobDetail;
    }

    /**
     * 配置采购入库单推送任务
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
     * 配置重推采购入库单任务
     *
     * @param syncFailPurchaseInJob syncJob为需要执行的任务
     * @return
     */
    @Bean(name = "syncFailPurchaseInJobDetail")
    public MethodInvokingJobDetailFactoryBean syncFailPurchaseInJobDetail(SyncFailPurchaseInJob syncFailPurchaseInJob) {
        MethodInvokingJobDetailFactoryBean jobDetail = new MethodInvokingJobDetailFactoryBean();
        // 是否并发执行
        jobDetail.setConcurrent(false);
        // 需要执行的对象
        jobDetail.setTargetObject(syncFailPurchaseInJob);
        /*
         * 执行QuartzTask类中的需要执行方法
         */
        jobDetail.setTargetMethod("sync");
        return jobDetail;
    }

    /**
     * 修改采购入库状态任务定时触发器
     *
     * @param modifyPurchaseInStatusJobDetail 任务
     * @return
     */
    @Bean(name = "modifyPurchaseInStatusJobTrigger")
    public CronTriggerFactoryBean modifyPurchaseInJobTrigger(JobDetail modifyPurchaseInStatusJobDetail) {
        CronTriggerFactoryBean tigger = new CronTriggerFactoryBean();
        tigger.setJobDetail(modifyPurchaseInStatusJobDetail);
        //cron表达式，每1分钟执行一次
        tigger.setCronExpression(modifyOrderStatusCron);
        return tigger;
    }

    /**
     * 采购订单定时触发器
     *
     * @param syncPurchaseInJobDetail 任务
     * @return
     */
    @Bean(name = "syncPurchaseInJobTrigger")
    public CronTriggerFactoryBean syncPurchaseOrderJobTrigger(JobDetail syncPurchaseInJobDetail) {
        CronTriggerFactoryBean tigger = new CronTriggerFactoryBean();
        tigger.setJobDetail(syncPurchaseInJobDetail);
        //cron表达式，每1分钟执行一次
        tigger.setCronExpression(syncPurchaseInCron);
        return tigger;
    }

    /**
     * 重推采购订单定时触发器
     *
     * @param syncFailPurchaseInJobDetail 任务
     * @return
     */
    @Bean(name = "syncFailPurchaseInJobTrigger")
    public CronTriggerFactoryBean syncFailPurchaseOrderJobTrigger(JobDetail syncFailPurchaseInJobDetail) {
        CronTriggerFactoryBean tigger = new CronTriggerFactoryBean();
        tigger.setJobDetail(syncFailPurchaseInJobDetail);
        //cron表达式，每1分钟执行一次
        tigger.setCronExpression(syncFailPurchaseInCron);
        return tigger;
    }

    /**
     * 调度工厂
     *
     * @param syncPurchaseInJobTrigger
     * @return
     */
    @Bean(name = "schedulerIn1")
    public SchedulerFactoryBean schedulerFactory(Trigger syncPurchaseInJobTrigger) {

        SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();

        // 用于quartz集群,QuartzScheduler 启动时更新己存在的Job
        factoryBean.setOverwriteExistingJobs(true);

        // 延时启动，应用启动1秒后
        factoryBean.setStartupDelay(10);

        // 注册触发器
        factoryBean.setTriggers(syncPurchaseInJobTrigger);
        return factoryBean;
    }

    @Bean(name = "schedulerIn2")
    public SchedulerFactoryBean purchaseInFailSchedulerFactory(Trigger syncFailPurchaseInJobTrigger) {

        SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();

        // 用于quartz集群,QuartzScheduler 启动时更新己存在的Job
        factoryBean.setOverwriteExistingJobs(true);

        // 延时启动，应用启动1秒后
        factoryBean.setStartupDelay(10);

        // 注册触发器
        factoryBean.setTriggers(syncFailPurchaseInJobTrigger);
        return factoryBean;
    }

    @Bean(name = "schedulerIn3")
    public SchedulerFactoryBean modifyInStatusSchedulerFactory(Trigger modifyPurchaseInStatusJobTrigger) {

        SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();

        // 用于quartz集群,QuartzScheduler 启动时更新己存在的Job
        factoryBean.setOverwriteExistingJobs(true);

        // 延时启动，应用启动1秒后
        factoryBean.setStartupDelay(10);

        // 注册触发器
        factoryBean.setTriggers(modifyPurchaseInStatusJobTrigger);
        return factoryBean;
    }

}
