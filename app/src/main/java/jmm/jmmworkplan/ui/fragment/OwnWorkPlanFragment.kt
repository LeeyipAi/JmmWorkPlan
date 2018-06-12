package jmm.jmmworkplan.ui.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.eightbitlab.rxbus.Bus
import com.eightbitlab.rxbus.registerInBus
import jmm.jmmworkplan.bean.WorkInfo
import jmm.jmmworkplan.common.excute
import jmm.jmmworkplan.net.Api
import jmm.jmmworkplan.net.BaseResp
import jmm.jmmworkplan.net.BaseSubscriber
import jmm.jmmworkplan.net.RetrofitFactory
import jmm.jmmworkplan.ui.adapter.BaseRvAdapter
import jmm.jmmworkplan.ui.adapter.LoadStatus
import jmm.jmmworkplan.ui.adapter.OwnWorkPlanAdapter
import org.jetbrains.anko.support.v4.toast
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.functions.Func1
import rx.schedulers.Schedulers


/**
 * user:Administrator
 * time:2018 05 24 15:41
 * package_name:jmm.jmmworkplan.ui.fragment
 */
class OwnWorkPlanFragment : BaseRvFragment<WorkInfo.DataBean>() {

    private var pageCount: String? = null


    init {
        Bus.observe<Boolean>()
                .subscribe(object : Action1<Boolean> {
                    override fun call(t: Boolean) {
                        if (t) loadData(LoadStatus.LOADING)
                    }
                }).registerInBus(this)
    }

    override fun getCurrentPage(): String {
        return pageCount.toString()
    }

    override fun getApi(currNum: String): Observable<List<WorkInfo.DataBean>> {
        return RetrofitFactory.instance.create(Api::class.java)
                .getOwnWorkPlan(mapOf("pageIndex" to currNum, "pageSize" to PAGE_SIZE.toString()))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(object : Func1<WorkInfo, List<WorkInfo.DataBean>> {
                    override fun call(t: WorkInfo): List<WorkInfo.DataBean>? {
                        pageCount = "10"
                        return t.getData()
                    }
                })
    }

    override fun getRecyclerViewAdapter(): BaseRvAdapter<WorkInfo.DataBean> {
        return OwnWorkPlanAdapter()
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {

    }

    override fun onItemLongClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        var item = adapter.getItem(position) as WorkInfo.DataBean
        var dialog = AlertDialog.Builder(activity)
        dialog.setTitle("温馨提示")
                .setMessage("是否删除本条工作计划")
                .setNegativeButton("取消",object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        toast("取消")
                    }
                })
                .setPositiveButton("确定",object: DialogInterface.OnClickListener{
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        var params = mapOf("id" to item.id, "_" to System.currentTimeMillis().toString())
                        RetrofitFactory.instance.create(Api::class.java).workPlanDelete(params as Map<String, String>)
                                .excute(object : BaseSubscriber<BaseResp<String>>(){
                                    override fun onNext(t: BaseResp<String>) {
                                        super.onNext(t)
                                        if(t.code == "1"){
                                            toast("删除成功")
                                            loadData(LoadStatus.LOADING)
                                        }else {
                                            toast("删除失败")
                                        }
                                    }

                                    override fun onError(e: Throwable?) {
                                        super.onError(e)
                                            toast("删除失败")
                                    }
                                })
                    }
                })
                .show()
    }

//    override fun getItemDecoration(): RecyclerView.ItemDecoration? {
//        return FloatDecoration(0)
//    }

}

