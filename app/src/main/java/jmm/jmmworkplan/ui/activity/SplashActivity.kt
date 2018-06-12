package jmm.jmmworkplan.ui.activity

import android.animation.Animator
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import jmm.jmmworkplan.R
import jmm.jmmworkplan.common.excute
import jmm.jmmworkplan.net.Api
import jmm.jmmworkplan.net.BaseResp
import jmm.jmmworkplan.net.BaseSubscriber
import jmm.jmmworkplan.net.RetrofitFactory
import kotlinx.android.synthetic.main.activity_splash.*
import org.jetbrains.anko.startActivity

class SplashActivity : AppCompatActivity() {

    val BASE_URL = "http://s.cn.bing.net"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
//        StatusBarUtils.transparencyBar(this)
//        initData()
        mSplashLottie.addAnimatorListener(object : Animator.AnimatorListener{
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                startActivity<LoginActivity>()
                finish()
            }

        })
    }

    private fun initData() {
        RetrofitFactory.instance.create(Api::class.java)
                .getBizhi().excute(object:BaseSubscriber<BaseResp<String>>(){
                    override fun onNext(t: BaseResp<String>) {
                        super.onNext(t)
                        var s = t.desc.indexOf("<url>")
                        var e = t.desc.indexOf("</url>")
                        var url = t.desc.substring(s + 5, e).replace("1366x768", "480x800")
                        Glide.with(applicationContext)
                                .load(BASE_URL + url)
                                .skipMemoryCache(false)
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .into(mIvSplashBg)
                    }

                    override fun onError(e: Throwable?) {
                        super.onError(e)
                    }
                })
    }
}
