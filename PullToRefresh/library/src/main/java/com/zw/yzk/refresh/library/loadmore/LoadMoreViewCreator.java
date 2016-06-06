package com.zw.yzk.refresh.library.loadmore;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;


/**
 * RecyclerView creator that will be called to create and bind loading list item
 */
public interface LoadMoreViewCreator<VH extends RecyclerView.ViewHolder> {

    VH onCreateViewHolder(ViewGroup parent, int viewType);

    void loading(VH holder, int position);

    void loadFail(VH holder, int position);

    void noItemToLoad(VH holder, int position);

}