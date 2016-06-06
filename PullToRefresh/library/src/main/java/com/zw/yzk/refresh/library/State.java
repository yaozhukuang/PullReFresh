package com.zw.yzk.refresh.library;

/**
 * Created by wei on 2016/4/23.
 */
public enum State {
    // 初始状态
    INIT,
    // 释放刷新
    RELEASE_TO_REFRESH,
    // 正在刷新
    REFRESHING,
    // 正在加载
    LOADING,
    // 操作完毕
    DONE,
    //已经全部加载完成
    NO_ITEM_LOADING,
    //刷新成功
    REFRESH_SUCCEED,
    //加载成功
    LOADING_SUCCEED,
    //刷新失败
    REFRESH_FAIL,
    //加载失败
    LOADING_FAIL
}
