package netty.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;

/**
 * Created by Orion on 2018/7/20.
 */
public class TimeServerHandler extends ChannelInboundHandlerAdapter {
    private int counter;

    public void channelRead(ChannelHandlerContext ctx,Object msg){
        String order = (String)msg;
        System.out.println("order: "+order+",次数"+ ++counter);
        String currtime = "query time order".equalsIgnoreCase(order) ? new Date(System.currentTimeMillis()).toString() : "Bad Order";
        currtime = currtime + System.getProperty("line.separator");
        ByteBuf res = Unpooled.copiedBuffer(currtime.getBytes());
        ctx.writeAndFlush(res);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
