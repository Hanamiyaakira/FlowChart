package com.hanamiyaakira.flowchartcore.virtualview;

import com.tmall.wireless.vaf.framework.VafContext;
import com.tmall.wireless.vaf.virtualview.core.ViewBase;
import com.tmall.wireless.vaf.virtualview.core.ViewCache;

/**
 * @Author Akira Eigawa
 * @Date 2019/3/14
 * <p>
 * 温馨提示:
 * 代码千万行,
 * 注释第一行.
 */
public abstract class PointViewBase extends ViewBase {

    //取值的ID
    protected int directlyAppendedTextId;
    protected int leftLineAppendixId;
    protected int rightLineAppendixId;
    protected int lineStateId;
    protected int highLightId;
    //获得的具体数字, 来自json
    protected int lineState;
    protected int highLight;
    //处理线
    protected String directlyAppendedText;
    protected String leftLineAppendix;
    protected String rightLineAppendix;
    //字段的字符常量
    public final static String LINE_STATE = "lineState";
    public final static String DIRECTLY_APPENDED_TEXT = "directlyText";
    public final static String HIGH_LIGHT = "highLight";
    public final static String LEFT_LINE_APPENDIX = "leftLineAppendix";
    public final static String RIGHT_LINE_APPENDIX = "rightLineAppendix";
    //字段的字符常量, 1代表true, 0代表false
    public final static int HAS_THIS_LINE = 1;

    public PointViewBase(VafContext context, ViewCache viewCache) {
        super(context, viewCache);
    }
}
