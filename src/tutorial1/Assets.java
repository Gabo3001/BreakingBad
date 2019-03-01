/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tutorial1;

import java.awt.image.BufferedImage;

/**
 *
 * @author HOME
 */
public class Assets {
    public static BufferedImage background;
    public static BufferedImage player;
    public static BufferedImage ball;
    public static BufferedImage smallBrick;
    
    public static void init(){
        background = ImageLoader.loadImage("/tutorial1/images/Background.png");
        player = ImageLoader.loadImage("/tutorial1/images/PoliceCar.png");
        ball = ImageLoader.loadImage("/tutorial1/images/Ball.png");
        smallBrick = ImageLoader.loadImage("/tutorial1/images/meth.png");
    }
    
    
    
}
