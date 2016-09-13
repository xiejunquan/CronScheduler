package com.scheduler.conf;

/**
 * 配置文件的元素名称枚举
 * @author 谢俊权
 * @create 2016/9/12 10:14
 */
public enum Element {

    SCHEDULER("scheduler"),
    QUARTZ_CONFIG("quartz-config"),
    PROPERTY("property"),
    TASKS("tasks"),
    TASK("task"),
    METHOD("method"),
    PARAM("param");



    private String eName;

    Element(String eName) {
        this.eName = eName;
    }


    @Override
    public String toString() {
        return eName;
    }
}
