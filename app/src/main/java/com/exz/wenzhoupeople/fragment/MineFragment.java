package com.exz.wenzhoupeople.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.EncryptUtils;
import com.exz.wenzhoupeople.R;
import com.exz.wenzhoupeople.activity.DiscountCouponActivity;
import com.exz.wenzhoupeople.activity.EnshrineActivity;
import com.exz.wenzhoupeople.activity.EvaluateActivity;
import com.exz.wenzhoupeople.activity.FootPrintActivity;
import com.exz.wenzhoupeople.activity.LoginActivity;
import com.exz.wenzhoupeople.activity.MsgListActivity;
import com.exz.wenzhoupeople.activity.MyAccountActivity;
import com.exz.wenzhoupeople.activity.MyInfoActivity;
import com.exz.wenzhoupeople.activity.OrderActivity;
import com.exz.wenzhoupeople.activity.SettingsActivity;
import com.exz.wenzhoupeople.activity.TakeGoodsAddressActivity;
import com.exz.wenzhoupeople.activity.WriteAdviceActivity;
import com.exz.wenzhoupeople.appclication.App;
import com.exz.wenzhoupeople.entity.MyInfoBean;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.com.szw.lib.myframework.base.MyBaseFragment;
import cn.com.szw.lib.myframework.config.Constants;
import cn.com.szw.lib.myframework.utils.DateUtils;
import cn.com.szw.lib.myframework.utils.Utils;
import cn.com.szw.lib.myframework.utils.net.NetEntity;
import cn.com.szw.lib.myframework.utils.net.callback.DialogCallback;

import static com.exz.wenzhoupeople.config.Urls.Balance;
import static com.exz.wenzhoupeople.config.Urls.CouponCount;
import static com.exz.wenzhoupeople.config.Urls.UserInfo;

/**
 * Created by pc on 2017/6/22.
 * 个人中心
 */

