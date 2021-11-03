package com.my.liufeng.rpc.netty.codec;

import com.my.liufeng.rpc.constants.RpcConstants;
import com.my.liufeng.rpc.model.RpcHeartMsg;
import com.my.liufeng.rpc.enums.RpcMessageType;
import com.my.liufeng.rpc.model.RpcRequest;
import com.my.liufeng.rpc.model.RpcResponse;
import com.my.liufeng.rpc.utils.SerialUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.Arrays;
import java.util.List;

/**
 * 解码器，负责反序列化服务器返回值
 */
public class CustomDecoder extends ByteToMessageDecoder {


    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < RpcConstants.MSG_BODY_LENGTH) {
            return;
        }
        // 标记从哪里开始读取流
        in.markReaderIndex();
        int dataLength = in.readInt() - RpcConstants.MSG_BODY_LENGTH;
        if (in.readableBytes() < dataLength) {
            // 如果消息不够完全，将读取位置恢复；不然这次读了，下次读不到length了
            in.resetReaderIndex();
            return;
        }
        checkMagicNumber(in);
        byte type = in.readByte();
        Class clazz = type == RpcMessageType.TYPE_RESPONSE.getType() ? RpcResponse.class : (
                RpcMessageType.TYPE_REQUEST.getType() == type ? RpcRequest.class : RpcHeartMsg.class
        );
        byte[] data = new byte[dataLength - RpcConstants.MSG_BODY_TYPE - RpcConstants.MAGIC_NUMBER.length];
        in.readBytes(data);
        try {
            Object obj = SerialUtil.deserialize(data, clazz);
            out.add(obj);
        } catch (Exception ex) {
            System.err.println("Decode error: " + ex.toString());
        }
    }


    private void checkMagicNumber(ByteBuf in) {
        // 比较byte[]数组的值是否相等
        // todo 如果失败了呢
        int len = RpcConstants.MAGIC_NUMBER.length;
        byte[] tmp = new byte[len];
        in.readBytes(tmp);
        for (int i = 0; i < len; i++) {
            if (tmp[i] != RpcConstants.MAGIC_NUMBER[i]) {
                throw new IllegalArgumentException("Unknown magic code: " + Arrays.toString(tmp) + "  expect: " + Arrays.toString(RpcConstants.MAGIC_NUMBER));
            }
        }
    }

}
