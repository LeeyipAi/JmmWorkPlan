package jmm.jmmworkplan.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View


/**
 * user:Administrator
 * time:2018 05 25 14:14
 * package_name:jmm.jmmworkplan.widget
 */
class MainBgView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private var mPain: Paint

    init {
        mPain = Paint()
        mPain.setColor(Color.parseColor("#88008E92"))            //设置画笔颜色为白色
        mPain.setAntiAlias(true)               //开启抗锯齿，平滑文字和圆弧的边缘
        mPain.setTextAlign(Paint.Align.CENTER) //设置文本位于相对于原点的中间
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        var x = width.toFloat() / 20
        canvas.drawCircle(x * 2, -height.toFloat() / 3, height.toFloat() / 3 * 4, mPain)
        canvas.drawCircle(x * 3, -height.toFloat() / 3, height.toFloat() / 3 * 4, mPain)
        canvas.drawCircle(x * 8, -height.toFloat() / 3, height.toFloat() / 3 * 4, mPain)
        canvas.drawCircle(x * 10, -height.toFloat() / 3, height.toFloat() / 3 * 4, mPain)
        canvas.drawCircle(x * 12, -height.toFloat() / 3, height.toFloat() / 3 * 4, mPain)
        canvas.drawCircle(x * 16, -height.toFloat() / 3, height.toFloat() / 3 * 4, mPain)
        canvas.drawCircle(x * 18, -height.toFloat() / 3, height.toFloat() / 3 * 4, mPain)
        canvas.drawCircle(x * 20, -height.toFloat() / 3, height.toFloat() / 3 * 4, mPain)
    }


}