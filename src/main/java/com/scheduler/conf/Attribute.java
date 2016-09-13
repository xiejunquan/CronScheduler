package com.scheduler.conf;

/**
 * @author 谢俊权
 * @create 2016/9/12 10:14
 */
public enum Attribute {

    KEY("key"),
    VALUE("value"),
    TYPE("type"),
    ID("id"),
    CLASS("class"),
    NAME("name"),
    CRON_EXPRESSION("cronExpression");

    private String aName;

    Attribute(String aName) {
        this.aName = aName;
    }


    @Override
    public String toString() {
        return aName;
    }
}
