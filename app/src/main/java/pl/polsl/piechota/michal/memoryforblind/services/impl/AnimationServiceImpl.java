package pl.polsl.piechota.michal.memoryforblind.services.impl;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import pl.polsl.piechota.michal.memoryforblind.Engine.Tile;
import pl.polsl.piechota.michal.memoryforblind.enums.Directions;
import pl.polsl.piechota.michal.memoryforblind.enums.TileState;
import pl.polsl.piechota.michal.memoryforblind.services.AnimationService;

/**
 * Created by majke on 31.10.2016.
 */

public class AnimationServiceImpl implements AnimationService {
    private final long DURATION = 500;
    private Point center;
    private Point size;
    private boolean swap = false;
    private boolean lock = false;

    private final LockListener lockListener = new LockListener();
    private final UnlockListener unlockListener = new UnlockListener();

    public AnimationServiceImpl(View view, Context context) {
        view.measure(0, 0);

        center = new Point((int)(view.getX()+view.getMeasuredWidth()/2),
                (int)view.getY());

        size = getSize(context);
    }

    public void swipe(TextView primary, TextView secondary, Directions direction, Tile tile) {
        if (swap){
            TextView tmp = primary;
            primary = secondary;
            secondary = tmp;
        }

        primary.measure(0, 0);
        secondary.measure(0, 0);

        switch (direction) {
            case DOWN: swipeDown(primary, secondary); break;
            case UP: swipeUp(primary, secondary); break;
            case LEFT: swipeLeft(primary, secondary); break;
            case RIGHT: swipeRight(primary, secondary); break;
        }

        setTextOnView(secondary, tile);

        swap = !swap;
    }

    private void setTextOnView(TextView view, Tile tile) {
        if (TileState.COVERED.equals(tile.getState())) {
            view.setText("?");
        } else if (tile.getState().equals(TileState.GUESSED)) {
            view.setText("X");
        } else {
            view.setText(String.valueOf(tile.getValue()));
        }
    }

    public void flip(TextView primary, TextView secondary, Tile tile, TileState state) {
        TextView actualView;
        if (swap) {
            actualView = secondary;
        } else {
            actualView = primary;
        }
        tile.setState(state);
        setTextOnView(actualView, tile);
    }

    private void swipeLeft(View primary, View secondary) {
        secondary.setX(primary.getX()+size.x);
        secondary.setY(primary.getY());

        primary.animate()
                .setListener(lockListener)
                .translationXBy(-(size.x))
                .setDuration(DURATION);

        secondary.animate()
                .setListener(unlockListener)
                .translationXBy(-(size.x))
                .setDuration(DURATION);
    }

    private void swipeRight(View primary, View secondary) {
        secondary.setX(primary.getX()-size.x);
        secondary.setY(primary.getY());

        primary.animate()
                .setListener(lockListener)
                .translationXBy((size.x))
                .setDuration(DURATION);

        secondary.animate()
                .setListener(unlockListener)
                .translationXBy((size.x))
                .setDuration(DURATION);
    }

    private void swipeUp(View primary, View secondary) {
        secondary.setX(center.x - secondary.getMeasuredWidth()/2);
        secondary.setY(primary.getY()+size.y);

        primary.animate()
                .setListener(lockListener)
                .translationYBy(-(size.y))
                .setDuration(DURATION);

        secondary.animate()
                .setListener(unlockListener)
                .translationYBy(-(size.y))
                .setDuration(DURATION);
    }

    private void swipeDown(View primary, View secondary) {
        secondary.setX(center.x - secondary.getMeasuredWidth()/2);
        secondary.setY(primary.getY()-size.y);

        primary.animate()
                .setListener(lockListener)
                .translationYBy(size.y)
                .setDuration(DURATION);

        secondary.animate()
                .setListener(unlockListener)
                .translationYBy(size.y)
                .setDuration(DURATION);
    }

    @NonNull
    private Point getSize(Context context) {
        Point size = new Point();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getSize(size);
        return size;
    }

    public boolean isLocked(){
        return lock;
    }

    private class LockListener implements Animator.AnimatorListener{

        @Override
        public void onAnimationStart(Animator animation) {
            lock = true;
        }

        @Override
        public void onAnimationEnd(Animator animation) {

        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }

    private class UnlockListener implements Animator.AnimatorListener{

        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {
            lock = false;
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }
}
