package Maze;

import java.util.Objects;

/**
 * A class containing a x and y position
 * used for any board objects
 * @author michael tang 300490290
 */
public class Position {
    private int x, y;

    /**
     * THe constructor of the position class
     * @param x The x position
     * @param y The y position
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * A constructor of the position
     * Creates the position based of the position parameter
     * moved 1 step in the direction of the direction parameter.
     * @param position The position
     * @param direction The direction to move to.
     */
    public Position(Position position, Game.DIRECTION direction) {
        switch (direction) {
            case UP:
                this.x = position.getX();
                this.y = position.getY()-1;
                break;
            case DOWN:
                this.x = position.getX();
                this.y = position.getY()+1;
                break;
            case LEFT:
                this.x = position.getX()-1;
                this.y = position.getY();
                break;
            case RIGHT:
                this.x = position.getX()+1;
                this.y = position.getY();
                break;
            default:
                throw new IllegalStateException("Unexpected direction: " + direction);
        }
    }

    /**
     * Moves the position 1 step in the specified direction.
     * @param direction The direction to move to.
     */
    public void move(Game.DIRECTION direction) {
        switch (direction) {
            case UP:
                y--;
                break;
            case DOWN:
                y++;
                break;
            case LEFT:
                x--;
                break;
            case RIGHT:
                x++;
                break;
            default:
                throw new IllegalStateException("Unexpected direction: " + direction);
        }
    }

    /**
     * returns the x position
     * @return Returns the x position
     */
    public int getX() {
        return x;
    }

    /**
     * returns the y position
     * @return Returns the y position
     */
    public int getY() {
        return y;
    }

    /**
     * Returns a copy of this position
     * @return Returns a copy of this position.
     */
    public Position getPositionCopy(){
        return new Position(x, y);
    }

    /**
     * Sets the this position to the specified position
     * @param position The position to be set to.
     */
    public void setPosition(Position position){
        this.x = position.getX();
        this.y = position.getY();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return x == position.x &&
                y == position.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "x: " + x + ", y: " + y;
    }


}
