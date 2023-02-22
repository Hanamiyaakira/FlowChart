package com.hanamiyaakira.flowchartcore.virtualview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;


import com.hanamiyaakira.flowchartcore.R;
import com.hanamiyaakira.flowchartcore.utils.ABAppUtil;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

/**
 * @Author Benjen Masters
 * @Date 2019/3/8
 */
public abstract class RectUTBaseView extends BaseView {


    public RectUTBaseView(Context context) {
        super(context);
    }

    public RectUTBaseView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RectUTBaseView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int insideColor = R.color.flow_chart_inside;
        //如果高亮, 就给高亮颜色
        if (isHighLight()) {
            insideColor = R.color.flow_chart_high_light;
        }
        paint.setColor(ContextCompat.getColor(getContext(), insideColor));
        paint.setStyle(Paint.Style.FILL);
        //准备画圆角矩形
        RectF rect = new RectF(
                lpoint - ABAppUtil.dip2px(getContext(), RECTANGLE_UT_WIDTH_DP / 2),
                ABAppUtil.dip2px(getContext(), TIED_LINE_HEIGHT_DP),
                lpoint + ABAppUtil.dip2px(getContext(), RECTANGLE_UT_WIDTH_DP / 2),
                ABAppUtil.dip2px(getContext(), RECTANGLE_UT_HEIGHT_DP + TIED_LINE_HEIGHT_DP)
        );
        float rectangleRound = ABAppUtil.dip2px(getContext(), RECTANGLE_ROUND_DP);
        canvas.drawRoundRect(rect, rectangleRound, rectangleRound, paint);
        //圆角矩形外边框
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(ABAppUtil.dip2px(getContext(), SHAPE_STROKE_WIDTH_DP));
        paint.setColor(ContextCompat.getColor(getContext(), R.color.flow_chart_shape));
        canvas.drawRoundRect(rect, rectangleRound, rectangleRound, paint);
    }

    /**
     * 设置文字的画笔
     */
    protected void setTextPaint(TextPaint paint) {
        paint.setStrokeWidth(TEXT_PAINT_STROKE_WIDTH);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(ABAppUtil.dip2px(getContext(), TEXT_SIZE_DP));
        paint.setColor(ContextCompat.getColor(getContext(), R.color.flow_chart_text));
    }
}
