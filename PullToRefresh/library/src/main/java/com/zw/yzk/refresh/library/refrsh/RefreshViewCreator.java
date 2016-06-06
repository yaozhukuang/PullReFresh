package com.zw.yzk.refresh.library.refrsh;

import android.content.Context;
import android.view.View;


/**
 * Created by wei on 2016/4/23.
 */
public interface RefreshViewCreator {

    //初始化refresh view
    View createView(Context context);
    //初始状态
    void stateInit();
    //释放刷新
    void stateReleaseToRefresh();
    //正在刷新
    void stateRefreshing();
    //刷新成功
    void stateRefreshSucceed();
    //刷新失败
    void stateRefreshFail();
    //下拉过程
    void stateMove(float percent);
}
