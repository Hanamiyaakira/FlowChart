package com.hanamiyaakira.flowchartcore.virtualview;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;


import com.hanamiyaakira.flowchartcore.R;
import com.hanamiyaakira.flowchartcore.utils.ABAppUtil;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

/**
 * @Author Benjen Masters
 * @Date 2019/3/8
 */
public abstract class BaseView extends View implements VirtualViewEntrance {

    //画笔
    protected Paint paint;
    protected float TEXT_PAINT_STROKE_WIDTH = 0.5f;
    protected float TEXT_SIZE_DP = 13f;
    protected int lpoint;
    protected int rightCornerX;
    protected int rightCornerY;
    protected int leftCornerX;
    protected int leftCornerY;
    protected int arcDiameter;
    protected int lineWidth;
    protected int highLight;

    //一个直线下降流程图的12种状态, 构成12位二进制数
    private int lineState;
    private boolean directlyPointToMe;//径直指向我
    private boolean directlyPointToNext;//径直指向其他控件
    private boolean passByLeft;//从我左边经过
    private boolean passByRight;//从我右边经过
    private boolean leftBreakUp;//从左到上指向其他控件
    private boolean leftBreakDown;//从左到下指向其他控件
    private boolean upBreakLeft;//从上打折指向我
    private boolean downBreakLeft;//从下打折指向我
    private boolean rightBreakUp;//从右到上指向其他控件
    private boolean rightBreakDown;//从右到下指向其他控件
    private boolean upBreakRight;//从上打折指向我
    private boolean downBreakRight;//从下打折指向我
    protected float pointViewWidthDp = RECTANGLE_UT_WIDTH_DP;
    protected float pointViewHeightDp = RECTANGLE_UT_HEIGHT_DP;
    //线上的文字
    private String directlyAppendedText;
    private String leftLineAppendix;
    private String rightLineAppendix;


    public BaseView(Context context) {
        super(context);
    }

    public BaseView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //基本变量
        int width = getWidth();
        /**
         * 中间的X值, 相对坐标
         */
        lpoint = width / 2;
        //画笔
        paint = new Paint();
        paint.setAntiAlias(true);
        lineWidth = ABAppUtil.dip2px(getContext(), LINE_WIDTH_DP);
        //拐角
        arcDiameter = ABAppUtil.dip2px(getContext(), ARC_DIAMETER_DP);
        rightCornerX = lpoint +
                ABAppUtil.dip2px(getContext(), RECTANGLE_UT_WIDTH_DP / 2 + LINE_POINT_DISTANCE_DP);
        rightCornerY =
                ABAppUtil.dip2px(getContext(), TIED_LINE_HEIGHT_DP + RECTANGLE_UT_HEIGHT_DP / 2);

        leftCornerX = lpoint -
                ABAppUtil.dip2px(getContext(), RECTANGLE_UT_WIDTH_DP / 2 + LINE_POINT_DISTANCE_DP);
        leftCornerY = rightCornerY;

        if (getDarkModeStatus(getContext())) {
            paint.setColor(ContextCompat.getColor(getContext(), android.R.color.black));
        }else{
            paint.setColor(ContextCompat.getColor(getContext(), android.R.color.white));
        }
        canvas.drawRect(0, 0, getRight(), getBottom(), paint);

