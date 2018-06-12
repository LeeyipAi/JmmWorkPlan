package jmm.jmmworkplan.ui.fragment

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import jmm.jmmworkplan.bean.WorkInfo
import jmm.jmmworkplan.net.Api
import jmm.jmmworkplan.net.RetrofitFactory
import jmm.jmmworkplan.ui.adapter.BaseRvAdapter
import jmm.jmmworkplan.ui.adapter.OwnWorkPlanAdapter
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Func1
import rx.schedulers.Schedulers

/**
 * user:Administrator
 * time:2018 05 29 16:15
 * package_name:jmm.jmmworkplan.ui.fragment
 */
class SearchListFragment: BaseRvFragment<WorkInfo.DataBean>() {

    private var pageCount: String? = null
    lateinit var ownWorkPlanAdapter: OwnWorkPlanAdapter

    override fun getCurrentPage(): String {
        return pageCount.toString()
    }

    override fun getApi(currNum: String): Observable<List<WorkInfo.DataBean>> {
        return RetrofitFactory.instance.create(Api::class.java)
                .getWorkPlan(mapOf("pageIndex" to currNum, "pageSize" to PAGE_SIZE.toString()))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(object : Func1<WorkInfo, List<WorkInfo.DataBean>> {
                    override fun call(t: WorkInfo): List<WorkInfo.DataBean>? {
                        pageCount = t.getTotal().toString()
                        return t.getData()
                    }
                })
    }

    override fun getRecyclerViewAdapter(): BaseRvAdapter<WorkInfo.DataBean> {
        ownWorkPlanAdapter = OwnWorkPlanAdapter()
        return ownWorkPlanAdapter
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
    }


}
