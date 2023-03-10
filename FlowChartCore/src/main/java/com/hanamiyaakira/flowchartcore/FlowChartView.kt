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


        //????????????
        builder!!.registerCard(TYPE_UT, LinearCard::class.java)
        builder!!.registerCard(TYPE_EG, LinearCard::class.java)
        builder!!.registerCard(TYPE_E, LinearCard::class.java)
        builder!!.registerCard(TYPE_S, LinearCard::class.java)

        //??????virtualView
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
        //??????????????????
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
            //??????
            val positionsBeans = collectPosition(procBean.nodePositions)
                ?: //???????????????, ???????????????, ????????????
                return
            val nodesBeans = procBean.nodes
            //??????key???NodeKey
            Flowable.fromIterable(nodesBeans)
                .toMap { nodesBean -> nodesBean.nodeKey }
                .subscribe { stringNodesBeanMap -> map = stringNodesBeanMap }
            //??????????????????
            val flowLinesBeans = procBean.flowLines
            for (nodesPositionBean in positionsBeans) {
                val nodesBean = map[nodesPositionBean.nodeKey]
                //??????????????????
                nodesBean!!.series = positionsBeans.indexOf(nodesPositionBean)
            }
            Flowable.fromIterable(flowLinesBeans)
                .groupBy { flowLinesBean -> flowLinesBean.fromAnchor != flowLinesBean.toAnchor }
                .subscribe { booleanFlowLinesBeanGroupedFlowable ->
                    booleanFlowLinesBeanGroupedFlowable
                        .subscribe { flowLinesBean ->
                            if (booleanFlowLinesBeanGroupedFlowable?.getKey() ?: false) {
                                //??????????????????
                                val toMeNodesBean = map[flowLinesBean.toNode]
                                if (toMeNodesBean != null) {
                                    var lineState = toMeNodesBean.lineState
                                    lineState = lineState or DIRECTLY_POINT_TO_ME_MASK
                                    toMeNodesBean.lineState = lineState
                                }
                                //?????????????????????
                                val toNextNodesBean = map[flowLinesBean.fromNode]
                                if (toNextNodesBean != null) {
                                    var lineState = toNextNodesBean.lineState
                                    lineState = lineState or DIRECTLY_POINT_TO_NEXT_MASK
                                    toNextNodesBean.lineState = lineState
                                }
                            } else {
                                /**
                                 * ??????new??????, ?????????, ???????????????????????????????????????
                                 */
                                //???????????????????????????
                                //?????????FromNode????????????, ??????toNode????????????????????????, ????????????0,
                                // ??????fromNode?????????toNode???????????????,????????????0
                                val toSeries = map[flowLinesBean.toNode]!!.series
                                val fromSeries = map[flowLinesBean.fromNode]!!.series
                                //???????????????????????????????????????0???????????????
                                val temp = toSeries - fromSeries
                                //temp??????0?????????????????????????????????
                                if (temp > 0) {
                                    //??????fromAnchor??????2, ?????????????????????(3 ??? 3),????????????, ???????????????0,?????????:1,2,3
                                    if (flowLinesBean.fromAnchor > 2) {
                                        //????????????, ??????????????????????????????
                                        val fromNodesBean = map[flowLinesBean.fromNode]
                                        if (fromNodesBean != null) {
                                            var lineState = fromNodesBean.lineState
                                            lineState = lineState or LEFT_BREAK_DOWN_MASK
                                            fromNodesBean.lineState = lineState
                                        }
                                    } else {
                                        //????????????, ??????????????????????????????
                                        val fromNodesBean = map[flowLinesBean.fromNode]
                                        if (fromNodesBean != null) {
                                            var lineState = fromNodesBean.lineState
                                            lineState = lineState or RIGHT_BREAK_DOWN_MASK
                                            fromNodesBean.lineState = lineState
                                        }
                                    }
                                } else {
                                    //??????formAnchor??????2,?????????????????????(1 ??? 1),????????????, ???????????????0,?????????:1,2,3
                                    if (flowLinesBean.fromAnchor > 2) {
                                        //????????????, ??????????????????????????????
                                        val fromNodesBean = map[flowLinesBean.fromNode]
                                        if (fromNodesBean != null) {
                                            var lineState = fromNodesBean.lineState
                                            lineState = lineState or LEFT_BREAK_UP_MASK
                                            fromNodesBean.lineState = lineState
                                        }
                                    } else {
                                        //????????????, ??????????????????????????????
                                        val fromNodesBean = map[flowLinesBean.fromNode]
                                        if (fromNodesBean != null) {
                                            var lineState = fromNodesBean.lineState
                                            lineState = lineState or RIGHT_BREAK_UP_MASK
                                            fromNodesBean.lineState = lineState
                                        }
                                    }
                                }
                                //?????????????????????
                                //?????????toNode????????????, ??????FromNode????????????????????????, ????????????0,
                                // ??????toNode?????????FromNode???????????????,????????????0
                                val pmet = fromSeries - toSeries
                                if (pmet > 0) {
                                    //????????????2, ?????????????????????(3 ??? 3)???????????????, ???????????????0,?????????:1,2,3
                                    if (flowLinesBean.fromAnchor > 2) {
                                        //????????????, ??????????????????????????????
                                        val toNodesBean = map[flowLinesBean.toNode]
                                        if (toNodesBean != null) {
                                            var lineState = toNodesBean.lineState
                                            lineState = lineState or DOWN_BREAK_LEFT_MASK
                                            toNodesBean.lineState = lineState
                                        }
                                    } else {
                                        //????????????, ??????????????????????????????
                                        val toNodesBean = map[flowLinesBean.toNode]
                                        if (toNodesBean != null) {
                                            var lineState = toNodesBean.lineState
                                            lineState = lineState or DOWN_BREAK_RIGHT_MASK
                                            toNodesBean.lineState = lineState
                                        }
                                    }//????????????2,?????????????????????(1 ??? 1),????????????, ???????????????0,?????????:1,2,3
                                } else {
                                    if (flowLinesBean.fromAnchor > 2) {
                                        //????????????, ??????????????????????????????
                                        val toNodesBean = map[flowLinesBean.toNode]
                                        if (toNodesBean != null) {
                                            var lineState = toNodesBean.lineState
                                            lineState = lineState or UP_BREAK_LEFT_MASK
                                            toNodesBean.lineState = lineState
                                        }
                                    } else {
                                        //????????????, ??????????????????????????????
                                        val toNodesBean = map[flowLinesBean.toNode]
                                        if (toNodesBean != null) {
                                            var lineState = toNodesBean.lineState
                                            lineState = lineState or UP_BREAK_RIGHT_MASK
                                            toNodesBean.lineState = lineState
                                        }
                                    }
                                }
                                //????????????????????????, ????????????, ???????????????, ????????????????????????????????????????????????2
                                //???????????????, ???from??????node??????, ????????????
                                //????????????
                                //fromNode??????????????????
                                val edgeLineCount = Math.abs(temp) - 1
                                for (i in 1..edgeLineCount) {
                                    //temp??????0?????????????????????????????????
                                    if (temp > 0) {
                                        val nodeKey = positionsBeans[fromSeries + i].nodeKey
                                        //????????????
                                        if (flowLinesBean.fromAnchor > 2) {
                                            val passByNodesBean = map[nodeKey]
                                            if (passByNodesBean != null) {
                                                var lineState = passByNodesBean.lineState
                                                lineState = lineState or PASS_BY_LEFT_MASK
                                                passByNodesBean.lineState = lineState
                                                //?????????????????????????????????bean???
                                                passByNodesBean.leftLineAppendix =
                                                    flowLinesBean.name
                                            }
                                        }
                                        //????????????
                                        else {
                                            val passByNodesBean = map[nodeKey]
                                            if (passByNodesBean != null) {
                                                var lineState = passByNodesBean.lineState
                                                lineState = lineState or PASS_BY_RIGHT_MASK
                                                passByNodesBean.lineState = lineState
                                                //?????????????????????????????????bean???
                                                passByNodesBean.rightLineAppendix =
                                                    flowLinesBean.name
                                            }
                                        }
                                    }
                                    //temp??????0??????????????????????????????
                                    else {
                                        val nodeKey = positionsBeans[toSeries + i].nodeKey
                                        //????????????
                                        if (flowLinesBean.fromAnchor > 2) {
                                            val passByNodesBean = map[nodeKey]
                                            if (passByNodesBean != null) {
                                                var lineState = passByNodesBean.lineState
                                                lineState = lineState or PASS_BY_LEFT_MASK
                                                passByNodesBean.lineState = lineState
                                                //?????????????????????????????????bean???
                                                passByNodesBean.leftLineAppendix =
                                                    flowLinesBean.name
                                            }
                                        }
                                        //????????????
                                        else {
                                            val passByNodesBean = map[nodeKey]
                                            if (passByNodesBean != null) {
                                                var lineState = passByNodesBean.lineState
                                                lineState = lineState or PASS_BY_RIGHT_MASK
                                                passByNodesBean.lineState = lineState
                                                //?????????????????????????????????bean???
                                                passByNodesBean.rightLineAppendix =
                                                    flowLinesBean.name
                                            }
                                        }

                                    }
                                }


                            }
                        }
                }
            //???Tangram??????????????????
            for (nodesPositionBean in positionsBeans) {
                //??????????????????????????????????????????text_start,text_eg,??????????????????????????????
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
                point.put("directlyText", "??????")
                point.put(LEFT_LINE_APPENDIX, nodesBean.leftLineAppendix)
                point.put(RIGHT_LINE_APPENDIX, nodesBean.rightLineAppendix)
                //??????????????????????????????
                point.put(LINE_STATE, nodesBean.lineState)
                jsonArray.put(point)
                jsonObject.put("items", jsonArray)
                data.put(jsonObject)
            }
            Log.d("helloTAG", data.toString())
            //????????????
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
                //????????????????????????????????????????????????????????????????????????Branch????????????????????????
                Math.abs(nodePositionsBean.x - nodePositionsBeans[0].x) < 50
            }
            .subscribe { nodePositionsBean -> positionsBeans.add(nodePositionsBean) }

        if (positionsBeans.size != nodePositionsBeans.size) {
            return null
        }
        positionsBeans.sortWith(Comparator { o1, o2 ->
            //??????
            o1.y - o2.y
        })
        return positionsBeans

    }
}