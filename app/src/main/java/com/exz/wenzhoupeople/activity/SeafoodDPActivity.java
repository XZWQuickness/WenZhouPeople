package com.exz.wenzhoupeople.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.EncryptUtils;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.exz.wenzhoupeople.R;
import com.exz.wenzhoupeople.appclication.App;
import com.exz.wenzhoupeople.config.Urls;
import com.exz.wenzhoupeople.entity.ModelImgEntity;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.szw.lib.myframework.base.BaseActivity;
import cn.com.szw.lib.myframework.config.Constants;
import cn.com.szw.lib.myframework.imageloder.GlideApp;
import cn.com.szw.lib.myframework.utils.net.NetEntity;
import cn.com.szw.lib.myframework.utils.net.callback.DialogCallback;

/**
 * Created by pc on 2017/8/23.
 * 海鲜单品
 */

public class SeafoodDPActivity extends BaseActivity {
    @BindView(R.id.mTitle)
    TextView mTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.img1)
    ImageView img1;
    @BindView(R.id.img2)
    ImageView img2;
    @Override
    public boolean initToolbar() {
        toolbar.setContentInsetsAbsolute(0, 0);
        mTitle.setTextSize(18);
        mTitle.setText("海鲜单品");
        setSupportActionBar(toolbar);
        return false;
    }

    @Override
    public int setInflateId() {
        return R.layout.activity_seafood_dp;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initSignFreshModelImg();
    }
    /*
       * 海鲜单品
       */
    public void initSignFreshModelImg() {
        Map<String, String> map = new HashMap<>();
        map.put("requestCheck", EncryptUtils.encryptMD5ToString("FreshModelImg", App.salt)
                .toLowerCase());
        OkGo.<NetEntity<ModelImgEntity>>post(Urls.SignFreshModelImg).params(map).tag(this).execute(new DialogCallback<NetEntity<ModelImgEntity>>(mContext) {
            @Override
            public void onSuccess(Response<NetEntity<ModelImgEntity>> response) {

                if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                    GlideApp.with(mContext)                             //配置上下文
                            .load(response.body().getData().getCurrent())
                            .error(R.mipmap.ic_sxhx)           //设置错误图片
                            .placeholder(R.mipmap.ic_sxhx)     //设置占位图片
                            .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存全尺寸
                            .into(img1);
                    GlideApp.with(mContext)                             //配置上下文
                            .load(response.body().getData().getSpecial())
                            .error(R.mipmap.ic_jxmw)           //设置错误图片
                            .placeholder(R.mipmap.ic_jxmw)     //设置占位图片
                            .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存全尺寸
                            .into(img2);
                }
            }

        });
    }
    @OnClick({R.id.mLeftImg, R.id.img1, R.id.img2})
    public void onViewClicked(View view) {
        Intent intent=new Intent(mContext,SeafoodListActivity.class);
        switch (view.getId()) {
            case R.id.mLeftImg:
                finish();
                break;
            case R.id.img1:

                intent.putExtra("className","时下海鲜");
                startActivity(intent);
                break;
            case R.id.img2:
                intent.putExtra("className","家乡美味");
                startActivity(intent);
                break;
        }
    }
}
