package com.exz.wenzhoupeople.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.EncryptUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.exz.wenzhoupeople.R;
import com.exz.wenzhoupeople.adapter.BannerAdapter;
import com.exz.wenzhoupeople.adapter.SeafoodPZBAdapter;
import com.exz.wenzhoupeople.appclication.App;
import com.exz.wenzhoupeople.config.Urls;
import com.exz.wenzhoupeople.entity.BannersBean;
import com.exz.wenzhoupeople.entity.SeafoodListEntity;
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
import cn.com.szw.lib.myframework.base.BaseActivity;
import cn.com.szw.lib.myframework.config.Constants;
import cn.com.szw.lib.myframework.utils.RecycleViewDivider;
import cn.com.szw.lib.myframework.utils.net.NetEntity;
import cn.com.szw.lib.myframework.utils.net.callback.JsonCallback;

import static com.exz.wenzhoupeople.config.Urls.BannerList;

/**
 * Created by pc on 2017/8/24.
 * 海鲜拼装包
 */

public class SeafoodPZBActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    @BindView(R.id.mTitle)
    TextView mTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;
    List<SeafoodListEntity> data = new ArrayList<>();
    SeafoodPZBAdapter adapter;
    ViewHolder headerHolder;
    @BindView(R.id.rb)
    RadioGroup rb;
    @BindView(R.id.rb1)
    RadioButton rb1;
    @BindView(R.id.rb2)
    RadioButton rb2;
    @BindView(R.id.rb3)
    RadioButton rb3;
    private int mSuspensionHeight;
    int type = 1;
    LinearLayoutManager linearLayoutManager = null;
    int page = 1;
    private int refreshState = Constants.RefreshState.STATE_REFRESH;
    @Override
    public boolean initToolbar() {
        toolbar.setContentInsetsAbsolute(0, 0);
        mTitle.setTextSize(18);
        mTitle.setTextColor(ContextCompat.getColor(this, R.color.white));
        mTitle.setText("海鲜拼装包");
        mTitle.setTextColor(ContextCompat.getColor(mContext, R.color.text_gray));
        setSupportActionBar(toolbar);
        return false;
    }

    @Override
    public int setInflateId() {
        return R.layout.activity_seafood_pzb;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initView();
        initBanner();
    }

    private void initBanner() {
        Map<String, String> map = new HashMap<>();
        map.put("type","3");
        map.put("requestCheck", EncryptUtils.encryptMD5ToString("3",App.salt).toLowerCase());
        OkGo.<NetEntity<List<BannersBean>>>post(BannerList).params(map).tag(this).execute(new JsonCallback<NetEntity<List<BannersBean>>>() {
            @Override
            public void onSuccess(Response<NetEntity<List<BannersBean>>> response) {

                if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                    if (headerHolder != null)
                        headerHolder.banner.setAdapter(new BannerAdapter(mContext,
                                response.body()
                                        .getData()));
                }
            }

        });
    }


    private void initView() {
        adapter = new SeafoodPZBAdapter();
        View viewHeader = LayoutInflater.from(mContext).inflate(R.layout.seafooter_pzb_header, null);
        adapter.addHeaderView(viewHeader);
        headerHolder = new ViewHolder(viewHeader);
        adapter.setEmptyView(LayoutInflater.from(mContext).inflate(R.layout.view_empty, new RelativeLayout(mContext), false));
        adapter.setHeaderAndEmpty(true);
        adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        adapter.setNewData(data);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.VERTICAL,
                1, ContextCompat.getColor(mContext, R.color.line_bg)));
        headerHolder.rb.setOnCheckedChangeListener(this);
        rb.setOnCheckedChangeListener(this);
        recyclerView.setHasFixedSize(true);
        refresh.setOnRefreshListener(this);
        adapter.setOnLoadMoreListener(this, recyclerView);
       onRefresh();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                mSuspensionHeight = (int) (headerHolder.banner.getHeight() * 0.7);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                switch (type){
                    case 0:
                        rb1.setChecked(true);
                        headerHolder.rb1.setChecked(true);
                        break;
                    case 1:
                        rb2.setChecked(true);
                        headerHolder.rb2.setChecked(true);
                        break;
                    case 2:
                        rb3.setChecked(true);
                        headerHolder.rb3.setChecked(true);
                        break;

                }
                if (getScollYDistance() >= mSuspensionHeight && mSuspensionHeight > 0) {
                    headerHolder.rb.setVisibility(View.GONE);
                    rb.setVisibility(View.VISIBLE);
                } else {
                    headerHolder.rb.setVisibility(View.VISIBLE);
                    rb.setVisibility(View.GONE);
                }

            }
        });

        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                SeafoodListEntity entity = (SeafoodListEntity) adapter.getData().get(position);
                Intent intent = new Intent(mContext, GoodsDetailActivity.class);
                intent.putExtra("goodsId", entity.getId());
                startActivity(intent);
            }
        });
    }

    /**
     * 获取RecyclerView滚动位置
     */
    public int getScollYDistance() {
        View firstVisibItem = recyclerView.getChildAt(0);
        int firstItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
        int itemCount = linearLayoutManager.getItemCount();
        int recycleViewHeight = recyclerView.getHeight();
        int itemHeight = firstVisibItem.getHeight();
        int firstItemBottom = linearLayoutManager.getDecoratedBottom(firstVisibItem);
        return (firstItemPosition + 1) * itemHeight - firstItemBottom;
    }

    @OnClick(R.id.mLeftImg)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
        switch (i) {
            case R.id.rb1:
                type = 0;
                break;
            case R.id.rb2:
                type = 1;
                break;
            case R.id.rb3:
                type = 2;
                break;

        }
        refresh();
    }

    public  void refresh(){
      new CountDownTimer(200, 1) {
          @Override
          public void onTick(long l) {

          }

          @Override
          public void onFinish() {
              onRefresh();

          }
      }.start();
    }

    private void initData() {

        Map<String, String> map = new HashMap<>();
        map.put("type", type+"");
        map.put("page", page + "");
        map.put("requestCheck", EncryptUtils.encryptMD5ToString(type + "",
                App.salt).toLowerCase());
        OkGo.<NetEntity<List<SeafoodListEntity>>>post(Urls.FreshPackageList).params(map).tag(this).execute(new JsonCallback<NetEntity<List<SeafoodListEntity>>>() {
            @Override
            public void onSuccess(Response<NetEntity<List<SeafoodListEntity>>> response) {
                refresh.setEnabled(true);
                refresh.setRefreshing(false);
                if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                    if (refreshState == Constants.RefreshState.STATE_REFRESH) {
                        adapter.setNewData(response.body().getData());
                    } else {
                        adapter.addData(response.body().getData());
                        adapter.loadMoreComplete();
                    }

                    if (response.body().getData() == null || response.body().getData().size() < 1) {
                        adapter.loadMoreEnd();
                    }
                }
            }

            @Override
            public void onError(Response<NetEntity<List<SeafoodListEntity>>> response) {
                super.onError(response);
                refresh.setEnabled(true);
                refresh.setRefreshing(false);
            }
        });

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
    static class ViewHolder {
        @BindView(R.id.banner)
        RollPagerView banner;
        @BindView(R.id.rb1)
        RadioButton rb1;
        @BindView(R.id.rb2)
        RadioButton rb2;
        @BindView(R.id.rb3)
        RadioButton rb3;
        @BindView(R.id.rb)
        RadioGroup rb;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
