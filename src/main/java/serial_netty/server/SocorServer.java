package serial_netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolver;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * Created by Orion on 2018/7/25.
 */
public class SocorServer {
    public void bind(int port){
        ServerBootstrap bootstrap = new ServerBootstrap();
        NioEventLoopGroup workgroup = new NioEventLoopGroup();
        NioEventLoopGroup bossgroup = new NioEventLoopGroup();
        bootstrap.group(bossgroup,workgroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG,100)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel sc) throws Exception {
                        sc.pipeline().addLast(new ObjectDecoder(1024*1024, ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
                        sc.pipeline().addLast(new ObjectEncoder());
                        sc.pipeline().addLast(new ServerHandler());
                    }
                });

        try {
            ChannelFuture f =bootstrap.bind(port).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            workgroup.shutdownGracefully();
            bossgroup.shutdownGracefully();
        }
    }

    public static void main(String[] args){
        int port = 8080;
        if(args!=null && args.length>0){
            port = Integer.parseInt(args[0]);
        }
        new SocorServer().bind(port);
    }
}
