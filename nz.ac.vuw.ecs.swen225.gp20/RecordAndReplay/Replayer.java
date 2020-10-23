package RecordAndReplay;

import Application.ChapsChallenge;
import Maze.BoardObjects.Actors.AbstractActor;
import Maze.Game;
import Maze.Position;
import Persistence.Persistence;
import RecordAndReplay.Actions.Action;
import RecordAndReplay.Actions.EnemyMove;
import RecordAndReplay.Actions.PlayerMove;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is JUST like Reader, Recorder, and Writer. A list of simple helper methods for RecordAndReplay.
 * The Replayer's job is mostly to detect when the player moves forwards "or backwards" and replicate
 * the moves accordingly.
 *
 * This class handles all replaying methods of the game. It heavily relies on ChapsChallenge in the application module
 * to display changes that occur in the replay. Therefore, ChapsChallenge contains many helper methods
 * just for this component to call.
 *
 */
public class Replayer {
    private ChapsChallenge application;

    private int level;

    private JButton prev;
    private JButton next;

    private int loadStateLocation;

    private boolean pause = true;
    private boolean doubleSpeed = false;
    private int location = 0; //The location in the playback

    private ArrayList<Recorder.Change> prepedChanges = new ArrayList<Recorder.Change>();

    private ArrayList<Position> enemyStartPos = new ArrayList<>();
    private ArrayList<AbstractActor> enemies = new ArrayList<>();

    private boolean teleport = true;

    /**
     * Used by Record and Replay.
     * The complicated json file reading stuff should be done in Reader.
     */
    public Replayer(ChapsChallenge application) {
        this.application = application;
    }

