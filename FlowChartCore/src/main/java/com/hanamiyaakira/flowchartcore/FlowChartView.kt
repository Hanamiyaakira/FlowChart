package com.hanamiyaakira.flowchartcore

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.hanamiyaakira.flowchartcore.bean.ProcBean
import com.hanamiyaakira.flowchartcore.byteview.E
import com.hanamiyaakira.flowchartcore.byteview.EG
import com.hanamiyaakira.flowchartcore.byteview.S
import com.hanamiyaakira.flowchartcore.byteview.UT
import com.hanamiyaakira.flowchartcore.virtualview.DiamondEG
import com.hanamiyaakira.flowchartcore.virtualview.EndPoint
import com.hanamiyaakira.flowchartcore.virtualview.PointViewBase.*
import com.hanamiyaakira.flowchartcore.virtualview.RectUTDzzy
import com.hanamiyaakira.flowchartcore.virtualview.StartPoint
import com.hanamiyaakira.flowchartcore.virtualview.VirtualViewEntrance.*
import com.tmall.wireless.tangram.TangramBuilder
import com.tmall.wireless.tangram.TangramEngine
import com.tmall.wireless.tangram.structure.card.LinearCard
import com.tmall.wireless.tangram.util.IInnerImageSetter
import com.tmall.wireless.vaf.framework.ViewManager
import com.tmall.wireless.vaf.virtualview.core.ViewBase
import io.reactivex.Flowable
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class FlowChartView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
    RelativeLayout(context, attrs, defStyleAttr) {

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0) {
    }

    constructor(context: Context?) : this(context, null) {
    }

    var mRootView: View
    var rvItem: RecyclerView
    private var nextNodeKey: String? = null
    private var processKey: String? = null
    private var instanceId: String? = null
    private var builder: TangramBuilder.InnerBuilder
    private var engine: TangramEngine
    internal var map: Map<String, ProcBean.NodesBean> = HashMap()

    init {
        mRootView = LayoutInflater.from(getContext()).inflate(R.layout.flow_chart_view, null)
        addView(mRootView)
        rvItem = mRootView.findViewById(R.id.rv_item)

        TangramBuilder.init(
            getContext(), object : IInnerImageSetter {
                override fun <IMAGE : ImageView?> doLoadImageUrl(view: IMAGE, url: String?) {
                }
            }, ImageView::class.java
        )

        builder = TangramBuilder.newInnerBuilder(getContext())


        //添加数据
        builder!!.registerCard(TYPE_UT, LinearCard::class.java)
        builder!!.registerCard(TYPE_EG, LinearCard::class.java)
        builder!!.registerCard(TYPE_E, LinearCard::class.java)
        builder!!.registerCard(TYPE_S, LinearCard::class.java)

        //注册virtualView
        builder!!.registerVirtualView<View>(TYPE_S.toLowerCase())
        builder!!.registerVirtualView<View>(TYPE_UT.toLowerCase())
        builder!!.registerVirtualView<View>(TYPE_EG.toLowerCase())
        builder!!.registerVirtualView<View>(TYPE_E.toLowerCase())
//        builder!!.registerVirtualView<View>("${processKey}_$TYPE_TEST_END")

        engine = builder!!.build()

        engine!!.enableAutoLoadMore(true)

        engine.bindView(rvItem)

        rvItem!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                engine!!.onScrolled()
            }
        })

        val viewManager = engine!!.getService(ViewManager::class.java)

        viewManager.viewFactory.registerBuilder(START_POINT, StartPoint.Builder())
