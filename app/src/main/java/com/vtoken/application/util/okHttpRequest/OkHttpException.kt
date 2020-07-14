package com.vtoken.application.util.okHttpRequest

enum class OkHttpExceptionEnum(val code:Int){
    NETWORK_ERROR(-1),
    JSON_ERROR(-2),
    OTHER_ERROR(-3),
    SERVER_ERROR(-4)
}

class OkHttpException : Exception {

    var errcode:OkHttpExceptionEnum

    var serverErrorCode:Int=0;

    var msg:String="";

    constructor(errCode: OkHttpExceptionEnum, e: Exception) {
        this.errcode=errCode;
    }

    constructor(errCode: OkHttpExceptionEnum, errorReason: String) {
        this.errcode=errCode;
        this.msg=errorReason
    }

    constructor(errCode: OkHttpExceptionEnum, serverErrorCode: Int?,errorReason: String) {
        this.errcode=errCode;
        this.serverErrorCode=serverErrorCode!!;
        this.msg=errorReason
    }

    override fun toString(): String {
        return "errcode:"+errcode.name+"\n serverErrorCode:"+serverErrorCode.toString()
    }
}
