package Maze;

import Maze.BoardObjects.Tiles.AbstractTile;

/**
 * The Board class containing the map array of tiles.
 * @author michael tang 300490290
 */
public class Board {

    private AbstractTile[][] map;

    /**
     * The board constructor
     * @param tileMap The array map of a level.
     */
    public Board(AbstractTile[][] tileMap) {
        this.map = tileMap;
    }

    /**
     * Gets the array tile map of the board.
     * @return Returns the array tile map of the board.
     */
    public AbstractTile[][] getMap(){
        return map;
    }


    /**
     * Finds the position of a tile in the board.
     * @param tile The tile to be found, MUST BE A POINTER TO THE ACTUAL TILE,
     *             NOT A COPY OF THE TILE.
     * @return Returns a position containing the x,y position of the tile in the array.
     */
    public Position findPosInBoard(AbstractTile tile){
        for (int i = 0; i < map.length; i++){
            for (int j = 0; j < map[i].length; j++){
                if(tile == map[i][j]){
                    return new Position(i, j);
                }
            }
        }
        return null; //Should never reach here
    }

}