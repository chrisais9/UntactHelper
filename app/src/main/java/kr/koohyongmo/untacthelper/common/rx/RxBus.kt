package kr.koohyongmo.untacthelper.common.rx

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

object RxBus {
    private val publisher = PublishSubject.create<Any>()

    fun publish(event: Any) {
        publisher.onNext(event)
    }

    fun <T> listen(eventType: Class<T>): Observable<T> = publisher.ofType(eventType)
}

class RxEvents() {
    class OnEcampusClassReady // 이캠퍼스 수강 목록이 준비 되었다고 알려줌
}