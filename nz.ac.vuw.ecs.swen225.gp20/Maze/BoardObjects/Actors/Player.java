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

    private final Map<String, Collection<? extends AbstractTile>> collectables = new HashMap<>();

    /**
     * .
     * @param position .
     */
    public Player(Position position) {
        super(position, 0);
        List<Key> keySet = new ArrayList<>(); //Keys that the player has picked up.
        Set<Treasure> treasureSet = new HashSet<>(); //Treasures that the player has picked up.
        collectables.put("keySet", keySet);
        collectables.put("treasureSet", treasureSet);
        images.put("Astronaut", Toolkit.getDefaultToolkit().getImage("Resources/actors/Astronaut.png"));
        images.put("AstronautFlipped", Toolkit.getDefaultToolkit().getImage("Resources/actors/AstronautFlipped.png"));
        currentImage = images.get("Astronaut");
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

    @SuppressWarnings("unchecked")
    public List<Key> getKeys() {
        //Making sure all tiles in keySet are Keys
        for (AbstractTile k : collectables.get("keySet")) {
            assert (k instanceof Key) : "Tiles in keySet isn't of the Key type";
        }
        return (List<Key>) collectables.get("keySet");
    }

    @SuppressWarnings("unchecked")
    public Set<Treasure> getTreasures() {
        //Making sure all tiles in treasureSet are Treasures
        for (AbstractTile t : collectables.get("treasureSet")) {
            assert (t instanceof Treasure) : "Tiles in keySet isn't of the Key type";
        }
        return (Set<Treasure>) collectables.get("treasureSet");
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

    public Map<String, Collection<? extends AbstractTile>> getCollectables() {
        return collectables;
    }

    public void flipRightImage(){
        currentImage = images.get("Astronaut");
    }

    public void flipLeftImage(){
        currentImage = images.get("AstronautFlipped");
    }
}
