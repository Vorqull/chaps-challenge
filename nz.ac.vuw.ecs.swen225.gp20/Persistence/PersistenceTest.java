package Persistence;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import Maze.Position;
import Maze.BoardObjects.Actors.AbstractActor;
import Maze.BoardObjects.Actors.PatternEnemy;
import Maze.BoardObjects.Actors.Player;
import Maze.BoardObjects.Tiles.AbstractTile;

public class PersistenceTest {
	public static void main(String[] args) {
	    CSVReader csvRead = new CSVReader();
	    LevelJSONMaker makeJSON = new LevelJSONMaker();
	    Persistence getJson = new Persistence();
	    makeJSON.makeJSON(csvRead.readCSV("nz.ac.vuw.ecs.swen225.gp20/Persistence/level.csv"), "nz.ac.vuw.ecs.swen225.gp20/Persistence/level.json");
	    
	    Level testLevel = getJson.getLevel(1);
	    AbstractTile[][] tileTest = testLevel.getTileArray();
	    String className = tileTest[10][10].getClass().getName();
	    //System.out.println("Enemies " + testLevel.getEnemies().get(0).getPos());
	    
	    ArrayList<AbstractActor> actorArray = new ArrayList<AbstractActor>();
	    
	    AbstractActor testActor = new PatternEnemy(new Position(2, 6), 5,"GRIMBA");
	    
	    actorArray.add(testActor);
	    
	    String testString = testActor.getClass().getName();
	    
	    getJson.saveGame(91, new Player(new Position(3, 4)), actorArray, 1, tileTest);
	    
	    Level newLevel = Persistence.loadGame(1);
	    System.out.println(newLevel.getEnemies().get(0).getPos());
	}
}
