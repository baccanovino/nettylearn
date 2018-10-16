package nio_timeserver.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Orion on 2018/7/11.
 */
public class TimeClientHandler implements Runnable {
    private String host;
    private  int port;
    private Selector selector;
    private SocketChannel socketChannel;
    private volatile boolean stop;

    public TimeClientHandler(String host, int port){
        this.host = host;
        this.port = port;
        try{
            selector = Selector.open();
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            doconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while(!stop){
            try {
                selector.select(1000);
                Set<SelectionKey> sk = selector.selectedKeys();
                Iterator<SelectionKey> it = sk.iterator();
                SelectionKey key =null;
                while (it.hasNext()){
                    key = it.next();
                    it.remove();
                    try{
                        handle(key);
                    }catch (Exception e){
                        key.cancel();
                        if(key.channel()!=null){
                            key.channel().close();
                        }
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        if(selector != null){

            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void handle(SelectionKey key) throws IOException {
        if(key.isValid()){
            SocketChannel sc = (SocketChannel) key.channel();
            if(key.isConnectable()) {
                if (sc.finishConnect()) {
                    sc.register(selector, SelectionKey.OP_READ);
                    dowrite(sc);
                } else {
                    System.exit(1);
                }
            }
                if(key.isReadable()){
                    ByteBuffer readbuffer = ByteBuffer.allocate(1024);
                    int rb = sc.read(readbuffer);
                    if(rb>0){
                        readbuffer.flip();
                        byte[] b = new byte[readbuffer.remaining()];
                        readbuffer.get(b);
                        String body = new String(b,"UTF-8");
                        System.out.print("time is :"+body);
                        this.stop=true;
                    }else if(rb<0){
                        key.cancel();
                        sc.close();
                    }else{
                        ;
                    }
                }

        }
    }

    private void doconnect() throws IOException {
        if(socketChannel.connect(new InetSocketAddress(host,port))){
            socketChannel.register(selector, SelectionKey.OP_READ);
            dowrite(socketChannel);
        }else{
            socketChannel.register(selector,SelectionKey.OP_CONNECT);
        }
    }

    private void dowrite(SocketChannel socketChannel) throws IOException {
        byte[] b = "time".getBytes();
        ByteBuffer bb = ByteBuffer.allocate(b.length);
        bb.put(b);
        bb.flip();
        socketChannel.write(bb);
        if(!bb.hasRemaining()){
            System.out.println("success");
        }
    }
}
