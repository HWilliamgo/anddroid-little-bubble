package com.example.william.customview.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.william.customview.widget.tool.BitmapHelper;
import com.example.william.customview.R;

/**
 * 圆形图片View。
 * TODO:1. 有点锯齿。 2. 没有设置padding，导致padding在LayoutParams中无效。
 */
public class CircleImageView extends View {
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

    private void setPath() {
        mPath.reset();
        mPath.addCircle(w / 2, h / 2, w / 2, Path.Direction.CCW);
    }

    private void initPaint() {
        setLayerType(LAYER_TYPE_SOFTWARE, mPaint);
        mPaint.setAntiAlias(true);
    }


    private Bitmap mBitmap;
    private Paint mPaint = new Paint();
    private Path mPath = new Path();
    private int w;
    private int h;
    private int bitmapW = 0;
    private int bitmapH = 0;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int ws = MeasureSpec.getSize(widthMeasureSpec);
        int hs = MeasureSpec.getSize(heightMeasureSpec);
        int wm = MeasureSpec.getMode(widthMeasureSpec);
        int hm = MeasureSpec.getMode(heightMeasureSpec);
        //如果子view是wrap_content，那么view就设置成bitmap的大小。
        if (wm == MeasureSpec.AT_MOST) {
            ws = bitmapW;
        }
        if (hm == MeasureSpec.AT_MOST) {
            hs = bitmapH;
        }
        int resultW = MeasureSpec.makeMeasureSpec(ws, wm);
        int resultH = MeasureSpec.makeMeasureSpec(hs, hm);

        super.onMeasure(resultW, resultH);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.w = w;
        this.h = h;
        setPath();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mBitmap==null){
            return;
        }
        canvas.clipPath(mPath);
        canvas.drawBitmap(mBitmap, 0, 0, mPaint);
    }

    public void setImage(Bitmap bitmap) {
        mBitmap = bitmap;
        bitmapH = mBitmap.getHeight();
        bitmapW = mBitmap.getWidth();

        requestLayout();
    }
}
