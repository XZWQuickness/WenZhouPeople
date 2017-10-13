package com.exz.wenzhoupeople.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.EncryptUtils;
import com.exz.wenzhoupeople.R;
import com.exz.wenzhoupeople.appclication.App;
import com.exz.wenzhoupeople.entity.GoodsClassifyBean;
import com.exz.wenzhoupeople.entity.GoodsDetailEntity;
import com.exz.wenzhoupeople.fragment.GoodsDetailFragment;
import com.exz.wenzhoupeople.fragment.GoodsEvaluateFragment;
import com.exz.wenzhoupeople.pop.GoodsDetailClassifyPop;
import com.exz.wenzhoupeople.utils.FixedSpeedScroller;
import com.exz.wenzhoupeople.utils.ScrollViewPager;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.szw.lib.myframework.base.BaseActivity;
import cn.com.szw.lib.myframework.config.Constants;
import cn.com.szw.lib.myframework.utils.Utils;
import cn.com.szw.lib.myframework.utils.net.NetEntity;
import cn.com.szw.lib.myframework.utils.net.callback.DialogCallback;

import static com.exz.wenzhoupeople.config.Urls.CollectAction;
import static com.exz.wenzhoupeople.config.Urls.Detail;
import static com.exz.wenzhoupeople.config.Urls.Rank;

/**
 * Created by pc on 2017/8/25.
 * 商品详情
 */

