package com.hanamiyaakira.flowchartcore.virtualview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

import com.hanamiyaakira.flowchartcore.R;
import com.hanamiyaakira.flowchartcore.utils.ABAppUtil;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

/**
 * @Author Benjen Masters
 * @Date 2019/3/11
 */
public abstract class OvalView extends BaseView implements VirtualViewEntrance {
    public OvalView(Context context) {
        super(context);
        init();
    }


    public OvalView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public OvalView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    protected void init() {
        pointViewHeightDp = CIRCLE_POINT_RADIUS_DP * 2;
        pointViewWidthDp = CIRCLE_POINT_RADIUS_DP * 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setStyle(Paint.Style.FILL);
        int insideColor = R.color.flow_chart_inside;
        //如果高亮, 就给高亮颜色
        if (isHighLight()) {
            insideColor = R.color.flow_chart_high_light;
        }
        paint.setColor(ContextCompat.getColor(getContext(), insideColor));
        paint.setAntiAlias(true);
        canvas.drawCircle(lpoint, ABAppUtil.dip2px(getContext(), CENTER_Y_DP),
                ABAppUtil.dip2px(getContext(), CIRCLE_POINT_RADIUS_DP), paint);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(ABAppUtil.dip2px(getContext(), SHAPE_STROKE_WIDTH_DP));
        //画节点的外边框
        paint.setColor(ContextCompat.getColor(getContext(), R.color.flow_chart_shape));
        canvas.drawCircle(lpoint, ABAppUtil.dip2px(getContext(), CENTER_Y_DP),
                ABAppUtil.dip2px(getContext(), CIRCLE_POINT_RADIUS_DP), paint);

        //画缘结线
/*        if (isDirectlyPointToMe()) {
            drawTopTiedLine(canvas);
        }
        if (isDirectlyPointToNext()) {
            drawBottomTiedLine(canvas);
        }*/

    }
}
