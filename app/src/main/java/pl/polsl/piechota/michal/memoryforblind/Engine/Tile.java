package pl.polsl.piechota.michal.memoryforblind.Engine;

import pl.polsl.piechota.michal.memoryforblind.enums.TileState;

/**
 * Created by majke on 25.11.2016.
 */

public class Tile {
    private char value;
    private TileState state;

    public Tile() {
        this.state = TileState.EMPTY;
    }

    public char getValue() {
        return value;
    }

    public void setValue(char value) {
        this.value = value;
    }

    public TileState getState() {
        return state;
    }

    public void setState(TileState state) {
        this.state = state;
    }
}
