package Maze.BoardObjects.Actors;

import Maze.Board;
import Maze.Position;

/**
 * An actor that follows a specific pattern/route defined by it's constructor parameter.
 */
public class PatternEnemy extends AbstractActor{

    private String route;

    public PatternEnemy(Position position, String route) {
        super(position);
        this.route = route;
    }

    @Override
    public void move(Player player, Board board){

    }

    public void interact(Player player) {
    }

}
