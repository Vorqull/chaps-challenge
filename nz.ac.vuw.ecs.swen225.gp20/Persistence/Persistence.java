package Persistence;

import java.io.File;
import java.util.ArrayList;

import Maze.BoardObjects.Actors.AbstractActor;
import Maze.BoardObjects.Actors.Player;
import Maze.BoardObjects.Tiles.AbstractTile;

public class Persistence {
	public static Level getLevel(int levelNumber){
		String levelString = "levels/level" + levelNumber + ".JSON";
		LevelJSONReader readJSON = new LevelJSONReader();
		Level returnLevel = readJSON.readJSON(levelString);
		return returnLevel;
	}
	
	public boolean saveGame(int remainingTime, Player player, ArrayList<AbstractActor> enemies, int levelNumber, AbstractTile[][] tiles) {
		String saveString = "saves/level"+levelNumber+".JSON";
		SaveJSONMaker.makeJSON(remainingTime, player, enemies, saveString, tiles);
		return true;
	}
	
	public static Level loadGame(int levelNumber) {
		String saveString = "saves/level"+levelNumber+".JSON";
		Level levelUnchanged = getLevel(levelNumber);
		Level levelLoaded = SaveJSONReader.readJSON(saveString, levelUnchanged);
		//Can return as null
		return levelLoaded;
	}
	
	public static boolean eraseSave(int levelNumber) {
		String saveString = "saves/level"+levelNumber+".JSON";
		try  {         
			File f= new File(saveString);           //file to be delete  
			if(f.delete()) {  
				return true;
			}  
			else {  
				return false; 
			}  
		}  
		catch(Exception e){  
			return false;
		}    
	}
}
