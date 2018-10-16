package serial_netty.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by Orion on 2018/7/27.
 */
public class ServerHandler extends ChannelInboundHandlerAdapter{
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("recevice req");
        SocorReq sreq = (SocorReq) msg;
        if("qwe".equals(sreq.getLience())){
            System.out.println(">>>"+sreq.getStudentname());
        }
        SocorResp  sresp = new SocorResp();
        sresp.setChinese(99);
        sresp.setEnglish((int) (Math.random()*10000));
        sresp.setMath((int) Math.random());
        sresp.setPj("优秀");
        ctx.writeAndFlush(sresp);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
