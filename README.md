# TimingScheduler

## 简介
此项目是一个封装quartz实现的一个定时调度器，支持配置和注解两种方式

## 使用

编写注解

    @Crontab(value = "0/20 * * * * ?", params = {"2", "hello"})
    public void put(int id, String name){
        System.out.println(System.currentTimeMillis() + "service put id:" + id + ", name:" + name);
    }

    @Crontab("0/10 * * * * ?")
    public void pop(){
        System.out.println(System.currentTimeMillis() + "service pop ");
    }

启动调度器

    TimingScheduler scheduler = new TimingScheduler();
    scheduler.start();

上面这种方式只启用了注解的功能，而且只使用了quartz的默认配置。 如果需要启用定时配置和quartz参数配置，需要编写xml文件

    <?xml version="1.0" encoding="UTF-8" ?>
    <!DOCTYPE scheduler SYSTEM "scheduler.dtd">
    <scheduler>
        <quartz-config>
            <property key="org.quartz.scheduler.instanceName" value="DefaultQuartzScheduler"></property>
            <property key="org.quartz.scheduler.rmi.export" value="false"></property>
            <property key="org.quartz.scheduler.rmi.proxy" value="false"></property>
            <property key="org.quartz.scheduler.wrapJobExecutionInUserTransaction" value="false"></property>
            <property key="org.quartz.threadPool.threadCount" value="20"></property>
            <property key="org.quartz.threadPool.threadPriority" value="5"></property>
            <property key="org.quartz.threadPool.threadsInheritContextClassLoaderOfInitializingThread" value="true"></property>
            <property key="org.quartz.jobStore.misfireThreshold" value="60000"></property>
        </quartz-config>
        <tasks>
            <task id="service" class="Service">
                <method name="put"  cronExpression="0/30 * * * * ?">
                    <param value="1" type="string"></param>
                    <param value="ha" type="string"></param>
                </method>
            </task>
        </tasks>
    </scheduler>

启动调度器

    TimingScheduler scheduler = new TimingScheduler("scheduler.xml");
    scheduler.start();


