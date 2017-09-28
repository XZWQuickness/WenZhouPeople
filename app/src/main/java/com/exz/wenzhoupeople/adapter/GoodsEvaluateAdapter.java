package com.exz.wenzhoupeople.adapter;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.xlhratingbar_lib.XLHRatingBar;
import com.exz.wenzhoupeople.R;
import com.exz.wenzhoupeople.entity.GoodsEvaluateEntity;
import com.exz.wenzhoupeople.entity.JudgeListBean;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.szw.lib.myframework.imageloder.GlideApp;
import cn.com.szw.lib.myframework.imageloder.GlideImageLoader;
import cn.com.szw.lib.myframework.utils.preview.PreviewActivity;

import static cn.com.szw.lib.myframework.utils.preview.PreviewActivity.PREVIEW_INTENT_IMAGES;
import static cn.com.szw.lib.myframework.utils.preview.PreviewActivity.PREVIEW_INTENT_IS_CAN_DELETE;
import static cn.com.szw.lib.myframework.utils.preview.PreviewActivity.PREVIEW_INTENT_POSITION;

/**
 * Created by pc on 2017/8/28.
 */

public class GoodsEvaluateAdapter extends BaseQuickAdapter<JudgeListBean, BaseViewHolder> {
    List<GoodsEvaluateEntity> list = new ArrayList<>();
    @BindView(R.id.headerImg)
    ImageView headerImg;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.ratingBar)
    XLHRatingBar ratingBar;
    @BindView(R.id.date)
    TextView date;
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    public GoodsEvaluateAdapter() {
        super(R.layout.adapter_goods_evaluate, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, final JudgeListBean item) {
        View itemView = helper.itemView;
        ButterKnife.bind(this, itemView);
        ViewGroup.LayoutParams layoutParams = headerImg.getLayoutParams();
        layoutParams.width= (int) (ScreenUtils.getScreenWidth()*0.16);
        layoutParams.height= (int) (ScreenUtils.getScreenWidth()*0.16);
        headerImg.setLayoutParams(layoutParams);
        GlideImageLoader.glideDisplayImage(mContext, item.getHeader(), R.mipmap.icon_load_default, headerImg);
        GlideApp.with(mContext)                             //配置上下文
                .load( item.getHeader())
                .error(R.mipmap.icon_load_default)           //设置错误图片
                .placeholder(R.mipmap.icon_load_default)     //设置占位图片
                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存全尺寸
                .into(headerImg);
        title.setText(item.getName());
        ratingBar.setCountSelected(Integer.parseInt(item.getStar()));
        date.setText(item.getDate());
        try {
            content.setText(URLDecoder.decode(item.getContent(),"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        GoodsEvaluateImgAdapter adapter = new GoodsEvaluateImgAdapter();
        adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        adapter.setNewData(item.getImages());
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addOnItemTouchListener(new com.chad.library.adapter.base.listener.OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                ArrayList<String> list= (ArrayList<String>) item.getImages();
                Intent intent=new Intent(mContext, PreviewActivity.class);
                intent.putExtra(PREVIEW_INTENT_POSITION, position);
                intent.putExtra(PREVIEW_INTENT_IMAGES, list);
                intent.putExtra(PREVIEW_INTENT_IS_CAN_DELETE, false);
                mContext.startActivity(intent);
            }
        });

    }

}
