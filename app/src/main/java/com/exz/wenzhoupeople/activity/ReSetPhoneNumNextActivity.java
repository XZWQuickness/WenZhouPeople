package com.exz.wenzhoupeople.activity;

import android.os.CountDownTimer;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.exz.wenzhoupeople.R;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.szw.lib.myframework.base.BaseActivity;

/**
 * Created by weicao on 2017/8/28.
 */

public class ReSetPhoneNumNextActivity extends BaseActivity {
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
    @BindView(R.id.btn_code)
    Button btnCode;
    @BindView(R.id.sure)
    Button sure;
    private TimeCount time;


    @Override
    public boolean initToolbar() {
        mTitle.setText("修改手机号码");
        mTitle.setTextSize(18);
        toolbar.setContentInsetsAbsolute(0, 0);
        setSupportActionBar(toolbar);
        return false;
    }

    @Override
    public int setInflateId() {
        return R.layout.activity_reset_phone_num_next;
    }

    @Override
    public void init() throws Exception {
        super.init();
        time = new TimeCount(60000, 1000);//构造CountDownTimer对象

    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            btnCode.setText("重新获取");
            btnCode.setBackground(getResources().getDrawable(R.drawable.settings_blue_ground));
            btnCode.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            btnCode.setText(String.format("重发(%s)", millisUntilFinished / 1000));
            btnCode.setClickable(false);
            btnCode.setBackground(getResources().getDrawable(R.drawable.shape_gray));

        }
    }

    @OnClick({R.id.mLeftImg, R.id.btn_code,R.id.sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mLeftImg:
                finish();
                break;
            case R.id.btn_code:
                time.start();
                break;
            case R.id.sure:
                break;
        }
    }
}
