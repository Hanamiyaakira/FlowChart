package com.hanamiyaakira.flowchartcore.virtualview;

import android.view.View;

import com.libra.Utils;
import com.libra.expr.common.StringSupport;
import com.tmall.wireless.vaf.framework.VafContext;
import com.tmall.wireless.vaf.virtualview.core.ViewBase;
import com.tmall.wireless.vaf.virtualview.core.ViewCache;

/**
 * @Author Benjen Masters
 * @Date 2019/3/8
 */
public class DiamondEG extends PointViewBase {

    protected DiamondEGView diamondEGView;

    public DiamondEG(VafContext context, ViewCache viewCache) {
        super(context, viewCache);
        diamondEGView = new DiamondEGView(context.forViewConstruction());
        StringSupport mStringSupport = context.getStringLoader();
        directlyAppendedTextId = mStringSupport.getStringId(DIRECTLY_APPENDED_TEXT, false);
        lineStateId = mStringSupport.getStringId(LINE_STATE, false);
        highLightId = mStringSupport.getStringId(HIGH_LIGHT, false);
    }

    @Override
    public void onComMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        diamondEGView.onComMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void onComLayout(boolean changed, int l, int t, int r, int b) {
        diamondEGView.onComLayout(changed, l, t, r, b);
    }

    @Override
    public void comLayout(int l, int t, int r, int b) {
        super.comLayout(l, t, r, b);
        diamondEGView.comLayout(l, t, r, b);
    }

    @Override
    public View getNativeView() {
        return diamondEGView;
    }

    @Override
    public int getComMeasuredWidth() {
        return diamondEGView.getComMeasuredWidth();
    }

    @Override
    public int getComMeasuredHeight() {
        return diamondEGView.getComMeasuredHeight();
    }

    /**
     * @param key
     * @param value
     * @return 肩负两个任务, 一个是处理"${****}", 一个是接收key为String类型的value
     */
    @Override
    protected boolean setAttribute(int key, String value) {
        boolean ret = true;
        if (key == directlyAppendedTextId) {
            if (Utils.isEL(value)) {
                mViewCache.put(this, directlyAppendedTextId, value, ViewCache.Item.TYPE_STRING);
            } else {
                directlyAppendedText = value;
            }
        } else if (key == lineStateId) {
            if (Utils.isEL(value)) {
                mViewCache.put(this, lineStateId, value, ViewCache.Item.TYPE_INT);
            }
        } else if (key == highLightId) {
            if (Utils.isEL(value)) {
                mViewCache.put(this, highLightId, value, ViewCache.Item.TYPE_INT);
            }
        } else {
            ret = super.setAttribute(key, value);
        }
        return ret;
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
    public void onParseValueFinished() {
        super.onParseValueFinished();
        diamondEGView.setLineState(lineState);
        diamondEGView.setDirectlyAppendedText(directlyAppendedText);
        diamondEGView.setHighLight(highLight);
    }

    public static class Builder implements IBuilder {

        @Override
        public ViewBase build(VafContext context, ViewCache vc) {
            return new DiamondEG(context, vc);
        }
    }
}
