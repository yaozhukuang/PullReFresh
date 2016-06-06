package com.zw.yzk.pulltorefresh.fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zw.yzk.pulltorefresh.R;
import com.zw.yzk.refresh.library.PullToRefreshLayout;
import com.zw.yzk.refresh.library.RefreshBuilder;
import com.zw.yzk.refresh.library.refrsh.RefreshViewCreator;

/**
 * Created by wei on 2016/4/29.
 */
public class WebViewFragment extends BaseFragment {
    private PullToRefreshLayout refreshLayout;
    private WebView webView;

    @Override
    public int setContent() {
        return R.layout.fragment_webview_refresh;
    }

    @Override
    public void initView(View view) {
        refreshLayout = (PullToRefreshLayout) view.findViewById(R.id.refresh_view);
        webView = (WebView) view.findViewById(R.id.webview);
    }

    @Override
    public void initData() {
        initWebView();
        webView.loadUrl("http://www.hao123.com/");
        new RefreshBuilder.Builder(refreshLayout)
                .setLoadMoreEnable(true)
                .setRefreshSuccessDelay(200)
//                .setHeader(LayoutInflater.from(getActivity()).inflate(R.layout.recycler_header_text, null))
                .setRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                webView.loadUrl("http://www.sina.com.cn/");
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

    private void initWebView() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }
        });
    }
}
