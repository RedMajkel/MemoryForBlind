package pl.polsl.piechota.michal.memoryforblind.activities;

import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
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

    private AnimationService animationService;
    private InGameService inGameService;
    private TTSService ttsService;

    private GestureDetector gestureDetector;
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
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        inGameService = new InGameServiceImpl();
        ttsService = new TTSServiceImpl(getApplicationContext());

        initializeGestureDetector();

        board = inGameService.createBoard(WIDTH, HEIGHT);

        primary.setText("?");

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
                if (coordinates.x + 1 < WIDTH && !animationService.isLocked()) {
                    coordinates.x += 1;
                    animationService.swipe(primary, secondary, Directions.LEFT,
                            board.getTile(coordinates));
                    read();
                }
            }

            @Override
            public void onSwipeRight() {
                if (coordinates.x -1 >= 0 && !animationService.isLocked()) {
                    coordinates.x -= 1;
                    animationService.swipe(primary, secondary, Directions.RIGHT,
                            board.getTile(coordinates));
                }
                read();
            }

            @Override
            public void onSwipeUp() {
                if (coordinates.y + 1 < HEIGHT && !animationService.isLocked()) {
                    coordinates.y += 1;
                    animationService.swipe(primary, secondary, Directions.UP,
                            board.getTile(coordinates));
                }
                read();
            }

            @Override
            public void onSwipeDown() {
                if (coordinates.y - 1 >= 0 && !animationService.isLocked()) {
                    coordinates.y -= 1;
                    animationService.swipe(primary, secondary, Directions.DOWN,
                            board.getTile(coordinates));
                }
                read();
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                if (TileState.COVERED.equals(board.getTile(coordinates).getState())) {
                    if (selected == null) {
                        animationService.flip(primary, secondary, board.getTile(coordinates),
                                TileState.UNCOVERED);
                        selected = board.getTile(coordinates);
                        ttsService.speak("Wartość karty to "
                                +board.getTile(coordinates).getValue()+".");
                    } else {
                        if (!selected.equals(board.getTile(coordinates))) {
                            animationService.flip(primary, secondary, board.getTile(coordinates),
                                    TileState.UNCOVERED);
                            ttsService.speak("Wartość karty to "
                                    +board.getTile(coordinates).getValue()+".");
                            Tile current = board.getTile(coordinates);
                            if (selected.getValue() == current.getValue()) {
                                animationService.flip(primary, secondary, selected, TileState.GUESSED);
                                animationService.flip(primary, secondary, current, TileState.GUESSED);
                                ttsService.speak("Para znaleziona.");
                            } else {
                                selected.setState(TileState.COVERED);
                                current.setState(TileState.COVERED);
                                ttsService.speak("Błędne dopasowanie.");
                            }
                            selected = null;
                        }
                    }
                }
                return true;
            }
        });
    }

    private void read() {
        ttsService.readTile(board.getTile(coordinates));
    }
}
