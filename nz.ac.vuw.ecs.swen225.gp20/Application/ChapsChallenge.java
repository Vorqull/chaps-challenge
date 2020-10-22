package Application;

import Maze.Board;
import Maze.BoardObjects.Actors.AbstractActor;
import Maze.BoardObjects.Actors.PatternEnemy;
import Maze.BoardObjects.Actors.StalkerEnemy;
import Maze.Game;
import Maze.Position;
import Persistence.Persistence;
import Persistence.Level;
import RecordAndReplay.RecordAndReplay;
import Renderer.Renderer;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

/**
 * Window of the actual game, Chap's Challenge
 *
 * @author Iqbal
 */
public class ChapsChallenge extends JFrame {

    //Panels
    private JPanel gameplayPanel;
    private JPanel infoPanel;
    public static final int INFO_WIDTH = 240;

    //Recording JMenuItems
    private JMenuItem recordItem;
    private JMenuItem replayItem;

    //Game
    private Game game;
    private Level currentLevel;
    private int levelCount = 1;
    private volatile boolean isPaused = false;
    private volatile Thread paintThread;

    //Informating stored for info panel
    private volatile Timer timer;
    private int timeRemaining;
    private InventoryView inventoryView;

    //Other modules
    private Renderer renderer;
    private RecordAndReplay recordAndReplayer;

