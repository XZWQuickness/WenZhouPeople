package com.exz.wenzhoupeople.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.xlhratingbar_lib.XLHRatingBar;
import com.exz.wenzhoupeople.R;
import com.exz.wenzhoupeople.entity.EvaluateModel;
import com.facebook.drawee.view.SimpleDraweeView;

import java.net.URLDecoder;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by weicao on 2017/2/10.
 */

public class EvaluateAdapter extends BaseQuickAdapter<EvaluateModel, BaseViewHolder> {


    public EvaluateAdapter() {
        super(R.layout.item_evaluate, new ArrayList<EvaluateModel>());
    }

    @Override
    protected void convert(final BaseViewHolder helper, final EvaluateModel item) {
        View convertView = helper.itemView;
        ButterKnife.bind(this, convertView);
        ViewHolder v = new ViewHolder(convertView);
        v.pic.setImageURI(item.getImgUrl());
        v.title.setText(item.getTitle());
        String s = URLDecoder.decode(item.getContent());
        v.evaluate.setText(s);
        v.ratingBar.setCountNum(Integer.parseInt(item.getScore()));
        v.dataTime.setText(item.getDate());

        PicAdapter adapter = new PicAdapter();

        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 3);
        v.recyclerView.setLayoutManager(layoutManager);
        adapter.setNewData(item.getImages());
        v.recyclerView.setAdapter(adapter);
    }



    class ViewHolder {
        @BindView(R.id.pic)
        SimpleDraweeView pic;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.ratingBar)
        XLHRatingBar ratingBar;
        @BindView(R.id.evaluate)
        TextView evaluate;
        @BindView(R.id.dataTime)
        TextView dataTime;
        @BindView(R.id.recyclerView)
        RecyclerView recyclerView;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
