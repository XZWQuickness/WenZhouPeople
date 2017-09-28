package com.exz.wenzhoupeople.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.EncryptUtils;
import com.exz.wenzhoupeople.R;
import com.exz.wenzhoupeople.appclication.App;
import com.exz.wenzhoupeople.config.Urls;
import com.exz.wenzhoupeople.utils.SPutils;
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
import cn.com.szw.lib.myframework.view.MyWebActivity;

import static cn.com.szw.lib.myframework.view.MyWebActivity.Intent_Title;
import static cn.com.szw.lib.myframework.view.MyWebActivity.Intent_Url;
import static com.exz.wenzhoupeople.config.Urls.Logout;

/**
 * Created by weicao on 2017/8/28.
 * 设置
 */

public class SettingsActivity extends BaseActivity {
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
    @BindView(R.id.account)
    LinearLayout account;
    @BindView(R.id.about_us)
    LinearLayout aboutUs;
    @BindView(R.id.checkbox)
    CheckBox checkbox;
    @BindView(R.id.btn_exit)
    Button btnExit;

    @Override
    public boolean initToolbar() {
        mTitle.setText("设置");
        mTitle.setTextSize(18);
        toolbar.setContentInsetsAbsolute(0, 0);
        setSupportActionBar(toolbar);
        return false;
    }

    @Override
    public int setInflateId() {
        return R.layout.activity_settings;
    }


    @OnClick({R.id.mLeftImg, R.id.account, R.id.about_us, R.id.checkbox,R.id.btn_exit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mLeftImg:
                finish();
                break;
            case R.id.account:

                Utils.startActivity(mContext, AccountSafeActivity.class);
                break;
            case R.id.about_us:
                Intent intent=new Intent(mContext, MyWebActivity.class);
                intent.putExtra(Intent_Title,"关于我们");
                intent.putExtra(Intent_Url, Urls.url+"mobile/aboutus.aspx");
                startActivity(intent);
                break;
            case R.id.checkbox:
                break;
            case R.id.btn_exit:
                initPort();
                break;
        }
    }

    private void initPort() {

        Map<String, String> map = new HashMap<>();
        map.put("id", App.getLoginUserId());
        map.put("timestamp", String.valueOf(DateUtils.dateToUnixTimestamp()));
        map.put("requestCheck", EncryptUtils.encryptMD5ToString(App.getLoginUserId() +String.valueOf(DateUtils.dateToUnixTimestamp()), App.salt)
                .toLowerCase());
        OkGo.<NetEntity<Void>>post(Logout).params(map).tag(this).execute(new DialogCallback<NetEntity<Void>>(mContext) {
            @Override
            public void onSuccess(Response<NetEntity<Void>> response) {
                if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                    Utils.startActivity(mContext,MainActivity.class);
                    Utils.startActivity(mContext,LoginActivity.class);
                    SPutils.save(mContext,"password","");
                    App.getUserInfo().setUserId("");
                    finish();
                }else {
                    Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

}
