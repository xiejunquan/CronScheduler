package com.scheduler;

import com.scheduler.conf.SchedulerConfig;
import com.scheduler.conf.TaskConfig;
import com.scheduler.conf.SchedulerConfigParser;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Properties;

/**
 * 定时器
 * @author 谢俊权
 * @create 2016/9/12 16:42
 */
public class TimingScheduler {

    private static final Logger logger = LoggerFactory.getLogger(TimingScheduler.class);

    private SchedulerConfig schedulerConfig;

    private Scheduler scheduler;

    private BeanFactory beanFactory;

    public TimingScheduler(String configName, BeanFactory beanFactory) {
        SchedulerConfigParser parser = new SchedulerConfigParser(configName);
        this.schedulerConfig = parser.get();
        this.beanFactory = beanFactory;
        initScheduler();
    }

    protected void initScheduler(){
        try {
            Properties properties = this.schedulerConfig.getQuartzConfig().getProperties();
            SchedulerFactory sf = new StdSchedulerFactory(properties);
            this.scheduler = sf.getScheduler();
            scheduler.setJobFactory(new TimingJobFactory());
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    public void start(){

        List<TaskConfig> taskConfigs = schedulerConfig.getTaskConfigs();
        for (int group = 0; group < taskConfigs.size(); group++) {
            TaskConfig taskConfig = taskConfigs.get(group);

            TimingJobFactory.put(taskConfig, beanFactory);

            String id = taskConfig.getId();
            String clazzName = taskConfig.getClazz();
            List<TaskConfig.MethodConfig> methodConfigs = taskConfig.getMethodConfigs();
            for (int trig = 0; trig < methodConfigs.size(); trig++) {
                TaskConfig.MethodConfig methodConfig = methodConfigs.get(trig);
                String methodName = methodConfig.getName();
                String cronExpression = methodConfig.getCronExpression();

                JobDataMap jobDataMap = new JobDataMap();
                jobDataMap.put("id", id);
                jobDataMap.put("clazzName", clazzName);
                jobDataMap.put("methodName", methodName);

                JobDetail job = JobBuilder.newJob(TimingJob.class)
                        .withIdentity("job" + trig, "group" + group)
                        .usingJobData(jobDataMap)
                        .build();

                CronTrigger trigger = TriggerBuilder.newTrigger()
                        .withIdentity("trigger" + trig, "group" + group)
                        .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                        .build();

                try {
                    scheduler.scheduleJob(job, trigger);
                } catch (SchedulerException e) {
                    logger.error("error to schedule job class {} method {}", clazzName, methodName);
                }
            }
        }
        try {
            scheduler.start();
        } catch (SchedulerException e) {
            logger.error("error to start scheduler", e);
        }
    }

    public void destroy(){
        try {
            scheduler.shutdown();
        } catch (SchedulerException e) {
            logger.error("error shutdown", e);
        }
    }
}
