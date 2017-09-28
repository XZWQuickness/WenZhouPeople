package com.exz.wenzhoupeople.activity;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.EncryptUtils;
import com.exz.wenzhoupeople.R;
import com.exz.wenzhoupeople.appclication.App;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.szw.lib.myframework.base.BaseActivity;
import cn.com.szw.lib.myframework.config.Constants;
import cn.com.szw.lib.myframework.utils.DateUtils;
import cn.com.szw.lib.myframework.utils.Utils;
import cn.com.szw.lib.myframework.utils.net.NetEntity;
import cn.com.szw.lib.myframework.utils.net.callback.DialogCallback;

import static com.exz.wenzhoupeople.config.Urls.Balance;


/**
 * Created by weicao on 2017/1/13.
 * 账户余额
 */
public class MyAccountActivity extends BaseActivity {


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
    @BindView(R.id.wallet)
    ImageView wallet;
    @BindView(R.id.wallet_money)
    TextView walletMoney;
    @BindView(R.id.recharge_money)
    TextView rechargeMoney;
    @BindView(R.id.relativeLayout)
    RelativeLayout relativeLayout;
    @BindView(R.id.apply_deposit)
    TextView applyDeposit;
    @BindView(R.id.next1)
    ImageView next1;
    @BindView(R.id.item2)
    RelativeLayout item2;
    @BindView(R.id.biandong)
    TextView biandong;
    @BindView(R.id.item)
    RelativeLayout item;

    @Override
    public boolean initToolbar() {

        mTitle.setText("我的余额");
        mTitle.setTextSize(18);
        toolbar.setContentInsetsAbsolute(0, 0);
        setSupportActionBar(toolbar);
        return false;
    }

    @Override
    public int setInflateId() {
        return R.layout.activity_account_balance;
    }

    @Override
    public void init() throws Exception {
        super.init();
        initAccount();
    }


    @Override
    public void onResume() {
        super.onResume();
        initAccount();
    }


    @OnClick({R.id.mLeftImg, R.id.item2, R.id.recharge_money, R.id.item})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mLeftImg:
                finish();
                break;
            case R.id.item2:
                Utils.startActivity(mContext, DepositActivity.class);
                break;
            case R.id.recharge_money:
                Utils.startActivity(mContext, RechargeActivity.class);
                break;
            case R.id.item:
                Utils.startActivity(mContext, BalanceChangesActivity.class);
                break;


        }
    }


    //    //账户余额
    private void initAccount() {
        Map<String, String> map = new HashMap<>();
        map.put("id", App.getLoginUserId());
        map.put("timestamp", String.valueOf(DateUtils.dateToUnixTimestamp()));
        map.put("requestCheck", EncryptUtils.encryptMD5ToString(App.getLoginUserId() + String.valueOf(DateUtils.dateToUnixTimestamp()), App.salt)
                .toLowerCase());
        OkGo.<NetEntity<String>>post(Balance).params(map).tag(this).execute(new DialogCallback<NetEntity<String>>(mContext) {
            @Override
            public void onSuccess(Response<NetEntity<String>> response) {
                if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                    walletMoney.setText(String.format("¥ %s", response.body().getData()));

                }
            }

        });
    }
}
