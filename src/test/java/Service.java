import com.scheduler.annotation.Scheduler;

/**
 * @author 谢俊权
 * @create 2016/9/12 17:58
 */
public class Service {

    @Scheduler(cron = "0/20 * * * * ?", params = {"2", "hello"})
    public void put(int id, String name){
        System.out.println(System.currentTimeMillis() + "service put id:" + id + ", name:" + name);
    }

    @Scheduler(cron = "0/10 * * * * ?")
    public void pop(){
        System.out.println(System.currentTimeMillis() + "service pop ");
    }

    public void put(String key, String name){
        System.out.println(System.currentTimeMillis() + "service put key:" + key + ", name:" + name);
    }
}
