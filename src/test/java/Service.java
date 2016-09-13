/**
 * @author 谢俊权
 * @create 2016/9/12 17:58
 */
public class Service {

    public void get(int id, String name){
        System.out.println(System.currentTimeMillis() + "service get " + id + ":" + name);
    }

    public void pop(){
        System.out.println(System.currentTimeMillis() + "service pop ");
    }
}
