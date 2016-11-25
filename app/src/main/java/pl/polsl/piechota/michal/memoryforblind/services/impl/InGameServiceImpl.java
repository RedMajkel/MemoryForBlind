package pl.polsl.piechota.michal.memoryforblind.services.impl;

import android.graphics.Point;

import java.util.Random;

import pl.polsl.piechota.michal.memoryforblind.Engine.Board;
import pl.polsl.piechota.michal.memoryforblind.enums.TileState;
import pl.polsl.piechota.michal.memoryforblind.services.InGameService;

public class InGameServiceImpl implements InGameService {
    private final Random random = new Random();

    @Override
    public Board createBoard(int width, int height) {
        if ((width*height) % 2 != 0){
            return null;
        }
        else {
            int count = (width*height) / 2;
            Board board = new Board(width, height);
            for (int i=0; i<count; i++){
                Point empty = findEmptyPlace(board);
                setTile(board, i, empty);

                empty = findEmptyPlace(board);
                setTile(board, i, empty);
            }
            return board;
        }

    }

    private void setTile(Board board, int i, Point empty) {
        board.getTile(empty.x, empty.y).setValue((char) ('A' + i));
        board.getTile(empty.x, empty.y).setState(TileState.COVERED);
    }

    private Point findEmptyPlace(Board board) {
        boolean success = false;
        int x = 0, y = 0;
        while (!success) {
            x = random.nextInt(board.getWidth());
            y = random.nextInt(board.getHeight());

            success = TileState.EMPTY.equals(board.getTile(x, y).getState());
        }

        return new Point(x, y);
    }


}
