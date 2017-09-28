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
import cn.com.szw.lib.myframework.utils.net.NetEntity;
import cn.com.szw.lib.myframework.utils.net.callback.DialogCallback;
import cn.com.szw.lib.myframework.view.ClearWriteEditText;

import static com.exz.wenzhoupeople.config.Urls.ResetPwd;

/**
 * Created by weicao on 2017/8/28.
 * 修改密码
 */

public class ReSetPwdActivity extends BaseActivity {
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
    @BindView(R.id.pwd)
    ClearWriteEditText pwd;
    @BindView(R.id.new_pwd)
    ClearWriteEditText newPwd;
    @BindView(R.id.new_pwd_again)
    ClearWriteEditText newPwdAgain;
    @BindView(R.id.sure)
    Button sure;

    @Override
    public boolean initToolbar() {
        mTitle.setText("修改密码");
        mTitle.setTextSize(18);
        toolbar.setContentInsetsAbsolute(0, 0);
        setSupportActionBar(toolbar);
        return false;
    }

    @Override
    public int setInflateId() {
        return R.layout.action_reset_pwd;
    }


    @OnClick({R.id.mLeftImg, R.id.sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mLeftImg:
                finish();
                break;
            case R.id.sure:
                initPort();
                break;
        }
    }


    private void initPort() {

        if (!newPwd.getText().toString().equals(newPwdAgain.getText().toString())) {
            Toast.makeText(mContext, "两次输入密码不一致", Toast.LENGTH_SHORT).show();
        } else {
            Map<String, String> map = new HashMap<>();
            map.put("id", App.getLoginUserId());
            map.put("oldPwd", pwd.getText().toString());
            map.put("newPwd", newPwd.getText().toString());
            map.put("requestCheck", EncryptUtils.encryptMD5ToString(App.getLoginUserId() + pwd.getText().toString(), App.salt)
                    .toLowerCase());
            OkGo.<NetEntity<Void>>post(ResetPwd).params(map).tag(this).execute(new DialogCallback<NetEntity<Void>>(mContext) {
                @Override
                public void onSuccess(Response<NetEntity<Void>> response) {
                    if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                        Toast.makeText(mContext, "密码修改成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

            });
        }


    }
}
