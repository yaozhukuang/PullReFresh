package com.zw.yzk.refresh.library.loadmore;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.zw.yzk.refresh.library.State;


public final class WrapperAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    int ITEM_VIEW_TYPE_LOADING = Integer.MAX_VALUE - 50;
    int ITEM_VIEW_TYPE_HEADER = Integer.MAX_VALUE - 51;

    private final RecyclerView.Adapter originAdapter;
    private final LoadMoreViewCreator loadingListItemCreator;
    private boolean displayLoadingRow;
    private State loadState;
    private View header;


    public WrapperAdapter(RecyclerView.Adapter adapter, LoadMoreViewCreator creator) {
        this.originAdapter = adapter;
        this.loadingListItemCreator = creator;
        displayLoadingRow = adapter.getItemCount() > 0;
        header = null;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_VIEW_TYPE_HEADER) {
            return new RecyclerView.ViewHolder(header) {
            };
        } else if (viewType == ITEM_VIEW_TYPE_LOADING) {
            return loadingListItemCreator.onCreateViewHolder(parent, viewType);
        } else {
            return originAdapter.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isHeaderRow(position)) {
            return;
        }
        int realPosition = hasHeader() ? position - 1 : position;
        if (isLoadingRow(position)) {
            if (loadState == State.LOADING) {
                loadingListItemCreator.loading(holder, realPosition);
            } else if (loadState == State.LOADING_FAIL) {
                loadingListItemCreator.loadFail(holder, realPosition);
            } else if (loadState == State.NO_ITEM_LOADING) {
                loadingListItemCreator.noItemToLoad(holder, realPosition);
            }
        } else {
            originAdapter.onBindViewHolder(holder, realPosition);
        }
    }

    @Override
    public int getItemCount() {
        int extra = 0;
        extra += hasHeader() ? 1 : 0;
        extra += displayLoadingRow ? 1 : 0;
        return extra + originAdapter.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderRow(position)) {
            return ITEM_VIEW_TYPE_HEADER;
        } else if (isLoadingRow(position)) {
            return ITEM_VIEW_TYPE_LOADING;
        } else {
            return hasHeader() ? originAdapter.getItemViewType(position - 1) : originAdapter.getItemViewType(position);
        }
    }

    @Override
    public long getItemId(int position) {
        if (isHeaderRow(position)) {
            return RecyclerView.NO_ID;
        } else if (isLoadingRow(position)) {
            return RecyclerView.NO_ID;
        } else {
            return originAdapter.getItemId(position);
        }
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
        originAdapter.setHasStableIds(hasStableIds);
    }

    public RecyclerView.Adapter getOriginAdapter() {
        return originAdapter;
    }

    public boolean isDisplayLoadingRow() {
        return displayLoadingRow;
    }

    public void displayLoadingRow(boolean displayLoadingRow, State loadingState) {
        if (this.displayLoadingRow != displayLoadingRow) {
            this.displayLoadingRow = displayLoadingRow;
        }
        loadState = loadingState;
        if (loadState != State.LOADING && loadState != State.LOADING_FAIL && loadState != State.NO_ITEM_LOADING) {
            this.displayLoadingRow = false;
        }
        notifyDataSetChanged();
    }

    public boolean isLoadingRow(int position) {
        return displayLoadingRow && position == getLoadingRowPosition();
    }

    private int getLoadingRowPosition() {
        return displayLoadingRow ? getItemCount() - 1 : -1;
    }

    public void setHeader(View header) {
        this.header = header;
        notifyDataSetChanged();
    }

    public void removeHeader() {
        this.header = null;
        notifyDataSetChanged();
    }

    public boolean isHeaderRow(int position) {
        return header != null && position == 0;
    }

    private boolean hasHeader() {
        return header != null;
    }
}