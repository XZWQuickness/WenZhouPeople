package com.exz.wenzhoupeople.activity;

import android.support.annotation.IdRes;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.EncryptUtils;
import com.exz.wenzhoupeople.R;
import com.exz.wenzhoupeople.appclication.App;
import com.exz.wenzhoupeople.config.Urls;
import com.exz.wenzhoupeople.entity.CheckPayEntity;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.lzy.okgo.OkGo;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.szw.lib.myframework.config.Constants;
import cn.com.szw.lib.myframework.utils.DialogUtils;
import cn.com.szw.lib.myframework.utils.net.NetEntity;
import cn.com.szw.lib.myframework.utils.net.callback.DialogCallback;

import static cn.com.szw.lib.myframework.config.Constants.BusAction.Pay_Finish;
import static com.exz.wenzhoupeople.config.Urls.RechargePayState;

/**
 * Created by weicao on 2017/8/29.
 */

public class RechargeActivity extends PayActivityAccount {

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
    @BindView(R.id.money)
    EditText money;
    @BindView(R.id.alipay)
    RadioButton alipay;
    @BindView(R.id.WeChat)
    RadioButton WeChat;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.kind_money)
    LinearLayout kindMoney;

    private int type = 0;

    @Override
    public boolean initToolbar() {

        mTitle.setText("充值");
        mTitle.setTextSize(18);
        toolbar.setContentInsetsAbsolute(0, 0);
        setSupportActionBar(toolbar);
        return false;
    }


    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Pay_Finish)})
    public void PayFinish(String tag) {
        checkPay();
    }

    private void checkPay() {
        Map<String, String> map = new HashMap<>();
        map.put("userId", App.getLoginUserId());
        map.put("rechargeOrderId", rechargeOrderId);
        map.put("requestCheck", EncryptUtils.encryptMD5ToString(App.getLoginUserId() + rechargeOrderId + App.salt).toLowerCase());
        OkGo.<NetEntity<CheckPayEntity>>post(RechargePayState).params(map).tag(this).execute(new DialogCallback<NetEntity<CheckPayEntity>>(mContext) {
            @Override
            public void onSuccess(com.lzy.okgo.model.Response<NetEntity<CheckPayEntity>> response) {
                if (response.body().getCode() == Constants.NetCode.SUCCESS) {
//                    0待支付 1支付成功 2支付失败
                    if (response.body().getData().getPayState().equals("1")) {
//                            Intent intent = new Intent(mContext, PaySuccessActivity.class);
//                            intent.putExtra(Intent_BuyCardRecordId, buyCardRecordId);
//                            intent.putExtra(Intent_BuyCardRecordOrderId, buyCardRecordOrderId);
//                            intent.putExtra(PaySuccessActivity.Intent_Price, responseData.getData().getPayMoney());
//                            intent.putExtra(PaySuccessActivity.Intent_Title, responseData.getData().getCardName());
//                            intent.putExtra(PaySuccessActivity.Intent_Time, responseData.getData().getPaySuccessDate());
//                            startActivity(intent);
                        Toast.makeText(mContext, "支付成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        DialogUtils.Warning(mContext, "服务器验证支付失败，请联系平台");
                    }
                }
            }

        });
    }



    @Override
    public int setInflateId() {
        return R.layout.activity_recharge;
    }

    @Override
    public void init() throws Exception {
        super.init();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.alipay:
                        type = 0;
                        break;

                    case R.id.WeChat:
                        type = 1;

                        break;


                }
            }
        });
    }

    @OnClick({R.id.mLeftImg, R.id.btn_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mLeftImg:
                finish();
                break;
            case R.id.btn_login:

                int i = 0;
                try {
                    i = Integer.parseInt(money.getText().toString());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                if (TextUtils.isEmpty(money.getText())) {

                    Toast.makeText(mContext, "请输入充值金额", Toast.LENGTH_SHORT).show();
                } else if (i<10) {
                    Toast.makeText(mContext, "请输入整数金额，最低10元", Toast.LENGTH_SHORT).show();

                } else {

                    if (type == 0) {
                        AliPay(Urls.AliPay_Account, "rechargeMoney", money.getText().toString());
                    } else {
                        WeChatPay(Urls.WeChatPay_Account, "rechargeMoney", money.getText().toString());
                    }
                }

                break;
        }
    }

}
