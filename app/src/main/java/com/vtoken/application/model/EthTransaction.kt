package com.vtoken.application.model

import com.vtoken.application.util.DateUtil
import vdsMain.transaction.CAmount
import java.io.Serializable
import java.math.BigInteger

class EthTransaction(val timeStamp:String,val hash:String,val from:String,val to:String,val value:String,contractAddress:String,val tokenDecimal:String?,val blockNumber:String,
                     val gasPrice:String,val gasUsed:String):
    Serializable
{

    //0 receive 1 send
    var type=0

    fun getDateFormat():String{
        return DateUtil.formatTimeStampDefault(timeStamp.toLong()*1000)
    }

    fun getValueFormat():String{
        com.orhanobut.logger.Logger.d(tokenDecimal)
        return (if(type==0) "+" else "-")+ if(tokenDecimal==null) CAmount.toDecimalEthString(value) else CAmount.toDecimalTokenString(value,tokenDecimal.toInt())
    }

    fun getFee():String{
        return CAmount.toDecimalEthString(BigInteger(gasPrice).multiply(BigInteger(gasUsed)).toString())
    }
}