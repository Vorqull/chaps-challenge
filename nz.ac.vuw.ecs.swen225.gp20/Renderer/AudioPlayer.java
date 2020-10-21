package Renderer;

import Maze.BoardObjects.Tiles.*;
import Maze.Game;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Deals with sound aspect of the game
 * @author Chris (ID: 300498017)
 */
public class AudioPlayer {
    private final Map<String, Clip> sounds;
    private final Set<AbstractTile> usedTiles; //This assumes all tiles can only make sounds once

    /**
     * Creates an audio player, loads audio files from
     * the directory /Resources/audio
     */
    public AudioPlayer() {
        sounds = new HashMap<>();
        usedTiles = new HashSet<>();

        //Iterates through the audio directory and loads all the clips
        File[] files = new File(System.getProperty("user.dir") + "/Resources/audio").listFiles();
        assert files != null;
        for (File file : files){
            try {
                //Creates the audio clip and stores it in the map to be played later
                AudioInputStream sound = AudioSystem.getAudioInputStream(file);
                Clip clip = AudioSystem.getClip();
                clip.open(sound);
                String name = file.getName().substring(0,file.getName().length()-4); //removes .wav extension
                sounds.put(name, clip);
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Every paint frame this will be called with the tile that the
     * player is standing on and play the associated sound. As long
     * as it hasn't been played already
     * @param tile Tile that the player is standing on
     * @param game The game
     */
    public void playTileSound(AbstractTile tile, Game game){
        if (usedTiles.contains(tile)) return;
        if (tile instanceof LockedDoor){
            playSound("SwipeGood");
            playSound("DoorOpen");
        }
        if (tile instanceof Key){
            playSound("TaskOpen");
        }
        if (tile instanceof Treasure){
            if (game.allTreasuresCollected()){
                playSound("Upload");
                playSound("DoorOpen");
            } else {
                playSound("Download");
            }
        }
        if (tile instanceof ExitPortal){
            playSound("Vent" + (int)(Math.random()*3 + 1));
        }
        usedTiles.add(tile);
    }

    /**
     * Play a given sound from the game
     * @param name The sound file without the file extension eg "DoorOpen"
     */
    public void playSound(String name){
        Clip clip = sounds.get(name);
        clip.setFramePosition(0);
        clip.start();
    }
}
