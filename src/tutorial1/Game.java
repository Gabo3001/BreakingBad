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
    private Brick flask;
    private KeyManager keyManager;
    private LinkedList<Brick> smallBricks;     //linked list for the small bricks
    private LinkedList<Brick> bigBricks;
    private boolean start;          //Boolean that control the start
    private boolean pause;          //Boolean that control the pause
    private int lives; //player's lives
    public boolean power; //to activate special power
    private int countForPower; //count down to activate special power
    private boolean finish; //Boolean that control when the game stop
    private int contBricks; //counter for the bricks
    
    public Game(String title, int width, int height){
        this.title = title;
        this.width = width;
        this.height = height;
        running = false;
        keyManager = new KeyManager();
        smallBricks = new LinkedList<Brick>();
        bigBricks = new LinkedList<Brick>();
        start = false;                  //Se inicializa start en false
        pause = true;                   //Pause is initialize in true
        lives = 3;
        power = false;
        finish = false;                 //Finish is initialized in false
        countForPower = 7; //when count down reaches 0, make power available
        contBricks = 0;   
        
    }

    public boolean isPower() {
        return power;
    }

    public void setPower(boolean power) {
        this.power = power;
    }
    
    public void setCountForPower(int countForPower) {
        this.countForPower = countForPower;
    }

    public int getCountForPower() {
        return countForPower;
    }

    public void setContBricks(int contBricks) {
        this.contBricks = contBricks;
    }

    public int getContBricks() {
        return contBricks;
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }

    public boolean isFinish() {
        return finish;
    }

    public boolean isPause() {
        return pause;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
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

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }
    
    

    private void init() {
        display = new Display(title, getWidth(), getHeight());
        Assets.init();
        player = new Player(330, getHeight() - 100, 1, 160, 80, this);
        ball = new Ball(385, getHeight() - 145, 1, 50, 50, this);
        flask = new Brick(getWidth() + 50, 200, 50, 50, this, 3, 1 ); //flask for power up
        //initialize small bricks
        for(int i = 0; i < 13; i++){
            smallBricks.add(new Brick(1*(i*60)+ 10, 50, 50, 50, this, 1, 1));
        }
        for(int i = 0; i < 6; i++){
            bigBricks.add(new Brick(1*(i*120)+ 20, 130, 100, 50, this, 2, 3));
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
            //Reset speed when ball falls
            ball.setSpeed(2);
        }
        //if finish is true and the r key is press
        if(isFinish() && getKeyManager().reset){
            //Se pone start en falso
            setStart(false);
            //Se coloca ball en la posicion inicial
            ball.setX(385);
            ball.setY(getHeight()-145);
            //Se coloca al player en la posicion inicial
            player.setX(330);
            player.setY(getHeight()-100);
            //set lives to 3
            setLives(3);
            //set countForPower to 7
            setCountForPower(7);
            //set contBrick to 0
            setContBricks(0);
            //Set power to false
            setPower(false);
            //Set flask to its initial position
            flask.setX(getWidth() + 50);
            for (int i = 0; i < smallBricks.size(); i++) {
                Brick brick =  smallBricks.get(i);
                brick.setLives(1);
                brick.setX(1*(i*60)+ 10);
                brick.tick();
            }
            for (int i = 0; i < bigBricks.size(); i++) {
                Brick brick =  bigBricks.get(i);
                brick.setLives(3);
                brick.setX(1*(i*120)+ 20);
                brick.tick();
            }
            //Set finish to false
            setFinish(false);
            getKeyManager().pStop();
        }
        if (getKeyManager().pause){
            if (isPause()){
                setPause(false);
                getKeyManager().pStop();
            }
            else{
                setPause(true);
                getKeyManager().pStop();
            }
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
            //lose a live
            setLives(getLives() - 1);
            
        }
        
        for (int i = 0; i < smallBricks.size(); i++) {
            Brick brick =  smallBricks.get(i);
            
            if(ball.intersecta(brick)){
                brick.setLives(brick.getLives() - 1);
                //decrease count down for power when brick is destroyed
                setCountForPower(getCountForPower()-1);
                setContBricks(getContBricks()+1);

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
        
        for (int i = 0; i < bigBricks.size(); i++) {
            Brick brick =  bigBricks.get(i);
            
            if(ball.intersecta(brick)){
                brick.setLives(brick.getLives() - 1);
                //decrease count down for power when brick is destroyed
                if(brick.getLives() == 0){
                    setCountForPower(getCountForPower()-1);
                    setContBricks(getContBricks()+1);
                }

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
        //show flask when count down is over
        if(getCountForPower() <= 0){
            flask.setX(getWidth()/2 - 50);
            setPower(true);
            setCountForPower(100);
        }
        
        if(ball.intersecta(flask)){
            ball.setSpeed(ball.getSpeed() + 2);
            flask.setX(width + 20);
        }
        //If you lose all your life or destroy all the bricks
        if(getLives() == 0 || getContBricks() == 19){
            //Set finish to true
            setFinish(true);
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
            //if power is true
            if(isPower()){
                //The render for the flask is done
                flask.render(g);
            }
            //if pause is true
            if(!isPause()){
                //A pause image is draw
                g.drawImage(Assets.pause, 320, 220, 150, 50, null);
            }
            //render small bricks
            for (int i = 0; i < smallBricks.size(); i++) {
                Brick brick =  smallBricks.get(i);
                brick.render(g);
            }
            //render big bricks
            for (int i = 0; i < bigBricks.size(); i++) {
                Brick brick =  bigBricks.get(i);
                brick.render(g);
            }
            //render lives
            for(int i =0 ; i < getLives(); i++){
               g.drawImage(Assets.heart, 1*(50*i), getHeight() - 50, 50, 50, null);
            }
            
            if(getLives() == 0 || getContBricks() == 19){
                g.drawImage(Assets.gameOver, 0, 0, width, height, null);
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
