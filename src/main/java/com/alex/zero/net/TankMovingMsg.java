package com.alex.zero.net;

import com.alex.zero.Dir;
import com.alex.zero.TankFrame;

import java.io.*;
import java.util.UUID;

/**
 * @author Alexander Zero
 * @version 1.0.0
 * @date 2020/11/11
 * @description
 */
public class TankMovingMsg extends Msg {
    public int x;
    public int y;
    public Dir dir;
    public UUID id;

    public TankMovingMsg(int x, int y, Dir dir, UUID id) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.id = id;
    }

    public TankMovingMsg() {
    }

    @Override
    public void handler() {
        if (id.equals(TankFrame.INSTANCE.id)) {
            return;
        }
        TankFrame.INSTANCE.updateTankStatus(this);
    }

    @Override
    public byte[] getBytes() {
        byte[] bytes = null;
        ByteArrayOutputStream baos = null;
        DataOutputStream dos = null;
        try {
            baos = new ByteArrayOutputStream();
            dos = new DataOutputStream(baos);
            dos.writeInt(x);
            dos.writeInt(y);
            dos.writeInt(dir.ordinal());
            dos.writeLong(id.getMostSignificantBits());
            dos.writeLong(id.getLeastSignificantBits());
            dos.flush();
            bytes = baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return bytes;
    }

    @Override
    public void parse(byte[] bytes) {
        DataInputStream dis = null;
        ByteArrayInputStream bais = null;
        try {
            bais = new ByteArrayInputStream(bytes);
            dis = new DataInputStream(bais);
            this.x = dis.readInt();
            this.y = dis.readInt();
            this.dir = Dir.values()[dis.readInt()];
            long high = dis.readLong();
            long low = dis.readLong();
            this.id = new UUID(high, low);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bais != null) {
                try {
                    bais.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (dis != null) {
                try {
                    dis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public MsgType getMsgType() {
        return MsgType.TankMoving;
    }


    @Override
    public String toString() {
        return "TankMovingMsg{" +
                "x=" + x +
                ", y=" + y +
                ", dir=" + dir +
                ", id=" + id +
                '}';
    }
}
