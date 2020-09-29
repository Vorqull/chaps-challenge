package RecordAndReplay;

import RecordAndReplay.Actions.Action;
import RecordAndReplay.Actions.PlayerMove;
import RecordAndReplay.Actions.PlayerTileInteraction;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import javax.json.*;

/**
 * Using an arraylist of arraylist.
 * Should be able to create and write a json file recording of the game.
 *
 * Every list of actions is an entity on its own.
 */
public class Writer {
    public Writer() {}

    /**
     * WRITES EVERYTHING IN JSON
     */
    public void writeRecording(List<ArrayList<Action>> gameplay) {
        //All actions that take place, in Json.
        JsonArrayBuilder gameplayInJson = Json.createArrayBuilder();

        for(ArrayList<Action> change : gameplay) {
            //'Changes' is all the actions that take place at this one singular moment of the game.
            JsonArrayBuilder changes = Json.createArrayBuilder();

            //turn all actions into json objects
            for(Action a : change) {
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
                //PLAYER INTERACTION
                else if(a instanceof PlayerTileInteraction) {
                    action.add("PlayerTileInteract", ((PlayerTileInteraction) a).getTileName());
                }
                changes.add(a.getType().getString() + ": " + action.build().toString()); //might wanna to change this later...
            }
            gameplayInJson.add(changes);
        }

        //Write to file
        try {
            OutputStream os = new FileOutputStream("nz.ac.vuw.ecs.swen225.gp20/RecordAndReplay/save.json");
            JsonWriter jsonWriter = Json.createWriter(os);
            jsonWriter.writeArray(gameplayInJson.build());
            jsonWriter.close();

        } catch (FileNotFoundException e) {
            System.out.println("ERROR SAVING GAMEPLAY: " + e);
        }
    }
}