    /**
     * Game instance
     */
    public ChapsChallenge(){
        initUI();

        // Initialize modules
        initModules();

        // Initialize hotkeys
        addHotKeys();

        // More window properties
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    public void initModules(){
        // Persistence, Maze and Renderer module
        loadLevel(levelCount);

        // Record & Replay module
        recordAndReplayer = new RecordAndReplay();
    }

    public void initPanels(){
        //GUI base panel
        JPanel basePanel = new JPanel();
        basePanel.setBackground(Color.BLACK);
        basePanel.setLayout(new BoxLayout(basePanel, BoxLayout.X_AXIS));

        int verticalGap = 85;
        int horizontalGap = 65;
        basePanel.setBorder(new EmptyBorder(new Insets(verticalGap, horizontalGap, verticalGap, horizontalGap)));

        // Gameplay panel
        gameplayPanel = createGamePanel();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                requestFocus();
                gameplayPanel.requestFocus();
            }
        });
        basePanel.add(gameplayPanel);
        basePanel.add(Box.createRigidArea(new Dimension(50, 0))); // Small gap between game and info panel

        // Info panel
        infoPanel = createInfoPanel();
        basePanel.add(infoPanel);

        add(basePanel);
    }

    /**
     * Initializes window properties
     */
    public void initUI(){
        setTitle("Chap's Challenge: Among Us Edition");
        createMenuBar();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }


    /**
     * Menu bar with options to change the game state
     */
    public void createMenuBar(){
        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");

        //selections
        JMenuItem level1 = new JMenuItem("Level 1");
        level1.addActionListener((e) -> loadLevel(1));

        JMenuItem level2 = new JMenuItem("Level 2");
        level2.addActionListener((e) -> loadLevel(2));

        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.addActionListener((e) -> Persistence.saveGame(timeRemaining, game.getPlayer(), currentLevel.getEnemies(), levelCount, currentLevel.getTileArray()));

        JMenuItem loadSaved = new JMenuItem("Load Saved Game");
        loadSaved.addActionListener((e) -> {
            String[] possibleValues = { "Level 1", "Level 2"};
            Object selectedValue = JOptionPane.showInputDialog(null,
                    "Choose a saved level to load (if it exists)", "Load Game",
                    JOptionPane.INFORMATION_MESSAGE, null,
                    possibleValues, possibleValues[0]);

            if (selectedValue.equals("Level 1")){
                loadLevel(Persistence.loadGame(1), 1);
            }
            else if (selectedValue.equals("Level 2")){
                loadLevel(Persistence.loadGame(2), 2);
            }
            else {
                //we're not supposed to be here
                System.out.println("Selected level not found: " + selectedValue.toString());
            }

        });

        JMenuItem pauseItem = new JMenuItem("Pause");
        pauseItem.addActionListener(e -> {
            if (!isPaused) pauseResume();
        });

        JMenuItem resumeItem = new JMenuItem("Resume");
        resumeItem.addActionListener(e -> {
            if (isPaused) pauseResume();
        });

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener((e) -> System.exit(0));

        //adding menu selections to the menu
        gameMenu.add(saveItem);
        gameMenu.add(loadSaved);
        gameMenu.add(level1);
        gameMenu.add(level2);
        gameMenu.add(pauseItem);
        gameMenu.add(resumeItem);
        gameMenu.add(exitItem);
        menuBar.add(gameMenu);

        //=====Record And Replay=====//
        JMenu recordAndReplay = new JMenu("Record And Replay");

        //SELECTIONS
        //1: Recording
        recordItem = new JMenuItem("Start Recording");
        recordItem.addActionListener((e) -> recordTrigger());
        //2: Replaying
        replayItem = new JMenuItem("Replay");
        replayItem.addActionListener((e) -> replayTrigger());

        //Add selections to RecordAndReplay Menu.
        recordAndReplay.add(recordItem);
        recordAndReplay.add(replayItem);

        //Add RecordAndReplay to MenuBar
        menuBar.add(recordAndReplay);

        //=====Setting the menu bar=====//
        setJMenuBar(menuBar);
    }


    // ===========================================
    // JPanels
    // ===========================================

    /**
     * Gameplay of the game is displayed here.
     * @return Gameplay panel
     */
    public JPanel createGamePanel(){
        JPanel gamePanel = new JPanel();
        gamePanel.setBackground(Color.DARK_GRAY);
        gamePanel.add(renderer);
        gamePanel.setFocusable(true);
        gamePanel.requestFocusInWindow();
        gamePanel.requestFocus();

        //Star background on own thread
        paintThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (!isPaused) {
                        try {
                            Thread.sleep(1000 / 30); //30FPS
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        recordAndReplayer.captureEnemyPreMoves(game.getComputerPlayers());
                        game.moveEnemies();
                        recordAndReplayer.captureEnemyPostMoves(game.getComputerPlayers());
                        repaint();
                    }
                    recordAndReplayer.clearRecorderBuffer(timeRemaining);
                }
            }
        });
        paintThread.start();

        //KeyListeners
        gamePanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!isPaused) {
                    if (!e.isControlDown()) {
                        //up
                        if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP) {
                            System.out.println("Up");
                            movementRecordHelper(Game.DIRECTION.UP);
                            game.movePlayer(Game.DIRECTION.UP);
                        }
                        //left
                        else if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT) {
                            System.out.println("Left");
                            movementRecordHelper(Game.DIRECTION.LEFT);
                            game.movePlayer(Game.DIRECTION.LEFT);
                        }
                        //down
                        else if (e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_DOWN) {
                            System.out.println("Down");
                            movementRecordHelper(Game.DIRECTION.DOWN);
                            game.movePlayer(Game.DIRECTION.DOWN);
                        }
                        //right
                        else if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT) {
                            System.out.println("Right");
                            movementRecordHelper(Game.DIRECTION.RIGHT);
                            game.movePlayer(Game.DIRECTION.RIGHT);
                        } else {
                            //dead code
                        }
                        nextLevel(); //check if the player is on the vent
                        //recordAndReplayer.storeRecorderBuffer();
                    }
                }
            }
        });

        return gamePanel;
    }

    /**
     * Game information such as timer, chips remaining and player inventory is displayed here.
     * @return Info panel
     */
    public JPanel createInfoPanel(){
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);

        int fontSize = 16;

        //Current level label
        JLabel levelLabel = new JLabel("LEVEL " + levelCount);
        levelLabel.setFont(new Font(levelLabel.getName(), Font.PLAIN, fontSize));
        levelLabel.setForeground(Color.RED);
        levelLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        //Timer thread
        JLabel timeLabel = new JLabel();
        JLabel chipsLabel = new JLabel();
        JLabel inventoryLabel = new JLabel("INVENTORY");
        inventoryLabel.setFont(new Font(timeLabel.getName(), Font.PLAIN, fontSize));
        inventoryLabel.setForeground(Color.RED);
        inventoryLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Time remaining
                timeLabel.setFont(new Font(timeLabel.getName(), Font.PLAIN, fontSize));
                timeLabel.setText("TIME REMAINING: \n" + timeRemaining);
                timeLabel.setForeground(Color.RED);
                timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

                //Chips Remaining
                chipsLabel.setFont(new Font(chipsLabel.getName(), Font.PLAIN, fontSize));
                chipsLabel.setText("CHIPS REMAINING: " + game.treasuresLeft());
                chipsLabel.setForeground(Color.RED);
                chipsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

                //Stopping the timer once it runs out of time
                if (timeRemaining == 0) {
                    timer.stop();
                    outOfTime();
                }
                if (!isPaused) {
                    timeRemaining--;
                }

            }
        });
        timer.start();

        //info panel
        infoPanel.add(Box.createRigidArea(new Dimension(INFO_WIDTH, 100)));
        infoPanel.add(levelLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(INFO_WIDTH, 66)));
        infoPanel.add(timeLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(INFO_WIDTH, 66)));
        infoPanel.add(chipsLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(INFO_WIDTH, 66)));
        infoPanel.add(inventoryLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(INFO_WIDTH, 10)));
        infoPanel.add(inventoryView);
        infoPanel.add(Box.createRigidArea(new Dimension(INFO_WIDTH, 55)));

        return infoPanel;
    }

    @Override
    public void repaint() {
        super.repaint();
        renderer.revalidate();
        renderer.repaint();
    }


    // ===========================================
    // Controlling Game Status
    // ===========================================

    /**
     * Loads a level from a specified integer
     * @param level Level number
     */
    public void loadLevel(int level){
        levelCount = level;
        isPaused = false; //make sure the game starts in an un-paused state
        if (paintThread != null && timer != null){
            paintThread.stop();
            timer.stop();
        }

        // Persistence and Levels module
        currentLevel =  Persistence.getLevel(level);
        timeRemaining = currentLevel.getTime();

        // Maze module
        game = new Game(new Board(currentLevel.getTileArray()), currentLevel.getPlayer(), currentLevel.getEnemies());

        // Initialize the player inventory
        inventoryView = new InventoryView(game.getPlayer()); //adding inventory view (Application)

        // Renderer module
        renderer = new Renderer(game);

        //Reset this JFrame and reinitialize panels
        this.getContentPane().removeAll();
        initPanels();
        addHotKeys();

        //reset focus in case this method was called from the menu bar
        gameplayPanel.setFocusable(true);
        gameplayPanel.requestFocusInWindow();
        gameplayPanel.requestFocus();
    }

    /**
     * Loads a level specified by a Level object
     * @param level Level object
     */
    public void loadLevel(Level level, int levelCount){
        this.levelCount = levelCount;
        isPaused = false; //make sure the game starts in an un-paused state
        if (paintThread != null && timer != null){
            paintThread.stop();
            timer.stop();
        }

        // Persistence and Levels module
        currentLevel =  level;
        timeRemaining = currentLevel.getTime();

        // Maze module
        game = new Game(new Board(currentLevel.getTileArray()), currentLevel.getPlayer(), currentLevel.getEnemies());

        // Initialize the player inventory
        inventoryView = new InventoryView(game.getPlayer()); //adding inventory view (Application)

        // Renderer module
        renderer = new Renderer(game);

        //Reset this JFrame and reinitialize panels
        this.getContentPane().removeAll();
        initPanels();
        addHotKeys();

        //reset focus in case this method was called from the menu bar
        gameplayPanel.setFocusable(true);
        gameplayPanel.requestFocusInWindow();
        gameplayPanel.requestFocus();
    }

    /**
     * Adds different keybindings that controls the state of the game
     */
    public void addHotKeys() {
        //CTRL + X: exit the game, the current game state will be lost, the next time the game is started, it will resume from the last unfinished level
        KeyStroke exitGame = KeyStroke.getKeyStroke('X', InputEvent.CTRL_DOWN_MASK);
        gameplayPanel.getInputMap().put(exitGame, "exit_game");
        gameplayPanel.getActionMap().put("exit_game", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("EXIT CALLED");
            }
        });

        //CTRL + S: exit the game, saves the game state, game will resume next time the application will be started
        KeyStroke saveGame = KeyStroke.getKeyStroke('S', InputEvent.CTRL_DOWN_MASK);
        gameplayPanel.getInputMap().put(saveGame, "save_game");
        gameplayPanel.getActionMap().put("save_game", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("SAVE CALLED");
            }
        });

        //CTRL + R: resume a saved game
        KeyStroke resumeSavedGame = KeyStroke.getKeyStroke('R', InputEvent.CTRL_DOWN_MASK);
        gameplayPanel.getInputMap().put(resumeSavedGame, "resume_saved_game");
        gameplayPanel.getActionMap().put("resume_saved_game", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("RESUME SAVED GAME");
            }
        });

        //CTRL + P: start a new game at the last unfinished level
        KeyStroke newGameLastLevel = KeyStroke.getKeyStroke('P', InputEvent.CTRL_DOWN_MASK);
        gameplayPanel.getInputMap().put(newGameLastLevel, "new_game_last_level");
        gameplayPanel.getActionMap().put("new_game_last_level", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("NEW GAME LAST LEVEL");
            }
        });

        //CTRL + 1: start a new game at level 1
        KeyStroke newLevel1 = KeyStroke.getKeyStroke('1', InputEvent.CTRL_DOWN_MASK);
        gameplayPanel.getInputMap().put(newLevel1, "new_game_level_1");
        gameplayPanel.getActionMap().put("new_game_level_1", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("NEW GAME LEVEL 1");
            }
        });

        //SPACEBAR: pause the game and display a “game is paused” dialog
        KeyStroke pauseGame = KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0);
        gameplayPanel.getInputMap().put(pauseGame, "pause");
        gameplayPanel.getActionMap().put("pause", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("PAUSE CALLED");
                if (!isPaused) {
                    pauseResume();
                }
            }
        });

        //ESC: pause the game and display a “game is paused” dialog
        KeyStroke resumeGame = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        gameplayPanel.getInputMap().put(resumeGame, "resume");
        gameplayPanel.getActionMap().put("resume", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("RESUME CALLED");
                pauseResume();
            }
        });
    }

    /**
     * Ends the game when the game clock runs out of time.
     */
    public void outOfTime(){
        JOptionPane.showMessageDialog(null, "You ran out of time!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }

    /**
     * Checks if the current level has been completed. If so, run the next level.
     */
    public void nextLevel(){
        if (game.isLevelCompleted()) {
            isPaused = true; //stop the game
            int options = JOptionPane.showConfirmDialog(null, "Continue to next level?", "Level 1 Completed!",
                    JOptionPane.YES_NO_OPTION);
            if (options == 0) {
                levelCount++;
                loadLevel(levelCount);
            } else {
                System.exit(0);
            }
        }
    }

    /**
     * Pauses and resumes the game
     */
    public void pauseResume(){
        isPaused = !isPaused;
        JOptionPane optionPane = new JOptionPane();
        optionPane.setMessageType(JOptionPane.INFORMATION_MESSAGE);
        optionPane.setMessage("Press ESC to unpause the game.");
        JDialog dialog = optionPane.createDialog(null, "Game Paused");
        if (isPaused){
            dialog.setVisible(true);
        }
    }

    // ===========================================
    // Getters
    // ===========================================

    /**
     * Getter for game.
     * @return game
     */
    public Game getGame() {
        return game;
    }

    /**
     * Getter for gameplay panel
     * @return gameplayPanel
     */
    public JPanel getGameplayPanel() {
        return gameplayPanel;
    }

    /**
     * Getter for info panel
     * @return infoPanel
     */
    public JPanel getInfoPanel() {
        return infoPanel;
    }


    // ===========================================
    // RecordAndReplay Helpers
    // ===========================================
    /**
     * Activated whenever a player moves in a direction.
     * Also helps check tiles they are about to move into in case of anything
     * being on said tile.
     */
    public void movementRecordHelper(Game.DIRECTION direction) {
        recordAndReplayer.capturePlayerMove(direction);
        Position newPos = new Position(game.getPlayer().getPos(), direction);
        recordAndReplayer.captureTileInteraction(game.getBoard().getMap()[newPos.getX()][newPos.getY()]);
    }

    /**
     * Helper for recording.
     * Has an activated and deactivated state, which the menu item can switch on and off.
     * During it's activated state, it records all movement into the recordbuffer.
     * During it's deactivated state, it saves everything on the recordbuffer and stops recording.
     */
    public void recordTrigger() {
        if(recordAndReplayer.getRecordingBoolean()) {
            //if it's true right now. Save gameplay, and switch it to false.
            recordAndReplayer.setRecordingBoolean(false);
            recordAndReplayer.saveGameplay(timeRemaining, game.getPlayer(), game.getComputerPlayers(), game.getBoard().getMap());

            recordItem.setText("Start Recording");
        } else {
            //if it's false right now. Switch it to true. gameplay should start being recorded
            recordAndReplayer.setStartingPosition(game.getPlayer().getPos());
            recordAndReplayer.setRecordingBoolean(true);
            recordAndReplayer.setStartedRecording(timeRemaining);
            recordAndReplayer.setEnemies(game.getComputerPlayers());
            recordAndReplayer.setLevelName(levelCount);

            recordItem.setText("Stop Recording");
        }
    }
    /**
     * Initiate replay mode
     */
    public void replayTrigger() {
        if(recordAndReplayer.getRecordingBoolean()) {
            JOptionPane.showMessageDialog(this, "ERROR: Cannot load replay while recording", "ERROR", JOptionPane.ERROR_MESSAGE);
        } else {
            if(!recordAndReplayer.loadConfirmation(this)) {
                return;
            }
            recordAndReplayer.selectSaveFile(this);

            recordAndReplayer.displayControlWindow();
        }
    }

    //====REPLAYING HELPERS====//
    public void movePlayer(Game.DIRECTION direction) {

    }

    /*public void moveEnemy(Position pos, Game.DIRECTION direction) {
        //game.getBoard().findPosInBoard()
        game.getComputerPlayers().contains();
    }*/
    public void moveEnemy(AbstractActor enemy, Game.DIRECTION direction) {
        /* Try this:
        enemy.move
         */

    }

    public void setTimeRemaining() {

    }

    //INCREASE FROM 30 FPS TO 60 FPS
    public void doubleSpeed() {

    }

}

