package com.example.rma_2_anis_karic;

import android.view.GestureDetector;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public abstract class SwipeListener extends GestureDetector.SimpleOnGestureListener {

    //RECOGNITION PARAMETERS
    private static final int DISTANCE_THRESHOLD = 100;
    private static final int VELOCITY_THRESHOLD = 100;

    //onDown necessary for onFling
    @Override
    public boolean onDown(@NonNull MotionEvent e) {
        return true;
    }

    //SWIPE RECOGNITION
    @Override
    public boolean onFling(@Nullable MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {

        //did motion result in a swipe?
        boolean result = false;

        try {
            //difference in positions of starting and ending points of the motion
            float diffX = e2.getX() - e1.getX();
            float diffY = e2.getY() - e1.getY();

            //if more horizontal distance travelled
            if (Math.abs(diffX) > Math.abs(diffY)){
                //if distance and speed satisfy the threshold parameters
                if (Math.abs(diffX) > DISTANCE_THRESHOLD && Math.abs(velocityX) > VELOCITY_THRESHOLD){
                    //if difference is positive call right handler
                    if(diffX > 0){
                        onSwipeRight();
                    }
                    //if negative call left handler
                    else {
                        onSwipeLeft();
                    }
                    result = true;
                }
            }
            //if more vertical distance travelled
            else {
                //if distance and speed satisfy the threshold parameters
                if (Math.abs(diffY) > DISTANCE_THRESHOLD && Math.abs(velocityY) > VELOCITY_THRESHOLD){
                    //if difference is positive call down handler
                    if (diffY > 0){
                        onSwipeDown();
                    }
                    // if negative call up handler
                    else {
                        onSwipeUp();
                    }
                    result = true;
                }
            }

        }
        catch (Exception exception){
            exception.printStackTrace();
        }
        return result;
    }

    //SWIPE LISTENER ABSTRACTIONS
    protected abstract void onSwipeRight();
    protected abstract void onSwipeLeft();

    protected abstract void onSwipeUp();
    protected abstract void onSwipeDown();
}
