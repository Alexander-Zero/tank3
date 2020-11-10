package com.alex.zero.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author Alexander Zero
 * @version 1.0.0
 * @date 2020/11/10
 * @description
 */
public class TankMsgEncoder extends MessageToByteEncoder<TankMsg> {
    @Override
    protected void encode(ChannelHandlerContext ctx, TankMsg tankMsg, ByteBuf byteBuf) throws Exception {
        byteBuf.writeInt(tankMsg.x);
        byteBuf.writeInt(tankMsg.y);
        byteBuf.writeBoolean(tankMsg.moving);
        byteBuf.writeInt(tankMsg.dir.ordinal());
        byteBuf.writeInt(tankMsg.group.ordinal());
        byteBuf.writeLong(tankMsg.id.getMostSignificantBits());
        byteBuf.writeLong(tankMsg.id.getLeastSignificantBits());
        //备注: 这里不能在写ctx.write方法了, 会自动给传入
    }
}
