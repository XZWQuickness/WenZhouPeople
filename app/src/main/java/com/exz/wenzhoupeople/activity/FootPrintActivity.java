package com.exz.wenzhoupeople.activity;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.EncryptUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.exz.wenzhoupeople.R;
import com.exz.wenzhoupeople.adapter.FootPrintAdapter;
import com.exz.wenzhoupeople.appclication.App;
import com.exz.wenzhoupeople.config.Constants;
import com.exz.wenzhoupeople.entity.FootPrintModel;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.szw.lib.myframework.base.BaseActivity;
import cn.com.szw.lib.myframework.utils.DateUtils;
import cn.com.szw.lib.myframework.utils.DialogUtils;
import cn.com.szw.lib.myframework.utils.RecycleViewDivider;
import cn.com.szw.lib.myframework.utils.net.NetEntity;
import cn.com.szw.lib.myframework.utils.net.callback.DialogCallback;
import cn.com.szw.lib.myframework.view.CustomLoadMoreView;

import static com.exz.wenzhoupeople.config.Urls.BrowseClear;
import static com.exz.wenzhoupeople.config.Urls.BrowseList;

/**
 * Created by weicao on 2017/8/30.
 * 我的足迹
 */

public class FootPrintActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
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
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;
    public static Date Time = null;
    private int page = 1;
    public int refreshState = Constants.RefreshState.STATE_REFRESH;

    private FootPrintAdapter adapter;
    private List<FootPrintModel> list = new ArrayList();


    @Override
    public boolean initToolbar() {
        mTitle.setText("我的足迹");
        mTitle.setTextSize(18);
        toolbar.setContentInsetsAbsolute(0, 0);
        setSupportActionBar(toolbar);
        mRightImg.setImageDrawable(getResources().getDrawable(R.mipmap.icon_mine_delete));
        mRightImg.setPadding(0, 0, 20, 0);
        return false;
    }

    @Override
    public int setInflateId() {
        return R.layout.activity_foot_print;
    }

    @Override
    public void init() throws Exception {
        super.init();
        initData();
        onRefresh();
    }

    private void initData() {

//        list.add(new FootPrintModel("1","http://img5q.duitang.com/uploads/item/201506/13/20150613114446_LkSZH.jpeg","小野马","100","11月31号"));
//        list.add(new FootPrintModel("1","http://img5q.duitang.com/uploads/item/201506/13/20150613114446_LkSZH.jpeg","小野马","100","11月31号"));
//        list.add(new FootPrintModel("1","http://img5q.duitang.com/uploads/item/201506/13/20150613114446_LkSZH.jpeg","小野马","100","11月31号"));
//        list.add(new FootPrintModel("1","http://img5q.duitang.com/uploads/item/201506/13/20150613114446_LkSZH.jpeg","小野马","100","12月31号"));
//        list.add(new FootPrintModel("1","http://img5q.duitang.com/uploads/item/201506/13/20150613114446_LkSZH.jpeg","小野马","100","12月31号"));
//        list.add(new FootPrintModel("1","http://img5q.duitang.com/uploads/item/201506/13/20150613114446_LkSZH.jpeg","小野马","100","12月31号"));


        adapter = new FootPrintAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL, 1, ContextCompat.getColor(this, R.color.lin_gray)));
        adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        adapter.setLoadMoreView(new CustomLoadMoreView());
        adapter.setOnLoadMoreListener(this, recyclerView);
        refresh.setOnRefreshListener(this);
        adapter.setEmptyView(LayoutInflater.from(mContext).inflate(R.layout.view_empty, new RelativeLayout(mContext), false));
        recyclerView.setAdapter(adapter);

//        adapter.setNewData(list);


        recyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()){
                    case R.id.layout:
                        Intent intent = new Intent(mContext,GoodsDetailActivity.class);
                        intent.putExtra("goodsId",((FootPrintModel)adapter.getData().get(position)).getId());
                        startActivity(intent);
                        break;
                }
            }
        });


    }


    @OnClick({R.id.mRightImg, R.id.mLeftImg})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mRightImg:
                if (adapter.getData().size() != 0) {
                    DialogUtils.reminder(mContext, "提示", "确定要清空商品浏览记录吗？", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
//                            list.clear();
//                            adapter.notifyDataSetChanged();

                            delete();
                        }
                    });
                }

                break;
            case R.id.mLeftImg:
                finish();
                break;
        }
    }


    //    //删除
    private void delete() {
        Map<String, String> map = new HashMap<>();
        map.put("id", App.getLoginUserId());
        map.put("timestamp", String.valueOf(DateUtils.dateToUnixTimestamp()));
        map.put("requestCheck", EncryptUtils.encryptMD5ToString(App.getLoginUserId() + String.valueOf(DateUtils.dateToUnixTimestamp()), App.salt)
                .toLowerCase());
        OkGo.<NetEntity<Void>>post(BrowseClear).params(map).tag(this).execute(new DialogCallback<NetEntity<Void>>(mContext) {
            @Override
            public void onSuccess(Response<NetEntity<Void>> response) {
                if (response.body().getCode() == cn.com.szw.lib.myframework.config.Constants.NetCode.SUCCESS) {
                    onRefresh();
                }
            }

        });
    }

    private void initPort() {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", App.getLoginUserId());
        params.put("timestamp", String.valueOf(DateUtils.dateToUnixTimestamp()));
        params.put("page", String.valueOf(page));
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(App.getLoginUserId() + String.valueOf(DateUtils.dateToUnixTimestamp()) + App.salt).toLowerCase());
        OkGo.<NetEntity<List<FootPrintModel>>>post(BrowseList)
                .params(params)
                .tag(this)
                .execute(new DialogCallback<NetEntity<List<FootPrintModel>>>(mContext) {

                    @Override
                    public void onSuccess(Response<NetEntity<List<FootPrintModel>>> response) {
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
                    public void onError(Response<NetEntity<List<FootPrintModel>>> response) {
                        super.onError(response);

                        refresh.setEnabled(true);
                        refresh.setRefreshing(false);
                        Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        adapter.loadMoreFail();
                    }
                });
    }

    @Override
    public void onRefresh() {
        refresh.setRefreshing(false);
        page = 1;
        refreshState = Constants.RefreshState.STATE_REFRESH;
//        initData();
        Time =null;
        initPort();

    }

    @Override
    public void onLoadMoreRequested() {
        refresh.setEnabled(false);
        refreshState = Constants.RefreshState.STATE_LOAD_MORE;
        initPort();
    }
}
