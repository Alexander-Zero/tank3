package com.alex.zero.net;

import com.alex.zero.Dir;
import com.alex.zero.Group;
import com.alex.zero.Tank;
import com.alex.zero.TankFrame;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;


/**
 * @author Alexander Zero
 * @version 1.0.0
 * @date 2020/11/10
 * @description
 */
public class ClientChannelHandler extends SimpleChannelInboundHandler<TankMsg> {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        TankMsg tank = new TankMsg(TankFrame.INSTANCE.myTank);
        ctx.writeAndFlush(tank);
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, TankMsg msg) throws Exception {
        TankMsg tankMsg = (TankMsg) msg;
        if (tankMsg.id.equals(TankFrame.INSTANCE.myTank.getId()) || TankFrame.INSTANCE.tankMap.containsKey(tankMsg.id)) {
            return;
        } else {
            TankFrame.INSTANCE.tankMap.put(tankMsg.id, new Tank(tankMsg));
            ctx.writeAndFlush(new TankMsg(TankFrame.INSTANCE.myTank));
        }
    }
}
