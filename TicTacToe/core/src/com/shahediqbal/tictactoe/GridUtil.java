package com.shahediqbal.tictactoe;

/**
 * Created by Shahed on 12,January,2019
 */
public class GridUtil {

    public static Grid checkWinner(char grid[]) {

        char first = grid[0];
        if (first != '?' && grid[1] == first && grid[2] == first) {
            return Grid.ROW_0;
        }

        first = grid[3];
        if (first != '?' && grid[4] == first && grid[5] == first) {
            return Grid.ROW_1;
        }

        first = grid[6];
        if (first != '?' && grid[7] == first && grid[8] == first) {
            return Grid.ROW_2;
        }

        first = grid[0];
        if (first != '?' && grid[3] == first && grid[6] == first) {
            return Grid.COL_0;
        }

        first = grid[1];
        if (first != '?' && grid[4] == first && grid[7] == first) {
            return Grid.COL_1;
        }

        first = grid[2];
        if (first != '?' && grid[5] == first && grid[8] == first) {
            return Grid.COL_2;
        }

        first = grid[0];
        if (first != '?' && grid[4] == first && grid[8] == first) {
            return Grid.LEFT_DIAGONAL;
        }

        first = grid[2];
        if (first != '?' && grid[4] == first && grid[6] == first) {
            return Grid.RIGHT_DIAGONAL;
        }

        return Grid.NO_MATCH;
    }
}
