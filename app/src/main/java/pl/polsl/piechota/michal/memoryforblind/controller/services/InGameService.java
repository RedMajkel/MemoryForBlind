package pl.polsl.piechota.michal.memoryforblind.controller.services;

import pl.polsl.piechota.michal.memoryforblind.model.BoardModel;

/**
 * Created by majke on 31.10.2016.
 */

public interface InGameService {
    BoardModel createBoard(int width, int height);
}
