package pl.polsl.piechota.michal.memoryforblind.activities;

import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnTouch;
import pl.polsl.piechota.michal.memoryforblind.Engine.Board;
import pl.polsl.piechota.michal.memoryforblind.Engine.Tile;
import pl.polsl.piechota.michal.memoryforblind.R;
import pl.polsl.piechota.michal.memoryforblind.enums.Directions;
import pl.polsl.piechota.michal.memoryforblind.enums.TileState;
import pl.polsl.piechota.michal.memoryforblind.listeners.GestureListener;
import pl.polsl.piechota.michal.memoryforblind.services.AnimationService;
import pl.polsl.piechota.michal.memoryforblind.services.InGameService;
import pl.polsl.piechota.michal.memoryforblind.services.TTSService;
import pl.polsl.piechota.michal.memoryforblind.services.impl.AnimationServiceImpl;
import pl.polsl.piechota.michal.memoryforblind.services.impl.InGameServiceImpl;
import pl.polsl.piechota.michal.memoryforblind.services.impl.TTSServiceImpl;

public class InGameActivity extends AppCompatActivity {
    /*TODO usunąć stałe*/
    private final int WIDTH = 4;
    private final int HEIGHT = 4;

    private int guessed = 0;

    private AnimationService animationService;
    private InGameService inGameService;
    private TTSService ttsService;

    private GestureDetector gestureDetector;
    private Vibrator vibrator;

    private Tile selected;
    private Point coordinates = new Point(0, 0);
    private Board board;

    @InjectView(R.id.primary)
    TextView primary;

    @InjectView(R.id.secondary)
    TextView secondary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ttsService = new TTSServiceImpl(getApplicationContext());
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        inGameService = new InGameServiceImpl();


        initializeGestureDetector();

        board = inGameService.createBoard(WIDTH, HEIGHT);

        primary.setText("?");
        read();

    }

    @OnTouch(R.id.activity_main)
    public boolean onTouch(View v, MotionEvent event) {
        if (animationService == null) {
            //TODO - OKROPNE(!!!) rozwiązanie
            animationService = new AnimationServiceImpl(primary, getApplicationContext());
        }
        return gestureDetector.onTouchEvent(event);
    }

    public void initializeGestureDetector() {
        gestureDetector = new GestureDetector(getApplicationContext(), new GestureListener(){
            @Override
            public void onSwipeLeft() {
                if (canAnimate()) {
                    if (coordinates.x + 1 < WIDTH) {
                        coordinates.x += 1;
                        animationService.swipe(primary, secondary, Directions.LEFT,
                                board.getTile(coordinates));
                        read();
                    }
                    else {
                        vibrator.vibrate(250);
                        ttsService.speak(getString(R.string.tts_end_of_board));
                    }
                }
                else {
                    vibrator.vibrate(100);
                }
            }

            @Override
            public void onSwipeRight() {
                if (canAnimate()) {
                    if (coordinates.x - 1 >= 0) {
                        coordinates.x -= 1;
                        animationService.swipe(primary, secondary, Directions.RIGHT,
                                board.getTile(coordinates));
                        read();
                    }
                    else {
                        vibrator.vibrate(250);
                        ttsService.speak(getString(R.string.tts_end_of_board));
                    }
                }
                else {
                    vibrator.vibrate(100);
                }
            }

            @Override
            public void onSwipeUp() {
                if (canAnimate()) {
                    if (coordinates.y + 1 < HEIGHT) {
                        coordinates.y += 1;
                        animationService.swipe(primary, secondary, Directions.UP,
                                board.getTile(coordinates));
                        read();
                    }
                    else {
                        vibrator.vibrate(250);
                        ttsService.speak(getString(R.string.tts_end_of_board));
                    }
                }
                else {
                    vibrator.vibrate(100);
                }
            }

            @Override
            public void onSwipeDown() {
                if (canAnimate()) {
                    if (coordinates.y - 1 >= 0) {
                        coordinates.y -= 1;
                        animationService.swipe(primary, secondary, Directions.DOWN,
                                board.getTile(coordinates));
                        read();
                    }
                    else {
                        vibrator.vibrate(250);
                        ttsService.speak(getString(R.string.tts_end_of_board));
                    }
                }
                else {
                    vibrator.vibrate(100);
                }
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                if (canAnimate() && TileState.COVERED.equals(board.getTile(coordinates).getState())) {
                    if (selected == null) {
                        flip();
                        selected = board.getTile(coordinates);
                    } else {
                        if (!selected.equals(board.getTile(coordinates))) {
                            flip();
                            check();
                            selected = null;
                        }
                    }
                    return true;
                } else {
                    vibrator.vibrate(100);
                    return false;
                }
            }

            @Override
            public void onLongPress(MotionEvent e){
                if (!ttsService.isSpeaking()) {
                    read();
                }
            }

            private boolean canAnimate() {
                return !animationService.isLocked() && !ttsService.isSpeaking();
            }

            private void flip() {
                animationService.flip(primary, secondary, board.getTile(coordinates),
                        TileState.UNCOVERED);
                ttsService.speak(String.format(getString(R.string.tts_tiles_value_is),
                        board.getTile(coordinates).getValue()));
            }


            private void check() {
                if (selected.getValue() == board.getTile(coordinates).getValue()) {
                    animationService.flip(primary, secondary, selected, TileState.GUESSED);
                    animationService.flip(primary, secondary, board.getTile(coordinates), TileState.GUESSED);
                    ttsService.speak(getString(R.string.tts_pair_found));
                } else {
                    selected.setState(TileState.COVERED);
                    board.getTile(coordinates).setState(TileState.COVERED);
                    ttsService.speak(getString(R.string.tts_pair_mismatch));
                }
            }

        });
    }

    private void read() {
        ttsService.readTile(board.getTile(coordinates));
    }
}
