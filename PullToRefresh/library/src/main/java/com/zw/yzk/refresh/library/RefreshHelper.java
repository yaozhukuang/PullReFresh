package com.zw.yzk.refresh.library;

import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.zw.yzk.refresh.library.MyTimer;
import com.zw.yzk.refresh.library.State;
import com.zw.yzk.refresh.library.loadmore.DefaultLoadMoreViewCreator;
import com.zw.yzk.refresh.library.loadmore.LoadMoreViewCreator;
import com.zw.yzk.refresh.library.loadmore.WrapperAdapter;
import com.zw.yzk.refresh.library.refrsh.DefaultRefreshViewCreator;
import com.zw.yzk.refresh.library.refrsh.RefreshViewCreator;


/**
 * Created by wei on 2016/4/23.
 */
public class RefreshHelper {
    private static int DEFAULT_REFRESH_DISTANCE = 249;

    //刷新成功停留时间
    private int successDelay;
    //刷新过程中的状态
    private State state;
    //释放刷新的高度
    private float refreshDist;
    //回滚速度
    private float moveSpeed;
    // 手指滑动距离与下拉头的滑动距离比，中间会随正切函数变化
    private float radio;
    //下拉布局Creator
    private RefreshViewCreator refreshViewCreator;
    //RecyclerView实际使用Adapter
    private WrapperAdapter wrapperAdapter;
    //下拉刷新是否可用
    private boolean refreshEnable = true;
    //上拉加载是否可用
    private boolean loadMoreEnable = true;
    //定时器
    private MyTimer timer;
    //RecyclerView Header
    private View header;

    public RefreshHelper(Handler updateHandler) {
        setDefault();

        timer = new MyTimer(updateHandler);
    }

    //初始化默认值
    private void setDefault() {
        state = State.INIT;
        refreshDist = -1;
        moveSpeed = 8;
        radio = 2;
        loadMoreEnable = true;
        successDelay = 1000;
    }

    //设置RecyclerView Header
    public void setHeader(View header) {
        if (header == null) {
            return;
        }
        if (wrapperAdapter == null) {
            throw new IllegalStateException("recycler view must has a adapter");
        }
        wrapperAdapter.setHeader(header);
    }

    //移除RecyclerView Header
    public void removeHeader() {
        if (wrapperAdapter == null) {
            throw new IllegalStateException("recycler view must has a adapter");
        }
        wrapperAdapter.removeHeader();
    }

    //创建实际RecyclerView使用的Adapter
    public WrapperAdapter createAdapter(RecyclerView.Adapter adapter, LoadMoreViewCreator creator) {
        if (wrapperAdapter == null) {
            wrapperAdapter = new WrapperAdapter(adapter, creator == null ? new DefaultLoadMoreViewCreator() : creator);
        }
        adapter.registerAdapterDataObserver(mDataObserver);

        return wrapperAdapter;
    }

    //是否将显示正在加载的item添加到adapter中
    private void onAdapterDataChanged(State refreshState) {
        if (wrapperAdapter == null) {
            return;
        }
        boolean showLoadingItem = (state != State.REFRESHING && wrapperAdapter.getOriginAdapter().getItemCount() > 0) && loadMoreEnable;

        wrapperAdapter.displayLoadingRow(showLoadingItem, refreshState);
    }

