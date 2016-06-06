package com.zw.yzk.refresh.library;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.zw.yzk.refresh.library.loadmore.GridSpanLookup;
import com.zw.yzk.refresh.library.loadmore.LoadMoreViewCreator;
import com.zw.yzk.refresh.library.loadmore.WrapperAdapter;
import com.zw.yzk.refresh.library.loadmore.WrapperSpanSizeLookup;
import com.zw.yzk.refresh.library.refrsh.RefreshViewCreator;


public class PullToRefreshLayout extends LinearLayout {

    private RefreshHelper refreshHelper;
    // 刷新回调接口
    private OnRefreshListener mListener;
    private float lastY;
    // 下拉的距离。
    public float pullDownY = 0;
    private MyTimer timer;
    // 在刷新过程中滑动操作
    private boolean isTouch = false;
    //下拉布局
    private LinearLayout refreshView;
    //contentView
    private View contentView;
    // 过滤多点触碰
    private int mEvents;

    public PullToRefreshLayout(Context context) {
        this(context, null);
    }

    public PullToRefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullToRefreshLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        intiSettings(context);
    }

    //初始化设置
    private void intiSettings(Context context) {
        refreshHelper = new RefreshHelper(updateHandler);

        timer = refreshHelper.getTimer();
        setOrientation(VERTICAL);

        initRefreshLayout(context);
    }

    //初始化RefreshView
    private void initRefreshLayout(Context context) {
        refreshView = new LinearLayout(context);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        refreshView.setGravity(Gravity.CENTER);
        refreshView.setLayoutParams(params);
        addView(refreshView, 0);
    }

    //获取refreshHelper
    public RefreshHelper getRefreshHelper() {
        return this.refreshHelper;
    }

    //设置刷新是否可用
    public void setRefreshEnable(boolean state) {
        refreshHelper.setRefreshEnable(state);
    }

    //设置上拉加载是否可用
    public void setLoadMoreEnable(boolean state) {
        refreshHelper.setLoadMoreEnable(state);
    }

    private void hide() {
        timer.schedule(5);
    }

    //设置监听事件
    public void setOnRefreshListener(final OnRefreshListener listener) {
        mListener = listener;
        if (!(contentView instanceof RecyclerView)) {
            return;
        }
        ((RecyclerView) contentView).addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (refreshHelper.getState() == State.NO_ITEM_LOADING || isLoading() || isRefreshing()) {
                    return;
                }
                if (refreshHelper.checkEndOffset(recyclerView) && mListener != null && dy > 0 && refreshHelper.isLoadMoreEnable()) {
                    mListener.onLoadMore(PullToRefreshLayout.this);
                    refreshHelper.setState(State.LOADING);
                }
            }

        });
    }

    //设置loadingView
    public void setLoadingViewCreator(LoadMoreViewCreator loadMoreViewCreator) {
        if (contentView instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) contentView;
            WrapperAdapter wrapperAdapter = refreshHelper.createAdapter(recyclerView.getAdapter(), loadMoreViewCreator);
            recyclerView.setAdapter(wrapperAdapter);

            if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                WrapperSpanSizeLookup wrapperSpanSizeLookup = new WrapperSpanSizeLookup(
                        ((GridLayoutManager) recyclerView.getLayoutManager()).getSpanSizeLookup(),
                        new GridSpanLookup(recyclerView.getLayoutManager()),
                        wrapperAdapter);
                ((GridLayoutManager) recyclerView.getLayoutManager()).setSpanSizeLookup(wrapperSpanSizeLookup);
            }
        }
    }

    //设置refreshView
    public void setRefreshViewCreator(RefreshViewCreator refreshViewCreator) {
        refreshHelper.setRefreshViewCreator(refreshViewCreator);
        if (refreshView.getChildCount() != 0) {
            refreshView.removeAllViews();
        }
        refreshView.addView(refreshHelper.getRefreshViewCreator().createView(getContext())
                , new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        refreshHelper.setState(State.INIT);
    }

    //设置header
    public void setHeader(View header) {
        if (contentView != null && contentView instanceof RecyclerView) {
            refreshHelper.setHeader(header);
        }
    }

    //移除header
    public void removeHeader() {
        refreshHelper.removeHeader();
    }

    // 设置刷新结果刷新完成后一定要调用 刷新成功: State.REFRESH_SUCCEED, 失败:State.REFRESH_FAIL,
    public void refreshFinish(State refreshResult) {
        refreshHelper.setState(refreshResult);

        if (pullDownY > 0) {
            // 刷新结果停留1秒
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    refreshHelper.setState(State.DONE);
                    hide();
                }
            }, refreshHelper.getSuccessDelay());
        } else {
            refreshHelper.setState(State.DONE);
            hide();
        }
    }

    //设置加载结果刷新完成后一定要调用 加载成功: State.LOADING_SUCCEED, 失败:State.LOADING_FAIL,没有更多:State.LOADING_FAIL
    public void loadMoreFinish(State loadResult) {
        refreshHelper.setState(loadResult);
    }

    public boolean isRefreshing() {
        return refreshHelper.getState() == State.REFRESHING;
    }

    public boolean isLoading() {
        return refreshHelper.getState() == State.LOADING;
    }

    /**
     * 设置刷新或者加载状态完成
     *
     * @param succeed 完成结果 true: 成功，false: 失败
     */
    public void finishRefreshingOrLoadingStatus(boolean succeed) {
        if (succeed) {
            if (isLoading()) {
                loadMoreFinish(State.LOADING_SUCCEED);
            } else if (isRefreshing()) {
                refreshFinish(State.REFRESH_SUCCEED);
            }
        } else {
            if (isLoading()) {
                loadMoreFinish(State.LOADING_FAIL);
            } else if (isRefreshing()) {
                refreshFinish(State.REFRESH_FAIL);
            }
        }
    }

    //是否可以下拉刷新,只有contentView本身不能下滑的时候下拉头才能开始下滑
    private boolean canPullDown() {
        return !ViewCompat.canScrollVertically(contentView, -1) && refreshHelper.getState() != State.LOADING;
    }

    // 由父控件决定是否分发事件，防止事件冲突
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!refreshHelper.isRefreshEnable()) {
            return super.dispatchTouchEvent(ev);
        }
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                lastY = ev.getY();
                timer.cancel();
                mEvents = 0;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_POINTER_UP:
                // 过滤多点触碰
                mEvents = -1;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mEvents == 0) {
                    if (pullDownY > 0 || (canPullDown() && !isLoading())) {
                        // 可以下拉，正在加载时不能下拉
                        // 对实际滑动距离做缩小，造成用力拉的感觉
                        pullDownY = pullDownY + (ev.getY() - lastY) / refreshHelper.getRadio();
                        if (pullDownY < 0) {
                            pullDownY = 0;
                        }
                        if (pullDownY > getMeasuredHeight())
                            pullDownY = getMeasuredHeight();
                        if (isRefreshing()) {
                            // 正在刷新的时候触摸移动
                            isTouch = true;
                        }
                    }
                    if (refreshHelper.getState() != State.REFRESHING && pullDownY > 0f) {
                        float percent = pullDownY / refreshHelper.getRefreshDist();
                        refreshHelper.getRefreshViewCreator().stateMove(percent < 1f ? percent : 1f);
                    }
                } else {
                    mEvents = 0;
                }
                lastY = ev.getY();
                // 根据下拉距离改变比例
                refreshHelper.setRadio((float) (2 + 2 * Math.tan(Math.PI / 2 / getMeasuredHeight() * pullDownY)));
                if (pullDownY > 0) {
                    requestLayout();
                    if (pullDownY <= refreshHelper.getRefreshDist()
                            && (refreshHelper.getState() == State.RELEASE_TO_REFRESH ||
                            refreshHelper.getState() == State.DONE || refreshHelper.getState() == State.NO_ITEM_LOADING)) {
                        // 如果下拉距离没达到刷新的距离且当前状态是释放刷新，改变状态为下拉刷新
                        refreshHelper.setState(State.INIT);
                    }
                    if (pullDownY >= refreshHelper.getRefreshDist() && refreshHelper.getState() == State.INIT) {
                        // 如果下拉距离达到刷新的距离且当前状态是初始状态刷新，改变状态为释放刷新
                        refreshHelper.setState(State.RELEASE_TO_REFRESH);
                    }
                    // 取消子控件的按下事件
                    ev.setAction(MotionEvent.ACTION_CANCEL);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (pullDownY > refreshHelper.getRefreshDist()) {
                    // 正在刷新时往下拉（正在加载时往上拉），释放后下拉头（上拉头）不隐藏
                    isTouch = false;
                }
                if (refreshHelper.getState() == State.RELEASE_TO_REFRESH) {
                    refreshHelper.setState(State.REFRESHING);
                    // 刷新操作
                    if (mListener != null)
                        mListener.onRefresh(this);
                }
                hide();
                break;
            case MotionEvent.ACTION_CANCEL:
                hide();
                break;
            default:
                break;
        }
        // 事件分发交给父类
        super.dispatchTouchEvent(ev);
        return true;
    }

    private class AutoRefreshAndLoadTask extends
            AsyncTask<Integer, Float, String> {
        @Override
        protected String doInBackground(Integer... params) {
            while (pullDownY < 4 / 3 * refreshHelper.getRefreshDist()) {
                pullDownY = refreshHelper.getRefreshDist();
                publishProgress(pullDownY);
                try {
                    Thread.sleep(params[0]);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            refreshHelper.setState(State.REFRESHING);
            // 刷新操作
            if (mListener != null)
                mListener.onRefresh(PullToRefreshLayout.this);
            hide();
        }

        @Override
        protected void onProgressUpdate(Float... values) {
            if (pullDownY > refreshHelper.getRefreshDist())
                refreshHelper.setState(State.RELEASE_TO_REFRESH);
            requestLayout();
        }
    }

    //设置自动刷新
    public void autoRefresh() {
        if (isRefreshing() || !refreshHelper.isRefreshEnable()) {
            return;
        }
        AutoRefreshAndLoadTask task = new AutoRefreshAndLoadTask();
        task.execute(20);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        contentView = getChildAt(1);

        refreshHelper.setRefreshDist(refreshView.getMeasuredHeight());
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // 改变子控件的布局
//        refreshView.layout(0, (int) pullDownY - refreshView.getMeasuredHeight(), refreshView.getMeasuredWidth(), (int) pullDownY);
//        contentView.layout(0, (int) pullDownY, contentView.getMeasuredWidth(), (int) pullDownY
//                + contentView.getMeasuredHeight());
        refreshView.layout(0, (int) pullDownY - refreshView.getMeasuredHeight(), refreshView.getMeasuredWidth(), (int) pullDownY);
        MarginLayoutParams marginLayoutParams = (MarginLayoutParams) contentView.getLayoutParams();
        contentView.layout(marginLayoutParams.leftMargin + getPaddingLeft(), (int) pullDownY + marginLayoutParams.topMargin + getPaddingTop(),
                contentView.getMeasuredWidth() + marginLayoutParams.rightMargin + getPaddingRight(),
                (int) pullDownY + marginLayoutParams.topMargin + contentView.getMeasuredHeight() + getPaddingBottom());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        for (int i = 0; i < getChildCount(); i++) {
            // 获取对应遍历下标的子元素
            View child = getChildAt(i);
            if (child.getVisibility() != View.GONE) {
                // 测量子元素并考量其外边距
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
            }
        }

    }

    //执行自动回滚的handler
    Handler updateHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // 回弹速度随下拉距离moveDeltaY增大而增大
            refreshHelper.setMoveSpeed((float) (8 + 5 * Math.tan(Math.PI / 2 / getMeasuredHeight() * (pullDownY))));
            if (!isTouch) {
                // 正在刷新，且没有往上推的话则悬停，显示"正在刷新..."
                if (isRefreshing() && pullDownY <= refreshHelper.getRefreshDist()) {
                    pullDownY = refreshHelper.getRefreshDist();
                    timer.cancel();
                }
            }
            if (pullDownY > 0) {
                pullDownY -= refreshHelper.getMoveSpeed();
            }
            if (pullDownY < 0) {
                // 已完成回弹
                pullDownY = 0;
            }
            // 刷新布局,会自动调用onLayout
            requestLayout();
            if (pullDownY == 0)
                timer.cancel();
        }
    };

    //下拉刷新、上拉加载回调接口
    public interface OnRefreshListener {
        //刷新回调接口
        void onRefresh(PullToRefreshLayout pullToRefreshLayout);

        //加载回调接口
        void onLoadMore(PullToRefreshLayout pullToRefreshLayout);
    }

}
