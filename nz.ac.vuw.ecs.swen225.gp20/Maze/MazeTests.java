package Maze;

import Maze.BoardObjects.Actors.AbstractActor;
import Maze.BoardObjects.Actors.PatternEnemy;
import Maze.BoardObjects.Actors.Player;
import Maze.BoardObjects.Tiles.*;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class MazeTests {


    /*
      TILE / PLAYER INTERACTIONS
     */

    /**
     * Testing Free tiles, infofields, and wall interactions.
     */
    @Test
    public void test1(){
        AbstractTile[][] map = makeMap();
        map[3][1] = new InfoField("test");

        Player player = new Player(new Position(1, 1));
        Set<AbstractActor> enemies = new HashSet<>();
        Game game = new Game(new Board(map), player, enemies);
        //Testing wall blocking
        game.movePlayer(Game.DIRECTION.UP);
        game.movePlayer(Game.DIRECTION.LEFT);
        assert player.getPos().equals(new Position(1, 1));
        //Testing free moving on free tile and infotile
        game.movePlayer(Game.DIRECTION.RIGHT);
        game.movePlayer(Game.DIRECTION.RIGHT);
        game.movePlayer(Game.DIRECTION.RIGHT);
        assert player.getPos().equals(new Position(3, 1));
    }

    /**
     * Testing Keys and doors 1.
     */
    @Test
    public void test2(){
        AbstractTile[][] map = makeMap();
        map[3][1] = new LockedDoor(true, "Red");
        map[3][2] = new Key("Red");

        Player player = new Player(new Position(1, 1));
        Set<AbstractActor> enemies = new HashSet<>();
        Game game = new Game(new Board(map), player, enemies);
        //Trying to move into locked door without key
        game.movePlayer(Game.DIRECTION.RIGHT);
        game.movePlayer(Game.DIRECTION.RIGHT);
        game.movePlayer(Game.DIRECTION.RIGHT);
        assert player.getPos().equals(new Position(2, 1));
        //Pick up key and move into door
        game.movePlayer(Game.DIRECTION.DOWN);
        game.movePlayer(Game.DIRECTION.RIGHT);
        game.movePlayer(Game.DIRECTION.UP);
        assert player.getPos().equals(new Position(3, 1));
        assert map[3][1].isChanged();
    }

    /**
     * Testing multiple Keys and doors.
     */
    @Test
    public void test3(){
        AbstractTile[][] map = makeMap();
        map[3][1] = new LockedDoor(true, "Red");
        map[2][1] = new Key("Blue");
        map[3][2] = new LockedDoor(true, "Blue");
        map[2][2] = new Key("Green");
        map[3][3] = new LockedDoor(true, "Green");
        map[2][3] = new Key("Red");

        Player player = new Player(new Position(1, 1));
        Set<AbstractActor> enemies = new HashSet<>();
        Game game = new Game(new Board(map), player, enemies);
        //Try move into door with wrong colour key
        game.movePlayer(Game.DIRECTION.RIGHT);
        game.movePlayer(Game.DIRECTION.RIGHT);
        assert player.getPos().equals(new Position(2, 1));
        //Pick up the other keys and move into their respective doors.
        game.movePlayer(Game.DIRECTION.DOWN);
        game.movePlayer(Game.DIRECTION.DOWN);
        game.movePlayer(Game.DIRECTION.RIGHT);
        game.movePlayer(Game.DIRECTION.UP);
        game.movePlayer(Game.DIRECTION.UP);
        assert player.getPos().equals(new Position(3, 1));
        assert map[3][1].isChanged();
        assert map[3][2].isChanged();
        assert map[3][3].isChanged();
    }

    /**
     * Testing multiple Keys and doors of the same Colour.
     */
    @Test
    public void test4(){
        AbstractTile[][] map = makeMap();
        map[3][1] = new LockedDoor(true, "Blue");
        map[2][1] = new Key("Blue");
        map[3][2] = new LockedDoor(true, "Blue");
        map[2][2] = new Key("Blue");

        Player player = new Player(new Position(1, 1));
        Set<AbstractActor> enemies = new HashSet<>();
        Game game = new Game(new Board(map), player, enemies);
        //Move into locked door with key
        game.movePlayer(Game.DIRECTION.RIGHT);
        game.movePlayer(Game.DIRECTION.RIGHT);
        assert player.getPos().equals(new Position(3, 1));
        //Try move into same colour locked door (key should be already used so can't move into door)
        game.movePlayer(Game.DIRECTION.DOWN);
        assert player.getPos().equals(new Position(3, 1));
        assert !map[3][2].isChanged();
        //Get another key and move into door
        game.movePlayer(Game.DIRECTION.LEFT);
        game.movePlayer(Game.DIRECTION.DOWN);
        game.movePlayer(Game.DIRECTION.RIGHT);
        assert player.getPos().equals(new Position(3, 2));
        assert map[3][2].isChanged();
    }

    /**
     * Testing Treasures, ExitLock and ExitPortal.
     */
    @Test
    public void test5(){
        AbstractTile[][] map = makeMap();
        map[2][1] = new ExitLock(true);
        map[3][2] = new ExitLock(false);
        map[2][2] = new Wall();
        map[3][1] = new ExitPortal();
        map[1][3] = new Treasure();
        map[1][2] = new Treasure();

        Player player = new Player(new Position(1, 1));
        Set<AbstractActor> enemies = new HashSet<>();
        Game game = new Game(new Board(map), player, enemies);
        //Try to move into Exitlock
        game.movePlayer(Game.DIRECTION.RIGHT);
        assert player.getPos().equals(new Position(1, 1));
        assert !map[2][1].isChanged();
        //Collect 1 treasure and try to move into exitlock
        game.movePlayer(Game.DIRECTION.DOWN);
        game.movePlayer(Game.DIRECTION.UP);
        game.movePlayer(Game.DIRECTION.RIGHT);
        assert player.getPos().equals(new Position(1, 1));
        assert !map[2][1].isChanged();
        //Collect both treasures, exitlock should now unlock
        game.movePlayer(Game.DIRECTION.DOWN);
        game.movePlayer(Game.DIRECTION.DOWN);
        assert map[3][2].isChanged();
        assert map[2][1].isChanged();
        //Check player can move through both exitlocks
        game.movePlayer(Game.DIRECTION.RIGHT);
        game.movePlayer(Game.DIRECTION.RIGHT);
        game.movePlayer(Game.DIRECTION.UP);
        game.movePlayer(Game.DIRECTION.UP);
        game.movePlayer(Game.DIRECTION.LEFT);
        assert player.getPos().equals(new Position(2, 1));
    }

    /**
     * Testing DeathTiles.
     */
    @Test
    public void test6(){
        AbstractTile[][] map = makeMap();
        map[3][1] = new DeathTile();

        Player player = new Player(new Position(1, 1));
        Set<AbstractActor> enemies = new HashSet<>();
        Game game = new Game(new Board(map), player, enemies);
        //Move into DeathTile, check that player respawned back at their starting point.
        game.movePlayer(Game.DIRECTION.RIGHT);
        game.movePlayer(Game.DIRECTION.RIGHT);
        assert player.getPos().equals(new Position(1, 1));
    }

    /*
      ACTOR / PLAYER INTERACTIONS
    */

    /**
     * Testing Pattern Enemies.
     */
    @Test
    public void test7(){
        AbstractTile[][] map = makeMap();
        map[3][1] = new InfoField("test");

        Player player = new Player(new Position(1, 1));
        Set<AbstractActor> enemies = new HashSet<>();
        PatternEnemy enemy = new PatternEnemy(new Position(2,1), 1, "ddddd");
        enemies.add(enemy);
        Game game = new Game(new Board(map), player, enemies);
        //Testing enemy wall block
        for(int i = 0; i < 5; i++){
            game.moveEnemies();
        }
        assert enemy.getPos().equals(new Position(3, 1));
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
