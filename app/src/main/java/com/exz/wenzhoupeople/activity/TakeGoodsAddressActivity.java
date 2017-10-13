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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.EncryptUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.exz.wenzhoupeople.R;
import com.exz.wenzhoupeople.adapter.TakeGoodsAddressAdapter;
import com.exz.wenzhoupeople.appclication.App;
import com.exz.wenzhoupeople.config.Urls;
import com.exz.wenzhoupeople.entity.TakeGoodsAddressEntity;
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
import cn.com.szw.lib.myframework.utils.RecycleViewDivider;
import cn.com.szw.lib.myframework.utils.net.NetEntity;
import cn.com.szw.lib.myframework.utils.net.callback.DialogCallback;
import cn.com.szw.lib.myframework.view.CustomLoadMoreView;

import static com.exz.wenzhoupeople.activity.ConfirmOrderActivity.RESULTCODE_ADDRESS;

/**
 * Created by pc on 2017/8/29.
 * 选择取货地址
 */

public class TakeGoodsAddressActivity extends BaseActivity {
    @BindView(R.id.mTitle)
    TextView mTitle;
    @BindView(R.id.mRight)
    TextView mRight;
    @BindView(R.id.mLeftImg)
    ImageView mLeftImg;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;
    TakeGoodsAddressAdapter adapter;
    int page = 1;
    private int refreshState = Constants.RefreshState.STATE_REFRESH;
    String className = "";
    public static final int RESULTCODE_REFRESH = 1003;

    @Override
    public boolean initToolbar() {
        toolbar.setContentInsetsAbsolute(0, 0);
        mTitle.setTextSize(18);
        mTitle.setText(getIntent().getStringExtra("className"));
        mRight.setTextSize(18);
        if (getIntent().getStringExtra("className").equals("选择收货地址")) {

            mRight.setText("管理");
        }
        setSupportActionBar(toolbar);
        return false;
    }

    @Override
    public int setInflateId() {
        return R.layout.activity_goods_address;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        refresh.setEnabled(false);
        className = getIntent().getStringExtra("className");
        adapter = new TakeGoodsAddressAdapter(className);
        adapter.setLoadMoreView(new CustomLoadMoreView());
        adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter.setEmptyView(LayoutInflater.from(this).inflate(R.layout.view_empty, new RelativeLayout(this), false));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.VERTICAL,
                1, ContextCompat.getColor(mContext, R.color.line_bg)));
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter a, View view, int position) {
                TakeGoodsAddressEntity entity = (TakeGoodsAddressEntity) a.getData().get(position);
                Bundle b = new Bundle();
                if (className.equals("选择收货地址")) {
                    for (TakeGoodsAddressEntity t : adapter.getData()) {
                        t.setIsDef("0");
                    }
                    entity.setIsDef("1");
                    adapter.notifyDataSetChanged();
                    b.putSerializable("bean", entity);
                    setResult(RESULTCODE_ADDRESS, new Intent().putExtras(b));
                    finish();
                } else {
                    adapter.notifyDataSetChanged();
                    b.putSerializable("bean", entity);
                    Intent intent = new Intent(mContext,GoodsAddressDetailActivity.class);
                    intent.putExtras(b);
                    startActivityForResult(intent, RESULTCODE_REFRESH);
                }
            }
        });
        initData();
    }


    private void initData() {
        long timestamp=System.currentTimeMillis();
        Map<String, String> map = new HashMap<>();
        map.put("id", App.getLoginUserId());
        map.put("timestamp", timestamp + "");
        map.put("requestCheck", EncryptUtils.encryptMD5ToString( App.getLoginUserId() + timestamp,
                App.salt).toLowerCase());
        OkGo.<NetEntity<List<TakeGoodsAddressEntity>>>post(Urls.AddressList).params(map).tag(this).execute(new DialogCallback<NetEntity<List<TakeGoodsAddressEntity>>>(mContext) {
            @Override
            public void onSuccess(Response<NetEntity<List<TakeGoodsAddressEntity>>> response) {
                if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                    if (refreshState == Constants.RefreshState.STATE_REFRESH) {
                        adapter.setNewData(response.body().getData());
                        if (getIntent().getStringExtra("bean") != null) {
                            adapter.getData().get(adapter.getData().indexOf(getIntent().getStringExtra("bean"))).setIsDef("1");
                        } else if (adapter.getData().size() > 0)
                            adapter.getData().get(0).setIsDef("1");
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
            public void onError(Response<NetEntity<List<TakeGoodsAddressEntity>>> response) {
                super.onError(response);
                adapter.setNewData(JSON.parseArray("[\n" +
                        "    {\n" +
                        "        \"id\": \"id\",\n" +
                        "        \"name\": \"姓名\",\n" +
                        "        \"phone\": \"电话号码\",\n" +
                        "        \"area\": \"所在地区（省市区）\",\n" +
                        "        \"detail\": \"详细地址\",\n" +
                        "        \"type\": \"0\",\n" +
                        "        \"isDef\": \"1\"\n" +
                        "    }, {\n" +
                        "        \"id\": \"id\",\n" +
                        "        \"name\": \"姓名\",\n" +
                        "        \"phone\": \"电话号码\",\n" +
                        "        \"area\": \"所在地区（省市区）\",\n" +
                        "        \"detail\": \"详细地址\",\n" +
                        "        \"type\": \"1\",\n" +
                        "        \"isDef\": \"0\"\n" +
                        "    }\n" +
                        "]", TakeGoodsAddressEntity.class));


                if (getIntent().getStringExtra("id") != null) {
                    for (TakeGoodsAddressEntity e : adapter.getData()) {
                        if (e.getId().equals(getIntent().getStringExtra("id"))) {
                            adapter.getData().get(adapter.getData().indexOf(e)).setIsDef("0");
                        }
                    }
                } else if (adapter.getData().size() > 0) adapter.getData().get(0).setIsDef("1");

                adapter.loadMoreEnd();
            }

        });


    }
    @OnClick({R.id.mRight, R.id.mLeftImg, R.id.addAddress})
    public void onViewClicked(View view) {
        Intent intent=null;
        switch (view.getId()) {
            case R.id.mRight:
                intent=new Intent(mContext,TakeGoodsAddressActivity.class);
                intent.putExtra("className","管理收货地址");
                startActivity(intent);
                break;
            case R.id.mLeftImg:
                finish();
                break;
            case R.id.addAddress:
                intent=new Intent(mContext,AddGoodsAddressActivity.class);
                intent.putExtra("className","新增收货地址");
                startActivityForResult(intent, RESULTCODE_REFRESH);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULTCODE_REFRESH:
             initData();
                break;
        }

    }


}
