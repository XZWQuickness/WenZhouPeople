package com.exz.wenzhoupeople.activity;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.exz.wenzhoupeople.R;
import com.exz.wenzhoupeople.activity.pay.PaymentActivity;
import com.exz.wenzhoupeople.adapter.ConfirmOrderAdapter;
import com.exz.wenzhoupeople.appclication.App;
import com.exz.wenzhoupeople.entity.GouWuCheEntity;
import com.exz.wenzhoupeople.entity.OrderSettleEntity;
import com.exz.wenzhoupeople.entity.SettleInfoEntity;
import com.exz.wenzhoupeople.entity.TakeGoodsAddressEntity;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.szw.lib.myframework.base.BaseActivity;
import cn.com.szw.lib.myframework.config.Constants;
import cn.com.szw.lib.myframework.utils.RecycleViewDivider;
import cn.com.szw.lib.myframework.utils.net.NetEntity;
import cn.com.szw.lib.myframework.utils.net.callback.DialogCallback;
import cn.com.szw.lib.myframework.utils.net.callback.JsonCallback;
import cn.com.szw.lib.myframework.view.CustomLoadMoreView;

import static com.exz.wenzhoupeople.config.Urls.Settle;
import static com.exz.wenzhoupeople.config.Urls.SettleInfo;

/**
 * Created by pc on 2017/8/29.
 * 确认订单
 */

