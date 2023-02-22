package com.hanamiyaakira.flowchartcore.virtualview;

import android.view.View;

import com.libra.Utils;
import com.libra.expr.common.StringSupport;
import com.tmall.wireless.vaf.framework.VafContext;
import com.tmall.wireless.vaf.virtualview.core.ViewBase;
import com.tmall.wireless.vaf.virtualview.core.ViewCache;

/**
 * @Author Benjen Masters
 * @Date 2019/3/7
 */
public class RectUTDzzy extends PointViewBase {

    protected RectUTDzzyView rectHandleUTView;

    public RectUTDzzy(VafContext context, ViewCache viewCache) {
        super(context, viewCache);
        rectHandleUTView = new RectUTDzzyView(context.forViewConstruction());
        StringSupport mStringSupport = context.getStringLoader();
        lineStateId = mStringSupport.getStringId(LINE_STATE, false);
        highLightId = mStringSupport.getStringId(HIGH_LIGHT, false);
        leftLineAppendixId = mStringSupport.getStringId(LEFT_LINE_APPENDIX, false);
        rightLineAppendixId = mStringSupport.getStringId(RIGHT_LINE_APPENDIX, false);
    }

    @Override
    public void onComMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        rectHandleUTView.onComMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void onComLayout(boolean changed, int l, int t, int r, int b) {
        rectHandleUTView.onComLayout(changed, l, t, r, b);
    }

    @Override
    public void comLayout(int l, int t, int r, int b) {
        super.comLayout(l, t, r, b);
        rectHandleUTView.comLayout(l, t, r, b);
    }

    @Override
    public View getNativeView() {
        return rectHandleUTView;
    }

    @Override
    public int getComMeasuredWidth() {
        return rectHandleUTView.getComMeasuredWidth();
    }

    @Override
    public int getComMeasuredHeight() {
        return rectHandleUTView.getComMeasuredHeight();
    }

    //这个方法要返回true, 不然, 不会回调onDraw方法
    @Override
    protected boolean setAttribute(int key, String value) {
        boolean ret = true;
        if (key == lineStateId) {
            if (Utils.isEL(value)) {
                mViewCache.put(this, lineStateId, value, ViewCache.Item.TYPE_INT);
            }
        } else if (key == highLightId) {
            if (Utils.isEL(value)) {
                mViewCache.put(this, highLightId, value, ViewCache.Item.TYPE_INT);
            }
        } else if (key == leftLineAppendixId) {
            if (Utils.isEL(value)) {
                mViewCache.put(this, leftLineAppendixId, value, ViewCache.Item.TYPE_STRING);
            } else {
                leftLineAppendix = value;
            }
        } else if (key == rightLineAppendixId) {
            if (Utils.isEL(value)) {
                mViewCache.put(this, rightLineAppendixId, value, ViewCache.Item.TYPE_STRING);
            } else {
                rightLineAppendix = value;
            }
        } else {
            ret = super.setAttribute(key, value);
        }
        return ret;
    }

    @Override
    protected boolean setAttribute(int key, float value) {
        return true;
    }

    @Override
    protected boolean setAttribute(int key, int value) {
        boolean ret = true;

        if (key == lineStateId) {
            lineState = value;
        } else if (key == highLightId) {
            highLight = value;
        } else {
            ret = super.setAttribute(key, value);
        }
        return ret;

    }

    @Override
    public void reset() {
        super.reset();
//        Picasso.with(mContext.getApplicationContext()).cancelRequest(ovalPointView);
    }

    @Override
    public void onParseValueFinished() {
        super.onParseValueFinished();
//        Picasso.with(mContext.getApplicationContext()).load(url).rotate(degrees).into(ovalPointView);
        rectHandleUTView.setLineState(lineState);
        rectHandleUTView.setHighLight(highLight);
        rectHandleUTView.setLeftLineAppendix(leftLineAppendix);
        rectHandleUTView.setRightLineAppendix(rightLineAppendix);
    }

    public static class Builder implements IBuilder {

        @Override
        public ViewBase build(VafContext context, ViewCache vc) {
            return new RectUTDzzy(context, vc);
        }
    }


}
