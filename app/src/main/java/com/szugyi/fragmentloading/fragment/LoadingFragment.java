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

import com.szugyi.fragmentloading.LoadOnSubscribe.LoadFinishedOnSubscribe;
import com.szugyi.fragmentloading.LoadOnSubscribe.OnLoadFinishedSubject;
import com.szugyi.fragmentloading.LoadOnSubscribe.OnLoadFinishedListener;
import com.szugyi.fragmentloading.R;

import rx.Observable;

public abstract class LoadingFragment extends Fragment implements OnLoadFinishedSubject {
    protected OnLoadFinishedListener onLoadFinishedListener;

    protected ProgressBar progressBar;
    protected TextView nameTv;
    protected CountDownTimer timer;

    protected abstract String getName();

    protected abstract int getLoadMillis();

    @Override
    public void setOnLoadFinishedListener(OnLoadFinishedListener listener) {
        this.onLoadFinishedListener = listener;
    }

    public Observable<Boolean> getLoadObservable() {
        return Observable.create(new LoadFinishedOnSubscribe(this));
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
            notifyListener(false);
            timer.cancel();
            timer = null;
        }
        onLoadFinishedListener = null;
    }
}
