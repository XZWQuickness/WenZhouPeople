package com.exz.wenzhoupeople.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.FrameLayout;

import com.exz.wenzhoupeople.R;
import com.exz.wenzhoupeople.appclication.App;
import com.exz.wenzhoupeople.entity.TabEntity;
import com.exz.wenzhoupeople.fragment.GouWuCheFragment;
import com.exz.wenzhoupeople.fragment.MainFragment;
import com.exz.wenzhoupeople.fragment.MineFragment;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.szw.lib.myframework.base.BaseActivity;
import cn.com.szw.lib.myframework.utils.Utils;

public class MainActivity extends BaseActivity {
    private String[] mTitles = {"首页", "购物车", "我的"};
    private int[] mIconSelectIds = {
            R.mipmap.ic_main_tab1, R.mipmap.ic_gwc_tab1,
            R.mipmap.ic_mine_tab1};
    private int[] mIconUnSelectIds = {
            R.mipmap.ic_main_tab2, R.mipmap.ic_gwc_tab2,
            R.mipmap.ic_mine_tab2};
    @BindView(R.id.main_container)
    FrameLayout mainContainer;
    @BindView(R.id.mainTabBar)
    public CommonTabLayout mainTabBar;
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private ArrayList<Fragment> mFragments = new ArrayList<>();

    @Override
    public boolean initToolbar() {
        return false;
    }

    @Override
    public int setInflateId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnSelectIds[i]));
        }
        mFragments.add(new MainFragment());
        mFragments.add(GouWuCheFragment.newInstance().setLeft(false));
        mFragments.add(new MineFragment());
        mainTabBar.setTabData(mTabEntities, this, R.id.main_container, mFragments);

        mainTabBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                if (App.getLoginUserId().equals("")) {
                    if (position == 1 || position == 2) {
                        if (App.getLoginUserId().equals("")) {
                            mainTabBar.setCurrentTab(0);
                            Utils.startActivity(mContext, LoginActivity.class);

                        }
                    }
                }

            }

            @Override
            public void onTabReselect(int position) {

            }
        });


    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag("currentTab")})
    public void setCurrentTab(String currentTab) {
        mainTabBar.setCurrentTab(Integer.parseInt(currentTab));

        mainTabBar.showMsg(2, 2);
    }
    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag("ShopCarNum")})
    public void setShopCarNum(String ShopCarNum) {

        mainTabBar.showMsg(1, Integer.parseInt(ShopCarNum));
        if(TextUtils.isEmpty(ShopCarNum)||ShopCarNum.equals("0"))mainTabBar.hideMsg(1);
    }

}
