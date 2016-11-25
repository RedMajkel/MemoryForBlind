package pl.polsl.piechota.michal.memoryforblind.services;

import android.widget.TextView;

import pl.polsl.piechota.michal.memoryforblind.enums.Directions;

/**
 * Created by majke on 31.10.2016.
 */

public interface AnimationService {
    void swipe(TextView primary, TextView secondary, Directions direction, String text);

    boolean isLocked();
}
