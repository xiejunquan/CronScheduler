import com.scheduler.SimpleBeanFactory;
import com.scheduler.TimingScheduler;

/**
 * @author 谢俊权
 * @create 2016/9/12 11:54
 */
public class Main {

    public static void main(String[] args) throws Exception {

        TimingScheduler scheduler = new TimingScheduler("scheduler.xml", new SimpleBeanFactory());
        scheduler.start();

        Thread.sleep(1000 * 60);

        scheduler.destory();

    }
}
