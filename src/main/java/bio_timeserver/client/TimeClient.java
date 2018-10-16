package bio_timeserver.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Orion on 2018/7/4.
 */
public class TimeClient {
    public static void main(String[] args){
        int port = 8087;
        if(args!=null && args.length>=1){
            port = Integer.valueOf(args[0]);
        }
        BufferedReader in = null;
        PrintWriter out = null;
        Socket socket = null;
        try{
            socket = new Socket("127.0.0.1",port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(),true);
            out.println("time");
            System.out.println(">>>>>>>>>>>>"+in.readLine());
            out.println("notime");
            System.out.println(">>>>>>>>>>>>"+in.readLine());
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(in!=null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(out!=null){
                out.close();
            }
            if(socket!=null){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
