package com.scheduler.conf;

import java.util.List;

/**
 * 配置文件的Scheduler信息
 * @author 谢俊权
 * @create 2016/9/12 10:23
 */
public class SchedulerConfig {

    private QuartzConfig quartzConfig;
    private List<TaskConfig> taskConfigs;

    public SchedulerConfig(QuartzConfig quartzConfig, List<TaskConfig> taskConfigs) {
        this.quartzConfig = quartzConfig;
        this.taskConfigs = taskConfigs;
    }

    public QuartzConfig getQuartzConfig() {
        return quartzConfig;
    }

    public List<TaskConfig> getTaskConfigs() {
        return taskConfigs;
    }
}
