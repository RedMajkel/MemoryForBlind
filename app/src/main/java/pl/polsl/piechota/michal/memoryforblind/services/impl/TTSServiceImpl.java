package pl.polsl.piechota.michal.memoryforblind.services.impl;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import pl.polsl.piechota.michal.memoryforblind.Engine.Tile;
import pl.polsl.piechota.michal.memoryforblind.services.TTSService;

/**
 * Created by michalp on 2016-12-27.
 */

@SuppressWarnings("deprecation")
public class TTSServiceImpl implements TTSService, TextToSpeech.OnInitListener {
    private final TextToSpeech textToSpeech;

    private final String TILE_STATE_WITH_VALUE = "Karta jest %s , a jej wartość to %s";
    private final String TILE_STATE = "Karta jest %s";


    public TTSServiceImpl(Context context) {
        textToSpeech = new TextToSpeech(context, this);
    }

    @Override
    public void speak(String text){
        textToSpeech.speak(text, TextToSpeech.QUEUE_ADD, null);
    }

    @Override
    public void readTile(Tile tile) {
        switch (tile.getState()){
            case COVERED:
                speak(String.format(TILE_STATE, "zakryta"));
                break;
            case UNCOVERED:
                speak(String.format(TILE_STATE_WITH_VALUE, "odkryta", tile.getValue()));
                break;
            case GUESSED:
                speak(String.format(TILE_STATE, "już odgadnięta"));
                break;
            default: break;
        }
    }

    @Override
    public void onInit(int status) {

    }
}
