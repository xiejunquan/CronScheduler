package com.scheduler.configure;

import java.util.Properties;

/**
 * quartz配置信息
 * @author 谢俊权
 * @create 2016/9/12 9:56
 */
public class QuartzConfig {

    private Properties properties = new Properties();

    public void put(String key, String value){
        properties.put(key, value);
    }

    public String get(String key){
        return (String) properties.get(key);
    }

    public Properties getProperties() {
        return properties;
    }
}