//        viewManager.viewFactory.registerBuilder(RECT_UT_KFZY, RectUTKfzy.Builder())
        viewManager.viewFactory.registerBuilder(RECT_UT_DZZY, RectUTDzzy.Builder())
        viewManager.viewFactory.registerBuilder(DIAMOND_EG, DiamondEG.Builder())
        viewManager.viewFactory.registerBuilder(END_POINT, EndPoint.Builder())
        //这些是二进制
        engine!!.setVirtualViewTemplate(S.BIN)
        engine!!.setVirtualViewTemplate(UT.BIN)
        engine!!.setVirtualViewTemplate(EG.BIN)
        engine!!.setVirtualViewTemplate(E.BIN)

        engine!!.layoutManager.setFixOffset(0, 40, 0, 0)

    }

    public fun setData(procBean: ProcBean) {
        transform(procBean)
    }

    private fun transform(procBean: ProcBean) {
        try {
            val data = JSONArray()
            //排序
            val positionsBeans = collectPosition(procBean.nodePositions)
                ?: //显示空白页, 提示不支持, 敬请期待
                return
            val nodesBeans = procBean.nodes
            //映射key为NodeKey
            Flowable.fromIterable(nodesBeans)
                .toMap { nodesBean -> nodesBean.nodeKey }
                .subscribe { stringNodesBeanMap -> map = stringNodesBeanMap }
            //区分直线曲线
            val flowLinesBeans = procBean.flowLines
            for (nodesPositionBean in positionsBeans) {
                val nodesBean = map[nodesPositionBean.nodeKey]
                //把序号埋进去
                nodesBean!!.series = positionsBeans.indexOf(nodesPositionBean)
            }
            Flowable.fromIterable(flowLinesBeans)
                .groupBy { flowLinesBean -> flowLinesBean.fromAnchor != flowLinesBean.toAnchor }
                .subscribe { booleanFlowLinesBeanGroupedFlowable ->
                    booleanFlowLinesBeanGroupedFlowable
                        .subscribe { flowLinesBean ->
                            if (booleanFlowLinesBeanGroupedFlowable?.getKey() ?: false) {
                                //指向我的直线
                                val toMeNodesBean = map[flowLinesBean.toNode]
                                if (toMeNodesBean != null) {
                                    var lineState = toMeNodesBean.lineState
                                    lineState = lineState or DIRECTLY_POINT_TO_ME_MASK
                                    toMeNodesBean.lineState = lineState
                                }
                                //从我出发的直线
                                val toNextNodesBean = map[flowLinesBean.fromNode]
                                if (toNextNodesBean != null) {
                                    var lineState = toNextNodesBean.lineState
                                    lineState = lineState or DIRECTLY_POINT_TO_NEXT_MASK
                                    toNextNodesBean.lineState = lineState
                                }
                            } else {
                                /**
                                 * 这里new的话, 有问题, 因为会把前一次的值给覆盖掉
                                 */
                                //这里要用二进制处理
                                //立足于FromNode这个节点, 如果toNode在这个节点的上方, 作差小于0,
                                // 如果fromNode节点在toNode节点的上面,作差大于0
                                val toSeries = map[flowLinesBean.toNode]!!.series
                                val fromSeries = map[flowLinesBean.fromNode]!!.series
                                //我把流程编号了，第一个编号0，逐个增加
                                val temp = toSeries - fromSeries
                                //temp大于0，代表线是从上指到下的
                                if (temp > 0) {
                                    //如果fromAnchor大于2, 代表左边的节点(3 → 3),按顺时针, 最上顶点为0,依次是:1,2,3
                                    if (flowLinesBean.fromAnchor > 2) {
                                        //线在左边, 然后要指向下面的节点
                                        val fromNodesBean = map[flowLinesBean.fromNode]
                                        if (fromNodesBean != null) {
                                            var lineState = fromNodesBean.lineState
                                            lineState = lineState or LEFT_BREAK_DOWN_MASK
                                            fromNodesBean.lineState = lineState
                                        }
                                    } else {
                                        //线在右边, 然后要指向下面的节点
                                        val fromNodesBean = map[flowLinesBean.fromNode]
                                        if (fromNodesBean != null) {
                                            var lineState = fromNodesBean.lineState
                                            lineState = lineState or RIGHT_BREAK_DOWN_MASK
                                            fromNodesBean.lineState = lineState
                                        }
                                    }
                                } else {
                                    //如果formAnchor小于2,代表右边的节点(1 → 1),按顺时针, 最上顶点为0,依次是:1,2,3
                                    if (flowLinesBean.fromAnchor > 2) {
                                        //线在左边, 然后要指向上面的节点
                                        val fromNodesBean = map[flowLinesBean.fromNode]
                                        if (fromNodesBean != null) {
                                            var lineState = fromNodesBean.lineState
                                            lineState = lineState or LEFT_BREAK_UP_MASK
                                            fromNodesBean.lineState = lineState
                                        }
                                    } else {
                                        //线在右边, 然后要指向上面的节点
                                        val fromNodesBean = map[flowLinesBean.fromNode]
                                        if (fromNodesBean != null) {
                                            var lineState = fromNodesBean.lineState
                                            lineState = lineState or RIGHT_BREAK_UP_MASK
                                            fromNodesBean.lineState = lineState
                                        }
                                    }
                                }
                                //代表指向我的值
                                //立足于toNode这个节点, 如果FromNode在这个节点的上方, 作差小于0,
                                // 如果toNode节点在FromNode节点的上面,作差大于0
                                val pmet = fromSeries - toSeries
                                if (pmet > 0) {
                                    //如果大于2, 代表左边的节点(3 → 3)，按顺时针, 最上顶点为0,依次是:1,2,3
                                    if (flowLinesBean.fromAnchor > 2) {
                                        //线在左边, 然后要指向下面的节点
                                        val toNodesBean = map[flowLinesBean.toNode]
                                        if (toNodesBean != null) {
                                            var lineState = toNodesBean.lineState
                                            lineState = lineState or DOWN_BREAK_LEFT_MASK
                                            toNodesBean.lineState = lineState
                                        }
                                    } else {
                                        //线在右边, 然后要指向下面的节点
                                        val toNodesBean = map[flowLinesBean.toNode]
                                        if (toNodesBean != null) {
                                            var lineState = toNodesBean.lineState
                                            lineState = lineState or DOWN_BREAK_RIGHT_MASK
                                            toNodesBean.lineState = lineState
                                        }
                                    }//如果小于2,代表右边的节点(1 → 1),按顺时针, 最上顶点为0,依次是:1,2,3
                                } else {
                                    if (flowLinesBean.fromAnchor > 2) {
                                        //线在左边, 然后要指向上面的节点
                                        val toNodesBean = map[flowLinesBean.toNode]
                                        if (toNodesBean != null) {
                                            var lineState = toNodesBean.lineState
                                            lineState = lineState or UP_BREAK_LEFT_MASK
                                            toNodesBean.lineState = lineState
                                        }
                                    } else {
                                        //线在右边, 然后要指向上面的节点
                                        val toNodesBean = map[flowLinesBean.toNode]
                                        if (toNodesBean != null) {
                                            var lineState = toNodesBean.lineState
                                            lineState = lineState or UP_BREAK_RIGHT_MASK
                                            toNodesBean.lineState = lineState
                                        }
                                    }
                                }
                                //画沿途经过我的线, 沿途控件, 如果是一个, 那它的编号为上下两个控件的和除以2
                                //如果是两个, 从from那个node开始, 每个加一
                                //以此类推
                                //fromNode是可以获取的
                                val edgeLineCount = Math.abs(temp) - 1
                                for (i in 1..edgeLineCount) {
                                    //temp大于0，代表线是从上指到下的
                                    if (temp > 0) {
                                        val nodeKey = positionsBeans[fromSeries + i].nodeKey
                                        //线在左边
                                        if (flowLinesBean.fromAnchor > 2) {
                                            val passByNodesBean = map[nodeKey]
                                            if (passByNodesBean != null) {
                                                var lineState = passByNodesBean.lineState
                                                lineState = lineState or PASS_BY_LEFT_MASK
                                                passByNodesBean.lineState = lineState
                                                //把线上面的文字描述放在bean里
                                                passByNodesBean.leftLineAppendix =
                                                    flowLinesBean.name
                                            }
                                        }
                                        //线在右边
                                        else {
                                            val passByNodesBean = map[nodeKey]
                                            if (passByNodesBean != null) {
                                                var lineState = passByNodesBean.lineState
                                                lineState = lineState or PASS_BY_RIGHT_MASK
                                                passByNodesBean.lineState = lineState
                                                //把线上面的文字描述放在bean里
                                                passByNodesBean.rightLineAppendix =
                                                    flowLinesBean.name
                                            }
                                        }
                                    }
                                    //temp小于0，代表线是从下指到上
                                    else {
                                        val nodeKey = positionsBeans[toSeries + i].nodeKey
                                        //线在左边
                                        if (flowLinesBean.fromAnchor > 2) {
                                            val passByNodesBean = map[nodeKey]
                                            if (passByNodesBean != null) {
                                                var lineState = passByNodesBean.lineState
                                                lineState = lineState or PASS_BY_LEFT_MASK
                                                passByNodesBean.lineState = lineState
                                                //把线上面的文字描述放在bean里
                                                passByNodesBean.leftLineAppendix =
                                                    flowLinesBean.name
                                            }
                                        }
                                        //线在右边
                                        else {
                                            val passByNodesBean = map[nodeKey]
                                            if (passByNodesBean != null) {
                                                var lineState = passByNodesBean.lineState
                                                lineState = lineState or PASS_BY_RIGHT_MASK
                                                passByNodesBean.lineState = lineState
                                                //把线上面的文字描述放在bean里
                                                passByNodesBean.rightLineAppendix =
                                                    flowLinesBean.name
                                            }
                                        }

                                    }
                                }


                            }
                        }
                }
            //为Tangram引擎设置数据
            for (nodesPositionBean in positionsBeans) {
                //用映射好的数据处理数字节点的text_start,text_eg,这些与节点数据的关系
                val nodesBean = map[nodesPositionBean.nodeKey]

                val jsonObject = JSONObject()
                jsonObject.put(ViewBase.TYPE, nodesBean!!.type)
                val jsonArray = JSONArray()
                val point = JSONObject()
                point.put("title", nodesBean.name)
                var color = "#3c3c3c"
                var highLight = 0
                if (TextUtils.equals(nodesPositionBean.nodeKey, nextNodeKey)) {
                    highLight = 1
                    color = "#ffffff"
                }
                point.put("color", color)
                point.put("highLight", highLight)
                point.put(ViewBase.TYPE, nodesBean.type.toLowerCase())
                point.put("directlyText", "通过")
                point.put(LEFT_LINE_APPENDIX, nodesBean.leftLineAppendix)
                point.put(RIGHT_LINE_APPENDIX, nodesBean.rightLineAppendix)
                //放入用二进制表示的线
                point.put(LINE_STATE, nodesBean.lineState)
                jsonArray.put(point)
                jsonObject.put("items", jsonArray)
                data.put(jsonObject)
            }
            Log.d("helloTAG", data.toString())
            //设置数据
            engine!!.setData(data)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun collectPosition(nodePositionsBeans: LinkedList<ProcBean.NodePositionsBean>?): List<ProcBean.NodePositionsBean>? {
        if (nodePositionsBeans == null || nodePositionsBeans.size == 0) {
            return null
        }

        val positionsBeans = ArrayList<ProcBean.NodePositionsBean>()
        Flowable.fromIterable(nodePositionsBeans)
            .filter { nodePositionsBean ->
                //如果上下两个节点的左右偏移太大，就怀疑是否为两个Branch，那就要另做处理
                Math.abs(nodePositionsBean.x - nodePositionsBeans[0].x) < 50
            }
            .subscribe { nodePositionsBean -> positionsBeans.add(nodePositionsBean) }

        if (positionsBeans.size != nodePositionsBeans.size) {
            return null
        }
        positionsBeans.sortWith(Comparator { o1, o2 ->
            //升序
            o1.y - o2.y
        })
        return positionsBeans

    }
}