package com.zw.yzk.pulltorefresh.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zw.yzk.refresh.library.refrsh.RefreshViewCreator;

/**
 * Created by wei on 2016/4/29.
 */
public abstract class BaseFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(setContent(), container, false);
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    public abstract int setContent();

    public abstract void initView(View view);

    public abstract void initData();

    public abstract void setRefreshViewCreator(RefreshViewCreator creator);
}
