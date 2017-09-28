package com.exz.wenzhoupeople.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.EncryptUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.exz.wenzhoupeople.R;
import com.exz.wenzhoupeople.activity.ConfirmOrderActivity;
import com.exz.wenzhoupeople.activity.GoodsDetailActivity;
import com.exz.wenzhoupeople.adapter.GouWuCheAdapter;
import com.exz.wenzhoupeople.appclication.App;
import com.exz.wenzhoupeople.config.Urls;
import com.exz.wenzhoupeople.entity.CouponEntity;
import com.exz.wenzhoupeople.entity.GouWuCheEntity;
import com.exz.wenzhoupeople.entity.OrderSettleEntity;
import com.exz.wenzhoupeople.pop.CouponMore;
import com.exz.wenzhoupeople.utils.DialogUtils;
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
import cn.com.szw.lib.myframework.listener.OnNumListener;
import cn.com.szw.lib.myframework.utils.RecycleViewDivider;
import cn.com.szw.lib.myframework.utils.RxBus;
import cn.com.szw.lib.myframework.utils.net.NetEntity;
import cn.com.szw.lib.myframework.utils.net.callback.DialogCallback;
import cn.com.szw.lib.myframework.utils.net.callback.JsonCallback;

import static com.exz.wenzhoupeople.config.Urls.CouponList;
import static com.exz.wenzhoupeople.config.Urls.CouponReceive;
import static com.exz.wenzhoupeople.config.Urls.DeleteShopCar;
import static com.exz.wenzhoupeople.config.Urls.ModifyGoodsCount;

/**
 * Created by pc on 2017/6/22.
 */

