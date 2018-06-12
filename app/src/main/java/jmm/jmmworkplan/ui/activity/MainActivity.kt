package jmm.jmmworkplan.ui.activity

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.TextInputLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import com.google.gson.Gson
import jmm.jmmworkplan.R
import jmm.jmmworkplan.bean.ChangePwdBean
import jmm.jmmworkplan.common.excute
import jmm.jmmworkplan.net.Api
import jmm.jmmworkplan.net.BaseResp
import jmm.jmmworkplan.net.BaseSubscriber
import jmm.jmmworkplan.net.RetrofitFactory
import jmm.jmmworkplan.ui.adapter.MainPagerAdapter
import jmm.jmmworkplan.ui.fragment.AddNewPlanFragmentDialog
import jmm.jmmworkplan.ui.fragment.SearchFragmentDialog
import jmm.jmmworkplan.utils.SpUtils
import jmm.jmmworkplan.utils.StatusBarUtils
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import rx.Observable
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity(), Toolbar.OnMenuItemClickListener {

    private val DEFAULT_TIME_FORMAT = "yyyy年MM月dd日HH:mm:ss"
    val dateFormatter = SimpleDateFormat(DEFAULT_TIME_FORMAT.toString())
    val time = dateFormatter.format(Calendar.getInstance().getTime())
    private var pressTime = 0L
    val INTENT_ALARM_LOG = "intent_alarm_log"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        StatusBarUtils.transparencyBar(this)
        initView()
    }

    private fun initView() {
        initToolbar()
        initViewPager()
        initFab()
    }

    private fun initViewPager() {
        vP.setPageTransformer(false, object : ViewPager.PageTransformer {
            override fun transformPage(page: View, position: Float) {
                if (position < -1) {
                    page.setAlpha(0F)
                } else if (position <= 0) {
                    page.setAlpha(1 + position)
                } else if (position <= 1) {
                    page.setAlpha(1 - position)
                } else {
                    page.setAlpha(0F)
                }
            }
        })
        vP.setAdapter(MainPagerAdapter(supportFragmentManager))
        tabLayout.setupWithViewPager(vP)
        vP.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                if (positionOffset != 0F) {
                    if (positionOffset < 0.5) {
                        fabAdd.setScaleX(1 - positionOffset * 2)
                        fabAdd.setScaleY(1 - positionOffset * 2)
                        fabAdd.setImageResource(R.drawable.ic_add)
                    } else {
                        fabAdd.setScaleX(positionOffset * 2 - 1)
                        fabAdd.setScaleY(positionOffset * 2 - 1)
                        fabAdd.setImageResource(R.drawable.ic_search)
                    }
                }
            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> toolBar.setTitle("我的工作计划")
                    1 -> toolBar.setTitle("工作计划")
                }
            }
        })
    }

    private fun initFab() {
        fabAdd.setOnClickListener {
            if (vP.currentItem == 0) {
                showAddNewPlan()
            } else {
                showSearch()
            }
        }
    }

    private fun showAddNewPlan() {
        var fabdialog = AddNewPlanFragmentDialog.instance
        fabdialog.setParentFab(fabAdd)
        fabdialog.show(supportFragmentManager, fabdialog.tag)
    }

    private fun showSearch() {
        var fabdialog = SearchFragmentDialog.instance
        fabdialog.setParentFab(fabAdd)
        fabdialog.show(supportFragmentManager, fabdialog.tag)
    }

    private fun initToolbar() {
        toolBar.setSubtitle(time)
        setSupportActionBar(toolBar)
        toolBar.setOnMenuItemClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        Observable.interval(1, 1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Long> {
                    override fun onError(e: Throwable?) {
                    }

                    override fun onCompleted() {
                    }

                    override fun onNext(t: Long?) {
                        val time = dateFormatter.format(Calendar.getInstance().getTime())
                        toolBar.setSubtitle(time)
                    }
                })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.menu, menu)
        return true
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search -> {
                var intent = Intent(Intent.ACTION_SEND)
                intent.setType("text/*")
                intent.putExtra(Intent.EXTRA_SUBJECT, "Share")
                intent.putExtra(Intent.EXTRA_TEXT, "https://fir.im/rhv1")
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(Intent.createChooser(intent, getTitle()))
            }
            R.id.action_settings -> {
                toast("设置")
//                var intent = Intent(this, AlarmReceiver::class.java)
//                intent.setAction(INTENT_ALARM_LOG)
//                var sender = PendingIntent.getBroadcast(this, 0, intent, 0)
//                val firstTime = SystemClock.elapsedRealtime() //获取系统当前时间
//                val systemTime = System.currentTimeMillis()//java.lang.System.currentTimeMillis()
//
//                val calendar = Calendar.getInstance()
//                calendar.timeInMillis = System.currentTimeMillis()
//                calendar.timeZone = TimeZone.getTimeZone("GMT+8") //  这里时区需要设置一下，不然会有8个小时的时间差
//                calendar.set(Calendar.HOUR_OF_DAY, 15)//设置为8：30点提醒
//                calendar.set(Calendar.MINUTE, 50)
//                calendar.set(Calendar.SECOND, 0)
//                calendar.set(Calendar.MILLISECOND, 0)
//                //选择的定时时间
//                var selectTime = calendar.timeInMillis   //计算出设定的时间
//                //  如果当前时间大于设置的时间，那么就从第二天的设定时间开始
//                if (systemTime > selectTime) {
//                    calendar.add(Calendar.DAY_OF_MONTH, 1)
//                    selectTime = calendar.timeInMillis
//                }
//                val time = selectTime - systemTime// 计算现在时间到设定时间的时间差
//                val my_Time = firstTime + time//系统 当前的时间+时间差
//                var am = getSystemService(ALARM_SERVICE) as AlarmManager
//                am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, my_Time, AlarmManager.INTERVAL_DAY, sender)

            }
            R.id.action_logout -> {
                finish()
                startActivity<LoginActivity>("autologin" to false)
            }
            R.id.action_changepwd -> {
                showChangePwdDialog()
            }
        }
        return true
    }

    private fun showChangePwdDialog() {
        var dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("修改密码")
        val view = LayoutInflater.from(this@MainActivity).inflate(R.layout.dialog_changepwd, null)
        dialogBuilder.setView(view)
        var mNewPassWord = view.findViewById<EditText>(R.id.mNewPassWord)
        var mConfirmPassWord = view.findViewById<EditText>(R.id.mConfirmPassWord)
        var mNewPassWordLayout = view.findViewById<TextInputLayout>(R.id.mNewPassWordLayout)

        dialogBuilder.setNegativeButton("取消", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface, which: Int) {
                toast("取消")
                dialog.dismiss()
            }
        })
        dialogBuilder.setPositiveButton("确定", null)
        var dialog = dialogBuilder.create()
        dialog.show()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            if (mNewPassWord.text.isEmpty() || mConfirmPassWord.text.isEmpty()) {
                toast("密码不能为空")
            } else if (mNewPassWord.text.toString() != mConfirmPassWord.text.toString()) {
                toast("两次密码不相同")
            } else {
                val data = arrayOfNulls<ChangePwdBean.DataBean>(1)
                data[0] = ChangePwdBean.DataBean("", mNewPassWord.text.toString(), mConfirmPassWord.text.toString())
                var map = mapOf("data" to Gson().toJson(data))
                RetrofitFactory.instance.create(Api::class.java).changepwd(map)
                        .excute(object : BaseSubscriber<BaseResp<String>>() {
                            override fun onNext(t: BaseResp<String>) {
                                super.onNext(t)
                                Log.e("tga", t.toString())
                                SpUtils.putString("password", mNewPassWord.text.toString())
                                toast("修改成功")
                                dialog.dismiss()
                            }

                            override fun onError(e: Throwable?) {
                                super.onError(e)
                                toast("修改失败")
                            }
                        })
            }
        }
    }


    override fun onBackPressed() {
        val time = System.currentTimeMillis()
        if (time - pressTime > 2500) {
            showSnack()
            pressTime = time
        } else {
            finish()
            //在后台活动
//            moveTaskToBack(true)
        }
    }

    private fun showSnack() {
        val snackbar = Snackbar.make(fabAdd, "再按一次返回键退出", Snackbar.LENGTH_SHORT)
        snackbar.view.setBackgroundColor(resources.getColor(R.color.colorPrimary))
        snackbar.show()
    }
}
