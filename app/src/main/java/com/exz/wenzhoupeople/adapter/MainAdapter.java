package com.exz.wenzhoupeople.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.blankj.utilcode.util.ScreenUtils;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.exz.wenzhoupeople.R;
import com.exz.wenzhoupeople.entity.ImgEntity;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.szw.lib.myframework.imageloder.GlideApp;

/**
 * Created by pc on 2017/8/21.
 */

public class MainAdapter extends BaseQuickAdapter<ImgEntity, BaseViewHolder> {

    @BindView(R.id.img)
    ImageView img;

    public MainAdapter() {
        super(R.layout.adapter_main, null);
    }


    @Override
    protected void convert(BaseViewHolder helper, ImgEntity item) {
        View itemView = helper.itemView;
        ButterKnife.bind(this, itemView);
        ViewGroup.LayoutParams layoutParams = img.getLayoutParams();
        layoutParams.width= ScreenUtils.getScreenWidth();
        layoutParams.height= (int) (ScreenUtils.getScreenWidth()*0.4);
        img.setLayoutParams(layoutParams);
        GlideApp.with(mContext)                             //配置上下文
                .load(item.getUrl())
                .error(item.getError())           //设置错误图片
                .placeholder(item.getError())     //设置占位图片
                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存全尺寸
                .into(img);

    }
}
