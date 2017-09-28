package com.exz.wenzhoupeople.activity;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import cn.com.szw.lib.myframework.utils.net.NetEntity;
import cn.com.szw.lib.myframework.utils.net.callback.DialogCallback;
import cn.com.szw.lib.myframework.view.ClearWriteEditText;

import static com.exz.wenzhoupeople.config.Urls.ApplyCrash;
import static com.exz.wenzhoupeople.config.Urls.WithdrawLimit;

/**
 * Created by weicao on 2017/8/29.
 */

public class DepositActivity extends BaseActivity {
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
    @BindView(R.id.commit)
    Button commit;
    @BindView(R.id.money)
    TextView money;
    @BindView(R.id.money_sum)
    ClearWriteEditText moneySum;
    @BindView(R.id.bank_card_num)
    ClearWriteEditText bankCardNum;
    @BindView(R.id.open_bank_name)
    ClearWriteEditText openBankName;
    @BindView(R.id.bank_add)
    ClearWriteEditText bankAdd;
    @BindView(R.id.hand_people_name)
    ClearWriteEditText handPeopleName;
    @BindView(R.id.people_phone)
    ClearWriteEditText peoplePhone;

    @Override
    public boolean initToolbar() {
        mTitle.setText("申请提现");
        mTitle.setTextSize(18);
        toolbar.setContentInsetsAbsolute(0, 0);
        setSupportActionBar(toolbar);
        return false;
    }

    @Override
    public int setInflateId() {
        return R.layout.activity_deposit;
    }

    @Override
    public void init() throws Exception {
        super.init();
        initAccount();
    }

    @OnClick({R.id.mLeftImg, R.id.commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mLeftImg:
                finish();
                break;
            case R.id.commit:
                initPort();
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
        OkGo.<NetEntity<String>>post(WithdrawLimit).params(map).tag(this).execute(new DialogCallback<NetEntity<String>>(mContext) {
            @Override
            public void onSuccess(Response<NetEntity<String>> response) {
                if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                    money.setText(String.format("¥ %s", response.body().getData()));
                }
            }

        });
    }

    //    //账户余额
    private void initPort() {

        if (moneySum.getText().toString().equals("")) {
            moneySum.setShakeAnimation();
            Toast.makeText(mContext, "请输入提现金额", Toast.LENGTH_SHORT).show();
        } else if (bankCardNum.getText().toString().equals("")) {
            Toast.makeText(mContext, "请输入银行卡号", Toast.LENGTH_SHORT).show();
            bankCardNum.setShakeAnimation();
        } else if (openBankName.getText().toString().equals("")) {
            Toast.makeText(mContext, "请输入开户行名称", Toast.LENGTH_SHORT).show();
            openBankName.setShakeAnimation();
//        } else if (bankAdd.getText().toString().equals("")) {
//            Toast.makeText(mContext, "请输入开户行地址", Toast.LENGTH_SHORT).show();

        } else if (handPeopleName.getText().toString().equals("")) {
            Toast.makeText(mContext, "请输入持卡人姓名", Toast.LENGTH_SHORT).show();
            handPeopleName.setShakeAnimation();
        } else if (peoplePhone.getText().toString().equals("")) {
            Toast.makeText(mContext, "请输入持卡人手机号", Toast.LENGTH_SHORT).show();
            peoplePhone.setShakeAnimation();
        }else{

        Map<String, String> map = new HashMap<>();
        map.put("id", App.getLoginUserId());
        map.put("timestamp", String.valueOf(DateUtils.dateToUnixTimestamp()));
        map.put("money", moneySum.getText().toString());
        map.put("cardNum", bankCardNum.getText().toString());
        map.put("bankName", openBankName.getText().toString());
        map.put("userName", handPeopleName.getText().toString());
        map.put("userPhone", peoplePhone.getText().toString());
        map.put("requestCheck", EncryptUtils.encryptMD5ToString(App.getLoginUserId() + String.valueOf(DateUtils.dateToUnixTimestamp()), App.salt)
                .toLowerCase());
        OkGo.<NetEntity<Void>>post(ApplyCrash).params(map).tag(this).execute(new DialogCallback<NetEntity<Void>>(mContext) {
            @Override
            public void onSuccess(Response<NetEntity<Void>> response) {
                if (response.body().getCode() == Constants.NetCode.SUCCESS) {


                    finish();
                }
                Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }
    }


}
