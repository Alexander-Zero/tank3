package com.alex.zero.net;

import com.alex.zero.TankFrame;

import java.util.UUID;

/**
 * @author Alexander Zero
 * @version 1.0.0
 * @date 2020/11/11
 * @description
 */
public class TankDieMsg extends Msg{
    public UUID id;

    @Override
    public void handler() {
        if (id.equals(TankFrame.INSTANCE.id)) {
            return;
        }
//        TankFrame.INSTANCE.updateTankStatus(this);
    }

    @Override
    public byte[] getBytes() {
        return new byte[0];
    }

    @Override
    public void parse(byte[] bytes) {

    }

    @Override
    public MsgType getMsgType() {
        return MsgType.TankDie;
    }
}