public class GouWuCheFragment extends MyBaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    Unbinder unbinder;
    @BindView(R.id.mTitle)
    TextView mTitle;
    @BindView(R.id.mLeftImg)
    ImageView mLeftImg;
    @BindView(R.id.mRight)
    TextView mRight;
    @BindView(R.id.llFreight)
    LinearLayout llFreight;
    @BindView(R.id.btnSubmit)
    TextView btnSubmit;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;
    @BindView(R.id.all)
    TextView all;
    @BindView(R.id.allPrice)
    TextView allPrice;
    @BindView(R.id.freight)
    TextView freight;
    @BindView(R.id.rlLay)
    RelativeLayout rlLay;
    View empty;
    TextView footer;
    GouWuCheAdapter adapter;
    CouponMore mCouponMore;
    private boolean canBack;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_gouwuche, container, false);
        }
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }


    public static GouWuCheFragment newInstance() {
        return new GouWuCheFragment();
    }
    /**
     * @param canBack 是否可以返回
     */

    public GouWuCheFragment  setLeft(boolean canBack){
        this.canBack=canBack;
        return this;
    }
    public void initToolbar() {
        if (canBack) {
            mLeftImg.setVisibility(View.VISIBLE);
            mLeftImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            });
        }else{
            mLeftImg.setVisibility(View.INVISIBLE);
        }
        toolbar.setContentInsetsAbsolute(0, 0);
        mTitle.setTextSize(18);
        mTitle.setTextColor(ContextCompat.getColor(getContext(), R.color.text_gray));
        mTitle.setText("购物车");
        mRight.setTextSize(18);
        mRight.setTextColor(ContextCompat.getColor(getContext(), R.color.text_gray));
        mRight.setText("编辑");
        onRefresh();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(App.getLoginUserId())) {
            onRefresh();
        }
    }

    @Override
    public void initView() {
        initToolbar();
        mCouponMore = new CouponMore(getActivity());
        adapter = new GouWuCheAdapter(this);
        empty = LayoutInflater.from(getContext()).inflate(R.layout.gouwuche_empty, new RelativeLayout(getContext()), false);
        footer = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.gouwuche_footer, new RelativeLayout(getContext()), false);
        adapter.setEmptyView(empty);
        adapter.addFooterView(footer);
        adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new RecycleViewDivider(getContext(), LinearLayoutManager.VERTICAL,
                1, ContextCompat.getColor(getContext(), R.color.line_bg)));
        refresh.setOnRefreshListener(this);
        recyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(final BaseQuickAdapter adapter, View view, final int position) {
                final GouWuCheEntity.GoodsInfoBean entity = (GouWuCheEntity.GoodsInfoBean) adapter.getData().get(position);
                switch (view.getId()) {
                    case R.id.minus:
                        if (Integer.parseInt(entity.getCount()) > 1) {

                            ModifyGoodsCount(entity, Integer.parseInt(entity.getCount()) - 1, position);
                        }
                        break;
                    case R.id.llChekc:
                        ((GouWuCheEntity.GoodsInfoBean) adapter.getData().get(adapter.getData().indexOf(entity))).setCheck(!entity.isCheck());
                        itemSelect(GouWuCheFragment.this, all);
                        break;
                    case R.id.add:
                        ModifyGoodsCount(entity, Integer.parseInt(entity.getCount()) + 1, position);
                        break;
                    case R.id.count:
                        DialogUtils.ChangeNum(getContext(), Integer.parseInt(entity.getCount()), new OnNumListener() {
                            @Override
                            public void onNum(int i) {
                                ModifyGoodsCount(entity, i, position);
                            }
                        });
                        break;

                }
                adapter.notifyDataSetChanged();
            }
        });
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                final GouWuCheEntity.GoodsInfoBean entity = (GouWuCheEntity.GoodsInfoBean) adapter.getData().get(position);
                Intent intent = new Intent(getActivity(), GoodsDetailActivity.class);
                intent.putExtra("goodsId", entity.getId());
                startActivity(intent);
            }
        });
        adapter.setDeleteOnclick(new GouWuCheAdapter.deleteOnclick() {
            @Override
            public void deleteOnclick(final GouWuCheEntity.GoodsInfoBean item) {
                DialogUtils.showDiaolog(getContext(), "确认要删除商品吗?", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DialogUtils.dialog.dismiss();
                        deleteShopCar(item.getShopCarId());

                    }
                });
            }
        });
        mCouponMore.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                CouponEntity couponEntity = (CouponEntity) adapter.getData().get(position);
                CouponReceive(couponEntity.getId());
            }
        });
        footer.setVisibility(View.GONE);
        rlLay.setVisibility(View.GONE);
        footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CouponList();
            }
        });


    }

    private void CouponList() {
        Map<String, String> map = new HashMap<>();
        map.put("goodsId", "0");
        map.put("requestCheck", EncryptUtils.encryptMD5ToString("0", App.salt)
                .toLowerCase());
        OkGo.<NetEntity<List<CouponEntity>>>post(CouponList).params(map).tag(this).execute(new DialogCallback<NetEntity<List<CouponEntity>>>(getContext()) {
            @Override
            public void onSuccess(Response<NetEntity<List<CouponEntity>>> response) {
                if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                    mCouponMore.setData(response.body().getData());
                    mCouponMore.showPopupWindow();
                } else {
                    Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Response<NetEntity<List<CouponEntity>>> response) {
                super.onError(response);
            }
        });
    }

    /**
     * 领取优惠券
     */
    private void CouponReceive(String id) {
        Map<String, String> map = new HashMap<>();
        map.put("id", App.getLoginUserId());
        map.put("couponId", id);
        map.put("goodsId", "0");
        map.put("requestCheck", EncryptUtils.encryptMD5ToString( App.getLoginUserId()+id, App.salt)
                .toLowerCase());
        OkGo.<NetEntity<String>>post(CouponReceive).params(map).tag(this).execute(new DialogCallback<NetEntity<String>>(getContext()) {
            @Override
            public void onSuccess(Response<NetEntity<String>> response) {
                if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                    mCouponMore.dismiss();
                }
                Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }

    public void deleteShopCar(String shopCarIds) {
        Map<String, String> map = new HashMap<>();
        map.put("id", App.getLoginUserId());
        map.put("shopCarIds", shopCarIds);
        map.put("requestCheck", EncryptUtils.encryptMD5ToString(App.getLoginUserId(),
                App.salt).toLowerCase());
        OkGo.<NetEntity<String>>post(DeleteShopCar).params(map).tag(this).execute(new JsonCallback<NetEntity<String>>() {
            @Override
            public void onSuccess(Response<NetEntity<String>> response) {
                if (response.body().getCode() == Constants.NetCode.SUCCESS) {

                    onRefresh();

                }
            }

        });
    }


    /*
    *  编辑购物车中商品数量
    */
    public void ModifyGoodsCount(final GouWuCheEntity.GoodsInfoBean goodsInfoBean, final int goodsCount, final int position) {
        Map<String, String> map = new HashMap<>();
        map.put("id", App.getLoginUserId());
        map.put("shopCarId", goodsInfoBean.getShopCarId());
        map.put("goodsCount", goodsCount + "");
        map.put("requestCheck", EncryptUtils.encryptMD5ToString(App.getLoginUserId(),
                App.salt).toLowerCase());
        OkGo.<NetEntity<String>>post(ModifyGoodsCount).params(map).tag(this).execute(new JsonCallback<NetEntity<String>>() {
            @Override
            public void onSuccess(Response<NetEntity<String>> response) {
                if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                    adapter.getData().get(position).setCount(goodsCount + "");
                    adapter.notifyItemChanged(position);
                    itemSelect(GouWuCheFragment.this, all);
                } else {
                    Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

        });
    }

    /**
     * 单选，是否全选
     */
    public void itemSelect(GouWuCheFragment context, TextView selectAll) {
        boolean b = false;
        float price = 0;
        for (GouWuCheEntity.GoodsInfoBean entity : context.adapter.getData()) {
            if (!entity.isCheck()) {
                b = true;
            } else {
                price += Float.parseFloat(entity.getPrice()) * Integer.parseInt(entity.getCount());
            }
        }
        allPrice.setText("合计:￥" + price);
        setSelectAll(context.getActivity(), selectAll, !b);
    }

    /**
     * @param b 是否全选
     */
    public static void setSelectAll(Context context, TextView view, boolean b) {
        view.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context, b ? R.mipmap.ic_gwc_true : R.mipmap.ic_gwc_false), null, null, null);
        view.setTag(b);
    }


    private void initData() {
        long timestamp = System.currentTimeMillis();
        Map<String, String> map = new HashMap<>();
        map.put("id", App.getLoginUserId());
        map.put("timestamp", timestamp + "");
        map.put("requestCheck", EncryptUtils.encryptMD5ToString(App.getLoginUserId() + timestamp,
                App.salt).toLowerCase());
        OkGo.<NetEntity<GouWuCheEntity>>post(Urls.ShopCarList).params(map).tag(this).execute(new JsonCallback<NetEntity<GouWuCheEntity>>() {
            @Override
            public void onSuccess(Response<NetEntity<GouWuCheEntity>> response) {
                refresh.setEnabled(true);
                refresh.setRefreshing(false);
                if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                    if (footer != null && response.body().getData().getIsCoupon() != null && response.body().getData().getIsCoupon().equals("1")) {//有优惠券
                        footer.setVisibility(View.VISIBLE);
                    } else {//无优惠券
                        footer.setVisibility(View.GONE);
                    }
                    adapter.setNewData(response.body().getData().getGoodsInfo());
                    adapter.loadMoreEnd();
                    rlLay.setVisibility(adapter.getData().size() < 1 ? View.GONE : View.VISIBLE);
                    mRight.setVisibility(adapter.getData().size() < 1 ? View.GONE : View.VISIBLE);
                    itemSelect(GouWuCheFragment.this, all);
                    int ShopCarNum = 0;
                    for (GouWuCheEntity.GoodsInfoBean g : adapter.getData()) {
                        ShopCarNum += Integer.parseInt(g.getCount());
                    }
                    RxBus.get().post("ShopCarNum", adapter.getData().size() + "");

                }
            }

            @Override
            public void onError(Response<NetEntity<GouWuCheEntity>> response) {
                super.onError(response);
                refresh.setEnabled(true);
                refresh.setRefreshing(false);
            }
        });


    }

    @OnClick({R.id.mLeftImg, R.id.all, R.id.btnSubmit, R.id.mRight})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mLeftImg:
                break;
            case R.id.all:
                Object tag = all.getTag();
                boolean tagB = (boolean) (tag == null ? false : tag);
                for (GouWuCheEntity.GoodsInfoBean goodsEntity : adapter.getData()) {
                    goodsEntity.setCheck(!tagB);
                }
                adapter.notifyDataSetChanged();
                itemSelect(GouWuCheFragment.this, all);
                break;
            case R.id.btnSubmit:
                if (adapter.getClickState().equals("2")) {//删除
                    DialogUtils.showDiaolog(getContext(), "确认要删除商品吗?", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String shopCarIds = "";
                            DialogUtils.dialog.dismiss();
                            List<GouWuCheEntity.GoodsInfoBean> deList = new ArrayList<>();
                            for (GouWuCheEntity.GoodsInfoBean g : GouWuCheFragment.this.adapter.getData()) {
                                if (g.isCheck()) {
                                    deList.add(g);
                                    shopCarIds += g.getShopCarId() + ",";
                                }
                            }
                            if (!TextUtils.isEmpty(shopCarIds) && shopCarIds.length() > 1) {
                                deleteShopCar(shopCarIds.substring(0, shopCarIds.length() - 1));
                            }

                        }
                    });

                } else {//结算
                    List<GouWuCheEntity.GoodsInfoBean> goodsInfo = new ArrayList<>();
                    List<GouWuCheEntity.GoodsInfoBean> goodsList = new ArrayList<>();
                    OrderSettleEntity orderSettleEntity = new OrderSettleEntity();
                    orderSettleEntity.setId(App.getLoginUserId());
                    orderSettleEntity.setRequestCheck(EncryptUtils.encryptMD5ToString(App.getLoginUserId(), App.salt).toLowerCase());
                    orderSettleEntity.setGoodsInfo(goodsInfo);
                    for (GouWuCheEntity.GoodsInfoBean entity : adapter.getData()) {
                        if (entity.isCheck()) {
                            goodsList.add(entity);
                            GouWuCheEntity.GoodsInfoBean g = new GouWuCheEntity.GoodsInfoBean();
                            g.setGoodsId(entity.getId());
                            g.setShopCarId(entity.getShopCarId());
                            g.setGoodsCount(entity.getCount());
                            g.setSkuid(entity.getSkuid());
                            goodsInfo.add(g);
                        }
                    }
                    if (goodsInfo.size() > 0) {
                        Intent intent = new Intent(getContext(), ConfirmOrderActivity.class);
                        Bundle b = new Bundle();
                        intent.putExtra("json", JSON.toJSONString(orderSettleEntity));
                        intent.putExtra("data", JSON.toJSONString(goodsList));
                        startActivity(intent);
                    }

                }
                break;
            case R.id.mRight:
                String rightText = mRight.getText().toString().trim();
                if (rightText.equals("编辑")) {
                    mRight.setText("完成");
                    btnSubmit.setText("删除");
                    adapter.setClickState("2");
                    llFreight.setVisibility(View.GONE);
                } else {
                    mRight.setText("编辑");
                    btnSubmit.setText("结算");
                    adapter.setClickState("1");
                    llFreight.setVisibility(View.VISIBLE);
                }
                adapter.notifyDataSetChanged();
                break;
        }
    }


    @Override
    public void onRefresh() {
        initData();
    }

}
