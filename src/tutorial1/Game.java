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
import java.io.FileReader;
import java.util.LinkedList;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;


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
    private SoundClip song;
    
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
        song = new SoundClip("/tutorial1/music/Tamacun.wav");
        //Activa la repetición del clip
        song.setLooping(true);
        //Reproduce el clip
        song.play();
        
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

            setCountForPower(7);

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
            //Set the smallBrick to their initial positions
            for (int i = 0; i < smallBricks.size(); i++) {
                Brick brick =  smallBricks.get(i);
                brick.setLives(1);
                brick.setX(1*(i*60)+ 10);
                brick.tick();
            }
             //Set the bigBrick to their initial positions
            for (int i = 0; i < bigBricks.size(); i++) {
                Brick brick =  bigBricks.get(i);
                brick.setLives(3);
                brick.setX(1*(i*120)+ 20);
                brick.tick();
            }
            //Set finish to false
            setFinish(false);
            //The song is played
            song.play();
            //pStop is called set the key press back to false
            getKeyManager().pStop();

        }
        //When p is press
        if (getKeyManager().pause){
            //if pause is true
            if (isPause()){
                //set pause to false
                setPause(false);
                //pStop is called set the key press back to false
                getKeyManager().pStop();
            }
            //if pause is false
            else{
                //set pause to true
                setPause(true);
                //pStop is called set the key press back to false
                getKeyManager().pStop();
            }
        }
        
        //when s is press
        if(getKeyManager().save){
            //the game is saved
            saveGame();
        }
        
        //when l is press
        if(getKeyManager().load){
            //The game is load
            loadGame();
            //The song is played
            song.play();
        }
        //Si la pelota intersecta con el player en la mitad derecha
        if (player.intersecta(ball)) {
            //The direction of the ball is changed to 2
            ball.setDirection(2);
        }
        //Si la pelota intersecta con el player en la mitad derecha
        else if (player.intersecta2(ball)) {
            //The direction of the ball is changed to 1
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
            //If the ball intersects a brick
            if(ball.intersecta(brick)){
                //the brick lose one life
                brick.setLives(brick.getLives() - 1);
                //decrease count down for power when brick is destroyed
                setCountForPower(getCountForPower()-1);
                //add q to the counter of bricks destroyed 
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
            //if ball intersects a brick
            if(ball.intersecta(brick)){
                //brick lose one life
                brick.setLives(brick.getLives() - 1);
                //decrease count down for power when brick is destroyed
                if(brick.getLives() == 0){
                //decrease count down for power when brick is destroyed
                setCountForPower(getCountForPower()-1);
                //add q to the counter of bricks destroyed 
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
        //if ball intersect the flask
        if(ball.intersecta(flask)){
            //ball increase its speed by 2
            ball.setSpeed(ball.getSpeed() + 2);
            //the flask disapear from the screen
            flask.setX(width + 20);
        }
        //If you lose all your life or destroy all the bricks
        if(getLives() == 0 || getContBricks() == 19){
            //Set finish to true
            setFinish(true);
            //The song is stoped
            song.stop();
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
               g.drawImage(Assets.heart, 1*(50*i), height - 50, 50, 50, null);
            }
            //if the lives get to 0 or all the bricks are destroyed
            if(getLives() == 0 || getContBricks() == 19){
                //the game over screen appears
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
    /**
     * Function that save key variables of the game in a txt file
     */
    private void saveGame(){
        try{
            FileWriter fw = new FileWriter ("save.txt");
            //player position
            fw.write(String.valueOf(player.getX()) + "\n");
            
            //ball atributes
            fw.write(String.valueOf(ball.getX()) + "\n");
            fw.write(String.valueOf(ball.getY()) + "\n");
            fw.write(String.valueOf(ball.getDirection()) + "\n");
            fw.write(String.valueOf(ball.getSpeed()) + "\n");
            
            //small bricks position
            for(int i = 0; i < smallBricks.size() ; i++){
                Brick brick = smallBricks.get(i);
                fw.write(String.valueOf(brick.getX() + "\n"));
            }
            
            //big bricks position and lives
            for(int i = 0; i < bigBricks.size() ; i++){
                Brick brick = bigBricks.get(i);
                fw.write(String.valueOf(brick.getX() + "\n"));
                fw.write(String.valueOf(brick.getLives()) + "\n");
            }
            
            //game lives
            fw.write(String.valueOf(getLives()) + "\n");
            
            //power position and bool
            fw.write(String.valueOf(flask.getX()) + "\n");
            fw.write(String.valueOf(power) + "\n");
            
            //pause bool
            fw.write(String.valueOf(pause) + "\n");
            
            //counters
            fw.write(String.valueOf(countForPower) + "\n");
            
            //start bool
            fw.write(String.valueOf(start) + "\n");
            
            fw.close();
            
            
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }
    /**
     * function that load the information saved in the save.txt file to recover
     * the key variables when the game was save for the last time
     */
    private void loadGame(){
        try{
            BufferedReader br =  new BufferedReader (new FileReader ("save.txt"));
            //player position
            player.setX(Integer.parseInt(br.readLine()));
            
            //ball attributes
            ball.setX(Integer.parseInt(br.readLine()));
            ball.setY(Integer.parseInt(br.readLine()));
            ball.setDirection(Integer.parseInt(br.readLine()));
            ball.setSpeed(Integer.parseInt(br.readLine()));
            
            //small bricks position
            for(int i = 0; i < smallBricks.size(); i++){
                Brick brick = smallBricks.get(i);
                brick.setX(Integer.parseInt(br.readLine()));
            }
            
            //big bricks position
            for(int i = 0; i < bigBricks.size(); i++){
                Brick brick = bigBricks.get(i);
                brick.setX(Integer.parseInt(br.readLine()));
                brick.setLives(Integer.parseInt(br.readLine()));
            }
            //game lives
            setLives(Integer.parseInt(br.readLine()));
            
            //power position and bool
            flask.setX(Integer.parseInt(br.readLine()));
            setPower(Boolean.parseBoolean(br.readLine()));
            
            //pause bool
            setPause(Boolean.parseBoolean(br.readLine()));
            
            //counters
            setCountForPower(Integer.parseInt(br.readLine()));

            
            //start bool
            setStart(Boolean.parseBoolean(br.readLine()));
            
            
        }catch(IOException ex){
            ex.printStackTrace();
            
        }
    }
    
    
}
