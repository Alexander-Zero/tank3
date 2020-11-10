package com.alex.zero.net;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author Alexander Zero
 * @version 1.0.0
 * @date 2020/11/10
 * @description
 */
public class Client {

    public static final Client INSTANCE = new Client();
    private Channel client;

    private Client() {
    }

    public static void main(String[] args) {
        INSTANCE.connect();
    }

    public void connect() {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        try {
            ChannelFuture future = bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            client = socketChannel;
                            socketChannel.pipeline()
                                    .addLast(new TankMsgDecoder())
                                    .addLast(new TankMsgEncoder())
                                    .addLast(new ClientChannelHandler());
                        }
                    })
                    .connect("localhost", 8888)
                    .addListener(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture channelFuture) throws Exception {

                        }
                    })
                    .sync();

            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

    public void sendMsg(TankMsg tankMsg) {
        client.writeAndFlush(tankMsg);
    }
}
