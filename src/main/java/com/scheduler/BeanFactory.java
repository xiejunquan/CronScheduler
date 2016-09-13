package com.scheduler;

/**
 * @author 谢俊权
 * @create 2016/9/12 17:04
 */
public interface BeanFactory {

    <T> T get(String id);

    <T> T get(Class<T> clazz);

}
