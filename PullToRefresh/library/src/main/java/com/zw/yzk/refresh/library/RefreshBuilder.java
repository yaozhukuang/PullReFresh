package com.zw.yzk.refresh.library;

import android.view.View;
import android.view.ViewGroup;

import com.zw.yzk.refresh.library.loadmore.LoadMoreViewCreator;
import com.zw.yzk.refresh.library.refrsh.RefreshViewCreator;

/**
 * Created by wei on 2016/4/26.
 */
public class RefreshBuilder {

    public static class RefreshParams {
        //下拉刷新上拉加载布局
        public PullToRefreshLayout pullToRefreshLayout;
        //Refresh帮助类
        public RefreshHelper refreshHelper;

        //监听事件
        public PullToRefreshLayout.OnRefreshListener listener;
        //下拉刷新是否可用
        public boolean refreshEnable = true;
        //上拉加载是否可用
        public boolean loadMoreEnable = true;
        //刷新成功后的停留时间
        public int successDelay = 1000;
        //contentView回滚速度
        public float moveSpeed = 8f;
        // 手指滑动距离与下拉头的滑动距离比初始值，中间会随正切函数变化
        public float radio = 2f;
        //设置RefreshView
        public RefreshViewCreator refreshViewCreator;
        //设置LoadMoreView
        public LoadMoreViewCreator loadMoreViewCreator;
        //设置header
        public View header;
        //是否自动刷新
        public boolean autoRefresh = false;

        public RefreshParams(PullToRefreshLayout pullToRefreshLayout) {
            this.pullToRefreshLayout = pullToRefreshLayout;
            this.refreshHelper = pullToRefreshLayout.getRefreshHelper();
        }
    }

    public static class Builder {
        private final RefreshParams refreshParams;

        public Builder(PullToRefreshLayout pullToRefreshLayout) {
            refreshParams = new RefreshParams(pullToRefreshLayout);
        }

        /**
         * 设置下拉刷新是否可用
         *
         * @param enable true: 可用，false: 不可用
         */
        public Builder setRefreshEnable(boolean enable) {
            refreshParams.refreshEnable = enable;
            return this;
        }

        /**
         * 设置上拉加载是否可用
         *
         * @param enable true: 可用，false: 不可用
         */
        public Builder setLoadMoreEnable(boolean enable) {
            refreshParams.loadMoreEnable = enable;
            return this;
        }

        /**
         * 刷新成功后RefreshView的停留时间
         *
         * @param delay 延时时间 ms
         * @return
         */
        public Builder setRefreshSuccessDelay(int delay) {
            refreshParams.successDelay = delay;
            return this;
        }

        /**
         * 设置RefreshView回滚的速度
         *
         * @param moveSpeed 回滚速递
         */
        public Builder setMoveSpeed(float moveSpeed) {
            refreshParams.moveSpeed = moveSpeed;
            return this;
        }

        /**
         * 设置手指移动距离与下拉头的滑动距离比初始值，中间会随正切函数变化
         *
         * @param radio 手指移动距离与下拉头的滑动距离比初始值
         */
        public Builder setRadio(float radio) {
            refreshParams.radio = radio;
            return this;
        }

        /**
         * 设置下拉刷新的View
         *
         * @param creator 用于创建refreshView 必须继承RefreshViewCreator接口
         */
        public Builder setRefreshViewCreator(RefreshViewCreator creator) {
            refreshParams.refreshViewCreator = creator;
            return this;
        }

        /**
         * 设置上拉加载的View
         *
         * @param creator 用于创建loadMoreView 必须继承LoadMoreViewCreator接口
         */
        public Builder setLoadMoreViewCreator(LoadMoreViewCreator creator) {
            refreshParams.loadMoreViewCreator = creator;
            return this;
        }

        /**
         * 设置RecyclerView Header
         *
         * @param header header
         */
        public Builder setHeader(View header) {
            refreshParams.header = header;
            ViewGroup.LayoutParams layoutParams = header.getLayoutParams();
            if (layoutParams == null) {
                layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
            refreshParams.header.setLayoutParams(layoutParams);
            return this;
        }

        /**
         * 设置监听事件
         *
         * @param listener 下拉刷新、上拉加载监听事件
         */
        public Builder setRefreshListener(PullToRefreshLayout.OnRefreshListener listener) {
            refreshParams.listener = listener;
            return this;
        }

        /**
         * 设置自动刷新
         *
         * @param auto true: 自动刷新，false：不自动刷新
         */
        public Builder setAutoRefresh(boolean auto) {
            refreshParams.autoRefresh = auto;
            return this;
        }

        /**
         * 如果是contentView是RecyclerView，必须在RecyclerView设置Adapter之后调用
         */
        public void build() {
            refreshParams.refreshHelper.setSuccessDelay(refreshParams.successDelay);
            refreshParams.refreshHelper.setMoveSpeed(refreshParams.moveSpeed);
            refreshParams.refreshHelper.setRadio(refreshParams.radio);
            refreshParams.pullToRefreshLayout.setRefreshEnable(refreshParams.refreshEnable);
            refreshParams.pullToRefreshLayout.setLoadMoreEnable(refreshParams.loadMoreEnable);
            refreshParams.pullToRefreshLayout.setRefreshViewCreator(refreshParams.refreshViewCreator);
            refreshParams.pullToRefreshLayout.setLoadingViewCreator(refreshParams.loadMoreViewCreator);
            refreshParams.pullToRefreshLayout.setHeader(refreshParams.header);
            refreshParams.pullToRefreshLayout.setOnRefreshListener(refreshParams.listener);
            if (refreshParams.autoRefresh) {
                refreshParams.pullToRefreshLayout.autoRefresh();
            }
        }
    }
}
