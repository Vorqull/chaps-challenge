package RecordAndReplay.Actions;

import Maze.Game;

public class EnemyMove extends Action{
    private int x, y; //the x and y which the move from
    private Game.DIRECTION direction;

    public EnemyMove(int x, int y, Game.DIRECTION direction) {
        super(ActionType.ENEMYACTION);
        this.direction = direction;
    }

    public Game.DIRECTION getDirection() {
        return direction;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
