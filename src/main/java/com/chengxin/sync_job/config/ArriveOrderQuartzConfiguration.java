package com.chengxin.sync_job.config;

import com.chengxin.sync_job.job.ModifyArriveOrderStatusJob;
import com.chengxin.sync_job.job.SyncFailArriveOrderJob;
import com.chengxin.sync_job.job.SyncArriveOrderJob;
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
@Configuration
public class ArriveOrderQuartzConfiguration {
    //采购入库任务周期
    @Value("${sync_arrive_order_cron}")
    private String syncArriveOrderCron;

    //重推采购入库周期
    @Value("${sync_fail_arrive_order_cron}")
    private String syncFailArriveOrderCron;

    @Value("${modify_order_status_cron}")
    private String modifyOrderStatusCron;

    /**
     * 配置修改收货单状态任务
     *
     * @param modifyArriveOrderStatusJob syncJob为需要执行的任务
     * @return
     */
    @Bean(name = "modifyArriveOrderStatusJobDetail")
    public MethodInvokingJobDetailFactoryBean modifyArriveOrderJobDetail(ModifyArriveOrderStatusJob modifyArriveOrderStatusJob) {
        MethodInvokingJobDetailFactoryBean jobDetail = new MethodInvokingJobDetailFactoryBean();
        // 是否并发执行
        jobDetail.setConcurrent(false);
        // 需要执行的对象
        jobDetail.setTargetObject(modifyArriveOrderStatusJob);
        /*
         * 执行QuartzTask类中的需要执行方法
         */
        jobDetail.setTargetMethod("modify");
        return jobDetail;
    }

    /**
     * 配置收货单推送任务
     *
     * @param syncArriveOrderJob syncJob为需要执行的任务
     * @return
     */
    @Bean(name = "syncArriveOrderJobDetail")
    public MethodInvokingJobDetailFactoryBean syncArriveOrderJobDetail(SyncArriveOrderJob syncArriveOrderJob) {
        MethodInvokingJobDetailFactoryBean jobDetail = new MethodInvokingJobDetailFactoryBean();
        // 是否并发执行
        jobDetail.setConcurrent(false);
        // 需要执行的对象
        jobDetail.setTargetObject(syncArriveOrderJob);
        /*
         * 执行QuartzTask类中的需要执行方法
         */
        jobDetail.setTargetMethod("sync");
        return jobDetail;
    }

    /**
     * 配置重推收货单任务
     *
     * @param syncFailArriveOrderJob syncJob为需要执行的任务
     * @return
     */
    @Bean(name = "syncFailArriveOrderJobDetail")
    public MethodInvokingJobDetailFactoryBean syncFailArriveOrderJobDetail(SyncFailArriveOrderJob syncFailArriveOrderJob) {
        MethodInvokingJobDetailFactoryBean jobDetail = new MethodInvokingJobDetailFactoryBean();
        // 是否并发执行
        jobDetail.setConcurrent(false);
        // 需要执行的对象
        jobDetail.setTargetObject(syncFailArriveOrderJob);
        /*
         * 执行QuartzTask类中的需要执行方法
         */
        jobDetail.setTargetMethod("sync");
        return jobDetail;
    }

    /**
     * 修改采购入库状态任务定时触发器
     *
     * @param modifyArriveOrderStatusJobDetail 任务
     * @return
     */
    @Bean(name = "modifyArriveOrderStatusJobTrigger")
    public CronTriggerFactoryBean modifyArriveOrderJobTrigger(JobDetail modifyArriveOrderStatusJobDetail) {
        CronTriggerFactoryBean tigger = new CronTriggerFactoryBean();
        tigger.setJobDetail(modifyArriveOrderStatusJobDetail);
        //cron表达式，每1分钟执行一次
        tigger.setCronExpression(modifyOrderStatusCron);
        return tigger;
    }

    /**
     * 收货单定时触发器
     *
     * @param syncArriveOrderJobDetail 任务
     * @return
     */
    @Bean(name = "syncArriveOrderJobTrigger")
    public CronTriggerFactoryBean syncPurchaseOrderJobTrigger(JobDetail syncArriveOrderJobDetail) {
        CronTriggerFactoryBean tigger = new CronTriggerFactoryBean();
        tigger.setJobDetail(syncArriveOrderJobDetail);
        //cron表达式，每1分钟执行一次
        tigger.setCronExpression(syncArriveOrderCron);
        return tigger;
    }

    /**
     * 重推收货单定时触发器
     *
     * @param syncFailArriveOrderJobDetail 任务
     * @return
     */
    @Bean(name = "syncFailArriveOrderJobTrigger")
    public CronTriggerFactoryBean syncFailPurchaseOrderJobTrigger(JobDetail syncFailArriveOrderJobDetail) {
        CronTriggerFactoryBean tigger = new CronTriggerFactoryBean();
        tigger.setJobDetail(syncFailArriveOrderJobDetail);
        //cron表达式，每1分钟执行一次
        tigger.setCronExpression(syncFailArriveOrderCron);
        return tigger;
    }

    /**
     * 调度工厂
     *
     * @param syncArriveOrderJobTrigger
     * @return
     */
    @Bean(name = "schedulerIn1")
    public SchedulerFactoryBean schedulerFactory(Trigger syncArriveOrderJobTrigger) {

        SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();

        // 用于quartz集群,QuartzScheduler 启动时更新己存在的Job
        factoryBean.setOverwriteExistingJobs(true);

        // 延时启动，应用启动1秒后
        factoryBean.setStartupDelay(10);

        // 注册触发器
        factoryBean.setTriggers(syncArriveOrderJobTrigger);
        return factoryBean;
    }

    @Bean(name = "schedulerIn2")
    public SchedulerFactoryBean ArriveOrderFailSchedulerFactory(Trigger syncFailArriveOrderJobTrigger) {

        SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();

        // 用于quartz集群,QuartzScheduler 启动时更新己存在的Job
        factoryBean.setOverwriteExistingJobs(true);

        // 延时启动，应用启动1秒后
        factoryBean.setStartupDelay(10);

        // 注册触发器
        factoryBean.setTriggers(syncFailArriveOrderJobTrigger);
        return factoryBean;
    }

    @Bean(name = "schedulerIn3")
    public SchedulerFactoryBean modifyInStatusSchedulerFactory(Trigger modifyArriveOrderStatusJobTrigger) {

        SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();

        // 用于quartz集群,QuartzScheduler 启动时更新己存在的Job
        factoryBean.setOverwriteExistingJobs(true);

        // 延时启动，应用启动1秒后
        factoryBean.setStartupDelay(10);

        // 注册触发器
        factoryBean.setTriggers(modifyArriveOrderStatusJobTrigger);
        return factoryBean;
    }

}
