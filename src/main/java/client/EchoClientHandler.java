package client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * Created by Orion on 2018/5/27.
 */
@ChannelHandler.Sharable
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
    @Override
    public void channelActive(ChannelHandlerContext ctx){
        ctx.writeAndFlush(Unpooled.copiedBuffer("Netty!!!!!!!!!!!!!! ricks!",CharsetUtil.UTF_8));
    }

    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        System.out.println("Client received:"+byteBuf.toString(CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause){
        cause.printStackTrace();
        ctx.close();
    }
}
