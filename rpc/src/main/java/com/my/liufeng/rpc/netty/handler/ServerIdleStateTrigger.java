package com.my.liufeng.rpc.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

public class ServerIdleStateTrigger extends ChannelInboundHandlerAdapter {
    private static InternalLogger logger = InternalLoggerFactory.getInstance(ServerIdleStateTrigger.class);


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.READER_IDLE) {
                // 指定时间没有收到数据包，断开连接
                ctx.disconnect();
                logger.info("server 收到reader idle事件，断开连接.client address:{}", ctx.channel().remoteAddress());
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
