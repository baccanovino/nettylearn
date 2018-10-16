package server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * Created by Orion on 2018/5/23.
 */
public class EchoServer {
    private static int port;

    public EchoServer(int port){
        this.port=port;
    }

    public static void main(String[] args) throws Exception{
        /*if(args.length != 1){
            System.err.println("usage:"+EchoServer.class.getSimpleName()+"<port>");
            return;
        }*/
        int port = Integer.parseInt("8889");
        new EchoServer(port).start();
    }

    public void start() throws Exception{
        NioEventLoopGroup group = new NioEventLoopGroup();
        try{
            ServerBootstrap b = new ServerBootstrap();
            b.group(group)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>(){
                        public void initChannel(SocketChannel sc) throws Exception {
                               sc.pipeline().addLast(new EchoServerHandler());
                        }
                    });
            ChannelFuture cf=b.bind().sync();
            System.out.println(EchoServer.class.getName()+"start and Listen on"+cf.channel().localAddress());
            cf.channel().closeFuture().sync();

        }finally {
            group.shutdownGracefully().sync();
        }
    }
}
