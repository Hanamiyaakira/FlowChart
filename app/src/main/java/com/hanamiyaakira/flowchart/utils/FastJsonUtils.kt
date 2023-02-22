package com.hanamiyaakira.flowchart.utils

import android.text.TextUtils
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.TypeReference


/**
 * @author weijianxing
 *
 *
 * 封装JSON的操作
 *
 *
 * 优点:如果想更换JSON的库的话,只需要修改这个类就可以了
 */
object FastJsonUtils {

    fun <T> stringToObject(jsonString: String?, clazz: Class<T>): T {
        return JSON.parseObject(jsonString, clazz)
    }

    fun <T> stringToObject1(jsonString: String?, clazz: Class<T>): T {
        return JSON.parseObject(jsonString, object : TypeReference<T>(clazz) {})
    }

    fun <T> stringToArrayList(jsonString: String?, clazz: Class<T>): MutableList<T> {
        if (TextUtils.isEmpty(jsonString)) {
            return mutableListOf<T>()
        }
        return JSONArray.parseArray(jsonString, clazz).toMutableList()
    }

    fun stringToObject(jsonString: String?): JSONObject {
        return JSONArray.parseObject(jsonString)
    }

    fun toJSONString(`object`: Any?): String {
        return JSON.toJSONString(`object`)
    }
}
