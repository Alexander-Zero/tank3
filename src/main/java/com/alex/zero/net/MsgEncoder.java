package com.alex.zero.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author Alexander Zero
 * @version 1.0.0
 * @date 2020/11/10
 * @description
 * 编码: 类型 + 长度 + 实际数据
 */
public class MsgEncoder extends MessageToByteEncoder<Msg> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Msg msg, ByteBuf byteBuf) throws Exception {
        byteBuf.writeInt(msg.getMsgType().ordinal());
        byte[] bytes = msg.getBytes();
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);
        //备注: 这里不能在写ctx.write方法了, 会自动给传入
    }
}
