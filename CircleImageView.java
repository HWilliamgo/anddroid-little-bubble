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

public class CircleImageView extends View {
    public CircleImageView(Context context) {
        this(context, null);
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private Bitmap mBitmap;
    private Paint mPaint;
    private Path mPath;

    private void init() {
        setLayerType(LAYER_TYPE_SOFTWARE, mPaint);
        BitmapFactory.Options options = new BitmapFactory.Options();
        mBitmap = BitmapHelper.decodeBitmapFromResource(getResources(), R.drawable.aaa, 200, 200);
        mPaint = new Paint();
        mPath = new Path();

        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();
        mPath.addCircle(width / 2, height / 2, width / 2, Path.Direction.CCW);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.clipPath(mPath);
        canvas.drawBitmap(mBitmap, 0, 0, mPaint);
        canvas.restore();
    }
}
