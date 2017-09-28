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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.EncryptUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.exz.wenzhoupeople.R;
import com.exz.wenzhoupeople.adapter.GoodsEvaluateAdapter;
import com.exz.wenzhoupeople.appclication.App;
import com.exz.wenzhoupeople.config.Urls;
import com.exz.wenzhoupeople.entity.JudgeCountEntity;
import com.exz.wenzhoupeople.entity.JudgeListBean;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.com.szw.lib.myframework.base.MyBaseFragment;
import cn.com.szw.lib.myframework.config.Constants;
import cn.com.szw.lib.myframework.utils.RecycleViewDivider;
import cn.com.szw.lib.myframework.utils.net.NetEntity;
import cn.com.szw.lib.myframework.utils.net.callback.JsonCallback;

/**
 * Created by pc on 2017/8/25.
 */

public class GoodsEvaluateFragment extends MyBaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    Unbinder unbinder;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;
    GoodsEvaluateAdapter adapter;
    List<JudgeListBean> list = new ArrayList<>();
    @BindView(R.id.rb1_t)
    TextView rb1T;
    @BindView(R.id.rb1_b)
    TextView rb1B;
    @BindView(R.id.llLay1)
    LinearLayout llLay1;
    @BindView(R.id.rb2_t)
    TextView rb2T;
    @BindView(R.id.rb2_b)
    TextView rb2B;
    @BindView(R.id.llLay2)
    LinearLayout llLay2;
    @BindView(R.id.rb3_t)
    TextView rb3T;
    @BindView(R.id.rb3_b)
    TextView rb3B;
    @BindView(R.id.llLay3)
    LinearLayout llLay3;
    @BindView(R.id.rb4_t)
    TextView rb4T;
    @BindView(R.id.rb4_b)
    TextView rb4B;
    @BindView(R.id.llLay4)
    LinearLayout llLay4;
    @BindView(R.id.rb5_t)
    TextView rb5T;
    @BindView(R.id.rb5_b)
    TextView rb5B;
    @BindView(R.id.llLay5)
    LinearLayout llLay5;
    int type = 0;
    List<JudgeListBean> listEntities = new ArrayList<>();
    int page = 1;
    private int refreshState = Constants.RefreshState.STATE_REFRESH;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_goods_evaluate, container, false);
        }
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;

    }

    @Override
    public void initView() {
        adapter = new GoodsEvaluateAdapter();
        adapter.setEmptyView(LayoutInflater.from(getContext()).inflate(R.layout.view_empty, new RelativeLayout(getContext()), false));
        adapter.setHeaderAndEmpty(true);
        adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new RecycleViewDivider(getContext(), LinearLayoutManager.VERTICAL,
                1, ContextCompat.getColor(getContext(), R.color.line_bg)));
        refresh.setOnRefreshListener(this);
        adapter.setOnLoadMoreListener(this, recyclerView);
        initHeader();

        onRefresh();

    }

    private void initHeader() {
        llLay1.setOnClickListener(this);
        llLay2.setOnClickListener(this);
        llLay3.setOnClickListener(this);
        llLay4.setOnClickListener(this);
        llLay5.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llLay1:
                setRadiouButtonColor(0);
                break;
            case R.id.llLay2:
                setRadiouButtonColor(1);
                break;
            case R.id.llLay3:
                setRadiouButtonColor(2);
                break;
            case R.id.llLay4:
                setRadiouButtonColor(3);
                break;
            case R.id.llLay5:
                setRadiouButtonColor(4);
                break;
        }
        onRefresh();
    }


    public void setRadiouButtonColor(int check) {
        type = check;
        switch (check) {
            case 0:
                rb1T.setTextColor(ContextCompat.getColor(getContext(), R.color.blue2));
                rb1B.setTextColor(ContextCompat.getColor(getContext(), R.color.blue2));
                rb2T.setTextColor(ContextCompat.getColor(getContext(), R.color.text_gray));
                rb2B.setTextColor(ContextCompat.getColor(getContext(), R.color.text_gray));
                rb3T.setTextColor(ContextCompat.getColor(getContext(), R.color.text_gray));
                rb3B.setTextColor(ContextCompat.getColor(getContext(), R.color.text_gray));
                rb4T.setTextColor(ContextCompat.getColor(getContext(), R.color.text_gray));
                rb4B.setTextColor(ContextCompat.getColor(getContext(), R.color.text_gray));
                rb5T.setTextColor(ContextCompat.getColor(getContext(), R.color.text_gray));
                rb5B.setTextColor(ContextCompat.getColor(getContext(), R.color.text_gray));
                break;
            case 1:
                rb1T.setTextColor(ContextCompat.getColor(getContext(), R.color.text_gray));
                rb1B.setTextColor(ContextCompat.getColor(getContext(), R.color.text_gray));
                rb2T.setTextColor(ContextCompat.getColor(getContext(), R.color.blue2));
                rb2B.setTextColor(ContextCompat.getColor(getContext(), R.color.blue2));
                rb3T.setTextColor(ContextCompat.getColor(getContext(), R.color.text_gray));
                rb3B.setTextColor(ContextCompat.getColor(getContext(), R.color.text_gray));
                rb4T.setTextColor(ContextCompat.getColor(getContext(), R.color.text_gray));
                rb4B.setTextColor(ContextCompat.getColor(getContext(), R.color.text_gray));
                rb5T.setTextColor(ContextCompat.getColor(getContext(), R.color.text_gray));
                rb5B.setTextColor(ContextCompat.getColor(getContext(), R.color.text_gray));
                break;
            case 2:
                rb1T.setTextColor(ContextCompat.getColor(getContext(), R.color.text_gray));
                rb1B.setTextColor(ContextCompat.getColor(getContext(), R.color.text_gray));
                rb2T.setTextColor(ContextCompat.getColor(getContext(), R.color.text_gray));
                rb2B.setTextColor(ContextCompat.getColor(getContext(), R.color.text_gray));
                rb3T.setTextColor(ContextCompat.getColor(getContext(), R.color.blue2));
                rb3B.setTextColor(ContextCompat.getColor(getContext(), R.color.blue2));
                rb4T.setTextColor(ContextCompat.getColor(getContext(), R.color.text_gray));
                rb4B.setTextColor(ContextCompat.getColor(getContext(), R.color.text_gray));
                rb5T.setTextColor(ContextCompat.getColor(getContext(), R.color.text_gray));
                rb5B.setTextColor(ContextCompat.getColor(getContext(), R.color.text_gray));
                break;
            case 3:
                rb1T.setTextColor(ContextCompat.getColor(getContext(), R.color.text_gray));
                rb1B.setTextColor(ContextCompat.getColor(getContext(), R.color.text_gray));
                rb2T.setTextColor(ContextCompat.getColor(getContext(), R.color.text_gray));
                rb2B.setTextColor(ContextCompat.getColor(getContext(), R.color.text_gray));
                rb3T.setTextColor(ContextCompat.getColor(getContext(), R.color.text_gray));
                rb3B.setTextColor(ContextCompat.getColor(getContext(), R.color.text_gray));
                rb4T.setTextColor(ContextCompat.getColor(getContext(), R.color.blue2));
                rb4B.setTextColor(ContextCompat.getColor(getContext(), R.color.blue2));
                rb5T.setTextColor(ContextCompat.getColor(getContext(), R.color.text_gray));
                rb5B.setTextColor(ContextCompat.getColor(getContext(), R.color.text_gray));
                break;
            case 4:
                rb1T.setTextColor(ContextCompat.getColor(getContext(), R.color.text_gray));
                rb1B.setTextColor(ContextCompat.getColor(getContext(), R.color.text_gray));
                rb2T.setTextColor(ContextCompat.getColor(getContext(), R.color.text_gray));
                rb2B.setTextColor(ContextCompat.getColor(getContext(), R.color.text_gray));
                rb3T.setTextColor(ContextCompat.getColor(getContext(), R.color.text_gray));
                rb3B.setTextColor(ContextCompat.getColor(getContext(), R.color.text_gray));
                rb4T.setTextColor(ContextCompat.getColor(getContext(), R.color.text_gray));
                rb4B.setTextColor(ContextCompat.getColor(getContext(), R.color.text_gray));
                rb5T.setTextColor(ContextCompat.getColor(getContext(), R.color.blue2));
                rb5B.setTextColor(ContextCompat.getColor(getContext(), R.color.blue2));
                break;
        }
    }

    private void initData() {

        Map<String, String> map = new HashMap<>();
        map.put("goodsId", getActivity().getIntent().getStringExtra("goodsId"));
        map.put("type", type + "");
        map.put("page", page + "");
        map.put("requestCheck", EncryptUtils.encryptMD5ToString(getActivity().getIntent().getStringExtra("goodsId") + "",
                App.salt).toLowerCase());
        OkGo.<NetEntity<List<JudgeListBean>>>post(Urls.JudgeList).params(map).tag(this).execute(new JsonCallback<NetEntity<List<JudgeListBean>>>() {
            @Override
            public void onSuccess(Response<NetEntity<List<JudgeListBean>>> response) {
                refresh.setEnabled(true);
                refresh.setRefreshing(false);
                if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                    if (refreshState == Constants.RefreshState.STATE_REFRESH) {
                        adapter.setNewData(response.body().getData());
                    } else {
                        adapter.addData(response.body().getData());
                        adapter.loadMoreComplete();
                    }
                    page++;
                    if (response.body().getData() == null || response.body().getData().size() < 1) {
                        adapter.loadMoreEnd();
                    }
                }

            }

            @Override
            public void onError(Response<NetEntity<List<JudgeListBean>>> response) {
                super.onError(response);
                refresh.setEnabled(true);
                refresh.setRefreshing(false);

            }
        });


    }

    private void JudgeCount() {
        Map<String, String> map = new HashMap<>();
        map.put("goodsId", getActivity().getIntent().getStringExtra("goodsId"));
        map.put("requestCheck", EncryptUtils.encryptMD5ToString(getActivity().getIntent().getStringExtra("goodsId") + "",
                App.salt).toLowerCase());
        OkGo.<NetEntity<JudgeCountEntity>>post(Urls.JudgeCount).params(map).tag(this).execute(new JsonCallback<NetEntity<JudgeCountEntity>>() {
            @Override
            public void onSuccess(Response<NetEntity<JudgeCountEntity>> response) {
                refresh.setEnabled(true);
                refresh.setRefreshing(false);
                if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                    rb1B.setText(response.body().getData().getTotal());//全部数量
                    rb2B.setText(response.body().getData().getNice());//好评数量
                    rb3B.setText(response.body().getData().getNormal());//中评数量
                    rb4B.setText(response.body().getData().getPoor());//差评数量
                    rb5B.setText(response.body().getData().getHaveImg());//晒图数量
                }
            }

            @Override
            public void onError(Response<NetEntity<JudgeCountEntity>> response) {
                super.onError(response);
                refresh.setEnabled(true);
                refresh.setRefreshing(false);
            }
        });


    }


    @Override
    public void onRefresh() {
        refresh.setEnabled(false);
        page = 1;
        refreshState = Constants.RefreshState.STATE_REFRESH;
        JudgeCount();
        initData();

    }

    @Override
    public void onLoadMoreRequested() {
        page++;
        refreshState = Constants.RefreshState.STATE_LOAD_MORE;
        initData();
    }
}
