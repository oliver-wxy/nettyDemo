package com.example.demo.handle;

import com.example.demo.context.HandlerContext;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.UUID;

/**
 * @author wxy
 * @create 2020-04-23 17:12
 **/
public class MyWebSocketHandler extends SimpleChannelInboundHandler<Object> {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("服务端channel通道激活-"+ctx.channel());
        HandlerContext.getInstance().addChannel(ctx, ctx.channel().remoteAddress()+"^"+ UUID.randomUUID());
        System.out.println("当前链路数量-"+HandlerContext.getInstance().getSize());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("服务端channel通道注销-"+ctx.channel());
        HandlerContext.getInstance().removeChannel(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        TextWebSocketFrame text = (TextWebSocketFrame) msg;
        System.out.println("接收到客户端-{"+ctx.channel()+"}的消息为-{"+text.text()+"}");
        ctx.channel().writeAndFlush(new TextWebSocketFrame("我收到了你的消息"));
        // 当前链接存入全局对象
        // HandlerContext.getInstance().addChannel(ctx, ctx.channel().remoteAddress().toString());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        HandlerContext.getInstance().removeChannel(ctx);
        cause.printStackTrace();
        ctx.close();
    }
}
