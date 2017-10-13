package com.exz.wenzhoupeople.adapter;

import android.content.Intent;
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
import cn.com.szw.lib.myframework.utils.preview.PreviewActivity;

import static cn.com.szw.lib.myframework.utils.preview.PreviewActivity.PREVIEW_INTENT_IMAGES;
import static cn.com.szw.lib.myframework.utils.preview.PreviewActivity.PREVIEW_INTENT_IS_CAN_DELETE;
import static cn.com.szw.lib.myframework.utils.preview.PreviewActivity.PREVIEW_INTENT_POSITION;
import static cn.com.szw.lib.myframework.utils.preview.PreviewActivity.PREVIEW_INTENT_SHOW_NUM;


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
    protected void convert(final BaseViewHolder helper, final String item) {
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
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> list = (ArrayList<String>) getData();
                Intent intent = new Intent(mContext, PreviewActivity.class);
                intent.putExtra(PREVIEW_INTENT_POSITION, helper.getAdapterPosition());
                intent.putExtra(PREVIEW_INTENT_SHOW_NUM, true);
                intent.putExtra(PREVIEW_INTENT_IMAGES, list);
                intent.putExtra(PREVIEW_INTENT_IS_CAN_DELETE, false);
                mContext.startActivity(intent);
            }
        });
    }
}
