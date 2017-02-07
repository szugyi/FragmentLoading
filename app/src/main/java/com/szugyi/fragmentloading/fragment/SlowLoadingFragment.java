package com.szugyi.fragmentloading.fragment;

public class SlowLoadingFragment extends LoadingFragment {
    @Override
    protected String getName() {
        return SlowLoadingFragment.class.getSimpleName();
    }

    @Override
    protected int getLoadMillis() {
        return 20000;
    }
}
