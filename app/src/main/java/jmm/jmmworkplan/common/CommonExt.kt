package jmm.jmmworkplan.common

import jmm.jmmworkplan.net.BaseSubscriber
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

//Kotlin通用扩展


/*
    扩展Observable执行
 */
fun <T> Observable<T>.excute(subscriber: BaseSubscriber<T>) {
    this.subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(subscriber)
}
