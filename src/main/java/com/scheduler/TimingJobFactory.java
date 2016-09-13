package com.scheduler;

import com.scheduler.conf.TaskConfig;
import org.quartz.*;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 谢俊权
 * @create 2016/9/12 16:40
 */
public class TimingJobFactory implements JobFactory {

    private static final Map<String, TimingJob> jobMap = new HashMap<>();

    public Job newJob(TriggerFiredBundle bundle, Scheduler scheduler) throws SchedulerException {

        JobDetail detail = bundle.getJobDetail();
        JobDataMap dataMap = detail.getJobDataMap();
        String id = dataMap.getString("id");
        String clazzName = dataMap.getString("clazzName");
        String methodName = dataMap.getString("methodName");

        return jobMap.get(getKey(id, clazzName, methodName));
    }

    public static void put(TaskConfig taskConfig, BeanFactory beanFactory){
        String id = taskConfig.getId();
        String clazz = taskConfig.getClazz();
        List<TaskConfig.MethodConfig> methodConfigs = taskConfig.getMethodConfigs();
        for(TaskConfig.MethodConfig methodConfig : methodConfigs){
            String methodName = methodConfig.getName();
            List<TaskConfig.Param> params = methodConfig.getParams();
            TimingJob job = new TimingJob(id, clazz, methodName, params, beanFactory);
            jobMap.put(getKey(id, clazz, methodName), job);
        }
    }

    private static String getKey(String id, String clazz, String methodName){
        return String.valueOf(id + "|" + clazz + "|" + methodName);
    }
}
