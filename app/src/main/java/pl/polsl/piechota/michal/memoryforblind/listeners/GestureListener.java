package pl.polsl.piechota.michal.memoryforblind.listeners;

import android.view.GestureDetector;
import android.view.MotionEvent;

public class GestureListener extends GestureDetector.SimpleOnGestureListener {
    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        boolean result = false;
        float deltaX = e1.getX() - e2.getX();
        float deltaY = e1.getY() - e2.getY();
        if (Math.abs(deltaX) > Math.abs(deltaY)) {
            if (Math.abs(deltaX) > 100 && Math.abs(velocityX) > 100) {
                if (deltaX > 0) {
                    onSwipeLeft();
                } else {
                    onSwipeRight();
                }
            }
            result = true;
        } else if (Math.abs(deltaY) > 100 && Math.abs(velocityY) > 100) {
            if (deltaY > 0) {
                onSwipeUp();
            } else {
                onSwipeDown();
            }
            result = true;
        }
        return result;
    }

    public void onSwipeDown() {

    }

    public void onSwipeUp() {

    }

    public void onSwipeRight() {

    }

    public void onSwipeLeft() {
    }
}
