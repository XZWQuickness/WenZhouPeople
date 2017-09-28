package com.exz.wenzhoupeople.pop;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.exz.wenzhoupeople.R;
import com.exz.wenzhoupeople.adapter.CouponAdapter;
import com.exz.wenzhoupeople.entity.CouponEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.szw.lib.myframework.utils.RecycleViewDivider;
import cn.com.szw.lib.myframework.view.CustomLoadMoreView;
import razerdp.basepopup.BasePopupWindow;

/**
 * Created by pc on 2017/6/26.
 */

public class CouponMore extends BasePopupWindow {

    CouponAdapter adapter;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.btnSubmit)
    TextView btnSubmit;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.rlLay)
    RelativeLayout rlLay;

    public CouponMore(Activity context) {
        super(context);
        adapter = new CouponAdapter();
        adapter.setLoadMoreView(new CustomLoadMoreView());
        adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        adapter.setEmptyView(LayoutInflater.from(getContext()).inflate(R.layout.view_empty, new RelativeLayout(getContext()), false));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(new RecycleViewDivider(context, LinearLayoutManager.VERTICAL,
                SizeUtils.dp2px(15), ContextCompat.getColor(context, R.color.white)));
    }

    public void setData(List<CouponEntity> data) {
        adapter.setNewData(data);
        adapter.loadMoreEnd();
    }

    public List<CouponEntity> getData() {
        return adapter.getData();
    }


    @Override
    protected Animation initShowAnimation() {
        Animation shakeAnimate = new TranslateAnimation(0, 0, (float) ScreenUtils.getScreenHeight(), 0);
        shakeAnimate.setDuration(400);
        return shakeAnimate;
    }

    @Override
    protected Animation initExitAnimation() {
        Animation shakeAnimate = new TranslateAnimation(0, 0, 0, (float) ScreenUtils.getScreenHeight());
        shakeAnimate.setDuration(400);
        return shakeAnimate;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mRecyclerView.addOnItemTouchListener(onItemClickListener);
    }

    @Override
    public View getClickToDismissView() {
        return getPopupWindowView();
    }

    @Override
    public View onCreatePopupView() {
        View inflate = View.inflate(getContext(), R.layout.pop_coupon, null);
        ButterKnife.bind(this, inflate);
        return inflate;
    }

    @Override
    public View initAnimaView() {
        return findViewById(R.id.rlLay);
    }


    @OnClick({R.id.btnSubmit, R.id.rlLay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnSubmit:
                dismiss();
                break;
            case R.id.rlLay:
                break;
        }
    }


}
