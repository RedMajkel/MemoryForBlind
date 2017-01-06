package pl.polsl.piechota.michal.memoryforblind.controller.activities;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnTouch;
import pl.polsl.piechota.michal.memoryforblind.R;
import pl.polsl.piechota.michal.memoryforblind.controller.listeners.GestureListener;
import pl.polsl.piechota.michal.memoryforblind.controller.services.TTSService;

public class MainMenuActivity extends AppCompatActivity {

    private GestureDetector gestureDetector;
    private TTSService ttsService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        ButterKnife.inject(this);
        initTTSService();
        initGestureDetector();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ttsService.speak(getString(R.string.tts_hello));
    }

    private void initTTSService() {
        ttsService = new TTSService(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                ttsService.speak(getString(R.string.tts_hello));
            }
        });
    }

    @OnTouch(R.id.activity_main_menu)
    public boolean onTouch(View v, MotionEvent e) {
        return gestureDetector.onTouchEvent(e);
    }

    private void startActivity(Class<?> c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }

    private void initGestureDetector() {
        gestureDetector = new GestureDetector(getApplicationContext(), new GestureListener() {
            int counter;

            @Override
            public void onLongPress(MotionEvent e) {
                ttsService.shutdown();
                System.exit(1);
            }

            @Override
            public void onSwipeUp() {
                if (!ttsService.isSpeaking()) {
                    startActivity(InGameActivity.class);
                }
            }

            @Override
            public void onSwipeLeft() {
                if (!ttsService.isSpeaking()) {
                    startActivity(TapCounterActivity.class);
                }
            }

            @Override
            public void onSwipeRight() {
                if (!ttsService.isSpeaking()) {
                    ttsService.speak(getString(R.string.tts_help));
                }
            }
        });
    }
}
