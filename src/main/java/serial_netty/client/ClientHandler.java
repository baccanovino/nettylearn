package serial_netty.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import serial_netty.server.SocorReq;

/**
 * Created by Orion on 2018/7/29.
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i<10; i++){
            ctx.write(makeReq(i));
            System.out.println("send : [ "+i+" ]");
        }
        ctx.flush();
    }

    private Object makeReq(int i) {
        SocorReq sr= new SocorReq();
        sr.setLience("qwe");
        sr.setStudentname("zdz"+i);
        return sr;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("receive : [ "+msg+" ]");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
