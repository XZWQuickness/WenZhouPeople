package com.exz.wenzhoupeople.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.EncryptUtils;
import com.exz.wenzhoupeople.R;
import com.exz.wenzhoupeople.appclication.App;
import com.exz.wenzhoupeople.config.Urls;
import com.exz.wenzhoupeople.entity.User;
import com.exz.wenzhoupeople.utils.MaterialDialogUtils;
import com.exz.wenzhoupeople.utils.SPutils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.szw.lib.myframework.base.BaseActivity;
import cn.com.szw.lib.myframework.config.Constants;
import cn.com.szw.lib.myframework.utils.Utils;
import cn.com.szw.lib.myframework.utils.net.NetEntity;
import cn.com.szw.lib.myframework.utils.net.callback.DialogCallback;
import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

import static com.exz.wenzhoupeople.R.id.qq;
import static com.exz.wenzhoupeople.config.Urls.Login;
import static com.exz.wenzhoupeople.config.Urls.ThirdLogin;

/**
 * Created by pc on 2017/8/21.
 */

public class LoginInActivity extends BaseActivity implements android.os.Handler.Callback, PlatformActionListener {
    @BindView(R.id.mTitle)
    TextView mTitle;
    @BindView(R.id.mRight)
    TextView mRight;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.edPhone)
    EditText edPhone;
    @BindView(R.id.edPassword)
    EditText edPassword;
    String type = "0";

    Platform mPlatform;

    @Override
    public boolean initToolbar() {
        toolbar.setContentInsetsAbsolute(0, 0);
        mRight.setTextSize(18);
        mRight.setTextColor(ContextCompat.getColor(this, R.color.white));
        mRight.setText("注册");
        mRight.setTextColor(ContextCompat.getColor(mContext, R.color.text_gray));
        setSupportActionBar(toolbar);
        return false;
    }

    @Override
    public int setInflateId() {
        return R.layout.activity_login;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        handler = new Handler(this);


        if (!TextUtils.isEmpty(SPutils.getString(mContext, "phone"))) {
            edPhone.setText(SPutils.getString(mContext, "phone"));
        }
        if (!TextUtils.isEmpty(SPutils.getString(mContext, "password"))) {
            edPassword.setText(SPutils.getString(mContext, "password"));
        }
    }


    @OnClick({R.id.mLeftImg, R.id.mRight, R.id.btLogin, R.id.tvForgetPwd, R.id.wechat, qq})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mLeftImg:
                finish();
                break;
            case R.id.mRight://注册
                Utils.startActivity(mContext, RegisterActivity.class);
                break;
            case R.id.btLogin:

                final String phone = edPhone.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(mContext, "请输入手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (phone.length() != 11) {
                    Toast.makeText(mContext, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                final String password = edPassword.getText().toString().trim();
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(mContext, "请输入登录密码", Toast.LENGTH_SHORT).show();
                    return;
                }

                Map<String, String> map = new HashMap<>();
                map.put("phone", phone);
                map.put("pwd", password);
                map.put("deviceType", "0");
                map.put("jpushToken", JPushInterface.getRegistrationID(mContext));
                map.put("requestCheck", EncryptUtils.encryptMD5ToString(phone + password, App.salt)
                        .toLowerCase());
                OkGo.<NetEntity<User>>post(Login).params(map).tag(this).execute(new DialogCallback<NetEntity<User>>(mContext) {
                    @Override
                    public void onSuccess(Response<NetEntity<User>> response) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            SPutils.save(mContext, "phone", phone);
                            SPutils.save(mContext, "password", password);
                            SPutils.save(mContext, "UserId", response.body().getData().getUserId());
                            App.setUserInfo(response.body().getData());
                            finish();
                        } else {
                            Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }


                    }

                });
                break;
            case R.id.tvForgetPwd:
                Utils.startActivity(mContext, ForgetPwdActivity.class);
                break;
            case R.id.wechat:
                //微信登录
                //测试时，需要打包签名；sample测试时，用项目里面的demokey.keystore
                //打包签名apk,然后才能产生微信的登录
                if (!isWXAppInstalledAndSupported()) {
                    Toast.makeText(LoginInActivity.this, "没安装微信客户端", Toast.LENGTH_SHORT).show();
                    return;
                }
                type = "1";
                mPlatform = ShareSDK.getPlatform(Wechat.NAME);
                authorize(mPlatform);
                break;

            case qq:
                type = "0";
                mPlatform = ShareSDK.getPlatform(QQ.NAME);
                authorize(mPlatform);
                break;

        }
    }

    private boolean isWXAppInstalledAndSupported() {
        IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
        msgApi.registerApp(Urls.APP_ID);
        return msgApi.isWXAppInstalled()
                && msgApi.isWXAppSupportAPI();
    }

    private static final int MSG_AUTH_CANCEL = 2;
    private static final int MSG_AUTH_ERROR = 3;
    private static final int MSG_AUTH_COMPLETE = 4;
    private Handler handler;
    private Platform platform;
    Intent intent = null;

    /**
     * 执行授权,获取用户信息
     *
     * @param plat
     */
    private void authorize(Platform plat) {
        if (plat == null) {
            return;
        }

        // 使用SSO授权。有客户端的都会优先启用客户端授权，没客户端的则任然使用网页版进行授权。
        plat.SSOSetting(false);
        plat.showUser(null);
        // 参数null表示获取当前授权用户资料
        plat.setPlatformActionListener(this);

    }

    @Override
    public void onComplete(Platform platform, int action, HashMap<String, Object> res) {
        if (action == Platform.ACTION_USER_INFOR) {
            Message msg = new Message();
            msg.what = MSG_AUTH_COMPLETE;
            msg.obj = new Object[]{platform.getName(), res};
            handler.sendMessage(msg);
        }
    }

    @Override
    public void onError(Platform platform, int action, Throwable t) {
        Log.i("第三方登录", "onError"+t.getMessage());
        if (action == Platform.ACTION_USER_INFOR) {
            handler.sendEmptyMessage(MSG_AUTH_ERROR);
        }
        t.printStackTrace();
    }

    @Override
    public void onCancel(Platform platform, int action) {
        Log.i("第三方登录", "onCancel");
        if (action == Platform.ACTION_USER_INFOR) {
            handler.sendEmptyMessage(MSG_AUTH_CANCEL);
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        Log.i("第三方登录", "handleMessage");
        switch (msg.what) {
            case MSG_AUTH_CANCEL: {
                if (mPlatform.isAuthValid()) {
                    mPlatform.removeAccount(true);
                }
                //取消授权
                Toast.makeText(this, "取消授权", Toast.LENGTH_SHORT).show();
            }
            break;
            case MSG_AUTH_ERROR: {
                if (mPlatform.isAuthValid()) {
                    mPlatform.removeAccount(true);
                }
                //授权失败
                Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show();
            }
            break;
            case MSG_AUTH_COMPLETE:
                Object[] objs = (Object[]) msg.obj;
                String platform = (String) objs[0];
                setPlatform(platform);
                thirdLogin(this.platform.getDb().getUserId());
                if (mPlatform.isAuthValid()) {
                    mPlatform.removeAccount(true);
                }
                break;
        }
        return false;
    }

    public void setPlatform(String platName) {
        platform = ShareSDK.getPlatform(platName);
    }


    private void thirdLogin(final String uid) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", uid);
        map.put("type", type);
        map.put("deviceType", "0");
        map.put("jpushToken", JPushInterface.getRegistrationID(mContext));
        map.put("requestCheck", EncryptUtils.encryptMD5ToString(uid, App.salt)
                .toLowerCase());
        OkGo.<NetEntity<User>>post(ThirdLogin).params(map).tag(this).execute(new DialogCallback<NetEntity<User>>(mContext) {
            @Override
            public void onSuccess(Response<NetEntity<User>> response) {
                if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                    if (TextUtils.isEmpty(response.body().getData().getUserId()) || response.body().getData().getUserId().equals("0")) {
                        MaterialDialogUtils.Warning(mContext, "提示", "您还未绑定账号,如果已有账号请选择[去绑定],若暂无账号请选择[去注册]", "去绑定", "去注册", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                MaterialDialogUtils.dialog.dismiss();
                                intent = new Intent(mContext, BindAccountActivity.class);
                                intent.putExtra("uid", uid);
                                intent.putExtra("type", type);
                                startActivityForResult(intent,1);
//                                mContext.(intent);
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                MaterialDialogUtils.dialog.dismiss();
                                intent = new Intent(mContext, RegisterActivity.class);
                                intent.putExtra("uid", uid);
                                intent.putExtra("type", type);
                                mContext.startActivity(intent);
                            }
                        });
                    } else {
                        App.setUserInfo(response.body().getData());
                        finish();
                    }
                } else {
                    Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        finish();

    }
}
