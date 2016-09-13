package com.scheduler;

import com.scheduler.conf.TaskConfig;
import org.quartz.*;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;

import java.util.List;

/**
 * @author 谢俊权
 * @create 2016/9/12 16:40
 */
public class TimingJobFactory implements JobFactory {

    public Job newJob(TriggerFiredBundle bundle, Scheduler scheduler) throws SchedulerException {

        JobDetail detail = bundle.getJobDetail();
        JobDataMap dataMap = detail.getJobDataMap();
        String id = dataMap.getString("id");
        String clazzName = dataMap.getString("clazzName");
        String methodName = dataMap.getString("methodName");
        List<TaskConfig.Param> methodParams = (List<TaskConfig.Param>) dataMap.get("methodParams");
        BeanFactory beanFactory = (BeanFactory) dataMap.get("beanFactory");

        return new TimingJob(id, clazzName, methodName, methodParams, beanFactory);
    }
}
