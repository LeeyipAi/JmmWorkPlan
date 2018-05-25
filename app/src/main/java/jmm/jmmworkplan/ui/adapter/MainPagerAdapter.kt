package jmm.jmmworkplan.ui.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import jmm.jmmworkplan.ui.fragment.OwnWorkPlanFragment
import jmm.jmmworkplan.ui.fragment.WorkPlanFragment

/**
 * user:Administrator
 * time:2018 05 24 15:08
 * package_name:jmm.jmmworkplan.ui.adapter
 */
class MainPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return if (position == 0) OwnWorkPlanFragment() else WorkPlanFragment()
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return if (position == 0) "我的工作计划" else "工作计划"
    }
}