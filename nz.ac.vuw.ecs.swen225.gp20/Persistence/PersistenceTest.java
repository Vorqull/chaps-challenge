package Persistence;


import java.util.Scanner;

/**
 * Utility class that can be used to turn persistence/level.csv into a level JSON file.
 * 
 * @author Lukas Stanley
 *
 */
public class PersistenceTest {
  /**
   * Run the test and create the level.
   * 
   * @param args unused, base param for main method.
   */
  public static void main(String[] args) {
    CsvReader csvRead = new CsvReader();

    Scanner sc = new Scanner(System.in);
    System.out.println("Enter the level number you wish to work with (1-9)");
    String levelNumberString = sc.nextLine();

    int levelNumber = stringToInt(levelNumberString);
    if (levelNumber > 10 || levelNumber <= 0) {
      System.out.println("Proper number please.");
      sc.close();
      return;
    }
    System.out.println(
        "Enter SKIP if you don't want to convert level.csv into levels/level" 
            + levelNumber 
            + ".json"
    );
    String skipString = sc.nextLine();
    sc.close();
    String levelString = "levels/level" + levelNumber + ".JSON";

    if (skipString.equals("SKIP") == false) {
      LevelJsonMaker.makeJson(
          csvRead.readCsv(
              "nz.ac.vuw.ecs.swen225.gp20/Persistence/level.csv"
          ), 
          levelString
      );
    }

    Level testLevel = Persistence.getLevel(levelNumber);
    
    //Generate test save file, can be deleted.
    Persistence.saveGame(91,                    //Seconds remaining
        testLevel.getPlayer(),
        testLevel.getEnemies(),
        levelNumber,
        testLevel.getTileArray());
    Level newLevel = Persistence.loadGame(levelNumber);
    if (newLevel.getEnemies().size() > 0) {
      System.out.println("Enemy count is > 0 " + newLevel.getEnemies().get(0).getPos());
    }
  }

  private static int stringToInt(String intString) {
    int charInt = 0;
    for (int i = 0; i < intString.length(); i++) {
      charInt = charInt * 10;
      charInt += intString.charAt(i) - '0';
    }
    return charInt;
  }
}
