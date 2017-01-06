package pl.polsl.piechota.michal.memoryforblind.activities;

import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnTouch;
import pl.polsl.piechota.michal.memoryforblind.R;
import pl.polsl.piechota.michal.memoryforblind.engine.Board;
import pl.polsl.piechota.michal.memoryforblind.engine.Tile;
import pl.polsl.piechota.michal.memoryforblind.enums.Directions;
import pl.polsl.piechota.michal.memoryforblind.enums.TileState;
import pl.polsl.piechota.michal.memoryforblind.listeners.GestureListener;
import pl.polsl.piechota.michal.memoryforblind.services.AnimationService;
import pl.polsl.piechota.michal.memoryforblind.services.InGameService;
import pl.polsl.piechota.michal.memoryforblind.services.TTSService;
import pl.polsl.piechota.michal.memoryforblind.services.impl.AnimationServiceImpl;
import pl.polsl.piechota.michal.memoryforblind.services.impl.InGameServiceImpl;

import static pl.polsl.piechota.michal.memoryforblind.engine.Const.HEIGHT;
import static pl.polsl.piechota.michal.memoryforblind.engine.Const.WIDTH;

public class InGameActivity extends AppCompatActivity {
    private int guessed = 0;

    private AnimationService animationService;
    private InGameService inGameService;
    private TTSService ttsService;

    private GestureDetector gestureDetector;
    private Vibrator vibrator;

    private Tile selected;
    private Point coordinates;
    private Board board;

    private long time;

    @InjectView(R.id.primary)
    TextView primary;

    @InjectView(R.id.secondary)
    TextView secondary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initGame();
    }

    private void init() {
        initActivityParams();
        initServices();
        initGestureDetector();
    }

    private void initGame() {
        board = inGameService.createBoard(WIDTH, HEIGHT);
        coordinates = new Point(0, 0);
        primary.setText("?");
        read();
        time = System.nanoTime();
    }

    private void initActivityParams() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);
    }

    private void initServices() {
        ttsService = new TTSService(getApplicationContext());
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        inGameService = new InGameServiceImpl();
    }

    @OnTouch(R.id.activity_main)
    public boolean onTouch(View v, MotionEvent event) {
        if (animationService == null) {
            //TODO - OKROPNE(!!!) rozwiązanie
            animationService = new AnimationServiceImpl(primary, secondary, getApplicationContext());
        }
        return gestureDetector.onTouchEvent(event);
    }

    public void initGestureDetector() {
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
                        endOfBoard();
                    }
                }
                else {
                    vibrator.vibrate(100);
                }
            }

            private void endOfBoard() {
                vibrator.vibrate(250);
                ttsService.speak(getString(R.string.tts_end_of_board));
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
                        endOfBoard();
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
                        endOfBoard();
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
                        endOfBoard();
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
                            checkWhenTTSIsDone();
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
                ttsService.stop();
                finish();
            }

            private boolean canAnimate() {
                return !animationService.isLocked() && !ttsService.isSpeaking();
            }

            private void flip() {
                animationService.flip(primary, secondary, board.getTile(coordinates),
                        TileState.UNCOVERED);
                ttsService.speak(String.valueOf(board.getTile(coordinates).getValue()));
            }

            public void checkWhenTTSIsDone() {

                final Handler h = new Handler();

                Runnable r = new Runnable() {
                    private volatile boolean shutdown = false;

                    public void run() {
                        while (!shutdown) {
                            if (!ttsService.isSpeaking()) {
                                shutdown = true;
                                check();
                            }

                            h.postDelayed(this, 1000);
                        }
                    }
                };

                h.postDelayed(r, 1000);
            }

            private void check() {
                if (selected.getValue() == board.getTile(coordinates).getValue()) {
                    animationService.flip(primary, secondary, selected, TileState.GUESSED);
                    animationService.flip(primary, secondary, board.getTile(coordinates), TileState.GUESSED);
                    ttsService.speak(getString(R.string.tts_pair_found));
                    guessed++;
                    if (guessed*2 == WIDTH*HEIGHT){
                        ttsService.speak(
                                String.format(getString(R.string.tts_you_have_won),
                                        (System.nanoTime() - time) / 1000000000));
                        finish();
                    }
                } else {
                    selected.setState(TileState.COVERED);
                    animationService.flip(primary, secondary, selected, TileState.COVERED);
                    board.getTile(coordinates).setState(TileState.COVERED);
                    ttsService.speak(getString(R.string.tts_pair_mismatch));
                }
                selected = null;
            }
        });
    }


    private void read() {
        ttsService.readTile(board.getTile(coordinates));
    }
}
