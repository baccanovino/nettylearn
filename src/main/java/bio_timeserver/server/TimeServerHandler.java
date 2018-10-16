package bio_timeserver.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

/**
 * Created by Orion on 2018/7/4.
 */
public class TimeServerHandler implements Runnable{
    private Socket socket;

    public TimeServerHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            out = new PrintWriter(this.socket.getOutputStream(),true);
            String s;
            while(true){
                s=in.readLine();
                if(s!=null){
                    if(s.equals("time")){
                        out.println(new Date().toString());
                    }else{
                        out.println("error commond");
                    }
                }else{
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(in!=null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                out.close();
            }
            if(socket!=null){
                try {
                    this.socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        this.socket = null;
    }
}
