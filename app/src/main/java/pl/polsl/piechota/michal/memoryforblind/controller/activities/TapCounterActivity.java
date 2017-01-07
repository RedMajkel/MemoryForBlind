package pl.polsl.piechota.michal.memoryforblind.controller.activities;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnTouch;
import pl.polsl.piechota.michal.memoryforblind.R;
import pl.polsl.piechota.michal.memoryforblind.controller.listeners.GestureListener;
import pl.polsl.piechota.michal.memoryforblind.controller.services.InGameService;
import pl.polsl.piechota.michal.memoryforblind.controller.services.TTSService;

public class TapCounterActivity extends AppCompatActivity {

    private int counter;
    private GestureDetector gestureDetector;
    private TTSService ttsService;
    private InGameService inGameService;

    @InjectView(R.id.counter)
    TextView counterView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tap_counter);
        ButterKnife.inject(this);
        initializeGestureDetector();
        initTTSService();
        counter = 0;
        counterView.setText(String.valueOf(counter));
        inGameService = InGameService.getInstance();
    }

    @OnTouch(R.id.activity_tap_counter)
    public boolean onTouch(View v, MotionEvent e) {
        gestureDetector.onTouchEvent(e);
        return true;
    }

    private void initTTSService() {
        ttsService = new TTSService(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                ttsService.speak(getString(R.string.tts_counter));
            }
        });
    }

    private void initializeGestureDetector() {
        gestureDetector = new GestureDetector(getApplicationContext(), new GestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                if (counter + 1 <= 18) {
                    counter++;
                    counterView.setText(String.valueOf(counter));
                    ttsService.speak(String.valueOf(counter));
                }
                return true;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                inGameService.calculateBoard(counter);
                finish();
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                ttsService.stop();
                finish();
            }
        });
    }
}
