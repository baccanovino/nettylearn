package server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * Created by Orion on 2018/5/24.
 */
@ChannelHandler.Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {
    public void channelRead(ChannelHandlerContext  ctx, Object msg){
        ByteBuf in = (ByteBuf) msg;
        System.out.println("Server received:"+in.toString(CharsetUtil.UTF_8));
        ctx.write(in);
    }
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception{
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
                .addListener(ChannelFutureListener.CLOSE);
    }
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause){
        cause.printStackTrace();
        ctx.close();
    }
}
