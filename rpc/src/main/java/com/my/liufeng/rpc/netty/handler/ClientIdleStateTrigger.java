package com.my.liufeng.rpc.netty.handler;

import com.my.liufeng.rpc.model.RpcHeartMsg;
import com.my.liufeng.rpc.utils.SerialUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

/**
 * 用于补货IdleState#WRITE_IDLE事件，然后向服务端发送一个心跳包
 */
public class ClientIdleStateTrigger extends ChannelInboundHandlerAdapter {
    private static InternalLogger logger = InternalLoggerFactory.getInstance(ClientIdleStateTrigger.class);

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.WRITER_IDLE) {
                ctx.writeAndFlush(SerialUtil.serialize(RpcHeartMsg.PING));
                logger.info("client 监听write idle事件，发送心跳包",ctx.channel());
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
