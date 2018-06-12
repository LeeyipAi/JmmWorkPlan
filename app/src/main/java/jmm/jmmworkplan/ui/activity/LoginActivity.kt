package jmm.jmmworkplan.ui.activity

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import jmm.jmmworkplan.R
import jmm.jmmworkplan.common.excute
import jmm.jmmworkplan.net.Api
import jmm.jmmworkplan.net.BaseResp
import jmm.jmmworkplan.net.BaseSubscriber
import jmm.jmmworkplan.net.RetrofitFactory
import jmm.jmmworkplan.utils.SpUtils
import jmm.jmmworkplan.widget.ProgressLoading
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast


class LoginActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var mLoadingDialog: ProgressLoading

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mBtnLogin.setOnClickListener(this)
        if (SpUtils.getString("username") !== "") {
            mUserName.setText(SpUtils.getString("username"))
            mPassWord.setText(SpUtils.getString("password"))
        }
        mLoadingDialog = ProgressLoading(this)
        if (SpUtils.getBoolean("autologin")) {
            mCbAuto.setChecked(true)
        }
        checkUpdate()
    }

    private fun checkUpdate() {
        RetrofitFactory.instance.create(Api::class.java)
                .checkUpdate()
                .excute(object : BaseSubscriber<BaseResp<String>>() {
                    override fun onNext(t: BaseResp<String>) {
                        if (t.code == "0000") {
                            if (SpUtils.getBoolean("autologin") && intent.getBooleanExtra("autologin", true)) {
                                login()
                            }
                        } else {
                            AlertDialog.Builder(this@LoginActivity)
                                    .setTitle("温馨提示")
                                    .setMessage("有新版本是否更新?")
                                    .setPositiveButton("更新", DialogInterface.OnClickListener { dialogInterface, i ->
                                        try {
                                            val intent = Intent()
                                            intent.action = "android.intent.action.VIEW"
                                            val content_url = Uri.parse("https://fir.im/rhv1")
                                            intent.data = content_url
                                            if (!hasPreferredApplication(this@LoginActivity, intent)) {
                                                intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity")
                                            }
                                            startActivity(intent)
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                        }

                                    })
                                    .setNeutralButton("取消", null)
                                    .create()
                                    .show()
                        }
                    }
                })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.mBtnLogin -> login()
        }
    }

    private fun login() {
        RetrofitFactory.instance.create(Api::class.java)
                .login(mapOf("username" to mUserName.text.toString(), "password" to mPassWord.text.toString()))
                .excute(object : BaseSubscriber<BaseResp<String>>() {
                    override fun onStart() {
                        super.onStart()
                        mLoadingDialog.showLoading()
                    }

                    override fun onNext(t: BaseResp<String>) {
                        mLoadingDialog.dismissLoading()
                        if (t.code == "0000") {
                            if (mCbRemember.isChecked) {
                                SpUtils.putString("username", mUserName.text.toString())
                                SpUtils.putString("password", mPassWord.text.toString())
                            }
                            if (mCbAuto.isChecked) {
                                SpUtils.putBoolean("autologin", true)
                            } else {
                                SpUtils.putBoolean("autologin", false)
                            }
                            toast("登录成功")
                            startActivity<MainActivity>()
                            finish()
                        } else {
                            toast("密码错误")
                            mPassWordLayout.setError(t.desc)
                            mPassWordLayout.getEditText()?.setFocusable(true)
                            mPassWordLayout.getEditText()?.setFocusableInTouchMode(true)
                            mPassWordLayout.getEditText()?.requestFocus()
                        }
                    }

                    override fun onError(e: Throwable?) {
                        super.onError(e)
                        toast("网络错误")
                        mLoadingDialog.dismissLoading()
                    }
                })
    }

    /**
     * 判断是否有默认浏览器
     */
    fun hasPreferredApplication(context: Context, intent: Intent): Boolean {
        val pm = context.getPackageManager()
        val info = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
        return "android" != info.activityInfo.packageName
    }
}
