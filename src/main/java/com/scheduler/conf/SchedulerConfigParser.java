package com.scheduler.conf;

import com.sun.org.apache.xerces.internal.impl.Constants;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 解析scheduler配置文件
 *
 * @author 谢俊权
 * @create 2016/1/30 20:20
 */
public class SchedulerConfigParser {

    private static final Logger logger = LoggerFactory.getLogger(SchedulerConfigParser.class);

    private String configName;

    public SchedulerConfigParser(String configName) {
        this.configName = configName;
    }

    public SchedulerConfig get(){
        org.dom4j.Element root = getRoot(configName);
        return getConfig(root);
    }

    private org.dom4j.Element getRoot(String configFileName){

        org.dom4j.Element root = null;
        try{
            SAXReader reader = new SAXReader();
            reader.setFeature(Constants.XERCES_FEATURE_PREFIX + Constants.LOAD_EXTERNAL_DTD_FEATURE, false);
            String url = ClassLoader.getSystemResource(configFileName).getPath();
            Document document = reader.read(url.substring(1));
            root = document.getRootElement();
        }catch (Exception e){
            logger.error("error to load xml {}", configFileName, e);
        }
        return root;
    }

    private SchedulerConfig getConfig(org.dom4j.Element rootElement){

        org.dom4j.Element quartzElement = rootElement.element(Element.QUARTZ_CONFIG.toString());
        org.dom4j.Element tasksElement = rootElement.element(Element.TASKS.toString());

        QuartzConfig quartzConfig = getQuartzConfig(quartzElement);
        List<TaskConfig> taskConfigs = getTaskConfigs(tasksElement);
        SchedulerConfig schedulerConfig = new SchedulerConfig(quartzConfig, taskConfigs);

        return schedulerConfig;
    }

    private QuartzConfig getQuartzConfig(org.dom4j.Element configElement){
        QuartzConfig quartzConfig = new QuartzConfig();
        List<org.dom4j.Element> propertyElements = configElement.elements(Element.PROPERTY.toString());
        for (org.dom4j.Element propertyElement : propertyElements) {
            org.dom4j.Attribute keyAttribute = propertyElement.attribute(Attribute.KEY.toString());
            org.dom4j.Attribute valueAttribute = propertyElement.attribute(Attribute.VALUE.toString());
            String key = keyAttribute.getStringValue();
            String value = valueAttribute.getStringValue();
            quartzConfig.put(key, value);
        }
        return quartzConfig;
    }

    private List<TaskConfig> getTaskConfigs(org.dom4j.Element tasksElement){
        List<TaskConfig> taskConfigs = new ArrayList<>();
        List<org.dom4j.Element> taskElements = tasksElement.elements(Element.TASK.toString());
        for (org.dom4j.Element taskElement : taskElements) {

            org.dom4j.Attribute idAttribute = taskElement.attribute(Attribute.ID.toString());
            org.dom4j.Attribute classAttribute = taskElement.attribute(Attribute.CLASS.toString());
            List<org.dom4j.Element> methodElements = taskElement.elements(Element.METHOD.toString());
            List<TaskConfig.MethodConfig> methodConfigs = getMethodConfigs(methodElements);
            String id = idAttribute.getStringValue();
            String clazz = classAttribute.getStringValue();
            TaskConfig taskConfig = new TaskConfig(id, clazz, methodConfigs);
            taskConfigs.add(taskConfig);
        }
        return taskConfigs;
    }

    private List<TaskConfig.MethodConfig> getMethodConfigs(List<org.dom4j.Element> methodElements){
        List<TaskConfig.MethodConfig> methodConfigs = new ArrayList<>();
        for (org.dom4j.Element methodElement : methodElements) {
            org.dom4j.Attribute nameAttribute = methodElement.attribute(Attribute.NAME.toString());
            org.dom4j.Attribute cronAttribute = methodElement.attribute(Attribute.CRON_EXPRESSION.toString());
            List<org.dom4j.Element> paramElements = methodElement.elements(Element.PARAM.toString());
            List<TaskConfig.Param> params = new ArrayList<>();
            for (org.dom4j.Element paramElement : paramElements) {
                org.dom4j.Attribute typeAttribute = paramElement.attribute(Attribute.TYPE.toString());
                org.dom4j.Attribute valueAttribute = paramElement.attribute(Attribute.VALUE.toString());
                String typeName = typeAttribute.getStringValue();
                String value = valueAttribute.getStringValue();
                ParamType paramType = ParamType.lookup(typeName);
                Class type = paramType.getType();
                Object object = paramType.convert(value);
                TaskConfig.Param param = new TaskConfig.Param(type, object);
                params.add(param);
            }
            String name = nameAttribute.getStringValue();
            String cron = cronAttribute.getStringValue();
            TaskConfig.MethodConfig methodConfig = new TaskConfig.MethodConfig(name, cron, params);
            methodConfigs.add(methodConfig);
        }
        return methodConfigs;
    }
}
