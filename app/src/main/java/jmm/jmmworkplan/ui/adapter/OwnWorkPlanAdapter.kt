package jmm.jmmworkplan.ui.adapter

import com.chad.library.adapter.base.BaseViewHolder
import jmm.jmmworkplan.R
import jmm.jmmworkplan.bean.WorkInfo

/**
 * user:Administrator
 * time:2018 05 24 15:44
 * package_name:jmm.jmmworkplan.ui.adapter
 */


class OwnWorkPlanAdapter : BaseRvAdapter<WorkInfo.DataBean>(R.layout.item_own) {

     override fun convert(helper: BaseViewHolder, item: WorkInfo.DataBean) {
        helper.setText(R.id.tvName, item.name)
        helper.setText(R.id.tvDate, item.date)
        helper.setText(R.id.tvPlan, item.plan_content)
    }
}