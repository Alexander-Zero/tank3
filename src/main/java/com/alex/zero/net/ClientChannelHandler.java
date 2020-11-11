package com.alex.zero.net;

import com.alex.zero.Dir;
import com.alex.zero.Group;
import com.alex.zero.TankFrame;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Random;
import java.util.UUID;


/**
 * @author Alexander Zero
 * @version 1.0.0
 * @date 2020/11/10
 * @description
 */
public class ClientChannelHandler extends SimpleChannelInboundHandler<Msg> {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Random random = new Random();
        TankJoinMsg tank = new TankJoinMsg(random.nextInt(600), random.nextInt(600), Dir.values()[random.nextInt(2)], random.nextBoolean(), Group.values()[random.nextInt(2)], TankFrame.INSTANCE.id);
        ctx.writeAndFlush(tank);
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Msg msg) throws Exception {
        msg.handler();
    }
}
