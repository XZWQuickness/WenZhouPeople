package com.exz.wenzhoupeople.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.exz.wenzhoupeople.R;
import com.exz.wenzhoupeople.appclication.App;
import com.exz.wenzhoupeople.config.Urls;
import com.exz.wenzhoupeople.entity.User;
import com.exz.wenzhoupeople.utils.SPutils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.szw.lib.myframework.base.BaseActivity;
import cn.com.szw.lib.myframework.config.Constants;
import cn.com.szw.lib.myframework.utils.net.NetEntity;
import cn.com.szw.lib.myframework.utils.net.callback.DialogCallback;
import cn.com.szw.lib.myframework.utils.net.callback.JsonCallback;
import cn.com.szw.lib.myframework.view.MyWebActivity;
import cn.jpush.android.api.JPushInterface;

import static cn.com.szw.lib.myframework.view.MyWebActivity.Intent_Title;
import static cn.com.szw.lib.myframework.view.MyWebActivity.Intent_Url;
import static com.exz.wenzhoupeople.config.Urls.Register;
import static com.exz.wenzhoupeople.config.Urls.VerifyCode;

/**
 * Created by pc on 2017/8/21.
 */

public class RegisterActivity extends BaseActivity {
    @BindView(R.id.mTitle)
    TextView mTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.edNickname)
    EditText edNickname;
    @BindView(R.id.edPhone)
    EditText edPhone;
    @BindView(R.id.edCode)
    EditText edCode;
    @BindView(R.id.code)
    TextView code;
    @BindView(R.id.edPwd1)
    EditText edPwd1;
    @BindView(R.id.edPwd2)
    EditText edPwd2;
    @BindView(R.id.edPhone2)
    EditText edPhone2;
    @BindView(R.id.mLeft)
    TextView mLeft;
    @BindView(R.id.mRight)
    TextView mRight;
    @BindView(R.id.mRightImg)
    ImageView mRightImg;
    @BindView(R.id.mLeftImg)
    ImageView mLeftImg;
    @BindView(R.id.parent_lay)
    RelativeLayout parentLay;
    @BindView(R.id.checkBox)
    CheckBox checkBox;
    @BindView(R.id.fwxy)
    LinearLayout fwxy;
    @BindView(R.id.btnSubmit)
    Button btnSubmit;

    @Override
    public boolean initToolbar() {
        toolbar.setContentInsetsAbsolute(0, 0);
        mTitle.setTextSize(18);
        mTitle.setTextColor(ContextCompat.getColor(this, R.color.white));
        mTitle.setText("注册账号");
        mTitle.setTextColor(ContextCompat.getColor(mContext, R.color.text_gray));
        setSupportActionBar(toolbar);
        return false;
    }

    @Override
    public int setInflateId() {
        return R.layout.activity_register;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.mLeftImg, R.id.code, R.id.fwxy, R.id.btnSubmit})
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
                map.put("purpose", "1");//用途：1注册，2忘记密码，3设置支付密码
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
            case R.id.fwxy:
                Intent intent=new Intent(mContext, MyWebActivity.class);
                intent.putExtra(Intent_Title,"用户协议");
                intent.putExtra(Intent_Url, Urls.url+"mobile/Service_Agreement.aspx");
                startActivity(intent);
                break;
            case R.id.btnSubmit:
                sumbint();

                break;
        }
    }

    private void sumbint() {
        final String phone = edPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(mContext, "请输入手机号", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!RegexUtils.isMobileExact(phone)) {
            Toast.makeText(mContext, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
            return;
        }
        String code = edCode.getText().toString().trim();
        if (TextUtils.isEmpty(code)) {
            Toast.makeText(mContext, "请输入验证码", Toast.LENGTH_SHORT).show();
            return;
        }

        String newPassword = edPwd1.getText().toString().trim();
        if (TextUtils.isEmpty(newPassword)) {
            Toast.makeText(mContext, "请输入您的密码", Toast.LENGTH_SHORT).show();
            return;
        }
        final String password = edPwd2.getText().toString().trim();
        if (!newPassword.equals(password)) {
            Toast.makeText(mContext, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!checkBox.isChecked()) {
            Toast.makeText(mContext, "请同意用户协议", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("phone", phone);
        map.put("code", code);
        map.put("uid", getIntent().getStringExtra("uid"));
        map.put("type", getIntent().getStringExtra("type"));
        map.put("deviceType", "0");
        map.put("pwd", password);
        map.put("jpushToken", JPushInterface.getRegistrationID(mContext));
        map.put("requestCheck", EncryptUtils.encryptMD5ToString(phone + code,
                App.salt).toLowerCase());
        OkGo.<NetEntity<User>>post(Register).params(map).tag(this).execute(new DialogCallback<NetEntity<User>>(mContext) {
            @Override
            public void onSuccess(Response<NetEntity<User>> response) {

                if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                    SPutils.save(mContext, "phone", phone);
                    SPutils.save(mContext, "password", password);
                    finish();
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
            code.setText(millisUntilFinished / 1000 + "s");
        }

    }
}
