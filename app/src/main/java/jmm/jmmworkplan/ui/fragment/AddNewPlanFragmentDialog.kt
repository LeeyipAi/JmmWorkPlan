package jmm.jmmworkplan.ui.fragment

import android.app.DatePickerDialog
import android.app.Dialog
import android.view.LayoutInflater
import android.widget.*
import com.allattentionhere.fabulousfilter.AAH_FabulousFragment
import com.eightbitlab.rxbus.Bus
import com.google.gson.Gson
import jmm.jmmworkplan.R
import jmm.jmmworkplan.bean.SavePlan
import jmm.jmmworkplan.bean.WorkInfo
import jmm.jmmworkplan.common.excute
import jmm.jmmworkplan.net.Api
import jmm.jmmworkplan.net.BaseSubscriber
import jmm.jmmworkplan.net.RetrofitFactory
import jmm.jmmworkplan.widget.ProgressLoading
import org.jetbrains.anko.support.v4.toast
import retrofit2.adapter.rxjava.HttpException
import java.text.SimpleDateFormat
import java.util.*

/**
 * user:Administrator
 * time:2018 05 28 13:50
 * package_name:jmm.jmmworkplan.ui.fragment
 */
class AddNewPlanFragmentDialog : AAH_FabulousFragment(){

    lateinit var mLoadingDialog: ProgressLoading

    companion object {
        val instance: AddNewPlanFragmentDialog by lazy { AddNewPlanFragmentDialog() }
    }

    private val DEFAULT_TIME_FORMAT = "yyyy年MM月dd日HH:mm:ss"
    val dateFormatter = SimpleDateFormat(DEFAULT_TIME_FORMAT)
    val time = dateFormatter.format(Calendar.getInstance().getTime())
    var mYear = Calendar.getInstance().get(Calendar.YEAR)
    var mMonth = Calendar.getInstance().get(Calendar.MONTH)
    var mDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

    override fun setupDialog(dialog: Dialog, style: Int) {
        mLoadingDialog = ProgressLoading(this!!.context!!)
        val view = LayoutInflater.from(activity).inflate(R.layout.dialog_add_new_plan, null)
        var rlContent = view.findViewById<RelativeLayout>(R.id.rl_content)
        var llBottom = view.findViewById<LinearLayout>(R.id.ll_bootm)
        var tvSelectDate = view.findViewById<TextView>(R.id.tvSelectDate)
        var etPlanContent = view.findViewById<EditText>(R.id.etPlanContent)
        var btnSubmit = view.findViewById<Button>(R.id.btnSubmit)
        var btnDissmiss = view.findViewById<Button>(R.id.btnDissmiss)
        view.findViewById<Button>(R.id.mBtnSelectDate).setOnClickListener {
            DatePickerDialog(activity, object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                    mYear = year
                    mMonth = month
                    mDay = dayOfMonth
                    tvSelectDate.setText("${mYear}-${mMonth + 1}-${mDay}")
                }

            }, mYear, mMonth, mDay).show()
        }
        btnDissmiss.setOnClickListener {
            closeFilter(0)
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
                            override fun onStart() {
                                super.onStart()
                                mLoadingDialog.showLoading()
                            }

                            override fun onCompleted() {
                                super.onCompleted()
                                mLoadingDialog.dismissLoading()
                            }
                            override fun onError(e: Throwable?) {
                                super.onError(e)
                                mLoadingDialog.dismissLoading()
                                if (e is HttpException) {
                                    toast("提交失败")
                                } else {
                                    Bus.send(true)
                                    closeFilter(0)
                                    toast("提交成功")
                                }
                            }
                        })
            }
        }
        //params to set
        setAnimationDuration(300) //optional; default 500ms
        setPeekHeight(400) // optional; default 400dp
//        setCallbacks(activity as AAH_FabulousFragment.Callbacks?) //optional; to get back result
//        setAnimationListener(activity as AAH_FabulousFragment.AnimationListener?) //optional; to get animation callbacks
//        setViewgroupStatic(llBottom) // optional; layout to stick at bottom on slide
//        setViewPager(vp_types) //optional; if you use viewpager that has scrollview
        setViewMain(rlContent) //necessary; main bottomsheet view
        setMainContentView(view) // necessary; call at end before super
        super.setupDialog(dialog, style)
    }

}