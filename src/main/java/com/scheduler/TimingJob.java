package com.scheduler;

import com.scheduler.configure.TaskConfig;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 定时器的执行类, 根据配置执行类方法
 * @author 谢俊权
 * @create 2016/9/12 17:30
 */
public class TimingJob implements Job{

    private static final Logger logger = LoggerFactory.getLogger(TimingJob.class);

    private String id;
    private String clazzName;
    private String methodName;
    private List<TaskConfig.Param> methodParams;
    private BeanFactory beanFactory;

    public TimingJob(String id, String clazzName, String methodName, List<TaskConfig.Param> methodParams, BeanFactory beanFactory) {
        this.id = id;
        this.clazzName = clazzName;
        this.methodName = methodName;
        this.methodParams = methodParams;
        this.beanFactory = beanFactory;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Class clazz = null;
        try {
            clazz = Class.forName(clazzName);
        } catch (ClassNotFoundException e) {
            logger.error("error forName class {}", clazzName, e);
        }

        Method executeMethod = getSameMethod(clazz, methodName, methodParams);

        if(executeMethod == null){
            logger.error("not found method {}", methodName);
        }else{
            Object object = beanFactory.get(id);
            if(object == null){
                object = beanFactory.get(clazz);
            }
            try {
                executeMethod.invoke(object, getParamValue(methodParams).toArray());
            } catch (Exception e) {
                logger.error("error to object {} invoke method {}", object, executeMethod.getName(), e);
            }
        }
    }

    private Method getSameMethod(Class clazz, String name, List<TaskConfig.Param> params){
        Method executeMethod = null;
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            String methodN = method.getName();
            if(methodN.equals(name)){
                Class[] types = method.getParameterTypes();
                if(types.length == params.size()){
                    boolean isSame = true;
                    for (int i = 0; i < types.length; i++) {
                        if(!isSameType(types[i], params.get(i).getType())){
                            isSame = false;
                            break;
                        }
                    }
                    if(isSame){
                        executeMethod = method;
                    }
                }
            }
        }
        return executeMethod;
    }

    private boolean isSameType(Class<?> type, Class<?> clazz){
        if(!type.isPrimitive()){
            return type.equals(clazz);
        }else{
            Class<?> warpper =
                    (type.equals(byte.class)) ? Byte.class :
                            (type.equals(boolean.class)) ? Boolean.class :
                                    (type.equals(char.class)) ? Character.class :
                                            (type.equals(short.class)) ? Short.class :
                                                    (type.equals(int.class)) ? Integer.class :
                                                            (type.equals(long.class)) ? Long.class :
                                                                    (type.equals(float.class)) ? Float.class :
                                                                            (type.equals(double.class)) ? Double.class :
                                                                                    Object.class;


            return warpper.equals(clazz);
        }
    }

    private List<Object> getParamValue(List<TaskConfig.Param> params){
        List<Object> list = new ArrayList<>();
        for (TaskConfig.Param param : params) {
            list.add(param.getValue());
        }
        return list;
    }
}
