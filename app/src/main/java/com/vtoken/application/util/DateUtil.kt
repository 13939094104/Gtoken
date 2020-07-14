package com.vtoken.application.util

import java.text.SimpleDateFormat
import java.util.*

object DateUtil {

    fun formatTimeStampDefault(timeStamp:Long,format:String="yyyy.MM.dd HH:mm:ss"):String{
        val dataformat=SimpleDateFormat(format)
        val date=Date(timeStamp)
        return dataformat.format(date)
    }

    fun getTimeDeltaSinceNow(timeStamp: Int):TimeDelta{
        var nowTimeStamp=(System.currentTimeMillis() / 1000).toInt()
        var delta=Math.abs(timeStamp-nowTimeStamp)
        return TimeDelta(delta)
    }

    class TimeDelta{

        constructor(delta: Int){
            day=delta/86400
            val dayReminder=delta%86400
            hour=dayReminder/3600
            val hourReminder =dayReminder%3600
            minute=hourReminder/60
            seconds=hourReminder%60
        }
        var day:Int=0
        var hour:Int=0
        var minute:Int=0
        var seconds:Int=0
    }

}