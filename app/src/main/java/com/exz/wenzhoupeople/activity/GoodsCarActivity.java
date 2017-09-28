package com.exz.wenzhoupeople.activity;

import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import com.exz.wenzhoupeople.R;
import com.exz.wenzhoupeople.fragment.GouWuCheFragment;

import butterknife.BindView;
import cn.com.szw.lib.myframework.base.BaseActivity;

/**
 * Created by pc on 2017/9/14.
 */

public class GoodsCarActivity extends BaseActivity {
    @BindView(R.id.frameLay)
    FrameLayout frameLay;

    @Override
    public boolean initToolbar() {
        return false;
    }

    @Override
    public int setInflateId() {
        return R.layout.activity_goods_car;
    }

    @Override
    public void init() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        GouWuCheFragment fragment =GouWuCheFragment.newInstance().setLeft(true);
        transaction.add(R.id.frameLay, fragment);
        transaction.commit();
    }
}
