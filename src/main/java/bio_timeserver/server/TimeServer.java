package bio_timeserver.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Orion on 2018/7/4.
 */
public class TimeServer {

    public static void main(String[] args){
        int port = 8087;
        if(args!=null&&args.length>=1){
            port = Integer.valueOf(args[0]);
        }
        ServerSocket server = null;
        try {
            server = new ServerSocket(port);
            while(true){
                Socket socket = server.accept();
                new Thread(new TimeServerHandler(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
