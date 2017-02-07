package com.szugyi.fragmentloading.fragment;

public class ImmediateLoadingFragment extends LoadingFragment {
    @Override
    protected String getName() {
        return ImmediateLoadingFragment.class.getSimpleName();
    }

    @Override
    protected int getLoadMillis() {
        return 0;
    }
}
