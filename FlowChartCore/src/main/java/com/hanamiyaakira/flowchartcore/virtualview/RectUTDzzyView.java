package com.hanamiyaakira.flowchartcore.virtualview;

import android.content.Context;
import android.graphics.Canvas;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;

import com.hanamiyaakira.flowchartcore.utils.ABAppUtil;

import androidx.annotation.Nullable;

/**
 * @Author Benjen Masters
 * @Date 2019/3/7
 */
public class RectUTDzzyView extends RectUTBaseView {


    public RectUTDzzyView(Context context) {
        super(context);
    }

    public RectUTDzzyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RectUTDzzyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
        Log.d("helloTAG", "rect_handle_ut");
//        int leftLoc = lpoint + ABAppUtil.dip2px(getContext(),
//                RECTANGLE_UT_WIDTH_DP / 2 + LINE_POINT_DISTANCE_DP);
        //文字
        int lineState = getLineState();
        //这只是种临时方案, 观察了显示的流程图之后, 发现:
        // 只要在经过我(pass by)的线上画上"打回修改", 就可以适应这个业务下见到的流程图.
        if ((lineState & PASS_BY_RIGHT_MASK) > 0) {
            setTextPaint();
            canvas.drawText(getRightLineAppendix(), rightCornerX + LINE_TEXT_MARGIN_DP,
                    canvas.getHeight() / 2 - (paint.descent() + paint.ascent()) / 2
                    , paint);
        }
        if ((lineState & PASS_BY_LEFT_MASK) > 0) {
            TextPaint paint = new TextPaint();
            setTextPaint(paint);
            String leftTips = getLeftLineAppendix();
            //如果是不通过，那么，那线就是不用显示文字，因为文字已经在菱形节点显示了
            if (TextUtils.equals(leftTips, "不通过")) {
                return;
            }
            StaticLayout layout = new StaticLayout(
                    leftTips, paint,
                    ABAppUtil.dip2px(getContext(), LINE_POINT_DISTANCE_DP),
                    Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true
            );
            canvas.save();
            canvas.translate(leftCornerX + LINE_TEXT_MARGIN_DP,
                    canvas.getHeight() / 2 - (paint.descent() + paint.ascent()) / 2
            );
//            ABAppUtil.dip2px(getContext(), TIED_LINE_HEIGHT_DP + RECTANGLE_UT_HEIGHT_DP / 2));
            layout.draw(canvas);
            canvas.restore();
        }
    }

}
