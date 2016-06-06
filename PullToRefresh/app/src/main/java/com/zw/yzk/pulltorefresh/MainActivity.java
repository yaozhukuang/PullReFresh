package com.zw.yzk.pulltorefresh;

import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;

import com.zw.yzk.pulltorefresh.fragment.AutoRefreshFragment;
import com.zw.yzk.pulltorefresh.fragment.BaseFragment;
import com.zw.yzk.pulltorefresh.fragment.GridRecyclerViewFragment;
import com.zw.yzk.pulltorefresh.fragment.LinearRecyclerViewFragment;
import com.zw.yzk.pulltorefresh.fragment.ScrollViewFragment;
import com.zw.yzk.pulltorefresh.fragment.TextViewFragment;
import com.zw.yzk.pulltorefresh.fragment.WebViewFragment;
import com.zw.yzk.pulltorefresh.refresh.JDRefreshViewCreator;
import com.zw.yzk.pulltorefresh.refresh.MeiTuanHeader;
import com.zw.yzk.refresh.library.refrsh.DefaultRefreshViewCreator;
import com.zw.yzk.refresh.library.refrsh.RefreshViewCreator;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigation;
    private LinearRecyclerViewFragment linearRecyclerViewFragment;
    private GridRecyclerViewFragment gridRecyclerViewFragment;
    private AutoRefreshFragment autoRefreshFragment;
    private TextViewFragment textViewFragment;
    private WebViewFragment webViewFragment;
    private ScrollViewFragment scrollViewFragment;
    private BaseFragment currentFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        initView();
        initNavigationView();
        initData();
    }

    private void initView() {
        navigation = (NavigationView) findViewById(R.id.navigation);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    }

    private void initData() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        linearRecyclerViewFragment = new LinearRecyclerViewFragment();
        currentFragment = linearRecyclerViewFragment;
        transaction.replace(R.id.content, linearRecyclerViewFragment, linearRecyclerViewFragment.getClass().getSimpleName());
        transaction.commitAllowingStateLoss();
    }

    private void drawerMenuSelected(MenuItem item) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        int itemId = item.getItemId();

        hideAllFragment(transaction);
        switch (itemId) {
            case R.id.navigation_item_linear_recycler_view:
                if (linearRecyclerViewFragment == null) {
                    linearRecyclerViewFragment = new LinearRecyclerViewFragment();
                    transaction.add(R.id.content, linearRecyclerViewFragment, linearRecyclerViewFragment.getClass().getSimpleName());
                }
                currentFragment = linearRecyclerViewFragment;
                break;
            case R.id.navigation_item_grid_recycler_view:
                if (gridRecyclerViewFragment == null) {
                    gridRecyclerViewFragment = new GridRecyclerViewFragment();
                    transaction.add(R.id.content, gridRecyclerViewFragment, linearRecyclerViewFragment.getClass().getSimpleName());
                }
                currentFragment = gridRecyclerViewFragment;
                break;
            case R.id.navigation_item_auto_refresh:
                if (autoRefreshFragment == null) {
                    autoRefreshFragment = new AutoRefreshFragment();
                    transaction.add(R.id.content, autoRefreshFragment, linearRecyclerViewFragment.getClass().getSimpleName());
                }
                currentFragment = autoRefreshFragment;
                break;
            case R.id.navigation_item_text_view:
                if (textViewFragment == null) {
                    textViewFragment = new TextViewFragment();
                    transaction.add(R.id.content, textViewFragment, linearRecyclerViewFragment.getClass().getSimpleName());
                }
                currentFragment = textViewFragment;
                break;
            case R.id.navigation_item_webview:
                if (webViewFragment == null) {
                    webViewFragment = new WebViewFragment();
                    transaction.add(R.id.content, webViewFragment, linearRecyclerViewFragment.getClass().getSimpleName());
                }
                currentFragment = webViewFragment;
                break;
            case R.id.navigation_item_scrollview:
                if (scrollViewFragment == null) {
                    scrollViewFragment = new ScrollViewFragment();
                    transaction.add(R.id.content, scrollViewFragment, linearRecyclerViewFragment.getClass().getSimpleName());
                }
                currentFragment = scrollViewFragment;
                break;
            case R.id.navigation_item_default:
                setRefreshViewCreator(new DefaultRefreshViewCreator());
                break;
            case R.id.navigation_item_jd:
                setRefreshViewCreator(new JDRefreshViewCreator());
                break;
            case R.id.navigation_item_mt:
                setRefreshViewCreator(new MeiTuanHeader());
                break;
            default:
                break;
        }
        transaction.show(currentFragment);
        transaction.commitAllowingStateLoss();
    }

    private void hideAllFragment(FragmentTransaction transaction) {
        if (linearRecyclerViewFragment != null) {
            transaction.hide(linearRecyclerViewFragment);
        }
        if (gridRecyclerViewFragment != null) {
            transaction.hide(gridRecyclerViewFragment);
        }
        if (autoRefreshFragment != null) {
            transaction.hide(autoRefreshFragment);
        }
        if (textViewFragment != null) {
            transaction.hide(textViewFragment);
        }
        if (webViewFragment != null) {
            transaction.hide(webViewFragment);
        }
        if (scrollViewFragment != null) {
            transaction.hide(scrollViewFragment);
        }
        if (currentFragment != null) {
            transaction.hide(currentFragment);
        }
    }

    private void setRefreshViewCreator(RefreshViewCreator creator) {
        currentFragment.setRefreshViewCreator(creator);
    }

    private void initNavigationView() {
        //去掉NavigationView滚动条
        navigation.getChildAt(0).setVerticalScrollBarEnabled(false);

        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                drawerMenuSelected(item);
                drawerLayout.closeDrawer(Gravity.LEFT);
                return true;
            }
        });
    }
}
