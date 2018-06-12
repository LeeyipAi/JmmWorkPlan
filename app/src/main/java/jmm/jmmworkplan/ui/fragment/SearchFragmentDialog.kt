package jmm.jmmworkplan.ui.fragment

import android.app.DatePickerDialog
import android.app.Dialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.widget.*
import com.allattentionhere.fabulousfilter.AAH_FabulousFragment
import jmm.jmmworkplan.R
import jmm.jmmworkplan.bean.WorkInfo
import jmm.jmmworkplan.common.excute
import jmm.jmmworkplan.net.Api
import jmm.jmmworkplan.net.BaseSubscriber
import jmm.jmmworkplan.net.RetrofitFactory
import jmm.jmmworkplan.ui.adapter.OwnWorkPlanAdapter
import jmm.jmmworkplan.widget.ProgressLoading
import org.jetbrains.anko.support.v4.toast
import java.util.*

/**
 * user:Administrator
 * time:2018 05 28 13:50
 * package_name:jmm.jmmworkplan.ui.fragment
 */
class SearchFragmentDialog : AAH_FabulousFragment() {

    lateinit var mLoadingDialog: ProgressLoading
    lateinit var mAdapter: OwnWorkPlanAdapter

    var mYear = Calendar.getInstance().get(Calendar.YEAR)
    var mMonth = Calendar.getInstance().get(Calendar.MONTH)
    var mDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

    companion object {
        val instance: SearchFragmentDialog by lazy { SearchFragmentDialog() }
    }

    override fun setupDialog(dialog: Dialog, style: Int) {
        mLoadingDialog = ProgressLoading(this!!.context!!)
        val view = LayoutInflater.from(activity).inflate(R.layout.dialog_search, null)
        var rlContent = view.findViewById<LinearLayout>(R.id.rl_content)
        var etSearch = view.findViewById<EditText>(R.id.et_search)
        var btnSearch = view.findViewById<Button>(R.id.btn_search)
        var rv = view.findViewById<RecyclerView>(R.id.rv)
        var tvSelectDate = view.findViewById<TextView>(R.id.tvSelectDate)
        var mIvDelete = view.findViewById<ImageView>(R.id.mIvDelete)
        mIvDelete.setOnClickListener {
            tvSelectDate.setText("")
        }
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

        rv.setLayoutManager(LinearLayoutManager(activity))
        mAdapter = OwnWorkPlanAdapter()
        rv.setAdapter(mAdapter)
        var map = mutableMapOf("pageIndex" to "0", "pageSize" to "10000")
        btnSearch.setOnClickListener {
            if (etSearch.text.isEmpty() && tvSelectDate.text == "") {
                toast("搜索内容和日期不能都为空")
            } else {
                if (!etSearch.text.isEmpty()) {
                    map.put("name", etSearch.text.toString())
                }else {
                    map.remove("name")
                }
                if (tvSelectDate.text != "") {
                    map.put("date", tvSelectDate.text.toString() + " 00:00:00")
                }else {
                    map.remove("date")
                }
                RetrofitFactory.instance.create(Api::class.java)
                        .getWorkPlan(map)
                        .excute(object : BaseSubscriber<WorkInfo>() {
                            override fun onNext(t: WorkInfo) {
                                super.onNext(t)
                                mAdapter.setNewData(t.getData())
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