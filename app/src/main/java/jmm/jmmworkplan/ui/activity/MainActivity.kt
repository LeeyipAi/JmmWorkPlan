package jmm.jmmworkplan.ui.activity

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import com.eightbitlab.rxbus.Bus
import com.google.gson.Gson
import jmm.jmmworkplan.R
import jmm.jmmworkplan.bean.SavePlan
import jmm.jmmworkplan.bean.WorkInfo
import jmm.jmmworkplan.common.excute
import jmm.jmmworkplan.net.Api
import jmm.jmmworkplan.net.BaseSubscriber
import jmm.jmmworkplan.net.RetrofitFactory
import jmm.jmmworkplan.ui.adapter.MainPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
import retrofit2.adapter.rxjava.HttpException
import rx.Observable
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity(), Toolbar.OnMenuItemClickListener {

    private val DEFAULT_TIME_FORMAT = "YYYY年MM月dd日HH:mm:ss"
    val dateFormatter = SimpleDateFormat(DEFAULT_TIME_FORMAT)
    val time = dateFormatter.format(Calendar.getInstance().getTime())
    var mYear = Calendar.getInstance().get(Calendar.YEAR)
    var mMonth = Calendar.getInstance().get(Calendar.MONTH)
    var mDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        initToolbar()
        initViewPager()
        initFab()

    }

    private fun initViewPager() {
        vP.setAdapter(MainPagerAdapter(supportFragmentManager))
        tabLayout.setupWithViewPager(vP)
        vP.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                Log.e("positionOffset", positionOffset.toString())
                Log.e("positionOffsetPixels", positionOffsetPixels.toString())
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
            showBottomSheetDialog()
        }
    }

    private fun showBottomSheetDialog() {
        val dialog = BottomSheetDialog(this)
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_add_new_plan, null)
        var tvSelectDate = view.findViewById<TextView>(R.id.tvSelectDate)
        var etPlanContent = view.findViewById<EditText>(R.id.etPlanContent)
        var btnSubmit = view.findViewById<Button>(R.id.btnSubmit)
        var btnDissmiss = view.findViewById<Button>(R.id.btnDissmiss)

        view.findViewById<Button>(R.id.mBtnSelectDate).setOnClickListener {
            DatePickerDialog(this, object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                    mYear = year
                    mMonth = month
                    mDay = dayOfMonth
                    tvSelectDate.setText("${mYear}-${mMonth + 1}-${mDay}")
                }

            }, mYear, mMonth, mDay).show()
        }
        btnDissmiss.setOnClickListener {
            dialog.dismiss()
        }
        btnSubmit.setOnClickListener {
            if (tvSelectDate.text.isEmpty() || etPlanContent.text.isEmpty()) {
                toast("日期和计划不能为空")
            } else {
                val savePlan = arrayOfNulls<SavePlan.DataBean>(1)
                savePlan[0] = SavePlan.DataBean("", "", tvSelectDate.text.toString(), etPlanContent.text.toString())
                var params = mapOf("data" to Gson().toJson(savePlan), "id" to "")
                RetrofitFactory.instance.create(Api::class.java).ownWorkPlanSave(params)
                        .excute(object : BaseSubscriber<WorkInfo>() {
                            override fun onError(e: Throwable?) {
                                super.onError(e)
                                if (e is HttpException) {
                                    toast("提交错误")
                                } else {
                                    Bus.send(true)
                                    dialog.dismiss()
                                    toast("提交成功")
                                }
                            }
                        })
            }

        }
        dialog.setContentView(view)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
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
                intent.setType("image/*")
                intent.putExtra(Intent.EXTRA_SUBJECT, "Share")
                intent.putExtra(Intent.EXTRA_TEXT, "https://fir.im/rhv1")
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(Intent.createChooser(intent, getTitle()))
            }
            R.id.action_settings -> toast("设置")
        }
        return true
    }

}
