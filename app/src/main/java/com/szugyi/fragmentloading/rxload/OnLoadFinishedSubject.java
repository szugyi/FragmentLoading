package com.szugyi.fragmentloading.rxload;

public interface OnLoadFinishedSubject {

    void setOnLoadFinishedListener(OnLoadFinishedListener onLoadFinishedListener);

    Observable<Boolean> getLoadObservable();
}
