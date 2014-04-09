package com.vravindranath.resizeguidesample;

import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends FragmentActivity implements
        ViewEventListener, View.OnClickListener {

    private SelectionView mSelectionView;
    private CustomImageView mCustomImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Inflate the resize guide view

        findViewById(R.id.rotate_plus_15).setOnClickListener(this);
        findViewById(R.id.rotate_minus_15).setOnClickListener(this);

        mCustomImageView = (CustomImageView) findViewById(R.id.image_view);
        mCustomImageView.setViewEventListener(this);

        mSelectionView = (SelectionView) findViewById(R.id.selection_view);
        mSelectionView.setViewEventListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private RectF calculateSelectionRect(View selectedView) {
        RectF selectedViewRect = new RectF();
        selectedViewRect.set(selectedView.getX(), selectedView.getY(),
                (selectedView.getX() + selectedView.getWidth()),
                (selectedView.getY() + selectedView.getHeight()));
        return selectedViewRect;
    }

    @Override
    public void onResize(float dx, float dy, SelectionView.KnobTypes knobTypes) {
        //Resize CustomImageView
        mCustomImageView.resize(dx, dy, knobTypes);
    }

    @Override
    public void onTouchMove(float dx, float dy) {
        mSelectionView.setSelectionRect(calculateSelectionRect(mCustomImageView));
    }

    @Override
    public void onTouchUp(RectF viewRect) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rotate_plus_15:
                setRotatePlus90();
                break;

            case R.id.rotate_minus_15:
                setRotateMinus15();
                break;

            default:
                break;
        }
    }

    private void setRotatePlus90() {
        View selectedview = mCustomImageView;
        float angle;
        if(selectedview != null) {
            angle = selectedview.getRotation();
            angle = (angle + 15) % 360;
            selectedview.setRotation(angle);
            mSelectionView.setRotation(angle);
        }
    }

    private void setRotateMinus15() {
        View selectedview = mCustomImageView;
        float angle;
        if(selectedview != null) {
            angle = selectedview.getRotation();
            angle = (angle - 15) % 360;
            selectedview.setRotation(angle);
            mSelectionView.setRotation(angle);
        }
    }
}
