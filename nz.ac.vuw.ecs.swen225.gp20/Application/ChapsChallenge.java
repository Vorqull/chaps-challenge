package Application;

import Maze.Board;
import Maze.BoardObjects.Actors.Player;
import Maze.BoardObjects.Tiles.AbstractTile;
import Maze.Game;
import Maze.Position;
import RecordAndReplay.RecordAndReplay;
import Renderer.Renderer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.*;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * Window of the actual game, Chap's Challenge
 *
 * @author Iqbal
 */
public class ChapsChallenge extends JFrame {

    //info panel
    private final int INFO_WIDTH = 240;
    private final int INFO_HEIGHT = 540;

    private Game game;

    private RecordAndReplay recordAndReplayer;

    /**
     * Game instance
     */
    public ChapsChallenge(){
        initUI();
        game = new Game(new Board(Renderer.level1()), new Player(new Position(4, 4)), null); //FIXME: placeholder replace later
        recordAndReplayer = new RecordAndReplay();

        JPanel basePanel = new JPanel();
        basePanel.setBackground(Color.BLACK);

        basePanel.setLayout(new BoxLayout(basePanel, BoxLayout.X_AXIS));

        int verticalGap = 85;
        int horizontalGap = 65;
        basePanel.setBorder(new EmptyBorder(new Insets(verticalGap, horizontalGap, verticalGap, horizontalGap)));

        //PANELS
        // Gameplay panel
        JPanel gameplay = createGamePanel(new Renderer(this));
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                requestFocus();
                gameplay.requestFocus();
            }
        });
        basePanel.add(gameplay);
        basePanel.add(Box.createRigidArea(new Dimension(50, 0))); // Small gap between game and info panel

        // Info panel
        JPanel info = createInfoPanel();
        basePanel.add(info);

        add(basePanel);

        // More window properties
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
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
        //=====Game Menu=====//
        JMenu gameMenu = new JMenu("Game");

        //selections
        JMenuItem restartItem = new JMenuItem("Restart");
        //restartItem.addActionListener((e) -> System.exit(0)); //TODO: add functionality

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener((e) -> System.exit(0));

        //adding menu selections to the gameMenu
        gameMenu.add(restartItem);
        gameMenu.add(exitItem);

        //Add gameMenu to menuBar.
        menuBar.add(gameMenu);

        //=====Record And Replay=====//
        JMenu recordAndReplay = new JMenu("Record And Replay");

        //Selections
        JMenuItem RecordTrigger = new JMenuItem("Start Recording");
        RecordTrigger.addActionListener((e) -> recordTriggerHelper(RecordTrigger));

        //Add selections to RecordAndReplay Menu.
        recordAndReplay.add(RecordTrigger);

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
    public JPanel createGamePanel(Renderer renderer){
        JPanel gamePanel = new JPanel();
        gamePanel.setBackground(Color.DARK_GRAY);
        gamePanel.add(renderer);
        gamePanel.setFocusable(true);
        gamePanel.requestFocusInWindow();
        gamePanel.requestFocus();

        //KeyListeners
        gamePanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()){
                    case KeyEvent.VK_W:
                        System.out.println("Up");
                        movementRecordHelper(Game.DIRECTION.UP);
                        game.movePlayer(Game.DIRECTION.UP);
                        break;
                    case KeyEvent.VK_A:
                        System.out.println("Left");
                        movementRecordHelper(Game.DIRECTION.LEFT);
                        game.movePlayer(Game.DIRECTION.LEFT);
                        break;
                    case KeyEvent.VK_S:
                        System.out.println("Down");
                        movementRecordHelper(Game.DIRECTION.DOWN);
                        game.movePlayer(Game.DIRECTION.DOWN);
                        break;
                    case KeyEvent.VK_D:
                        System.out.println("Right");
                        movementRecordHelper(Game.DIRECTION.RIGHT);
                        game.movePlayer(Game.DIRECTION.RIGHT);
                        break;
                    default:
                        //if player isn't moving add a println here
//                        System.out.println("Key Pressed");
                        break;
                }
                recordAndReplayer.clearRecorderBuffer();
                renderer.revalidate();
                renderer.repaint();
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

        //level
        JLabel levelLabel = new JLabel("Level X: placeholder");
        levelLabel.setForeground(Color.RED);
        levelLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        //time remaining
        JLabel timeLabel = new JLabel("Time Remaining: ");
        timeLabel.setForeground(Color.RED);
        timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        //chips remaining
        JLabel chipsLabel = new JLabel("Chips Remaining: ");
        chipsLabel.setForeground(Color.RED);
        chipsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);


        //TODO: inventory view

        infoPanel.add(Box.createRigidArea(new Dimension(INFO_WIDTH, 150)));
        infoPanel.add(levelLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(INFO_WIDTH, 100)));
        infoPanel.add(timeLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(INFO_WIDTH, 100)));
        infoPanel.add(chipsLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(INFO_WIDTH, 150)));


        return infoPanel;
    }

    /**
     * Getter for game.
     * @return game
     */
    public Game getGame() {
        return game;
    }

    /**
     * Activated whenever a player moves in a direction.
     * Also helps check tiles they are about to move into incase of anything
     * being on said tile.
     */
    public void movementRecordHelper(Game.DIRECTION direction) {
        recordAndReplayer.capturePlayerMove(direction);
        Position newPos = new Position(game.getPlayer().getPos(), direction);
        recordAndReplayer.captureTileInteraction(game.getBoard().getMap()[newPos.getX()][newPos.getY()]);
    }

    /**
     *
     */
    public void recordTriggerHelper(JMenuItem menuItem) {
        if(recordAndReplayer.triggerRecordingSwitch()) {
            //if it's true right now. Save gameplay, and switch it to false.
            recordAndReplayer.saveGameplay();

            menuItem.setText("Start Recording");
        } else {
            //if it's false right now. Switch it to true. gameplay should start being recorded
            //also, change the menu text
            recordAndReplayer.setStartingPosition(game.getPlayer().getPos());
            menuItem.setText("Stop Recording");
        }
    }
}
