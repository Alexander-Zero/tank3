package com.alex.zero.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author Alexander Zero
 * @version 1.0.0
 * @date 2020/11/10
 * @description
 */
public class MsgDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (byteBuf.readableBytes() < 8) {
            return;
        }
        byteBuf.markReaderIndex();

        MsgType msgType = MsgType.values()[byteBuf.readInt()];
        int length = byteBuf.readInt();
        if (byteBuf.readableBytes() < length) {
            byteBuf.resetReaderIndex();
            return;
        }

        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);
        String packageName = this.getClass().getPackageName();
        Msg msg = (Msg) Class.forName(packageName + "." + msgType.name() + "Msg").getConstructor().newInstance();
        msg.parse(bytes);
        list.add(msg);

    }
}
