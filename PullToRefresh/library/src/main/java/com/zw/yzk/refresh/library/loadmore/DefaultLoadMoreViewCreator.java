package com.zw.yzk.refresh.library.loadmore;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zw.yzk.refresh.library.R;


/**
 * Created by wei on 2016/4/25.
 */
public class DefaultLoadMoreViewCreator implements LoadMoreViewCreator<DefaultLoadMoreViewCreator.DefaultLoadingHolder> {

    @Override
    public DefaultLoadMoreViewCreator.DefaultLoadingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.loading_row, parent, false);
        return new DefaultLoadingHolder(view);
    }

    @Override
    public void loading(DefaultLoadingHolder holder, int position) {
        holder.progress.setVisibility(View.VISIBLE);
        holder.noMoreItem.setVisibility(View.GONE);
        holder.loadingFail.setVisibility(View.GONE);

    }

    @Override
    public void loadFail(DefaultLoadingHolder holder, int viewType) {
        holder.progress.setVisibility(View.GONE);
        holder.noMoreItem.setVisibility(View.GONE);
        holder.loadingFail.setVisibility(View.VISIBLE);
    }

    @Override
    public void noItemToLoad(DefaultLoadingHolder holder, int viewType) {
        holder.progress.setVisibility(View.GONE);
        holder.noMoreItem.setVisibility(View.VISIBLE);
        holder.loadingFail.setVisibility(View.GONE);
    }

    public static class DefaultLoadingHolder extends RecyclerView.ViewHolder {
        public ProgressBar progress;
        public TextView loadingFail;
        public TextView noMoreItem;

        public DefaultLoadingHolder(View itemView) {
            super(itemView);
            progress = (ProgressBar) itemView.findViewById(R.id.progress);
            loadingFail = (TextView) itemView.findViewById(R.id.loading_fail);
            noMoreItem = (TextView) itemView.findViewById(R.id.no_more);
        }
    }
}
