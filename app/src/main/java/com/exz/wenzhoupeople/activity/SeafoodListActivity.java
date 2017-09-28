package com.exz.wenzhoupeople.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
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
import com.exz.wenzhoupeople.pop.SerachPop;
import com.jude.rollviewpager.RollPagerView;
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
import cn.com.szw.lib.myframework.utils.net.callback.JsonCallback;

import static com.exz.wenzhoupeople.activity.SerachShowActivity.Intent_Search_Content;
import static com.exz.wenzhoupeople.config.Urls.BannerList;
import static com.exz.wenzhoupeople.config.Urls.DryCargoList;


/**
 * Created by pc on 2017/8/23.
 */

public class SeafoodListActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    @BindView(R.id.mTitle)
    TextView mTitle;
    @BindView(R.id.edSearch)
    TextView edSearch;
    @BindView(R.id.tvSearch)
    TextView tvSearch;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbarSearch)
    Toolbar toolbarSearch;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;
    SeafoodListAdapter adapter;
    ViewHolder headerHolder;
    int priceState = 1;
    @BindView(R.id.rb)
    RadioGroup rb;
    @BindView(R.id.toolbarSpecialty)
    Toolbar toolbarSpecialty;
    private int mSuspensionHeight;
    LinearLayoutManager linearLayoutManager = null;

    @BindView(R.id.mLeftImgSearch)
    ImageView mLeftImgSearch;
    @BindView(R.id.type)
    TextView tvTypeSerach;
    @BindView(R.id.btSearch)
    TextView btSearch;
    @BindView(R.id.synthesize_)
    TextView synthesize_;
    @BindView(R.id.lin1_)
    View lin1_;
    @BindView(R.id.sales_)
    TextView sales_;
    @BindView(R.id.lin2_)
    View lin2_;
    @BindView(R.id.price_)
    TextView price_;
    @BindView(R.id.lin3_)
    View lin3_;
    @BindView(R.id.llLay_)
    LinearLayout llLay_;
    int page = 1;
    private int refreshState = Constants.RefreshState.STATE_REFRESH;
    private String type = "", bannerType = "0", ulrs = "", squence = "0", requestCheck = "";
    public String content = "";

    @Override
    public boolean initToolbar() {
        toolbar.setContentInsetsAbsolute(0, 0);
        toolbarSpecialty.setContentInsetsAbsolute(0, 0);
        toolbarSearch.setContentInsetsAbsolute(0, 0);
        mTitle.setTextSize(18);
        mTitle.setText(getIntent().getStringExtra("className"));
        mTitle.setTextColor(ContextCompat.getColor(mContext, R.color.text_gray));
        return false;
    }

    @Override
    public int setInflateId() {
        return R.layout.activity_seafood_list;
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
        map.put("type", bannerType);
        map.put("requestCheck", EncryptUtils.encryptMD5ToString(bannerType, App.salt).toLowerCase());
        OkGo.<NetEntity<List<BannersBean>>>post(BannerList).params(map).tag(this).execute(new JsonCallback<NetEntity<List<BannersBean>>>() {
            @Override
            public void onSuccess(Response<NetEntity<List<BannersBean>>> response) {

                if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                    if (headerHolder != null)
                        headerHolder.banner.setAdapter(new BannerAdapter(mContext,
                                response.body().getData()));
                }
            }

        });
    }

    private void initView() {
        // bannerType 0首页 1时下海鲜 2家乡美味 3海鲜拼装包 4当季主推 5海鲜制品 6家乡特产
        switch (getIntent().getStringExtra("className")) {
            case "时下海鲜":
                type = "0";
                ulrs = Urls.FreshSignList;
                bannerType = "1";
                break;
            case "家乡美味":
                type = "1";
                ulrs = Urls.FreshSignList;
                bannerType = "1";
                break;
            case "海鲜制品":
                type = "";
                ulrs = DryCargoList;
                bannerType = "5";
                break;
            case "家乡特产":
                type = "0";
                ulrs = Urls.SpecialList;
                bannerType = "6";
                toolbar.setVisibility(View.GONE);
                toolbarSpecialty.setVisibility(View.VISIBLE);
                break;
        }

        adapter = new SeafoodListAdapter();
        linearLayoutManager = new LinearLayoutManager(this);
        View viewHeader = LayoutInflater.from(mContext).inflate(R.layout.seafooter_list_header, null);
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

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                mSuspensionHeight = (int) (headerHolder.llLay.getHeight() * 2);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (getScollYDistance() >= mSuspensionHeight && mSuspensionHeight > 0) {
                    headerHolder.llLay.setVisibility(View.GONE);
                    llLay_.setVisibility(View.VISIBLE);
                    Log.i("getHeight", "llLay=GONE");
                } else {
                    Log.i("getHeight", "llLay_=GONE");
                    headerHolder.llLay.setVisibility(View.VISIBLE);
                    llLay_.setVisibility(View.GONE);
                }

                Log.i("getHeight", "mSuspensionHeight=" + mSuspensionHeight + "getScollYDistance=" + getScollYDistance());
            }
        });
        initFooter();
        rb.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i) {
                    case R.id.rb1:
                        type = "0";
                        break;
                    case R.id.rb2:
                        type = "1";
                        break;

                }
                onRefresh();
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
        if (getIntent().hasExtra(Intent_Search_Content)) {
            type = SerachPop.type;
            content = getIntent().getStringExtra(Intent_Search_Content);
            ulrs = Urls.SearchList;
            headerHolder.banner.setVisibility(View.GONE);
            toolbar.setVisibility(View.GONE);
            btSearch.setVisibility(View.GONE);
            tvTypeSerach.setVisibility(View.GONE);
            edSearch.setVisibility(View.GONE);
            tvSearch.setVisibility(View.VISIBLE);
            tvSearch.setPadding(15,15,15,15);
            tvSearch.setText(content);
            toolbarSearch.setVisibility(View.VISIBLE);
            tvSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(mContext,SerachShowActivity.class);
                    intent.putExtra(Intent_Search_Content,content);
                    startActivity(intent);
                    finish();
                }
            });
        }
        onRefresh();
    }

    private void initData() {
        switch (getIntent().getStringExtra("className")) {
            case "时下海鲜":
                requestCheck = EncryptUtils.encryptMD5ToString(type + "", App.salt).toLowerCase();
                break;
            case "家乡美味":
                requestCheck = EncryptUtils.encryptMD5ToString(type + "", App.salt).toLowerCase();
                break;
            case "海鲜制品":
                requestCheck = EncryptUtils.encryptMD5ToString("DryCargoList", App.salt).toLowerCase();
                break;
            case "家乡特产":
                requestCheck = EncryptUtils.encryptMD5ToString(type + "", App.salt).toLowerCase();
                break;
            case "":
                requestCheck = EncryptUtils.encryptMD5ToString(type + "", App.salt).toLowerCase();
                break;
        }
        Map<String, String> map = new HashMap<>();
        if (!TextUtils.isEmpty(type)) map.put("type", type);
        if (!TextUtils.isEmpty(content)) map.put("content", content);
        map.put("page", page + "");
        map.put("squence", squence);
        map.put("requestCheck", requestCheck);
        OkGo.<NetEntity<List<SeafoodListEntity>>>post(ulrs).params(map).tag(this).execute(new JsonCallback<NetEntity<List<SeafoodListEntity>>>() {
            @Override
            public void onSuccess(Response<NetEntity<List<SeafoodListEntity>>> response) {
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

        refreshState = Constants.RefreshState.STATE_LOAD_MORE;
        initData();
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

    private void initFooter() {
        headerHolder.synthesize.setOnClickListener(this);
        headerHolder.sales.setOnClickListener(this);
        headerHolder.price.setOnClickListener(this);
        synthesize_.setOnClickListener(this);
        sales_.setOnClickListener(this);
        price_.setOnClickListener(this);
    }

    private void setTextLineColor(int page) {
        Drawable d = null;
        switch (page) {
            case 1:
                d = ContextCompat.getDrawable(mContext, R.mipmap.ic_price_gray);
                headerHolder.synthesize.setTextColor(ContextCompat.getColor(mContext, R.color.blue2));
                headerHolder.lin1.setBackgroundColor(ContextCompat.getColor(mContext, R.color.blue2));
                headerHolder.sales.setTextColor(ContextCompat.getColor(mContext, R.color.text_gray));
                headerHolder.lin2.setBackgroundColor(ContextCompat.getColor(mContext, R.color.lin_gray));
                headerHolder.price.setTextColor(ContextCompat.getColor(mContext, R.color.text_gray));
                headerHolder.lin3.setBackgroundColor(ContextCompat.getColor(mContext, R.color.lin_gray));
                headerHolder.price.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, d, null);

                synthesize_.setTextColor(ContextCompat.getColor(mContext, R.color.blue2));
                lin1_.setBackgroundColor(ContextCompat.getColor(mContext, R.color.blue2));
                sales_.setTextColor(ContextCompat.getColor(mContext, R.color.text_gray));
                lin2_.setBackgroundColor(ContextCompat.getColor(mContext, R.color.lin_gray));
                price_.setTextColor(ContextCompat.getColor(mContext, R.color.text_gray));
                lin3_.setBackgroundColor(ContextCompat.getColor(mContext, R.color.lin_gray));
                price_.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, d, null);
                break;
            case 2:
                d = ContextCompat.getDrawable(mContext, R.mipmap.ic_price_gray);
                headerHolder.sales.setTextColor(ContextCompat.getColor(mContext, R.color.blue2));
                headerHolder.lin2.setBackgroundColor(ContextCompat.getColor(mContext, R.color.blue2));
                headerHolder.synthesize.setTextColor(ContextCompat.getColor(mContext, R.color.text_gray));
                headerHolder.lin1.setBackgroundColor(ContextCompat.getColor(mContext, R.color.lin_gray));
                headerHolder.price.setTextColor(ContextCompat.getColor(mContext, R.color.text_gray));
                headerHolder.lin3.setBackgroundColor(ContextCompat.getColor(mContext, R.color.lin_gray));
                headerHolder.price.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, d, null);

                sales_.setTextColor(ContextCompat.getColor(mContext, R.color.blue2));
                lin2_.setBackgroundColor(ContextCompat.getColor(mContext, R.color.blue2));
                synthesize_.setTextColor(ContextCompat.getColor(mContext, R.color.text_gray));
                lin1_.setBackgroundColor(ContextCompat.getColor(mContext, R.color.lin_gray));
                price_.setTextColor(ContextCompat.getColor(mContext, R.color.text_gray));
                lin3_.setBackgroundColor(ContextCompat.getColor(mContext, R.color.lin_gray));
                price_.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, d, null);
                break;
            case 3:
                headerHolder.price.setTextColor(ContextCompat.getColor(mContext, R.color.blue2));
                headerHolder.lin3.setBackgroundColor(ContextCompat.getColor(mContext, R.color.blue2));
                headerHolder.synthesize.setTextColor(ContextCompat.getColor(mContext, R.color.text_gray));
                headerHolder.lin1.setBackgroundColor(ContextCompat.getColor(mContext, R.color.lin_gray));
                headerHolder.sales.setTextColor(ContextCompat.getColor(mContext, R.color.text_gray));
                headerHolder.lin2.setBackgroundColor(ContextCompat.getColor(mContext, R.color.lin_gray));

                price_.setTextColor(ContextCompat.getColor(mContext, R.color.blue2));
                lin3_.setBackgroundColor(ContextCompat.getColor(mContext, R.color.blue2));
                synthesize_.setTextColor(ContextCompat.getColor(mContext, R.color.text_gray));
                lin1_.setBackgroundColor(ContextCompat.getColor(mContext, R.color.lin_gray));
                sales_.setTextColor(ContextCompat.getColor(mContext, R.color.text_gray));
                lin2_.setBackgroundColor(ContextCompat.getColor(mContext, R.color.lin_gray));
                if (priceState == 2) {
                    d = ContextCompat.getDrawable(mContext, R.mipmap.ic_price_top_blue);
                    headerHolder.price.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, d, null);
                    price_.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, d, null);
                } else {
                    d = ContextCompat.getDrawable(mContext, R.mipmap.ic_price_bottom_blue);
                    headerHolder.price.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, d, null);
                    price_.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, d, null);
                }
                break;
        }


    }

    @OnClick({R.id.mLeftImg,R.id.mLeftImgSearch, R.id.mLeftImgSpecialty})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mLeftImg:
            case R.id.mLeftImgSearch:
                finish();
                break;
            case R.id.mLeftImgSpecialty:
                finish();
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.synthesize:
            case R.id.synthesize_:
                priceState = 1;
                squence = "0";
                setTextLineColor(1);
                break;
            case R.id.sales:
            case R.id.sales_:
                priceState = 1;
                squence = "1";
                setTextLineColor(2);
                break;
            case R.id.price:
            case R.id.price_:
                switch (priceState) {
                    case 1:
                        squence = "2";
                        priceState = 2;
                        break;
                    case 2:
                        squence = "3";
                        priceState = 3;
                        break;
                    case 3:
                        squence = "2";
                        priceState = 2;
                        break;
                }
                setTextLineColor(3);
                break;
        }
        onRefresh();
    }

    static class ViewHolder {
        @BindView(R.id.banner)
        RollPagerView banner;
        @BindView(R.id.llLay)
        LinearLayout llLay;
        @BindView(R.id.synthesize)
        TextView synthesize;
        @BindView(R.id.lin1)
        View lin1;
        @BindView(R.id.sales)
        TextView sales;
        @BindView(R.id.lin2)
        View lin2;
        @BindView(R.id.price)
        TextView price;
        @BindView(R.id.lin3)
        View lin3;


        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
