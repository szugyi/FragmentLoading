package com.szugyi.fragmentloading;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;

import com.szugyi.fragmentloading.fragment.FastLoadingFragment;
import com.szugyi.fragmentloading.fragment.ImmediateLoadingFragment;
import com.szugyi.fragmentloading.fragment.LoadingFragment;
import com.szugyi.fragmentloading.fragment.SlowLoadingFragment;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.FuncN;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private EditText searchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchEditText = (EditText) findViewById(R.id.search_editText);

        FragmentManager fragmentManager = getSupportFragmentManager();
        List<Observable<Boolean>> observableList = new ArrayList<>();
        if (fragmentManager.getFragments() == null) {
            List<LoadingFragment> fragmentList = new ArrayList<>();
            fragmentList.add(new ImmediateLoadingFragment());
            fragmentList.add(new FastLoadingFragment());
            fragmentList.add(new SlowLoadingFragment());
            fragmentList.add(new FastLoadingFragment());

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            for (LoadingFragment fragment : fragmentList) {
                observableList.add(fragment.getLoadObservable());
                transaction.add(R.id.fragment_wrapper, fragment);
            }
            transaction.commit();
        } else {
            for (Fragment fragment : fragmentManager.getFragments()) {
                observableList.add(((LoadingFragment) fragment).getLoadObservable());
            }
        }

        Observable.zip(observableList, new FuncN<Boolean>() {
            @Override
            public Boolean call(Object... args) {
                Log.v(MainActivity.class.getSimpleName(), "Zip ended");
                searchEditText.setEnabled(true);
                return null;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }
}
