package MonkeyTest;

import javax.swing.JOptionPane;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;

import Maze.Board;
import Persistence.Level;
import Persistence.Persistence;

/**
 * Loads a level and plays it using randomly generated input
 * 
 * @author Ben
 *
 */


public class MonkeyTest {
	static int i;
	static int k = 0;
	static String l;
	static Board board;
	static TestSuite test;
	static Level level;

@BeforeEach
  void initialize() {
	level = Persistence.getLevel(1);
	board = new Board(level.getTileArray());
	i = 500000;
	 test = new TestSuite(board);
	}

	

	
	@Test
	
	public void runTimeTest() { //Plays the game for 30 seconds, regardless of how many commands are sent
		long t = System.currentTimeMillis();
		long end = t + 30000;
		int count = 0;
		while(System.currentTimeMillis() < end) {test.randomMovement(); count++;}
		System.out.println("DEBUG: Time Test performed " + count + " commands");
		}
	
	@Test
	public void runNumberTest() {while( k < i) {test.randomMovement(); k++;}} //Runs tests for k number of commands, regardless of time taken
	
	@RepeatedTest(15)
	public void rapidTest() { // Similar to time test, designed to run a new game every 10 seconds to trial many different possible scenarios
		long t = System.currentTimeMillis();
		long end = t + 10000;
		int count = 0;
		while(System.currentTimeMillis() < end) {test.randomMovement(); count++;}
	}
	
}

