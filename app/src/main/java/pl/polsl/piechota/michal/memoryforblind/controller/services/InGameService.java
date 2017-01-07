package pl.polsl.piechota.michal.memoryforblind.controller.services;

import android.graphics.Point;

import java.util.Random;

import pl.polsl.piechota.michal.memoryforblind.model.BoardModel;
import pl.polsl.piechota.michal.memoryforblind.model.utils.TileStateEnum;

import static pl.polsl.piechota.michal.memoryforblind.model.utils.Const.HEIGHT;
import static pl.polsl.piechota.michal.memoryforblind.model.utils.Const.WIDTH;

public class InGameService {
    private final Random random = new Random();
    private static InGameService instance;

    private InGameService() {

    }

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

    public void calculateBoard(int pairsCount) {
        int tiles = pairsCount * 2;
        int prevWidth = pairsCount;
        int prevHeight = 2;
        int width = prevWidth;
        int heigth = prevHeight;
        int w, h;
        for (int i = 3; i <= pairsCount; i++) {
            if (tiles % i == 0) {
                h = i;
                w = tiles / i;
                if ((h == prevWidth && w == prevHeight) || h * w == tiles) {
                    width = prevWidth;
                    heigth = prevHeight;
                }
                prevHeight = h;
                prevWidth = w;
            }
        }

        WIDTH = width;
        HEIGHT = heigth;
    }

    public static InGameService getInstance() {
        if (instance == null) {
            instance = new InGameService();
        }
        return instance;
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
