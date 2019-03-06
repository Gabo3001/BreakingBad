/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tutorial1;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author HOME
 */
public class KeyManager implements KeyListener {

    public boolean up;
    public boolean down;
    public boolean left;
    public boolean right;
    public boolean space;
    public boolean pause;
    public boolean save;
    public boolean load;
    public boolean reset;
    private boolean keys[];

    public KeyManager() {
        keys = new boolean[256];
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //if the keyCode is different to 80 (keyCode for p)
        if (e.getKeyCode() != KeyEvent.VK_P && e.getKeyCode() != KeyEvent.VK_R) {
            // set true to every key pressed
            keys[e.getKeyCode()] = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //if the keyCode is equl to 80 (keyCode for p)
        if (e.getKeyCode() == KeyEvent.VK_P) {
            keys[e.getKeyCode()] = true;
        } 
        else if(e.getKeyCode() == KeyEvent.VK_R){
            keys[e.getKeyCode()] = true;
        }
        else {
            // set false to every key released
            keys[e.getKeyCode()] = false;
        }
    }
    
    public void pStop(){
        //Function that set in false the p and r key
        keys [KeyEvent.VK_P] = false;
        keys [KeyEvent.VK_R] = false;
    }

    /**
     * to enable or disable moves on every tick
     */
    public void tick() {
        up = keys[KeyEvent.VK_UP];
        down = keys[KeyEvent.VK_DOWN];
        left = keys[KeyEvent.VK_LEFT];
        right = keys[KeyEvent.VK_RIGHT];
        space = keys[KeyEvent.VK_SPACE];
        pause = keys[KeyEvent.VK_P];
        save = keys[KeyEvent.VK_S];
        load = keys[KeyEvent.VK_L];
        reset = keys[KeyEvent.VK_R];

    }
}
