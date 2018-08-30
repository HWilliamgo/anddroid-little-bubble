package com.example.william.customview.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.support.annotation.Size;
import android.util.AttributeSet;
import android.view.View;

public class SpiderView extends View {
    public SpiderView(Context context) {
        this(context, null);
    }

    public SpiderView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpiderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private Paint mRadarPaint;//用于绘制网格
    private Paint mValuePaint;//用于绘制结果图

    private static final int ANGLE_COUNT = 6;//6边形
    private static final float NEST_COUNT = 6;//嵌套几层多边形。
    private static final float ANGLE = (float) (Math.PI * 2 / ANGLE_COUNT);//单个角的角度

    private void init() {
        mRadarPaint = new Paint();
        mRadarPaint.setStyle(Paint.Style.STROKE);
        mRadarPaint.setColor(Color.BLACK);
        mRadarPaint.setStrokeWidth(3);

        mValuePaint = new Paint();
        mValuePaint.setColor(Color.BLUE);
        mValuePaint.setStyle(Paint.Style.FILL);
    }

    private float mRadius;//最外层半径
    //中心点坐标
    private int mCenterX;
    private int mCenterY;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mRadius = Math.min(w, h) / 2 * 0.9f;
        //中心坐标
        mCenterX = w / 2;
        mCenterY = h / 2;
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawPolygon(canvas);
        drawLines(canvas);
        drawRegion(canvas);
    }

    //绘制多边形
    private void drawPolygon(Canvas canvas) {
        Path path = new Path();
        for (int i = 1; i <= NEST_COUNT; i++) {//中心点不用绘制
            float radius = mRadius * i / NEST_COUNT;//当前嵌套的半径
            path.reset();
            for (int j = 0; j < ANGLE_COUNT; j++) {
                if (j == 0) {
                    path.moveTo(mCenterX + radius, mCenterY);
                } else {
                    float x = (float) (mCenterX + radius * Math.cos(ANGLE * j));
                    float y = (float) (mCenterY + radius * Math.sin(ANGLE * j));
                    path.lineTo(x, y);
                }
            }
            path.close();
            canvas.drawPath(path, mRadarPaint);
        }
    }

    private void drawLines(Canvas canvas) {
        Path path = new Path();
        for (int i = 0; i < ANGLE_COUNT; i++) {
            path.reset();
            path.moveTo(mCenterX, mCenterY);
            float x = mCenterX + (float) (mRadius * Math.cos(i * ANGLE));
            float y = mCenterY + (float) (mRadius * Math.sin(i * ANGLE));
            path.lineTo(x, y);
            canvas.drawPath(path, mRadarPaint);
        }
    }

    private static final int[] DEFAULT_DATA = new int[]{2, 5, 1, 6, 4, 5};
    private int mData[] = DEFAULT_DATA;
    private static final float DEFAULT_CIRCLE_RADIUS = 15;
    private float mCircleRadius = DEFAULT_CIRCLE_RADIUS;

    private void drawRegion(Canvas canvas) {
        Path path = new Path();
        mValuePaint.setAlpha(127);
        for (int i = 0; i < ANGLE_COUNT; i++) {
            double percent = mData[i] / NEST_COUNT;
            float x = (float) (mCenterX + mRadius * Math.cos(ANGLE * i) * percent);
            float y = (float) (mCenterY + mRadius * Math.sin(ANGLE * i) * percent);

            if (i == 0) {
                path.moveTo(x, mCenterY);
            } else {
                path.lineTo(x, y);
            }
            canvas.drawCircle(x, y, mCircleRadius, mValuePaint);
        }
        mValuePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawPath(path, mValuePaint);
    }

    public SpiderView setContentColor(int color) {
        mValuePaint.setColor(color);
        return this;
    }

    public SpiderView setLineWidth(int pixel) {
        mRadarPaint.setStrokeWidth(pixel);
        return this;
    }

    public SpiderView setCircleRadius(int radius) {
        this.mCircleRadius = radius;
        return this;
    }

    /**
     * 设置数据
     *
     * @param data 长度为6，每个元素的取值范围为[1,6]的数组，由注解保证了参数检查。
     * @return this
     */
    public SpiderView setData(@Size(6) @IntRange(from = 1, to = 6) int[] data) {
        this.mData = data;
        return this;
    }


}
