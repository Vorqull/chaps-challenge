package Maze.BoardObjects.Actors;

import Maze.Board;
import Maze.BoardObjects.Tiles.*;
import Maze.Game;
import Maze.Position;

import java.awt.*;

/**
 * An actor that follows a specific pattern/route defined by it's constructor parameter.
 */
public class PatternEnemy extends AbstractActor{

    private final char[] route;
    private int routePos = 0;

    /**
     * THe constructor of the pattern enemy
     * @param position The starting position of the enemy
     * @param tickRate The tick rate of the enemy. Determines how fast the enemy is.
     * @param routeStr The route of the enemy. Follows this string route.
     */
    public PatternEnemy(Position position, int tickRate, String routeStr) {
        super(position, tickRate);
        this.route = routeStr.toCharArray();
        images.put("Enemy1", Toolkit.getDefaultToolkit().getImage("Resources/actors/Enemy1.png"));
        images.put("Enemy1Flipped", Toolkit.getDefaultToolkit().getImage("Resources/actors/Enemy1Flipped.png"));
        currentImage = images.get("Enemy1");
    }

    @Override
    public void move(Player player, Board board){

        char direction = route[routePos]; //Direction of the next move;
        Position newPos;

        switch (Character.toLowerCase(direction)) {
            case 'w':
                newPos = new Position(position, Game.DIRECTION.UP);
                break;
            case 's':
                newPos = new Position(position, Game.DIRECTION.DOWN);
                break;
            case 'a':
                newPos = new Position(position, Game.DIRECTION.LEFT);
                currentImage = images.get("Enemy1Flipped"); //Changes the actor image direction
                break;
            case 'd':
                newPos = new Position(position, Game.DIRECTION.RIGHT);
                currentImage = images.get("Enemy1"); //Changes the actor image direction
                break;
            default:
                throw new IllegalStateException("Unexpected direction: " + direction);
        }

        assert (newPos.getX() >= 0 &&
                newPos.getX() <= board.getMap().length - 1 &&
                newPos.getY() >= 0 &&
                newPos.getY() <= board.getMap()[0].length - 1)
                : "New position is out of bounds.";
        assert (board.getMap()[newPos.getX()][newPos.getY()] != null)
                : "Position at array is null. If you're here then something really bad happened...";


        AbstractTile tile = board.getMap()[newPos.getX()][newPos.getY()];

        //Don't move the actor into a wall or locked door.
        //Move the enemy to the new position
        if (!(tile instanceof Wall) && !((tile instanceof LockedDoor) && ((LockedDoor) tile).isLocked())) {
            //Interact with the player if positions are the same.
            if(player.getPos().equals(this.position) || player.getPos().equals(newPos)) {
                interact(player);
            }
            position.setPosition(newPos);
        }

        nextRPos(); //Move route position forward.
    }

    /**
     * The enemy "kills" the player and sends them back to their starting position.
     * @param player The player.
     */
    @Override
    public void interact(Player player) {
        player.getPos().setPosition(player.getStartingPos());
    }

    /**
     * Moves the route position of the route to the next iteration.
     */
    private void nextRPos(){
        if(routePos >= route.length - 1) routePos = 0;
        else routePos++;
    }

}
