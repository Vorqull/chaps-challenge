package Maze.BoardObjects.Actors;

import Maze.Board;
import Maze.BoardObjects.Tiles.AbstractTile;
import Maze.BoardObjects.Tiles.Key;
import Maze.BoardObjects.Tiles.Treasure;
import Maze.Position;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * The actor that the player is controlling.
 */
public class Player extends AbstractActor{

    private final Map<String, Collection<? extends AbstractTile>> collectibles = new HashMap<>();

    /**
     * The player's constructor, doesn't need a tick rate parameter
     * because it moves independently based on the player's input.
     * @param position The starting position of the player.
     */
    public Player(Position position) {
        super(position, 0);
        List<Key> keySet = new ArrayList<>(); //Keys that the player has picked up.
        Set<Treasure> treasureSet = new HashSet<>(); //Treasures that the player has picked up.
        collectibles.put("keySet", keySet);
        collectibles.put("treasureSet", treasureSet);
        images.put("Astronaut", Toolkit.getDefaultToolkit().getImage("Resources/actors/Astronaut.png"));
        images.put("AstronautFlipped", Toolkit.getDefaultToolkit().getImage("Resources/actors/AstronautFlipped.png"));
        currentImage = images.get("Astronaut");
    }

    public boolean hasKey(String key) {
        for (AbstractTile k : collectibles.get("keySet")) {
            assert (k instanceof Key) : "Tiles in keySet isn't of the Key type";
            if(key.equals(((Key) k).getColour())) return true;
        }
        return false;
    }

    public Key getKey(String colour) {
        for (AbstractTile k : collectibles.get("keySet")) {
            assert (k instanceof Key) : "Tiles in keySet isn't of the Key type";
            if(colour.equals(((Key) k).getColour())) return (Key) k;
        }
        return null;
    }

    /**
     * Allows the player to pick up a key. Used for save files.
     * @param key The key to be picked up
     */
    public void pickupKey(Key key){
        key.interact(this); //Makes sure the player interacts with that tile.
        getKeys().add(key); //Adds the key to the player.
    }

    @SuppressWarnings("unchecked")
    public List<Key> getKeys() {
        //Making sure all tiles in keySet are Keys
        for (AbstractTile k : collectibles.get("keySet")) {
            assert (k instanceof Key) : "Tiles in keySet isn't of the Key type";
        }
        return (List<Key>) collectibles.get("keySet");
    }

    @SuppressWarnings("unchecked")
    public Set<Treasure> getTreasures() {
        //Making sure all tiles in treasureSet are Treasures
        for (AbstractTile t : collectibles.get("treasureSet")) {
            assert (t instanceof Treasure) : "Tiles in keySet isn't of the Key type";
        }
        return (Set<Treasure>) collectibles.get("treasureSet");
    }

    /**
     * Allows the player to pick up a treasure. Used for save files.
     * @param treasure The treasure to be picked up.
     */
    public void pickupTreasure(Treasure treasure){
        treasure.interact(this); //Makes sure the player interacts with that tile
        getTreasures().add(treasure); //Adds the treasure to the player
    }

    /**
     * Not used for player since it receives inputs from the GUI
     * @param player .
     * @param board .
     */
    @Override
    public void move(Player player, Board board) {

    }

    /**
     * Player doesn't interact with itself, though this could be set up for co-op...?
     * @param player .
     */
    public void interact(Player player) {
    }

    public Map<String, Collection<? extends AbstractTile>> getCollectibles() {
        return collectibles;
    }

    public void flipRightImage(){
        currentImage = images.get("Astronaut");
    }

    public void flipLeftImage(){
        currentImage = images.get("AstronautFlipped");
    }
}
