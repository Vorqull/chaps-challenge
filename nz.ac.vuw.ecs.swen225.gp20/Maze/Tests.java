package Maze;

import Maze.Board;
import Maze.BoardObjects.Actors.AbstractActor;
import Maze.BoardObjects.Actors.Player;
import Maze.BoardObjects.Tiles.*;
import Maze.Game;
import Maze.Position;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class Tests {


    /**
     *
     * TILE / PLAYER INTERACTIONS
     *
     */

    /**
     * Testing Free tiles, infofields, and wall interactions
     */
    @Test
    public void test1(){
        AbstractTile[][] map = makeMap();
        map[3][2] = new InfoField("test");

        Player player = new Player(new Position(1, 1));
        Set<AbstractActor> enemies = new HashSet<>();
        Game game = new Game(new Board(map), player, enemies);

        game.movePlayer(Game.DIRECTION.UP);
        game.movePlayer(Game.DIRECTION.LEFT);
        game.movePlayer(Game.DIRECTION.RIGHT);
        game.movePlayer(Game.DIRECTION.RIGHT);
        game.movePlayer(Game.DIRECTION.RIGHT);
    }












    private AbstractTile[][] makeMap(){
        AbstractTile[][] map = new AbstractTile[5][5];
        //Wall tile borders
        for(int i = 0; i < map.length; i++){
            map[i][0] = new Wall();
            map[i][map[i].length-1] = new Wall();
        }
        for(int j = 1; j < map[j].length - 1; j++){
            map[0][j] = new Wall();
            map[map.length-1][j] = new Wall();
        }

        //Free Tiles in the middle
        for(int i = 1; i < map.length - 1; i++){
            for(int j = 1; j < map[i].length - 1; j++){
                map[i][j] = new FreeTile();
            }
        }

        /**
        map[3][1] = new InfoField("test");
        map[4][1] = new Key("Red");
        map[4][2] = new LockedDoor(false, "Red");
         **/

        return map;
    }
}
