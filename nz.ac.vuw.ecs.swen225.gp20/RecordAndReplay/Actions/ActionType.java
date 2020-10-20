package RecordAndReplay.Actions;

import RecordAndReplay.RecordAndReplay;

/**
 * Just to avoid spaghetti code.
 * This is the list of changes that can occur on the level.
 * A player could move, a creature could move, a map could change.
 *
 * An "actiontype" is an action that can spark multiple functions (ie: checks to see if creature attacks player)
 *
 */
public enum ActionType {
    PLAYERACTION("playerAction"), ENEMYACTION("enemyAction"), MAPACTION("mapAction");

    private String asString;
    ActionType(String asString) {
        this.asString = asString; //to make it easier to write n stuff
    }
    public String getString() { return asString; }
}
