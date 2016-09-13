package com.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 谢俊权
 * @create 2016/9/12 17:06
 */
public class SimpleBeanFactory implements BeanFactory{

    private static final Logger logger = LoggerFactory.getLogger(SimpleBeanFactory.class);

    @Override
    public <T> T get(String id) {
        return null;
    }

    @Override
    public <T> T get(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            logger.error("error to new {} instance", clazz.getName());
        }
        return null;
    }
}
