package nio_timeserver.server;

import bio_timeserver.server.TimeServerHandler;

/**
 * Created by Orion on 2018/7/9.
 */
public class server {
    public static void main(String[] args){
        int port=8080;
        if(args!=null&&args.length>0){
            port=Integer.valueOf(args[0]);
        }
        serverHandler serverHandler = new serverHandler(port);
        new Thread(serverHandler).start();
    }
}
