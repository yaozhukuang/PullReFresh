package com.zw.yzk.pulltorefresh.fragment;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.zw.yzk.pulltorefresh.R;
import com.zw.yzk.refresh.library.PullToRefreshLayout;
import com.zw.yzk.refresh.library.RefreshBuilder;
import com.zw.yzk.refresh.library.State;
import com.zw.yzk.refresh.library.refrsh.RefreshViewCreator;

/**
 * Created by wei on 2016/4/29.
 */
public class ScrollViewFragment extends BaseFragment {

    private PullToRefreshLayout refreshLayout;
    private TextView textView1;
    private TextView textView2;

    @Override
    public int setContent() {
        return R.layout.fragment_scrollview_refresh;
    }

    @Override
    public void initView(View view) {
        refreshLayout = (PullToRefreshLayout) view.findViewById(R.id.refresh_view);
        textView1 = (TextView) view.findViewById(R.id.text1);
        textView2 = (TextView) view.findViewById(R.id.text2);
    }

    @Override
    public void initData() {
        initScrollView();
    }

    @Override
    public void setRefreshViewCreator(RefreshViewCreator creator) {
        refreshLayout.setRefreshViewCreator(creator);
    }

    private void initScrollView() {
        new RefreshBuilder.Builder(refreshLayout)
                .setLoadMoreEnable(true)
                .setRefreshSuccessDelay(200)
                .setHeader(LayoutInflater.from(getActivity()).inflate(R.layout.recycler_header_text, null))
                .setRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                textView1.setText("刷新之后");
                                textView2 .setText("刷新之后");
//                                refreshLayout.refreshFinish(State.REFRESH_SUCCEED);
                                refreshLayout.finishRefreshingOrLoadingStatus(true);
                            }
                        }, 3000);
                    }

                    @Override
                    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                textView1.setText("刷新之后");
                                refreshLayout.finishRefreshingOrLoadingStatus(true);
                            }
                        }, 3000);
                    }
                }).build();
    }
}
