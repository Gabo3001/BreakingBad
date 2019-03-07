/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tutorial1;

import java.awt.Graphics;
import java.awt.Rectangle;

/**
 *
 * @author HOME
 */
public class Brick extends Item{

    private int width;
    private int height;
    private Game game;
    private int lives;
    private int type;

    public Brick(int x, int y, int width, int height, Game game, int type, int lives) {
        super(x, y);
        this.width = width;
        this.height = height;
        this.game = game;
        //types:  small bricks(1), big bricks (2), power(3)
        this.type = type;
        this.lives = lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public int getLives() {
        return lives;
    }

    public int getType() {
        return type;
    }
    

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }


    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    
    @Override
    public void tick() {
        
        if(getLives() ==  0)
            setX(game.getWidth() + 50);
            
        
    }
    /**
     * Creates a rectangle around the brick
     * 
     * @return a rectangle with the dimensions of the brick
     */
    public Rectangle getPerimetro() {
        return new Rectangle(getX(), getY(), getWidth(), getHeight());
    }

    @Override
    public void render(Graphics g) {
        if(getType() == 1)
        g.drawImage(Assets.smallBrick, getX(), getY(), getWidth(), getHeight(), null);    
        
        if(getType() == 2){
            //bigBrick with no damage
            if(getLives() == 3)
            g.drawImage(Assets.bigBrick, getX(), getY(), getWidth(), getHeight(), null);
            //bigBrick after one hit
            if(getLives() == 2)
            g.drawImage(Assets.bigBrickCracked, getX(), getY(), getWidth(), getHeight(), null);
            
            if(getLives() == 1)
            g.drawImage(Assets.bigBrickLast, getX(), getY(), getWidth(), getHeight(), null);
        }
        
        if(getType() == 3)
            g.drawImage(Assets.flask, getX(), getY(), getWidth(), getHeight(), null);
        
    }
    
   
}
