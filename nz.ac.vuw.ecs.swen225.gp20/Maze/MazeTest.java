package Maze;

import Maze.BoardObjects.Actors.AbstractActor;
import Maze.BoardObjects.Actors.PatternEnemy;
import Maze.BoardObjects.Actors.Player;
import Maze.BoardObjects.Actors.StalkerEnemy;
import Maze.BoardObjects.Tiles.*;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * THe maze module JUnit tests.
 * @author michael tang 300490290
 */
public class MazeTest {


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
     * Testing Pattern Enemy movement
     */
    @Test
    public void test7(){
        AbstractTile[][] map = makeMap();

        Player player = new Player(new Position(1, 1));
        Set<AbstractActor> enemies = new HashSet<>();
        PatternEnemy enemy = new PatternEnemy(new Position(2,1), 0, "dasw");
        enemies.add(enemy);
        Game game = new Game(new Board(map), player, enemies);
        //Move enemy
        for(int i = 0; i < 4; i++){
            game.moveEnemies();
        }
        assert enemy.getPos().equals(new Position(2, 1));
    }

    /**
     * Testing Pattern enemy block interactions
     */
    @Test
    public void test8(){
        AbstractTile[][] map = makeMap();
        map[3][2] = new LockedDoor(false, "Red");

        Player player = new Player(new Position(1, 1));
        Set<AbstractActor> enemies = new HashSet<>();
        PatternEnemy enemy = new PatternEnemy(new Position(2,1), 0, "ddddss");
        enemies.add(enemy);
        Game game = new Game(new Board(map), player, enemies);
        //Testing enemy wall and locked door block
        for(int i = 0; i < 6; i++){
            game.moveEnemies();
        }
        assert enemy.getPos().equals(new Position(3, 1));
    }

    /**
     * Testing Pattern enemy "kills" the player and respawn them back to the start
     */
    @Test
    public void test9(){
        AbstractTile[][] map = makeMap();

        Player player = new Player(new Position(1, 1));
        Set<AbstractActor> enemies = new HashSet<>();
        PatternEnemy enemy = new PatternEnemy(new Position(2,1), 0, "ssa");
        enemies.add(enemy);
        Game game = new Game(new Board(map), player, enemies);

        game.movePlayer(Game.DIRECTION.DOWN);
        game.movePlayer(Game.DIRECTION.DOWN);
        //Testing enemy wall block
        for(int i = 0; i < 3; i++){
            assert player.getPos().equals(new Position(1,3)); //Check the player isn't moved
            game.moveEnemies();
        }
        assert enemy.getPos().equals(new Position(1, 3)); //Check enemy has moved
        assert player.getPos().equals(player.getStartingPos()); //Check player has moved
    }

    /**
     * Testing Stalker Enemy not moving when player has no items
     */
    @Test
    public void test10(){
        AbstractTile[][] map = makeMap();

        Player player = new Player(new Position(1, 1));
        Set<AbstractActor> enemies = new HashSet<>();
        StalkerEnemy enemy = new StalkerEnemy(new Position(3,3), 0);
        enemies.add(enemy);
        Game game = new Game(new Board(map), player, enemies);

        //Testing enemy not moving
        for(int i = 0; i < 6; i++){
            game.moveEnemies();
        }
        assert enemy.getPos().equals(new Position(3, 3)); //Check enemy has not moved
    }

    /**
     * Testing Stalker Enemy robbing the player
     */
    @Test
    public void test11(){
        AbstractTile[][] map = makeMap();
        Key key = new Key("Red");
        map[1][2] = key;

        Player player = new Player(new Position(1, 1));
        Set<AbstractActor> enemies = new HashSet<>();
        StalkerEnemy enemy = new StalkerEnemy(new Position(3,3), 0);
        enemies.add(enemy);
        Game game = new Game(new Board(map), player, enemies);

        game.movePlayer(Game.DIRECTION.DOWN);
        game.movePlayer(Game.DIRECTION.DOWN);
        assert !player.getKeys().isEmpty();
        assert key.isChanged();

        game.moveEnemies();
        assert enemy.getPos().equals(new Position(2, 3)); //Check enemy has moved
        game.moveEnemies();
        assert enemy.getPos().equals(new Position(3, 3)); //Check enemy has moved back
        assert player.getKeys().isEmpty();
        assert !key.isChanged();
    }

    /**
     * Testing Stalker Enemy not moving behind doors.
     */
    @Test
    public void test12(){
        AbstractTile[][] map = makeMap();
        Treasure treasure = new Treasure();
        ExitLock exitLock1 = new ExitLock(true);
        ExitLock exitLock2 = new ExitLock(true);
        ExitLock exitLock3 = new ExitLock(true);
        map[2][1] = exitLock1;
        map[2][2] = exitLock2;
        map[2][3] = exitLock3;
        map[1][2] = treasure;

        Player player = new Player(new Position(1, 1));
        Set<AbstractActor> enemies = new HashSet<>();
        StalkerEnemy enemy = new StalkerEnemy(new Position(3,3), 0);
        enemies.add(enemy);
        Game game = new Game(new Board(map), player, enemies);

        game.moveEnemies(); //Shouldn't move
        assert enemy.getPos().equals(enemy.getStartingPos());
        game.movePlayer(Game.DIRECTION.DOWN); //Collect treasure
        game.movePlayer(Game.DIRECTION.DOWN); //Move down
        assert exitLock1.isChanged() && exitLock2.isChanged() && exitLock3.isChanged();
        game.moveEnemies(); //Stalker should now move
        assert !enemy.getPos().equals(enemy.getStartingPos());
    }


    /**
     * Makes a tile map of 5 by 5.
     * With wall tiles of the edges and
     * free tile on the inside.
     * @return Returns a basic 5 by 5 tile map.
     */
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
        return map;
    }
}
