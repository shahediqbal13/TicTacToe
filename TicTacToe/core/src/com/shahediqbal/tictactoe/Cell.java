package com.shahediqbal.tictactoe;

import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Shahed on 12,January,2019
 */
public class Cell {
    
    public Rectangle square;

    public Cell(float x, float y, float width, float height) {
        square = new Rectangle(x, y, width, height);
    }
}
