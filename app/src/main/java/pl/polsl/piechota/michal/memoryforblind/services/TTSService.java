package pl.polsl.piechota.michal.memoryforblind.services;

import pl.polsl.piechota.michal.memoryforblind.Engine.Tile;

/**
 * Created by michalp on 2016-12-27.
 */

public interface TTSService {
    void speak(String text);

    void readTile(Tile tile);

    boolean isSpeaking();
}
