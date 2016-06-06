package com.zw.yzk.refresh.library.loadmore;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

public class GridSpanLookup {

    private final int loadingListItemSpan;

    public GridSpanLookup(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof GridLayoutManager) {
            loadingListItemSpan = ((GridLayoutManager) layoutManager).getSpanCount();
        } else {
            loadingListItemSpan = 1;
        }
    }

    public int getSpanSize() {
        return loadingListItemSpan;
    }
}