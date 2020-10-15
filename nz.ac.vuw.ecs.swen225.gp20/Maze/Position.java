package Maze;

import java.util.Objects;

public class Position {
    private int x, y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

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

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Position getPositionCopy(){
        return new Position(x, y);
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