        //绘制线
        //上线
        if ((lineState & DIRECTLY_POINT_TO_ME_MASK) > 0) {
            drawTopTiedLine(canvas);
        }
        //下线
        if ((lineState & DIRECTLY_POINT_TO_NEXT_MASK) > 0) {
            drawBottomTiedLine(canvas);
        }
        //由下从左指向我
        if ((lineState & DOWN_BREAK_LEFT_MASK) > 0) {
            drawLeftArrow(canvas, pointViewWidthDp, pointViewHeightDp);
            drawLeftBreakDown(canvas);
        }
        //由下从右指向我
        if ((lineState & DOWN_BREAK_RIGHT_MASK) > 0) {
            drawRightBreakDown(canvas);
            drawRightArrow(canvas, pointViewWidthDp, pointViewHeightDp);
        }
        //由上从左指向我
        if ((lineState & UP_BREAK_LEFT_MASK) > 0) {
            drawLeftArrow(canvas, pointViewWidthDp, pointViewHeightDp);
            drawLeftBreakUp(canvas, pointViewWidthDp);
        }
        //由上从右指向我
        if ((lineState & UP_BREAK_RIGHT_MASK) > 0) {
            drawRightBreakUp(canvas, pointViewWidthDp);
            drawRightArrow(canvas, pointViewWidthDp, pointViewHeightDp);
        }
        //右边线 pass by
        if ((lineState & PASS_BY_RIGHT_MASK) > 0) {
            drawLinePassByRight(canvas);
        }
        //左边线 pass by
        if ((lineState & PASS_BY_LEFT_MASK) > 0) {
            drawLinePassByLeft(canvas);
        }
        //从左向下指向其他
        if ((lineState & LEFT_BREAK_DOWN_MASK) > 0) {
            drawLeftBreakDown(canvas);
        }
        //从右向上指向其他
        if ((lineState & RIGHT_BREAK_UP_MASK) > 0) {
            drawRightBreakUp(canvas, pointViewWidthDp);
        }
        //从左向上指向其他
        if ((lineState & LEFT_BREAK_UP_MASK) > 0) {
            drawLeftBreakUp(canvas, pointViewWidthDp);
        }
        //从右向下指向其他
        if ((lineState & RIGHT_BREAK_DOWN_MASK) > 0) {
            drawRightBreakDown(canvas);
        }

    }

    //画指向我的缘结线
    public void drawTopTiedLine(Canvas canvas) {
        //箭头
        setArrowPaint();
        Path arrowPath = new Path();
        arrowPath.moveTo(lpoint, ABAppUtil.dip2px(getContext(), TIED_LINE_HEIGHT_DP));
        arrowPath.lineTo(lpoint - ABAppUtil.dip2px(getContext(), ARROW_WIDTH_DP / 2),
                ABAppUtil.dip2px(getContext(), TIED_LINE_HEIGHT_DP - ARROW_HEIGHT_DP));
        arrowPath.lineTo(lpoint,
                ABAppUtil.dip2px(getContext(), TIED_LINE_HEIGHT_DP - ARROW_HEIGHT_DP * ARROW_SCALE));
        arrowPath.lineTo(lpoint + ABAppUtil.dip2px(getContext(), ARROW_WIDTH_DP / 2),
                ABAppUtil.dip2px(getContext(), TIED_LINE_HEIGHT_DP - ARROW_HEIGHT_DP));
        arrowPath.close();
        canvas.drawPath(arrowPath, paint);
        //线
        paint.setStrokeWidth(ABAppUtil.dip2px(getContext(), LINE_WIDTH_DP));
        canvas.drawLine(lpoint, 0,
                lpoint, ABAppUtil.dip2px(getContext(), TIED_LINE_HEIGHT_DP), paint);
    }

    /**
     * 设置箭头的颜色, 空心实心
     */
    private void setArrowPaint() {
        paint.setColor(ContextCompat.getColor(getContext(), R.color.flow_chart_line));
        paint.setStyle(Paint.Style.FILL);
    }

    //画左边的箭头
    protected void drawLeftArrow(Canvas canvas, float pointViewWidth, float pointViewHeight) {
        setArrowPaint();
        Path arrow = new Path();
        int rectRightX = lpoint - ABAppUtil.dip2px(getContext(), pointViewWidth / 2);
        int rectRightY =
                ABAppUtil.dip2px(getContext(), TIED_LINE_HEIGHT_DP + pointViewHeight / 2);
        arrow.moveTo(rectRightX, rectRightY);
        arrow.lineTo(rectRightX - ABAppUtil.dip2px(getContext(), ARROW_HEIGHT_DP),
                rectRightY - ABAppUtil.dip2px(getContext(), ARROW_WIDTH_DP / 2));
        arrow.lineTo(rectRightX - ABAppUtil.dip2px(getContext(), ARROW_HEIGHT_DP * ARROW_SCALE),
                rectRightY);
        arrow.lineTo(rectRightX - ABAppUtil.dip2px(getContext(), ARROW_HEIGHT_DP),
                rectRightY + ABAppUtil.dip2px(getContext(), ARROW_WIDTH_DP / 2));
        arrow.close();
        canvas.drawPath(arrow, paint);
    }


    //画右边的箭头
    protected void drawRightArrow(Canvas canvas, float pointViewWidth, float pointViewHeight) {
        setArrowPaint();
        Path arrow = new Path();
        int rectRightX = lpoint + ABAppUtil.dip2px(getContext(), pointViewWidth / 2);
        int rectRightY =
                ABAppUtil.dip2px(getContext(), TIED_LINE_HEIGHT_DP + pointViewHeight / 2);
        arrow.moveTo(rectRightX, rectRightY);
        arrow.lineTo(rectRightX + ABAppUtil.dip2px(getContext(), ARROW_HEIGHT_DP),
                rectRightY - ABAppUtil.dip2px(getContext(), ARROW_WIDTH_DP / 2));
        arrow.lineTo(rectRightX + ABAppUtil.dip2px(getContext(), ARROW_HEIGHT_DP * ARROW_SCALE),
                rectRightY);
        arrow.lineTo(rectRightX + ABAppUtil.dip2px(getContext(), ARROW_HEIGHT_DP),
                rectRightY + ABAppUtil.dip2px(getContext(), ARROW_WIDTH_DP / 2));
        arrow.close();
        canvas.drawPath(arrow, paint);
    }

    //画由我指出的缘结线
    protected void drawBottomTiedLine(Canvas canvas) {
        setLinePaint();
        canvas.drawLine(lpoint, ABAppUtil.dip2px(getContext(), MODEL_HEIGHT_DP - TIED_LINE_HEIGHT_DP),
                lpoint, ABAppUtil.dip2px(getContext(), MODEL_HEIGHT_DP), paint);
    }

    //画左上拐角
    protected void drawLeftBreakUp(Canvas canvas, float pointViewWidthDp) {
        setLinePaint();
        Path leftCurvedArrow = new Path();
        leftCurvedArrow.moveTo(leftCornerX, 0);
        RectF leftArcRange = new RectF(
                leftCornerX,
                leftCornerY - arcDiameter,
                leftCornerX + arcDiameter,
                leftCornerY
        );
        //奇怪, 这里逆时针了
        leftCurvedArrow.arcTo(leftArcRange, 180, -90, false);
        leftCurvedArrow.lineTo(lpoint - ABAppUtil.dip2px(getContext(), pointViewWidthDp / 2),
                leftCornerY);
        canvas.drawPath(leftCurvedArrow, paint);
    }

    //画左下拐角
    protected void drawLeftBreakDown(Canvas canvas) {
        setLinePaint();
        Path leftCurvedArrow = new Path();
        leftCurvedArrow.moveTo(leftCornerX, ABAppUtil.dip2px(getContext(), MODEL_HEIGHT_DP));
        RectF leftArcRange = new RectF(
                leftCornerX,
                leftCornerY,
                leftCornerX + arcDiameter,
                leftCornerY + arcDiameter
        );
        leftCurvedArrow.arcTo(leftArcRange, 180, 90, false);
        leftCurvedArrow.lineTo(lpoint - ABAppUtil.dip2px(getContext(), pointViewWidthDp / 2),
                leftCornerY);
        canvas.drawPath(leftCurvedArrow, paint);
    }

    //画右上拐角
    protected void drawRightBreakUp(Canvas canvas, float pointViewWidthDp) {
        setLinePaint();
        Path rightCurvedArrow = new Path();
        rightCurvedArrow.moveTo(rightCornerX, 0);
        RectF rightArcRange = new RectF(
                rightCornerX - arcDiameter,
                rightCornerY - arcDiameter,
                rightCornerX,
                rightCornerY
        );
        //顺时针
        rightCurvedArrow.arcTo(rightArcRange, 0, 90, false);
        rightCurvedArrow.lineTo(lpoint + ABAppUtil.dip2px(getContext(), pointViewWidthDp / 2),
                ABAppUtil.dip2px(getContext(), TIED_LINE_HEIGHT_DP + pointViewHeightDp / 2));
        canvas.drawPath(rightCurvedArrow, paint);
    }

    /**
     * 设置好画线的画笔
     */
    private void setLinePaint() {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(ABAppUtil.dip2px(getContext(), LINE_WIDTH_DP));
        paint.setColor(ContextCompat.getColor(getContext(), R.color.flow_chart_line));
    }

    /**
     * 设置文字的画笔
     */
    protected void setTextPaint() {
        paint.setStrokeWidth(TEXT_PAINT_STROKE_WIDTH);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(ABAppUtil.dip2px(getContext(), TEXT_SIZE_DP));
        paint.setColor(ContextCompat.getColor(getContext(), R.color.flow_chart_text));
    }

    //画右下拐角
    protected void drawRightBreakDown(Canvas canvas) {
        setLinePaint();
        Path rightCurvedArrow = new Path();
        rightCurvedArrow.moveTo(lpoint + ABAppUtil.dip2px(getContext(), pointViewWidthDp / 2),
                ABAppUtil.dip2px(getContext(), TIED_LINE_HEIGHT_DP + pointViewHeightDp / 2));
        RectF rectF = new RectF(
                rightCornerX - arcDiameter,
                rightCornerY,
                rightCornerX,
                rightCornerY + arcDiameter
        );
        rightCurvedArrow.arcTo(rectF, -90, 90, false);
        rightCurvedArrow.lineTo(lpoint +
                        ABAppUtil.dip2px(getContext(), pointViewWidthDp / 2 + LINE_POINT_DISTANCE_DP),
                ABAppUtil.dip2px(getContext(), MODEL_HEIGHT_DP)
        );
        canvas.drawPath(rightCurvedArrow, paint);
    }

    //画左边径直通过线
    protected void drawLinePassByLeft(Canvas canvas) {
        canvas.drawLine(leftCornerX, 0, leftCornerX, ABAppUtil.dip2px(getContext(), MODEL_HEIGHT_DP), paint);
    }


    //画右边径直通过线
    protected void drawLinePassByRight(Canvas canvas) {
        canvas.drawLine(rightCornerX, 0, rightCornerX, ABAppUtil.dip2px(getContext(), MODEL_HEIGHT_DP), paint);
    }

    public String getDirectlyAppendedText() {
        return directlyAppendedText;
    }

    public void setDirectlyAppendedText(String directlyAppendedText) {
        this.directlyAppendedText = directlyAppendedText;
    }

    public int getLineState() {
        return lineState;
    }

    public void setLineState(int lineState) {
        this.lineState = lineState;
    }

    public boolean isHighLight() {
        return highLight == 1;
    }

    public void setHighLight(int highLight) {
        this.highLight = highLight;
    }

    public String getLeftLineAppendix() {
        return leftLineAppendix;
    }

    public void setLeftLineAppendix(String leftLineAppendix) {
        this.leftLineAppendix = leftLineAppendix;
    }

    public String getRightLineAppendix() {
        return rightLineAppendix;
    }

    public void setRightLineAppendix(String rightLineAppendix) {
        this.rightLineAppendix = rightLineAppendix;
    }

    //检查当前系统是否已开启暗黑模式
    protected boolean getDarkModeStatus(Context context) {
        int mode =
                context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return mode == Configuration.UI_MODE_NIGHT_YES;
    }
}
