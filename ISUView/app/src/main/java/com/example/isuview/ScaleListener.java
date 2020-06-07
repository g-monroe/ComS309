package com.example.isuview;

import android.view.ScaleGestureDetector;
/**
 * Modified By Gavin on 10/21/2019.
 */
public abstract class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
    public boolean scaling = false;
    @Override
    public boolean onScale(ScaleGestureDetector scaleGestureDetector){
        onSpread(scaleGestureDetector);
        return true;
    }
    public abstract boolean onSpread(ScaleGestureDetector scaleGestureDetector);
}
