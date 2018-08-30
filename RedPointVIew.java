package com.example.william.customview.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class RedPointVIew extends View {
    public RedPointVIew(Context context) {
        this(context, null);
    }

    public RedPointVIew(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RedPointVIew(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mRect = new Rect(100, 10, 300, 100);
    }

    private Paint mPaint;
    private Rect mRect;
    private int mX;
    private int mY;

    @Override
    protected void onDraw(Canvas canvas) {
        if (mRect.contains(mX, mY)) {
            mPaint.setColor(Color.RED);
        } else {
            mPaint.setColor(Color.GREEN);
        }
        canvas.drawRect(mRect, mPaint);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mX = (int) event.getX();
        mY = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                invalidate();
                return true;//如果在ACTION_DOWN不返回true的话，该view将接收不到后续的事件。
            case MotionEvent.ACTION_UP:
                mX = -1;
                mY = -1;
                break;
        }
        postInvalidate();
        return super.onTouchEvent(event);
    }

}
