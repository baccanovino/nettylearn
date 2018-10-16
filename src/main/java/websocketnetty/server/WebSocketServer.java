package websocketnetty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * Created by Orion on 2018/7/25.
 */
public class WebSocketServer {
    public void bind(int port){
        ServerBootstrap bootstrap = new ServerBootstrap();
        NioEventLoopGroup workgroup = new NioEventLoopGroup();
        NioEventLoopGroup bossgroup = new NioEventLoopGroup();
        bootstrap.group(bossgroup,workgroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel sc) throws Exception {
                        sc.pipeline().addLast("http-codec",new HttpServerCodec());
                        sc.pipeline().addLast("aggregator",new HttpObjectAggregator(65536));
                        sc.pipeline().addLast("http-chunked",new ChunkedWriteHandler());
                        sc.pipeline().addLast("handler",new WebSocketServerHandler());
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
        new WebSocketServer().bind(port);
    }
}
