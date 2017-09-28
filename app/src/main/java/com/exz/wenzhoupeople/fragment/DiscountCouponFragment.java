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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.blankj.utilcode.util.EncryptUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.exz.wenzhoupeople.R;
import com.exz.wenzhoupeople.adapter.DiscountCouponAdapter;
import com.exz.wenzhoupeople.appclication.App;
import com.exz.wenzhoupeople.config.Constants;
import com.exz.wenzhoupeople.entity.DiscountCouponModel;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.com.szw.lib.myframework.base.MyBaseFragment;
import cn.com.szw.lib.myframework.utils.DateUtils;
import cn.com.szw.lib.myframework.utils.RecycleViewDivider;
import cn.com.szw.lib.myframework.utils.net.NetEntity;
import cn.com.szw.lib.myframework.utils.net.callback.DialogCallback;
import cn.com.szw.lib.myframework.view.CustomLoadMoreView;

import static com.exz.wenzhoupeople.config.Urls.Account_CouponList;

/**
 * Created by weicao on 2017/8/29.
 * 优惠券
 */

public class DiscountCouponFragment extends MyBaseFragment implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    Unbinder unbinder;
    public static final String FRAGMENT_TYPE = "type";
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;
    private int page = 1;
    public int refreshState = Constants.RefreshState.STATE_REFRESH;
    private DiscountCouponAdapter adapter;

    private List<DiscountCouponModel> list = new ArrayList();
    private String state;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_discount_coupon, container, false);
        }
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;

    }


    public static DiscountCouponFragment newInstance(String fragment_type) {
        Bundle bundle = new Bundle();
        DiscountCouponFragment fragment = new DiscountCouponFragment();
        bundle.putString(FRAGMENT_TYPE, fragment_type);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void initView() {
        state = getArguments().getString(FRAGMENT_TYPE);
        initData();
        onRefresh();

    }

    private void initData() {
//        list.add(new DiscountCouponModel());
//        list.add(new DiscountCouponModel());
//        list.add(new DiscountCouponModel());

        adapter = new DiscountCouponAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new RecycleViewDivider(getContext(), LinearLayoutManager.HORIZONTAL, 15, ContextCompat.getColor(getContext(), R.color.app_bg1)));
        adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        adapter.setLoadMoreView(new CustomLoadMoreView());
        adapter.setOnLoadMoreListener(this, recyclerView);
        refresh.setOnRefreshListener(this);
        adapter.setEmptyView(LayoutInflater.from(getActivity()).inflate(R.layout.view_empty, new RelativeLayout(getActivity()), false));
        recyclerView.setAdapter(adapter);
        adapter.setNewData(list);
    }

    @Override
    public void onRefresh() {
        refresh.setRefreshing(false);
        page = 1;
        refreshState = Constants.RefreshState.STATE_REFRESH;
        initPort();

    }

    @Override
    public void onLoadMoreRequested() {
        refresh.setEnabled(false);
        refreshState = Constants.RefreshState.STATE_LOAD_MORE;
        initPort();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    private void initPort() {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", App.getLoginUserId());
        params.put("timestamp", String.valueOf(DateUtils.dateToUnixTimestamp()));
        params.put("state", state);
        params.put("page", String.valueOf(page));
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(App.getLoginUserId() + String.valueOf(DateUtils.dateToUnixTimestamp()) + App.salt).toLowerCase());
        OkGo.<NetEntity<List<DiscountCouponModel>>>post(Account_CouponList)
                .params(params)
                .tag(this)
                .execute(new DialogCallback<NetEntity<List<DiscountCouponModel>>>(getActivity()) {

                    @Override
                    public void onSuccess(Response<NetEntity<List<DiscountCouponModel>>> response) {
                        refresh.setEnabled(true);
                        refresh.setRefreshing(false);

                        if (response.body().getCode() == cn.com.szw.lib.myframework.config.Constants.NetCode.SUCCESS) {
                            if (refreshState == cn.com.szw.lib.myframework.config.Constants.RefreshState.STATE_REFRESH) {
                                adapter.setNewData(response.body().getData());

                            } else {
                                adapter.addData(response.body().getData());
                            }

                            if (response.body().getData().isEmpty()) {
                                adapter.loadMoreComplete();
                                page++;
                            } else {
                                adapter.loadMoreEnd();
                            }
                        }


                    }

                    @Override
                    public void onError(Response<NetEntity<List<DiscountCouponModel>>> response) {
                        super.onError(response);
                        refresh.setEnabled(true);
                        refresh.setRefreshing(false);
                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        adapter.loadMoreFail();
                    }
                });
    }
}