    /**
     * Spawns the separate window for controls
     */
    public void controlsWindow() {
        JFrame controlWindow = new JFrame("Replay Controls");

        JPanel mainPanel = new JPanel();

        Icon prevIcon = new ImageIcon(System.getProperty("user.dir") + "/Resources/replayButtons/prev.png");
        Icon playIcon = new ImageIcon(System.getProperty("user.dir") + "/Resources/replayButtons/play.png");
        Icon pauseIcon = new ImageIcon(System.getProperty("user.dir") + "/Resources/replayButtons/pause.png");
        Icon nextIcon = new ImageIcon(System.getProperty("user.dir") + "/Resources/replayButtons/next.png");
        Icon doubleSpeedIcon = new ImageIcon(System.getProperty("user.dir") + "/Resources/replayButtons/doubleSpeed.png");
        Icon doubleSpeedActiveIcon = new ImageIcon(System.getProperty("user.dir") + "/Resources/replayButtons/doubleSpeedActive.png");

        prev = new JButton(prevIcon);
        JButton play = new JButton(playIcon);
        next = new JButton(nextIcon);
        JButton doubleSpeed = new JButton(doubleSpeedIcon);

        prev.addActionListener(e -> {
            prevButton();
        });
        play.addActionListener(e -> {
            pausePlayButton(play, playIcon, pauseIcon);
        });
        next.addActionListener(e -> {
            nextButton();
        });
        doubleSpeed.addActionListener(e -> {
            doubleSpeedButton(doubleSpeed, doubleSpeedIcon, doubleSpeedActiveIcon);
        });

        prev.setPreferredSize(new Dimension(50, 50));
        play.setPreferredSize(new Dimension(50, 50));
        next.setPreferredSize(new Dimension(50, 50));
        doubleSpeed.setPreferredSize(new Dimension(50, 50));

        mainPanel.add(prev);
        mainPanel.add(play);
        mainPanel.add(next);
        mainPanel.add(doubleSpeed);

        controlWindow.add(mainPanel);

        controlWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        controlWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                int selection = JOptionPane.showConfirmDialog(controlWindow, "Are you sure you wanna quit replay mode?", "Confirmation", JOptionPane.YES_NO_CANCEL_OPTION);
                if(selection == 0) {
                    application.loadLevel(application.getCurrentLevelCount());
                    application.setReplayMoveActive(false);
                    controlWindow.dispose();
                }
            }
        });
        controlWindow.setLocation(500, 300);
        controlWindow.pack();

        controlWindow.setAlwaysOnTop(true);
        controlWindow.setResizable(false);
        controlWindow.setFocusable(false);

        controlWindow.setVisible(true);
    }

    //Button functions
    public void prevButton() {
        teleport = true;
        System.out.println("location:" + location + "of" + prepedChanges.size());

        /*if(location == prepedChanges.size()) {
            System.out.println("I break down too often");
            location--;
        }*/

        if(location >= 0) {
            location--;
            int timeStamp = prepedChanges.get(location).timestamp;
            ArrayList<Action> actions = prepedChanges.get(location).actions;
            if(actions != null) {
                for (int i = 0; i < actions.size(); i++) {
                    //Do stuff in here using Chaps Challenge's helper methods
                    Action a = actions.get(i);
                    if(a instanceof PlayerMove) {
                        if(((PlayerMove) a).getDirection() == Game.DIRECTION.UP){
                                application.movePlayer(Game.DIRECTION.DOWN);
                        }
                        else if(((PlayerMove) a).getDirection() == Game.DIRECTION.DOWN) {
                                application.movePlayer(Game.DIRECTION.UP);
                        }
                        else if(((PlayerMove) a).getDirection() == Game.DIRECTION.LEFT) {
                                application.movePlayer(Game.DIRECTION.RIGHT);
                        }
                        else if(((PlayerMove) a).getDirection() == Game.DIRECTION.RIGHT) {
                                application.movePlayer(Game.DIRECTION.LEFT);
                        }
                        System.out.println("applying backward move changes: " + ((PlayerMove) a).getDirection());

                    } else if (a instanceof EnemyMove) {
                        int x = ((EnemyMove) a).getX();
                        int y = ((EnemyMove) a).getY();
                        AbstractActor enemy;

                        if(((EnemyMove) a).getDirection() == Game.DIRECTION.UP) {
                            y--;
                            enemy = application.findEnemyAtPos(new Position(x, y));
                            application.moveEnemy(enemy, Game.DIRECTION.DOWN);
                        }
                        else if(((EnemyMove) a).getDirection() == Game.DIRECTION.DOWN) {
                            y++;
                            enemy = application.findEnemyAtPos(new Position(x, y));
                            application.moveEnemy(enemy, Game.DIRECTION.UP);
                        }
                        else if(((EnemyMove) a).getDirection() == Game.DIRECTION.LEFT) {
                            x--;
                            enemy = application.findEnemyAtPos(new Position(x, y));
                            application.moveEnemy(enemy, Game.DIRECTION.RIGHT);
                        }
                        else if(((EnemyMove) a).getDirection() == Game.DIRECTION.RIGHT) {
                            x++;
                            enemy = application.findEnemyAtPos(new Position(x, y));
                            application.moveEnemy(enemy, Game.DIRECTION.LEFT);
                        }
                    }

                }
            }
            //Change the time remaining
            application.setTimeRemaining(timeStamp);
        } else {
            System.out.println("min");
        }
    }

    public void pausePlayButton(JButton button, Icon playIcon, Icon pauseIcon) {
        pause = !pause;
        if(pause) {
            //JUST paused
            prev.setEnabled(true);
            next.setEnabled(true);
            button.setIcon(playIcon);
            System.out.println("paused");
        } else {
            //JUST unpaused
            prev.setEnabled(false);
            next.setEnabled(false);
            button.setIcon(pauseIcon);
            System.out.println("play");
        }
    }

    public void nextButton() {
        System.out.println("Location: " + location + "of" + prepedChanges.size());

        if(location < 0) {
            location = 0;
        }

        if(location < prepedChanges.size()) {
            int timeStamp = prepedChanges.get(location).timestamp;
            ArrayList<Action> actions = prepedChanges.get(location).actions;
            if(actions != null) {
                for (int i = 0; i < actions.size(); i++) {
                    //Do stuff in here using Chaps Challenge's helper methods
                    Action a = actions.get(i);
                    if(a instanceof PlayerMove) {
                        application.movePlayer(((PlayerMove) a).getDirection());
                    } else if (a instanceof EnemyMove) {
                        int x = ((EnemyMove) a).getX();
                        int y = ((EnemyMove) a).getY();
                        Position pos = new Position(x, y);
                        AbstractActor enemy = application.findEnemyAtPos(pos);
                        application.moveEnemy(enemy,((EnemyMove) a).getDirection());
                        }
                    }
                }
            if(teleport) teleport = false;
            location++;
            //Change the time remaining
            application.setTimeRemaining(timeStamp);
        } else {
            System.out.println("max");
        }
    }

    public void doubleSpeedButton(JButton button, Icon dSpeedIcon, Icon dSpeedActiveIcon) {
        doubleSpeed = !doubleSpeed;
        if(doubleSpeed) {
            button.setIcon(dSpeedActiveIcon);
        } else {
            button.setIcon(dSpeedIcon);
        }
    }

    //apply an action every time a tick happens
    public void tick() {
        if(location < prepedChanges.size()-1) {
            nextButton();
        } else {
            pause = true;
            System.out.println("END OF TAPE");
        }
    }

    public void doubleTickSpeed(boolean t) {
        doubleSpeed = t;
    }

    public void loadToStart() {
        location = 0;
        application.loadLevel(Persistence.loadGame(loadStateLocation), level);
        application.teleportEnemies(enemyStartPos);
    }

    /** SETTERS **/
    public void setRecordedChanges(ArrayList<Recorder.Change> recordedChanges) {
        this.prepedChanges = recordedChanges;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setLoadState(int loadStateLocation) {
        this.loadStateLocation = loadStateLocation;
    }

    public void setEnemyStartPos(ArrayList<Position> enemyStartPos) {
        this.enemyStartPos = enemyStartPos;
    }

    /** GETTERS **/
    public boolean isPaused() {
        return pause;
    }
}
