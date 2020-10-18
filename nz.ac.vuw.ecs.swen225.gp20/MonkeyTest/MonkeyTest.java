package MonkeyTest;

import javax.swing.JOptionPane;
import Renderer.*;
import org.junit.*;


import Maze.Board;

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

@BeforeClass
	/**
	 * @param args .
	 */
	public static void initialize() {


		//runDialog();
		//runLevelChooser();
	board = new Board(Renderer.level1());
	i = 100000;
		


		System.out.println(i);
	}

	/** private static void runLevelChooser() {
		Object[] levels = {"Level 1", "Level 2"};
		l = (String)JOptionPane.showInputDialog(null,"Choose a level to test","Level Chooser",JOptionPane.PLAIN_MESSAGE,null,levels,"Level 1");
		if(l.equals("Level 1")) {board = new Board(Renderer.level1());}
		else {} 

	} **/


	/**private static void runDialog() {

		Object[] options = {"Yes","No"};
		int n = JOptionPane.showOptionDialog(null,
				"Would you like to specify the number of commands?\n" +	"The default is 100,000\n",	"MonkeyTest", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,options[0]);
		if(n == 0){
			try {
				i = Integer.parseInt(JOptionPane.showInputDialog(null, "How many random commands to send?\n"));
			} catch(Exception e) { JOptionPane.showMessageDialog(null, "You must input an integer");}
		}
		else { i = 100000;} **/

	
	@Test
	public void testSuite() { test = new TestSuite(board);}
	
	@Test
	
	public void runTests() {while(k < i) {test.randomMovement(); k++;}}
	

}

