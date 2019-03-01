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
public class Ball extends Item {

    private int direction;
    private int width;
    private int height;
    private Game game;
    private int speed;

    public Ball(int x, int y, int direction, int width, int height, Game game) {

        super(x, y);
        this.direction = direction;
        this.width = width;
        this.height = height;
        this.game = game;
        this.speed = 2;
    }

    public int getDirection() {
        return direction;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    @Override
    public void tick() {
        //Mientras start sea true
        if (game.isStart()) {
            //Las siguientes 4 condiciones indicaran al personaje la direccion a la 
            // que debe moverse, 1 para la derecha.arriba, 2 izquierda.arriba, 
            // 3 derecha.abajo y 4 izquierda.abajo
            if (getDirection() == 1) {
                setX(getX() + getSpeed());
                setY(getY() - getSpeed());
            }
            if (getDirection() == 2) {
                setX(getX() - getSpeed());
                setY(getY() - getSpeed());
            }
            if (getDirection() == 3) {
                setX(getX() + getSpeed());
                setY(getY() + getSpeed());
            }
            if (getDirection() == 4) {
                setX(getX() - getSpeed());
                setY(getY() + getSpeed());
            }

            // Las siguientes condiciones representan las coliciones con las paredes
            // Cada vez que se genera una colicion se cambia de direccion a la 
            // Contraria del movimiento actual de la bola
            //Si choca en el borde derecho y la direccion es 1
            if (getX() + 50 >= game.getWidth() && getDirection() == 1) {
                //Se cambia la direccion a 2
                setDirection(2);
            } //Si choca en el borde izquierdo y la direccion es 2
            else if (getX() <= 0 && getDirection() == 2) {
                //Se cambia la direccion a 1
                setDirection(1);
            } //Si choca en el borde derecho y la direccion es 3
            else if (getX() + 50 >= game.getWidth() && getDirection() == 3) {
                //Se cambia la direccion a 4
                setDirection(4);
            } //Si choca en el borde izquierdo y la direccion es 4
            else if (getX() <= 0 && getDirection() == 4) {
                //Se cambia la direccion a 3
                setDirection(3);
            }
            //Si choca en el borde superior y la direccion es 1
            if (getY() <= 0 && getDirection() == 1) {
                //Se cambia la direccion a 3
                setDirection(3);
            } //Si choca en el borde superior y la direccion es 2
            else if (getY() <= 0 && getDirection() == 2) {
                //Se cambia la direccion a 4
                setDirection(4);
            }
        }

    }

    public Rectangle getPerimetro() {
        return new Rectangle(getX(), getY(), getWidth(), getHeight());
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(Assets.ball, getX(), getY(), getWidth(), getHeight(), null);
    }

    public boolean intersecta(Object obj) {
        return obj instanceof Brick && getPerimetro().intersects(((Brick) obj).getPerimetro());
    }
    

}
