package com.alex.zero.net;

import com.alex.zero.Dir;
import com.alex.zero.Group;
import com.alex.zero.Tank;
import com.alex.zero.TankFrame;

import java.io.*;
import java.util.Objects;
import java.util.UUID;

/**
 * @author Alexander Zero
 * @version 1.0.0
 * @date 2020/11/10
 * @description
 */

public class TankJoinMsg extends Msg {
    public int x, y;
    public Dir dir;
    public boolean moving;
    public Group group;
    public UUID id;

    public TankJoinMsg() {
    }

    public TankJoinMsg(int x, int y, Dir dir, boolean moving, Group group, UUID id) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.moving = moving;
        this.group = group;
        this.id = id;
    }

    public TankJoinMsg(Tank myTank) {
        this.x = myTank.getX();
        this.y = myTank.getY();
        this.dir = myTank.getDir();
        this.moving = myTank.moving;
        this.group = myTank.getGroup();
        this.id = myTank.getId();
    }

    @Override
    public void handler() {
        if ( TankFrame.INSTANCE.tankMap.containsKey(id)) {
            return;
        } else {
            TankFrame.INSTANCE.tankMap.put(id, new Tank(this));
            Client.INSTANCE.sendMsg(new TankJoinMsg(TankFrame.INSTANCE.tankMap.get(TankFrame.INSTANCE.id)));
        }
    }

    @Override
    public byte[] getBytes() {
        ByteArrayOutputStream baos = null;
        DataOutputStream dos = null;
        byte[] bytes = null;
        try {
            baos = new ByteArrayOutputStream();
            dos = new DataOutputStream(baos);

            dos.writeInt(x);
            dos.writeInt(y);
            dos.writeBoolean(moving);
            dos.writeInt(dir.ordinal());
            dos.writeInt(group.ordinal());
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
                if (dos != null) {
                    try {
                        dos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return bytes;
    }

    @Override
    public void parse(byte[] bytes) {
        ByteArrayInputStream bais = null;
        DataInputStream dis = null;
        try {
            bais = new ByteArrayInputStream(bytes);
            dis = new DataInputStream(bais);
            this.x = dis.readInt();
            this.y = dis.readInt();
            this.moving = dis.readBoolean();
            this.dir = Dir.values()[dis.readInt()];
            this.group = Group.values()[dis.readInt()];
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
        return MsgType.TankJoin;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TankJoinMsg tankJoinMsg = (TankJoinMsg) o;
        return x == tankJoinMsg.x &&
                y == tankJoinMsg.y &&
                moving == tankJoinMsg.moving &&
                dir == tankJoinMsg.dir &&
                group == tankJoinMsg.group &&
                Objects.equals(id, tankJoinMsg.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, dir, moving, group, id);
    }

    @Override
    public String toString() {
        return "TankMsg{" +
                "x=" + x +
                ", y=" + y +
                ", dir=" + dir +
                ", moving=" + moving +
                ", group=" + group +
                ", uuid=" + id +
                '}';
    }
}
