package pl.polsl.piechota.michal.memoryforblind.services;

import pl.polsl.piechota.michal.memoryforblind.Engine.Board;

/**
 * Created by majke on 31.10.2016.
 */

public interface InGameService {
    Board createBoard(int width, int height);
}
