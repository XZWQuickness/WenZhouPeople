package com.exz.wenzhoupeople.adapter;


import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.exz.wenzhoupeople.R;
import com.exz.wenzhoupeople.activity.GoodsDetailActivity;
import com.exz.wenzhoupeople.entity.BannersBean;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;

import java.util.List;

import cn.com.szw.lib.myframework.imageloder.GlideApp;
import cn.com.szw.lib.myframework.view.MyWebActivity;

import static cn.com.szw.lib.myframework.view.MyWebActivity.Intent_Url;

public class BannerAdapter extends StaticPagerAdapter {

    private Context ctx;
    private List<BannersBean> list;

    public BannerAdapter(Context ctx, List<BannersBean> list) {
        this.ctx = ctx;
        this.list = list;
    }

    @Override
    public View getView(ViewGroup container, final int position) {
        View inflate = View.inflate(ctx, R.layout.banner_imgview, null);
        ImageView imageView = (ImageView) inflate.findViewById(R.id.img);
        //加载图片
        GlideApp.with(ctx)                             //配置上下文
//                            .load(Uri.fromFile(new File(path)))      //设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
                .load(list.get(position).getImgUrl())
                .error(R.mipmap.icon_empty)           //设置错误图片
                .placeholder(R.mipmap.icon_empty)     //设置占位图片
                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存全尺寸
                .into(imageView);
        //点击事件
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                if (list.get(position).getMark().equals("1")) {//商品
                    intent = new Intent(ctx, GoodsDetailActivity.class);
                    intent.putExtra("goodsId",list.get(position).getId());
                    ctx.startActivity(intent);
                } else {//广告
                    if(TextUtils.isEmpty(list.get(position).getAdUrl())){
                        return;
                    }
                    intent = new Intent(ctx, MyWebActivity.class);
                    intent.putExtra(Intent_Url,list.get(position).getAdUrl());
                    ctx.startActivity(intent);
                }

            }
        });
        return inflate;
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
