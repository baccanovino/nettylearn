package netty.server;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * Created by Orion on 2018/7/19.
 */
public class TimeServer {
    public void bind(int port){
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        ServerBootstrap  b = new ServerBootstrap();
        b.group(bossGroup,workGroup);
        b.channel(NioServerSocketChannel.class);
        b.option(ChannelOption.SO_BACKLOG,1024);
        b.childHandler(new childChannelHandler());
        try {
            ChannelFuture f = b.bind(port).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    private class childChannelHandler extends ChannelInitializer<SocketChannel>{

        protected void initChannel(SocketChannel socketChannel) throws Exception {
            socketChannel.pipeline().addLast(new LineBasedFrameDecoder(1024));
            socketChannel.pipeline().addLast(new StringDecoder());
            socketChannel.pipeline().addLast( new TimeServerHandler());
        }
    }

    public static void main(String[] args){
        int port = 8080;
        if(args!=null&&args.length>0){
            port = Integer.parseInt(args[0]);
        }
        new TimeServer().bind(port);
    }
}
