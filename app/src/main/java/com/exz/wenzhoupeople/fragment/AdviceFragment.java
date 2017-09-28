package com.exz.wenzhoupeople.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.exz.wenzhoupeople.R;
import com.exz.wenzhoupeople.activity.WriteAdviceActivity;
import com.exz.wenzhoupeople.adapter.AdviceAdapter;
import com.exz.wenzhoupeople.config.Constants;
import com.exz.wenzhoupeople.entity.AdviceModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.com.szw.lib.myframework.base.MyBaseFragment;
import cn.com.szw.lib.myframework.utils.RecycleViewDivider;
import cn.com.szw.lib.myframework.utils.Utils;
import cn.com.szw.lib.myframework.view.CustomLoadMoreView;

/**
 * Created by weicao on 2017/8/29.
 * 优惠券
 */

public class AdviceFragment extends MyBaseFragment implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    Unbinder unbinder;
    public static final String FRAGMENT_TYPE = "type";
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;
    private int page = 1;
    public int refreshState = Constants.RefreshState.STATE_REFRESH;
    private AdviceAdapter mAdapter;
    private String type;

    private List<AdviceModel>list = new ArrayList();
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_discount_coupon, container, false);
        }
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;

    }


    public static AdviceFragment newInstance(String fragment_type) {
        Bundle bundle = new Bundle();
        AdviceFragment fragment = new AdviceFragment();
        bundle.putString(FRAGMENT_TYPE, fragment_type);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void initView() {
        type = getArguments().getString(FRAGMENT_TYPE);
        initData();

    }

    private void initData() {
        list.add(new AdviceModel());
        list.add(new AdviceModel());
        list.add(new AdviceModel());

        mAdapter = new AdviceAdapter(type);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new RecycleViewDivider(getContext(), LinearLayoutManager.HORIZONTAL, 15, ContextCompat.getColor(getContext(), R.color.app_bg1)));
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        mAdapter.setOnLoadMoreListener(this, recyclerView);
        refresh.setOnRefreshListener(this);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setNewData(list);


        recyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                switch (view.getId()){
                    case R.id.btn_advice:
                        Utils.startActivity(getActivity(),WriteAdviceActivity.class);
                        break;
                }
            }
        });
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMoreRequested() {

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
