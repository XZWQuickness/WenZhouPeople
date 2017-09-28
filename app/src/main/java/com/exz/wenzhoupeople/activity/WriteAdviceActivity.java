package com.exz.wenzhoupeople.activity;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import static com.exz.wenzhoupeople.config.Urls.Complain;

/**
 * Created by weicao on 2017/8/31.
 * 填写建议
 */

public class WriteAdviceActivity extends BaseActivity {
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
    @BindView(R.id.content)
    EditText content;

    @Override
    public boolean initToolbar() {
        mTitle.setText("投诉建议");
        mTitle.setTextSize(18);
        toolbar.setContentInsetsAbsolute(0, 0);
        setSupportActionBar(toolbar);
        return false;
    }

    @Override
    public int setInflateId() {
        return R.layout.activity_write_advice;
    }


    @OnClick({R.id.mLeftImg, R.id.commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mLeftImg:
                finish();
                break;
            case R.id.commit:
                initAccount();

                break;
        }
    }

    @Override
    public void init() throws Exception {
        super.init();
    }

    //投诉建议
    private void initAccount() {
        if (!content.getText().toString().equals("")){
            Map<String, String> map = new HashMap<>();
            map.put("id", App.getLoginUserId());
            map.put("content", content.getText().toString());
            map.put("timestamp", String.valueOf(DateUtils.dateToUnixTimestamp()));
            map.put("requestCheck", EncryptUtils.encryptMD5ToString(App.getLoginUserId() + String.valueOf(DateUtils.dateToUnixTimestamp()), App.salt)
                    .toLowerCase());
            OkGo.<NetEntity<Void>>post(Complain).params(map).tag(this).execute(new DialogCallback<NetEntity<Void>>(mContext) {
                @Override
                public void onSuccess(Response<NetEntity<Void>> response) {
                    if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                        Toast.makeText(mContext, "提交成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }


                }

            });
        }else {
            Toast.makeText(mContext, "请填写内容", Toast.LENGTH_SHORT).show();
        }

    }


}
