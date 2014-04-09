package com.vravindranath.resizeguidesample;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vivek on 14/3/14.
 */
public class SelectionView extends View {

    private Paint mKnobPaint;
    private RectF mSelectionRect;
    private int mKnobRadius;
    private HashMap<KnobTypes, Region> mKnobRegions;
    private float mPrevTouchX;
    private float mPrevTouchY;

    private String TAG = SelectionView.class.getSimpleName();
    private KnobTypes mTouchedKnobIndex;
    private boolean mIsResizing;

    private ViewEventListener mViewEventListener;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        Log.e(TAG, "onTouchEvent");
        float touchX = event.getX();
        float touchY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPrevTouchX = touchX;
                mPrevTouchY = touchY;
                if(isTouchOnKnob(new PointF(touchX, touchY))) {
                    mIsResizing = true;
//                    Log.e(TAG, "knob clicked");
                    return mIsResizing;
                }
                break;

            case MotionEvent.ACTION_MOVE:
                float dx = touchX - mPrevTouchX;
                float dy = touchY - mPrevTouchY;
                mPrevTouchX = touchX;
                mPrevTouchY = touchY;
//                Log.e(TAG, "(dx, dy): " + "(" + dx + ", " + dy + ")");
                if (mIsResizing) {
                    mViewEventListener.onResize(dx, dy, mTouchedKnobIndex);
                }
                break;

            case MotionEvent.ACTION_UP:
                mIsResizing = false;
                break;

            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    public SelectionView(Context context) {
        super(context);
        init(context);
    }

    public SelectionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SelectionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mKnobPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mKnobPaint.setStyle(Paint.Style.STROKE);
        mKnobPaint.setStrokeWidth(4);
        mKnobPaint.setColor(Color.BLUE);

        mKnobRadius = context.getResources().getDimensionPixelSize(R.dimen.knob_radius);
        mKnobRegions = new HashMap<KnobTypes, Region>();
    }

    /**
     * Use for debugging.
     * @param canvas
     */
    private void drawKnobRegions(Canvas canvas) {
        for (Map.Entry<SelectionView.KnobTypes, Region> knobTypesRegionEntry : mKnobRegions.entrySet()) {
            Map.Entry pairs = (Map.Entry) knobTypesRegionEntry;
            Region region = (Region) pairs.getValue();
            Rect bounds = region.getBounds();
            canvas.drawRect(bounds, mKnobPaint);
        }
    }

    private void drawResizeKnobs(Canvas canvas) {
        if(mSelectionRect == null)
            return;
        canvas.drawCircle(mSelectionRect.left, mSelectionRect.top, mKnobRadius, mKnobPaint);
        canvas.drawCircle(mSelectionRect.left + mSelectionRect.width(),
                mSelectionRect.top, mKnobRadius, mKnobPaint);
        canvas.drawCircle(mSelectionRect.left + mSelectionRect.width(),
                mSelectionRect.top + mSelectionRect.height(), mKnobRadius, mKnobPaint);
        canvas.drawCircle(mSelectionRect.left,
                mSelectionRect.top + mSelectionRect.height(), mKnobRadius, mKnobPaint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mSelectionRect == null)
            return;
        drawResizeKnobs(canvas);
        drawSelectionRect(canvas);
//        generateKnobRegions();
        drawKnobRegions(canvas);
    }

    private void drawSelectionRect(Canvas canvas) {
        canvas.drawRect(mSelectionRect, mKnobPaint);
    }

    private Region generateRegionForCircle(float x, float y, float radius) {
        Path path = new Path();
        path.addCircle(x, y, radius, Path.Direction.CW);
        RectF rectF = new RectF();
        path.computeBounds(rectF, true);
        Rect rect = new Rect();
        rectF.roundOut(rect);
        return new Region(rect);
    }

    public void setViewEventListener(ViewEventListener viewEventListener) {
        mViewEventListener = viewEventListener;
    }

    public enum KnobTypes {
        eLeftTop,
        eTopRight,
        eRightBottom,
        eLeftBottom
    }

    private void generateKnobRegions() {
        mKnobRegions.put(KnobTypes.eLeftTop,
                generateRegionForCircle(mSelectionRect.left, mSelectionRect.top, mKnobRadius));
        mKnobRegions.put(KnobTypes.eTopRight,
                generateRegionForCircle(mSelectionRect.left
                        + mSelectionRect.width(), mSelectionRect.top, mKnobRadius));
        mKnobRegions.put(KnobTypes.eRightBottom,
                generateRegionForCircle(mSelectionRect.left
                        + mSelectionRect.width(), mSelectionRect.top
                        + mSelectionRect.height(), mKnobRadius));
        mKnobRegions.put(KnobTypes.eLeftBottom,
                generateRegionForCircle(mSelectionRect.left,
                        mSelectionRect.top + mSelectionRect.height(), mKnobRadius));
    }

    private boolean isTouchOnKnob(PointF touchPoint) {
        for (Map.Entry<SelectionView.KnobTypes, Region> knobTypesRegionEntry : mKnobRegions.entrySet()) {
            Map.Entry pairs = (Map.Entry) knobTypesRegionEntry;
            Region region = (Region) pairs.getValue();
            if (region.contains((int) touchPoint.x, (int) touchPoint.y)) {
                mTouchedKnobIndex = (SelectionView.KnobTypes) pairs.getKey();
                Log.e(TAG, "mTouchedKnobIndex: " + mTouchedKnobIndex);
                return true;
            }
        }
        return false;
    }

    public void setSelectionRect(RectF selectionRect) {
        mSelectionRect = new RectF();
        mSelectionRect.set(mKnobRadius, mKnobRadius,
                selectionRect.width() + mKnobRadius,
                selectionRect.height() + mKnobRadius);
        setTranslationX((selectionRect.left - mKnobRadius));
        setTranslationY((selectionRect.top - mKnobRadius));
        FrameLayout.LayoutParams params = new FrameLayout
                .LayoutParams(Math.round(selectionRect.width() + mKnobRadius * 2),
                Math.round(selectionRect.height() + mKnobRadius * 2));
        setLayoutParams(params);
        generateKnobRegions();
    }
}
