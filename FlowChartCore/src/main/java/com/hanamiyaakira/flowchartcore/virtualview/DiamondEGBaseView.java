package com.hanamiyaakira.flowchartcore.virtualview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.hanamiyaakira.flowchartcore.R;
import com.hanamiyaakira.flowchartcore.utils.ABAppUtil;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

/**
 * @Author Benjen Masters
 * @Date 2019/3/8
 */
public abstract class DiamondEGBaseView extends BaseView {

    public DiamondEGBaseView(Context context) {
        super(context);
    }

    public DiamondEGBaseView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DiamondEGBaseView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //菱形
        int insideColor = R.color.flow_chart_inside;
        //如果高亮
        if (isHighLight()) {
            insideColor = R.color.flow_chart_high_light;
        }
        paint.setColor(ContextCompat.getColor(getContext(), insideColor));
        paint.setStyle(Paint.Style.FILL);
        Path diamondPath = new Path();
        diamondPath.moveTo(lpoint, ABAppUtil.dip2px(getContext(), TIED_LINE_HEIGHT_DP));
        diamondPath.lineTo(lpoint + ABAppUtil.dip2px(getContext(), RECTANGLE_UT_WIDTH_DP / 2),
                ABAppUtil.dip2px(getContext(), TIED_LINE_HEIGHT_DP + RECTANGLE_UT_HEIGHT_DP / 2));
        diamondPath.lineTo(lpoint, ABAppUtil.dip2px(getContext(), TIED_LINE_HEIGHT_DP + RECTANGLE_UT_HEIGHT_DP));
        diamondPath.lineTo(lpoint - ABAppUtil.dip2px(getContext(), RECTANGLE_UT_WIDTH_DP / 2),
                ABAppUtil.dip2px(getContext(), TIED_LINE_HEIGHT_DP + RECTANGLE_UT_HEIGHT_DP / 2));
        diamondPath.close();
        canvas.drawPath(diamondPath, paint);

        //菱形边框
        paint.setColor(ContextCompat.getColor(getContext(), R.color.flow_chart_shape));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(ABAppUtil.dip2px(getContext(), SHAPE_STROKE_WIDTH_DP));
        canvas.drawPath(diamondPath, paint);

        /**
         * 画线上的文字
         */
        if (!TextUtils.isEmpty(getDirectlyAppendedText())) {
            setTextPaint();
            canvas.drawText(getDirectlyAppendedText(), lpoint, ABAppUtil.dip2px(getContext(), MODEL_HEIGHT_DP)
                    - paint.getFontMetrics().descent, paint);
        }

    }

}
