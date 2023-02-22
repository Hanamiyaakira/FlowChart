package com.hanamiyaakira.flowchartcore.virtualview;

import com.tmall.wireless.vaf.virtualview.core.IView;

/**
 * @Author Akira Eigawa
 * @Date 2019/3/8
 * <p>
 * 参数配置
 */
public interface VirtualViewEntrance extends IView {

    /**
     * 总的参数
     */
    //上下缘结线的高度
    float TIED_LINE_HEIGHT_DP = 25;
    /**
     * 一个模块的高度
     * 模块的高度, 是圆型矩形菱形 + 上半段线 + 下半段线
     */
    float MODEL_HEIGHT_DP = 150;
    //圆型矩形菱形的外边框
    float SHAPE_STROKE_WIDTH_DP = 2;
    //线的宽度
    float LINE_WIDTH_DP = 1;
    //箭头的高度
    float ARROW_HEIGHT_DP = 8;
    //箭头的宽度
    float ARROW_WIDTH_DP = 8;
    //流程线与节点的距离
    float LINE_POINT_DISTANCE_DP = 30;
    //字线边距
    float LINE_TEXT_MARGIN_DP = 5;
    //圆弧的直径
    float ARC_DIAMETER_DP = 10;
    //箭头的比例
    float ARROW_SCALE = 0.5f;
    /**
     * 圆形节点的参数
     */
    //中心点的位置参数
    float CENTER_Y_DP = 75;
    //原型节点的半径
    float CIRCLE_POINT_RADIUS_DP = 50;
    /**
     * 矩形节点的参数
     */
    //矩形的高度
    float RECTANGLE_UT_HEIGHT_DP = 100;
    //矩形的宽度
    float RECTANGLE_UT_WIDTH_DP = 150;
    //圆角的弧度
    float RECTANGLE_ROUND_DP = 5;

    //二进制数, 用来做位屏蔽操作
    int DIRECTLY_POINT_TO_ME_MASK = 2048;
    int DIRECTLY_POINT_TO_NEXT_MASK = 1024;
    int PASS_BY_LEFT_MASK = 512;
    int PASS_BY_RIGHT_MASK = 256;
    int LEFT_BREAK_UP_MASK = 128;
    int LEFT_BREAK_DOWN_MASK = 64;
    int UP_BREAK_LEFT_MASK = 32;
    int DOWN_BREAK_LEFT_MASK = 16;
    int RIGHT_BREAK_UP_MASK = 8;
    int RIGHT_BREAK_DOWN_MASK = 4;
    int UP_BREAK_RIGHT_MASK = 2;
    int DOWN_BREAK_RIGHT_MASK = 1;

    //逻辑控件标识
    String TYPE_UT = "UT";
    String TYPE_EG = "EG";
    String TYPE_E = "E";
    String TYPE_S = "S";
    //nodeKey, 节点唯一ID
    String TYPE_TEST_START = "start";//开始节点
    String TYPE_TEST_KFZY = "kfzy";
    String TYPE_TEST_DZZY = "dzzy";
    String TYPE_TEST_EG = "eg1";
    String TYPE_TEST_END = "end";
    //控件标识
    int START_POINT = 1014;
    int RECT_UT_KFZY = 1016;
    int RECT_UT_DZZY = 1015;
    int DIAMOND_EG = 1017;
    int END_POINT = 1018;
}