public class MineFragment extends MyBaseFragment {
    Unbinder unbinder;
    @BindView(R.id.icon_head)
    SimpleDraweeView iconHead;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.settings)
    TextView settings;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.collect)
    TextView collect;
    @BindView(R.id.evaluate)
    TextView evaluate;
    @BindView(R.id.footprint)
    TextView footprint;
    @BindView(R.id.advice)
    TextView advice;
    @BindView(R.id.account)
    LinearLayout account;
    @BindView(R.id.discount_coupon)
    LinearLayout discountCoupon;
    @BindView(R.id.textView3)
    TextView textView3;
    @BindView(R.id.llOrder)
    LinearLayout llOrder;
    @BindView(R.id.daifukuan)
    TextView daifukuan;
    @BindView(R.id.daifuhuo)
    TextView daifuhuo;
    @BindView(R.id.daishouhuo)
    TextView daishouhuo;
    @BindView(R.id.daipingjia)
    TextView daipingjia;
    @BindView(R.id.daituikuan)
    TextView daituikuan;
    @BindView(R.id.account_money)
    TextView accountMoney;
    @BindView(R.id.discount_coupon_count)
    TextView discountCouponCount;

    private String headUrl;
    private String userName;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_mine, container, false);
        }
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;

    }

    @Override
    public void initView() {

        initPort();
        initSum();
        initAcoount();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!App.getLoginUserId().equals("")){
            initPort();
            initSum();
            initAcoount();
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.icon_head, R.id.settings,R.id.msg, R.id.address, R.id.account, R.id.evaluate, R.id.discount_coupon, R.id.footprint, R.id.collect, R.id.advice, R.id.llOrder, R.id.daifukuan, R.id.daifuhuo, R.id.daishouhuo, R.id.daipingjia, R.id.daituikuan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.icon_head:

                Intent intent1 = new Intent(getActivity(), MyInfoActivity.class);
                intent1.putExtra("url", headUrl);
                intent1.putExtra("name", userName);
                startActivity(intent1);
                break;
            case R.id.msg:
                if(!App.checkUserLogin()){
                    Utils.startActivity(getContext(), LoginActivity.class);
                    return;
                }
                Utils.startActivity(getContext(), MsgListActivity.class);
                break;
            case R.id.settings:
                Utils.startActivity(getActivity(), SettingsActivity.class);
                break;
            case R.id.address:
                Intent intent = new Intent(getContext(), TakeGoodsAddressActivity.class);
                intent.putExtra("className", "管理收货地址");
                startActivity(intent);
                break;
            case R.id.account:
                Utils.startActivity(getActivity(), MyAccountActivity.class);
                break;
            case R.id.evaluate:
                Utils.startActivity(getActivity(), EvaluateActivity.class);
                break;
            case R.id.discount_coupon:
                Utils.startActivity(getActivity(), DiscountCouponActivity.class);
                break;
            case R.id.footprint:
                Utils.startActivity(getActivity(), FootPrintActivity.class);
                break;
            case R.id.collect:
                Utils.startActivity(getActivity(), EnshrineActivity.class);
                break;
            case R.id.advice:
                Utils.startActivity(getActivity(), WriteAdviceActivity.class);

                break;
            case R.id.llOrder://全部订单
                intent=new Intent(getContext(),OrderActivity.class);
                intent.putExtra(OrderActivity.Companion.getIntent_Order_Type(),"0");
                startActivity(intent);
                break;
            case R.id.daifukuan://代付款
                intent=new Intent(getContext(),OrderActivity.class);
                intent.putExtra(OrderActivity.Companion.getIntent_Order_Type(),"1");
                startActivity(intent);
                break;
            case R.id.daifuhuo://代发货
                intent=new Intent(getContext(),OrderActivity.class);
                intent.putExtra(OrderActivity.Companion.getIntent_Order_Type(),"2");
                startActivity(intent);
                break;
            case R.id.daishouhuo://待收货
                intent=new Intent(getContext(),OrderActivity.class);
                intent.putExtra(OrderActivity.Companion.getIntent_Order_Type(),"3");
                startActivity(intent);
                break;
            case R.id.daipingjia://待评价
                intent=new Intent(getContext(),OrderActivity.class);
                intent.putExtra(OrderActivity.Companion.getIntent_Order_Type(),"4");
                startActivity(intent);
                break;
        }
    }


    private void initPort() {

        Map<String, String> map = new HashMap<>();
        map.put("id", App.getLoginUserId());
        map.put("timestamp", String.valueOf(DateUtils.dateToUnixTimestamp()));
        map.put("requestCheck", EncryptUtils.encryptMD5ToString(App.getLoginUserId() + String.valueOf(DateUtils.dateToUnixTimestamp()), App.salt)
                .toLowerCase());
        OkGo.<NetEntity<MyInfoBean>>post(UserInfo).params(map).tag(this).execute(new DialogCallback<NetEntity<MyInfoBean>>(getContext()) {
            @Override
            public void onSuccess(Response<NetEntity<MyInfoBean>> response) {
                if (response.body().getCode() == Constants.NetCode.SUCCESS) {

                    userName = response.body().getData().getName();
                    headUrl = response.body().getData().getHeaderUrl();
                    iconHead.setImageURI(response.body().getData().getHeaderUrl());
                    name.setText(response.body().getData().getName());
                    phone.setText(response.body().getData().getPhone());

                }
            }

        });
    }

    //账户余额
    private void initAcoount() {
        Map<String, String> map = new HashMap<>();
        map.put("id", App.getLoginUserId());
        map.put("timestamp", String.valueOf(DateUtils.dateToUnixTimestamp()));
        map.put("requestCheck", EncryptUtils.encryptMD5ToString(App.getLoginUserId() + String.valueOf(DateUtils.dateToUnixTimestamp()), App.salt)
                .toLowerCase());
        OkGo.<NetEntity<String>>post(Balance).params(map).tag(this).execute(new DialogCallback<NetEntity<String>>(getContext()) {
            @Override
            public void onSuccess(Response<NetEntity<String>> response) {
                if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                    accountMoney.setText(String.format("%s元", response.body().getData()));

                }
            }

        });
    }

    //优惠券数量
    private void initSum() {
        Map<String, String> map = new HashMap<>();
        map.put("id", App.getLoginUserId());
        map.put("timestamp", String.valueOf(DateUtils.dateToUnixTimestamp()));
        map.put("state", "0");
        map.put("requestCheck", EncryptUtils.encryptMD5ToString(App.getLoginUserId() + String.valueOf(DateUtils.dateToUnixTimestamp()), App.salt)
                .toLowerCase());
        OkGo.<NetEntity<String>>post(CouponCount).params(map).tag(this).execute(new DialogCallback<NetEntity<String>>(getContext()) {
            @Override
            public void onSuccess(Response<NetEntity<String>> response) {
                if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                    discountCouponCount.setText(response.body().getData());
                }
            }

        });
    }
}
