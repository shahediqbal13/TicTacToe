package com.shahediqbal.tictactoe;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class TicTacToeGame extends Game {

    ShapeRenderer shapeRenderer;

    SpriteBatch batch;
    BitmapFont font;

    public void create() {

        shapeRenderer = new ShapeRenderer();

        batch = new SpriteBatch();

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("arial.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 65;
        parameter.color = Color.WHITE;
        font = generator.generateFont(parameter);
        generator.dispose();

        this.setScreen(new GameScreen(this));
    }

    @Override
    public void render() {
        super.render(); // important!
    }

    @Override
    public void dispose() {

        shapeRenderer.dispose();

        batch.dispose();
        font.dispose();
    }
}
