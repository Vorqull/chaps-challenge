package Maze.BoardObjects.Actors;

import Maze.Board;
import Maze.BoardObjects.Tiles.AbstractTile;
import Maze.BoardObjects.Tiles.Key;
import Maze.BoardObjects.Tiles.Treasure;
import Maze.Position;
import com.google.common.base.Preconditions;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * The actor that the player is controlling.
 * @author michael tang 300490290
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

    /**
     * Returns whether or not the player has a key of the specified colour.
     * @param key The colour of the key being asked.
     * @return Returns true if the player has a key of the colour. False if not.
     */
    public boolean hasKey(String key) {
        Preconditions.checkArgument(key == "Red" ||
                key == "Green" ||
                key == "Blue", "Key colour must be Red, Green or Blue");
        for (AbstractTile k : collectibles.get("keySet")) {
            assert (k instanceof Key) : "Tiles in keySet isn't of the Key type";
            if(key.equals(((Key) k).getColour())) return true;
        }
        return false;
    }

    /**
     * Returns the a key of the specified colour that the player
     * has picked up.
     * @param colour THe colour of the key
     * @return Returns the a key of the specified colour that the player
     *         has picked up.
     */
    public Key getKey(String colour) {
        Preconditions.checkArgument(colour == "Red" ||
                colour == "Green" ||
                colour == "Blue", "Key colour must be Red, Green or Blue");
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
        Preconditions.checkNotNull(key, "Key must not be null").interact(this); //Makes sure the player interacts with that tile.
        getKeys().add(key); //Adds the key to the player.
    }

    /**
     * Returns a list of the keys that the player owns.
     * @return Returns a list of the keys that the player has.
     */
    @SuppressWarnings("unchecked")
    public List<Key> getKeys() {
        //Making sure all tiles in keySet are Keys
        for (AbstractTile k : collectibles.get("keySet")) {
            assert (k instanceof Key) : "Tiles in keySet isn't of the Key type";
        }
        return (List<Key>) collectibles.get("keySet");
    }

    /**
     * Returns a set of treasures that the player has.
     * @return Returns a set of treasures that the player has.
     */
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
        Preconditions.checkNotNull(treasure, "Treasure must not be null").interact(this); //Makes sure the player interacts with that tile
        getTreasures().add(treasure); //Adds the treasure to the player
    }

    /**
     * Not used for player since it receives inputs from the GUI
     * @param player .
     * @param board .
     */
    @Override
    public void move(Player player, Board board) {
        assert false : "Player move should never be called upon";
    }

    /**
     * Player doesn't interact with itself, though this could be set up for co-op...?
     * @param player .
     */
    public void interact(Player player) {
        assert false : "Player interact should never be called upon";
    }

    /**
     * Returns a collection of the collectibles that the player has.
     * Lets people create new key like tile plugins easily if needed.
     * @return Returns a collection of the collectibles that the player has.
     */
    public Map<String, Collection<? extends AbstractTile>> getCollectibles() {
        return collectibles;
    }

    /**
     * Changes the player's image to the right.
     */
    public void flipRightImage(){
        currentImage = images.get("Astronaut");
    }

    /**
     * Changes the player's image to the left.
     */
    public void flipLeftImage(){
        currentImage = images.get("AstronautFlipped");
    }
}
