package com.zw.yzk.pulltorefresh.loadmore;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import com.zw.yzk.pulltorefresh.R;
import com.zw.yzk.refresh.library.loadmore.LoadMoreViewCreator;

/**
 * Created by wei on 2016/5/3.
 */
public class CustomLoadMoreCreator implements LoadMoreViewCreator<CustomLoadMoreCreator.MyHolder> {

    @Override
    public CustomLoadMoreCreator.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_custom_load_moe_view, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void loading(MyHolder holder, int position) {
        holder.icon.setVisibility(View.VISIBLE);
        holder.hint.setText("正在加载...");
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(holder.icon,
                "rotationY", 0, -360);
        animatorX.setDuration(800);
        animatorX.setRepeatCount(ValueAnimator.INFINITE);
        animatorX.setInterpolator(new LinearInterpolator());
        animatorX.start();
    }

    @Override
    public void loadFail(MyHolder holder, int position) {
        holder.icon.clearAnimation();
        holder.icon.setVisibility(View.GONE);
        holder.hint.setText("加载失败");
    }

    @Override
    public void noItemToLoad(MyHolder holder, int position) {
        holder.icon.clearAnimation();
        holder.icon.setVisibility(View.GONE);
        holder.hint.setText("已经全部加载完毕");
    }


    public static class MyHolder extends RecyclerView.ViewHolder {
        public View icon;
        public TextView hint;

        public MyHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            hint = (TextView) itemView.findViewById(R.id.hint);
        }
    }
}
