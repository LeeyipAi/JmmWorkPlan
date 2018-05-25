package jmm.jmmworkplan

import android.app.Application
import android.content.Context

/*
    Application 基类
 */
open class App : Application() {

    /*
        全局伴生对象
     */
    companion object {
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = this
    }

}
