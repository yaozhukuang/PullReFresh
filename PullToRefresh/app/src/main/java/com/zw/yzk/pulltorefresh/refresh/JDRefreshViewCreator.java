package com.zw.yzk.pulltorefresh.refresh;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.zw.yzk.pulltorefresh.R;
import com.zw.yzk.refresh.library.refrsh.RefreshViewCreator;

/**
 * Created by wei on 2016/4/27.
 */
public class JDRefreshViewCreator implements RefreshViewCreator {

    private View refreshLayout;
    private ImageView speed;
    private ImageView run;
    private AnimationDrawable runDrawable;
    private Animation speedAnimation;
    private int runHeight;

    @Override
    public View createView(Context context) {
        refreshLayout = LayoutInflater.from(context).inflate(R.layout.layout_jd_refresh_view, null);
        speed = (ImageView) refreshLayout.findViewById(R.id.ivSpeed);
        run = (ImageView) refreshLayout.findViewById(R.id.ivRefresh);
        runDrawable = (AnimationDrawable) run.getBackground();
        speedAnimation = AnimationUtils.loadAnimation(context, R.anim.twinkle);
        runHeight = runDrawable.getIntrinsicHeight();
        return refreshLayout;
    }

    @Override
    public void stateInit() {


        speed.clearAnimation();
        speed.setVisibility(View.VISIBLE);
        if (runDrawable.isRunning()) {
            runDrawable.stop();
        }
    }

    @Override
    public void stateReleaseToRefresh() {
    }

    @Override
    public void stateRefreshing() {
        speed.setVisibility(View.VISIBLE);
        speed.startAnimation(speedAnimation);
        if (!runDrawable.isRunning()) {
            runDrawable.start();
        }
    }

    @Override
    public void stateRefreshSucceed() {

    }

    @Override
    public void stateRefreshFail() {

    }



    @Override
    public void stateMove(float percent) {
//        ViewGroup.LayoutParams params = run.getLayoutParams();
//        params.height = (int) (runHeight * percent);
//        run.setLayoutParams(params);
    }
}
