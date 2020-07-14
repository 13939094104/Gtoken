package com.vtoken.vdsecology.vcash


public class InitInfo {


    var initInfo = InitInfoEnum.NO_INIT


    var error = ""


    var errorCode = 0

    //C3882a
    enum class InitInfoEnum {
        NO_INIT,
        INITTING,
        INIT_SUCCESS,
        INIT_FAILED,
        DESTROYING
    }

    override fun toString(): String {
        val stringBuffer = StringBuffer()
        val sb = StringBuilder()
        sb.append("status: ")
        sb.append(this.initInfo.name)
        stringBuffer.append(sb.toString())
        val sb2 = StringBuilder()
        sb2.append("\nerrorCode: 0x")
        sb2.append(Integer.toHexString(this.errorCode))
        stringBuffer.append(sb2.toString())
        val sb3 = StringBuilder()
        sb3.append("\nerror: ")
        sb3.append(this.error)
        stringBuffer.append(sb3.toString())
        return stringBuffer.toString()
    }
}