    //判断recyclerView是否到达最后一个item
    public boolean checkEndOffset(RecyclerView recyclerView) {
        int visibleItemCount = recyclerView.getChildCount();
        int totalItemCount = recyclerView.getLayoutManager().getItemCount();

        int firstVisibleItemPosition;
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            firstVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
        } else if (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
            if (recyclerView.getLayoutManager().getChildCount() > 0) {
                firstVisibleItemPosition = ((StaggeredGridLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPositions(null)[0];
            } else {
                firstVisibleItemPosition = 0;
            }
        } else {
            throw new IllegalStateException("LayoutManager needs to subclass LinearLayoutManager or StaggeredGridLayoutManager");
        }

        // 最后一个Item是否显示出来
        return (totalItemCount - visibleItemCount) <= firstVisibleItemPosition && visibleItemCount != 0;
    }

    //查询下拉刷新是否可用
    public boolean isRefreshEnable() {
        return refreshEnable;
    }

    //设置下拉刷新可用状态
    public void setRefreshEnable(boolean refreshEnable) {
        this.refreshEnable = refreshEnable;
    }

    //查询上拉加载是否可用
    public boolean isLoadMoreEnable() {
        return this.loadMoreEnable;
    }

    //设置上拉加载是否可用
    public void setLoadMoreEnable(boolean loadMoreEnable) {
        this.loadMoreEnable = loadMoreEnable;
    }

    //获取当前刷新状态
    public State getState() {
        return state;
    }

    //获取刷新触发的距离
    public float getRefreshDist() {
        return refreshDist;
    }

    //设置刷新触发距离
    public void setRefreshDist(float refreshDist) {
        if (this.refreshDist == -1) {
            this.refreshDist = refreshDist <= 0 ? DEFAULT_REFRESH_DISTANCE : refreshDist;
        }
    }

    //获取刷新成功RefreshView的停留时间
    public int getSuccessDelay() {
        return successDelay;
    }

    //设置刷新成功RefreshView的停留时间
    public void setSuccessDelay(int successDelay) {
        if (successDelay >= 0) {
            this.successDelay = successDelay;
        }
    }

    //获取RefreshView 回滚速度
    public float getMoveSpeed() {
        return moveSpeed;
    }

    //设置Refresh View 回滚速度
    public void setMoveSpeed(float moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

    //获取手指滑动距离与下拉头的滑动距离比
    public float getRadio() {
        return radio;
    }

    //设置手指滑动距离与下拉头的滑动距离比，中间会随正切函数变化
    public void setRadio(float radio) {
        this.radio = radio;
    }

    //获取RefreshViewCreator，如果没有设置，则取默认值
    public RefreshViewCreator getRefreshViewCreator() {
        if (refreshViewCreator == null) {
            refreshViewCreator = new DefaultRefreshViewCreator();
        }
        return refreshViewCreator;
    }

    //设置RefreshViewCreator
    public void setRefreshViewCreator(RefreshViewCreator refreshViewCreator) {
        this.refreshViewCreator = refreshViewCreator == null ? new DefaultRefreshViewCreator() : refreshViewCreator;
    }

    //获取TimerTask
    public MyTimer getTimer() {
        return this.timer;
    }

    //设置当前刷新状态
    public void setState(State state) {
        this.state = state;

        switch (state) {
            case INIT:
                // 下拉布局初始状态
                refreshViewCreator.stateInit();
                break;
            case RELEASE_TO_REFRESH:
                // 释放刷新状态
                refreshViewCreator.stateReleaseToRefresh();
                break;
            case REFRESHING:
                // 正在刷新状态
                refreshViewCreator.stateRefreshing();
                break;
            case REFRESH_SUCCEED:
                //刷新成功
                refreshViewCreator.stateRefreshSucceed();
                break;
            case REFRESH_FAIL:
                //刷新失败
                refreshViewCreator.stateRefreshFail();
                break;
            case LOADING_SUCCEED:
                //加载成功
                this.state = State.DONE;
                break;
            case LOADING_FAIL:
                //加载失败
                this.state = State.DONE;
                break;
            case NO_ITEM_LOADING:
                //已经全部加载完成
            case DONE:
                break;
        }
        onAdapterDataChanged(state);
    }

    private final RecyclerView.AdapterDataObserver mDataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            wrapperAdapter.notifyDataSetChanged();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            wrapperAdapter.notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            wrapperAdapter.notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            wrapperAdapter.notifyItemRangeChanged(positionStart, itemCount, payload);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            wrapperAdapter.notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            wrapperAdapter.notifyItemMoved(fromPosition, toPosition);
        }
    };
}
