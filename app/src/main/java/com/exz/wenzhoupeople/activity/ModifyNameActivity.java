package com.exz.wenzhoupeople.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.exz.wenzhoupeople.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.szw.lib.myframework.base.BaseActivity;
import cn.com.szw.lib.myframework.view.ClearWriteEditText;

/**
 * Created by weicao on 2017/8/7.
 * 个人中心，修改昵称
 */

public class ModifyNameActivity extends BaseActivity {
    @BindView(R.id.mTitle)
    TextView mTitle;
    @BindView(R.id.modify_name)
    ClearWriteEditText modifyName;
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
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btn_sure)
    Button btnSure;

    @Override
    public boolean initToolbar() {
        mTitle.setText("修改昵称");
        mTitle.setTextSize(18);
        toolbar.setContentInsetsAbsolute(0, 0);
        setSupportActionBar(toolbar);

        return false;
    }

    @Override
    public int setInflateId() {
        return R.layout.activity_modify_name;
    }


    @Override
    public void init() throws Exception {
        super.init();

        modifyName.setText(getIntent().getStringExtra("name"));
    }

    @OnClick({R.id.mLeftImg, R.id.btn_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mLeftImg:
                finish();
                break;
            case R.id.btn_sure:
                Intent intent = new Intent();
                intent.putExtra("name", modifyName.getText().toString());
                setResult(1, intent);
                finish();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
