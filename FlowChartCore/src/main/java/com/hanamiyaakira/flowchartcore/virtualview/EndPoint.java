/*
 * MIT License
 *
 * Copyright (c) 2018 Alibaba Group
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.hanamiyaakira.flowchartcore.virtualview;

import android.view.View;

import com.libra.Utils;
import com.libra.expr.common.StringSupport;
import com.tmall.wireless.vaf.framework.VafContext;
import com.tmall.wireless.vaf.virtualview.core.ViewBase;
import com.tmall.wireless.vaf.virtualview.core.ViewCache;

/**
 * Created by longerian on 2018/3/17.
 *
 * @author longerian
 * @date 2018/03/17
 */

public class EndPoint extends PointViewBase {

    protected EndPointView ovalPointView;

    public EndPoint(VafContext context,
                    ViewCache viewCache) {
        super(context, viewCache);
        ovalPointView = new EndPointView(context.forViewConstruction());
        StringSupport mStringSupport = context.getStringLoader();
        lineStateId = mStringSupport.getStringId(LINE_STATE, false);
        highLightId = mStringSupport.getStringId(HIGH_LIGHT, false);
    }

    @Override
    public void onComMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        ovalPointView.onComMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void onComLayout(boolean changed, int l, int t, int r, int b) {
        ovalPointView.onComLayout(changed, l, t, r, b);
    }

    @Override
    public void comLayout(int l, int t, int r, int b) {
        super.comLayout(l, t, r, b);
        ovalPointView.comLayout(l, t, r, b);
    }

    @Override
    public View getNativeView() {
        return ovalPointView;
    }

    @Override
    public int getComMeasuredWidth() {
        return ovalPointView.getComMeasuredWidth();
    }

    @Override
    public int getComMeasuredHeight() {
        return ovalPointView.getComMeasuredHeight();
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
    protected boolean setAttribute(int key, String stringValue) {
        boolean ret = true;
        if (key == lineStateId) {
            if (Utils.isEL(stringValue)) {
                mViewCache.put(this, lineStateId, stringValue, ViewCache.Item.TYPE_INT);
            }
        } else if (key == highLightId) {
            if (Utils.isEL(stringValue)) {
                mViewCache.put(this, highLightId, stringValue, ViewCache.Item.TYPE_INT);
            }
        } else {
            ret = super.setAttribute(key, stringValue);
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
        ovalPointView.setLineState(lineState);
        ovalPointView.setHighLight(highLight);
    }

    public static class Builder implements IBuilder {
        @Override
        public ViewBase build(VafContext context, ViewCache viewCache) {
            return new EndPoint(context, viewCache);
        }
    }
}
