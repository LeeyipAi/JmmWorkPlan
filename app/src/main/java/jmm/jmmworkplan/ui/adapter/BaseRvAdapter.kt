package jmm.jmmworkplan.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * user:Administrator
 * time:2018 05 24 15:30
 * package_name:jmm.jmmworkplan.ui.adapter
 */
abstract class BaseRvAdapter<T> : BaseQuickAdapter<T, BaseViewHolder> {

    constructor(layoutResId: Int) : super(layoutResId) {}

    constructor(data: List<T>) : super(data) {}

}
