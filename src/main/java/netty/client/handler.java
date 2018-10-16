package netty.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by Orion on 2018/7/21.
 */
public class handler extends ChannelInboundHandlerAdapter {
    private int counter;
    private byte[] req;

    public handler(){
        req = ("query time order" + System.getProperty("line.separator")).getBytes();
    }

    public void channelActive(ChannelHandlerContext ctx){
        ByteBuf bf = null;
        for(int i=0;i<100;i++){
            bf = Unpooled.buffer(req.length);
            bf.writeBytes(req);
            ctx.writeAndFlush(bf);
        }
    }

    public void channelRead(ChannelHandlerContext ctx,Object msg){
        String body = (String)msg;
        System.out.println("Now is :"+body+",次数"+ ++counter);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
