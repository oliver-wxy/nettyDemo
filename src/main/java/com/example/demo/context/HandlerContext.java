package com.example.demo.context;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.ReferenceCountUtil;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wxy
 * @create 2020-04-26 11:28
 **/
public class HandlerContext {
    public final static ConcurrentHashMap<ChannelHandlerContext, String> maps = new ConcurrentHashMap<ChannelHandlerContext, String>(
            0);

    private static class SingletonHolder {
        private final static HandlerContext INSTANCE = new HandlerContext();
    }

    private HandlerContext() {
    }

    public static HandlerContext getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void addChannel(ChannelHandlerContext socket, String ID) {
        maps.put(socket, ID);
    }

    public void removeChannel(ChannelHandlerContext ctx) {
        maps.remove(ctx);
    }

    public int getSize() {
        return maps.size();
    }

    public void sendMsg(String msg) {
        if(!maps.isEmpty()){
            Iterator<Entry<ChannelHandlerContext,String>> it = maps.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<ChannelHandlerContext,String> entry = (Map.Entry<ChannelHandlerContext,String>) it.next();
                ChannelHandlerContext ctx = (ChannelHandlerContext)entry.getKey();
                String s = (String)entry.getValue();
                if(s.length() == 24){//html5websocket存储的Sec-WebSocket-Key
                    ctx.writeAndFlush(new TextWebSocketFrame(msg));
                }else{
                    ctx.writeAndFlush(msg + "\n");
                }
            }
            ReferenceCountUtil.release(msg);
        }
    }
}
