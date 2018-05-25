package jmm.jmmworkplan.ui.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import jmm.jmmworkplan.R
import jmm.jmmworkplan.common.excute
import jmm.jmmworkplan.net.Api
import jmm.jmmworkplan.net.BaseResp
import jmm.jmmworkplan.net.BaseSubscriber
import jmm.jmmworkplan.net.RetrofitFactory
import jmm.jmmworkplan.utils.SpUtils
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mBtnLogin.setOnClickListener(this)
        if (SpUtils.getString("username") !== "") {
            mUserName.setText(SpUtils.getString("username"))
            mPassWord.setText(SpUtils.getString("password"))
        }
    }


    override fun onClick(v: View?) {
        when(v?.id){
            R.id.mBtnLogin -> login()
        }
    }

    private fun login() {
        RetrofitFactory.instance.create(Api::class.java)
                .login(mapOf("username" to mUserName.text.toString(),"password" to mPassWord.text.toString()))
                .excute(object : BaseSubscriber<BaseResp<String>>(){
                    override fun onNext(t: BaseResp<String>) {
                        if (t.code == "0000") {
                            if (mCbRemember.isChecked) {
                                SpUtils.putString("username",mUserName.text.toString())
                                SpUtils.putString("password",mPassWord.text.toString())
                            }
                            toast("登录成功")
                            startActivity<MainActivity>()
                            finish()
                        }else {
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
                    }
                })
    }
}
