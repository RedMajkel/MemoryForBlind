package pl.polsl.piechota.michal.memoryforblind.services;

import android.widget.TextView;

import pl.polsl.piechota.michal.memoryforblind.engine.Tile;
import pl.polsl.piechota.michal.memoryforblind.enums.Directions;
import pl.polsl.piechota.michal.memoryforblind.enums.TileState;

/**
 * Created by majke on 31.10.2016.
 */

public interface AnimationService {
    void swipe(TextView primary, TextView secondary, Directions direction, Tile tile);

    void flip(TextView primary, TextView secondary, Tile tile, TileState state);

    boolean isLocked();
}
