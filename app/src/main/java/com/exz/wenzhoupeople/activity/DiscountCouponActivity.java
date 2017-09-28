package com.exz.wenzhoupeople.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.exz.wenzhoupeople.R;
import com.exz.wenzhoupeople.fragment.DiscountCouponFragment;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.szw.lib.myframework.base.BaseActivity;

/**
 * Created by weicao on 2017/8/29.
 * 优惠券
 */

public class DiscountCouponActivity extends BaseActivity {
    @BindView(R.id.mLeft)
    TextView mLeft;
    @BindView(R.id.mTitle)
    TextView mTitle;
    @BindView(R.id.mRight)
    TextView mRight;
    @BindView(R.id.mRightImg)
    ImageView mRightImg;
    @BindView(R.id.mLeftImg)
    ImageView mLeftImg;
    @BindView(R.id.parent_lay)
    RelativeLayout parentLay;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tab_stub)
    SlidingTabLayout tabStub;
    @BindView(R.id.pager)
    ViewPager pager;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    String[] tabTitles = new String[]{"未使用", "已使用", "已过期"};

    @Override
    public boolean initToolbar() {
        mTitle.setText("优惠券");
        mTitle.setTextSize(18);
        toolbar.setContentInsetsAbsolute(0, 0);
        setSupportActionBar(toolbar);
        return false;
    }

    @Override
    public int setInflateId() {
        return R.layout.activity_discount;
    }

    @Override
    public void init() throws Exception {
        super.init();
        initTab();
    }


    private void initTab() {
        mFragments.add(DiscountCouponFragment.newInstance("0"));
        mFragments.add(DiscountCouponFragment.newInstance("1"));
        mFragments.add(DiscountCouponFragment.newInstance("2"));

        pager.setOffscreenPageLimit(3);
        tabStub.setViewPager(pager, tabTitles, this, mFragments);
        tabStub.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {

            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }

    @OnClick(R.id.mLeftImg)
    public void onViewClicked() {
        finish();
    }
}
