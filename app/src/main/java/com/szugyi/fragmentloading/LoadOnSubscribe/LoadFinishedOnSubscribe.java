package com.szugyi.fragmentloading.LoadOnSubscribe;

import com.szugyi.fragmentloading.fragment.LoadingFragment;

import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;

public class LoadFinishedOnSubscribe implements Observable.OnSubscribe<Boolean> {
    private final OnLoadFinishedSubject subject;

    public LoadFinishedOnSubscribe(OnLoadFinishedSubject subject) {
        this.subject = subject;
    }

    @Override
    public void call(final Subscriber<? super Boolean> subscriber) {
        subject.setOnLoadFinishedListener(new OnLoadFinishedListener() {
            @Override
            public void onLoadFinished(boolean isLoadSuccessful) {
                if (subscriber.isUnsubscribed()) return;
                subscriber.onNext(isLoadSuccessful);
                subscriber.onCompleted();
            }
        });

        subscriber.add(new MainThreadSubscription() {
            @Override
            protected void onUnsubscribe() {
                subject.setOnLoadFinishedListener(null);
            }
        });
    }
}
