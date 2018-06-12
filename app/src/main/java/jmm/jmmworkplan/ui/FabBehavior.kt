package jmm.jmmworkplan.ui

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.FloatingActionButton
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator

/**
 * user:Administrator
 * time:2018 05 29 15:50
 * package_name:jmm.jmmworkplan.ui
 */
class FabBehavior(context: Context, attrs: AttributeSet) : FloatingActionButton.Behavior() {
    private var visible = true//是否可见

    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout,
                                     child: FloatingActionButton, directTargetChild: View, target: View,
                                     nestedScrollAxes: Int): Boolean {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL || super.onStartNestedScroll(coordinatorLayout, child, directTargetChild,
                target, nestedScrollAxes)
    }

    override fun onNestedScroll(coordinatorLayout: CoordinatorLayout,
                                child: FloatingActionButton, target: View, dxConsumed: Int,
                                dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed,
                dxUnconsumed, dyUnconsumed)
        if (dyConsumed > 0 && visible) {
            //show
            visible = false
            onHide(child, coordinatorLayout.getChildAt(0))
        } else if (dyConsumed < 0) {
            //hide
            visible = true
            onShow(child, coordinatorLayout.getChildAt(0))
        }

    }

    fun onHide(fab: FloatingActionButton, toolbar: View) {
        //        toolbar.animate().translationY(-toolbar.getHeight()).setInterpolator(new AccelerateInterpolator(3));
        val layoutParams = fab.layoutParams as CoordinatorLayout.LayoutParams
        fab.animate().translationY((fab.height + layoutParams.bottomMargin).toFloat()).interpolator = AccelerateInterpolator(3f)
        //        fab.animate().scaleX(0).scaleY(0).setInterpolator(new AccelerateInterpolator(3));
    }

    fun onShow(fab: FloatingActionButton, toolbar: View) {
        //        toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(3));
        fab.animate().translationY(0f).interpolator = DecelerateInterpolator(3f)
        //        fab.animate().scaleX(1).scaleY(1).setInterpolator(new AccelerateInterpolator(3));
    }

}
