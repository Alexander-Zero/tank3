package com.alex.zero;

import com.alex.zero.net.*;

import java.awt.*;
import java.rmi.server.UID;
import java.util.Random;
import java.util.UUID;

public class Tank {
    private static final int SPEED = 2;
    public static int WIDTH = ResourceMgr.goodTankU.getWidth();

    public static int HEIGHT = ResourceMgr.goodTankU.getHeight();

    Rectangle rect = new Rectangle();

    private Random random = new Random();

    private int x, y;

    private Dir dir = Dir.DOWN;
    private UUID id;
    public boolean moving = true;
    private TankFrame tf = null;
    private boolean living = true;
    private Group group;

    public Tank(TankJoinMsg tankJoinMsg) {
        this.x = tankJoinMsg.x;
        this.y = tankJoinMsg.y;
        this.moving = tankJoinMsg.moving;
        this.group = tankJoinMsg.group;
        this.dir = tankJoinMsg.dir;
        this.id = tankJoinMsg.id;

        updateRect();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Tank(int x, int y, Dir dir, Group group, UUID id) {
        super();
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.group = group;
        this.id = id;

        updateRect();

    }

    private void updateRect() {
        rect.x = this.x;
        rect.y = this.y;
        rect.width = WIDTH;
        rect.height = HEIGHT;
    }

    public void fire() {
        int bX = this.x + Tank.WIDTH / 2 - Bullet.WIDTH / 2;
        int bY = this.y + Tank.HEIGHT / 2 - Bullet.HEIGHT / 2;

        Client.INSTANCE.sendMsg(new BulletFireMsg(bX, bY, this.dir, this.group));

        if (this.group == Group.GOOD) {
            new Thread(() -> new Audio("audio/tank_fire.wav").play()).start();
        }
    }

    public Dir getDir() {
        return dir;
    }

    public int getX() {
        return x;
    }


    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public int getY() {
        return y;
    }

    public boolean isMoving() {
        return moving;
    }

    private void move() {

        if (!moving) return;

        switch (dir) {
            case LEFT:
                x -= SPEED;
                break;
            case UP:
                y -= SPEED;
                break;
            case RIGHT:
                x += SPEED;
                break;
            case DOWN:
                y += SPEED;
                break;
        }

//		if(this.group == Group.BAD && random.nextInt(100) > 95)
//			this.fire();
//
//		if(this.group == Group.BAD && random.nextInt(100) > 95)
//			randomDir();

        boundsCheck();
        //update rect
        updateRect();

    }

    private void boundsCheck() {
        if (this.x < 2) x = 2;
        if (this.y < 28) y = 28;
        if (this.x > TankFrame.GAME_WIDTH - Tank.WIDTH - 2) x = TankFrame.GAME_WIDTH - Tank.WIDTH - 2;
        if (this.y > TankFrame.GAME_HEIGHT - Tank.HEIGHT - 2) y = TankFrame.GAME_HEIGHT - Tank.HEIGHT - 2;
    }

    private void randomDir() {

        this.dir = Dir.values()[random.nextInt(4)];
    }

    public void paint(Graphics g) {
        if (!living) TankFrame.INSTANCE.tankMap.remove(this.getId());

        Color gColor = g.getColor();
        g.setColor(Color.YELLOW);
        g.drawString(id.toString().substring(0, 10), x, y - 10);
        g.setColor(gColor);

        switch (dir) {
            case LEFT:
                g.drawImage(this.group == Group.GOOD ? ResourceMgr.goodTankL : ResourceMgr.badTankL, x, y, null);
                break;
            case UP:
                g.drawImage(this.group == Group.GOOD ? ResourceMgr.goodTankU : ResourceMgr.badTankU, x, y, null);
                break;
            case RIGHT:
                g.drawImage(this.group == Group.GOOD ? ResourceMgr.goodTankR : ResourceMgr.badTankR, x, y, null);
                break;
            case DOWN:
                g.drawImage(this.group == Group.GOOD ? ResourceMgr.goodTankD : ResourceMgr.badTankD, x, y, null);
                break;
        }

        move();

    }


    public void setDir(Dir dir) {
        this.dir = dir;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public boolean getMoving() {
        return this.moving;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void die() {
        this.living = false;
    }


    public void updateStatus(TankMovingMsg tankMovingMsg) {
        setX(tankMovingMsg.x);
        setY(tankMovingMsg.y);
        setDir(tankMovingMsg.dir);
        setMoving(true);
        updateRect();
    }

    public void updateStatus(TankStopMsg tankStopMsg) {
        setX(tankStopMsg.x);
        setY(tankStopMsg.y);
        setDir(tankStopMsg.dir);
        setMoving(false);
        updateRect();
    }
}
