package com.exz.wenzhoupeople.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.blankj.utilcode.util.EncryptUtils;
import com.exz.wenzhoupeople.R;
import com.exz.wenzhoupeople.appclication.App;
import com.exz.wenzhoupeople.entity.User;
import com.exz.wenzhoupeople.utils.SPutils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.HashMap;
import java.util.Map;

import cn.com.szw.lib.myframework.base.BaseActivity;
import cn.com.szw.lib.myframework.config.Constants;
import cn.com.szw.lib.myframework.utils.Utils;
import cn.com.szw.lib.myframework.utils.net.NetEntity;
import cn.com.szw.lib.myframework.utils.net.callback.DialogCallback;
import cn.jpush.android.api.JPushInterface;

import static com.exz.wenzhoupeople.config.Urls.Login;


public class LogoActivity extends BaseActivity {

    ImageView img_logo;
    String type = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        img_logo = (ImageView) this.findViewById(R.id.img_logo);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.logo_fade_in);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                if (!TextUtils.isEmpty(SPutils.getString(mContext, "phone"))&&!TextUtils.isEmpty(SPutils.getString(mContext, "password"))) {
                    Map<String, String> map = new HashMap<>();
                    map.put("phone", SPutils.getString(mContext, "phone"));
                    map.put("pwd", SPutils.getString(mContext, "password"));
                    map.put("deviceType", "0");
                    map.put("jpushToken", JPushInterface.getRegistrationID(mContext));
                    map.put("requestCheck", EncryptUtils.encryptMD5ToString(SPutils.getString(mContext, "phone") +SPutils.getString(mContext, "password"), App.salt)
                            .toLowerCase());
                    OkGo.<NetEntity<User>>post(Login).params(map).tag(this).execute(new DialogCallback<NetEntity<User>>(mContext) {
                        @Override
                        public void onSuccess(Response<NetEntity<User>> response) {
                            if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                                SPutils.save(mContext, "phone", SPutils.getString(mContext, "phone"));
                                SPutils.save(mContext, "password", SPutils.getString(mContext, "password"));
                                SPutils.save(mContext, "UserId", response.body().getData().getUserId());
                                App.setUserInfo(response.body().getData());
                                Utils.startActivity(mContext,MainActivity.class);
                            } else {
                                jump();
                            }


                        }

                        @Override
                        public void onError(Response<NetEntity<User>> response) {
                            super.onError(response);
                            jump();
                        }
                    });
                }else{

                    jump();
                }


            }
        });
        img_logo.setAnimation(anim);

    }

    @Override
    public boolean initToolbar() {

        return false;
    }

    @Override
    public int setInflateId() {
        return R.layout.activity_logo;
    }


    private void jump() {
        Intent intent = new Intent(LogoActivity.this, MainActivity.class);
        startActivity(intent);
        LogoActivity.this.finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
    }

}

