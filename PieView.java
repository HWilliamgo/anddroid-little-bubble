package com.example.william.customview.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import android.graphics.RectF;
import android.support.annotation.FloatRange;
import android.support.annotation.Nullable;

import android.support.v4.util.ArrayMap;
import android.util.AttributeSet;
import android.view.View;

import java.util.Collection;
import java.util.Random;

public class PieView extends View {
    public PieView(Context context) {
        this(context, null);
    }

    public PieView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PieView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private ArrayMap<String, Float> mMap = new ArrayMap<String, Float>();
    private Paint mPaint = new Paint();
    private int w;
    private int h;
    private RectF mRectF = new RectF();
    //外界操作饼图的代理类
    private Delegate mDelegate = new Delegate();
    private Random mRandom = new Random();

    private void init() {
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.w = w;
        this.h = h;
        int r = w > h ? h : w;
        mRectF.set(0, 0, r, r);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Collection<Float> collection = mMap.values();
        float total = 0;
        for (Float value : collection) {
            total = total + value;
        }
        float currentAngle = 0;
        for (Float value : collection) {
            float percent = value / total;//每一个值占总值的比例
            float sweepAngle = 360f * percent;//扇形角度
            mPaint.setColor(getRandomColor());//设置画笔随机颜色
            canvas.drawArc(mRectF, currentAngle, sweepAngle, true, mPaint);
            currentAngle = currentAngle + sweepAngle;
        }
    }

    //获取随机颜色
    private int getRandomColor() {
        int a = mRandom.nextInt(255);
        int r = mRandom.nextInt(255);
        int g = mRandom.nextInt(255);
        int b = mRandom.nextInt(255);
        return Color.argb(a, r, g, b);
    }

    public Delegate getDelegate() {
        return mDelegate;
    }

    public class Delegate {
        public Delegate add(String name, @FloatRange(from = 0f, to = 100f) float value) {
            mMap.put(name, value);
            return this;
        }

        public void invalidate() {
            PieView.this.invalidate();
        }

        public void postInvalidate() {
            PieView.this.postInvalidate();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mDelegate = null;
    }
}
