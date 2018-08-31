package com.example.william.customview.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.nfc.Tag;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.william.customview.widget.tool.BitmapHelper;
import com.example.william.customview.R;

/**
 * 圆形图片View。
 * TODO:1. 有点锯齿。
 * TODO 2. 没有设置padding，导致padding在LayoutParams中无效。
 * TODO 3. 宽高都用wrap_content时没有问题，都用确定值也没问题，但是一个确定值一个wrap_content时绘制有点问题。
 */
public class CircleImageView extends View {
    private static final String TAG = "CircleImageView";

    public CircleImageView(Context context) {
        this(context, null);
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initPaint();
    }

    private void initPaint() {
        setLayerType(LAYER_TYPE_SOFTWARE, mPaint);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);
    }

    private Bitmap mBitmap;
    private Paint mPaint = new Paint();
    private Path mPath = new Path();
    private int w;
    private int h;
    private int bitmapW = 0;//图片宽度。在wrap_content下的初始值为0。
    private int bitmapH = 0;//图片高度。在wrap_content下的初始值为0。
    private int oldBitmapW = -1;
    private int oldBitmapH = -1;
    private Rect dst = new Rect();

    private boolean isWidthWrap = false;//标志位
    private boolean isHeightWrap = false;//标志位

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int ws = MeasureSpec.getSize(widthMeasureSpec);
        int hs = MeasureSpec.getSize(heightMeasureSpec);
        int wm = MeasureSpec.getMode(widthMeasureSpec);
        int hm = MeasureSpec.getMode(heightMeasureSpec);
        //如果子view是wrap_content，那么view就设置成bitmap的大小。
        if (wm == MeasureSpec.AT_MOST) {
            ws = bitmapW;
            isWidthWrap = true;
        } else {
            isWidthWrap = false;
        }
        if (hm == MeasureSpec.AT_MOST) {
            hs = bitmapH;
            isHeightWrap = true;
        } else {
            isHeightWrap = false;
        }
        int resultW = MeasureSpec.makeMeasureSpec(ws, wm);
        int resultH = MeasureSpec.makeMeasureSpec(hs, hm);

        super.onMeasure(resultW, resultH);
    }

    private void setPath() {
        mPath.reset();
        mPath.addCircle(w / 2, h / 2, w / 2, Path.Direction.CCW);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.w = w;
        this.h = h;
        dst.set(0, 0, w, h);
        setPath();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mBitmap == null) {
            return;
        }
        canvas.clipPath(mPath);
        canvas.drawBitmap(mBitmap, null, dst, mPaint);
    }

    public void setImage(Bitmap bitmap) {
        mBitmap = bitmap;
        bitmapH = mBitmap.getHeight();
        bitmapW = mBitmap.getWidth();

        if (!isHeightWrap && !isWidthWrap) {
            //如果宽高都是EXACTYL,那么宽高将一直不变，那么直接重绘。
            invalidate();
        } else {
            //如果图片尺寸相等，那么就强行重绘。如果图片尺寸不等，那么就重新布局。
            if (bitmapW == oldBitmapW && bitmapH == oldBitmapH) {
                invalidate();
            } else {
                requestLayout();
            }
        }

        oldBitmapH = bitmapH;
        oldBitmapW = bitmapW;
    }
}
