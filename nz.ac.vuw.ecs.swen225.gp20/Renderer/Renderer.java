package Renderer;

import Application.ChapsChallenge;
import Maze.BoardObjects.Actors.AbstractActor;
import Maze.BoardObjects.Actors.Player;
import Maze.BoardObjects.Tiles.*;
import Maze.Game;
import Maze.Position;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * This is responsible for displaying the Maze on the screen
 * including all it's tiles, animations and sound effects
 * @author Chris (ID: 300498017)
 */
public class Renderer extends JComponent {
    public static final int FOCUS_SIZE = 9; //The grid size of the board shown, which is 9x9 tiles
    public static final int IMAGE_SIZE = 60;
    public static final int CANVAS_SIZE = 540; //Size in pixels, 9 x 60px images

    //private final Map<String, Image> images;
    private final Set<Star> stars;

    private AudioPlayer audioPlayer;

    private int tick = 0;
    private boolean playerFlipped = false;
    private Position playerPrevPos;

    private Game game;

    public enum DIRECTION {
        UP, DOWN, LEFT, RIGHT, NULL
    }

    /**
     * Creates a new renderer canvas
     */
    public Renderer(Game game){
        stars = new HashSet<>();
        audioPlayer = new AudioPlayer();
        this.game = game;
        playerPrevPos = game.getPlayer().getPos();
        setPreferredSize(new Dimension(CANVAS_SIZE, CANVAS_SIZE));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g; //Graphics 2D gives you more drawing options

        //Set background
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, getWidth(), getHeight());

        //Get all the necessary data from the Maze module (a Game object)
        AbstractTile[][] board = game.getBoard().getMap();
        Player player = game.getPlayer();
        int playerX = player.getPos().getX();
        int playerY = player.getPos().getY();

        DIRECTION orientation = getPlayerOrientation(playerX, playerY);

        //Add a star
        if (tick % 5 == 0) {
            stars.add(new Star(0, (int) (Math.random() * CANVAS_SIZE), (int) (Math.random() * 5 + 5), (int) (Math.random() * 5 + 5)));
        }

        drawStars(g2, orientation);
        drawFocusArea(playerX, playerY, board, g2);
        drawPlayer(player, g2);
        drawEnemies(playerX, playerY, game, g2);

