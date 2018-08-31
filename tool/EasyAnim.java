package com.example.william.customview.widget.tool;

import android.support.annotation.FloatRange;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

/**
 * 动画工具类。
 */
public class EasyAnim {


    private static final int sDEFAULT_FIRST_DURATION = 50;
    private static final int sDEFAULT_SECOND_DURATION = 300;
    private static final float sZoomRatio = 1.2f;

    /**
     * 弹性点击
     * @param target 目标view
     */
    public static void elasticAnim(final View target) {
        elasticAnim(target, sZoomRatio, sDEFAULT_FIRST_DURATION, sDEFAULT_SECOND_DURATION);
    }

    /**
     * 弹性点击
     * <p>
     * 弹性缩放动画，放大或缩小View后恢复到原状。
     * 用的Animation实现。
     *
     * @param target 目标View
     * @param ratio  缩放比例，ratio>1时为弹性放大，ratio<1时为弹性缩小。
     */
    private static void elasticAnim(final View target, @FloatRange(from = 0) float ratio, int firstDuration, int secondDuration) {
        //第二阶段的动画。
        final Animation second = new ScaleAnimation(ratio, 1f, ratio, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5F);
        second.setDuration(secondDuration);
        //第一阶段的动画。
        Animation first = new ScaleAnimation(1f, ratio, 1f, ratio, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        first.setDuration(firstDuration);
        first.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                target.startAnimation(second);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        target.startAnimation(first);
    }
}
