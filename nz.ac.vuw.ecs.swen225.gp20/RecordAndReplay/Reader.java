package RecordAndReplay;

import Maze.Game;
import Maze.Position;
import RecordAndReplay.Actions.Action;
import RecordAndReplay.Actions.EnemyMove;
import RecordAndReplay.Actions.PlayerMove;

import javax.json.*;
import java.io.*;
import java.util.ArrayList;

/**
 * Should be able to read a json file.
 * Once it's done loading json, it can be used as an object for other components to retrieve data from.
 */
public class Reader {
    private ArrayList<Recorder.Change> recordedChanges = new ArrayList<Recorder.Change>();
    private int level;
    private int startRecordingTimeStamp;
    private int playerStartX;
    private int playerStartY;
    private ArrayList<Position> enemyStartPositions = new ArrayList<>();
    private int levelLocation;

    /**
     * Empty constructor
     */
    public Reader() {}

    /**
     * Read a json file and prepare all variables.
     * @param file
     */
    public void readJson(File file) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            System.out.println("Error: " + e);
        }
        JsonReader reader = Json.createReader(inputStream);
        JsonObject obj = reader.readObject();
        level = obj.getInt("Level");
        startRecordingTimeStamp = obj.getInt("startRecordingTimeStamp");
        JsonObject playerPos = obj.getJsonObject("playerPos");
        playerStartX = playerPos.getInt("startX");
        playerStartY = playerPos.getInt("startY");

        //Get enemy starting positions
        JsonObject enemyStartPos = obj.getJsonObject("enemies");
        for(int i = 0; i < enemyStartPos.size(); i++) {
            JsonObject pos = enemyStartPos.getJsonObject("" + i);
            Position p = new Position(pos.getInt("startX"), pos.getInt("startY"));
            enemyStartPositions.add(p);
        }

        //Get the array of changes.
        int noChanges = obj.getInt("noChanges");
        for(int i = 0; i < noChanges; i++) {
            int actual = i + 1;
            JsonObject changeList = obj.getJsonObject("change" + actual);

            //Get timestamp
            int timeStamp = changeList.getInt("Timestamp");

            //Get the array of actions
            ArrayList<Action> actions = new ArrayList<Action>();
            for(int j = 0; j < changeList.size()-1; j++) {
                //FIRST get object
                JsonObject jsonAction = changeList.getJsonObject("" + j);

                //SECOND check type (brute force)
                Action action;
                if(jsonAction.get("PlayerMove") != null) {
                    if(jsonAction.get("PlayerMove").equals(Json.createValue("UP"))) {
                        action = new PlayerMove(Game.DIRECTION.UP);
                        actions.add(action);
                    } else if(jsonAction.get("PlayerMove").equals(Json.createValue("DOWN"))) {
                        action = new PlayerMove(Game.DIRECTION.DOWN);
                        actions.add(action);
                    } else if(jsonAction.get("PlayerMove").equals(Json.createValue("LEFT"))) {
                        action = new PlayerMove(Game.DIRECTION.LEFT);
                        actions.add(action);
                    } else if(jsonAction.get("PlayerMove").equals(Json.createValue("RIGHT"))) {
                        action = new PlayerMove(Game.DIRECTION.RIGHT);
                        actions.add(action);
                    } else {
                        //should NEVER get to this point.
                    }
                    //Add to array of actions
                } else if(jsonAction.get("EnemyMove") != null) {
                    int x = jsonAction.getInt("x");
                    int y = jsonAction.getInt("y");
                    if(jsonAction.get("EnemyMove").equals(Json.createValue("UP"))) {
                        action = new EnemyMove(x, y, Game.DIRECTION.UP);
                        actions.add(action);
                    } else if(jsonAction.get("EnemyMove").equals(Json.createValue("DOWN"))) {
                        action = new EnemyMove(x, y, Game.DIRECTION.DOWN);
                        actions.add(action);
                    } else if(jsonAction.get("EnemyMove").equals(Json.createValue("LEFT"))) {
                        action = new EnemyMove(x, y, Game.DIRECTION.LEFT);
                        actions.add(action);
                    } else if(jsonAction.get("EnemyMove").equals(Json.createValue("RIGHT"))) {
                        action = new EnemyMove(x, y, Game.DIRECTION.RIGHT);
                        actions.add(action);
                    } else {
                        //should NEVER get to this point.
                    }
                }
            }
            Recorder.Change c = new Recorder.Change(actions, timeStamp);
            recordedChanges.add(c);
        }

        levelLocation = obj.getInt("loadState");
    }

    /** GETTERS **/
    public ArrayList<Recorder.Change> getRecordedChanges() {
        return recordedChanges;
    }
    public int getLevel() {
        return level;
    }
    public int getStartRecordingTimeStamp() {
        return startRecordingTimeStamp;
    }
    public int getPlayerStartX() {
        return playerStartX;
    }
    public int getPlayerStartY() {
        return playerStartY;
    }
    public ArrayList<Position> getEnemies() {
        return enemyStartPositions;
    }
    public int getLevelLocation() { return levelLocation; }
}
