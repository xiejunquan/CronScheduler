package com.scheduler;

/**
 * bean工厂, 用于获取配置的类实例
 * @author 谢俊权
 * @create 2016/9/12 17:04
 */
public interface BeanFactory {

    <T> T get(String id);

    <T> T get(Class<T> clazz);

}
