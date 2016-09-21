package com.scheduler;

import com.scheduler.annotation.AnnotationScanner;
import com.scheduler.configure.SchedulerConfig;
import com.scheduler.configure.TaskConfig;
import com.scheduler.configure.SchedulerConfigParser;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 定时器
 * @author 谢俊权
 * @create 2016/9/12 16:42
 */
public class CronScheduler {

    private static final Logger logger = LoggerFactory.getLogger(CronScheduler.class);

    private SchedulerConfig schedulerConfig;

    private Scheduler scheduler;

    private BeanFactory beanFactory;

    public CronScheduler() {
        this(null);
    }

    public CronScheduler(String configName) {
        this(configName, new SimpleBeanFactory());
    }

    public CronScheduler(String configName, BeanFactory beanFactory) {
        if(configName != null){
            SchedulerConfigParser parser = new SchedulerConfigParser(configName);
            this.schedulerConfig = parser.get();
        }
        this.beanFactory = beanFactory;
        initScheduler();
    }

    protected void initScheduler(){
        try {
            SchedulerFactory sf = null;
            if(schedulerConfig == null){
                sf = new StdSchedulerFactory();
            }else{
                Properties properties = this.schedulerConfig.getQuartzConfig().getProperties();
                sf = new StdSchedulerFactory(properties);
            }
            this.scheduler = sf.getScheduler();
            this.scheduler.setJobFactory(new CronJobFactory());
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    public void start(){

        List<TaskConfig> taskConfigs = new ArrayList<>();
        if(schedulerConfig != null){
            List<TaskConfig> configTasks = schedulerConfig.getTaskConfigs();
            taskConfigs.addAll(configTasks);
        }
        List<TaskConfig> annotateTasks = AnnotationScanner.scan();
        taskConfigs.addAll(annotateTasks);
        for (int group = 0; group < taskConfigs.size(); group++) {
            TaskConfig taskConfig = taskConfigs.get(group);

            CronJobFactory.put(taskConfig, beanFactory);

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

                JobDetail job = JobBuilder.newJob(CronJob.class)
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
