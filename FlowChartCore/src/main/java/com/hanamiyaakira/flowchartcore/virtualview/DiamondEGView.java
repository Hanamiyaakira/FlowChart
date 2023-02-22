package com.hanamiyaakira.flowchartcore.virtualview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.hanamiyaakira.flowchartcore.utils.ABAppUtil;

import androidx.annotation.Nullable;

/**
 * @Author Benjen Masters
 * @Date 2019/3/8
 */
public class DiamondEGView extends DiamondEGBaseView {

    public DiamondEGView(Context context) {
        super(context);
    }

    public DiamondEGView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DiamondEGView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void measureComponent(int widthMeasureSpec, int heightMeasureSpec) {
        this.measure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void comLayout(int l, int t, int r, int b) {
        this.layout(l, t, r, b);
    }

    @Override
    public void onComMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void onComLayout(boolean changed, int l, int t, int r, int b) {
        this.onLayout(changed, l, t, r, b);
    }

    @Override
    public int getComMeasuredWidth() {
        return this.getMeasuredWidth();
    }

    @Override
    public int getComMeasuredHeight() {
        return this.getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //右下拐角


        //左下拐角线

        //文字
        setTextPaint();
        /**
         *"通过"的文字是活的, 因为菱形框可能是其他文字
         */
//        canvas.drawText("通过", lpoint, ABAppUtil.dip2px(getContext(), MODEL_HEIGHT_DP)
//                - paint.getFontMetrics().descent, paint);
        /**
         * "不通过"的文字先写死
         */
        canvas.drawText("不通过", leftCornerX, ABAppUtil.dip2px(getContext(), MODEL_HEIGHT_DP
                - paint.getFontMetrics().descent), paint);
    }
}
