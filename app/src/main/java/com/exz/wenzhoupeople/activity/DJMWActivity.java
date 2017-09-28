package com.exz.wenzhoupeople.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.EncryptUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.exz.wenzhoupeople.R;
import com.exz.wenzhoupeople.adapter.BannerAdapter;
import com.exz.wenzhoupeople.adapter.SeafoodListAdapter;
import com.exz.wenzhoupeople.appclication.App;
import com.exz.wenzhoupeople.config.Urls;
import com.exz.wenzhoupeople.entity.BannersBean;
import com.exz.wenzhoupeople.entity.SeafoodListEntity;
import com.exz.wenzhoupeople.entity.TabEntity;
import com.exz.wenzhoupeople.utils.MyLinearLayoutManager;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
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

import static com.exz.wenzhoupeople.config.Urls.CurrentList;


/**
 * Created by pc on 2017/8/23.
 * 当季美味
 */

public class DJMWActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener, OnTabSelectListener {
    @BindView(R.id.mTitle)
    TextView mTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;
    SeafoodListAdapter adapter;
    int priceState = 1;
    private int mSuspensionHeight;
    MyLinearLayoutManager linearLayoutManager = null;
    @BindView(R.id.mainTabBar)
    CommonTabLayout mainTabBar;

    private String[] mTitles = {"海鲜", "干货", "土特产"};
    private int[] mIconSelectIds = {
            R.mipmap.ic_haixian_blue, R.mipmap.ic_ganhuo_blue,
            R.mipmap.ic_techan_blue};
    private int[] mIconUnSelectIds = {
            R.mipmap.ic_haixian_gray, R.mipmap.ic_ganhuo_gray,
            R.mipmap.ic_techan_gray};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    int page = 1,bannerSize;
    private int refreshState = Constants.RefreshState.STATE_REFRESH;
    ViewHolder headerHolder;
    int type;
    @Override
    public boolean initToolbar() {
        toolbar.setContentInsetsAbsolute(0, 0);
        mTitle.setTextSize(18);
        mTitle.setTextColor(ContextCompat.getColor(this, R.color.white));
        mTitle.setText("当季美味");
        mTitle.setTextColor(ContextCompat.getColor(mContext, R.color.text_gray));
        setSupportActionBar(toolbar);
        return false;
    }

    @Override
    public int setInflateId() {
        return R.layout.activity_seafood_djmw;
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
        map.put("type","4");//0首页 1时下海鲜 2家乡美味 3海鲜拼装包 4当季主推 5海鲜制品 6家乡特产
        map.put("requestCheck", EncryptUtils.encryptMD5ToString("4",App.salt).toLowerCase());
        OkGo.<NetEntity<List<BannersBean>>>post(Urls.BannerList).params(map).tag(this).execute(new JsonCallback<NetEntity<List<BannersBean>>>() {
            @Override
            public void onSuccess(Response<NetEntity<List<BannersBean>>> response) {

                if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                    if (headerHolder != null)
                        headerHolder.banner.setAdapter(new BannerAdapter(mContext, response.body().getData()));
                    bannerSize=response.body().getData().size();
                    setLinColor(response.body().getData().size(), 0);
                }
            }
        });
    }

    private void setLinColor(int size, int position) {
        headerHolder.llLine.removeAllViews();
        for (int i = 0; i < size; i++) {
            View v =  LayoutInflater.from(mContext).inflate(R.layout.line_lay, null);
            TextView lin =(TextView) v.findViewById(R.id.lin1);
            if (i == position) {
                lin.setBackgroundColor(ContextCompat.getColor(mContext,R.color.blue2));
            } else {
                lin.setBackgroundColor(ContextCompat.getColor(mContext,R.color.lin_gray));
            }
            headerHolder.llLine.addView(v);
        }

    }


    private void initView() {
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnSelectIds[i]));
        }
        adapter = new SeafoodListAdapter();
        linearLayoutManager = new MyLinearLayoutManager(this);
        View viewHeader = LayoutInflater.from(mContext).inflate(R.layout.seafooter_djmw_header, null);
        adapter.addHeaderView(viewHeader);
        headerHolder = new ViewHolder(viewHeader);
        adapter.setEmptyView(LayoutInflater.from(mContext).inflate(R.layout.view_empty, new RelativeLayout(mContext), false));
        adapter.setHeaderAndEmpty(true);
        adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.VERTICAL,
                10, ContextCompat.getColor(mContext, R.color.line_bg)));
        recyclerView.setHasFixedSize(true);
        refresh.setOnRefreshListener(this);
        adapter.setOnLoadMoreListener(this, recyclerView);
        onRefresh();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                mSuspensionHeight = (int) (headerHolder.llSuspensionHeight.getHeight());
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mainTabBar.setCurrentTab(position);
                headerHolder.mainTabBar.setCurrentTab(position);
                if (linearLayoutManager.getScrollY() >= mSuspensionHeight && mSuspensionHeight > 0) {
                    headerHolder.mainTabBar.setVisibility(View.GONE);
                    mainTabBar.setVisibility(View.VISIBLE);
                } else {
                    headerHolder.mainTabBar.setVisibility(View.VISIBLE);
                    mainTabBar.setVisibility(View.GONE);
                }

                Log.i("getHeight", "mSuspensionHeight=" + mSuspensionHeight + "getScrollY=" + linearLayoutManager.getScrollY());
            }
        });


        initHeaderView();
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                SeafoodListEntity seafood= (SeafoodListEntity) adapter.getData().get(position);
                Intent intent=new Intent(mContext,GoodsDetailActivity.class);
                intent.putExtra("goodsId",seafood.getId());
                startActivity(intent);
            }
        });
    }

    private void initHeaderView() {
        headerHolder.banner.setHintView(null);
        mainTabBar.setTabData(mTabEntities);
        headerHolder.mainTabBar.setTabData(mTabEntities);
        mainTabBar.setOnTabSelectListener(this);
        headerHolder.mainTabBar.setOnTabSelectListener(this);
        headerHolder.banner.getViewPager().addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
            setLinColor(bannerSize,position);


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void initData() {

        Map<String, String> map = new HashMap<>();
        map.put("type", type+"");
        map.put("page", page + "");
        map.put("requestCheck", EncryptUtils.encryptMD5ToString(type + "",
                App.salt).toLowerCase());
        OkGo.<NetEntity<List<SeafoodListEntity>>>post(CurrentList).params(map).tag(this).execute(new JsonCallback<NetEntity<List<SeafoodListEntity>>>() {
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
                    page++;
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

    @OnClick(R.id.mLeftImg)
    public void onViewClicked() {
        finish();
    }


    int position;

    @Override
    public void onTabSelect(int position) {
        this.position = position;
        type=position;
        onRefresh();
    }

    @Override
    public void onTabReselect(int position) {

    }

    static class ViewHolder {
        @BindView(R.id.banner)
        RollPagerView banner;
        @BindView(R.id.llSuspensionHeight)
        LinearLayout llSuspensionHeight;
        @BindView(R.id.llLine)
        LinearLayout llLine;
        @BindView(R.id.mainTabBar)
        CommonTabLayout mainTabBar;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
