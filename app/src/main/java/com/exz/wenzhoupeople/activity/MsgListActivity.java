package com.exz.wenzhoupeople.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.EncryptUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.exz.wenzhoupeople.R;
import com.exz.wenzhoupeople.adapter.MsgListAdapter;
import com.exz.wenzhoupeople.appclication.App;
import com.exz.wenzhoupeople.config.Urls;
import com.exz.wenzhoupeople.entity.MsgListEntity;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.szw.lib.myframework.base.BaseActivity;
import cn.com.szw.lib.myframework.config.Constants;
import cn.com.szw.lib.myframework.utils.net.NetEntity;
import cn.com.szw.lib.myframework.utils.net.callback.JsonCallback;

/**
 * Created by pc on 2017/8/22.
 */

public class MsgListActivity extends BaseActivity implements BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.mTitle)
    TextView mTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;
    MsgListAdapter adapter;
    int page = 1;
    private int refreshState = Constants.RefreshState.STATE_REFRESH;

    @Override
    public boolean initToolbar() {
        toolbar.setContentInsetsAbsolute(0, 0);
        mTitle.setTextSize(18);
        mTitle.setTextColor(ContextCompat.getColor(this, R.color.white));
        mTitle.setText("系统通知");
        mTitle.setTextColor(ContextCompat.getColor(mContext, R.color.text_gray));
        setSupportActionBar(toolbar);
        return false;
    }

    @Override
    public int setInflateId() {
        return R.layout.refresh_recycler_toolbar;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        adapter = new MsgListAdapter();
        adapter.setEmptyView(LayoutInflater.from(mContext).inflate(R.layout.view_empty, new RelativeLayout(mContext), false));
        adapter.setHeaderAndEmpty(true);
        adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(adapter);
        refresh.setOnRefreshListener(this);
        adapter.setOnLoadMoreListener(this, recyclerView);
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                MsgListEntity entity = (MsgListEntity) adapter.getData().get(position);
                OrderActivity.Companion.setOrderId(entity.getOrderId());
                Intent intent = new Intent(mContext, OrderDetailActivity.class);
                startActivity(intent);
            }
        });
        onRefresh();
    }

    private void initData() {
        long timestamp = System.currentTimeMillis();
        Map<String, String> map = new HashMap<>();
        map.put("id", App.getLoginUserId());
        map.put("timestamp", timestamp + "");
        map.put("page", page + "");
        map.put("requestCheck", EncryptUtils.encryptMD5ToString(App.getLoginUserId() + timestamp,
                App.salt).toLowerCase());
        OkGo.<NetEntity<List<MsgListEntity>>>post(Urls.Message).params(map).tag(this).execute(new JsonCallback<NetEntity<List<MsgListEntity>>>() {
            @Override
            public void onSuccess(Response<NetEntity<List<MsgListEntity>>> response) {
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
            public void onError(Response<NetEntity<List<MsgListEntity>>> response) {
                super.onError(response);
                refresh.setRefreshing(false);
                adapter.setNewData(JSON.parseArray(json, MsgListEntity.class));
                adapter.loadMoreEnd();
            }
        });


    }

    @OnClick(R.id.mLeftImg)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void onRefresh() {
        page = 1;
        refreshState = Constants.RefreshState.STATE_REFRESH;
        initData();

    }


    @Override
    public void onLoadMoreRequested() {
        page++;
        refreshState = Constants.RefreshState.STATE_LOAD_MORE;
        initData();
    }

    String json = "[\n" +
            "    {\n" +
            "        \"id\": \"消息id\",\n" +
            "        \"orderId\": \"订单id\",\n" +
            "        \"title\": \"标题\",\n" +
            "        \"img\": \"http://123.png\",\n" +
            "        \"subTitle\": \"内容\"\n" +
            "    }\n" +
            "]";
}
