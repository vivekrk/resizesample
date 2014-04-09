package com.vravindranath.resizeguidesample;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Created by vivek on 14/3/14.
 */
public class CustomImageView extends ImageView {

    private float mPrevTouchX;
    private float mPrevTouchY;

    private ViewEventListener mViewEventListener = null;
    private int mKnobRadius;

    public CustomImageView(Context context) {
        super(context);
        init(context);
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public void setViewEventListener(ViewEventListener viewEventListener) {
        mViewEventListener = viewEventListener;
    }

    private void init(Context context) {
        mKnobRadius = context.getResources().getDimensionPixelSize(R.dimen.knob_radius);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getRawX();
        float touchY = event.getRawY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPrevTouchX = touchX;
                mPrevTouchY = touchY;
                break;

            case MotionEvent.ACTION_MOVE:
                float dx = touchX - mPrevTouchX;
                float dy = touchY - mPrevTouchY;
                mPrevTouchX = touchX;
                mPrevTouchY = touchY;
                setTranslationX(getTranslationX() + dx);
                setTranslationY(getTranslationY() + dy);
                mViewEventListener.onTouchMove(dx, dy);
                break;

            case MotionEvent.ACTION_UP:
                RectF viewRect = new RectF();
                viewRect.set(getX(), getY(),
                        (getX() + getWidth()), (getY() + getHeight()));
                mViewEventListener.onTouchUp(viewRect);
                break;

            default:
                break;
        }
        return true;
    }

    public void resize(float dx, float dy, SelectionView.KnobTypes selectedKnob) {
        float newDx = dx;
        float newDy = dy;
        FrameLayout.LayoutParams params;
        float viewWidth, viewHeight, ratio, newWidth, newHeight;
        viewWidth = getWidth();
        viewHeight = getHeight();
        ratio = 1;
        switch (selectedKnob) {
            case eLeftTop:
                newWidth = viewWidth - dx;
                newHeight = viewHeight - dy;

                if (newWidth <= mKnobRadius * 4 || newHeight <= mKnobRadius * 4) {
                    break;
                }

                if (viewWidth > viewHeight) {
                    if (viewHeight != 0) {
                        ratio = viewWidth / viewHeight;
                    }
                    newWidth = newHeight * ratio;
                } else {
                    if (viewWidth != 0) {
                        ratio = viewHeight / viewWidth;
                    }
                    newHeight = newWidth * ratio;
                }
                params = new FrameLayout.LayoutParams(Math.round(newWidth),
                        Math.round(newHeight));
                setLayoutParams(params);


                newDx = Math.round(newWidth) - Math.round(viewWidth);
                newDy = Math.round(newHeight) - Math.round(viewHeight);

                setTranslationX(getTranslationX() - newDx);
                setTranslationY(getTranslationY() - newDy);
                break;

            case eTopRight:
                newWidth = viewWidth + dx;
                newHeight = viewHeight - dy;

                if (newWidth <= mKnobRadius * 4 || newHeight <= mKnobRadius * 4) {
                    break;
                }

                if (viewWidth > viewHeight) {
                    if (viewHeight != 0) {
                        ratio = viewWidth / viewHeight;
                    }
                    newWidth = newHeight * ratio;
                } else {
                    if (viewWidth != 0) {
                        ratio = viewHeight / viewWidth;
                    }
                    newHeight = newWidth * ratio;
                }
                params = new FrameLayout.LayoutParams(Math.round(newWidth),
                        Math.round(newHeight));
                setLayoutParams(params);
                newDx = Math.round(newWidth) - Math.round(viewWidth);
                newDy = Math.round(newHeight) - Math.round(viewHeight);
                setTranslationY(getTranslationY() - newDy);
                break;

            case eRightBottom:
                newWidth = viewWidth + dx;
                newHeight = viewHeight + dy;

                if (newWidth <= mKnobRadius * 4 || newHeight <= mKnobRadius * 4) {
                    break;
                }

                if (viewWidth > viewHeight) {
                    if (viewHeight != 0) {
                        ratio = viewWidth / viewHeight;
                    }
                    newWidth = newHeight * ratio;
                } else {
                    if (viewWidth != 0) {
                        ratio = viewHeight / viewWidth;
                    }
                    newHeight = newWidth * ratio;
                }
                params = new FrameLayout.LayoutParams(Math.round(newWidth),
                        Math.round(newHeight));
                setLayoutParams(params);
                break;

            case eLeftBottom:
                newWidth = viewWidth - dx;
                newHeight = viewHeight + dy;

                if (newWidth <= mKnobRadius * 4 || newHeight <= mKnobRadius * 4) {
                    break;
                }

                if (viewWidth > viewHeight) {
                    if (viewHeight != 0) {
                        ratio = viewWidth / viewHeight;
                    }
                    newWidth = newHeight * ratio;
                } else {
                    if (viewWidth != 0) {
                        ratio = viewHeight / viewWidth;
                    }
                    newHeight = newWidth * ratio;
                }
                params = new FrameLayout.LayoutParams(Math.round(newWidth),
                        Math.round(newHeight));
                setLayoutParams(params);
                newDx = Math.round(newWidth) - Math.round(viewWidth);
                newDy = Math.round(newHeight) - Math.round(viewHeight);

                setTranslationX(getTranslationX() - newDx);
                break;

            default:
                break;
        }
    }
}
