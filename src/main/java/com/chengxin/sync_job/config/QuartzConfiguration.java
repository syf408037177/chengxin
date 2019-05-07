package com.chengxin.sync_job.config;

import com.chengxin.sync_job.job.SyncPurchaseInJob;
import org.quartz.*;
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

    /**
     *  配置任务
     * @param syncPurchaseInJob syncJob为需要执行的任务
     * @return
     */
    @Bean(name = "reptilianJob")
    public MethodInvokingJobDetailFactoryBean detailFactoryBean(SyncPurchaseInJob syncPurchaseInJob) {

        MethodInvokingJobDetailFactoryBean jobDetail = new MethodInvokingJobDetailFactoryBean();

        // 是否并发执行
        jobDetail.setConcurrent(false);

        // 设置任务的名字
        jobDetail.setName("reptilianJob");

        // 设置任务的分组，在多任务的时候使用
        jobDetail.setGroup("reptilianJobGroup");

        // 需要执行的对象
        jobDetail.setTargetObject(syncPurchaseInJob);

        /*
         * 执行QuartzTask类中的需要执行方法
         */
        jobDetail.setTargetMethod("sync");
        return jobDetail;
    }

    /**
     * 定时触发器
     * @param syncJob 任务
     * @return
     */
    @Bean(name = "jobTrigger")
    public CronTriggerFactoryBean cronJobTrigger(JobDetail syncJob){

        CronTriggerFactoryBean tigger = new CronTriggerFactoryBean();

        tigger.setJobDetail(syncJob);

        //cron表达式，每1分钟执行一次
        tigger.setCronExpression("0 0/1 * * * ?");
        tigger.setName("syncTrigger");
        return tigger;
    }

    /**
     * 调度工厂
     * @param jobTrigger 触发器
     * @return
     */
    @Bean(name = "scheduler")
    public SchedulerFactoryBean schedulerFactory(Trigger jobTrigger) {

        SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();

        // 用于quartz集群,QuartzScheduler 启动时更新己存在的Job
        factoryBean.setOverwriteExistingJobs(true);

        // 延时启动，应用启动1秒后
        factoryBean.setStartupDelay(1);

        // 注册触发器
        factoryBean.setTriggers(jobTrigger);
        return factoryBean;
    }
}
