package pl.polsl.piechota.michal.memoryforblind.services.impl;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import pl.polsl.piechota.michal.memoryforblind.Engine.Tile;
import pl.polsl.piechota.michal.memoryforblind.R;
import pl.polsl.piechota.michal.memoryforblind.services.TTSService;

/**
 * Created by michalp on 2016-12-27.
 */

@SuppressWarnings("deprecation")
public class TTSServiceImpl implements TTSService, TextToSpeech.OnInitListener {
    private final TextToSpeech textToSpeech;
    private final Context c;

    public TTSServiceImpl(Context context) {
        c = context;
        textToSpeech = new TextToSpeech(c, this);
    }

    @Override
    public void speak(String text){
        textToSpeech.speak(text, TextToSpeech.QUEUE_ADD, null);
    }

    @Override
    public void readTile(Tile tile) {
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

    @Override
    public boolean isSpeaking() {
        return textToSpeech.isSpeaking();
    }

    @Override
    public void onInit(int status) {
    }
}