public class ConfirmOrderActivity extends BaseActivity implements View.OnClickListener {
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
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.priceLay)
    ConstraintLayout priceLay;
    @BindView(R.id.buyNow)
    TextView buyNow;
    ConfirmOrderAdapter adapter;
    private HeaderHolder headerHolder;
    private FooterHolder footerHolder;
    public final static int RESULTCODE_COUPON = 1002, RESULTCODE_ADDRESS = 1003;
    TakeGoodsAddressEntity entityAddress;
    String coupon = "";
    String addressId = "";
    String couponId = "";
    float postMoney;
    float couponMoney;
    float total;
    String msg = "";
    float goodsPrice;
    int goodsCount;

    @Override
    public boolean initToolbar() {
        toolbar.setContentInsetsAbsolute(0, 0);
        mTitle.setTextSize(18);
        mTitle.setText("确认订单");
        mLeftImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setSupportActionBar(toolbar);
        return false;
    }

    @Override
    public int setInflateId() {
        return R.layout.activity_confim_order;
    }

    @Override
    public void init() {
        View headerView = View.inflate(mContext, R.layout.header_billing, null);
        View footerView = View.inflate(mContext, R.layout.footer_billing, null);
        headerHolder = new HeaderHolder(this, headerView);
        footerHolder = new FooterHolder(this, footerView);
        headerHolder.btAddress.setOnClickListener(this);
        footerHolder.btCoupon.setOnClickListener(this);
        buyNow.setOnClickListener(this);
        adapter = new ConfirmOrderAdapter();
        adapter.addHeaderView(headerView);
        adapter.addFooterView(footerView);
        adapter.bindToRecyclerView(recyclerView);
        adapter.setNewData(JSON.parseArray(getIntent().getStringExtra("data"), GouWuCheEntity.GoodsInfoBean.class));
        adapter.setEmptyView(LayoutInflater.from(this).inflate(R.layout.view_empty, new RelativeLayout(this), false));
        adapter.setHeaderFooterEmpty(true, true);
        adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        adapter.setLoadMoreView(new CustomLoadMoreView());
        recyclerView.setClipToPadding(false);
        recyclerView.setFocusable(false);
        recyclerView.requestFocus();
        recyclerView.setPadding(SizeUtils.dp2px(0), SizeUtils.dp2px(10), SizeUtils.dp2px(0), SizeUtils.dp2px(0));
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.VERTICAL,
                10, ContextCompat.getColor(mContext, R.color.line_bg)));
        refresh.setEnabled(false);

        for (GouWuCheEntity.GoodsInfoBean goods : adapter.getData()) {
            goodsCount = Integer.parseInt(goods.getCount());
            goodsPrice += Float.parseFloat(goods.getPrice()) * goodsCount;
        }
        footerHolder.price.setText("￥" + goodsPrice);
        price.setText("￥" + goodsPrice);
        footerHolder.goodsCount.setText(goodsCount + "");
        //============商品价格-优惠券+运费===================
        SettleInfo();
    }

    private void SettleInfo() {

        Map<String, String> map = new HashMap<>();
        map.put("info", getIntent().getStringExtra("json"));
        OkGo.<NetEntity<SettleInfoEntity>>post(SettleInfo).params(map).tag(this).execute(new JsonCallback<NetEntity<SettleInfoEntity>>() {
            @Override
            public void onSuccess(Response<NetEntity<SettleInfoEntity>> response) {
                if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                    entityAddress = response.body().getData().getAddress();
                    coupon = JSON.toJSONString(response.body().getData().getCoupon());
                    footerHolder.postMoney.setText(response.body().getData().getPostMoney().equals("0")?"免邮":"￥"+response.body().getData().getPostMoney());//运费
                    postMoney = Float.parseFloat(response.body().getData().getPostMoney());
                    //===========商品金额-优惠券+运费=支付金额=================================
                    total = goodsPrice - couponMoney + postMoney;
                    price.setText(total>0?"￥ "+total:"￥ 0");//总计
                    initAddress(entityAddress);
                } else {

                }

            }

        });
    }


    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.bt_Address://选择收货地址
                intent = new Intent(mContext, TakeGoodsAddressActivity.class);
                intent.putExtra("className", "选择收货地址");
                if (entityAddress != null) intent.putExtra("id", entityAddress.getId());
                startActivityForResult(intent, RESULTCODE_ADDRESS);
                break;
            case R.id.bt_coupon://领取优惠券
                intent = new Intent(mContext, GetCouponActivity.class);
                intent.putExtra("json", coupon);
                startActivityForResult(intent, RESULTCODE_COUPON);
                break;
            case R.id.buyNow:
                if (TextUtils.isEmpty(addressId)) {
                    Toast.makeText(mContext, "请选择您的收货地址", Toast.LENGTH_SHORT).show();
                    return;
                }
                msg = footerHolder.msg.getText().toString().trim();
                OrderSettleEntity orderSettleEntity = new OrderSettleEntity();
                orderSettleEntity.setId(App.getLoginUserId());
                orderSettleEntity.setRequestCheck(EncryptUtils.encryptMD5ToString(App.getLoginUserId(), App.salt).toLowerCase());
                orderSettleEntity.setGoodsInfo(adapter.getData());
                orderSettleEntity.setAddressId(addressId);
                orderSettleEntity.setCouponId(couponId);
                orderSettleEntity.setPostMoney(postMoney + "");
                orderSettleEntity.setTotal(total + "");
                try {
                    String content=URLDecoder.decode(msg,"UTF-8");
                    orderSettleEntity.setMsg(content);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }


                Map<String, String> map = new HashMap<>();
                map.put("info", JSON.toJSONString(orderSettleEntity));
                OkGo.<NetEntity<String>>post(Settle).params(map).tag(this).execute(new DialogCallback<NetEntity<String>>(mContext) {
                    @Override
                    public void onSuccess(Response<NetEntity<String>> response) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            Intent intent = new Intent(mContext, PaymentActivity.class);//结算页面
                            intent.putExtra("orderId", response.body().getData());
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }

                });
                break;
        }
    }


    static class HeaderHolder {
        @BindView(R.id.consigneeText)
        TextView consigneeText;
        @BindView(R.id.consignee)
        TextView consignee;
        @BindView(R.id.address)
        TextView address;
        @BindView(R.id.right)
        ImageView right;
        @BindView(R.id.bt_Address)
        ConstraintLayout btAddress;
        private Context context;

        HeaderHolder(Context context, View view) {
            this.context = context;
            ButterKnife.bind(this, view);
        }
    }

    static class FooterHolder {
        @BindView(R.id.bt_coupon)
        LinearLayout btCoupon;
        @BindView(R.id.postMoney)
        TextView postMoney;
        @BindView(R.id.msg)
        EditText msg;
        @BindView(R.id.goodsCount)
        TextView goodsCount;
        @BindView(R.id.left)
        TextView left;
        @BindView(R.id.coupon)
        TextView coupon;
        @BindView(R.id.price)
        TextView price;
        private Context context;

        FooterHolder(Context context, View view) {
            this.context = context;
            ButterKnife.bind(this, view);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULTCODE_COUPON://领取优惠券
                if (data != null) {
                    couponMoney = Float.parseFloat(data.getStringExtra("money"));
                    footerHolder.coupon.setText("￥"+couponMoney);
                    couponId = data.getStringExtra("id");
                    //===========商品金额-优惠券+运费=支付金额=================================
                    total = goodsPrice - couponMoney + postMoney;
                    price.setText(total>0?"￥ "+total:"￥ 0");//总计
                    couponId = data.getStringExtra("id");//优惠券id
                }
                break;
            case RESULTCODE_ADDRESS://收货地址
                if (data != null && data.getSerializableExtra("bean") != null) {
                    entityAddress = (TakeGoodsAddressEntity) data.getSerializableExtra("bean");
                    initAddress(entityAddress);
                }
                break;
        }

    }

    private void initAddress(TakeGoodsAddressEntity entityAddress) {
        headerHolder.consignee.setText(entityAddress.getName());//收货人
        headerHolder.address.setText(entityAddress.getArea() + entityAddress.getDetail());//收货地址
        addressId = entityAddress.getId();
    }
}
