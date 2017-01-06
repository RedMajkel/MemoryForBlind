package pl.polsl.piechota.michal.memoryforblind.model;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by majke on 25.11.2016.
 */

public class BoardModel {
    final private List<List<TileModel>> board;

    public BoardModel(int w, int h) {
        board = new ArrayList<>();
        for (int i = 0; i < w; i++) {
            board.add(new ArrayList<TileModel>());
            for (int j = 0; j < h; j++) {
                board.get(i).add(new TileModel());
            }
        }
    }

    public int getWidth() {
        return board.size();
    }

    public int getHeight() {
        return board.get(0).size();
    }

    public TileModel getTile(int x, int y) {
        return board.get(x).get(y);
    }

    public TileModel getTile(Point p) {
        return board.get(p.x).get(p.y);
    }
}
