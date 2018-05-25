package jmm.jmmworkplan.ui.activity

import android.animation.Animator
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import jmm.jmmworkplan.R
import kotlinx.android.synthetic.main.activity_splash.*
import org.jetbrains.anko.startActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
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
}
