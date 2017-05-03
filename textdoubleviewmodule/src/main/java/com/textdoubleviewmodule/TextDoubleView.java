package com.textdoubleviewmodule;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.TextView;


/**
 * Created by Administrator on 2015/9/24.
 * 双击textView
 */
public class TextDoubleView extends TextView {
    private GestureDetector mGesture;
    private OnDoubleTapListener onDoubleTapListener;

    public TextDoubleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ongid(context, attrs);
    }

    public TextDoubleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        ongid(context, attrs);

    }

    public TextDoubleView setOnDoubleTapListener(OnDoubleTapListener onDoubleTapListener) {
        this.onDoubleTapListener = onDoubleTapListener;
        return this;
    }

    private void ongid(Context context, AttributeSet attrs) {
        TypedArray array = getContext().obtainStyledAttributes(attrs,
                R.styleable.TextDoubleView);
        mGesture = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                if (onDoubleTapListener != null) {
                    onDoubleTapListener.onDoubleTap(TextDoubleView.this);
                }
                return super.onDoubleTap(e);
            }

            @Override
            public void onLongPress(MotionEvent e) {

                super.onLongPress(e);
            }

        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGesture.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    public interface OnDoubleTapListener {
        void onDoubleTap(TextDoubleView myButton);
    }

}
