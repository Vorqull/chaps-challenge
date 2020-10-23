package RecordAndReplay;

import Maze.BoardObjects.Actors.AbstractActor;
import Maze.Position;
import RecordAndReplay.Actions.Action;
import RecordAndReplay.Actions.EnemyMove;
import RecordAndReplay.Actions.PlayerMove;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.json.*;

/**
 * ONLY ACCESSED THROUGH RecordAndReplay.java
 */
public class Writer {
    public Writer() {}

    /**
     * Write everything needed to replay to a json file.
     */
    public void writeRecording(List<Recorder.Change> gameplay, Position pos, int level, int startRecordingTimeStamp, ArrayList<AbstractActor> enemies) {
        //All actions that take place, in Json.
        JsonObjectBuilder gameplayInJson = Json.createObjectBuilder();

        //FIRST: Note down the level and begin recording timestamp
        gameplayInJson.add("Level", level);
        gameplayInJson.add("startRecordingTimeStamp", startRecordingTimeStamp);

        //SECOND: Note down the positions of Player
        JsonObjectBuilder playerPos = Json.createObjectBuilder();
        playerPos.add("startX", pos.getX());
        playerPos.add("startY", pos.getY());

        gameplayInJson.add("playerPos", playerPos);

        //THIRD: Note down all the posistions of any enemies inside the level.
        int enemyCounter = 0;
        JsonObjectBuilder arrayOfEnemies = Json.createObjectBuilder();
        for(AbstractActor e : enemies) {
            //if(e == null) continue;
            JsonObjectBuilder hostile = Json.createObjectBuilder();
            System.out.println("x: " + e.getPos().getX());
            System.out.println("y: " + e.getPos().getY());

            hostile.add("startX", e.getPos().getX());
            hostile.add("startY", e.getPos().getY());

            arrayOfEnemies.add("" + enemyCounter++, hostile);
        }

        gameplayInJson.add("enemies", arrayOfEnemies);

        int changesCounter = 0;
        for(Recorder.Change c : gameplay) {
            ArrayList<Action> actions = c.actions;

            //'Changes' is all the actions that take place at this one singular moment of the game.
            JsonObjectBuilder changes = Json.createObjectBuilder();

            //Mark down the timestamp first
            JsonObjectBuilder timeStamp = Json.createObjectBuilder();
            changes.add("Timestamp", c.timestamp);

            //turn all actions into json objects
            int actionCounter = 0;
            for(Action a : actions) {
                JsonObjectBuilder action = Json.createObjectBuilder();
                //PLAYER MOVEMENT
                if(a instanceof PlayerMove) {
                    switch (((PlayerMove) a).getDirection()) {
                        case UP:
                            action.add("PlayerMove", "UP");
                            break;
                        case DOWN:
                            action.add("PlayerMove", "DOWN");
                            break;
                        case LEFT:
                            action.add("PlayerMove", "LEFT");
                            break;
                        case RIGHT:
                            action.add("PlayerMove", "RIGHT");
                            break;
                    }
                }
                //ENEMY MOVE
                else if(a instanceof EnemyMove) {
                    switch (((EnemyMove) a).getDirection()) {
                        case UP:
                            action.add("EnemyMove", "UP");
                            action.add("x", ((EnemyMove) a).getX());
                            action.add("y", ((EnemyMove) a).getY());
                            break;
                        case DOWN:
                            action.add("EnemyMove", "DOWN");
                            action.add("x", ((EnemyMove) a).getX());
                            action.add("y", ((EnemyMove) a).getY());
                            break;
                        case LEFT:
                            action.add("EnemyMove", "LEFT");
                            action.add("x", ((EnemyMove) a).getX());
                            action.add("y", ((EnemyMove) a).getY());
                            break;
                        case RIGHT:
                            action.add("EnemyMove", "RIGHT");
                            action.add("x", ((EnemyMove) a).getX());
                            action.add("y", ((EnemyMove) a).getY());
                            break;
                    }
                }
                changes.add("" + actionCounter++, action.build());
            }
            changesCounter++;
            gameplayInJson.add("change" + changesCounter, changes);
        }
        gameplayInJson.add("noChanges", changesCounter);

        gameplayInJson.add("loadState", level);

        //Write to file
        try {
            Date date = Calendar.getInstance().getTime();
            DateFormat dtf = new SimpleDateFormat("yyyyMMddHHmmss");
            String saveFileName = dtf.format(date) + "savedReplay.JSON";

            OutputStream os = new FileOutputStream("SavedReplay/" + saveFileName);
            JsonWriter jsonWriter = Json.createWriter(os);
            jsonWriter.writeObject(gameplayInJson.build());
            jsonWriter.close();
        } catch (FileNotFoundException e) {
            System.out.println("ERROR SAVING GAMEPLAY: " + e);
        }
    }
}
