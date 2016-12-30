package pl.polsl.piechota.michal.memoryforblind.controller.services;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import pl.polsl.piechota.michal.memoryforblind.R;
import pl.polsl.piechota.michal.memoryforblind.model.TileModel;

/**
 * Created by michalp on 2016-12-27.
 */

@SuppressWarnings("deprecation")
public class TTSService extends TextToSpeech {
    private final Context c;

    public TTSService(Context context) {
        super(context, new OnInitListener() {
            @Override
            public void onInit(int status) {

            }
        });
        c = context;
    }

    public TTSService(Context context, OnInitListener listener) {
        super(context, listener);
        c = context;
    }

    public void speak(String text){
        speak(text, TextToSpeech.QUEUE_ADD, null);
    }

    public void readTile(TileModel tile) {
        switch (tile.getState()){
            case COVERED:
                speak(c.getString(R.string.tts_tile_covered));
                break;
            case UNCOVERED:
                speak(String.format(c.getString(R.string.tts_tile_uncovered), tile.getValue()));
                break;
            case GUESSED:
                speak(c.getString(R.string.tts_tile_guessed));
                break;
            default: break;
        }
    }
}
