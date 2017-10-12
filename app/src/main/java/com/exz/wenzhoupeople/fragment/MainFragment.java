package com.exz.wenzhoupeople.fragment;

import android.content.Intent;
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
import android.widget.TextView;

import com.blankj.utilcode.util.EncryptUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.exz.wenzhoupeople.R;
import com.exz.wenzhoupeople.activity.DJMWActivity;
import com.exz.wenzhoupeople.activity.FootPrintActivity;
import com.exz.wenzhoupeople.activity.FreshSeafoodActivity;
import com.exz.wenzhoupeople.activity.LoginInActivity;
import com.exz.wenzhoupeople.activity.MsgListActivity;
import com.exz.wenzhoupeople.activity.OrderActivity;
import com.exz.wenzhoupeople.activity.SeafoodListActivity;
import com.exz.wenzhoupeople.activity.SerachShowActivity;
import com.exz.wenzhoupeople.adapter.BannerAdapter;
import com.exz.wenzhoupeople.adapter.MainAdapter;
import com.exz.wenzhoupeople.appclication.App;
import com.exz.wenzhoupeople.config.Urls;
import com.exz.wenzhoupeople.entity.BannersBean;
import com.exz.wenzhoupeople.entity.ImgEntity;
import com.exz.wenzhoupeople.entity.ModelImgEntity;
import com.exz.wenzhoupeople.utils.MaterialDialogUtils;
import com.jude.rollviewpager.RollPagerView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.com.szw.lib.myframework.base.MyBaseFragment;
import cn.com.szw.lib.myframework.config.Constants;
import cn.com.szw.lib.myframework.utils.RecycleViewDivider;
import cn.com.szw.lib.myframework.utils.Utils;
import cn.com.szw.lib.myframework.utils.net.NetEntity;
import cn.com.szw.lib.myframework.utils.net.callback.DialogCallback;
import cn.com.szw.lib.myframework.utils.net.callback.JsonCallback;


/**
 * Created by pc on 2017/6/22.
 */

public class MainFragment extends MyBaseFragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    Unbinder unbinder;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;
    MainAdapter adapter;
    List<ImgEntity> list = new ArrayList<>();
    ViewHolder headHoler;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_main, container, false);
        }
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void initView() {
        list = new ArrayList<>();
        adapter = new MainAdapter();
        View headView = LayoutInflater.from(getContext()).inflate(R.layout.maine_head, null);
        adapter = new MainAdapter();
        adapter.addHeaderView(headView);
        headHoler = new ViewHolder(headView);
        adapter.setEmptyView(LayoutInflater.from(getContext()).inflate(R.layout.view_empty, new RelativeLayout(getContext()), false));
        adapter.setHeaderAndEmpty(true);
        adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        recyclerView.addItemDecoration(new RecycleViewDivider(getContext(), LinearLayoutManager.VERTICAL,
                10, ContextCompat.getColor(getContext(), R.color.line_bg)));
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = null;
                switch (position) {
                    case 0://鲜活海鲜
                        Utils.startActivity(getContext(), FreshSeafoodActivity.class);
                        break;
                    case 1://当季美味
                        Utils.startActivity(getContext(), DJMWActivity.class);
                        break;
                    case 2://海鲜制品
                        intent = new Intent(getContext(), SeafoodListActivity.class);
                        intent.putExtra("className", "海鲜制品");
                        startActivity(intent);
                        break;
                    case 3://家乡零食（特产）列表
                        intent = new Intent(getContext(), SeafoodListActivity.class);
                        intent.putExtra("className", "家乡特产");
                        startActivity(intent);
                        break;

                }
            }
        });
        refresh.setOnRefreshListener(this);
        headHoler.cuji.setOnClickListener(this);
        headHoler.jilu.setOnClickListener(this);
        headHoler.kefu.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.cuji://我的足迹
                if(!App.checkUserLogin()){
                    Utils.startActivity(getActivity(), LoginInActivity.class);
                    return;
                }
                Utils.startActivity(getActivity(), FootPrintActivity.class);
                break;
            case R.id.jilu:
                if(!App.checkUserLogin()){
                    Utils.startActivity(getActivity(), LoginInActivity.class);
                    return;
                }
                intent = new Intent(getContext(), OrderActivity.class);
                intent.putExtra(OrderActivity.Companion.getIntent_Order_Type(), "0");
                startActivity(intent);
                break;
            case R.id.kefu:
                MaterialDialogUtils.Call(getContext(),"0577-62792277");
                break;
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initBanner();
        initHomeModelImg();
    }

    int[] imgError = {R.mipmap.ic_main_list1, R.mipmap.ic_main_list2, R.mipmap.ic_main_list3, R.mipmap.ic_main_list4};

    private void initHomeModelImg() {
        Map<String, String> map = new HashMap<>();
        map.put("requestCheck", EncryptUtils.encryptMD5ToString("HomeModelImg", App.salt)
                .toLowerCase());
        OkGo.<NetEntity<ModelImgEntity>>post(Urls.HomeModelImg).params(map).tag(this).execute(new DialogCallback<NetEntity<ModelImgEntity>>(getContext()) {
            @Override
            public void onSuccess(Response<NetEntity<ModelImgEntity>> response) {
                refresh.setRefreshing(false);
                if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                    list.clear();
                    list.add(new ImgEntity(response.body().getData().getFresh(), imgError[0]));//鲜活海鲜
                    list.add(new ImgEntity(response.body().getData().getCurrent(), imgError[1]));//当季美味
                    list.add(new ImgEntity(response.body().getData().getDryCargo(), imgError[2]));//海鲜制品
                    list.add(new ImgEntity(response.body().getData().getSpecial(), imgError[3]));//家乡特产
                    adapter.setNewData(list);
                    adapter.loadMoreEnd();

                }
            }

            @Override
            public void onError(Response<NetEntity<ModelImgEntity>> response) {
                super.onError(response);
                refresh.setRefreshing(false);
            }
        });
    }

    private void initBanner() {
        Map<String, String> map = new HashMap<>();
        map.put("type", "0");
        map.put("requestCheck", EncryptUtils.encryptMD5ToString("0", App.salt)
                .toLowerCase());
        OkGo.<NetEntity<List<BannersBean>>>post(Urls.BannerList).params(map).tag(this).execute(new JsonCallback<NetEntity<List<BannersBean>>>() {
            @Override
            public void onSuccess(Response<NetEntity<List<BannersBean>>> response) {
                refresh.setRefreshing(false);
                if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                    if (headHoler != null)
                        headHoler.banner.setAdapter(new BannerAdapter(getContext(),
                                response.body()
                                        .getData()));
                }
            }

            @Override
            public void onError(Response<NetEntity<List<BannersBean>>> response) {
                super.onError(response);
                refresh.setRefreshing(false);
            }
        });
    }


    @OnClick({R.id.msg, R.id.rlSerach})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.msg:
                if(!App.checkUserLogin()){
                    Utils.startActivity(getContext(), LoginInActivity.class);
                    return;
                }
                Utils.startActivity(getContext(), MsgListActivity.class);
                break;
            case R.id.rlSerach:

                Utils.startActivity(getContext(),SerachShowActivity.class);
                break;
        }
    }

    @Override
    public void onRefresh() {
        initBanner();
        initHomeModelImg();
    }


    static class ViewHolder {
        @BindView(R.id.banner)
        RollPagerView banner;
        @BindView(R.id.cuji)
        TextView cuji;
        @BindView(R.id.jilu)
        TextView jilu;
        @BindView(R.id.kefu)
        TextView kefu;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
