package com.example.william.customview.widget.tool;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.style.LineHeightSpan;

public class BitmapHelper {
    public static Bitmap decodeBitmapFromResource(Resources res, int resId, int w, int h) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        int outHeight = options.outHeight;
        int outWidth = options.outWidth;
        int inSampleSize = 1;
        if (outHeight > h || outWidth > w) {
            int heightRatio = Math.round((float) outHeight / (float) h);
            int widthRatio = Math.round((float) outWidth / (float) w);
            inSampleSize = heightRatio > widthRatio ? heightRatio : widthRatio;
        }

        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }
}
