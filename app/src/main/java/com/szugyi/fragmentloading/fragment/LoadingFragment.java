package com.szugyi.fragmentloading.fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.szugyi.fragmentloading.R;

import rx.Observable;
import rx.Subscriber;

public abstract class LoadingFragment extends Fragment {
    protected OnLoadFinishedListener onLoadFinishedListener;

    protected ProgressBar progressBar;
    protected TextView nameTv;
    protected CountDownTimer timer;

    protected abstract String getName();

    protected abstract int getLoadMillis();

    public void setOnLoadFinishedListener(OnLoadFinishedListener listener) {
        this.onLoadFinishedListener = listener;
    }

    public Observable<Boolean> getLoadObservable() {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(final Subscriber<? super Boolean> subscriber) {
                setOnLoadFinishedListener(new OnLoadFinishedListener() {
                    @Override
                    public void onLoadFinished(boolean isLoadFinished) {
                        if (subscriber.isUnsubscribed()) return;
                        subscriber.onNext(isLoadFinished);
                        subscriber.onCompleted();
                    }
                });
            }
        });
    }

    private void notifyListener(boolean isFinished) {
        if (onLoadFinishedListener != null) {
            onLoadFinishedListener.onLoadFinished(isFinished);
        }
    }

    private void loadFinished() {
        notifyListener(true);
        progressBar.setVisibility(View.GONE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_loading, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        nameTv = (TextView) view.findViewById(R.id.name_textView);
        nameTv.setText(getName());

        int loadMillis = getLoadMillis();
        if (0 < loadMillis) {
            timer = new CountDownTimer(loadMillis, loadMillis) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    loadFinished();
                }
            };
            timer.start();
        } else {
            loadFinished();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        onLoadFinishedListener = null;
    }

    public interface OnLoadFinishedListener {
        void onLoadFinished(boolean isLoadFinished);
    }
}
