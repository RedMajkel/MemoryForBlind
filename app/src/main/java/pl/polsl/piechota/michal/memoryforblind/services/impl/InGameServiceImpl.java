package pl.polsl.piechota.michal.memoryforblind.services.impl;

import android.graphics.Point;

import java.util.Random;

import pl.polsl.piechota.michal.memoryforblind.services.InGameService;

public class InGameServiceImpl implements InGameService {
    private final Random random = new Random();
    private char[][] board;

    @Override
    public char[][] createBoard(int width, int height) {
        if ((width*height) % 2 != 0){
            return board;
        }
        else {
            int count = (width*height) / 2;
            board = initializeEmptyBoard(width, height);

            for (int i=0; i<count; i++){
                Point empty = findEmptyPlace(board);
                board[empty.x][empty.y] = (char)('A' + i);

                empty = findEmptyPlace(board);
                board[empty.x][empty.y] = (char)('A' + i);
            }
        }
        return board;
    }

    private Point findEmptyPlace(char[][] board) {
        boolean success = false;
        int x = 0, y = 0;
        while (!success) {
            x = random.nextInt(board.length);
            y = random.nextInt(board[0].length);

            success = board[x][y] == '0';
        }

        return new Point(x, y);
    }

    private char[][] initializeEmptyBoard(int width, int height) {
        char[][] board = new char[width][height];

        for (int x=0; x<width; x++) {
            for (int y=0; y<height; y++) {
                board[x][y] = '0';
            }
        }
        return board;
    }
}
