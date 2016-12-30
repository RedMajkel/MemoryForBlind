package pl.polsl.piechota.michal.memoryforblind.view.services;

import android.widget.TextView;

import pl.polsl.piechota.michal.memoryforblind.model.TileModel;
import pl.polsl.piechota.michal.memoryforblind.model.utils.DirectionsEnum;
import pl.polsl.piechota.michal.memoryforblind.model.utils.TileStateEnum;

/**
 * Created by majke on 31.10.2016.
 */

public interface AnimationService {
    void swipe(TextView primary, TextView secondary, DirectionsEnum direction, TileModel tile);

    void flip(TextView primary, TextView secondary, TileModel tile, TileStateEnum state);

    boolean isLocked();
}
