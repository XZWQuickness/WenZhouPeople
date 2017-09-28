package com.exz.wenzhoupeople.activity;

import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.exz.wenzhoupeople.R;
import com.exz.wenzhoupeople.appclication.App;
import com.exz.wenzhoupeople.entity.User;
import com.exz.wenzhoupeople.utils.SPutils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.szw.lib.myframework.base.BaseActivity;
import cn.com.szw.lib.myframework.config.Constants;
import cn.com.szw.lib.myframework.utils.net.NetEntity;
import cn.com.szw.lib.myframework.utils.net.callback.DialogCallback;
import cn.com.szw.lib.myframework.utils.net.callback.JsonCallback;
import cn.jpush.android.api.JPushInterface;

import static com.exz.wenzhoupeople.config.Urls.BindPhone;
import static com.exz.wenzhoupeople.config.Urls.VerifyCode;

/**
 * Created by weicao on 2017/9/7.
 */

public class BindAccountActivity extends BaseActivity {
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
    @BindView(R.id.edPhone)
    EditText edPhone;
    @BindView(R.id.edCode)
    EditText edCode;
    @BindView(R.id.code)
    TextView code;
    @BindView(R.id.btnSubmit)
    Button btnSubmit;

    private String uid;
    private String type;


    @Override
    public boolean initToolbar() {
        mTitle.setText("绑定账号");
        mTitle.setTextSize(18);
        toolbar.setContentInsetsAbsolute(0, 0);
        setSupportActionBar(toolbar);
        return false;
    }

    @Override
    public int setInflateId() {
        return R.layout.action_bind_account;
    }

    @Override
    public void init() throws Exception {
        super.init();

        uid = getIntent().getStringExtra("uid");
        type = getIntent().getStringExtra("type");

    }

    @OnClick({R.id.mLeftImg, R.id.code, R.id.btnSubmit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mLeftImg:
                finish();
                break;
            case R.id.code:
                String phone = edPhone.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(mContext, "请输入手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!RegexUtils.isMobileExact(phone)) {
                    Toast.makeText(mContext, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String, String> map = new HashMap<>();
                map.put("phone", phone);
                map.put("purpose", "4");//用途：1注册，2忘记密码，3设置支付密码4.绑定手机号
                map.put("requestCheck", EncryptUtils.encryptMD5ToString(phone, App.salt).toLowerCase());
                OkGo.<NetEntity<User>>post(VerifyCode).params(map).tag(this).execute(new JsonCallback<NetEntity<User>>() {
                    @Override
                    public void onSuccess(Response<NetEntity<User>> response) {

                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            downTimer();
                        }
                        Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Response<NetEntity<User>> response) {
                        super.onError(response);
                        downTimer();
                    }
                });
                break;
            case R.id.btnSubmit:

                phoneLogin();
                break;
        }
    }


    private void phoneLogin() {
        final String phone = edPhone.getText().toString().trim();
        final String code = edCode.getText().toString().trim();

        Map<String, String> map = new HashMap<>();
        map.put("uid", uid);
        map.put("type", type);
        map.put("phone", phone);
        map.put("code", code);
        map.put("deviceType", "0");
        map.put("jpushToken", JPushInterface.getRegistrationID(mContext));
        map.put("requestCheck", EncryptUtils.encryptMD5ToString(uid, App.salt)
                .toLowerCase());
        OkGo.<NetEntity<User>>post(BindPhone).params(map).tag(this).execute(new DialogCallback<NetEntity<User>>(mContext) {
            @Override
            public void onSuccess(Response<NetEntity<User>> response) {
                if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                    SPutils.save(mContext, "phone", phone);
                    SPutils.save(mContext, "UserId", response.body().getData().getUserId());
                    App.setUserInfo(response.body().getData());
                    setResult(10);
                    finish();

                } else {
                    Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    CountDownTimer countDownTimer;

    private void downTimer() {
        countDownTimer = new CountDownTimer(120000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                resetTimer(false, millisUntilFinished);
            }

            @Override
            public void onFinish() {
                resetTimer(true, Long.MIN_VALUE);
            }
        };
        countDownTimer.start();
    }

    private void resetTimer(boolean b, long millisUntilFinished) {
        if (b) {
            countDownTimer.cancel();
            code.setText("获取验证码");
            code.setClickable(true);
            code.setTextColor(ContextCompat.getColor(this, R.color.white));
            code.setBackgroundResource(R.drawable.shape_login_blue_all);
            code.setPadding(SizeUtils.dp2px(10), SizeUtils.dp2px(10), SizeUtils.dp2px(10), SizeUtils.dp2px(10));
        } else {
            code.setClickable(false);
            code.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_register_code_gray));
            code.setTextColor(ContextCompat.getColor(this, R.color.white));
            code.setText(String.format("%s", millisUntilFinished / 1000));
        }

    }
}
