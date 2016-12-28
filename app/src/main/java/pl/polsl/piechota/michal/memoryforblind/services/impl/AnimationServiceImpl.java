package pl.polsl.piechota.michal.memoryforblind.services.impl;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import pl.polsl.piechota.michal.memoryforblind.engine.Tile;
import pl.polsl.piechota.michal.memoryforblind.enums.Directions;
import pl.polsl.piechota.michal.memoryforblind.enums.TileState;
import pl.polsl.piechota.michal.memoryforblind.services.AnimationService;

/**
 * Created by majke on 31.10.2016.
 */

public class AnimationServiceImpl implements AnimationService {
    private final long DURATION = 500;
    private Point center;
    private Point screenSize;
    private boolean swap = false;
    private boolean lock = false;

    private final LockListener lockListener = new LockListener();
    private final UnlockListener unlockListener = new UnlockListener();

    public AnimationServiceImpl(View primary, View secondary, Context context) {
        primary.measure(0, 0);

        center = new Point((int) (primary.getX() + primary.getMeasuredWidth() / 2),
                (int) primary.getY());

        primary.setX(center.x - primary.getMeasuredWidth() / 2);
        secondary.setX(center.x - secondary.getMeasuredWidth() / 2);

        screenSize = getSize(context);
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
            view.setText("#");
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
        actualView.setX(center.x - actualView.getMeasuredWidth() / 2);
    }

    private void swipeLeft(View primary, View secondary) {
        secondary.setX(center.x - secondary.getMeasuredWidth() / 2 + screenSize.x);
        secondary.setY(primary.getY());

        primary.animate()
                .setListener(lockListener)
                .translationXBy(-(screenSize.x))
                .setDuration(DURATION);

        secondary.animate()
                .setListener(unlockListener)
                .translationXBy(-(screenSize.x))
                .setDuration(DURATION);
    }

    private void swipeRight(View primary, View secondary) {
        secondary.setX(center.x - secondary.getMeasuredWidth() / 2 - screenSize.x);
        secondary.setY(primary.getY());

        primary.animate()
                .setListener(lockListener)
                .translationXBy((screenSize.x))
                .setDuration(DURATION);

        secondary.animate()
                .setListener(unlockListener)
                .translationXBy((screenSize.x))
                .setDuration(DURATION);
    }

    private void swipeUp(View primary, View secondary) {
        secondary.setX(center.x - secondary.getMeasuredWidth()/2);
        secondary.setY(primary.getY() + screenSize.y);

        primary.animate()
                .setListener(lockListener)
                .translationYBy(-(screenSize.y))
                .setDuration(DURATION);

        secondary.animate()
                .setListener(unlockListener)
                .translationYBy(-(screenSize.y))
                .setDuration(DURATION);
    }

    private void swipeDown(View primary, View secondary) {
        secondary.setX(center.x - secondary.getMeasuredWidth()/2);
        secondary.setY(primary.getY() - screenSize.y);

        primary.animate()
                .setListener(lockListener)
                .translationYBy(screenSize.y)
                .setDuration(DURATION);

        secondary.animate()
                .setListener(unlockListener)
                .translationYBy(screenSize.y)
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
