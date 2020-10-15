package Maze.BoardObjects.Actors;

import Maze.Board;
import Maze.BoardObjects.Tiles.AbstractTile;
import Maze.BoardObjects.Tiles.Key;
import Maze.BoardObjects.Tiles.Treasure;
import Maze.Position;

import java.util.*;

/**
 * The actor that the player is controlling.
 */
public class Player extends AbstractActor{

    private Map<String, Collection<? extends AbstractTile>> collectables = new HashMap<>();

    /**
     * .
     * @param position .
     */
    public Player(Position position) {
        super(position);
        Set<Key> keySet = new HashSet<>(); //Keys that the player has picked up.
        Set<Treasure> treasureSet = new HashSet<>(); //Treasures that the player has picked up.
        collectables.put("keySet", keySet);
        collectables.put("treasureSet", treasureSet);
    }

    public boolean hasKey(String key) {
        for (AbstractTile k : collectables.get("keySet")) {
            assert (k instanceof Key) : "Tiles in keySet isn't of the Key type";
            if(key.equals(((Key) k).getColour())) return true;
        }
        return false;
    }

    public Key getKey(String colour) {
        for (AbstractTile k : collectables.get("keySet")) {
            assert (k instanceof Key) : "Tiles in keySet isn't of the Key type";
            if(colour.equals(((Key) k).getColour())) return (Key) k;
        }
        return null;
    }

    public Set<Key> getUnmodKeys(){

        Set<Key> keyCopies = new HashSet<>();

            //Create copies of keys

        return keyCopies;
    }

    public Set<Key> getKeys() {
        //Making sure all tiles in keySet are Keys
        for (AbstractTile k : collectables.get("keySet")) {
            assert (k instanceof Key) : "Tiles in keySet isn't of the Key type";
        }
        return (Set<Key>) collectables.get("keySet");
    }

    public Set<Treasure> getTreasures() {
        //Making sure all tiles in treasureSet are Treasures
        for (AbstractTile t : collectables.get("treasureSet")) {
            assert (t instanceof Treasure) : "Tiles in keySet isn't of the Key type";
        }
        return (Set<Treasure>) collectables.get("treasureSet");
    }

    @Override
    public void move(Player player, Board board) {

    }

    public void interact(Player player) {
    }
}