        //Store player's previous position for the next frame
        playerPrevPos = player.getPos().getPositionCopy();
        tick++;
    }

    /**
     * Draws all the stars on the screen
     * @param g2 Paint graphic
     */
    private void drawStars(Graphics2D g2, DIRECTION direction){
        List<Star> toRemove = new ArrayList<>();
        for (Star star : stars){
            if (direction != DIRECTION.NULL){
                star.playerMoved(direction);
            }
            if (!star.updatePos()){
                toRemove.add(star);
            }
            star.draw(g2);
        }
        stars.removeAll(toRemove);
    }

    /**
     * Compared the players last position with it's current position
     * to figure out with direction it moved
     * @param playerX Player x position
     * @param playerY Player y position
     * @return The direction the player has moved
     */
    private DIRECTION getPlayerOrientation(int playerX, int playerY){
        DIRECTION direction = DIRECTION.NULL;
        if (playerX < playerPrevPos.getX()){
            playerFlipped = true;
            direction = DIRECTION.LEFT;
        }
        if (playerX > playerPrevPos.getX()){
            playerFlipped = false;
            direction = DIRECTION.RIGHT;
        }
        if (playerY < playerPrevPos.getY()){
            direction = DIRECTION.UP;
        }
        if (playerY > playerPrevPos.getY()){
            direction = DIRECTION.DOWN;
        }
        return direction;
    }

    /**
     * Draws all the tiles on the focus area
     * @param playerX Player x position
     * @param playerY Player y position
     * @param board The game board
     * @param g2 Paint graphic
     */
    private void drawFocusArea(int playerX, int playerY, AbstractTile[][] board, Graphics2D g2){
        for (int y = -4; y <= 4; y++) {
            for (int x = -4; x <= 4; x++) {
                //If in board bounds
                if (playerX + x >= 0 && playerY + y >= 0 && playerX + x < board.length && playerY + y < board[0].length){
                    g2.drawImage(board[playerX + x][playerY + y].getCurrentImage(),
                            (x+4) * IMAGE_SIZE, (y+4) * IMAGE_SIZE, this);
                }
            }
        }
    }

    /**
     * Draws the player in the centre of the focus area
     * @param player The player
     * @param g2 Graphic from paintComponent
     */
    private void drawPlayer(Player player, Graphics2D g2){
        if (playerFlipped) {
            g2.drawImage(player.getCurrentImage(), 4 * IMAGE_SIZE, 4 * IMAGE_SIZE, this);
        } else {
            g2.drawImage(player.getCurrentImage(), 4 * IMAGE_SIZE, 4 * IMAGE_SIZE, this);
        }
    }

    private void drawEnemies(int playerX, int playerY, Game game, Graphics2D g2){
        for (AbstractActor actor : game.getComputerPlayers()){
            Position pos = actor.getPos();
            int relX = Math.abs(pos.getX() - playerX);
            int relY = Math.abs(pos.getY() - playerY);
            g2.drawImage(actor.getCurrentImage(), (relX + 4) * IMAGE_SIZE, (relY + 4) * IMAGE_SIZE, this);
        }
    }

    /**
     * Returns a test board which is 9x9 and has every tile image that exists on it
     * @return the board (AbstractTile 2D array)
     */
    public static AbstractTile[][] testBoard(){
        AbstractTile[][] board = new AbstractTile[9][9];
        for (int y = 0; y < 9; y++){
            for (int x = 0; x < 9; x++){
                board[x][y] = new FreeTile();
            }
        }
        board[0][0] = new ExitLock(false);
        board[0][1] = new ExitLock(true);
        board[1][0] = new ExitPortal();
        board[2][0] = new InfoField("Hello");
        board[3][0] = new Key("Blue");
        board[3][1] = new Key("Green");
        board[3][2] = new Key("Red");
        board[3][3] = new Key("Yellow");
        board[4][0] = new LockedDoor(false, "Blue");
        board[4][1] = new LockedDoor(false, "Green");
        board[4][2] = new LockedDoor(false, "Red");
        board[4][3] = new LockedDoor(false, "Yellow");
        board[4][4] = new LockedDoor(true, "Blue");
        board[4][5] = new LockedDoor(true, "Green");
        board[4][6] = new LockedDoor(true, "Red");
        board[4][7] = new LockedDoor(true, "Yellow");
        board[5][0] = new Treasure();
        board[6][0] = new Wall();
        return board;
    }

    /**
     * Level 1 in code form, may not be final
     * @return the 15x15 board (AbstractTile 2D array)
     */
    public static AbstractTile[][] level1(){
        AbstractTile[][] board = new AbstractTile[15][15];
        for (int y = 0; y < 15; y++){
            for (int x = 0; x < 15; x++){
                if (x == 0 || y == 0 || x == 14 || y == 14 || x == 7 || y == 7){
                    board[x][y] = new Wall();
                } else {
                    board[x][y] = new FreeTile();
                }
            }
        }
        board[13][1] = new ExitPortal();
        board[12][1] = new ExitLock(false);
        board[13][2] = new ExitLock(true);
        board[12][2] = new Wall();
        board[6][6] = new Key("Blue");
        board[1][7] = new LockedDoor(false, "Blue");
        board[6][8] = new Key("Green");
        board[7][13] = new LockedDoor(true, "Green");
        board[8][8] = new Key("Red");
        board[13][7] = new LockedDoor(false, "Red");
        board[1][1] = new Treasure();
        board[1][13] = new Treasure();
        board[13][13] = new Treasure();
        board[8][1] = new Treasure();
        return board;
    }

    //This is just to test sound works
    public static void main(String[] args) {
        AudioPlayer audioPlayer = new AudioPlayer();
        audioPlayer.playSound("SwipeGood");
        JOptionPane.showMessageDialog(null, "Press for next sound");
        audioPlayer.playSound("SwipeGood");
        JOptionPane.showMessageDialog(null, "Press for next sound");
        audioPlayer.playSound("DoorOpen");
        JOptionPane.showMessageDialog(null, "This is just so the program doesn't end immediately");
    }
}
