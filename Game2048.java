package com.codegym.games.game2048;

import com.codegym.engine.cell.*;
import java.util.Arrays;

public class Game2048 extends Game {
    private static final int SIDE = 4;
    private int[][] gameField = new int[SIDE][SIDE];
    private boolean isGameStopped = false;
    private int score = 0;

    private void createGame() { // set all grid to value zero
        gameField = new int[SIDE][SIDE];
        createNewNumber();
        createNewNumber();
    }

    private void drawScene() { // color all tiles same
        for (int x = 0; x < SIDE; x++) {
            for (int y = 0; y < SIDE; y++) {
                setCellColoredNumber(y, x, gameField[x][y]);
            }
        }
    }

    private void createNewNumber() { // start the game with 2 random cells with 2(probability -90%) or 4 (10%)
        int x;
        int y;
        int random = getRandomNumber(10);

        if (getMaxTileValue() == 2048) // call win() method if tile is 2048
            win();

        do {
            x = getRandomNumber(SIDE);
            y = getRandomNumber(SIDE);
        } while (gameField[x][y] != 0);

        if (random == 9) {// start the game with 2 random cells with 2(probability -90%) or 4 (10%)
            gameField[x][y] = 4;
        } else {
            gameField[x][y] = 2;
        }

    }

    private Color getColorByValue(int value) {

        if (value == 0) {
            return Color.WHITE;
        } else if (value == 2) {
            return Color.PURPLE;
        } else if (value == 4) {
            return Color.ORANGE;
        } else if (value == 8) {
            return Color.AQUA;
        } else if (value == 16) {
            return Color.RED;
        } else if (value == 32) {
            return Color.YELLOW;
        } else if (value == 64) {
            return Color.GREEN;
        } else if (value == 128) {
            return Color.BISQUE;
        } else if (value == 256) {
            return Color.GRAY;
        } else if (value == 512) {
            return Color.PINK;
        } else if (value == 1024) {
            return Color.BLUE;
        } else if (value == 2048) {
            return Color.BLUEVIOLET;
        } else
            return Color.WHITE;

    }

    private void setCellColoredNumber(int y, int x, int value) {// paint cells with same values

        if (value != 0) {
            setCellValueEx(y, x, getColorByValue(value), Integer.toString(value));
        } else {
            setCellValueEx(y, x, getColorByValue(value), "");
        }
    }

    // shifts all non-zero elements of the row array to the left , while zero
    // elements are moved to the right.
    private boolean compressRow(int[] row) {
        int temp = 0;
        int[] rowtemp = row.clone();
        boolean isChanged = false;
        for (int i = 0; i < row.length; i++) {
            for (int j = 0; j < row.length - i - 1; j++) {
                if (row[j] == 0) {
                    temp = row[j];
                    row[j] = row[j + 1];
                    row[j + 1] = temp;
                }
            }
        }
        if (!Arrays.equals(row, rowtemp))
            isChanged = true;
        // row = rowtemp;
        return isChanged;
    }

    private boolean mergeRow(int[] row) {
        boolean moved = false;
        for (int i = 0; i < row.length - 1; i++)
            if ((row[i] == row[i + 1]) && (row[i] != 0)) {
                row[i] = 2 * row[i];
                row[i + 1] = 0;
                moved = true;
                score = score + row[i];
                setScore(score);
            }

        return moved;
    }

    private void moveLeft() {
        boolean compress; // variable to get return from compressRow
        boolean merge; // variable to get return from mergeRow
        boolean compresss;
        int move = 0; // to check if compressRow or mergeRow occurs
        for (int i = 0; i < SIDE; i++) {
            compress = compressRow(gameField[i]);
            merge = mergeRow(gameField[i]);
            compresss = compressRow(gameField[i]);
            if (compress || merge || compresss)
                move++;
        }
        if (move != 0) {
            createNewNumber();
        }
    }

    private void moveRight() {
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
    }

    private void moveUp() {
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();
    }

    private void moveDown() {
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
    }

    private void rotateClockwise() {

        for (int i = 0; i < SIDE / 2; i++) {
            for (int j = i; j < SIDE - i - 1; j++) {
                // Swap elements of each cycle in clockwise direction
                int temp = gameField[i][j];
                gameField[i][j] = gameField[SIDE - 1 - j][i];
                gameField[SIDE - 1 - j][i] = gameField[SIDE - 1 - i][SIDE - 1 - j];
                gameField[SIDE - 1 - i][SIDE - 1 - j] = gameField[j][SIDE - 1 - i];
                gameField[j][SIDE - 1 - i] = temp;
            }
        }
    }

    // get the max value of the tile in the matrix
    private int getMaxTileValue() {
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < gameField.length; i++) {
            for (int j = 0; j < gameField[i].length; j++) {
                if (gameField[i][j] > max) {
                    max = gameField[i][j];
                }
            }
        }
        return max;
    }

    private void win() {
        isGameStopped = true;
        showMessageDialog(getColorByValue(2048), "You Won!", Color.BLUE, 12);
    }

    private boolean canUserMove() {
        boolean movesLeft = false;
        // comparison loop, checking for any duplicates when all spaces are taken.
        for (int i = 0; i < SIDE; i++)
            for (int j = 0; j < SIDE; j++) {
                if (gameField[i][j] == 0) {
                    movesLeft = true;
                    break;
                }
                // this if statement checks if there is an abovee in it cell has it got the same
                // value in it
                if ((i - 1) > 0 && (gameField[i][j] == gameField[i - 1][j])) {
                    movesLeft = true;
                }
                if ((i + 1) < SIDE && (gameField[i][j] == gameField[i + 1][j])) // this checks for DOWN
                {
                    movesLeft = true;
                }

                if ((j + 1) < SIDE && (gameField[i][j] == gameField[i][j + 1])) // this checks for RIGHT
                {
                    movesLeft = true;
                }

                if ((j - 1) > 0 && (gameField[i][j] == gameField[i][j - 1])) // this checks for LEFT
                {
                    movesLeft = true;
                }

            }
        // System.out.println(movesLeft);
        return movesLeft;

    }

    private void gameOver() {
        isGameStopped = true;
        showMessageDialog(Color.BLUE, "Game Over", Color.ORANGE, 12);
    }

    private void restart() {
        isGameStopped = false;
        score = 0;
        setScore(score);
        createGame();
        drawScene();
    }

    @Override
    public void initialize() {
        setScreenSize(SIDE, SIDE); // method to set the no.of grids
        createGame();
        drawScene();
    }

    public void onKeyPress(Key key) {

        if (isGameStopped) {
            if (key == Key.SPACE)
                restart();
            return;
        }
        if (!canUserMove()) {
            gameOver();
            if (key == Key.SPACE) {
                restart();
            }
            return;
        }

        if (key == Key.LEFT) {
            moveLeft();
            drawScene();
        } else if (key == Key.RIGHT) {
            moveRight();
            drawScene();
        } else if (key == Key.UP) {
            moveUp();
            drawScene();
        } else if (key == Key.DOWN) {
            moveDown();
            drawScene();
        }

    }

}