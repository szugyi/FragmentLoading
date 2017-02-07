package com.szugyi.fragmentloading.fragment;

public class FastLoadingFragment extends LoadingFragment {
    @Override
    protected String getName() {
        return FastLoadingFragment.class.getSimpleName();
    }

    @Override
    protected int getLoadMillis() {
        return 750;
    }
}
