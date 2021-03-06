package Renderer;

import java.awt.*;

/**
 * Represents a star to be drawn in the space background of
 * the rendering window.
 * @author Chris (ID: 300498017)
 */
public class Star {
    private double x, y;
    private final double velocity;
    private final int size;

    /**
     * Creates a star.
     * @param x position
     * @param y position
     * @param velocity The speed it will travel along the x axis
     * @param size Size of the star
     */
    public Star(double x, double y, double velocity, int size) {
        this.x = x;
        this.y = y;
        this.velocity = velocity;
        this.size = size;
    }

    /**
     * Updates the position of the star for the next tick.
     * @return True if star is still on the screen, else false
     */
    public boolean updatePos(){
        x += velocity;
        if (x > Renderer.CANVAS_SIZE){
            return false;
        }
        return true;
    }

    /**
     * Moves the star so that when the plays moves in a direction
     * the start appears to stay in the same place
     */
    public void playerMoved(Renderer.DIRECTION direction){
        switch (direction){
            case UP:
                y += Renderer.IMAGE_SIZE/2.0; break;
            case DOWN:
                y -= Renderer.IMAGE_SIZE/2.0; break;
            case LEFT:
                x += Renderer.IMAGE_SIZE/2.0; break;
            case RIGHT:
                x -= Renderer.IMAGE_SIZE/2.0; break;
            default:

        }
    }

    /**
     * Draws itself on the screen.
     * @param g2 The graphics object from Renderer.paint()
     */
    public void draw(Graphics2D g2){
        g2.setColor(Color.WHITE);
        g2.fillOval((int)x, (int)y, size, size);
    }
}
