package pl.polsl.piechota.michal.memoryforblind.controller.services.impl;

import android.graphics.Point;

import java.util.Random;

import pl.polsl.piechota.michal.memoryforblind.controller.services.InGameService;
import pl.polsl.piechota.michal.memoryforblind.model.BoardModel;
import pl.polsl.piechota.michal.memoryforblind.model.utils.TileStateEnum;

public class InGameServiceImpl implements InGameService {
    private final Random random = new Random();

    @Override
    public BoardModel createBoard(int width, int height) {
        if ((width*height) % 2 != 0){
            return null;
        }
        else {
            int count = (width*height) / 2;
            BoardModel board = new BoardModel(width, height);
            for (int i=0; i<count; i++){
                Point empty = findEmptyPlace(board);
                setTile(board, i, empty);

                empty = findEmptyPlace(board);
                setTile(board, i, empty);
            }
            return board;
        }

    }

    private void setTile(BoardModel board, int i, Point empty) {
        board.getTile(empty.x, empty.y).setValue((char) ('A' + i));
        board.getTile(empty.x, empty.y).setState(TileStateEnum.COVERED);
    }

    private Point findEmptyPlace(BoardModel board) {
        boolean success = false;
        int x = 0, y = 0;
        while (!success) {
            x = random.nextInt(board.getWidth());
            y = random.nextInt(board.getHeight());

            success = TileStateEnum.EMPTY.equals(board.getTile(x, y).getState());
        }

        return new Point(x, y);
    }


}
