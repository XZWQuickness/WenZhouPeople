package com.exz.wenzhoupeople.adapter;

import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.exz.wenzhoupeople.R;
import com.exz.wenzhoupeople.entity.SeafoodListEntity;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.szw.lib.myframework.imageloder.GlideApp;

/**
 * Created by pc on 2017/8/24.
 */

public class SeafoodPZBAdapter extends BaseQuickAdapter<SeafoodListEntity, BaseViewHolder> {
    @BindView(R.id.imgUrl)
    ImageView imgUrl;
    @BindView(R.id.title)
    TextView title;
//    @BindView(R.id.subTitle)
//    TextView subTitle;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.oldPrice)
    TextView oldPrice;

    public SeafoodPZBAdapter() {
        super(R.layout.adapter_seafood_pzb, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, SeafoodListEntity item) {
        View itemView = helper.itemView;
        ButterKnife.bind(this, itemView);
        View textView = LayoutInflater.from(mContext).inflate(R.layout.tv_layout, null);
        GlideApp.with(mContext)                             //配置上下文
                .load(item.getImgUrl())
                .error(R.mipmap.icon_empty)           //设置错误图片
                .placeholder(R.mipmap.icon_empty)     //设置占位图片
                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存全尺寸
                .into(imgUrl);
        try {
            if(!TextUtils.isEmpty(item.getTitle()))
                title.setText(URLDecoder.decode(item.getTitle(),"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
//        subTitle.setText(item.getSubTitle());
        price.setText("￥"+item.getPrice());
        oldPrice.setText("￥"+item.getOldPrice());
        oldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //下划线
        oldPrice.getPaint().setAntiAlias(true);//抗锯齿

    }
}
