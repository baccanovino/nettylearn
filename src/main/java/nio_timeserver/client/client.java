package nio_timeserver.client;

/**
 * Created by Orion on 2018/7/11.
 */
public class client {
    public static void main(String[] args){
        int port =8080;
        if(args!=null&&args.length>0){
            try{
                port = Integer.valueOf(args[0]);
            }catch (Exception e){

            }
        }
        new Thread(new TimeClientHandler("127.0.0.1",port)).start();
    }
}
