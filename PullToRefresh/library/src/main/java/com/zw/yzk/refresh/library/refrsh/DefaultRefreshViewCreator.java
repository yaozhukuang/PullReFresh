package com.zw.yzk.refresh.library.refrsh;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import com.zw.yzk.refresh.library.R;


/**
 * Created by wei on 2016/4/23.
 */
public class DefaultRefreshViewCreator implements RefreshViewCreator {

    // 下拉布局
    private View refreshView;
    // 下拉的箭头
    private View pullView;
    // 正在刷新的图标
    private View refreshingView;
    // 刷新结果图标
    private View refreshStateImageView;
    // 刷新结果：成功或失败
    private TextView refreshStateTextView;

    // 下拉箭头的转180°动画
    private RotateAnimation rotateAnimation;
    // 均匀旋转动画
    private RotateAnimation refreshingAnimation;

    @Override
    public View createView(Context context) {
        //初始化控件
        refreshView = LayoutInflater.from(context).inflate(R.layout.refresh_head, null);
        pullView = refreshView.findViewById(R.id.pull_icon);
        refreshStateTextView = (TextView) refreshView.findViewById(R.id.state_tv);
        refreshingView = refreshView.findViewById(R.id.refreshing_icon);
        refreshStateImageView = refreshView.findViewById(R.id.state_iv);

        //初始化动画
        rotateAnimation = (RotateAnimation) AnimationUtils.loadAnimation(context, R.anim.reverse_anim);
        refreshingAnimation = (RotateAnimation) AnimationUtils.loadAnimation(context, R.anim.rotating);
        // 添加匀速转动动画
        LinearInterpolator lir = new LinearInterpolator();
        rotateAnimation.setInterpolator(lir);
        refreshingAnimation.setInterpolator(lir);

        return refreshView;
    }

    @Override
    public void stateInit() {
        refreshingView.setVisibility(View.GONE);
        refreshingView.clearAnimation();
        refreshStateImageView.setVisibility(View.GONE);
        refreshStateTextView.setText(R.string.pull_to_refresh);
        pullView.clearAnimation();
        pullView.setVisibility(View.VISIBLE);
    }

    @Override
    public void stateReleaseToRefresh() {
        refreshingView.setVisibility(View.GONE);
        refreshingView.clearAnimation();
        refreshStateTextView.setText(R.string.release_to_refresh);
        pullView.startAnimation(rotateAnimation);
    }

    @Override
    public void stateRefreshing() {
        pullView.clearAnimation();
        refreshingView.setVisibility(View.VISIBLE);
        pullView.setVisibility(View.GONE);
        refreshingView.startAnimation(refreshingAnimation);
        refreshStateTextView.setText(R.string.refreshing);
    }

    @Override
    public void stateRefreshSucceed() {
        refreshingView.clearAnimation();
        refreshingView.setVisibility(View.GONE);
        // 刷新成功
        refreshStateImageView.setVisibility(View.VISIBLE);
        refreshStateTextView.setText(R.string.refresh_succeed);
    }

    @Override
    public void stateRefreshFail() {
        refreshingView.clearAnimation();
        refreshingView.setVisibility(View.GONE);
        // 刷新失败
        refreshStateImageView.setVisibility(View.VISIBLE);
        refreshStateTextView.setText(R.string.refresh_fail);
    }

    @Override
    public void stateMove(float percent) {
    }
}
