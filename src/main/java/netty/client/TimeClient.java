package netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * Created by Orion on 2018/7/20.
 */
public class TimeClient {

    public void connect(int port,String host){
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY,true).handler(new clienthandler());
        try {
            ChannelFuture f = b.connect(host,port).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally{
            group.shutdownGracefully();
        }
    }

    private class clienthandler extends ChannelInitializer<SocketChannel>{
        protected void initChannel(SocketChannel socketChannel) throws Exception {
            socketChannel.pipeline().addLast(new LineBasedFrameDecoder(1024));
            socketChannel.pipeline().addLast(new StringDecoder());
            socketChannel.pipeline().addLast(new handler());
        }
    }

    public static void main(String[] args){
        int port = 8080;
        if(args!=null&&args.length>0){
            port = Integer.parseInt(args[0]);
        }
        new TimeClient().connect(port,"127.0.0.1");
    }
}
