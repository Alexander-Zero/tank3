package com.alex.zero;

import com.alex.zero.net.Client;
import com.alex.zero.net.TankJoinMsg;
import com.alex.zero.net.TankMovingMsg;
import com.alex.zero.net.TankStopMsg;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.*;

public class TankFrame extends Frame {
    private static final Random RANDOM = new Random();
    public static final TankFrame INSTANCE = new TankFrame();
    public Client client;
    public UUID id;
    public List<Bullet> bullets = new ArrayList<>();
    public List<Explode> explodes = new ArrayList<>();
    public Map<UUID, Tank> tankMap = new HashMap<>();


    static final int GAME_WIDTH = 1080, GAME_HEIGHT = 960;

    private TankFrame() {
        setSize(GAME_WIDTH, GAME_HEIGHT);
        setResizable(false);
        setTitle("tank war");
        client = Client.INSTANCE;

        this.addKeyListener(new MyKeyListener());

        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) { // bjmashibing/tank
                System.exit(0);
            }

        });

        id = UUID.randomUUID();
    }

    Image offScreenImage = null;

    @Override
    public void update(Graphics g) {
        if (offScreenImage == null) {
            offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);
        }
        Graphics gOffScreen = offScreenImage.getGraphics();
        Color c = gOffScreen.getColor();
        gOffScreen.setColor(Color.BLACK);
        gOffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
        gOffScreen.setColor(c);
        paint(gOffScreen);
        g.drawImage(offScreenImage, 0, 0, null);
    }

    @Override
    public void paint(Graphics g) {
        Color c = g.getColor();
        g.setColor(Color.WHITE);
        g.drawString("子弹的数量:" + bullets.size(), 10, 60);
        g.drawString("敌人的数量:" + tankMap.size(), 10, 80);
        g.drawString("爆炸的数量:" + explodes.size(), 10, 100);
        g.setColor(c);

        List<Map.Entry<UUID, Tank>> tankEntry = new ArrayList<>(tankMap.entrySet());
        for (int i = 0; i < tankEntry.size(); i++) {
            tankEntry.get(i).getValue().paint(g);
        }
        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).paint(g);
        }

        for (int i = 0; i < explodes.size(); i++) {
            explodes.get(i).paint(g);
        }
        List<Tank> tanks = new ArrayList<>(tankMap.values());

        for (int i = 0; i < bullets.size(); i++) {
            for (int j = 0; j < tanks.size(); j++) {
                bullets.get(i).collideWith(tanks.get(j));
            }
        }
    }

    public void updateTankStatus(TankMovingMsg tankMovingMsg) {
        Tank tank = tankMap.get(tankMovingMsg.id);
        tank.updateStatus(tankMovingMsg);

    }

    public void updateTankStatus(TankStopMsg tankStopMsg) {
        Tank tank = tankMap.get(tankStopMsg.id);
        tank.updateStatus(tankStopMsg);
    }

//    public void updateTankStatus(TankDieMsg tankDieMsg) {
//        Tank tank = tankMap.get(tankDieMsg.id);
//        tank.die();
//    }

    class MyKeyListener extends KeyAdapter {

        boolean bL = false;
        boolean bU = false;
        boolean bR = false;
        boolean bD = false;

        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            switch (key) {
                case KeyEvent.VK_LEFT:
                    bL = true;
                    break;
                case KeyEvent.VK_UP:
                    bU = true;
                    break;
                case KeyEvent.VK_RIGHT:
                    bR = true;
                    break;
                case KeyEvent.VK_DOWN:
                    bD = true;
                    break;

                default:
                    break;
            }

            setMainTankDir();

            new Thread(() -> new Audio("audio/tank_move.wav").play()).start();
        }

        @Override
        public void keyReleased(KeyEvent e) {
            int key = e.getKeyCode();
            switch (key) {
                case KeyEvent.VK_LEFT:
                    bL = false;
                    break;
                case KeyEvent.VK_UP:
                    bU = false;
                    break;
                case KeyEvent.VK_RIGHT:
                    bR = false;
                    break;
                case KeyEvent.VK_DOWN:
                    bD = false;
                    break;

                case KeyEvent.VK_CONTROL:
                    tankMap.get(id).fire();
                    break;

                default:
                    break;
            }

            setMainTankDir();
        }

        private void setMainTankDir() {
            Tank mainTank = tankMap.get(id);
            boolean changeMoving = !mainTank.getMoving();
            if (!bL && !bU && !bR && !bD) {
                mainTank.setMoving(false);
                if (!changeMoving) {
                    Client.INSTANCE.sendMsg(new TankStopMsg(mainTank.getX(), mainTank.getY(), mainTank.getDir(), mainTank.getId()));
                }
            } else {
                Dir lastDir = mainTank.getDir();
                mainTank.setMoving(true);

                if (bL) {
                    mainTank.setDir(Dir.LEFT);
                }
                if (bU) {
                    mainTank.setDir(Dir.UP);
                }
                if (bR) {
                    mainTank.setDir(Dir.RIGHT);
                }
                if (bD) {
                    mainTank.setDir(Dir.DOWN);
                }
                boolean changeDir = mainTank.getDir() != lastDir;
                if (changeMoving || changeDir) {
                    Client.INSTANCE.sendMsg(new TankMovingMsg(mainTank.getX(), mainTank.getY(), mainTank.getDir(), mainTank.getId()));
                }
            }
        }
    }
}
