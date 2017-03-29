package com.szugyi.fragmentloading.rxload;

import rx.Observable;

public interface OnLoadFinishedSubject {

    void setOnLoadFinishedListener(OnLoadFinishedListener onLoadFinishedListener);

    Observable<Boolean> getLoadObservable();
}
