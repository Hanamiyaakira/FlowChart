package com.hanamiyaakira.flowchartcore.virtualview;

import android.view.View;

import com.libra.Utils;
import com.libra.expr.common.StringSupport;
import com.tmall.wireless.vaf.framework.VafContext;
import com.tmall.wireless.vaf.virtualview.core.ViewBase;
import com.tmall.wireless.vaf.virtualview.core.ViewCache;

/**
 * @Author Benjen Masters
 * @Date 2019/3/6
 */
public class RectUTKfzy extends PointViewBase {

    protected RectUTKfzyView mPicassoImageView;

    private int urlId;

    private int degreeId;

    private String url;

    private float degrees;

    public RectUTKfzy(VafContext context,
                      ViewCache viewCache) {
        super(context, viewCache);
        mPicassoImageView = new RectUTKfzyView(context.forViewConstruction());
        StringSupport mStringSupport = context.getStringLoader();
        lineStateId = mStringSupport.getStringId(LINE_STATE, false);
    }

    @Override
    public void onComMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mPicassoImageView.onComMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void onComLayout(boolean changed, int l, int t, int r, int b) {
        mPicassoImageView.onComLayout(changed, l, t, r, b);
    }

    @Override
    public void comLayout(int l, int t, int r, int b) {
        super.comLayout(l, t, r, b);
        mPicassoImageView.comLayout(l, t, r, b);
    }

    @Override
    public View getNativeView() {
        return mPicassoImageView;
    }

    @Override
    public int getComMeasuredWidth() {
        return mPicassoImageView.getComMeasuredWidth();
    }

    @Override
    public int getComMeasuredHeight() {
        return mPicassoImageView.getComMeasuredHeight();
    }

    @Override
    protected boolean setAttribute(int key, float value) {
        boolean ret = true;
/*        if (key == degreeId) {
            degrees = value;
        } else {
            ret = super.setAttribute(key, value);
        }*/
        return ret;
    }

    @Override
    protected boolean setAttribute(int key, String stringValue) {
        boolean ret = true;
        if (key == degreeId) {
            if (Utils.isEL(stringValue)) {
                mViewCache.put(this, degreeId, stringValue, ViewCache.Item.TYPE_FLOAT);
            }
        } else if (key == lineStateId) {
            if (Utils.isEL(stringValue)) {
                mViewCache.put(this, lineStateId, stringValue, ViewCache.Item.TYPE_INT);
            }
        } else if (key == urlId) {
            if (Utils.isEL(stringValue)) {
                mViewCache.put(this, urlId, stringValue, ViewCache.Item.TYPE_STRING);
            } else {
                url = stringValue;
            }
        } else {
            ret = super.setAttribute(key, stringValue);
        }
        return ret;
    }

    @Override
    protected boolean setAttribute(int key, int value) {
        boolean ret = true;

        if (key == lineStateId) {
            lineState = value;
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
        mPicassoImageView.setLineState(lineState);
    }

    public static class Builder implements IBuilder {
        @Override
        public ViewBase build(VafContext context, ViewCache viewCache) {
            return new RectUTKfzy(context, viewCache);
        }
    }
}