public class GoodsDetailActivity extends BaseActivity implements GoodsDetailFragment.OnChooseListener, GoodsDetailFragment.OnMoreCommentListener {
    @BindView(R.id.mTitle)
    TextView mTitle;
    @BindView(R.id.mLeftImg)
    ImageView mLeftImg;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collect)
    TextView collect;
    @BindView(R.id.mScrollViewPager)
    ScrollViewPager viewPager;
    @BindView(R.id.addGWC)
    TextView addGWC;
    private MyPagerAdapter pagerAdapter;
    GoodsDetailFragment mGoodsDetailFragment;
    GoodsEvaluateFragment mGoodsEvaluateFragment;
    GoodsDetailClassifyPop mGoodsDetailClassifyPop;
    GoodsDetailEntity mGoodsDetailEntity;
    String type = "";

    @Override
    public boolean initToolbar() {
        toolbar.setContentInsetsAbsolute(0, 0);
        mTitle.setTextSize(18);
        mTitle.setTextColor(ContextCompat.getColor(this, R.color.white));
        mTitle.setText("商品详情");
        mTitle.setTextColor(ContextCompat.getColor(mContext, R.color.text_gray));
        setSupportActionBar(toolbar);
        return false;
    }

    @Override
    public int setInflateId() {
        return R.layout.activity_goods_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initView();
        initData();

    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void getRank(final GoodsDetailEntity data) {
        Map<String, String> map = new HashMap<>();
        if (!TextUtils.isEmpty(App.getLoginUserId())) map.put("id", App.getLoginUserId());
        map.put("goodsId", getIntent().getStringExtra("goodsId"));
        map.put("requestCheck", EncryptUtils.encryptMD5ToString(getIntent().getStringExtra("goodsId"), App.salt)
                .toLowerCase());
        OkGo.<NetEntity<GoodsClassifyBean>>post(Rank).params(map).tag(this).execute(new DialogCallback<NetEntity<GoodsClassifyBean>>(mContext) {
            @Override
            public void onSuccess(Response<NetEntity<GoodsClassifyBean>> response) {
                if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                    if (mGoodsDetailClassifyPop != null) {
//                        GoodsClassifyBean d = JSON.parseObject(guige, GoodsClassifyBean.class);
                        GoodsClassifyBean d = response.body().getData();
                        d.setHeaderImg(data.getImgUrls().size() > 0 ? data.getImgUrls().get(0) : "");
                        d.setPrice(data.getPrice());
                        d.setStock(data.getStock());
                        String typeName = "";
                        if (d.getRankInfo() != null) {
                            for (GoodsClassifyBean.RankInfoBean g : d.getRankInfo()) {
                                typeName += g.getRankName() + " ";
                            }
                        }
                        d.setTypeName("请选择 " + typeName);
                        mGoodsDetailClassifyPop.setData(d);
                    }
                }
            }

            @Override
            public void onError(Response<NetEntity<GoodsClassifyBean>> response) {
                super.onError(response);
//                if (mGoodsDetailClassifyPop != null) {
//                    GoodsClassifyBean d = JSON.parseObject(guige, GoodsClassifyBean.class);
//                    d.setHeaderImg(data.getImgUrls().size() > 0 ? data.getImgUrls().get(0) : "");
//                    d.setPrice(data.getPrice());
//                    d.setStock(data.getStock());
//                    String typeName = "";
//                    for (GoodsClassifyBean.RankInfoBean g : d.getRankInfo()) {
//                        typeName += g.getRankName();
//                    }
//                    d.setTypeName("请选择" + typeName);
//                    mGoodsDetailClassifyPop.setData(d);
//                }
            }
        });
    }


    private void initData() {

        Map<String, String> map = new HashMap<>();
        if (!TextUtils.isEmpty(App.getLoginUserId())) map.put("id", App.getLoginUserId());
        map.put("goodsId", getIntent().getStringExtra("goodsId"));
        map.put("requestCheck", EncryptUtils.encryptMD5ToString(getIntent().getStringExtra("goodsId"), App.salt)
                .toLowerCase());
        OkGo.<NetEntity<GoodsDetailEntity>>post(Detail).params(map).tag(this).execute(new DialogCallback<NetEntity<GoodsDetailEntity>>(mContext) {
            @Override
            public void onSuccess(Response<NetEntity<GoodsDetailEntity>> response) {
                if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                    mGoodsDetailFragment.setData(response.body().getData());
                    getRank(response.body().getData());

                    Drawable d = null;
                    type = response.body().getData().getIsCollected();
                    if (response.body().getData().getIsCollected().equals("0")) {
                        d = ContextCompat.getDrawable(mContext, R.mipmap.ic_goods_detail_star_gray);
                    } else {
                        d = ContextCompat.getDrawable(mContext, R.mipmap.ic_goods_detail_star_blue);
                    }
                    collect.setCompoundDrawablesRelativeWithIntrinsicBounds(null, d, null, null);
                    addGWC.setBackgroundColor(ContextCompat.getColor(mContext,response.body().getData().getIsDelete().equals("1")?R.color.text_gray:R.color.blue2));
                    addGWC.setClickable(!response.body().getData().getIsDelete().equals("1"));
                }
            }

            @Override
            public void onError(Response<NetEntity<GoodsDetailEntity>> response) {
                super.onError(response);
//                mGoodsDetailFragment.setData(JSON.parseObject(json, GoodsDetailEntity.class));
            }
        });
    }

    private void initView() {
        mGoodsDetailClassifyPop = new GoodsDetailClassifyPop(this);
        List<Fragment> fragments = new ArrayList<Fragment>();
        mGoodsDetailFragment = new GoodsDetailFragment();
        mGoodsEvaluateFragment = new GoodsEvaluateFragment();
        fragments.add(mGoodsDetailFragment);
        fragments.add(mGoodsEvaluateFragment);
        mGoodsDetailFragment.setListener(this);
        mGoodsDetailFragment.setChooseListener(this);
        pagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setScanScroll(false);
        controlViewPagerSpeed();
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == 0) {
                    viewPager.setScanScroll(false);
                }
            }
        });

    }

    private FixedSpeedScroller mScroller = null;

    private void controlViewPagerSpeed() {
        try {
            Field mField;

            mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);

            mScroller = new FixedSpeedScroller(viewPager.getContext(),
                    new AccelerateInterpolator());
            mScroller.setmDuration(200); // 2000ms
            mField.set(viewPager, mScroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*
    *
    *弹窗
    */
    @Override
    public void onChoose() {
        if (!App.checkUserLogin()) {
            Utils.startActivity(mContext, LoginInActivity.class);
            return;
        }
        mGoodsDetailClassifyPop.showPopupWindow();
    }

    /*
    *
    *
    */
    @Override
    public void onChange(int id) {
        viewPager.setScanScroll(true);
        viewPager.setCurrentItem(1);
        mTitle.setText("评价");
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 1) {
            viewPager.setScanScroll(true);
            viewPager.setCurrentItem(0);
            mTitle.setText("商品详情");
            return;
        } else {
            super.onBackPressed();
        }
    }

    /**
     * viewpager适配器
     */
    class MyPagerAdapter extends FragmentStatePagerAdapter {
        List<Fragment> fragments;

        public MyPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        public void setList(List<Fragment> fragments) {
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }


    @OnClick({R.id.mLeftImg, R.id.collect, R.id.gwc, R.id.addGWC})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mLeftImg:
                if (viewPager.getCurrentItem() == 1) {
                    viewPager.setScanScroll(true);
                    viewPager.setCurrentItem(0);
                    mTitle.setText("商品详情");
                    return;
                } else {
                    super.onBackPressed();
                }
                break;
            case R.id.collect:
                if (!App.checkUserLogin()) {
                    Utils.startActivity(mContext, LoginInActivity.class);
                    return;
                }
                type = type.equals("0") ? "1" : "0";
                Map<String, String> map = new HashMap<>();
                map.put("id", App.getLoginUserId());
                map.put("goodsId", getIntent().getStringExtra("goodsId"));
                map.put("type", type);
                map.put("requestCheck", EncryptUtils.encryptMD5ToString(App.getLoginUserId() + getIntent().getStringExtra("goodsId"), App.salt)
                        .toLowerCase());
                OkGo.<NetEntity<String>>post(CollectAction).params(map).tag(this).execute(new DialogCallback<NetEntity<String>>(mContext) {
                    @Override
                    public void onSuccess(Response<NetEntity<String>> response) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            Drawable d = null;
                            if (type.equals("0")) {
                                d = ContextCompat.getDrawable(mContext, R.mipmap.ic_goods_detail_star_gray);
                            } else {
                                d = ContextCompat.getDrawable(mContext, R.mipmap.ic_goods_detail_star_blue);
                            }
                            collect.setCompoundDrawablesRelativeWithIntrinsicBounds(null, d, null, null);

                        } else {
                            Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                });
                break;
            case R.id.gwc:
                if (!App.checkUserLogin()) {
                    Utils.startActivity(mContext, LoginInActivity.class);
                    return;
                }
                Utils.startActivity(mContext, GoodsCarActivity.class);
                break;
            case R.id.addGWC:
//                if (!App.checkUserLogin()) {
//                    Utils.startActivity(mContext, LoginInActivity.class);
//                    return;
//                }
                mGoodsDetailClassifyPop.showPopupWindow();
                break;
        }
    }

    String guige = "{\n" +
            "    \"rankInfo\": [\n" +
            "        {\n" +
            "            \"rankId\": \"1\",\n" +
            "            \"rankName\": \"颜色\",\n" +
            "            \"subRank\": [\n" +
            "                {\n" +
            "                    \"rankId\": \"11\",\n" +
            "                    \"rankName\": \"红色\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"rankId\": \"12\",\n" +
            "                    \"rankName\": \"蓝色\"\n" +
            "                }\n" +
            "            ]\n" +
            "        },\n" +
            "        {\n" +
            "            \"rankId\": \"2\",\n" +
            "            \"rankName\": \"尺寸\",\n" +
            "            \"subRank\": [\n" +
            "                {\n" +
            "                    \"rankId\": \"21\",\n" +
            "                    \"rankName\": \"L\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"rankId\": \"22\",\n" +
            "                    \"rankName\": \"XL\"\n" +
            "                }\n" +
            "            ]\n" +
            "        }\n" +
            "    ],\n" +
            "    \"rankComb\": [\n" +
            "        {\n" +
            "            \"skuid\": \"1\",\n" +
            "            \"rankCombId\": \"11\",\n" +
            "            \"stock\": \"123\",\n" +
            "            \"price\": \"48.8\",\n" +
            "            \"image\": \"https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3269061743,2028437678&fm=27&gp=0.jpg\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"skuid\": \"1\",\n" +
            "            \"rankCombId\": \"12\",\n" +
            "            \"stock\": \"23\",\n" +
            "            \"price\": \"48.8\",\n" +
            "            \"image\": \"https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3269061743,2028437678&fm=27&gp=0.jpg\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"skuid\": \"1\",\n" +
            "            \"rankCombId\": \"11,21\",\n" +
            "            \"stock\": \"154\",\n" +
            "            \"price\": \"48.8\",\n" +
            "            \"image\": \"https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3269061743,2028437678&fm=27&gp=0.jpg\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"skuid\": \"1\",\n" +
            "            \"rankCombId\": \"21\",\n" +
            "            \"stock\": \"21\",\n" +
            "            \"price\": \"48.8\",\n" +
            "            \"image\": \"https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3269061743,2028437678&fm=27&gp=0.jpg\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"skuid\": \"1\",\n" +
            "            \"rankCombId\": \"22\",\n" +
            "            \"stock\": \"0\",\n" +
            "            \"price\": \"48.8\",\n" +
            "            \"image\": \"https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3269061743,2028437678&fm=27&gp=0.jpg\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"skuid\": \"2\",\n" +
            "            \"rankCombId\": \"11,22\",\n" +
            "            \"stock\": \"23\",\n" +
            "            \"price\": \"48.8\",\n" +
            "            \"image\": \"https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2993321848,3135960404&fm=27&gp=0.jpg\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"skuid\": \"3\",\n" +
            "            \"rankCombId\": \"12,21\",\n" +
            "            \"stock\": \"200\",\n" +
            "            \"price\": \"48.8\",\n" +
            "            \"image\": \"https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2993321848,3135960404&fm=27&gp=0.jpg\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"skuid\": \"4\",\n" +
            "            \"rankCombId\": \"12,22\",\n" +
            "            \"stock\": \"212\",\n" +
            "            \"price\": \"48.8\",\n" +
            "            \"image\": \"https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3269061743,2028437678&fm=27&gp=0.jpg\"\n" +
            "        }\n" +
            "    ]\n" +
            "}";
}

