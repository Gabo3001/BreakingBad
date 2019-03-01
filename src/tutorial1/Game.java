/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tutorial1;
//GABO
//Natalia

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.LinkedList;

/**
 *
 * @author HOME
 */
public class Game implements Runnable {

    private BufferStrategy bs;
    private Graphics g;
    private Display display;
    String title;
    private int width;
    private int height;
    private Thread thread;
    private boolean running;
    private Player player;
    private Ball ball;              //Variable de tipo Ball
    private Brick brick;
    private KeyManager keyManager;
    private LinkedList<Brick> smallBricks;     //linked list for the small bricks
    private boolean start;
    
    public Game(String title, int width, int height){
        this.title = title;
        this.width = width;
        this.height = height;
        running = false;
        keyManager = new KeyManager();
        smallBricks = new LinkedList<Brick>();
        start = false;                  //Se inicializa start en false
    }

    public boolean isStart() {
        return start;
    }

    public void setStart(boolean start) {
        this.start = start;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    private void init() {
        display = new Display(title, getWidth(), getHeight());
        Assets.init();
        player = new Player(330, getHeight() - 100, 1, 160, 80, this);
        ball = new Ball(385, getHeight() - 145, 1, 50, 50, this);
        //initialize small bricks
        for(int i = 0; i < 13; i++){
            smallBricks.add(new Brick(1*(i*60)+ 10, 50, 50, 50, this, 1, 1));
        }
        
        display.getJframe().addKeyListener(keyManager);
    }

    @Override
    public void run() {
        init();
        //frames per seconds
        int fps = 60;
        //time for each tick in nano seconds
        double timeTick = 1000000000 / fps;
        // inizialing delta
        double delta = 0;
        // define now to use inside the loop
        long now;
        // initializing last time to the computer time in nanosecs
        long lastTime = System.nanoTime();
        while (running) {
            // setting the time now to the actual time
            now = System.nanoTime();
            // acumulating to delta the difference between times in timeTick units
            delta += (now - lastTime) / timeTick;
            //updating the last time
            lastTime = now;

            // if delta is positive we tick the game
            if (delta >= 1) {
                tick();
                render();
                delta--;
            }
        }
        stop();
    }

    public KeyManager getKeyManager() {
        return keyManager;
    }

    private void tick() {
        keyManager.tick();

        player.tick();
        ball.tick();
        //Si se preciona space
        if (getKeyManager().space) {
            //Se colocara start en true
            setStart(true);
        }
        //Si la pelota intersecta con el player en la mitad derecha
        if (player.intersecta(ball)) {
            ball.setDirection(2);
        }
        //Si la pelota intersecta con el player en la mitad derecha
        else if (player.intersecta2(ball)) {
            ball.setDirection(1);
        }
        //Si la pelota sale de la pantalla por abajo
        if(ball.getY() >= getHeight()){
            //Se pone start en falso
            setStart(false);
            //Se coloca ball en la posicion inicial
            ball.setX(385);
            ball.setY(getHeight()-145);
            //Se coloca al player en la posicion inicial
            player.setX(330);
            player.setY(getHeight()-100);
        }
        
        for (int i = 0; i < smallBricks.size(); i++) {
            Brick brick =  smallBricks.get(i);
            
            if(ball.intersecta(brick)){
                brick.setLives(brick.getLives() - 1);

                //Make the ball bounce away from brick
                if(ball.getDirection() == 1)
                    ball.setDirection(3);
                
                else if(ball.getDirection() == 2)
                    ball.setDirection(4);
                
                else if(ball.getDirection() == 3)
                    ball.setDirection(1);
                
                else if(ball.getDirection() == 4)
                    ball.setDirection(2);

            }
            
            brick.tick();
            
        }
    }

    private void render() {
        bs = display.getCanvas().getBufferStrategy();

        if (bs == null) {
            display.getCanvas().createBufferStrategy(3);
        } else {
            g = bs.getDrawGraphics();
            g.drawImage(Assets.background, 0, 0, width, height, null);
            player.render(g);
            ball.render(g);
            
            for (int i = 0; i < smallBricks.size(); i++) {
                Brick brick =  smallBricks.get(i);
                brick.render(g);
            }
            
            bs.show();
            g.dispose();
        }
    }

    public synchronized void start() {
        if (!running) {
            running = true;
            thread = new Thread(this);
            thread.start();
        }
    }

    public synchronized void stop() {
        if (running) {
            running = false;
            try {
                thread.join();
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
    }
}
