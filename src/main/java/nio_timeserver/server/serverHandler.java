package nio_timeserver.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Orion on 2018/7/9.
 */
public class serverHandler implements Runnable {
    private Selector selector;
    private ServerSocketChannel serversocketchannel;
    private volatile boolean stop;
    
    public serverHandler(int port){
        try {
            selector = Selector.open();
            serversocketchannel = ServerSocketChannel.open();
            serversocketchannel.configureBlocking(false);
            serversocketchannel.socket().bind(new InetSocketAddress(port),1024);
            serversocketchannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.print("start in port:"+port);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    
    
    public void stop(){
        this.stop=true;
    }
    
    public void run() {
        while (!stop) {
            try {
                selector.select(1000);
                Set<SelectionKey> selectionKeySets = selector.selectedKeys();
                Iterator<SelectionKey> it = selectionKeySets.iterator();
                SelectionKey key = null;
                while(it.hasNext()){
                    key=it.next();
                    it.remove();
                    try {
                        handlekey(key);
                    }catch (Exception e){
                        if(key!=null){
                            key.cancel();
                            if(key.channel()!=null){
                                key.channel().close();
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(selector!=null){
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handlekey(SelectionKey key) throws IOException {
        if(key.isValid()){
            if(key.isAcceptable()){
                ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                SocketChannel sc = ssc.accept();
                sc.configureBlocking(false);
                sc.register(selector,SelectionKey.OP_READ);
            }
            if(key.isReadable()){
                SocketChannel sc = (SocketChannel) key.channel();
                ByteBuffer readbuffer = ByteBuffer.allocate(1024);
                int readbyte = sc.read(readbuffer);
                if(readbyte > 0){
                    readbuffer.flip();
                    byte[] bytes = new byte[readbuffer.remaining()];
                    readbuffer.get(bytes);
                    String body = new String(bytes,"UTF-8");
                    System.out.println("recevice order :"+ body);
                    String currtime = "time".equalsIgnoreCase(body)? new Date(System.currentTimeMillis()).toString():"Bad order";
                    doWrite(sc,currtime);
                }else if(readbyte<0){
                    key.channel();
                    sc.close();
                }else{
                    ;
                }
            }
        }
    }

    private void doWrite(SocketChannel sc, String currtime) throws IOException {
        if(currtime!=null&&currtime.trim().length()>0){
            byte[] b = currtime.getBytes();
            ByteBuffer writebuffer = ByteBuffer.allocate(b.length);
            writebuffer.put(b);
            writebuffer.flip();
            sc.write(writebuffer);
        }
    }
}
