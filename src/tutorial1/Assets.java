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
    public static BufferedImage bigBrick; //bigBrick with no damage
    public static BufferedImage bigBrickCracked; //bigBrick after taking one hit
    public static BufferedImage bigBrickLast; //bigBrick after teaking two hits
    public static BufferedImage heart; //heart image for lives
    public static BufferedImage gameOver; //game over screen
    public static BufferedImage flask; //flask icon for special power
    
    public static void init(){
        background = ImageLoader.loadImage("/tutorial1/images/Background.png");
        player = ImageLoader.loadImage("/tutorial1/images/PoliceCar.png");
        ball = ImageLoader.loadImage("/tutorial1/images/Ball.png");
        smallBrick = ImageLoader.loadImage("/tutorial1/images/meth.png");
        bigBrick = ImageLoader.loadImage("/tutorial1/images/bigBrick.png");
        bigBrickCracked = ImageLoader.loadImage("/tutorial1/images/bigBrickCracked.png");
        bigBrickLast = ImageLoader.loadImage("/tutorial1/images/bigBrickLast.png");
        heart = ImageLoader.loadImage("/tutorial1/images/heart.png");
        gameOver = ImageLoader.loadImage("/tutorial1/images/gameOver.png");
        flask = ImageLoader. loadImage("/tutorial1/images/flask.png");
    }
    
    
    
}
