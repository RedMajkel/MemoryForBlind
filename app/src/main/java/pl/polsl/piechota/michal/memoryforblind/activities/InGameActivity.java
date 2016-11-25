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
import pl.polsl.piechota.michal.memoryforblind.R;
import pl.polsl.piechota.michal.memoryforblind.enums.Directions;
import pl.polsl.piechota.michal.memoryforblind.listeners.GestureListener;
import pl.polsl.piechota.michal.memoryforblind.services.AnimationService;
import pl.polsl.piechota.michal.memoryforblind.services.InGameService;
import pl.polsl.piechota.michal.memoryforblind.services.impl.AnimationServiceImpl;
import pl.polsl.piechota.michal.memoryforblind.services.impl.InGameServiceImpl;

public class InGameActivity extends AppCompatActivity {
    /*TODO usunąć stałe*/
    private final int WIDTH = 4;
    private final int HEIGHT = 4;

    private AnimationService animationService;
    private InGameService inGameService;
    private GestureDetector gestureDetector;

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

        initializeGestureDetector();

        board = inGameService.createBoard(WIDTH, HEIGHT);

        primary.setText(String.valueOf(board.getTile(0, 0).getValue()));

    }

    @OnTouch(R.id.activity_main)
    public boolean onTouch(View v, MotionEvent event) {
        if (animationService == null) {
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
                            String.valueOf(board.getTile(coordinates).getValue()));
                }
            }

            @Override
            public void onSwipeRight() {
                if (coordinates.x -1 >= 0 && !animationService.isLocked()) {
                    coordinates.x -= 1;
                    animationService.swipe(primary, secondary, Directions.RIGHT,
                            String.valueOf(board.getTile(coordinates).getValue()));
                }
            }

            @Override
            public void onSwipeUp() {
                if (coordinates.y + 1 < HEIGHT && !animationService.isLocked()) {
                    coordinates.y += 1;
                    animationService.swipe(primary, secondary, Directions.UP,
                            String.valueOf(board.getTile(coordinates).getValue()));
                }
            }

            @Override
            public void onSwipeDown() {
                if (coordinates.y - 1 >= 0 && !animationService.isLocked()) {
                    coordinates.y -= 1;
                    animationService.swipe(primary, secondary, Directions.DOWN,
                            String.valueOf(board.getTile(coordinates).getValue()));
                }
            }
        });
    }
}
