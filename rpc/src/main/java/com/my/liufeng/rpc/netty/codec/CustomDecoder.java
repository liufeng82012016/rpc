package com.my.liufeng.rpc.netty.codec;

import com.my.liufeng.rpc.constants.RpcConstants;
import com.my.liufeng.rpc.enums.RpcMessageType;
import com.my.liufeng.rpc.model.RpcHeartMsg;
import com.my.liufeng.rpc.model.RpcRequest;
import com.my.liufeng.rpc.model.RpcResponse;
import com.my.liufeng.rpc.utils.SerialUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.util.List;

/**
 * 解码器，负责反序列化服务器返回值
 */
public class CustomDecoder extends ByteToMessageDecoder {
    private static InternalLogger logger = InternalLoggerFactory.getInstance(CustomDecoder.class);


    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < RpcConstants.MSG_EXTRA_LENGTH) {
            return;
        }
        // 标记从哪里开始读取流
        in.markReaderIndex();
        // 校验头部分隔符
        if (!checkSeparator(in)) {
            discardReadBytes(in);
            return;
        }
        // 如果消息长度低于分割符等额外长度，
        int length = in.readInt();
        if (length < RpcConstants.MSG_EXTRA_LENGTH) {
            discardReadBytes(in);
            return;
        }
        // 如果可读消息长度低于消息长度，重置可读进度
        int bodyLength = length - RpcConstants.MSG_EXTRA_LENGTH;
        if (in.readableBytes() < bodyLength) {
            in.resetReaderIndex();
            return;
        }
        byte type = in.readByte();
        // 读取消息体
        byte[] data = new byte[bodyLength];
        in.readBytes(data);
        // 检查尾部分隔符
        if (!checkSeparator(in)) {
            discardReadBytes(in);
            return;
        }
        Class clazz = type == RpcMessageType.TYPE_RESPONSE.getType() ? RpcResponse.class : (
                RpcMessageType.TYPE_REQUEST.getType() == type ? RpcRequest.class : RpcHeartMsg.class
        );
        // todo 心跳消息处理 如果是ping，回复pong；如果是pong；不处理
        try {
            Object obj = SerialUtil.deserialize(data, clazz);
            out.add(obj);
        } catch (Exception ex) {
            System.err.println("Decode error: " + ex.toString());
        }
    }

    private void discardReadBytes(ByteBuf in) {
        while (in.readableBytes() > 0) {
            byte b = in.readByte();
            if (b != RpcConstants.SEPARATOR[0]) {
                logger.warn("discard: {}", +b);
                //  还没有匹配到分隔符的第一位，丢弃掉
                continue;
            }
            in.markReaderIndex();
            for (int i = 1; i < RpcConstants.SEPARATOR.length; i++) {
                if (in.readByte() == RpcConstants.SEPARATOR[i]) {
                    //  匹配
                } else {
                    // 不匹配 reset
                    in.resetReaderIndex();
                }
            }
        }
    }

    /**
     * 校验消息是否包含指定的分割符
     *
     * @param in ByteBuf容器
     * @return true：校验通过
     */
    private boolean checkSeparator(ByteBuf in) {
        // 比较byte[]数组的值是否相等
        int len = RpcConstants.SEPARATOR.length;
        byte[] tmp = new byte[len];
        in.readBytes(tmp);
        for (int i = 0; i < len; i++) {
            if (tmp[i] != RpcConstants.SEPARATOR[i]) {
                return false;
            }
        }
        return true;
    }

}
