package pl.polsl.piechota.michal.memoryforblind.controller.activities;

import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
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
import pl.polsl.piechota.michal.memoryforblind.model.BoardModel;
import pl.polsl.piechota.michal.memoryforblind.model.TileModel;
import pl.polsl.piechota.michal.memoryforblind.model.utils.DirectionsEnum;
import pl.polsl.piechota.michal.memoryforblind.model.utils.TileStateEnum;
import pl.polsl.piechota.michal.memoryforblind.view.services.AnimationService;
import pl.polsl.piechota.michal.memoryforblind.view.services.impl.AnimationServiceImpl;

import static pl.polsl.piechota.michal.memoryforblind.model.utils.Const.HEIGHT;
import static pl.polsl.piechota.michal.memoryforblind.model.utils.Const.WIDTH;

public class InGameActivity extends AppCompatActivity {
    private int guessed = 0;

    private AnimationService animationService;
    private InGameService inGameService;
    private TTSService ttsService;

    private GestureDetector gestureDetector;
    private Vibrator vibrator;

    private TileModel selected;
    private Point coordinates;
    private BoardModel board;

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
        time = System.nanoTime();
    }

    private void initActivityParams() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        primary.measure(0, 0);
        primary.setY(primary.getY() - primary.getMeasuredHeight() * 0.15f);
    }

    private void initServices() {
        ttsService = new TTSService(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                readCurrentTile();
            }
        });
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        inGameService = InGameService.getInstance();
    }

    @OnTouch(R.id.activity_main)
    public boolean onTouch(View v, MotionEvent event) {
        if (animationService == null) {
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
                        animationService.swipe(primary, secondary, DirectionsEnum.LEFT,
                                board.getTile(coordinates));
                        readCurrentTile();
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
                        animationService.swipe(primary, secondary, DirectionsEnum.RIGHT,
                                board.getTile(coordinates));
                        readCurrentTile();
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
                        animationService.swipe(primary, secondary, DirectionsEnum.UP,
                                board.getTile(coordinates));
                        readCurrentTile();
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
                        animationService.swipe(primary, secondary, DirectionsEnum.DOWN,
                                board.getTile(coordinates));
                        readCurrentTile();
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
                if (canAnimate() && TileStateEnum.COVERED.equals(board.getTile(coordinates).getState())) {
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
                        TileStateEnum.UNCOVERED);
                ttsService.speak(String.valueOf(board.getTile(coordinates).getValue()));
            }

            private void checkWhenTTSIsDone() {

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
                    animationService.flip(primary, secondary, selected, TileStateEnum.GUESSED);
                    animationService.flip(primary, secondary, board.getTile(coordinates), TileStateEnum.GUESSED);
                    ttsService.speak(getString(R.string.tts_pair_found));
                    guessed++;
                    if (guessed*2 == WIDTH*HEIGHT){
                        ttsService.speak(
                                String.format(getString(R.string.tts_you_have_won),
                                        (System.nanoTime() - time) / 1000000000));
                        finish();
                    }
                } else {
                    selected.setState(TileStateEnum.COVERED);
                    animationService.flip(primary, secondary, selected, TileStateEnum.COVERED);
                    board.getTile(coordinates).setState(TileStateEnum.COVERED);
                    ttsService.speak(getString(R.string.tts_pair_mismatch));
                }
                selected = null;
            }
        });
    }


    private void readCurrentTile() {
        ttsService.readTile(board.getTile(coordinates));
    }
}
