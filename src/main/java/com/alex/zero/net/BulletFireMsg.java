package com.alex.zero.net;

import com.alex.zero.Bullet;
import com.alex.zero.Dir;
import com.alex.zero.Group;
import com.alex.zero.TankFrame;

import java.io.*;
import java.util.Objects;

/**
 * @author Alexander Zero
 * @version 1.0.0
 * @date 2020/11/11
 * @description
 */
public class BulletFireMsg extends Msg {
    public int x;
    public int y;
    public Dir dir;
    public Group group;

    public BulletFireMsg() {
    }

    public BulletFireMsg(int x, int y, Dir dir, Group group) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.group = group;
    }



    @Override
    public void handler() {
        TankFrame.INSTANCE.bullets.add(new Bullet(this));
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
            dos.writeInt(group.ordinal());
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
            this.group = Group.values()[dis.readInt()];

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
        return MsgType.BulletFire;
    }

    @Override
    public String toString() {
        return "BulletFireMsg{" +
                "x=" + x +
                ", y=" + y +
                ", dir=" + dir +
                ", group=" + group +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BulletFireMsg that = (BulletFireMsg) o;
        return x == that.x &&
                y == that.y &&
                dir == that.dir &&
                group == that.group;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, dir, group);
    }
}
