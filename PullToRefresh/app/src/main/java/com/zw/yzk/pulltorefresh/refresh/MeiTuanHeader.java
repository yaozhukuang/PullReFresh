package com.zw.yzk.pulltorefresh.refresh;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zw.yzk.pulltorefresh.R;
import com.zw.yzk.refresh.library.refrsh.RefreshViewCreator;

/**
 * Created by wei on 2016/4/27.
 */
public class MeiTuanHeader implements RefreshViewCreator {
    public View refreshLayout;
    public ImageView beforeRelease;
    public ImageView releaseToRefresh;
    public ImageView refreshing;
    public AnimationDrawable releaseDrawable;
    public AnimationDrawable refreshingDrawable;

    public int refreshViewHeight;

    @Override
    public View createView(Context context) {
        refreshLayout = LayoutInflater.from(context).inflate(R.layout.layout_meituan_refresh_view, null);
        beforeRelease = (ImageView) refreshLayout.findViewById(R.id.before_release);
        releaseToRefresh = (ImageView) refreshLayout.findViewById(R.id.release_to_refresh);
        refreshing = (ImageView) refreshLayout.findViewById(R.id.refreshing);
        releaseDrawable = (AnimationDrawable) releaseToRefresh.getBackground();
        refreshingDrawable = (AnimationDrawable) refreshing.getBackground();

        refreshViewHeight = beforeRelease.getDrawable().getIntrinsicHeight();
        return refreshLayout;
    }

    @Override
    public void stateInit() {
        beforeRelease.setVisibility(View.VISIBLE);
        releaseToRefresh.setVisibility(View.GONE);
        refreshing.setVisibility(View.INVISIBLE);
        if (releaseDrawable.isRunning()) {
            releaseDrawable.stop();
        }
        if (refreshingDrawable.isRunning()) {
            refreshingDrawable.stop();
        }
    }

    @Override
    public void stateReleaseToRefresh() {
        beforeRelease.setVisibility(View.GONE);
        releaseToRefresh.setVisibility(View.VISIBLE);
        refreshing.setVisibility(View.INVISIBLE);
        if (!releaseDrawable.isRunning()) {
            releaseDrawable.start();
        }
    }

    @Override
    public void stateRefreshing() {
        beforeRelease.setVisibility(View.GONE);
        releaseToRefresh.setVisibility(View.GONE);
        refreshing.setVisibility(View.VISIBLE);
        if (!refreshingDrawable.isRunning()) {
            refreshingDrawable.start();
        }
    }

    @Override
    public void stateRefreshSucceed() {
        releaseToRefresh.clearAnimation();
        refreshing.clearAnimation();

        beforeRelease.setVisibility(View.VISIBLE);
        releaseToRefresh.setVisibility(View.GONE);
        refreshing.setVisibility(View.INVISIBLE);
    }

    @Override
    public void stateRefreshFail() {
        releaseToRefresh.clearAnimation();
        refreshing.clearAnimation();

        beforeRelease.setVisibility(View.VISIBLE);
        releaseToRefresh.setVisibility(View.GONE);
        refreshing.setVisibility(View.GONE);
    }

    @Override
    public void stateMove(float percent) {
        ViewGroup.LayoutParams params = beforeRelease.getLayoutParams();
        params.height = (int) (refreshViewHeight * percent);
        beforeRelease.setLayoutParams(params);
    }
}
