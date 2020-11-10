package com.alex.zero.net;

import com.alex.zero.Dir;
import com.alex.zero.Group;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;
import java.util.UUID;

/**
 * @author Alexander Zero
 * @version 1.0.0
 * @date 2020/11/10
 * @description
 */
public class TankMsgDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (byteBuf.readableBytes() < 33) {
            return;
        }
        int x = byteBuf.readInt();
        int y = byteBuf.readInt();
        boolean moving = byteBuf.readBoolean();
        Dir dir = Dir.values()[byteBuf.readInt()];
        Group group = Group.values()[byteBuf.readInt()];
        long high = byteBuf.readLong();
        long low = byteBuf.readLong();
        UUID id = new UUID(high, low);

        list.add(new TankMsg(x, y, dir, moving, group, id));
    }
}
