package Application;

import java.awt.EventQueue;

/**
 * Runs an instance of the game.
 *
 * @author Iqbal
 */
public class Main {
    public static void main(String[] args) {
        EventQueue.invokeLater(StartMenu::new);
    }
}
