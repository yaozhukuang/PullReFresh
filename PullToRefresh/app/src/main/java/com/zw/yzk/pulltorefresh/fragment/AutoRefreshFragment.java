package com.zw.yzk.pulltorefresh.fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.zw.yzk.pulltorefresh.R;
import com.zw.yzk.refresh.library.PullToRefreshLayout;
import com.zw.yzk.refresh.library.RefreshBuilder;
import com.zw.yzk.refresh.library.refrsh.RefreshViewCreator;

/**
 * Created by wei on 2016/4/29.
 */
public class AutoRefreshFragment extends BaseFragment {
    private TextView textView;
    private PullToRefreshLayout refreshLayout;

    @Override
    public int setContent() {
        return R.layout.fragment_text_view_refresh;
    }

    @Override
    public void initView(View view) {
        refreshLayout = (PullToRefreshLayout) view.findViewById(R.id.refresh_view);
        textView = (TextView) view.findViewById(R.id.text);
    }

    @Override
    public void initData() {
        new RefreshBuilder.Builder(refreshLayout)
                .setLoadMoreEnable(true)
                .setRefreshSuccessDelay(200)
//                .setHeader(LayoutInflater.from(getActivity()).inflate(R.layout.recycler_header_text, null))
                .setAutoRefresh(true)
                .setRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                textView.setText("刷新之后");
//                                refreshLayout.refreshFinish(State.REFRESH_SUCCEED);
                                refreshLayout.finishRefreshingOrLoadingStatus(true);
                            }
                        }, 3000);
                    }

                    @Override
                    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                    }
                }).build();
    }

    @Override
    public void setRefreshViewCreator(RefreshViewCreator creator) {
        refreshLayout.setRefreshViewCreator(creator);
    }
}
