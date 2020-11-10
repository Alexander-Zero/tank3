package com.alex.zero;

import com.alex.zero.net.Client;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;
import java.util.List;

public class TankFrame extends Frame {
    private static final Random RANDOM = new Random();
    public static final TankFrame INSTANCE = new TankFrame();
    public Client client;
    public Tank myTank = new Tank(RANDOM.nextInt(500), RANDOM.nextInt(500), Dir.values()[RANDOM.nextInt(2)], Group.values()[RANDOM.nextInt(2)], this);
    List<Bullet> bullets = new ArrayList<>();
    //    List<Tank> tanks = new ArrayList<>();
    List<Explode> explodes = new ArrayList<>();
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

        myTank.paint(g);

        tankMap.forEach((id, tank) -> {
            tank.paint(g);
        });
        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).paint(g);
        }

        for (int i = 0; i < explodes.size(); i++) {
            explodes.get(i).paint(g);
        }
        //collision detect
        List<Tank> tanks = new ArrayList<>(tankMap.values());
        for (int i = 0; i < bullets.size(); i++) {
            for (int j = 0; j < tanks.size(); j++)
                bullets.get(i).collideWith(tanks.get(j));
        }
    }

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
                    myTank.fire();
                    break;

                default:
                    break;
            }

            setMainTankDir();
        }

        private void setMainTankDir() {

            if (!bL && !bU && !bR && !bD)
                myTank.setMoving(false);
            else {
                myTank.setMoving(true);

                if (bL)
                    myTank.setDir(Dir.LEFT);
                if (bU)
                    myTank.setDir(Dir.UP);
                if (bR)
                    myTank.setDir(Dir.RIGHT);
                if (bD)
                    myTank.setDir(Dir.DOWN);
            }
        }
    }
}
