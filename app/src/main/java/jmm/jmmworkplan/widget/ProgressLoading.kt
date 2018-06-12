package jmm.jmmworkplan.widget

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.support.annotation.StyleRes
import android.view.View
import jmm.jmmworkplan.R


/**
 * user:Administrator
 * time:2018 04 17 10:54
 * package_name:com.jmm.csg.widget
 */

class ProgressLoading : Dialog {

    private var mAnimation: AnimationDrawable? = null

    constructor(context: Context) : super(context, R.style.LightProgressDialog) {
        init()
    }

    constructor(context: Context, @StyleRes themeResId: Int) : super(context, themeResId) {
        init()
    }

    private fun init() {
        val view = View.inflate(context, R.layout.dialog_loading, null)
        val window = window
        val params = window!!.attributes
        params.dimAmount = 0.2f
        window.attributes = params
        setCanceledOnTouchOutside(false)
        setContentView(view)
    }

    fun showLoading() {
        super.show()
    }

    fun dismissLoading() {
        super.dismiss()
    }
}
