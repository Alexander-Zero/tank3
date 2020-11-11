package com.alex.zero.net;

import com.alex.zero.Explode;
import com.alex.zero.TankFrame;

import java.io.*;

/**
 * @author Alexander Zero
 * @version 1.0.0
 * @date 2020/11/11
 * @description
 */
public class ExplodeMsg  extends Msg{
    public int x;
    public int y;

    public ExplodeMsg(){}
    public ExplodeMsg(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void handler() {
//        TankFrame.INSTANCE.explodes.add(new Explode(this));
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
        return MsgType.Exploded;
    }
}
