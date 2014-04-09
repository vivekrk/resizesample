package com.vravindranath.resizeguidesample;

import android.graphics.RectF;

/**
 * Created by vivek on 4/4/14.
 */
public interface ViewEventListener {

    public void onResize(float dx, float dy, SelectionView.KnobTypes knobTypes);

    public void onTouchMove(float dx, float dy);

    public void onTouchUp(RectF viewRect);
}
