package com.scheduler.conf;

import java.util.List;

/**
 * @author 谢俊权
 * @create 2016/9/12 9:56
 */
public class TaskConfig {

    private String id;
    private String clazz;
    private List<MethodConfig> methodConfigs;


    public TaskConfig(String id, String clazz, List<MethodConfig> methodConfigs) {
        this.id = id;
        this.clazz = clazz;
        this.methodConfigs = methodConfigs;
    }

    public String getId() {
        return id;
    }

    public String getClazz() {
        return clazz;
    }

    public List<MethodConfig> getMethodConfigs() {
        return methodConfigs;
    }

    public static class MethodConfig{
        private String name;
        private String cronExpression;
        private List<Param> params;

        public MethodConfig(String name, String cronExpression, List<Param> params) {
            this.name = name;
            this.cronExpression = cronExpression;
            this.params = params;
        }

        public String getName() {
            return name;
        }

        public String getCronExpression() {
            return cronExpression;
        }

        public List<Param> getParams() {
            return params;
        }
    }

    public static class Param{
        private Class type;
        private Object value;

        public Param(Class type, Object value) {
            this.type = type;
            this.value = value;
        }

        public Class getType() {
            return type;
        }

        public Object getValue() {
            return value;
        }
    }
}
