package pl.polsl.piechota.michal.memoryforblind.model;

import pl.polsl.piechota.michal.memoryforblind.model.utils.TileStateEnum;

/**
 * Created by majke on 25.11.2016.
 */

public class TileModel {
    private char value;
    private TileStateEnum state;

    public TileModel() {
        this.state = TileStateEnum.EMPTY;
    }

    public char getValue() {
        return value;
    }

    public void setValue(char value) {
        this.value = value;
    }

    public TileStateEnum getState() {
        return state;
    }

    public void setState(TileStateEnum state) {
        this.state = state;
    }
}
