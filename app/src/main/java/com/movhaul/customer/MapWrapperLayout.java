package com.movhaul.customer;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Created by Salman on 13-03-2017.
 * map wrapper class for if touch map the top and bottom layout will be hide
 *
 */
public class MapWrapperLayout extends FrameLayout {
    interface OnDragListener {
        public void onDrag(MotionEvent motionEvent);
    }
    private OnDragListener mOnDragListener;
    public MapWrapperLayout(Context context) {
        super(context);
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mOnDragListener != null) {
            mOnDragListener.onDrag(ev);
        }
        return super.dispatchTouchEvent(ev);
    }
    public void setOnDragListener(OnDragListener mOnDragListener) {
        this.mOnDragListener = mOnDragListener;
    }
}