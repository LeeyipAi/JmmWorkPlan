package jmm.jmmworkplan.widget

import android.content.Context
import android.os.Handler
import android.os.Message
import android.support.annotation.Nullable
import android.util.AttributeSet
import java.text.SimpleDateFormat
import java.util.*

/**
 * user:Administrator
 * time:2018 05 24 14:56
 * package_name:jmm.jmmworkplan.widget
 */
class TimeView(context: Context, @Nullable attrs: AttributeSet) : android.support.v7.widget.AppCompatTextView(context, attrs) {

    internal var handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
           setText(msg.obj.toString())
        }
    }
    private val DEFAULT_TIME_FORMAT = "YYYY年MM月dd日HH:mm:ss"

    init {
        init()
    }

    private fun init() {
        Thread(Runnable {
            while (true) {
                //                    SimpleDateFormat sdf=new SimpleDateFormat(DEFAULT_TIME_FORMAT);
                //                    String str=sdf.format(new Date());
                val dateFormatter = SimpleDateFormat(DEFAULT_TIME_FORMAT)
                val time = dateFormatter.format(Calendar.getInstance().getTime())
                handler.sendMessage(handler.obtainMessage(100, time))
                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

            }
        }).start()
    }

}