package com.scheduler.annotation;

import com.scheduler.configure.ParamType;
import com.scheduler.configure.TaskConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 缓存注解的扫描器
 * @author 谢俊权
 * @create 2016/7/25 10:24
 */
public class AnnotationScanner {

    private static final Logger logger = LoggerFactory.getLogger(AnnotationScanner.class);

    /**
     * 扫描项目根下所有带有缓存注解的类
     * @return 类注解信息列表
     */
    public static List<TaskConfig> scan(){

        List<TaskConfig> result = new ArrayList<>();
        Set<Class<?>> classSet = PackageScanner.getClasses();
        for(Class<?> clazz : classSet){
            Method[] methods = clazz.getDeclaredMethods();
            List<TaskConfig.MethodConfig> methodConfigs = getMethodConfigs(methods);
            if(!methodConfigs.isEmpty()){
                TaskConfig taskConfig = new TaskConfig(null, clazz.getName(), methodConfigs);
                result.add(taskConfig);
            }
        }
        return result;
    }

    private static List<TaskConfig.MethodConfig> getMethodConfigs(Method[] methods){
        List<TaskConfig.MethodConfig> methodConfigs = new ArrayList<>();
        for(Method method : methods){
            Crontab crontab = method.getAnnotation(Crontab.class);
            if(crontab != null){
                String cronExpression = crontab.value();
                String[] params = crontab.params();
                Class<?>[] paramClasses = method.getParameterTypes();
                if(paramClasses.length == params.length){
                    List<TaskConfig.Param> paramList = getMethodParams(paramClasses, params);
                    if(paramList != null){
                        TaskConfig.MethodConfig methodConfig = new TaskConfig.MethodConfig(method.getName(),cronExpression, paramList);
                        methodConfigs.add(methodConfig);
                    }else{
                        logger.error("method {}, @Crontab only support byte,short,int,long,float,double,char,string type", method.getName());
                    }
                }
            }
        }
        return methodConfigs;
    }

    private static List<TaskConfig.Param> getMethodParams(Class<?>[] paramClasses, String[] params){
        List<TaskConfig.Param> paramList = new ArrayList<>();
        int index = 0;
        for(Class<?> paramClass : paramClasses){
            String name =  paramClass.getSimpleName().toLowerCase();
            ParamType paramType = ParamType.lookup(name);
            if(paramType != null){
                Object object = paramType.convert(params[index++]);
                TaskConfig.Param param = new TaskConfig.Param(paramType.getType(), object);
                paramList.add(param);
            }
        }
        if(index != paramClasses.length){
            return null;
        }
        return paramList;
    }



}
