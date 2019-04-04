package com.paradise.transitmonitor.scheduler;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 项目启动时，初始化加载quartz任务
 *
 * @author dzhang
 */
@Service
public class MyJobs implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(MyJobs.class);

    @Resource
    private MyJobService myJobService;

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info(">>> afterPropertiesSet");

        List<QuartzInfo> quartzInfoList = myJobService.selectEnableJobList();
        if (!quartzInfoList.isEmpty()) {
            for (QuartzInfo quartzInfo : quartzInfoList) {
                initSchedule(quartzInfo);
            }
        } else {
            logger.info("no enable job ...");
            QuartzInfo info = new QuartzInfo();
            info.setCron("0 1/10 * * * ?");
            info.setIdentityGroup("Transit");
            info.setIdentityName("Monitor");
            info.setJobClassName("com.paradise.transitmonitor.scheduler.MonitorForTransit");
            initSchedule(info);
        }
    }

    @SuppressWarnings("unchecked")
    private static void initSchedule(QuartzInfo quartzInfo) throws SchedulerException {
        logger.info("job :" + quartzInfo.toString() + " initializing ...");
        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
        scheduler.start();

        JobDetail jobDetail = null;
        try {
            jobDetail = JobBuilder.newJob((Class<? extends Job>) Class.forName(quartzInfo.getJobClassName()))
                    .withIdentity(quartzInfo.getIdentityName(), quartzInfo.getIdentityGroup())
                    .withDescription(quartzInfo.getDescription())
                    .build();
        } catch (ClassNotFoundException e) {
            logger.error("job :" + quartzInfo.getDescription() + " initialize fail!");
            logger.error(e.getLocalizedMessage(), e);
            return;
        }

        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder
                .cronSchedule(quartzInfo.getCron());

        CronTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(quartzInfo.getIdentityName(), quartzInfo.getIdentityGroup())
                .withSchedule(scheduleBuilder).build();
        scheduler.scheduleJob(jobDetail, trigger);

        logger.info("job initialize success!");
    }
}
