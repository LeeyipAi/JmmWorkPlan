package jmm.jmmworkplan.net

data class BaseResp<out T>(val code:String, val desc:String, val data:T)
