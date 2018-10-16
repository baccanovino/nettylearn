package websocketnetty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

/**
 * Created by Orion on 2018/7/28.
 */
public class SocorClient {
    public void connect(int port){
        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY,true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel sc) throws Exception {
                        sc.pipeline().addLast(new ObjectDecoder(1024, ClassResolvers.cacheDisabled(this.getClass().getClassLoader())));
                        sc.pipeline().addLast(new ObjectEncoder());
                        sc.pipeline().addLast(new ClientHandler());
                    }
                });
        try {
            ChannelFuture f = b.connect("127.0.0.1",port).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args){
        int port = 8080;
        if(args!=null&&args.length>0){
            port = Integer.parseInt(args[0]);
        }
        new SocorClient().connect(port);
    }
}
