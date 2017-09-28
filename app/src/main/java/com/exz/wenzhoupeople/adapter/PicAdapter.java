package com.exz.wenzhoupeople.adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.ScreenUtils;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.exz.wenzhoupeople.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.szw.lib.myframework.imageloder.GlideApp;


/**
 * Created by weicao on 2017/3/24.
 */

public class PicAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    @BindView(R.id.photo)
    ImageView photo;
    @BindView(R.id.layout)
    LinearLayout layout;

    public PicAdapter() {
        super(R.layout.item_pic, new ArrayList<String>());
    }


    @Override
    protected void convert(BaseViewHolder helper, final String item) {
        ButterKnife.bind(this, helper.itemView);

        ViewGroup.LayoutParams layoutParams = photo.getLayoutParams();
        layoutParams.width= (int) (ScreenUtils.getScreenWidth()*0.3);
        layoutParams.height= (int) (ScreenUtils.getScreenWidth()*0.3);
        photo.setLayoutParams(layoutParams);

        GlideApp.with(mContext)                             //配置上下文
                .load( item)
                .error(R.mipmap.icon_load_default)           //设置错误图片
                .placeholder(R.mipmap.icon_load_default)     //设置占位图片
                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存全尺寸
                .into(photo);

        if(TextUtils.isEmpty(item)){
            photo.setVisibility(View.GONE);
        }else{
            photo.setVisibility(View.VISIBLE);
        }
    }
}
