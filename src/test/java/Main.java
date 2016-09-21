import com.scheduler.CronScheduler;

/**
 * @author 谢俊权
 * @create 2016/9/12 11:54
 */
public class Main {

    public static void main(String[] args) throws Exception {

        CronScheduler scheduler = new CronScheduler("scheduler.xml");
        scheduler.start();

        Thread.sleep(1000 * 60);

        scheduler.destroy();

    }
}
