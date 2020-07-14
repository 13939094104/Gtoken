package com.vtoken.application.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder

class GsonUtil {

    private var gson:Gson?=null

    private var serializeNullsGson:Gson?=null

    fun getGson():Gson{
        if(gson==null)
            gson= Gson()
        return gson!!
    }

    fun getserializeNullsGson():Gson{
        if(serializeNullsGson==null)
            serializeNullsGson= GsonBuilder().serializeNulls().create()
        return serializeNullsGson!!
    }

    companion object{

        var mInstance:GsonUtil?=null

        fun getInstance(): GsonUtil{
            if(mInstance==null)
                mInstance=GsonUtil()
            return mInstance!!
        }
    }
}