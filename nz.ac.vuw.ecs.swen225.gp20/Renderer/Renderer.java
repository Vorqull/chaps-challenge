package Renderer;

import Maze.BoardObjects.Actors.AbstractActor;
import Maze.BoardObjects.Actors.Player;
import Maze.BoardObjects.Tiles.*;
import Maze.Game;
import Maze.Position;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * This is responsible for displaying the Maze on the screen
 * including all it's tiles, animations and sound effects.
 * @author Chris (ID: 300498017)
 */
public class Renderer extends JComponent {
    public static final int IMAGE_SIZE = 60;
    public static final int CANVAS_SIZE = 540; //Size in pixels, 9 x 60px images

    private final Set<Star> stars;

    private final Game game;
    private final AudioPlayer audioPlayer;

    private int tick = 0;
    private Position playerPrevPos;

    public enum DIRECTION {
        UP, DOWN, LEFT, RIGHT, NULL
    }

    /**
     * Creates a new renderer canvas.
     * @param game The game (Maze module)
     */
    public Renderer(Game game){
        stars = new HashSet<>();
        audioPlayer = new AudioPlayer();
        this.game = game;
        playerPrevPos = game.getPlayer().getPos();
        setPreferredSize(new Dimension(CANVAS_SIZE, CANVAS_SIZE));

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("Resources/fonts/VCR_OSD_MONO_1.001.ttf")));
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
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
            Random random = new Random(); //Spotbugs told me this was more efficient that Math.random() then converting to int
            stars.add(new Star(0, random.nextInt(CANVAS_SIZE), random.nextInt(5) + 5, random.nextInt(5) + 5));
        }

        //Play audio
        if (orientation != DIRECTION.NULL){
            audioPlayer.playSound("Step" + (int)(Math.random()*2 + 1));
        }
        audioPlayer.playTileSound(board[playerX][playerY], game);

        //Draw stuff
        drawStars(g2, orientation);
        drawFocusArea(playerX, playerY, board, g2);
        g2.drawImage(player.getCurrentImage(), 4 * IMAGE_SIZE, 4 * IMAGE_SIZE, this); //Draw player
        drawEnemies(playerX, playerY, game, g2);
        drawInfoText(playerX, playerY, board, g2);

        //Store player's previous position for the next frame
        playerPrevPos = player.getPos().getPositionCopy();
        tick++;
    }

    /**
     * Draws all the stars on the screen.
     * @param g2 Paint graphic
     * @param direction Direction that the player has moved this frame
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
     * to figure out with direction it moved.
     * @param playerX Player x position
     * @param playerY Player y position
     * @return The direction the player has moved
     */
    private DIRECTION getPlayerOrientation(int playerX, int playerY){
        DIRECTION direction = DIRECTION.NULL;
        if (playerX < playerPrevPos.getX()){
            direction = DIRECTION.LEFT;
        }
        if (playerX > playerPrevPos.getX()){
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
     * Draws all the tiles on the focus area.
     * @param playerX Player x position
     * @param playerY Player y position
     * @param board The game board
     * @param g2 Paint graphic
     */
    private void drawFocusArea(int playerX, int playerY, AbstractTile[][] board, Graphics2D g2){
        for (int y = -4; y <= 4; y++) {
            for (int x = -4; x <= 4; x++) {
                //If in board bounds
                if (playerX + x >= 0 && playerY + y >= 0 && playerX + x < board.length && playerY + y < board[0].length
                    && board[playerX + x][playerY + y] != null){
                    g2.drawImage(board[playerX + x][playerY + y].getCurrentImage(),
                            (x+4) * IMAGE_SIZE, (y+4) * IMAGE_SIZE, this);
                }
            }
        }
    }

    /**
     * Draws all the enemies on the focus area.
     * @param playerX Player x position
     * @param playerY Player y position
     * @param game The game
     * @param g2 Paint graphic
     */
    private void drawEnemies(int playerX, int playerY, Game game, Graphics2D g2){
        if (game.getComputerPlayers() == null) return;
        for (AbstractActor actor : game.getComputerPlayers()){
            Position pos = actor.getPos();
            //Calculates the position of the enemy relative to the player
            int relX = pos.getX() - playerX;
            int relY = pos.getY() - playerY;
            g2.drawImage(actor.getCurrentImage(), (relX + 4) * IMAGE_SIZE, (relY + 4) * IMAGE_SIZE, this);
        }
    }

    /**
     * Draws the info text on an info field if the player is on one.
     * @param playerX Player x position
     * @param playerY Player y position
     * @param board The game board
     * @param g2 Paint graphic
     */
    private void drawInfoText(int playerX, int playerY, AbstractTile[][] board, Graphics2D g2){
        if (board[playerX][playerY] instanceof InfoField){
            InfoField infoField = (InfoField) board[playerX][playerY];
            g2.setFont(new Font("VCR OSD Mono", Font.BOLD, 45));
            g2.setColor(Color.WHITE);
            drawWrappedText(infoField.getInfoText(), g2, 50, 150, 500);
        }
    }

    /**
     * Draws wrapped text within a certain box width.
     * @param text Text to be displayed
     * @param g2 Paint graphic
     * @param startX Left pos of the text box
     * @param startY Y pos of the base of the first line
     * @param boxWidth Width of the box to wrap the text in
     */
    private void drawWrappedText(String text, Graphics2D g2, int startX, int startY, int boxWidth){
        FontMetrics metrics = g2.getFontMetrics();
        int textHeight = metrics.getHeight();
        int y = startY;
        Scanner scan = new Scanner(text);
        String line = "";

        while (scan.hasNext()){
            String word = scan.next() + " ";

            if (metrics.stringWidth(line + word) < boxWidth){
                line += word;
            } else {
                g2.drawString(line, startX, y);
                y += textHeight;
                line = "";
                line += word;
            }
        }
        g2.drawString(line, startX, y);
    }

    /**
     * Returns a test board which is 9x9 and has every tile image that exists on it.
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
     * Level 1 in code form, may not be final.
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
        board[0][0] = null;
        board[13][1] = new ExitPortal();
        board[12][1] = new ExitLock(true);
        board[13][2] = new ExitLock(false);
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
        board[5][4] = new InfoField("COLLECT CHIPS TO GET PAST THE CHIP SOCKET. USE KEYS TO OPEN DOORS");
        return board;
    }
}
