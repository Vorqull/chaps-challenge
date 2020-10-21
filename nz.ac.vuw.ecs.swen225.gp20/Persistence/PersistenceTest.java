package Persistence;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Scanner;

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
	    
		Scanner sc= new Scanner(System.in);
		System.out.println("Enter the level number you wish to output this as (1-9)");
		String levelNumberString = sc.nextLine();
		sc.close();
		int levelNumber = StringToInt(levelNumberString);
		if(levelNumber > 10 || levelNumber <= 0) {
			System.out.println("Proper number please.");
			return;
		}
		String levelString = "levels/level" + levelNumber + ".JSON";
	    makeJSON.makeJSON(csvRead.readCSV("nz.ac.vuw.ecs.swen225.gp20/Persistence/level.csv"), levelString);
	    
	    Level testLevel = getJson.getLevel(levelNumber);    	        
	    getJson.saveGame(91, testLevel.getPlayer(), testLevel.getEnemies(), 1, testLevel.getTileArray());    
	    Level newLevel = Persistence.loadGame(1);
	    if(newLevel.getEnemies().size()>0) {
	    	System.out.println(newLevel.getEnemies().get(0).getPos());
	    }
	}
	
	  private static int StringToInt(String intString) {
		  int charInt = 0;
		  for(int i = 0; i < intString.length(); i++) {
			  charInt = charInt*10;
			  charInt += intString.charAt(i) - '0';
		  }
		  return charInt;
	  }
}
