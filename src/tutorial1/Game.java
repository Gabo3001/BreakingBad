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

/**
 *
 * @author HOME
 */
public class Game implements Runnable{
    
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
    private KeyManager keyManager;
    private boolean start;          //Booleana que controlara el inicio
    
    public Game(String title, int width, int height){
        this.title = title;
        this.width = width;
        this.height = height;
        running = false;
        keyManager = new KeyManager();
        start = false;                  //Se inicializa start en false
    }

    public boolean isStart() {
        return start;
    }

    public void setStart(boolean start) {
        this.start = start;
    }
    
    public int getWidth(){
        return width;
    }

    public int getHeight() {
        return height;
    }
    
    private void init(){
        display = new Display(title, getWidth(), getHeight());
        Assets.init();
        player = new Player(330, getHeight() - 100, 1, 160, 80, this);
        ball = new Ball(385, getHeight() - 145, 1, 50, 50, this);
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
        while (running){
            // setting the time now to the actual time
            now = System.nanoTime();
            // acumulating to delta the difference between times in timeTick units
            delta += (now - lastTime) / timeTick;
            //updating the last time
            lastTime = now;
            
            // if delta is positive we tick the game
            if (delta >= 1){
                tick();
                render();
                delta --;
            }
        }
        stop();    
    }

    public KeyManager getKeyManager() {
        return keyManager;
    }
    
    private void tick(){
        keyManager.tick();

        player.tick();
        ball.tick();
        //Si se preciona space
        if (getKeyManager().space){
            //Se colocara start en true
            setStart(true);
        }
        //Si la pelota intersecta con el player
        if (player.intersecta(ball)){
            //Si la bola se mueve en la direccion 3 (Abajo a la derecha)
            if (ball.getDirection() == 3){
                //Se cambia la direccion a 1 (Arriba a la derecha)
                ball.setDirection(1);
            }
            //En el caso contrario
            else{
                //Se cambia la direcaccion a 2 (Arriba a la izquierda)
                ball.setDirection(2);
            }
        }
    }
    
    private void render(){
        bs = display.getCanvas().getBufferStrategy();
        
        if(bs == null){
            display.getCanvas().createBufferStrategy(3);
        }
        else
        {
            g = bs.getDrawGraphics();
            g.drawImage(Assets.background, 0, 0, width, height, null);
            player.render(g);
            ball.render(g);
            bs.show();
            g.dispose();
        }
    }
    
    public synchronized void start(){
        if(!running){
            running = true;
            thread = new Thread(this);
            thread.start();
        }
    }
    
    public synchronized void stop(){
        if(running){
            running = false;
            try{
                thread.join();
            }catch (InterruptedException ie){
                ie.printStackTrace();
            }
        }
    }
}
