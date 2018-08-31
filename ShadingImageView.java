package com.example.william.customview.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Region;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.william.customview.R;

public class ShadingImageView extends View {
    public ShadingImageView(Context context) {
        this(context, null);
    }

    public ShadingImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShadingImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private Delegate mDelegate = new Delegate();

    private static final int sDEFAULT_CLIP_HEIGHT = 30;
    private static final int sDEFAULT_SPEED = 10;

    private Bitmap mBitmap;//图片
    private Region mRgn;//剪裁区域
    private Rect left;//左边的剪裁区域
    private Rect right;//右边的剪裁区域
    private int width;//图片宽度
    private int height;//图片高度
    private int mClipHeight;//每个Region的剪裁高度
    private int mClipWidth;//每个Region的剪裁宽度
    private Path mPath;//辅助Region的Path
    private Paint paint;
    private int mSpeed;//渐变速度

    private void init() {
        setLayerType(LAYER_TYPE_SOFTWARE, null);

        mClipHeight = sDEFAULT_CLIP_HEIGHT;
        mSpeed = sDEFAULT_SPEED;

        mRgn = new Region();

        left = new Rect();
        right = new Rect();
        mPath = new Path();
        paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mBitmap == null) {
            return;
        }
        mRgn.setEmpty();
        mPath.reset();
        int count = 0;
        while (count * mClipHeight <= height) {
            if (count % 2 == 0) {
                left.set(0, count * mClipHeight, mClipWidth, (count + 1) * mClipHeight);
                mRgn.union(left);
            } else {
                right.set(width - mClipWidth, count * mClipHeight, width, (count + 1) * mClipHeight);
                mRgn.union(right);
            }
            count++;
        }

        mRgn.getBoundaryPath(mPath);
        canvas.clipPath(mPath);
        canvas.drawBitmap(mBitmap, 0, 0, paint);
        if (mClipWidth > width) {
            return;
        }
        mClipWidth += mSpeed;
        invalidate();
    }

    public Delegate getDelegate() {
        return mDelegate;
    }

    public class Delegate {

        public void setSpeed(@IntRange(from = 0, to = 30) int speed) {
            mSpeed = speed;
        }

        public void setClipHeight(@IntRange(from = 0) int clipHeight) {
            mClipHeight = clipHeight;
        }

        public void setBitmap(Bitmap bitmap) {
            mBitmap = bitmap;
            height = bitmap.getHeight();
            width = bitmap.getWidth();

            invalidate();
        }
    }
}
