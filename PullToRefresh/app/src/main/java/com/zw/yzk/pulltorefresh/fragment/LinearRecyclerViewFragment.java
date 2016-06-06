package com.zw.yzk.pulltorefresh.fragment;

import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.zw.yzk.pulltorefresh.MyAdapter;
import com.zw.yzk.pulltorefresh.R;
import com.zw.yzk.pulltorefresh.loadmore.CustomLoadMoreCreator;
import com.zw.yzk.refresh.library.PullToRefreshLayout;
import com.zw.yzk.refresh.library.RefreshBuilder;
import com.zw.yzk.refresh.library.State;
import com.zw.yzk.refresh.library.refrsh.RefreshViewCreator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wei on 2016/4/29.
 */
public class LinearRecyclerViewFragment extends BaseFragment {

    private PullToRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private MyAdapter adapter;

    @Override
    public int setContent() {
        return R.layout.fragment_recycler_refresh;
    }

    @Override
    public void initView(View view) {
        refreshLayout = (PullToRefreshLayout) view.findViewById(R.id.refresh_view);
        recyclerView = (RecyclerView) view.findViewById(R.id.record);
    }

    @Override
    public void initData() {
        initRecyclerView();
    }

    @Override
    public void setRefreshViewCreator(RefreshViewCreator creator) {
        refreshLayout.setRefreshViewCreator(creator);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    //content为RecyclerView
    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new MyAdapter(createStringList(0, 0));
        recyclerView.setAdapter(adapter);


        new RefreshBuilder.Builder(refreshLayout)
                .setLoadMoreEnable(true)
                .setRefreshSuccessDelay(200)
                .setLoadMoreViewCreator(new CustomLoadMoreCreator())
                .setHeader(LayoutInflater.from(getActivity()).inflate(R.layout.recycler_header_text, null))
                .setRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                adapter.refresh(createStringList(0, 12));
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
                                if (adapter.getItemCount() > 50) {
//                                    refreshLayout.finishRefreshingOrLoadingStatus(false);
                                    refreshLayout.loadMoreFinish(State.LOADING_FAIL);
                                } else {
//                                    adapter.add(createStringList(adapter.getItemCount(), 10));
                                    adapter.refresh(createStringList(0, adapter.getItemCount() + 10));
//                                    refreshLayout.loadMoreFinish(State.LOADING_SUCCEED);
                                    refreshLayout.finishRefreshingOrLoadingStatus(true);
                                }
                            }
                        }, 3000);
                    }
                }).build();
    }

    private List<String> createStringList(int start, int count) {
        List<String> result = new ArrayList<>();
        if (count <= 0) {
            return result;
        }
        for (int i = start; i < start + count; i++) {
            result.add("item " + i + " created");
        }
        return result;
    }
}
