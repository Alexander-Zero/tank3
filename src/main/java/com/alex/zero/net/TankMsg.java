package com.alex.zero.net;

import com.alex.zero.Dir;
import com.alex.zero.Group;
import com.alex.zero.Tank;

import java.util.Objects;
import java.util.UUID;

/**
 * @author Alexander Zero
 * @version 1.0.0
 * @date 2020/11/10
 * @description
 */

public class TankMsg {
    public int x, y;
    public Dir dir;
    public boolean moving;
    public Group group;
    public UUID id;

    public TankMsg(int x, int y, Dir dir, boolean moving, Group group, UUID id) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.moving = moving;
        this.group = group;
        this.id = id;
    }

    public TankMsg(Tank myTank) {
        this.x = myTank.getX();
        this.y = myTank.getY();
        this.dir = myTank.getDir();
        this.moving = myTank.moving;
        this.group = myTank.getGroup();
        this.id = myTank.getId();
    }

//    public byte[] toBytes() {
//        ByteArrayOutputStream baos = null;
//        DataOutputStream dos = null;
//        byte[] bytes = null;
//        try {
//            baos = new ByteArrayOutputStream();
//            dos = new DataOutputStream(baos);
//
//            //dos.writeInt(TYPE.ordinal());
//            dos.writeInt(x);
//            dos.writeInt(y);
//            dos.writeInt(dir.ordinal());
//            dos.writeBoolean(moving);
//            dos.writeInt(group.ordinal());
//            dos.writeLong(id.getMostSignificantBits());
//            dos.writeLong(id.getLeastSignificantBits());
//            //dos.writeUTF(name);
//            dos.flush();
//            bytes = baos.toByteArray();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if(baos != null) {
//                    baos.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            try {
//                if(dos != null) {
//                    dos.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        return bytes;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TankMsg tankMsg = (TankMsg) o;
        return x == tankMsg.x &&
                y == tankMsg.y &&
                moving == tankMsg.moving &&
                dir == tankMsg.dir &&
                group == tankMsg.group &&
                Objects.equals(id, tankMsg.id);
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
