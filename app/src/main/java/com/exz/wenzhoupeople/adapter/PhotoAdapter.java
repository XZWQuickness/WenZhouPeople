package com.exz.wenzhoupeople.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.ScreenUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.exz.wenzhoupeople.R;
import com.exz.wenzhoupeople.entity.PhotoEntity;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.szw.lib.myframework.imageloder.GlideApp;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;


public class PhotoAdapter extends BaseQuickAdapter<PhotoEntity, BaseViewHolder> {
    @BindView(R.id.ivPhoto)
    ImageView ivPhoto;
    @BindView(R.id.delete)
    ImageView delete;

    public PhotoAdapter() {
        super(R.layout.adapter_photo, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, PhotoEntity item) {
        View convertView = helper.itemView;
        ButterKnife.bind(this, convertView);

        ViewGroup.LayoutParams layoutParams = ivPhoto.getLayoutParams();
        layoutParams.width= (int) (ScreenUtils.getScreenWidth()*0.2);
        layoutParams.height= (int) (ScreenUtils.getScreenWidth()*0.2);
        ivPhoto.setLayoutParams(layoutParams);
        RelativeLayout.LayoutParams l=new RelativeLayout.LayoutParams(WRAP_CONTENT,WRAP_CONTENT);
        if(getData().indexOf(item)>4){
            ivPhoto.setVisibility(View.GONE);
        }else{
            ivPhoto.setVisibility(View.VISIBLE);
        }
        switch (item.getType()) {
            case "1":
                ivPhoto.setImageResource((Integer) item.getPhotoImg());
                delete.setVisibility(View.GONE);

                break;
            case "2":
                GlideApp.with(mContext).load(new File((String) item.getPhotoImg())).error(R.mipmap.icon_empty).into(ivPhoto);
                delete.setVisibility(View.VISIBLE);
                l.setMargins((int) ((ScreenUtils.getScreenWidth()*0.2)-10),20,0,0);
                delete.setLayoutParams(l);
                break;
            case "3":
                delete.setVisibility(View.VISIBLE);
                l.setMargins((int) ((ScreenUtils.getScreenWidth()*0.2)-10),20,0,0);
                delete.setLayoutParams(l);
                GlideApp.with(mContext).load(item.getPhotoImg()).error(R.mipmap.icon_empty).into(ivPhoto);
                break;
        }
    }


}
