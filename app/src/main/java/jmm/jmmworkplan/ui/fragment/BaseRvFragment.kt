package jmm.jmmworkplan.ui.fragment

import `in`.srain.cube.views.ptr.PtrDefaultHandler
import `in`.srain.cube.views.ptr.PtrFrameLayout
import `in`.srain.cube.views.ptr.PtrHandler
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.SimpleClickListener
import jmm.jmmworkplan.R
import jmm.jmmworkplan.net.BaseSubscriber
import jmm.jmmworkplan.ui.adapter.BaseRvAdapter
import jmm.jmmworkplan.ui.adapter.LoadStatus
import kotlinx.android.synthetic.main.framgent_base_rv.*
import rx.Observable


/**
 * user:Administrator
 * time:2018 05 24 15:17
 * package_name:jmm.jmmworkplan.ui.fragment
 */
open abstract class BaseRvFragment<T> : Fragment() {

    internal lateinit var mRvAdapter: BaseRvAdapter<T>
    protected var mPageCount: Int = 0
    protected var mCurrentPage = 1
    protected var PAGE_SIZE = 10

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return View.inflate(getActivity(), R.layout.framgent_base_rv, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        initRv()
        initPtrFrameLayout()
        loadData(LoadStatus.LOADING)
    }

    /**
     * 初始化RecyclerView
     */
    private fun initRv() {
        recyclerView.addOnItemTouchListener(RecyclerItemClickListener(this))
        if (getLayoutManger() != null) {
            recyclerView.setLayoutManager(getLayoutManger())
        } else {
            recyclerView.setLayoutManager(LinearLayoutManager(activity))
        }
        if (getItemDecoration() != null) {
            recyclerView.addItemDecoration(getItemDecoration())
        }
        mRvAdapter = getRecyclerViewAdapter()
        mRvAdapter.openLoadAnimation()
        recyclerView.setAdapter(mRvAdapter)
        mRvAdapter.setEnableLoadMore(true) //开启加载更多
        //        mRvAdapter.setLoadMoreView(new CustomLoadMoreView());     //自定义加载更多
        mRvAdapter.setOnLoadMoreListener({ loadData(LoadStatus.MORE) }, recyclerView)//添加加载更多监听
//        mRvAdapter.setEmptyView(R.layout.layout_state_empty)
    }

    /**
     * 初始化下拉刷新控件
     */
    private fun initPtrFrameLayout() {
//        val krHeader = JmmHeader(activity)
//        krHeader.setShowRefreshInfo(true)
//        krHeader.getCompleteView().setText("暂无更新内容")
//        mPtrFrameLayout.setHeaderView(krHeader)
//        mPtrFrameLayout.addPtrUIHandler(krHeader)
//        mPtrFrameLayout.setDurationToCloseHeader(1000)
//        mPtrFrameLayout.setDurationToClose(200)
//        mPtrFrameLayout.setLoadingMinTime(2000)
//        mPtrFrameLayout.setEnabledNextPtrAtOnce(true)
        ptrFrameLayout.setLastUpdateTimeRelateObject(this)
        ptrFrameLayout.setPtrHandler(object : PtrHandler {
            override fun checkCanDoRefresh(frame: PtrFrameLayout, content: View, header: View): Boolean {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header)
            }

            override fun onRefreshBegin(frame: PtrFrameLayout) {
                loadData(LoadStatus.REFRESH)
            }
        })
    }

    /**
     * 加载数据
     */
    protected fun loadData(status: LoadStatus) {
        if (status === LoadStatus.MORE) {
            val currentPage = getCurrentPage()
            if (currentPage != null) {
                mPageCount = Integer.parseInt(currentPage!!)
            }
            mCurrentPage = ++mCurrentPage
            if (mCurrentPage > mPageCount) {
                mRvAdapter.loadMoreEnd()
                return
            }
        } else {
            mCurrentPage = 0
        }

        getApi(mCurrentPage.toString()).subscribe(object : BaseSubscriber<List<T>>() {

            override fun onStart() {
                super.onStart()
                if (status === LoadStatus.LOADING) {
                    if(recyclerView != null){
                        recyclerView.scrollToPosition(0)
                    }
                }
            }

            override fun onNext(t: List<T>) {
                when (status) {
                    LoadStatus.LOADING, LoadStatus.REFRESH -> {
                        if (t.size < PAGE_SIZE) {
                            mRvAdapter.loadMoreEnd()
                        }
                        mRvAdapter.setNewData(t) // 刷新或者第一次加载成功
                        ptrFrameLayout.refreshComplete()//刷新完成
                    }
                    LoadStatus.MORE -> {
                        if (mCurrentPage >= mPageCount || t.size < PAGE_SIZE) {
                            mRvAdapter.loadMoreEnd() //没有更多数据了
                        }
                        mRvAdapter.loadMoreComplete() // 加载更多完成
                        mRvAdapter.addData(t)
                    }
                    else -> {
                    }
                }
            }

            override fun onCompleted() {
                super.onCompleted()
                when (status) {
                    LoadStatus.REFRESH -> ptrFrameLayout.refreshComplete()//刷新完成
                    LoadStatus.MORE -> {
                    }
                    else -> {
                    }
                }
            }

            override fun onError(e: Throwable?) {
                when (status) {
                    LoadStatus.LOADING -> ptrFrameLayout.refreshComplete()
                    LoadStatus.REFRESH -> ptrFrameLayout.refreshComplete()//刷新完成
                    LoadStatus.MORE -> mRvAdapter.loadMoreFail() // 加载更多失败
                }
            }

        })
    }

    protected abstract fun getCurrentPage(): String

    protected abstract fun getApi(currNum: String): Observable<List<T>>


    protected fun getLayoutManger(): RecyclerView.LayoutManager? {
        return null
    }

    protected abstract fun getRecyclerViewAdapter(): BaseRvAdapter<T>

    protected open fun getItemDecoration(): RecyclerView.ItemDecoration? {
        return null
    }

    protected abstract fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int)

    protected open fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {

    }

    protected open fun onItemLongClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {

    }

    protected open fun onItemChildLongClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {

    }

    private class RecyclerItemClickListener(val fragment: BaseRvFragment<*>) : SimpleClickListener() {

        override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
            fragment.onItemChildClick(adapter!!, view!!, position)
        }

        override fun onItemLongClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
            fragment.onItemLongClick(adapter!!, view!!, position)
        }

        override fun onItemChildLongClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
            fragment.onItemChildLongClick(adapter!!, view!!, position)
        }

        override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
            fragment.onItemClick(adapter!!, view!!, position);
        }

    }

}