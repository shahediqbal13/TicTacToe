package com.shahediqbal.tictactoe;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.shahediqbal.tictactoe.ai.Algorithms;
import com.shahediqbal.tictactoe.ai.Board;

/**
 * Created by Shahed on 12,January,2019
 */
public class GameScreen implements Screen {

    private static final String TAG = "GameScreen";

    private final TicTacToeGame game;
    private OrthographicCamera camera;
    private Viewport viewport;

    private Texture gridImage;
    private Texture circleImage;
    private Texture crossImage;
    private Texture horizontalLineImage;
    private Texture verticalLineImage;
    private Texture leftDiagonalImage;
    private Texture rightDiagonalImage;
    private Texture playAgainNormalImage;
    private Texture playAgainPressedImage;
    private Texture crossWonImage;
    private Texture circleWonImage;
    private Texture drawImage;

    private Rectangle boardRect;
    private Rectangle playAgainRect;
    private Rectangle scoreRect;
    private Rectangle wonRect;

    Array<Cell> cells;

    private float circleWidth = 129;
    private float crossWidth = 112;

    private char boardStatus[] = {'?', '?', '?', '?', '?', '?', '?', '?', '?'};

    private Board board;

    GameScreen(final TicTacToeGame game) {
        this.game = game;

        gridImage = new Texture(Gdx.files.internal("grid.png"));
        gridImage.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        circleImage = new Texture(Gdx.files.internal("circle.png"));
        circleImage.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        crossImage = new Texture(Gdx.files.internal("cross.png"));
        crossImage.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        horizontalLineImage = new Texture(Gdx.files.internal("horizontal_line.png"));
        horizontalLineImage.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        verticalLineImage = new Texture(Gdx.files.internal("vertical_line.png"));
        verticalLineImage.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        leftDiagonalImage = new Texture(Gdx.files.internal("diagonal_left.png"));
        leftDiagonalImage.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        rightDiagonalImage = new Texture(Gdx.files.internal("diagonal_right.png"));
        rightDiagonalImage.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        playAgainNormalImage = new Texture(Gdx.files.internal("play_again_normal.png"));
        playAgainNormalImage.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        playAgainPressedImage = new Texture(Gdx.files.internal("play_again_pressed.png"));
        playAgainPressedImage.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        crossImage = new Texture(Gdx.files.internal("cross.png"));
        crossImage.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        circleWonImage = new Texture(Gdx.files.internal("circle_won.png"));
        circleWonImage.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        crossWonImage = new Texture(Gdx.files.internal("cross_won.png"));
        crossWonImage.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        drawImage = new Texture(Gdx.files.internal("match_draw.png"));
        drawImage.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        boardRect = new Rectangle(75, 350, 570, 570);
        playAgainRect = new Rectangle(98, 140, 539, 99);
        scoreRect = new Rectangle(60, 1125, 609, 100);
        wonRect = new Rectangle(105, 1050, 490, 91);

        float width = (boardRect.width - 30) / 3f;
        float height = (boardRect.height - 30) / 3f;

        Gdx.app.log(TAG, "Cell width: " + width + " Height: " + height);
        cells = new Array<Cell>();
        int offsetX = 0;
        int offsetY = 0;

        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                cells.add(new Cell(x * width + boardRect.x + offsetX, y * height + boardRect.y + offsetY,
                        width, height));
                offsetX += 15;
            }
            offsetX = 0;
            offsetY += 15;
        }

        board = new Board();
        camera = new OrthographicCamera();
        viewport = new ExtendViewport(720, 1280, camera);
        viewport.apply();
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(255, 255, 255, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(gridImage, boardRect.x, boardRect.y);
        game.batch.end();

        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            if (playAgainRect.contains(touchPos.x, touchPos.y)) {
                resetBoard();
                game.batch.begin();
                game.batch.draw(playAgainPressedImage, playAgainRect.x, playAgainRect.y);
                game.batch.end();
            }

            int index = 0;
            for (Cell cell : cells) {
                if (cell.square.contains(touchPos.x, touchPos.y)) {
                    playMove(index);
                }
                index++;
            }
        }

        game.batch.begin();
        paintBoard();
        prepareBoardStatus();
        paintWinnerLine();
        paintWinner();
        game.batch.end();
    }

    private void resetBoard() {
        for (int i = 0; i < boardStatus.length; i++) {
            boardStatus[i] = '?';
        }
        board.reset();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        gridImage.dispose();
        circleImage.dispose();
        crossImage.dispose();
        horizontalLineImage.dispose();
        verticalLineImage.dispose();
        Gdx.app.log(TAG, "Dispose called.");
    }

    private void playMove(int move) {
        if (!board.isGameOver() && move != -1) {
            boolean validMove = board.move(move);
            if (validMove && !board.isGameOver()) {
                Algorithms.alphaBetaAdvanced(board);
            }
        }
    }

    private void paintBoard() {
        Board.State[][] boardArray = board.toArray();

        int index = 0;
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {

                Cell cell = cells.get(index);
                if (boardArray[y][x] == Board.State.X) {
                    float offset = (cell.square.width - crossWidth) / 2f;
                    game.batch.draw(crossImage, cell.square.x + offset, cell.square.y + offset);
                } else if (boardArray[y][x] == Board.State.O) {
                    float offset = (cell.square.width - circleWidth) / 2f;
                    game.batch.draw(circleImage, cell.square.x + offset, cell.square.y + offset);
                }
                index++;
            }
        }
    }

    private void paintWinnerLine() {
        switch (GridUtil.checkWinner(boardStatus)) {
            case NO_MATCH:
                break;
            case ROW_0:
                Cell cell = cells.get(0);
                float offsetX = 5;
                float offsetY = cell.square.width / 2f - 6;
                game.batch.draw(horizontalLineImage, cell.square.x + offsetX, cell.square.y + offsetY);
                break;
            case ROW_1:
                cell = cells.get(3);
                offsetX = 5;
                offsetY = cell.square.width / 2f - 6;
                game.batch.draw(horizontalLineImage, cell.square.x + offsetX, cell.square.y + offsetY);
                break;
            case ROW_2:
                cell = cells.get(6);
                offsetX = 5;
                offsetY = cell.square.width / 2f - 6;
                game.batch.draw(horizontalLineImage, cell.square.x + offsetX, cell.square.y + offsetY);
                break;
            case COL_0:
                cell = cells.get(0);
                offsetY = 5;
                offsetX = cell.square.width / 2f - 6;
                game.batch.draw(verticalLineImage, cell.square.x + offsetX, cell.square.y + offsetY);
                break;
            case COL_1:
                cell = cells.get(1);
                offsetY = 5;
                offsetX = cell.square.width / 2f - 6;
                game.batch.draw(verticalLineImage, cell.square.x + offsetX, cell.square.y + offsetY);
                break;
            case COL_2:
                cell = cells.get(2);
                offsetY = 5;
                offsetX = cell.square.width / 2f - 6;
                game.batch.draw(verticalLineImage, cell.square.x + offsetX, cell.square.y + offsetY);
                break;
            case LEFT_DIAGONAL:
                game.batch.draw(leftDiagonalImage, boardRect.x, boardRect.y);
                break;
            case RIGHT_DIAGONAL:
                game.batch.draw(rightDiagonalImage, boardRect.x, boardRect.y);
                break;
        }
    }

    private void paintWinner() {
        if (board.isGameOver()) {
            switch (board.getWinner()) {

                case Blank:
                    game.batch.draw(drawImage, wonRect.x, wonRect.y);
                    break;
                case X:
                    game.batch.draw(crossWonImage, wonRect.x, wonRect.y);
                    break;
                case O:
                    game.batch.draw(circleWonImage, wonRect.x, wonRect.y);
                    break;
            }

            game.batch.draw(playAgainNormalImage, playAgainRect.x, playAgainRect.y);
        }
    }

    private void prepareBoardStatus() {
        int index = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                switch (board.board[i][j]) {

                    case Blank:
                        boardStatus[index] = '?';
                        break;
                    case X:
                        boardStatus[index] = 'X';
                        break;
                    case O:
                        boardStatus[index] = 'O';
                        break;
                }
                index++;
            }
        }
    }
}
