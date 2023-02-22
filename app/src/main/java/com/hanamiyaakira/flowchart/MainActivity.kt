package com.hanamiyaakira.flowchart

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hanamiyaakira.flowchart.utils.AssetsUtil
import com.hanamiyaakira.flowchart.utils.FastJsonUtils
import com.hanamiyaakira.flowchartcore.FlowChartView
import com.hanamiyaakira.flowchartcore.bean.ProcBean

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val flv = findViewById<FlowChartView>(R.id.flow_chart_view)
        flv.setData(
            FastJsonUtils.stringToObject(
                AssetsUtil.readText(
                    this,
                    "test_process_data_0221.json"
                ), ProcBean::class.java
            )
        )
    }
}