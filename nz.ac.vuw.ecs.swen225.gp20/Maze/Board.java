package Maze;

import Maze.BoardObjects.Tiles.AbstractTile;
import org.junit.experimental.theories.internal.SpecificDataPointsSupplier;

public class Board {

    private AbstractTile[][] map;

    public Board(AbstractTile[][] tileMap) {
        //this.board = board;
        this.map = tileMap;
    }

    public AbstractTile[][] getUnmodMap(){
        return map;
    }

    public AbstractTile[][] getMap(){
        return map;
    }


    public Position findPosInBoard(AbstractTile tile){
        for (int i = 0; i < map.length; i++){
            for (int j = 0; j < map[i].length; j++){
                if(tile == map[i][j]){
                    return new Position(i, j);
                }
            }
        }
        return null;
    }

}