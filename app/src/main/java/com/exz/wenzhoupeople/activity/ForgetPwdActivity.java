package com.exz.wenzhoupeople.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
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
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.szw.lib.myframework.base.BaseActivity;
import cn.com.szw.lib.myframework.config.Constants;
import cn.com.szw.lib.myframework.utils.net.NetEntity;
import cn.com.szw.lib.myframework.utils.net.callback.JsonCallback;

import static com.exz.wenzhoupeople.config.Urls.ForgetPwd;
import static com.exz.wenzhoupeople.config.Urls.VerifyCode;

/**
 * Created by pc on 2017/8/21.
 */

public class ForgetPwdActivity extends BaseActivity {
    @BindView(R.id.mTitle)
    TextView mTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
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

    @Override
    public boolean initToolbar() {
        toolbar.setContentInsetsAbsolute(0, 0);
        mTitle.setTextSize(18);
        mTitle.setText("忘记密码");
        setSupportActionBar(toolbar);
        return false;
    }

    @Override
    public int setInflateId() {
        return R.layout.activity_forget_pwd;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
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
                map.put("purpose", "2");//用途：1注册，2忘记密码，3设置支付密码
                map.put("requestCheck", EncryptUtils.encryptMD5ToString(phone, App.salt).toLowerCase());
                OkGo.<NetEntity<User>>post(VerifyCode).params(map).tag(this).execute(new JsonCallback<NetEntity<User>>() {
                    @Override
                    public void onSuccess(Response<NetEntity<User>> response) {

                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            downTimer();
                        }
                        Toast.makeText(ForgetPwdActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Response<NetEntity<User>> response) {
                        super.onError(response);
                        downTimer();
                    }
                });
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

        Map<String, String> map = new HashMap<>();
        map.put("phone", phone);
        map.put("code", code);
        map.put("pwd", password);
        map.put("requestCheck", EncryptUtils.encryptMD5ToString(phone+code,
                App.salt).toLowerCase());
        OkGo.<NetEntity<User>>post(ForgetPwd).params(map).tag(this).execute(new JsonCallback<NetEntity<User>>() {
            @Override
            public void onSuccess(Response<NetEntity<User>> response) {

                if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                    SPutils.save(mContext,"phone",phone);
                    SPutils.save(mContext,"password",password);
                    finish();
                }
                Toast.makeText(ForgetPwdActivity.this, "密码修改成功", Toast.LENGTH_SHORT).show();
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
