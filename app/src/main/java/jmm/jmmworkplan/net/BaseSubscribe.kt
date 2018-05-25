package jmm.jmmworkplan.net

import rx.Subscriber

/**
 * user:Administrator
 * time:2018 05 24 13:47
 * package_name:jmm.jmmworkplan.net
 */
/*
    Rx订阅者默认实现
 */
open class BaseSubscriber<T>():Subscriber<T>() {

    override fun onCompleted() {
    }

    override fun onNext(t: T) {
    }

    override fun onError(e: Throwable?) {
    }
}
