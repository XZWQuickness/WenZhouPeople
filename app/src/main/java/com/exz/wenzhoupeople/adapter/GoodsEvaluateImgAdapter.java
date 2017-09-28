package com.exz.wenzhoupeople.adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.blankj.utilcode.util.ScreenUtils;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.exz.wenzhoupeople.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.szw.lib.myframework.imageloder.GlideApp;

/**
 * Created by pc on 2017/8/28.
 */

public class GoodsEvaluateImgAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    @BindView(R.id.headerImg)
    ImageView headerImg;

    public GoodsEvaluateImgAdapter() {
        super(R.layout.adapter_goods_evaluate_img, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        View itemView = helper.itemView;
        ButterKnife.bind(this, itemView);
        ViewGroup.LayoutParams layoutParams = headerImg.getLayoutParams();
        layoutParams.width= (int) (ScreenUtils.getScreenWidth()*0.3);
        layoutParams.height= (int) (ScreenUtils.getScreenWidth()*0.3);
        headerImg.setLayoutParams(layoutParams);

        GlideApp.with(mContext)                             //配置上下文
                .load( item)
                .error(R.mipmap.icon_load_default)           //设置错误图片
                .placeholder(R.mipmap.icon_load_default)     //设置占位图片
                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存全尺寸
                .into(headerImg);
        if(TextUtils.isEmpty(item)){
            headerImg.setVisibility(View.GONE);
        }else{
            headerImg.setVisibility(View.VISIBLE);
        }
    }

}
